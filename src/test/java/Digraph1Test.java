import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class Digraph1Test {
    public static void main(String[] args) {
        System.out.println("----------------------Running Digraph1Test---------------------");
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        // if one node is not a node in the graph
        int distance = sap.length(2, 6);
        if (distance != -1)
            throw new AssertionError("The length of Minimum Distance between 2 and 6 should be -1, but it is: " + distance);
        int ancestor = sap.ancestor(2, 6);
        if (ancestor != -1)
            throw new AssertionError("the value of ancestor() to nonexistent node should be -1, but it actually is: " + ancestor);
        // if root is 'from' or 'to'
        distance = sap.length(2, 0);
        if (distance != 1)
            throw new AssertionError("the value of length() between 2 and 0 should be 1, but it actually is: " + distance);
        ancestor = sap.ancestor(2, 0);
        if (ancestor != 0)
            throw new AssertionError("The value of ancestor() for 2,0 should be 0 but it is: " + ancestor);
        // if root is 'from' or 'to'
        distance = sap.length(1, 0);
        if (distance != 1)
            throw new AssertionError("the value of length() between 1 and 0 should be 1, but it actually is: " + distance);
        // if root is 'from' or 'to'
        distance = sap.length(4, 1);
        if (distance != 1)
            throw new AssertionError("the value of length() between 4 and 1 should be 1, but it actually is: " + distance);
        // multihop path
        distance = sap.length(7, 9);
        if (distance != 4)
            throw new AssertionError("the value of length() between 7 and 9 should be 4, but it actually is: " + distance);
        distance = sap.length(10, 1);
        if (distance != 2)
            throw new AssertionError("the value of length() between 10 and 1 should be 2, but it actually is: " + distance);
        distance = sap.length(1, 10);
        if (distance != 2)
            throw new AssertionError("the value of length() between 1 and 10 should 2, but it actually is: " + distance);
        distance = sap.length(12, 9);
        if (distance != 3)
            throw new AssertionError("the value of length() between 12 and 9 should be 3, but it actually is: " + distance);
        // if (result != 45) throw new AssertionError("why isn't minDistance value = 45? ");
        ancestor = sap.ancestor(3, 3);
        if (ancestor != 3)
            throw new AssertionError("the value ancestor() returns for 3,3 should be 3, but it is: " + ancestor);
        distance = sap.length(3, 3);
        if (distance != 0)
            throw new AssertionError("the value of length() between 3 and 3 should be 0, but it is: " + distance);
        ancestor = sap.ancestor(3, 3);
        if (ancestor != 3)
            throw new AssertionError("the value of ancestor() between 3 and 3 should be 3, but it is: " + ancestor);
        distance = sap.length(0, 3);
        if (distance != 2)
            throw new AssertionError("the value of length() between 0 and 3 should be 2, but it is: " + distance);
        distance = sap.length(0, 3);
        if (distance != 2)
            throw new AssertionError("the value of length() between 0 and 3 should be 2, but it is: " + distance);
        distance = sap.length(0, 3);
        if (distance != 2)
            throw new AssertionError("the value of length() between 0 and 3 should be 2, but it is: " + distance);
        ancestor = sap.ancestor(3, 3);
        if (ancestor != 3)
            throw new AssertionError("the value of ancestor() between 3 and 3 should be 3, but it is: " + ancestor);
        ancestor = sap.ancestor(11, 4);
        if (ancestor != 1)
            throw new AssertionError("the value of ancestor for nodes 4, 11 should be 1 but it is: " + ancestor);
        distance = sap.length(11, 4);
        if (distance != 4)
            throw new AssertionError("the value of length() for nodes 4, 11 should be 4 but it is: " + distance);
    }
}
