import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Command {
    private Path currentDir = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
    private static final List<String> BUILTINS = List.of("echo", "exit", "type", "pwd", "cd");

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

    public void execute(List<String> tokens, ProcessBuilder pb){
        try {
            pb.directory(currentDir.toFile());
            Process process = pb.start();
            process.waitFor();
        } catch (IOException e) {
            System.err.println(tokens.getFirst() + ": command not found");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void pwd(){
        System.out.println(this.currentDir);
    }

    public void cd(List<String> tokens){
        if (tokens.size() < 2) return;

        String targetDir = tokens.get(1);

        if (targetDir.startsWith("~")){
            targetDir = targetDir.replaceFirst("^~", System.getenv("HOME"));
        }

        Path targetPath = currentDir.resolve(Paths.get(targetDir));

        if (Files.exists(targetPath) && Files.isDirectory(targetPath)){
            currentDir = targetPath.normalize();
        } else {
            System.err.println("cd: " + targetDir + ": No such file or directory");
        }
    }
}
