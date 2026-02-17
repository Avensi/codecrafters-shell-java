import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Command commandService = new Command();
        CommandParser commandParser = new CommandParser();
        PrintStream originalOut = System.out;

        while (true){
            System.out.print("$ ");
            String input = scanner.nextLine().trim();
            String fileName = null;

            if (input.isEmpty()) {
                continue;
            }
            List<String> tokens = commandParser.parseCommand(input);
            String commandName = tokens.getFirst();
            int redirectIndex = commandParser.getRedirectIndex(tokens);

            if (redirectIndex != -1){
                fileName = tokens.get(redirectIndex + 1);
                tokens = tokens.subList(0, redirectIndex);
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
            System.setOut(originalOut);

        }
    }
}
