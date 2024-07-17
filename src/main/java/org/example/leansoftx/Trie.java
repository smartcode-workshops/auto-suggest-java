package org.example.leansoftx;

import java.util.*;

public class Trie {
    private TrieNode root;

    public Trie() {
        this.root = new TrieNode();
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
        return null;
    }

    // 从前缀树中删除一个单词
    public boolean delete(String word) {
        return _delete(root, word, 0);
    }

    // 生成 Helper 方法 _delete 使用递归的方式将一个单词的所有结点从 trie 中逐个删除
    private boolean _delete(TrieNode node, String word, int index) {
        // 如果当前节点是一个单词的结尾，就将这个节点标记为非单词结尾
        if (index == word.length()) {
            if (!node.isEndOfWord) {
                return false;
            }
            node.isEndOfWord = false;
            // 如果当前节点没有子节点，就可以将这个节点从前缀树中删除
            return node.children.isEmpty();
        }
        // 如果当前节点不是一个单词的结尾，就继续遍历前缀树
        char c = word.charAt(index);
        TrieNode child = node.children.get(c);
        if (child == null) {
            return false;
        }
        // 如果当前节点的子节点中有要删除的字符，就继续遍历
        boolean shouldDeleteCurrentNode = _delete(child, word, index + 1);
        // 如果 shouldDeleteCurrentNode 为 true，就将当前节点从前缀树中删除
        if (shouldDeleteCurrentNode) {
            node.children.remove(c);
            // 如果当前节点是一个单词的结尾，就返回 false，表示不能删除当前节点
            return !node.isEndOfWord && node.children.isEmpty();
        }
        return false;
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
