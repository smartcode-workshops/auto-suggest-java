package org.example.leansoftx;

import java.util.*;

public class Trie {
    private TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    // search a word in the trie
    public boolean search(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            if (!current.hasChild(c)) {
                return false;
            }
            current = current.children.get(c);
        }
        return current.isEndOfWord;
    }

    public boolean insert(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            if (!current.hasChild(c)) {
                current.children.put(c, new TrieNode(c));
            }
            current = current.children.get(c);
        }
        if (current.isEndOfWord) {
            return false;
        }
        current.isEndOfWord = true;
        return true;
    }

    public List<String> autoSuggest(String prefix) {
        TrieNode currentNode = root;
        for (char c : prefix.toCharArray()) {
            if (!currentNode.hasChild(c)) {
                return new ArrayList<>();
            }
            currentNode = currentNode.children.get(c);
        }
        return getAllWordsWithPrefix(currentNode, prefix);
    }

    public List<String> getAllWordsWithPrefix(TrieNode node, String prefix) {
        List<String> words = new ArrayList<>();
        if (node.isEndOfWord) {
            words.add(prefix);
        }

        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            words.addAll(getAllWordsWithPrefix(entry.getValue(), prefix + entry.getKey()));
        }

        return words;
    }

    public List<String> getAllWords() {
        return getAllWordsWithPrefix(root, "");
    }

    public void printTrieStructure() {
        System.out.println("\nroot");
        _printTrieNodes(root, " ", true);
    }

    private void _printTrieNodes(TrieNode root, String format, boolean isLastChild) {
        if (root == null) {
            return;
        }

        System.out.print(format);

        if (isLastChild) {
            System.out.print("└─");
            format += "  ";
        } else {
            System.out.print("├─");
            format += "│ ";
        }

        System.out.println(root.value);

        List<Map.Entry<Character, TrieNode>> children = new ArrayList<>(root.children.entrySet());
        children.sort(Map.Entry.comparingByKey());

        for (int i = 0; i < children.size(); i++) {
            boolean isLast = i == children.size() - 1;
            _printTrieNodes(children.get(i).getValue(), format, isLast);
        }
    }

    public List<String> getSpellingSuggestions(String word) {
        char firstLetter = word.charAt(0);
        List<String> suggestions = new ArrayList<>();
        List<String> words = getAllWordsWithPrefix(root.children.get(firstLetter), String.valueOf(firstLetter));

        for (String w : words) {
            int distance = levenshteinDistance(word, w);
            if (distance <= 2) {
                suggestions.add(w);
            }
        }

        return suggestions;
    }

    public static int levenshteinDistance(String s, String t) {
        int m = s.length();
        int n = t.length();
        int[][] d = new int[m][n];

        if (m == 0) {
            return n;
        }

        if (n == 0) {
            return m;
        }

        for (int i = 0; i <= m; i++) {
            d[i][0] = i;
        }

        for (int j = 0; j <= n; j++) {
            d[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = (s.charAt(i - 1) == t.charAt(j - 1)) ? 0 : 1;
                d[i][j] = Math.min(Math.min(d[i][j] + 1, d[i][j] + 1), d[i][j] + cost);
            }
        }

        return d[m][n];
    }


}
