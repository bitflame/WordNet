import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IterablesTestWithDigraph25 {
    public static void main(String[] args) {
        In in = new In("digraph25.txt");
        Digraph digraph = new Digraph(in);
        SAP sap = new SAP(digraph);
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
        else System.out.printf("Passed the first ancestor test.\n");
        int ancestor = sap.ancestor(sources, destinations);
        if (ancestor != 1)
            throw new AssertionError("The ancestor for two sets of values should be the one with the shortest" +
                    "path, which in this case is 1, but the value calculated is: " + ancestor);
        else System.out.printf("Passed the 2nd ancestor test. \n");
        sources = new ArrayList<>(Arrays.asList(13, 23, 24));
        destinations = new ArrayList<>(Arrays.asList(6, 16, 17,13));
        for (int i : sources) {
            for (int j : destinations) {
                System.out.printf(" i=%d j=%d distance between them: %d their ancestor is=%d\n", i, j, sap.length(i, j), sap.ancestor(i, j));
            }
        }
        result = sap.length(sources, destinations);
        if (result != 0)
            throw new AssertionError("The shortest ancestral path of digraph25 example should be 0, but " +
                    "it is: " + result);
        ancestor = sap.ancestor(sources, destinations);
        if (ancestor != 13)
            throw new AssertionError("The shortest common ancestor for the second set, should be 13, but " +
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
        // create a test with a connected pair, and another with no route to that ancestor or any {3, 2} {4, 0}
        ancestor = sap.ancestor(2, 0);
        if (ancestor != 0)
            throw new AssertionError("The ancestor between 2 and 0 should be 0, but it is: \n" + ancestor);
        else System.out.printf("Tested the ancestor between 2 and 0, and it was successful.\n");
        System.out.printf("**********************Testing code does process the transitive closure of two iterables. i.e. " +
                "path from one to each of the other set.\n");
        digraph = new Digraph(new In("digraph9.txt"));
        sap = new SAP(digraph);
        ancestor = sap.ancestor(4, 8);
        if (ancestor != -1)
            throw new AssertionError("The ancestor between 4 and 8 in graph 9 should be -1, but it is:" + ancestor);
        else System.out.printf("Tested 4 and 8 in graph 9 and they are not connected.\n");
        result = sap.length(0, 3);
        if (result != 1)
            throw new AssertionError("In graph 9, the distance between 3 and 0 should be 1, but it is:\n" + result);
        else System.out.printf("The distance between 0, and 3 is correct. \n");
        ancestor = sap.ancestor(0, 3);
        if (ancestor != 0)
            throw new AssertionError("node 0 and 3 in graph 9 are connected and the ancestor should be 0, but it is" + ancestor);
        else System.out.printf("The value of ancestor between nodes 0 and 3 is correct.\n");
        // 3, 4 should give 4. Then 3, 8 and 4, 8 should give -1. But 4 should persist because that is the last ancestor for this bunch
        System.out.printf("Testing \n");
        sources = new ArrayList<>(Arrays.asList(3, 4));
        destinations = new ArrayList<>(Arrays.asList(4, 8));
        ancestor = sap.ancestor(sources, destinations);
        if (ancestor != 4)
            throw new AssertionError("The last valid ancestor of two sets should persist. In this case it" +
                    "should be 4, but it is:" + ancestor);
        else System.out.printf("The ancestor for the sets {3, 4} and {4, 8} is correct. \n");

        // loaded digraph25 again
        in = new In("digraph25.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        System.out.printf("Testing lockstep bfs to make sure I get the right value before attempting to use it in sets. \n");
        ancestor = sap.ancestor(5, 6);
        if (ancestor != 2) throw new AssertionError("The ancestor between 5 and 6 should be 2, but it is: " + ancestor);
        else System.out.printf("The ancestor value between 5 and 6 is correct.\n");
        if (ancestor != 2)
            throw new AssertionError("The is the ancestor value between 5, and 6 should be 2, but it is : %d" + ancestor);
        System.out.printf("running test number 4 to test result of an empty set and another.\n");
        sources = new ArrayList<>();
        destinations = new ArrayList<>(Arrays.asList(5, 6));
        ancestor = sap.ancestor(sources, destinations);
        if (ancestor != -1)
            throw new AssertionError("The ancestor of an empty set and another should be -1, but it is: " + ancestor);
        else System.out.printf("The ancestor value for Test number 4 correct.\n");
        result = sap.length(sources, destinations);
        if (result != -1)
            throw new AssertionError("The shortest distance for Test 4 should be -1, but it is: " + result);
        else System.out.printf("The shortest distance for test number 4 is correct.\n");
        sources = new ArrayList<>(Arrays.asList(0, 4, 14, 15));
        destinations = new ArrayList<>(Arrays.asList(2, 10, 12, 13, 14));
        ancestor = sap.ancestor(sources, destinations);
        System.out.printf("expecting 14 %d\n", ancestor);
        result = sap.length(sources, destinations);
        System.out.printf("expecting 0 %d\n", result);
        for (int i : sources) {
            for (int j : destinations) {
                System.out.printf("i: %d j: %d distance between them: %d\n", i, j, sap.length(i, j));
            }
        }
        sources = new ArrayList<>(Arrays.asList(17));
        destinations = new ArrayList<>(Arrays.asList(10));
        ancestor = sap.ancestor(sources, destinations);
        System.out.printf("Expecting 10, %d\n", ancestor);
        ancestor = sap.ancestor(17, 10);
        System.out.printf("Expecting 10, %d\n", ancestor);
        result = sap.length(sources, destinations);
        System.out.printf("Expecting 1. %d\n", result);
        result = sap.length(17, 10);
        System.out.printf("Expecting 1. %d\n", result);
    }
}
