import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class Digraph2Test {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        int ancestor = sap.ancestor(1, 5);
        if (ancestor != 0)
            throw new AssertionError("the value of ancesttor(1, 5) should be 0 but it is: " + ancestor);
        int dist = sap.length(1, 5);
        if (dist != 2) throw new AssertionError("the value of length(1, 5) should be 2 but it is: " +
                dist);
        ancestor = sap.ancestor(1, 3);
        if (ancestor != 3)
            throw new AssertionError("the value of ancesttor(1, 3) should be 3 but it is: " + ancestor);
         dist = sap.length(1, 3);
        if (dist != 2) throw new AssertionError("the value of length(1, 3) should be 2 but it is: " +
                dist);
         /* System.out.println("length between 1 and 3, should be 2: " + sap.length(1, 3));
         System.out.println("length between 1 and 2, should be 1: " + sap.length(1, 2));
         int result = sap.getAncestorII(1, 3);
         if (result != 2) throw new AssertionError("the value of length() between 1 and 3 should be 2, but it is: " + result);
         result = sap.length(1, 2);
         if (result != 1) throw new AssertionError("the value of length() between 1 and 2 should be 1, but it is: " + result);
         result = sap.length(1, 4);
         if (result != 3) throw new AssertionError("the value of length() between 1 and 4 should be 3, but it is: " + result);
         int result = sap.length(1,5);
        ancestor = sap.getAncestorII(1, 3);
        if (ancestor != 3)
            throw new AssertionError("the value of getAncestorII(1, 3) should be 3 but it is: " + ancestor);

        ancestor = sap.getAncestorII(1, 5);
        if (ancestor != 0)
            throw new AssertionError("the value of getAncestorII(1, 5) should be 0 but it is: " + ancestor);
        ancestor = sap.getAncestorII(1, 0);
        if (ancestor != 0) throw new AssertionError("the value of getAncestorII(1,0) should be 0 but it is" + ancestor);
        ancestor = sap.getAncestorII(1, 4);
        if (ancestor != 0)
            throw new AssertionError("the value of getAncestorII(1,4) should be 0 but it is: " + ancestor);
        ancestor = sap.getAncestorII(1, 2);
        if (ancestor != 2)
            throw new AssertionError("the value of getAncestorII(1,2) should be 2, but it is: " + ancestor);
         result = sap.length(1, 3);
         if (result != 2) throw new AssertionError("the value of length() between 1 and 3 should be 2, but it is: " + result);
         result = sap.length(1, 0);
         if (result != 1) throw new AssertionError("the value of length() between 1 and 0 should be 1, but it is: " + result);
         */
    }
}
