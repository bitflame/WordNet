import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class BreadthFirstDirectedPathsTest {
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        List<Integer> sources = new ArrayList<>();
        sources.add(Integer.parseInt(args[1]));
        sources.add(Integer.parseInt(args[2]));
        BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(G, sources);
        for(int s: sources){
            for (int v = 0; v < G.V(); v++) {
                if (bfs.hasPathTo(v)) {
                    StdOut.printf("%d to %d (%d): ", s, v, bfs.distTo(v));
                    for (int x : bfs.pathTo(v)) {
                        if (x == s) StdOut.print(x);
                        else StdOut.print("->" + x);
                    }
                    StdOut.println();
                } else {
                    StdOut.printf("%d to %d (-): not connected \n", s, v);
                }
            }
        }
    }
}
