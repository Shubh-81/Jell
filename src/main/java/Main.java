import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        CommandHandler commandHandler = new CommandHandler();
        while (true) {
            System.out.print("$ ");
            String input = br.readLine();

            commandHandler.handleCommand(input);
        }
    }
}
