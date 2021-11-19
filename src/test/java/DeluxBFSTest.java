import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class DeluxBFSTest {
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        int s = Integer.parseInt(args[1]);
        DeluxBFS bfs = new DeluxBFS(G, s);
        for (int v = 0; v < G.V(); v++) {
            if (bfs.hasPathTo(v)) {
                StdOut.printf("%d to %d (%d): ", s, v, bfs.distTo(v));
                for (int x : bfs.pathTo(v)) {
                    if (x == s) StdOut.print(x);
                    else StdOut.print("->" + x);
                }
                StdOut.println();
            } else {
                StdOut.printf("%d to %d (-): not connected ", s, v);
            }
        }
    }
}
