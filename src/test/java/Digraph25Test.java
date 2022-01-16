import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class Digraph25Test {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        int result = sap.length(13, 16);
        if (result != 4) throw new AssertionError("The length of the minimum distance between 13 and 16 " +
                "should be 4, but it is: " + result);
        result = sap.length(13, 6);
        if (result != 6) throw new AssertionError("The length of the minimum distance between 13 and 6 " +
                "should be 6, but is: " + result);
    }
}
