import java.lang.module.FindException;
import java.util.*;

class Solution {
    public int minimumLengthEncoding(String[] words) {
        Arrays.sort(words, new Comparator<String>() {

            @Override
            public int compare(String s1, String s2) {
                if (s1.length() > s2.length()) return -1;
                if (s2.length() > s1.length()) return 1;
                return 0;
            }
        });
        Trie trie = new Trie();
        int sLength, totalSum = 0;
        for (String s : words) {
            sLength = s.length() + 1;
            if (trie.getNodeInReverse(s) == null) {
                totalSum += sLength;
                trie.insertInReverse(s);
            }
        }
        return totalSum;
    }


    class Trie {
        private class Node {
            private Node[] children;
            private boolean isWord;
            private char c;

            Node(char c) {
                children = new Node[26];
                isWord = false;
                this.c = c;
            }

            Node[] getChildren() {
                return children;
            }

            boolean isWord() {
                return isWord;
            }

            void setIsWord(boolean isWord) {
                this.isWord = isWord;
            }
        }

        private Node root;

        Trie() {
            root = new Node('\0');
        }

        void insert(String word) {
            Node curr = root;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (curr.children[c - 'a'] == null) curr.children[c - 'a'] = new Node(c);
                curr = curr.children[c - 'a'];
            }
            curr.isWord = true;
        }

        void insertInReverse(String word) {
            Node curr = root;
            for (int i = word.length() - 1; i >= 0; i--) {
                char c = word.charAt(i);
                if (curr.children[c - 'a'] == null) curr.children[c - 'a'] = new Node(c);
                curr = curr.children[c - 'a'];
            }
            curr.isWord = true;
        }

        Node getNodeInReverse(String word) {
            Node curr = root;
            for (int i = word.length() - 1; i >= 0; i--) {
                char c = word.charAt(i);
                if (curr.children[c - 'a'] == null) return null;
                curr = curr.children[c - 'a'];
            }
            return curr;
        }

        boolean search(String word) {
            Node node = getNode(word);
            return node != null && node.isWord;
        }

        boolean startsWith(String prefix) {
            return getNode(prefix) != null;
        }


        Node getNode(String word) {
            Node curr = root;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (curr.children[c - 'a'] == null) return null;
                curr = curr.children[c - 'a'];
            }
            return curr;
        }
    }


    public static void main(String[] args) {
        // if it matches from the end of both string and trie, do not count the matches. Only start counting if and when the first
        // mismatch occurs
        Solution solution = new Solution();
        String[] words = {"time", "me", "bell"};
        System.out.printf(" Expected answer is 10, we get: %d\n", solution.minimumLengthEncoding(words));
        words = new String[]{"me", "time"};
        System.out.printf(" Expected Answer is 5, we get: %d\n", solution.minimumLengthEncoding(words));
        words = new String[]{"t"};
        System.out.printf(" Expected answer is 2, but we get: %d\n", solution.minimumLengthEncoding(words));
        words = new String[]{"feipyxx", "e"};
        System.out.printf("Expected answer is 10, but we get: %d\n", solution.minimumLengthEncoding(words));
        words = new String[]{"ctxdic", "c"};
        System.out.printf("Expected answer is 7, but we get: %d\n", solution.minimumLengthEncoding(words));
    }
}