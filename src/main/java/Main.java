import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        List<String> builtIn = List.of("echo", "exit", "type");
        Command commandService = new Command();

        while (true){
            System.out.print("$ ");
            String input = scanner.nextLine().trim();
            String[] tokens = input.split("\\s+");
            String commandName = tokens[0];

            if (input.isEmpty()) {
                continue;
            }

            switch (commandName) {
                case "exit" -> System.exit(0);
                case "echo" -> commandService.echo(input);
                case "type" -> commandService.type(input, builtIn);
                default -> commandService.execute(input);
            }
        }
    }
}
