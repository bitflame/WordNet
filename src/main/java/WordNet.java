import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordNet {
    // this is a hashmap of nouns and ids
    HashMap<Integer, String> db = new HashMap<>();
    String[] synsets;
    int size = 0;  // number of synsets
    SAP sap;
    boolean hasCycle = false;
    boolean rooted = true;
    Digraph digraph;

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
                db.put(val, noun);
            }
            size++;
        }
    }

    private void createGraph(String hypernyms) {
        In in = new In(hypernyms);
        synsets = new String[db.size()];
        digraph = new Digraph(size);
        int index = 0;
        while (in.hasNextLine()) {
            index++;
            String[] a = in.readLine().split(",");
            for (int i = 0; i < a.length - 1; i++) {
                synsets[i] = db.get(i);
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
        return db.values();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return db.get(word) != null;
    }

    // distance between nounA and nounB (defined below )
    public int distance(String nounA, String nounB) {
        List<Integer> nounAIds = new ArrayList<>();
        List<Integer> nounBIds = new ArrayList<>();
        for (int i : db.keySet()) {
            for (String s : db.get(i).split(" ")) {
                if (nounA.equals(s)) nounAIds.add(i);
                if (nounB.equals(db.get(i))) nounBIds.add(i);
            }
        }
        int length = sap.length(nounAIds, nounBIds);
        return length;// if the nouns are not in the db
    }

    /* a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB in a shortest ancestral
     * path (defined below) */
    public String sap(String nounA, String nounB) {
        StringBuilder sb = new StringBuilder();
        List<Integer> nounAIds = new ArrayList<>();
        List<Integer> nounBIds = new ArrayList<>();
        for (int i : db.keySet()) {
            for (String s : db.get(i).split(" ")) {
                if (nounA.equals(s)) nounAIds.add(i);
                if (nounB.equals(db.get(i))) nounBIds.add(i);
            }
        }
        sap = new SAP(digraph);
        int i = sap.ancestor(nounAIds, nounBIds);
        if (i == -1) return "";
        //else return synsets[i];
        else return db.get(i);
    }

    /* todo -- cache nounAIds and nounBIds in case these methods are called back to back */
    public String getSapPath(String nounA, String nounB) {
        StringBuilder sb = new StringBuilder();
        int nounAId = -1;
        int nounBId = -1;
        for (int i : db.keySet()) {
            for (String s : db.get(i).split(" ")) {
                if (nounA.equals(s)) nounAId = i;
                if (nounB.equals(db.get(i))) nounBId = i;
            }
        }
        SAP s = new SAP(digraph);
        for (int id : s.getPath(nounAId, nounBId)) {
            ///sb.append(db.get(id) + " ");
            sb.append(synsets[id] + " "); // this should be even faster
        }
        return sb.toString();
    }

    // do unit testing here
    public static void main(String[] args) {
        System.out.println("using " + args[0] + " and " + args[1] + "files for this round.");
        WordNet wordNet = new WordNet(args[0], args[1]);
        System.out.println(wordNet.isNoun("entity"));
        System.out.println("The common ancestor " + wordNet.sap("worm", "bird"));
        System.out.println("The distance expected between worm and bird is 5, the result: " +
                wordNet.distance("worm", "bird"));
        //System.out.println("The shortest path between worm and bird is: " + wordNet.getSapPath("worm", "bird"));
    }
}
