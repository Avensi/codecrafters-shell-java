import commands.Command;
import commands.CommandDispatcher;
import commands.CommandParser;
import completion.CompletionWidget;
import completion.Completer;
import models.ShellState;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class Main {
    public static void main(String[] args) throws Exception {
        ShellState shellState = new ShellState();
        CommandDispatcher commandDispatcher = new CommandDispatcher(new Command(shellState), new CommandParser(), System.out, System.err, shellState);

        Terminal terminal = TerminalBuilder.builder().system(true).build();

        DefaultParser parser = new DefaultParser();
        parser.setEscapeChars(null);

        Completer completer = new Completer();

        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .parser(parser)
                .option(LineReader.Option.AUTO_MENU, false)
                .option(LineReader.Option.AUTO_LIST, true)
                .option(LineReader.Option.INSERT_TAB, false)
                .build();

        CompletionWidget.create(lineReader, completer);

        while (true){
            String input = lineReader.readLine("$ ");
            if (input.isEmpty()) {
                continue;
            }
            commandDispatcher.dispatch(input);
        }
    }
}
