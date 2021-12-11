import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordNet {
    // this is a hashmap of nouns and ids
    HashMap<String, Integer> db = new HashMap<>();
    String[] synsets;
    int size = 0;  // number of synsets

    boolean hasCycle = false;
    boolean rooted = true;
    Digraph digraph;
    // SAP sap; use this when you are ready to improve the performance and other unit tests pass

    // constructor takes the name of two input files
    public WordNet(String synsets, String hypernyms) {
        createDb(synsets);
        createGraph(hypernyms);
    }

    private void createDb(String synsets) {
        In in = new In(synsets);
        int val;
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(",");
            val = Integer.parseInt(a[0]);
            String[] syns = a[1].split(" ");

            for (String noun : syns) {


            }
            size++;
        }
    }

    private void createGraph(String hypernyms) {
        In in = new In(hypernyms);
        synsets = new String[db.size()];
        synsets = db.keySet().toArray(new String[0]);// Intellij suggested everything after equals! So glad I am using it
        digraph = new Digraph(size);
        int index = 0;
        while (in.hasNextLine()) {
            index++;
            String[] a = in.readLine().split(",");
            for (int i = 0; i < a.length - 1; i++) {
                digraph.addEdge(Integer.parseInt(a[0]), Integer.parseInt(a[i + 1]));
            }
        }// check for cycles
        DirectedCycle cycleFinder = new DirectedCycle(digraph);
        if (cycleFinder.hasCycle()) {
            hasCycle = true;
            throw new IllegalArgumentException("The input to the constructor does not correspond to a rooted DAG - cycle detected");
        }
        if (Math.abs(index - size) > 1) {
            rooted = false;
            throw new IllegalArgumentException("The input to the constructor does not correspond to a rooted DAG - Graph Not rooted");
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return db.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return db.get(word) != null;
    }

    // distance between nounA and nounB (defined below )
    public int distance(String nounA, String nounB) {
        List<Integer> nounAIds = new ArrayList<>();
        return -1;// if the nouns are not in the db
    }

    /* a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB in a shortest ancestral
     * path (defined below) */
    public String sap(String nounA, String nounB) {
        StringBuilder sb = new StringBuilder();
        int idOfA = -1;
        int idOfB = -1;
        // a word can have a number of keys. see if this was discussed in the forum and if not, ask what to do
        SAP sap = new SAP(digraph);
        return synsets[sap.ancestor(db.get(nounA), db.get(nounB))];
    }

    // do unit testing here
    public static void main(String[] args) {
        System.out.println("using " + args[0] + " and " + args[1] + "files for this round.");
        WordNet wordNet = new WordNet(args[0], args[1]);
        System.out.println(wordNet.isNoun("entity"));
        System.out.println("The common ancestor " + wordNet.sap("worm", "bird"));
        System.out.println("The distance expected between worm and bird is 5, the result: " + wordNet.distance("worm", "bird"));
    }
}
