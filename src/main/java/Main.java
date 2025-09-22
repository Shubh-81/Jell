import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception {

        String path = System.getenv("PATH");
        String homePath = System.getenv("HOME");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        CommandHandler commandHandler = new CommandHandler(path, homePath);
        while (true) {
            try {
                System.out.print("$ ");
                String input = br.readLine();

                commandHandler.handleCommand(input);
            } catch (RuntimeException e) {
                System.out.println("Faced runtime exception " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Faced exception " + e.getMessage());
            }
        }
    }
}
