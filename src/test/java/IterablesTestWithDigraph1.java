import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IterablesTestWithDigraph1 {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        In in = new In("digraph25.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        List<Integer> sources = new ArrayList<>();
        sources.add(1);
        List<Integer> destinations = new ArrayList<>();
        destinations.add(0);
        int result = sap.length(sources, destinations);
        if (result != 1)
            throw new AssertionError("The minimum distance between 1 and 0 should be 1, but it is: " + result);
        sources.add(7);
        destinations.add(1);
        result = sap.length(sources,destinations);
        if (result != 0)
            throw new AssertionError("The minimum distance between two sets of values {1,0} {7,1} should be the minimum " +
                    "of each outcome, which in this case should be 0, but it is: " + result);
        sources = new ArrayList<>(Arrays.asList(13, 23, 24));
        destinations = new ArrayList<>(Arrays.asList(6, 16, 17));
        result = sap.length(sources, destinations);
        if (result != 4)
            throw new AssertionError("The shortest ancestral path of digraph25 example should be 4, but " +
                    "it is: " + result);
    }
}
