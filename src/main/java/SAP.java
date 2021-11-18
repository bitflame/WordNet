import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;
import java.util.List;

public class SAP {
    int[] edgeTo;
    boolean[] marked;
    boolean hasCycle;


    // constructor takes a digraph ( not necessarily a DAG )
    public SAP(Digraph digraph) {
        DirectedCycle cycleFinder = new DirectedCycle(digraph);
        if (cycleFinder.hasCycle()) {
            hasCycle = true;
            return;
        }
        edgeTo = new int[digraph.V()];
        for (int v = 0; v < digraph.V(); v++) {
            bfs(digraph, v);
        }
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

    public void bfs(Digraph digraph, int v) {
        marked[v] = true;
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(v);
        while (!queue.isEmpty()) {
            v = queue.dequeue();
            for (int w : digraph.adj(v)) {
                edgeTo[w] = v;
                marked[w] = true;
                queue.enqueue(w);
            }
        }
    }

    private List<Integer> getPath(int from, int to) {
        List<Integer> shortestPath = new ArrayList<>();
        for (int i = edgeTo[from]; i < edgeTo[to]; i = edgeTo[i]) {
            shortestPath.add(i);
        }
        shortestPath.add(to);
        return shortestPath;
    }

    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
    }
}
