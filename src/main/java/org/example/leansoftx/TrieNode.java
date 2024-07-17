package org.example.leansoftx;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    public Map<Character, TrieNode> children;
    public boolean isEndOfWord;
    public char value = ' ';

    public TrieNode() {
        this.children = new HashMap<>();
        this.isEndOfWord = false;
    }

    public TrieNode(char value) {
        this.children = new HashMap<>();
        this.isEndOfWord = false;
        this.value = value;
    }

    public boolean hasChild(char c) {
        return children.containsKey(c);
    }
}
