import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class CommandDispatcher {

    private final Command commandService;
    private final CommandParser commandParser;
    private final PrintStream originalOut;
    private final PrintStream originalErr;

    public CommandDispatcher(Command commandService, CommandParser commandParser, PrintStream originalOut, PrintStream originalErr){
        this.commandService = commandService;
        this.commandParser = commandParser;
        this.originalOut = originalOut;
        this.originalErr = originalErr;
    }

    public void dispatch(String input){
        String fileName = null;
        String redirectionOperator = null;

        List<String> tokens = commandParser.parseCommand(input);
        String commandName = tokens.getFirst();
        int redirectIndex = commandParser.getRedirectIndex(tokens);

        if (redirectIndex != -1){
            fileName = tokens.get(redirectIndex + 1);
            redirectionOperator = tokens.get(redirectIndex);
            tokens = new ArrayList<>(tokens.subList(0, redirectIndex));
        }

        ProcessBuilder processBuilder = new ProcessBuilder(tokens);
        processBuilder.inheritIO();

        try (RedirectionHandler redirectionHandler = new RedirectionHandler(originalOut,originalErr)) {
            if (redirectionOperator != null && fileName != null){
                redirectionHandler.redirectJvmStreams(redirectionOperator, fileName);
                redirectionHandler.redirectOsProcess(processBuilder, redirectionOperator, fileName);
            }

            switch (commandName) {
                case "exit" -> System.exit(0);
                case "echo" -> commandService.echo(tokens);
                case "type" -> commandService.type(tokens);
                case "pwd" -> commandService.pwd();
                case "cd" -> commandService.cd(tokens);
                default -> commandService.execute(tokens, processBuilder);
            }
        } catch (FileNotFoundException e) {
            System.err.println("No such file or directory");
        }

    }
}
