import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Command {
    public void echo(String input) {
        System.out.println(input.substring(5));
    }

    public void type(String input, List<String> builtIn){
        String argument = input.substring(5);
        String output = argument + ": not found";
        if (builtIn.contains(argument)){
            output = argument + " is a shell builtin";
        }
        else {
            String pathEnv = System.getenv("PATH");
            String[] directories = pathEnv.split(":");
            StringBuilder pathToTry = new StringBuilder();
            for (String directory: directories){
                pathToTry.append(directory);
            }
            Path filePath = Paths.get(pathToTry.toString(), argument);
            if (Files.exists(filePath) && Files.isExecutable(filePath)) {
                output = argument + "is " + filePath;
            }
        }
        System.out.println(output);

    }

}
