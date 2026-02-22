import DataStructures.Trie;
import com.google.inject.Inject;
import utils.ExecutableProvider;
import utils.SystemProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

import static utils.Constants.BELL_CHAR;
import static utils.Constants.BLANK_SPACE;
import static utils.Constants.BUILTIN_COMMANDS;
import static utils.Constants.NEW_LINE;

public class AutoCompleteHandler implements BaseAutoCompleteHandler {

    private boolean firstTab;
    private final Trie trie;

    @Inject
    AutoCompleteHandler(ExecutableProvider executableProvider) {
        this.firstTab = true;

        // Gets executables names from system properties
        Set<String> executables = new HashSet<>(executableProvider.getExecutables().keySet());
        // Adds builtin commands to executables
        executables.addAll(BUILTIN_COMMANDS);

        // Build trie for efficient prefix matching
        trie = new Trie(new ArrayList<>(executables));
    }

    private void handleOutput(ArrayList<String> matches, String input) {

        // Incase of first tab, just ring the bell
        if (firstTab) {
            System.out.print(BELL_CHAR);
            firstTab = false;
            return;
        }

        // Print possible matches in new line
        System.out.print(NEW_LINE);
        for (String match: matches) {
            System.out.print(input + match + "  ");
        }

        // Throw user back to same point
        System.out.print(NEW_LINE + "$ " + input);
    }

    public String autoComplete(String input) {
        // Get extension beyond current command
        String match = trie.match(input);
        // Get all matches
        ArrayList<String> matches = trie.getMatches();
        // For a single match simply return the match
        if (match != null && !match.isEmpty()) {
            // Add blank space as only one command is possible, user will continue ahead
            if (matches.size() == 1) {
                match += BLANK_SPACE;
            }
            return match;
        }

        if (matches != null) {
            Collections.sort(matches);
        }
        handleOutput(matches, input);
        return "";
    }

}
