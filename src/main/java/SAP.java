import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SAP {
    boolean hasCycle;
    Digraph digraph;
    int[] edgeTo;
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
        List<Integer> sources = new ArrayList<>(Arrays.asList(from, to));
        // get the path from each point to other points in the graph starting from zero, and collect only the
        // nodes common in both paths, and the least distance
        // If there are two of them add both, if there is only one add it and return shortest path
        // List of node, and distance
        DeluxBFS pathToFrom = new DeluxBFS(digraph, from);
        DeluxBFS pathToTo = new DeluxBFS(digraph, to);
        for (int i = 0; i < digraph.V(); i++) {
            for (int j = 0; j < digraph.V(); j++) {
                if (pathToFrom.hasPathTo(i) && pathToTo.hasPathTo(j) && pathToFrom.distTo(i) == pathToTo.distTo(j)) {
                    if (!shortestPath.contains(i)) shortestPath.add(i);
                    if (!shortestPath.contains(j)) shortestPath.add(j);
                }
            }
        }
        return shortestPath;
    }

    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);


        System.out.print("The path between 1 and 2 should be [0 1 2] ");
        System.out.print("[");
        for (int i : sap.getPath(1, 2)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();
        System.out.print("The path between 3 and 4 should be [1 3 4] ");
        System.out.print("[");
        for (int i : sap.getPath(3, 4)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();
        System.out.print("The path between 4 and 3 should be [1 3 4] ");
        System.out.print("[");
        for (int i : sap.getPath(4, 3)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();
        System.out.print("The path between 5 and 6 should be [5 2 6] ");
        System.out.print("[");
        for (int i : sap.getPath(5, 6)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();
        System.out.print("The path between 6 and 5 should be [ 5 2 6] ");
        System.out.print("[");
        for (int i : sap.getPath(6, 5)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();
        System.out.print("The path between 4 and 6 should be: [ 4 0 1 2 6 ] ");
        System.out.print("[");
        for (int i : sap.getPath(4, 6)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();
//        while (!StdIn.isEmpty()) {
//            int v = StdIn.readInt();
//            int w = StdIn.readInt();
//            int length = sap.length(v, w);
//            //int ancestor = sap.ancestor((v,w));
//            StdOut.printf("length = %d\n", length);
//        }
    }
}
