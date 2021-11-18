import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.List;

public class SAP {
    boolean hasCycle;
    Digraph digraph;
    int [] edgeTo;
    boolean[] marked;

    // constructor takes a digraph ( not necessarily a DAG )
    public SAP(Digraph digraph) {
        DirectedCycle cycleFinder = new DirectedCycle(digraph);
        if (cycleFinder.hasCycle()) {
            hasCycle = true;
            return;
        }
        this.digraph = digraph;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return getPath(v, w).size();
    }

    // length of the shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        List<List<Integer>> paths = new ArrayList<>();
        List<Integer> singlePath = new ArrayList<>();
        for (int i : v) {
            for (int j : w) {
                singlePath = getPath(i, j);
            }
            paths.add(singlePath);
        }
        for (Object obj : paths) {
            ArrayList<Integer> temp = (ArrayList<Integer>) obj;
            if (temp.size() < singlePath.size()) singlePath = temp;
        }
        return singlePath.size();
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return -1; // for now
    }



    private List<Integer> getPath(int from, int to) {
        List<Integer> shortestPath = new ArrayList<>();
        BreadthFirstDirectedPaths breadthFirstDirectedPaths = new BreadthFirstDirectedPaths(digraph, from);
        if (breadthFirstDirectedPaths.hasPathTo(to)){
            for (int i:breadthFirstDirectedPaths.pathTo(to)) {
                shortestPath.add(i);
            }
        }
        return shortestPath;
    }

    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        StdOut.println("Shortest path from 1 to 2 is expected to be 0, and it is: " + sap.getPath(1, 2));
        StdOut.println("Shortest path from 3 to 4 is expected to be 1, and it is: " + sap.getPath(3, 4));
        StdOut.println("Shortest path from 4 to 3 is expected to be 1, and it is: " + sap.getPath(4, 3));
        StdOut.println("Shortest path from 5 to 6 is expected to be 2, and it is: " + sap.getPath(5, 6));
        StdOut.println("Shortest path from 10 to 6 is expected to be [ 5 2 ], and it is: " + sap.getPath(10, 6));
        StdOut.println("Shortest path from 17 to 6 is expected to be [ 10 5 2 ], and it is: " + sap.getPath(17, 6));
    }
}
