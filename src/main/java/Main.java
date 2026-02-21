import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class Main {
    public static void main(String[] args) throws Exception {
        CommandDispatcher commandDispatcher = new CommandDispatcher(new Command(), new CommandParser(), System.out, System.err);
        Terminal terminal = TerminalBuilder.builder().streams(System.in, System.out).build();

        DefaultParser parser = new DefaultParser();
        parser.setEscapeChars(null);

        Completer stringsCompleter = new StringsCompleter("echo", "exit");

        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .parser(parser)
                .completer(stringsCompleter)
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
