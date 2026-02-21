import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class Main {
    public static void main(String[] args) throws Exception {
        CommandDispatcher commandDispatcher = new CommandDispatcher(new Command(), new CommandParser(), System.out, System.err);
        Terminal terminal = TerminalBuilder.builder().streams(System.in, System.out).build();

        DefaultParser parser = new DefaultParser();
        parser.setEscapeChars(null);

        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .parser(parser)
                .build();

        while (true){
            System.out.print("$ ");
            String input = lineReader.readLine();
            if (input.isEmpty()) {
                continue;
            }
            commandDispatcher.dispatch(input);
        }
    }
}
