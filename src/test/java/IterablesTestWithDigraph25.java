import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IterablesTestWithDigraph25 {
    public static void main(String[] args) {
        In in = new In("digraph25.txt");
        Digraph digraph = new Digraph(in);
        SAP sap = sap = new SAP(digraph);
        List<Integer> sources = new ArrayList<>();
        sources.add(7);
        List<Integer> destinations = new ArrayList<>();
        destinations.add(1);
        // int result = sap.length(sources, destinations);
        // if (result != 1)
        //    throw new AssertionError("The minimum distance between 1 and 0 should be 1, but it is: " + result);
        sources.add(3);
        destinations.add(4);
        int result = sap.length(sources, destinations);
        if (result != 1)
            throw new AssertionError("The minimum distance between two sets of values {7,3} {1,4} should be the minimum " +
                    "of each outcome, which in this case should be 1, but it is: " + result);
        int ancestor = sap.ancestor(sources, destinations);
        if (ancestor != 1)
            throw new AssertionError("The ancestor for two sets of values should be the one with the shortest" +
                    "path, which in this case is 1, but the value calculated is: " + ancestor);
        sources = new ArrayList<>(Arrays.asList(13, 23, 24));
        destinations = new ArrayList<>(Arrays.asList(6, 16, 17));
        result = sap.length(sources, destinations);
        if (result != 4)
            throw new AssertionError("The shortest ancestral path of digraph25 example should be 4, but " +
                    "it is: " + result);
        ancestor = sap.ancestor(sources, destinations);
        if (ancestor != 3)
            throw new AssertionError("The shortest common ancestor for the second set, should be 3, but " +
                    "it is: " + ancestor);
        sources = new ArrayList<>(Arrays.asList(3, 12));
        destinations = new ArrayList<>(Arrays.asList(4, 6));
        ancestor = sap.ancestor(sources, destinations);
        if (ancestor != 1)
            throw new AssertionError("The shortest common ancestor for test3 should be 1, but it is: " + ancestor);
        else System.out.printf("Ancestor value in Test number 3 is correct.\n");
        result = sap.length(sources, destinations);
        if (result != 2)
            throw new AssertionError("The shortest distance of two sets of nodes in Test 3 should be 2, but it is: " + result);
        else System.out.printf("The shortest distance in Test number 3 is connect.\n");
        System.out.printf("Testing lockstep bfs to make sure I get the right value before attempting to use it in sets. ");
        ancestor = sap.ancestor(5, 6);
        if (ancestor != 2)
            throw new AssertionError("The is the ancestor value between 5, and 6 should be 2, but it is : %d" + ancestor);
        System.out.printf("running test number 4 to test result of an empty set and another.\n");
        sources = new ArrayList<>();
        destinations = new ArrayList<>(Arrays.asList(5, 6));
        ancestor = sap.ancestor(sources, destinations);
        if (ancestor != -1)
            throw new AssertionError("The ancestor of an empty set and another should be -1, but it is: " + ancestor);
        else System.out.printf("The ancestor value for Test number 4 correct.");
        result = sap.length(sources, destinations);
        if (result != -1) throw new AssertionError("The shortest distance for Test 4 should be -1, but it is: " + result);
        else System.out.printf("The shortest distance for test number 4 is correct.");

    }
}
