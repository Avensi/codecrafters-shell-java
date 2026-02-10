import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        while (true){
            System.out.print("$ ");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            if(command.equals("exit")){
                break;
            } else if(command.startsWith("echo")){
                System.out.println(command.replace("echo", ""));
            } else {
                System.out.println(command + ": command not found");
            }
        }
    }
}
