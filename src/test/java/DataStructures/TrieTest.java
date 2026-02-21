package DataStructures;

import java.util.ArrayList;
import java.util.Comparator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrieTest {
    @Test
    void testTrieConstruction() {
        ArrayList<String> inputs = new ArrayList<>();
        inputs.add("and");
        inputs.add("ant");
        inputs.add("dad");
        inputs.add("do");

        Trie trie = new Trie(inputs);
        String prefix = "a";

        String extendedPrefix = trie.match(prefix);
        assertEquals("n", extendedPrefix);

        ArrayList<String> matches = trie.getMatches();

        ArrayList<String> expectedMatches = new ArrayList<>();
        expectedMatches.add("nd");
        expectedMatches.add("nt");
        assertEquals(matches.size(), 2);
        matches.sort(Comparator.naturalOrder());

        int i = 0;
        for (String match: matches) {
            assertEquals(expectedMatches.get(i), match);
            i++;
        }
    }
}
