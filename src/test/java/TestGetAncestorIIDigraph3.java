import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class TestGetAncestorIIDigraph3 {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        int ancestor = sap.getAncestorII(12,13);
        if (ancestor != 11)
            throw new AssertionError("the value of ancesttor(12, 13) should be 11 but it is: " + ancestor);
        int distance = sap.getLengthII(12, 13);
        if (distance != 4)
            throw new AssertionError("The length of Minimum Distance between 12 and 13 should be 4, but it is: " + distance);
    }
}
