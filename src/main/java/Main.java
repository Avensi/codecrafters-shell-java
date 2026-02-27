import jline.DoubleTabWidget;
import jline.ExecutableCompleter;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class Main {
    public static void main(String[] args) throws Exception {
        CommandDispatcher commandDispatcher = new CommandDispatcher(new Command(), new CommandParser(), System.out, System.err);

        Terminal terminal = TerminalBuilder.builder().system(true).build();

        DefaultParser parser = new DefaultParser();
        parser.setEscapeChars(null);

        ExecutableCompleter executableCompleter = new ExecutableCompleter();

        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .parser(parser)
                .option(LineReader.Option.AUTO_MENU, false)
                .option(LineReader.Option.AUTO_LIST, true)
                .option(LineReader.Option.INSERT_TAB, false)
                .build();

        DoubleTabWidget.create(lineReader, executableCompleter);

        while (true){
            String input = lineReader.readLine("$ ");
            if (input.isEmpty()) {
                continue;
            }
            commandDispatcher.dispatch(input);
        }
    }
}
