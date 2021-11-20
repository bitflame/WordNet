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
        List<Integer> sources = new ArrayList<Integer>(Arrays.asList(from, to));
        DeluxBFS deluxBFS = new DeluxBFS(digraph, sources);
        for (int i : deluxBFS.pathTo(from)) {
            shortestPath.add(i);
        }
        for (int j : deluxBFS.pathTo(to)) {
            if (!shortestPath.contains(j)) shortestPath.add(j);
        }
        return shortestPath;
        //return shortestPath;
    }

    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);


        System.out.print("The path between 1 and 2 should be [0 1 2] ");
        for (int i : sap.getPath(1, 2)) {
            System.out.println("[" + i + "]");
        }

        System.out.print("The path between 3 and 4 should be [1 3 4] ");
        for (int i : sap.getPath(3, 4)) {
            System.out.println("[" + i + "]");
        }
        System.out.print("The path between 4 and 3 should be [1 3 4] ");
        for (int i : sap.getPath(4, 3)) {
            System.out.println("[" + i + "]");
        }

        System.out.print("The path between 5 and 6 should be [5 2 6] ");
        for (int i : sap.getPath(5, 6)) {
            System.out.println("[" + i + "]");
        }
        System.out.print("The path between 6 and 5 should be [ 5 2 6] ");
        for (int i : sap.getPath(6, 5)) {
            System.out.println("[" + i + "]");
        }
        System.out.print("The path between 4 and 6 should be: [ 4 0 1 2 6 ] ");
        System.out.print("[");
        for (int i : sap.getPath(4, 6)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
//        while (!StdIn.isEmpty()) {
//            int v = StdIn.readInt();
//            int w = StdIn.readInt();
//            int length = sap.length(v, w);
//            //int ancestor = sap.ancestor((v,w));
//            StdOut.printf("length = %d\n", length);
//        }
    }
}
