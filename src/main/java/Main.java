import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "stty raw -echo < /dev/tty"});

        String path = System.getenv("PATH");
        String homePath = System.getenv("HOME");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        AutoCompleteHandler autoCompleteHandler = new AutoCompleteHandler();
        CommandHandler commandHandler = new CommandHandler(path, homePath);

        try {
            while (true) {
                try {
                    System.out.print("$ ");
    
                    String input = "";
                    int ch;
                    while ((ch = br.read()) != -1) {
                        char c = (char)ch;
                        if (c == '\t') {
                            String completion = autoCompleteHandler.autoComplete(input);
                            if (completion.length() > 0)    completion += " ";
                            input += completion;
                            System.out.print(completion);
                        } else if (c == '\n' || c == '\r') {
                            System.out.print("\r\n");
                            commandHandler.handleCommand(input);
                            break;
                        } else if (c == '\b' || (int) c == 127) {
                            if (!input.isEmpty()) {
                                input = input.substring(0, input.length() - 1);
                                System.out.print("\b \b");
                            }
                        } else {
                            System.out.print(c);
                            input += c;
                        }
                    }
                } catch (RuntimeException e) {
                    System.out.println("Faced runtime exception " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Faced exception " + e.getMessage());
                }
            }
        } finally {
            Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "stty cooked echo < /dev/tty"});
        }
    }
}
