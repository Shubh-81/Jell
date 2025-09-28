import java.util.ArrayList;
import java.util.Set;

public class AutoCompleteHandler {

    private final Set<String> commands;
    private final String[] builtinCommands = {"echo", "exit"};
    private boolean firstTab;

    AutoCompleteHandler(Set<String> executables) {
        this.firstTab = true;
        for (String command: builtinCommands) {
            executables.add(command);
        }
        commands = executables;
    }

    private void handleMultipleMatches(String input, ArrayList<String> matches) {
        if (firstTab) {
            System.out.print('\007');
            firstTab = !firstTab;
            return;
        }

        System.out.print("\n\r");
        for (String match: matches) {
            System.out.print(input + match + "  ");
        }

        System.out.print("\n\r$ " + input);
    }

    public String autoComplete(String input) {
        String match = "";

        ArrayList<String> matches = new ArrayList<>();

        for (String command: commands) {
            if (command.startsWith(input)) {
                match = command.substring(input.length());
                matches.add(match);
            }
        }

        if (matches.size() != 1) {
            handleMultipleMatches(input, matches);
            return "";
        }

        return match;
    }
}
