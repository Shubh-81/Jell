package DataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TrieNode {
    private boolean isLeaf;
    private HashMap<Character, TrieNode> children;

    public HashMap<Character, TrieNode> getChildren() {
        return children;
    }

    public void setChildren(HashMap<Character, TrieNode> children) {
        this.children = children;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    TrieNode(boolean isLeaf) {
        this.isLeaf = isLeaf;
        children = new HashMap<>();
    }

    public String toString() {
        String res = "\n\risLeaf: " + isLeaf +" \n\r";
        for (var ch: children.keySet()) {
            res += "ch: " + ch;
            res += children.get(ch).toString() + "\n\r";
        }

        return res;
    }
}



