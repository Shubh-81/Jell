package DataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;

@Getter
public class Trie {

    private final TrieNode rootNode;
    private ArrayList<String> matches;

    public ArrayList<String> getMatches() {
        return matches;
    }

    void buildTrie(ArrayList<String> inputStrings) {
        for (String currString: inputStrings) {
            TrieNode curr = rootNode;
            for (char c: currString.toCharArray()) {
                HashMap<Character, TrieNode> childMap = curr.getChildren();
                if (childMap.containsKey(c)) {
                    curr = childMap.get(c);
                    continue;
                }

                TrieNode newNode = new TrieNode(false);
                childMap.putIfAbsent(c, newNode);
                curr.setChildren(childMap);
                curr = newNode;
            }

            curr.setLeaf(true);
        }
    }

    public Trie(ArrayList<String> inputStrings) {
        rootNode = new TrieNode(false);
        buildTrie(inputStrings);
    }

    public void getAllMatches(String currPrefix, TrieNode currNode) {
        // If a match is complete, add it to matches
        if (currNode.isLeaf()) {
            matches.add(currPrefix);
        }

        // Extend for all children of current node
        for (char child: currNode.getChildren().keySet()) {
            getAllMatches(currPrefix + child, currNode.getChildren().get(child));
        }
    }

    public String match(String prefix) {
        TrieNode curr = rootNode;

        // First match entire prefix (user's input to available matches)
        for (char c: prefix.toCharArray()) {
            if (!curr.getChildren().containsKey(c)) {
                return "";
            }

            curr = curr.getChildren().get(c);
        }

        // Extend prefix by matching till only one choice is available for extending
        String extendedPrefix = "";
        while (curr.getChildren().size() == 1 && curr.isLeaf() == false) {
            char next = curr.getChildren().keySet().iterator().next();
            extendedPrefix += next;

            curr = curr.getChildren().get(next);
        }

        // Get list of all possible matches
        matches = new ArrayList<>();
        getAllMatches(extendedPrefix, curr);

        return extendedPrefix;
    }

}
