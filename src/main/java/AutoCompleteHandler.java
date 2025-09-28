import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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

    private String handleMultipleMatches(String input, Set<String> matches) {
        String prefix = null;
        for (String match: matches) {
            if (prefix == null) {
                prefix = match;
                continue;
            }
            int l = Math.min(prefix.length(), match.length());
            for (int i = 0; i <= l; i++) {
                if (i == l || prefix.charAt(i) != match.charAt(i)) {
                    if (i == 0) {
                        prefix = "";
                        break;
                    }
                    prefix = prefix.substring(0, i);
                    break;
                }
            }
        }

        if (prefix != null && prefix.length() > 0) {
            return prefix;
        }

        if (firstTab) {
            System.out.print('\007');
            firstTab = !firstTab;
            return "";
        }

        System.out.print("\n\r");
        for (String match: matches) {
            System.out.print(input + match + "  ");
        }

        System.out.print("\n\r$ " + input);
        return "";
    }

    public String autoComplete(String input) {
        String match = "";
        Set<String> matches = new TreeSet<>();


        for (String command: commands) {
            if (command.startsWith(input)) {
                match = command.substring(input.length());
                matches.add(match);
            }
        }

        if (matches.size() != 1) {
            return handleMultipleMatches(input, matches);
        }

        return match + " ";
    }
}
