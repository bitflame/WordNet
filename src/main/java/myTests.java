import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;


public class myTests {
    In in = new In("tinyDG.txt");
    Digraph digraph = new Digraph(in);
    BreadthFirstDirectedPaths breadthFirstDirectedPaths;
    public void myMethod(int s) {
        breadthFirstDirectedPaths = new BreadthFirstDirectedPaths(digraph, s);
        for (int v = 0; v < digraph.V(); v++) {
            if (breadthFirstDirectedPaths.hasPathTo(v)) {
                System.out.printf("%d to %d (%d) :", s, v, breadthFirstDirectedPaths.distTo(v));
                for (int x : breadthFirstDirectedPaths.pathTo(v)) {
                    if (x == s) StdOut.print(x);
                    else StdOut.print("->" + x);
                }
                StdOut.println();
            } else {
                StdOut.printf("%d to %d (-): not connected\n ", s, v);
            }
        }
    }
public void myMethod(Iterable<Integer> sources){
    breadthFirstDirectedPaths = new BreadthFirstDirectedPaths(digraph, sources);
    for(int s: sources){
        for (int v = 0; v < digraph.V(); v++) {
            if (breadthFirstDirectedPaths.hasPathTo(v)) {
                System.out.printf("%d to %d (%d) :", s, v, breadthFirstDirectedPaths.distTo(v));
                for (int x : breadthFirstDirectedPaths.pathTo(v)) {
                    if (x == s) StdOut.print(x);
                    else StdOut.print("->" + x);
                }
                StdOut.println();
            } else {
                StdOut.printf("%d to %d (-): not connected\n ", s, v);
            }
        }
    }
}
    public static void main(String[] args) {
        myTests mt = new myTests();
        mt.myMethod(3);
        Iterable<Integer> v = new ArrayList<>(Arrays.asList(3,2));
        mt.myMethod(v);
    }
}
