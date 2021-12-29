import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

class SAPTest {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        System.out.println(sap.length(1, 5));
    }
}