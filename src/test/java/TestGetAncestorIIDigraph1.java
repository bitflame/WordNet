import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class TestGetAncestorIIDigraph1 {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        int ancestor = sap.getAncestorII(2, 0);
        if (ancestor != 0)
            throw new AssertionError("the value of getAncestorII(2, 0) should be 0 but it is: " + ancestor);
        int distance = sap.getLengthII(2, 0);
        if (distance != 1)
            throw new AssertionError("the value of distance for getAncestorII should be 1, but it is: " + distance);
        ancestor = sap.getAncestorII(1, 0);
        if (ancestor != 0)
            throw new AssertionError("the value of getAncestorII(1,0) should be 0 but it is: " + ancestor);
        ancestor = sap.getAncestorII(7, 8);
        if (ancestor != 3)
            throw new AssertionError("the value of getAncestorII(7, 8) should be 3, but it is: " + ancestor);
        distance = sap.getLengthII(7, 8);
        if (distance != 2)
            throw new AssertionError("the value of distance for getAncestorII(7,8) should be 2, but it is: " + distance);
        ancestor = sap.getAncestorII(8, 7);
        if (ancestor != 3)
            throw new AssertionError("the value of getAncestorII(8,7) should be 3, but it is:" + ancestor);
        distance = sap.getLengthII(8, 7);
        if (distance != 2)
            throw new AssertionError("the value of distance for getAncestorII(8,7) should be 2, but it is: " + distance);
        ancestor = sap.getAncestorII(0, 0);
        if (ancestor != 0)
            throw new AssertionError("the value of getAncestorII(0, 0) should be 0, but it is: " + ancestor);
        ancestor = sap.getAncestorII(1, 1);
        if (ancestor != 1)
            throw new AssertionError("the value of getAncestorII(1, 1) should be 1, but it is: " + ancestor);
    }
}
