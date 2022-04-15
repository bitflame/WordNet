import java.lang.module.FindException;
import java.util.*;
// in this problem order matters. That is why simply checking an array for previous strings does not work. I need Trie
class Solution {
    public int minimumLengthEncoding(String[] words) {
        Arrays.sort(words,new Comparator<String>(){

            @Override
            public int compare(String o1, String o2) {
                if (o1.length()> o2.length())return -1;
                if (o2.length()>o1.length()) return 1;
                else return 0;
            }
        });
        int[] letters = new int[26];
        int currentSum = 0, totalSum = 0;
        boolean matched = true;
        for (String s : words) {
            for (int i = s.length() - 1; i >= 0; i--) {
                currentSum++;
                char c = s.charAt(i);
                if (letters[c - 'a'] == 0) {
                    letters[c - 'a']++;
                    matched = false;
                }
            }
            if (!matched) totalSum += currentSum + 1;
            currentSum = 0;
            matched = true;
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