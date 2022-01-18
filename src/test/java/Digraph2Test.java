import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class Digraph2Test {
    public static void main(String[] args) {
        System.out.println("----------------------Running Digraph2Test---------------------");
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        int ancestor = sap.ancestor(1, 2);
        if (ancestor != 2)
            throw new AssertionError("the value of ancestor(1,2) should be 2 but it is: " + ancestor);
        int dist = sap.length(1, 2);
        if (dist != 1) throw new AssertionError("the value of length(1,2) should be 1 but it is: " +
                dist);
        ancestor = sap.ancestor(1, 0);
        if (ancestor != 0)
            throw new AssertionError("the value of ancestor(1,0) should be 0 but it is: " + ancestor);
        dist = sap.length(1, 0);
        if (dist != 1) throw new AssertionError("the value of length(1,0) should be 1 but it is: " + dist);
        ancestor = sap.ancestor(2, 0);
        if (ancestor != 0)
            throw new AssertionError("the value of ancesttor(2, 0) should be 0 but it is: " + ancestor);
        dist = sap.length(2, 0);
        if (dist != 4) throw new AssertionError("the value of length(2, 0) should be 4 but it is: " +
                dist);
        ancestor = sap.ancestor(1, 5);
        if (ancestor != 0)
            throw new AssertionError("the value of ancesttor(1, 5) should be 0 but it is: " + ancestor);
        dist = sap.length(1, 5);
        if (dist != 2) throw new AssertionError("the value of length(1, 5) should be 2 but it is: " +
                dist);
        ancestor = sap.ancestor(1, 3);
        if (ancestor != 3)
            throw new AssertionError("the value of ancesttor(1, 3) should be 3 but it is: " + ancestor);
        dist = sap.length(1, 3);
        if (dist != 2) throw new AssertionError("the value of length(1, 3) should be 2 but it is: " +
                dist);
        ancestor = sap.ancestor(5, 5);
        if (ancestor != 5)
            throw new AssertionError("the value of ancestor() for 5,5 should be 5, but it is:" + ancestor);
        dist = sap.length(5, 5);
        if (dist != 0) throw new AssertionError("the value of length() for 5,5 should be 0, but its: " + dist);
        dist = sap.length(4, 1);
        if (dist != 3) throw new AssertionError("The value of length() between 4, 1 should be 3, but it is: " + dist);
    }
}
