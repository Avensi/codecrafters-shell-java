import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.utils.AttributedString;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class ExecutableCompleter implements Completer {

    @Override
    public void complete(LineReader reader, ParsedLine commandLine, List<Candidate> candidates) {
        assert commandLine != null;
        assert candidates != null;

        String pathEnv = System.getenv("PATH");
        String[] directories = pathEnv.split(":");

        for (String directory: directories){
            Path directoryPath = Paths.get(directory);
            try (Stream<Path> paths = Files.list(directoryPath)){
                paths.filter(Files::isExecutable)
                        .map(Path::getFileName)
                        .forEach(filename -> candidates.add(new Candidate(AttributedString.stripAnsi(filename.getFileName().toString()), filename.toFile().getName(), null, null, null, null, true)));
            } catch (IOException e) {
                System.err.println("Could not read directory: " + e.getMessage());
            }
        }
    }
}
