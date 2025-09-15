import java.util.HashMap;
import java.util.function.Consumer;

public class CommandHandler {
    private final HashMap<String, Consumer<String[]>> commands = new HashMap<>();

    public CommandHandler() {
        commands.put("echo", this::handleEcho);
        commands.put("exit", this::handleExit);
    }

    private void handleEcho(String[] args) {

    }

    private void handleExit(String[] args) {
        System.out.println(args);
        System.exit(0);
    }

    private void handleDefault(String[] args) {
        System.out.println();
    }

    public void handleCommand(String input) {
        String[] args = input.trim().split(" ");
        commands.getOrDefault(args[0], this::handleDefault).accept(args);
    }
}
