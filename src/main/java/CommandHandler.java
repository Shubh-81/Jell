import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

public class CommandHandler {
    private final HashMap<String, Consumer<String[]>> commands = new HashMap<>();

    public CommandHandler() {
        commands.put("echo", this::handleEcho);
        commands.put("exit", this::handleExit);
    }

    private void handleEcho(String[] args) {
        if (args.length < 2) {
            throw new RuntimeException("Invalid command, expected: echo <args>");
        }

        for (int i = 1; i < args.length; i++) {
            System.out.print(args[i] + " ");
        }

        System.out.println();
    }

    private void handleExit(String[] args) {
        System.exit(0);
    }

    private void handleDefault(String[] args) {
        System.out.println(args[0] + ": command not found");
    }

    public void handleCommand(String input) {
        String[] args = input.trim().split(" ");
        commands.getOrDefault(args[0], this::handleDefault).accept(args);
    }
}
