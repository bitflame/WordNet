import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Graph5Test {
    In in;
    Digraph digraph;
    SAP sap;
    int shortestDistance;
    int ancestor;

    private void runTest() {


        in = new In("digraph5.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        shortestDistance = sap.length(17, 21);
        if (shortestDistance != 5)
            System.out.printf("The distance between 17 and 21 should be 5, but it is: %d\n", shortestDistance);
        //else System.out.printf("Test 1 of Graph5 shortest distance passed. \n");
        ancestor = sap.ancestor(17, 21);
        if (ancestor != 21)
            System.out.printf("ancestor between 17, and 21 ing Graph5 should be 21, but it is : %d\n", ancestor);
        //else System.out.printf("Test 1 of Graph5 ancestor passed.\n");

        shortestDistance = sap.length(14, 21);
        if (shortestDistance != 8)
            System.out.printf("The distance between 14 and 21 should be 8, but it is: %d\n", shortestDistance);
        //else System.out.printf("Test 2 of Graph5 shortest distance passed.\n");
        ancestor = sap.ancestor(14, 21);
        if (ancestor != 21)
            System.out.printf("The distance between 14 and 21 in Graph5 should be 21, but it is: %d\n", ancestor);
        //else System.out.printf("Test 2 of Graph5 ancestor passed.\n");

        shortestDistance = sap.length(9, 20);
        if (shortestDistance != 3)
            System.out.printf("The distance between 9 and 20 should be 3, but it is: %d\n", shortestDistance);
        //else System.out.printf("Test 3 of Graph5 shortest distance passed.\n");
        ancestor = sap.ancestor(9, 20);
        if (ancestor != 9)
            System.out.printf("The ancestor between 9, and 20 in graph5 should be 20, but it is: %d\n", ancestor);
        //else System.out.printf("Test 3 of Graph5 ancestor passed.\n");

        ancestor = sap.ancestor(10, 12);
        if (ancestor != 10) System.out.printf("The value of ancestor should be 10, and it is: %d\n ", ancestor);
        //else System.out.printf("Test 4 of Graph5 ancestor passed.\n");
        shortestDistance = sap.length(10, 12);
        if (shortestDistance != 2)
            System.out.printf("The distance between 10 and 12 should be 2, but it is: %d\n", shortestDistance);
        //else System.out.printf("Test 4 of Graph5 shortest distance passed.\n");
        shortestDistance = sap.length(21, 0);
        if (shortestDistance != 1)
            System.out.printf("The distance between 21, and 0 should be 1, and it is: %d\n", shortestDistance);
        //else System.out.printf("Test 5 of Graph5 shortest distance passed.\n");
        ancestor = sap.ancestor(21, 0);
        if (ancestor != 0)
            System.out.printf("ancestor between 21 and 0 in Graph5 should be 0, but it is: %d\n", ancestor);
        //else System.out.printf("Test 5 of Graph5 ancestor passed.\n");
        shortestDistance = sap.length(7, 12);
        if (shortestDistance != 5)
            System.out.printf("The distance between 7 and 12 should be 5, but it is: %d\n ", shortestDistance);
        //else System.out.printf("Test 6 of Graph5 shortest distance passed.\n");
        ancestor = sap.ancestor(7, 12);
        if (ancestor != 10)
            System.out.printf("The ancestor for nodes 7 and 12 in graph 5 should be 13, but it is: %d\n", ancestor);
        //else System.out.printf("Test 6 of Graph5 ancestor passed.\n");
        shortestDistance = sap.length(13, 21);
        if (shortestDistance != 4)
            System.out.printf("The distance between nodes 13, and 21 should be 4, but it is: %d\n", shortestDistance);
        //else System.out.printf("Test 7 of Graph5 shortest distance passed.\n");
        ancestor = sap.ancestor(13, 21);
        if (ancestor != 10)
            System.out.printf("The ancestor for nodes 13, and 21 in graph 3 should be 10, but it is: %d\n", ancestor);
        //else System.out.printf("Test 7 of Graph5 ancestor passed.\n");
        shortestDistance = sap.length(7, 8);
        if (shortestDistance != 1)
            System.out.printf("The distance between nodes 7 and 8 should be 1, but it is: %d\n ");
        //else System.out.printf("Test 8 of Graph5 shortest distance passed.\n");
        ancestor = sap.ancestor(7, 8);
        if (ancestor != 8)
            System.out.printf("The ancestor for the nodes 7 and 8 in graph 5 should be 8, but it is: %d\n", ancestor);
        //else System.out.printf("Test 8 of Graph5 ancestor passed.\n");
        List<Integer> sources = new ArrayList<>(Arrays.asList(7, 7));
        List<Integer> destinations = new ArrayList<>(Arrays.asList(8, 8));
        ancestor = sap.ancestor(sources, destinations);
        if (ancestor != 8)
            System.out.printf("The ancestor for nodes 7 and 8 in graph 3 should be 8, but it is: %d\n ", ancestor);
        shortestDistance = sap.length(sources, destinations);
        if (shortestDistance != 1)
            System.out.printf("The distance between nodes 7 and 8 should be 1, but it is: %d\n ", shortestDistance);
        ancestor = sap.ancestor(13, 21);
        if (ancestor != 10)
            System.out.printf("ancestor for 13, and 21 in Graph5 should be 9, but it is: %d\n", ancestor);
        //else System.out.printf("Test 9 ancestor for Graph5 passed.\n");
        shortestDistance = sap.length(13, 21);
        if (shortestDistance != 4)
            System.out.printf("shortest distance between 13, and 21 in Graph5 should be 4, but it is: %d\n", shortestDistance);
        //else System.out.printf("Test 9 for Graph5 shortest distance passed.\n");

    }
private void runIterables() {
    List<Integer> sources = new ArrayList<>(Arrays.asList(13, 7, 21, 10, 9, 14, 17));
    List<Integer> destinations = new ArrayList<>(Arrays.asList(21, 12, 0, 12, 20));
    sap.length(sources, destinations);
    sap.ancestor(sources, destinations);
}
    public static void main(String[] args) {
        Graph5Test graph5Test = new Graph5Test();
        StdOut.println("----------------------------------Running AutoGrader Tests for Digraph 5 ----------------------------------");
        for (int i = 0; i < 100000; i++) {
            graph5Test.runTest();
            graph5Test.runIterables();
            graph5Test.runTest();
            graph5Test.runIterables();
        }
    }
}
