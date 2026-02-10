import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        List<String> builtIn = List.of("echo", "exit", "type");

        while (true){
            System.out.print("$ ");
            String command = scanner.nextLine();

            if(command.equals("exit")){
                System.exit(0);
            } else if(command.startsWith("echo")){
                System.out.println(command.substring(5));
            } else if(command.startsWith("type")){
                String argument = command.substring(5);
                String output = builtIn.contains(argument) ? argument + " is a shell builtin" : argument + ": not found";
                System.out.println(output);
            }
            else {
                System.out.println(command + ": command not found");
            }
        }
    }
}
