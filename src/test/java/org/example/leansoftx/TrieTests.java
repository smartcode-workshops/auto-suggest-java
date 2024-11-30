package org.example.leansoftx;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TrieTests {

    @Test
    public void testInsertAndSearch() {
        Trie trie = new Trie();
        assertTrue(trie.insert("hello"));
        assertTrue(trie.search("hello"));
        assertFalse(trie.search("hell"));
        assertFalse(trie.search("helloo"));
        assertFalse(trie.search("world"));
    }

    @Test
    public void testInsertDuplicate() {
        Trie trie = new Trie();
        assertTrue(trie.insert("hello"));
        assertFalse(trie.insert("hello"));
    }

    @Test
    public void testSearchEmptyTrie() {
        Trie trie = new Trie();
        assertFalse(trie.search("hello"));
    }

    @Test
    public void testSearchPrefix() {
        Trie trie = new Trie();
        trie.insert("hello");
        assertFalse(trie.search("hell"));
    }

    @Test
    public void testSearchWordWithCommonPrefix() {
        Trie trie = new Trie();
        trie.insert("hello");
        trie.insert("hell");
        assertTrue(trie.search("hello"));
        assertTrue(trie.search("hell"));
    }
}
