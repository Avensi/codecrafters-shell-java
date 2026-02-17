import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        CommandDispatcher commandDispatcher = new CommandDispatcher(new Command(), new CommandParser());

        while (true){
            System.out.print("$ ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                continue;
            }
            commandDispatcher.dispatch(input);
        }
    }
}
