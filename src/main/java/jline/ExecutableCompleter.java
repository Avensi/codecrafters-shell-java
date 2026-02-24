package jline;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.utils.AttributedString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

public class ExecutableCompleter implements Completer {

    @Override
    public void complete(LineReader reader, ParsedLine commandLine, List<Candidate> candidates) {
        assert commandLine != null;
        assert candidates != null;

        String prefix = commandLine.word();
        Set<String> matches = getMatches(prefix);
        for (String name: matches){
            candidates.add(new Candidate(AttributedString.stripAnsi(name), name, null, null, null, null, true));
        }
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
