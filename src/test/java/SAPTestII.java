import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SAPTestII {
    Digraph digraph1 = new Digraph(new In("digraph1.txt"));
    Digraph digraph2 = new Digraph(new In("digraph2.txt"));
    SAP sap1 = new SAP(digraph1);
    SAP sap2 = new SAP(digraph2);

    @Test
    void length() {
        // Assertions.assertTrue(sap1.length(1, 0) == 1);
        Assertions.assertTrue(sap2.length(1, 5) == 2);
    }
}