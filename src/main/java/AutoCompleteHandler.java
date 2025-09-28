import java.util.ArrayList;
import java.util.Set;

public class AutoCompleteHandler {

    private final Set<String> commands;
    private final String[] builtinCommands = {"echo", "exit"};

    AutoCompleteHandler(Set<String> executables) {
        for (String command: builtinCommands) {
            executables.add(command);
        }
        commands = executables;
    }

    public String autoComplete(String input) {
        String match = "";
        for (String command: commands) {
            if (command.startsWith(input)) {
                if (match.length() != 0) {
                    System.out.print('\007');
                    return "";
                }
                match = command.substring(input.length());
            }
        }

        if (match.length() == 0) {
            System.out.print('\007');
        }

        return match;
    }
}
