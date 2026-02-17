import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class CommandDispatcher {

    private final Command commandService;
    private final CommandParser commandParser;

    public CommandDispatcher(Command commandService, CommandParser commandParser){
        this.commandService = commandService;
        this.commandParser = commandParser;
    }

    public void dispatch(String input) {
        String fileName = null;
        PrintStream originalOut = System.out;

        List<String> tokens = commandParser.parseCommand(input);
        String commandName = tokens.getFirst();

        int redirectIndex = commandParser.getRedirectIndex(tokens);

        try {
            if (redirectIndex != -1){
                fileName = tokens.get(redirectIndex + 1);
                tokens = new ArrayList<>(tokens.subList(0, redirectIndex));;
                FileOutputStream outputFile = new FileOutputStream(fileName);
                System.setOut(new PrintStream(outputFile));
            }

            switch (commandName) {
                case "exit" -> System.exit(0);
                case "echo" -> commandService.echo(tokens);
                case "type" -> commandService.type(tokens);
                case "pwd" -> commandService.pwd();
                case "cd" -> commandService.cd(tokens);
                default -> commandService.execute(tokens, fileName);
            }
        } catch (FileNotFoundException e) {
            System.err.println("No such file or directory");
        } finally {
            System.setOut(originalOut);
        }

    }
}
