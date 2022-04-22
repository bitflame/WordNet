import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class SAPTest {
    Digraph digraph = new Digraph(new In("digraph3.txt"));
    SAP sap = new SAP(digraph);

    public static void main(String[] args) {

        //System.out.println(sap.length(1, 5));
    }

    @Test
    void length() {
        Assertions.assertTrue(sap.length(1, 8) == -1);
    }

    @Test
    void testLength() {
    }

    @Test
    void ancestor() {
    }

    @Test
    void testAncestor() {
    }

    @Test
    void testMain() {
    }
}