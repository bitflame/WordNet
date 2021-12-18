import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordNet {
    // this is a hashmap of nouns and ids -todo: Try two arrays of String and Integer
    private HashMap<Integer, String> db = new HashMap<>();
    private String[] synsets;
    private int size = 0;  // number of synsets
    private SAP sap;
    private boolean hasCycle = false;
    private boolean rooted = true;
    private final Digraph digraphDFCopy;

    // constructor takes the name of two input files
    public WordNet(String synsets, String hypernyms) {
        createDb(synsets);
        digraphDFCopy= createGraph(hypernyms);
    }

    private void createDb(String synsets) {
        In in = new In(synsets);
        int val;
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(",");
            val = Integer.parseInt(a[0]);
            String[] syns = a[1].split(" ");
            db.put(val, a[1]);
            for (String noun : syns) {
                size++;
            }
        }
    }

    private Digraph createGraph(String hypernyms) {
        In in = new In(hypernyms);
        synsets = new String[size];
        Digraph digraph = new Digraph(db.size());
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
        if (Math.abs(index - db.size()) > 1) {
            rooted = false;
            throw new IllegalArgumentException("The input to the constructor does not correspond to a rooted DAG - Graph Not rooted");
        }
        return digraph;
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
        List<Integer> nounAIds = new ArrayList<>();
        List<Integer> nounBIds = new ArrayList<>();
        for (int i : db.keySet()) {
            for (String s : db.get(i).split(" ")) {
                if (s.equals(nounA)) nounAIds.add(i);
                if (s.equals(nounB)) nounBIds.add(i);
            }
        }
        sap = new SAP(digraphDFCopy);
        int i = sap.ancestor(nounAIds, nounBIds);
        if (i == -1) return "";
            //else return synsets[i];
        else return db.get(i);
    }

    /* todo -- cache nounAIds and nounBIds in case these methods are called back to back */
    private String getSapPath(String nounA, String nounB) {
        StringBuilder sb = new StringBuilder();
        int nounAId = -1;
        int nounBId = -1;
        for (int i : db.keySet()) {
            for (String s : db.get(i).split(" ")) {
                if (nounA.equals(s)) nounAId = i;
                if (nounB.equals(db.get(i))) nounBId = i;
            }
        }
        SAP s = new SAP(digraphDFCopy);
        /*for (int id : s.getPath(nounAId, nounBId)) {
            sb.append(synsets[id] + " ");
        }*/
        return sb.toString();
    }

    // do unit testing here
    public static void main(String[] args) {
        System.out.println("using " + args[0] + " and " + args[1] + "files for this round.");
        WordNet wordNet = new WordNet(args[0], args[1]);
        //System.out.println(wordNet.isNoun("entity"));
        System.out.println("The common ancestor between worm and animal: " + wordNet.sap("worm", "bird"));
        System.out.println("The distance expected between worm and bird is 5, the result: " +
                wordNet.distance("worm", "bird"));
        System.out.println("The shortest path between worm and bird is: " + wordNet.getSapPath("worm", "bird"));
    }
}
