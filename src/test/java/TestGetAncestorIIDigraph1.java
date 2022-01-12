import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class TestGetAncestorIIDigraph1 {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        int ancestor = sap.getAncestorII(2, 0);
        if (ancestor != 0)
            throw new AssertionError("the value of ancesttor(2, 0) should be 0 but it is: " + ancestor);
        int distance = sap.getLengthII(2, 0);
        if (distance != 1)
            throw new AssertionError("the value of distance for getAncestorII should be 1, but it is: " + distance);
        ancestor = sap.getAncestorII(1, 0);
        if (ancestor != 0) throw new AssertionError("the value of ancestor(1,0) should be 0 but it is: " + ancestor);

    }
}
