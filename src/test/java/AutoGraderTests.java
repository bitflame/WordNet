import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class AutoGraderTests {
    public static void main(String[] args) {
        StdOut.println("----------------------------------Running AutoGrader Tests for Digraph3----------------------------------");
        Digraph digraph = new Digraph(new In("digraph3.txt"));
        SAP sap = new SAP(digraph);
        int distance = sap.length(10, 7);
        if (distance != 3) throw new AssertionError("Distance between 10 and 7 should be 3, but it is: " + distance);
        distance = sap.length(5, 14);
        if (distance != -1)
            throw new AssertionError("Distance between 5 and 14 should be -1 since the two nodes are not connected, but it is: " + distance);
        distance = sap.length(2, 13);
        if (distance != -1)
            throw new AssertionError("Distance between 2 and 13 should be -1 since the two nodes are not connected, but it is: " + distance);
        distance = sap.length(10, 3);
        if (distance != -1)
            throw new AssertionError("Distance between 10 and 3 should be -1 since the two nodes are not connected, but it is: " + distance);
        int ancestor = sap.ancestor(7, 11);
        if (ancestor != 8)
            throw new AssertionError("The value of ancestor between 7 and 11 should be 8, but it is: " + ancestor);
        distance = sap.length(7, 11);
        if (distance != 3)
            throw new AssertionError("The distance between 7 and 11 should be 3, but it is: " + distance);
        distance = sap.length(2, 3);
        if (distance != 1) throw new AssertionError("The distance between 2 and 3 should be 1, but it is: " + distance);
        distance = sap.length(10, 9);
        if (distance != 1)
            throw new AssertionError("The distance between 10 and 9 should be 1, but it is: " + distance);
        distance = sap.length(12, 13);
        if (distance != 4)
            throw new AssertionError("The distance between 12 and 13 should be 4, but it is: " + distance);
        StdOut.println("----------------------------------Running AutoGrader Tests for Digraph4----------------------------------");
        digraph = new Digraph(new In("digraph4.txt"));
        sap = new SAP(digraph);
        distance = sap.length(1, 4);
        if (distance != 3) throw new AssertionError("The distance between 1 and 4 should be 3, but it is: " + distance);
        StdOut.println("----------------------------------Running AutoGrader Tests for Digraph5----------------------------------");
        digraph = new Digraph(new In("digraph5.txt"));
        sap = new SAP(digraph);
        distance = sap.length(17, 21);
        if (distance != 5)
            throw new AssertionError("The distance between 17 and 21 should be 5, but it is: " + distance);
        StdOut.println("----------------------------------Running AutoGrader Tests for Digraph6----------------------------------");
        digraph = new Digraph(new In("digraph6.txt"));
        sap = new SAP(digraph);
        distance = sap.length(0, 5);
        if (distance != 5) throw new AssertionError("The distance between 0 and 5 should be 5, but it is: " + distance);
        StdOut.println("----------------------------------Running AutoGrader Tests for Digraph9----------------------------------");
        digraph = new Digraph(new In("digraph9.txt"));
        sap = new SAP(digraph);
        distance = sap.length(7, 8);
        if (distance != -1) throw new AssertionError("The distance between 7 and 8 should be -1, but it is: " + distance);
    }
}
