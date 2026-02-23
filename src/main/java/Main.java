import jline.ExecutableCompleter;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import javax.sound.sampled.Line;

public class Main {
    public static void main(String[] args) throws Exception {
        CommandDispatcher commandDispatcher = new CommandDispatcher(new Command(), new CommandParser(), System.out, System.err);

        Terminal terminal = TerminalBuilder.builder().system(true).build();

        DefaultParser parser = new DefaultParser();
        parser.setEscapeChars(null);

        Completer aggregateCompleter = new AggregateCompleter(
                new StringsCompleter("echo", "exit"),
                new ExecutableCompleter());

        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .parser(parser)
                .completer(aggregateCompleter)
                // Disable AUTO_MENU to prevent showing the list on the 1st press
                .option(LineReader.Option.AUTO_MENU, false)
                .option(LineReader.Option.INSERT_TAB, false)
                .build();

        lineReader.setVariable(LineReader.BELL_STYLE, "audible");
        lineReader.setVariable(LineReader.AMBIGUOUS_BINDING, true);

        while (true){
            String input = lineReader.readLine("$ ");
            if (input.isEmpty()) {
                continue;
            }
            commandDispatcher.dispatch(input);
        }
    }
}
