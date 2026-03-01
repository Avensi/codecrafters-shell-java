package completion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

public class Completer {

    public Completer(){
    }

    public Set<String> getMatches(String prefix){
        Set<String> matches = new TreeSet<>();
        String pathEnv = System.getenv("PATH");
        String[] directories = pathEnv.split(":");
        List<String> builtIns = List.of("echo", "exit");

        for (String directory: directories){
            Path directoryPath = Paths.get(directory);
            try (Stream<Path> paths = Files.list(directoryPath)){
                paths.filter(filePath -> filePath.getFileName().toString().startsWith(prefix))
                        .filter(Files::isExecutable)
                        .map(Path::getFileName)
                        .forEach(filename -> {
                            matches.add(filename.toString());
                        });
            } catch (IOException _) {}
        }

        for (String builtIn: builtIns){
            if(builtIn.startsWith(prefix)){
                matches.add(builtIn);
            }
        }
        return matches;
    }
}
