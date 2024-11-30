package org.example.leansoftx;


import java.util.List;
import java.util.Scanner;

public class Main {

    public static String[] words = {
            "as", "astronaut", "asteroid", "are", "around",
            "cat", "cars", "cares", "careful", "carefully",
            "for", "follows", "forgot", "from", "front",
            "mellow", "mean", "money", "monday", "monster",
            "place", "plan", "planet", "planets", "plans",
            "the", "their", "they", "there", "towards"
    };

    public static Trie dictionary = initializeTrie(words);

    public static void main(String[] args) {

        dictionary.printTrieStructure();
        searchWord();
        //prefixAutoComplete();
        //deleteWord();
        //getSpellingSuggestions();

    }

    public static Trie initializeTrie(String[] words) {
        Trie trie = new Trie();
        for (String word : words) {
            trie.insert(word);
        }
        return trie;
    }

    public static void searchWord() {
        printTrie(dictionary);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter a word to search for, or press Enter to exit.");
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                break;
            }
            if (dictionary.search(input)) {
                System.out.println("Found \"" + input + "\" in dictionary");
            } else {
                System.out.println("Did not find \"" + input + "\" in dictionary");
            }
        }
        scanner.close();
    }

    public static void prefixAutoComplete(){
        printTrie(dictionary);
        getPrefixInput();
    }

    public static void deleteWord() {
        printTrie(dictionary);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nEnter a word to delete, or press Enter to exit.");
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                break;
            }
            /*
            if (dictionary.search(input)) {
                dictionary.delete(input);
                System.out.println("Deleted \"" + input + "\" from dictionary\n");
                printTrie(dictionary);
            }
            */
            else {
                System.out.println("Did not find \"" + input + "\" in dictionary");
            }
        }
        scanner.close();
    }

    public static void getSpellingSuggestions() {
        printTrie(dictionary);
        System.out.println("\nEnter a word to get spelling suggestions for, or press Enter to exit.");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (!input.isEmpty()) {
            List<String> similarWords = dictionary.getSpellingSuggestions(input);
            System.out.println("Spelling suggestions for \"" + input + "\":");
            if (similarWords.isEmpty()) {
                System.out.println("No suggestions found.");
            } else {
                for (String word : similarWords) {
                    System.out.println(word);
                }
            }
        }
    }

    public static void getPrefixInput() {
        System.out.println("\nEnter a prefix to search for, then press Tab to cycle through search results. Press Enter to exit.");

        boolean running = true;
        String prefix = "";
        StringBuilder sb = new StringBuilder();
        List<String> words = null;
        int wordsIndex = 0;

        Scanner scanner = new Scanner(System.in);

        try{
            while (running) {
                if (System.in.available() > 0) {
                    char input = (char) System.in.read();

                    // if the input is spacebar
                    if (input == ' ' && sb.length() > 0) {
                        System.out.print(' ');
                        prefix = "";
                        sb.append(' ');
                        continue;
                    } else if (input == '\b' && sb.length() > 0) {
                        System.out.print("\b \b");
                        sb.deleteCharAt(sb.length() - 1);
                        prefix = sb.toString().split(" ")[sb.toString().split(" ").length - 1];
                    } else if (input == '\n') {
                        System.out.println();
                        running = false;
                        continue;
                    } else if (input == '\t' && prefix.length() > 1) {
                        String previousWord = sb.toString().split(" ")[sb.toString().split(" ").length - 1];

                        if (words != null) {
                            if (!previousWord.equals(words.get(wordsIndex - 1))) {
                                words = dictionary.autoSuggest(prefix);
                                wordsIndex = 0;
                            }
                        } else {
                            words = dictionary.autoSuggest(prefix);
                            wordsIndex = 0;
                        }

                        for (int i = prefix.length(); i < previousWord.length(); i++) {
                            System.out.print("\b \b");
                            sb.deleteCharAt(sb.length() - 1);
                        }

                        if (words.size() > 0 && wordsIndex < words.size()) {
                            String output = words.get(wordsIndex++);
                            System.out.print(output.substring(prefix.length()));
                            sb.append(output.substring(prefix.length()));
                        }
                        continue;
                    } else if (input != '\t') {
                        System.out.print(input);
                        prefix += input;
                        sb.append(input);
                        words = null;
                        wordsIndex = 0;
                    }
                }
            }
        }catch(Exception e){
            // print exception details
            System.out.println(e.toString());
        }

        scanner.close();
    }

    public static void printTrie(Trie trie) {
        System.out.println("The dictionary contains the following words:");
        List<String> words = trie.getAllWords();
        for (String word : words) {
            System.out.print(word + ", ");
        }
        System.out.println();
    }
}
