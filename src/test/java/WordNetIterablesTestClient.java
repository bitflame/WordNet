import edu.princeton.cs.algs4.*;

public class WordNetIterablesTestClient {
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        Queue<Integer> v = new Queue<>();
        Queue<Integer> w = new Queue<>();

        while (!StdIn.isEmpty()) {
            int numV = StdIn.readInt();
            int numW = StdIn.readInt();

            for (int i = 0; i < numV; i++)
                v.enqueue(StdIn.readInt());
            for (int i = 0; i < numW; i++)
                w.enqueue(StdIn.readInt());

            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
