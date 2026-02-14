import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Command {
    private static final List<String> BUILTINS = List.of("echo", "exit", "type", "pwd", "cd");

    public void echo(String input) {
        System.out.println(input.substring(5));
    }

    public void type(String input){
        String argument = input.substring(5);
        String output = argument + ": not found";
        if (BUILTINS.contains(argument)){
            output = argument + " is a shell builtin";
        }
        else {
            String pathEnv = System.getenv("PATH");
            String[] directories = pathEnv.split(":");

            for (String directory: directories){
                Path filePath = Paths.get(directory, argument);
                if (Files.exists(filePath) && Files.isExecutable(filePath)) {
                    output = argument + " is " + filePath;
                    break;
                }
            }
        }
        System.out.println(output);
    }

    public void execute(String input){
        String[] commandArgs = input.split("\\s+");
        try {
            ProcessBuilder pb = new ProcessBuilder(commandArgs);
            pb.inheritIO();
            Process process = pb.start();
            process.waitFor();
        } catch (IOException e) {
            System.out.println(commandArgs[0] + ": command not found");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void pwd(){
        System.out.println(System.getProperty("user.dir"));
    }

    public void cd(String input){
        String[] commandArgs = input.split("\\s+");
        Path path = Paths.get(commandArgs[1]);
        if (Files.exists(path)){
            System.setProperty("user.dir", path.toString());
        } else {
            System.out.println("cd: " + input + ": No such file or directory");
        }
    }
}
