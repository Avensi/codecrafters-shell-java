import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        List<String> builtIn = List.of("echo", "exit", "type");
        Command commandService = new Command();

        while (true){
            System.out.print("$ ");
            String input = scanner.nextLine();

            if(input.equals("exit")){
                System.exit(0);
            } else if(input.startsWith("echo")){
                commandService.echo(input);
            } else if(input.startsWith("type")){
                commandService.type(input, builtIn);
            }
            else {
                commandService.execute(input);
            }
        }
    }
}
