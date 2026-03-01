package commands;

import handlers.RedirectionHandler;
import models.PipelineSegment;
import models.ShellState;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.channels.Pipe;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CommandDispatcher {

    private final Command commandService;
    private final CommandParser commandParser;
    private final PrintStream originalOut;
    private final PrintStream originalErr;
    private final ShellState shellState;

    public CommandDispatcher(Command commandService, CommandParser commandParser, PrintStream originalOut, PrintStream originalErr, ShellState shellState){
        this.commandService = commandService;
        this.commandParser = commandParser;
        this.originalOut = originalOut;
        this.originalErr = originalErr;
        this.shellState = shellState;
    }

    public void dispatch(String input){

        List<String> tokens = commandParser.parseCommand(input);
        List<PipelineSegment> segments = commandParser.parseSegments(tokens);

        String commandName = tokens.getFirst();
        try (RedirectionHandler redirectionHandler = new RedirectionHandler(originalOut,originalErr)) {
            if (segments.size() == 1 && segments.getFirst().redirectOp() != null && segments.getFirst().fileName() != null){
                redirectionHandler.redirectJvmStreams(segments.getFirst().redirectOp(), segments.getFirst().fileName());
            }
            tokens = segments.getFirst().tokens();
            switch (commandName) {
                case "exit" -> System.exit(0);
                case "echo" -> commandService.echo(tokens);
                case "type" -> commandService.type(tokens);
                case "pwd" -> commandService.pwd();
                case "cd" -> commandService.cd(tokens);
                default -> processSegments(segments);
            }

        } catch (FileNotFoundException e) {
            System.err.println("No such file or directory");
        }

    }

    public void processSegments(List<PipelineSegment> segments){
        List<ProcessBuilder> processBuilders = new ArrayList<>();

        try (RedirectionHandler redirectionHandler = new RedirectionHandler(originalOut,originalErr)) {
            for (PipelineSegment segment: segments){
                String commandName = segment.tokens().getFirst();
                if (!executableExists(commandName)) {
                    System.err.println(commandName + ": command not found");
                    return;
                }

                ProcessBuilder processBuilder = new ProcessBuilder(segment.tokens());
                boolean isSegmentLast = segment == segments.getLast();
                processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
                if (segment.redirectOp() != null && segment.fileName() != null && isSegmentLast){
                    redirectionHandler.redirectOsProcess(processBuilder, segment.redirectOp(), segment.fileName());
                }
                processBuilder.directory(shellState.getCurrentDir().toFile());
                processBuilders.add(processBuilder);
            }
            List<Process> pipeline = ProcessBuilder.startPipeline(processBuilders);

            for (Process process: pipeline){
                process.waitFor();
            }

        } catch (FileNotFoundException e) {
            System.err.println("No such file or directory");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean executableExists(String commandName) {
        String pathEnv = System.getenv("PATH");
        if (pathEnv == null) return false;
        for (String dir : pathEnv.split(":")) {
            Path filePath = Paths.get(dir, commandName);
            if (Files.exists(filePath) && Files.isExecutable(filePath)) return true;
        }
        return false;
    }
}
