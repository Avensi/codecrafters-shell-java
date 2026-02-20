import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class RedirectionHandler implements AutoCloseable{
    private final PrintStream originalOut;
    private final PrintStream originalErr;

    public RedirectionHandler(PrintStream originalOut, PrintStream originalErr){
        this.originalOut = originalOut;
        this.originalErr = originalErr;
    }

    public void setup(String redirectionOperator, String fileName) throws FileNotFoundException {
        if (fileName != null){
            FileOutputStream outputFile = new FileOutputStream(fileName);
            FileOutputStream outputAppendFile = new FileOutputStream(fileName, true);
            switch (redirectionOperator){
                case ">", "1>" -> System.setOut(new PrintStream(outputFile));
                case "1>>", ">>" -> System.setOut(new PrintStream(outputAppendFile));
                case "2>" -> System.setErr(new PrintStream(outputFile));
            }
        }
    }

    public void configureProcess(ProcessBuilder processBuilder, String redirectionOperator, String fileName) {
        if (fileName != null){
            switch (redirectionOperator){
                case ">", "1>" -> processBuilder.redirectOutput(new File(fileName));
                case "1>>", ">>" -> processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(new File(fileName)));
                case "2>" -> processBuilder.redirectError(new File(fileName));
            }
        }
    }

    public void restore() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Override
    public void close() {
        restore();
    }
}
