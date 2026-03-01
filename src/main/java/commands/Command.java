package commands;

import models.ShellState;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Command {
    private static final List<String> BUILTINS = List.of("echo", "exit", "type", "pwd", "cd");
    private final ShellState shellState;

    public Command(ShellState shellState){
        this.shellState = shellState;
    }

    public void echo(List<String> tokens) {
        System.out.println(String.join(" ", tokens.subList(1, tokens.size())));
    }

    public void type(List<String> tokens){
        if (tokens.size() < 2) return;
        String argument = tokens.get(1);
        if (BUILTINS.contains(argument)){
            System.out.println(argument + " is a shell builtin");
        }
        else {
            String pathEnv = System.getenv("PATH");
            String[] directories = pathEnv.split(":");

            for (String directory: directories){
                Path filePath = Paths.get(directory, argument);
                if (Files.exists(filePath) && Files.isExecutable(filePath)) {
                    System.out.println(argument + " is " + filePath);
                    return;
                }
            }
            System.err.println(argument + ": not found");
        }
    }

    public void pwd(){
        System.out.println(shellState.getCurrentDir());
    }

    public void cd(List<String> tokens){
        if (tokens.size() < 2) return;

        String targetDir = tokens.get(1);

        if (targetDir.startsWith("~")){
            targetDir = targetDir.replaceFirst("^~", System.getenv("HOME"));
        }

        Path targetPath = shellState.getCurrentDir().resolve(Paths.get(targetDir));

        if (Files.exists(targetPath) && Files.isDirectory(targetPath)){
            shellState.setCurrentDir(targetPath.normalize());
        } else {
            System.err.println("cd: " + targetDir + ": No such file or directory");
        }
    }
}
