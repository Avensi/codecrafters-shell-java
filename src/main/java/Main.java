import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Command commandService = new Command();

        while (true){
            System.out.print("$ ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }
            List<String> tokens = parseCommand(input);
            String commandName = tokens.getFirst();

            switch (commandName) {
                case "exit" -> System.exit(0);
                case "echo" -> commandService.echo(tokens);
                case "type" -> commandService.type(tokens);
                case "pwd" -> commandService.pwd();
                case "cd" -> commandService.cd(tokens);
                default -> commandService.execute(tokens);
            }
        }
    }

    private static List<String> parseCommand(String input){
        List<String> result = new ArrayList<>();
        boolean inSingleQuote = false;
        StringBuilder currentWord = new StringBuilder();
        for (char c: input.toCharArray()){
            if (c == '\''){
                inSingleQuote = !inSingleQuote;
            }
            else if (c == ' ' && !inSingleQuote){
                if (!currentWord.isEmpty()){
                    result.add(currentWord.toString());
                    currentWord.setLength(0);
                }
            } else {
                currentWord.append(c);
            }
        }
        if(!currentWord.isEmpty()){
            result.add(currentWord.toString());
        }
        return result;
    }
}
