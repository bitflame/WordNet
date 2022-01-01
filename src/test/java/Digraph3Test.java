import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class Digraph3Test {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        int distance = sap.length(10, 7);
        if (distance != 3)
            throw new AssertionError("The first time running length() for Digraph3Test. The value of " +
                    "length(10, 7) should be 3 but it is: " + distance);
        args = new String[]{"digraph1.txt"};
        Digraph1Test digraph1Test = new Digraph1Test();
        digraph1Test.main(args);
        args = new String[]{"digraph2.txt"};
        Digraph2Test digraph2Test = new Digraph2Test();
        digraph2Test.main(args);
        args = new String[]{"digraph3.txt"};
        distance = sap.length(10, 7);
        if (distance != 3)
            throw new AssertionError("The second time running length() for Digraph3Test. the value of " +
                    "length(10, 7) should be 3 but it is: " + distance);
    }
}
