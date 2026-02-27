package jline;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

public class ExecutableCompleter{

    public ExecutableCompleter(){

    }

    public Set<String> getMatches(String prefix){
        Set<String> matches = new TreeSet<>();
        String pathEnv = System.getenv("PATH");
        String[] directories = pathEnv.split(":");

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
        return matches;
    }
}
