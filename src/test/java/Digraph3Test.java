import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class Digraph3Test {
    public static void main(String[] args) {
        System.out.println("----------------------Running Digraph3Test---------------------");
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        int distance = sap.length(10, 9);
        if (distance != 1)
            throw new AssertionError("The first time running length() for Digraph3Test. The value of " +
                    "length(10, 9) should be 1 but it is: " + distance);
        distance = sap.length(12, 13);
        if (distance != 4) throw new AssertionError("The length between 12 and 13 should be 4, but it is: " + distance);
    }
}
