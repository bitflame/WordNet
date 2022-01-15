import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IterablesTestWithDigraph1 {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        List<Integer> sources = new ArrayList<>(Arrays.asList(1, 7));
        List<Integer> destinations = new ArrayList<>(Arrays.asList(0, 1));
        int result = sap.length(sources, destinations);
        if (result != 1)
            throw new AssertionError("The minimum distances of two iterable sets should be 1, but it is: " + result);
    }
}
