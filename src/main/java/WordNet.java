import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordNet {
    // this is a hashmap of nouns and ids -todo: Try two arrays of String and Integer
    private final HashMap<Integer, String> db = new HashMap<>();
    private SAP sap;
    private final Digraph digraphDFCopy;


    // constructor takes the name of two input files
    public WordNet(String synsets, String hypernyms) {
        createDb(synsets);
        digraphDFCopy = createGraph(hypernyms);
        sap = new SAP(digraphDFCopy);
    }

    private void createDb(String synsets) {
        In in = new In(synsets);
        int val;
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(",");
            val = Integer.parseInt(a[0]);
            db.put(val, a[1]);
            // todo -- you can not read twice. Fix 
        }
    }

    private Digraph createGraph(String hypernyms) {
        In in = new In(hypernyms);
        Digraph digraph = new Digraph(db.size());
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
            throw new IllegalArgumentException("The input to the constructor does not correspond to a rooted DAG - cycle detected");
        }
        if (Math.abs(index - db.size()) > 1) {
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
        for (int i : db.keySet()) {
            for (String s : db.get(i).split(" ")) {
                if (s.equals(word)) return true;
            }
        }
        return false;
    }

    // distance between nounA and nounB (defined below )
    public int distance(String nounA, String nounB) {
        List<Integer> nounAIds = new ArrayList<>();
        List<Integer> nounBIds = new ArrayList<>();
        for (int i : db.keySet()) {
            for (String s : db.get(i).split(" ")) {
                if (s.equals(nounA)) nounAIds.add(i);
                if (s.equals(nounB)) nounBIds.add(i);
            }
        }
        return sap.length(nounAIds, nounBIds);
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
        int i = sap.ancestor(nounAIds, nounBIds);
        if (i == -1) return "";
        else return db.get(i);
    }

    // do unit testing here
    public static void main(String[] args) {
        System.out.println("----------------------Running WordNet Main---------------------");
        System.out.println("using " + args[0] + " and " + args[1] + "files for this round.");
        WordNet wordNet = new WordNet(args[0], args[1]);
        Stopwatch time = new Stopwatch();
        System.out.println("The common ancestor between worm and animal: " + wordNet.sap("worm", "bird"));
        double now = time.elapsedTime();
        StdOut.println("worm/bird test using HashMap took: " + now);
        System.out.println("The distance expected between worm and bird is 5, the result: " +
                wordNet.distance("worm", "bird"));
        System.out.println(wordNet.isNoun("entity"));
        StdOut.println("The common ancestor for quadrangle and mountain_devil is:" +
                wordNet.sap("quadrangle", "mountain_devil"));
        StdOut.println("The distance expected between mountain_devil and quadrangle should be 11, the result: " +
                wordNet.distance("quadrangle", "mountain_devil"));
        StdOut.println("The common ancestor between individual and edible_fruit should be physical_entity, and it is: " + wordNet.sap("individual", "edible_fruit"));
        StdOut.println("The distance expected between the nouns individual and edible_fruit should be 7, and it is: " + wordNet.distance("individual", "edible_fruit"));
        StdOut.println("The distance between the nouns white_marlin and mileage should be 23, and it is: " + wordNet.distance("white_marlin", "mileage"));
        StdOut.println("The distance between the nouns Black_Plague and black_marline should be 33, and it is:" + wordNet.distance("Black_Plague", "black_marlin"));
        StdOut.println("The distance between the nouns American_water_spaniel and histology should be 27 and it is: " + wordNet.distance("American_water_spaniel", "histology"));
        StdOut.println("The distance between the nouns Brown_Swiss, and barrel_roll should be 29, and it is: " + wordNet.distance("Brown_Swiss", "barrel_roll"));
        /*
        * wordNet = new WordNet("hypernyms3InvalidCycle.txt", "synsets3.txt");
        wordNet.sap("a","b");
        * */

    }
}
