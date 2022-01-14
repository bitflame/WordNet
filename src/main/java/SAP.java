import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class SAP {
    private boolean hasCycle = false;
    private final Digraph digraphDFCopy;
    private int ancestor;
    private int minDistance;
    private Stack<Integer> path;
    private int from;
    private int to;
    private int n;
    private boolean[] marked;
    private int[] edgeTo;
    private int[] disTo;
    Topological topological;
    private static final int INFINITY = Integer.MAX_VALUE;

    private static class DeluxeBFS {
        private final boolean[] marked;
        private final int[] edgeTo;
        private final int[] distTo;

        public DeluxeBFS(Digraph G, int s) {
            marked = new boolean[G.V()];
            distTo = new int[G.V()];
            edgeTo = new int[G.V()];

            for (int v = 0; v < G.V(); v++)
                distTo[v] = INFINITY;
            validateVertex(s);
            bfs(G, s);
        }

        public DeluxeBFS(Digraph G, Iterable<Integer> sources) {
            marked = new boolean[G.V()];
            distTo = new int[G.V()];
            edgeTo = new int[G.V()];

            for (int v = 0; v < G.V(); v++)
                distTo[v] = INFINITY;
            validateVertices(sources);
            bfs(G, sources);
        }


        private void bfs(Digraph G, int s) {
            Queue<Integer> q = new Queue<>();
            marked[s] = true;
            distTo[s] = 0;
            q.enqueue(s);
            while (!q.isEmpty()) {
                int v = q.dequeue();
                for (int w : G.adj(v)) {
                    if (!marked[w]) {
                        edgeTo[w] = v;
                        distTo[w] = distTo[v] + 1;
                        marked[w] = true;
                        q.enqueue(w);
                    }
                }
            }
        }

        private void bfs(Digraph G, Iterable<Integer> sources) {
            Queue<Integer> q = new Queue<>();
            for (int s : sources) {
                marked[s] = true;
                distTo[s] = 0;
                q.enqueue(s);
            }
            while (!q.isEmpty()) {
                int v = q.dequeue();
                for (int w : G.adj(v)) {
                    if (!marked[w]) {
                        edgeTo[w] = v;
                        distTo[w] = distTo[v] + 1;
                        marked[w] = true;
                        q.enqueue(w);
                    }
                }
            }
        }

        public boolean hasPathTo(int v) {
            validateVertex(v);
            return marked[v];
        }

        public int distTo(int v) {
            validateVertex(v);
            return distTo[v];
        }

        public Iterable<Integer> pathTo(int v) {
            validateVertex(v);
            if (!hasPathTo(v)) return null;
            Stack<Integer> path = new Stack<>();
            int x;
            for (x = v; distTo[x] != 0; x = edgeTo[x])
                path.push(x);
            path.push(x);
            return path;
        }


        private void validateVertex(int v) {
            int V = marked.length;
            if (v < 0 || v >= V)
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }

        private void validateVertices(Iterable<Integer> vertices) {
            if (vertices == null) {
                throw new IllegalArgumentException("argument is null");
            }
            int V = marked.length;
            int count = 0;
            for (Integer v : vertices) {
                count++;
                if (v == null) {
                    throw new IllegalArgumentException("vertex is null");
                }
                validateVertex(v);
            }
            if (count == 0) {
                throw new IllegalArgumentException("zero vertices");
            }
        }
    }

    // constructor takes a digraph ( not necessarily a DAG )
    public SAP(Digraph digraph) {
        if (digraph == null) throw new IllegalArgumentException("Digraph value can not be null");
        DirectedCycle cycleFinder = new DirectedCycle(digraph);
        if (cycleFinder.hasCycle()) {
            hasCycle = true;
        }
        digraphDFCopy = digraph;
        minDistance = -1;
        ancestor = -1;
    }

    // length of the shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v == from && w == to) return minDistance;
        if (v == w) {
            ancestor = v;
            return minDistance = 0;
        }
        ancestor(v, w);
        /*
        if (ancestor == -1) return minDistance;
        DeluxeBFS fromBFS = new DeluxeBFS(digraphDFCopy, v);
        DeluxeBFS toBFS = new DeluxeBFS(digraphDFCopy, w);
        minDistance = fromBFS.distTo(ancestor) + toBFS.distTo(ancestor);
        */
        return minDistance;
    }

    // length of the shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Iterable value to SAP.length() can not be null.");

        ancestor(v, w);
        return minDistance;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (this.from == v && this.to == w) return ancestor;
        if (v == w) {
            minDistance = 0;
            ancestor = v;
            return ancestor;
        }
        from = v;
        to = w;
        ancestor = -1;
        minDistance = -1;
        if ((digraphDFCopy.indegree(w) == 0 && digraphDFCopy.outdegree(w) == 0) || (digraphDFCopy.indegree(v) == 0 && digraphDFCopy.outdegree(v) == 0))
            return ancestor;
        n = digraphDFCopy.V();
        marked = new boolean[n];
        edgeTo = new int[n];
        disTo = new int[n];
        path = new Stack<>();
        for (int i = 0; i < n; i++) {
            disTo[i] = INFINITY;
        }
        lockStepBFS(from, to);
        return ancestor;
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Iterable value to SAP.ancestor() can not be null.");
        minDistance = -1;
        ancestor = -1;
        for (int i : v) {
            for (int j : w) {
                ancestor(i, j);
            }
        }
        return ancestor;
    }

    private Stack<Integer> getPath(int from, int to) {
        ancestor(from, to);
        return path;
    }


    public int getAncestorII(int from, int to) {
        if (this.from == from && this.to == to) return ancestor;
        if (from == to) {
            minDistance = 0;
            return ancestor = from;
        }
        ancestor = -1;
        minDistance = -1;
        this.from = from;
        this.to = to;
        n = digraphDFCopy.V();
        marked = new boolean[n];
        edgeTo = new int[n];
        disTo = new int[n];
        for (int i = 0; i < n; i++) {
            disTo[i] = INFINITY;
        }
        lockStepBFS(from, to);
        return ancestor;
    }

    public int getLengthII(int f, int t) {
        if (f != from && t != to) {
            getAncestorII(f, t);
        }
        return minDistance;
    }

    private void lockStepBFS(int f, int t) {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(f);
        queue.enqueue(t);
        marked[f] = true;
        path.push(f);
        disTo[f] = 0;
        marked[t] = true;
        path.push(t);
        minDistance = 1;
        disTo[t] = 0;
        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            int w = queue.dequeue();
            for (int j : digraphDFCopy.adj(v)) {
                if (j == w) {
                    ancestor = j;
                    minDistance = 1;
                    while (!path.isEmpty()) {
                        minDistance += disTo[path.pop()];
                    }
                    return;
                } else if (!marked[j]) {
                    edgeTo[j] = v;
                    marked[j] = true;
                    disTo[j] = disTo[v] + 1;
                    queue.enqueue(j);
                } else {
                    ancestor = j;
                    minDistance = 0;
                    while (!path.isEmpty()) {
                        minDistance += disTo[path.pop()];
                    }
                    return;
                }
                path.push(j);
            }
            for (int k : digraphDFCopy.adj(w)) {
                if (!marked[k]) {
                    edgeTo[k] = v;
                    marked[k] = true;
                    disTo[k] = disTo[v] + 1;
                    queue.enqueue(k);
                } else {
                    ancestor = k;
                    minDistance = 0;
                    while (!path.isEmpty()) {
                        minDistance += disTo[path.pop()];
                    }
                    return;
                }
                path.push(k);
            }

        }
    }


    public static void main(String[] args) {
        /*
        System.out.println(sap.ancestor(1, 2));
        System.out.println(sap.ancestor(0, 2));
        System.out.println(sap.ancestor(0, 1));
        System.out.println(sap.ancestor(0, 10));
        sap.ancestor(1, 2);
        sap.ancestor(0, 24);
        [13, 23, 24] | [6, 16, 17] | [3]
        digraph = new Digraph(new In(new File("src/main/resources/digraph1.txt")));
        sap = new SAP(digraph);
        System.out.println("Here is result of 1 and 6: " + sap.ancestor(1, 6));
        Digraph digraph = new Digraph(new In(new File("src/main/resources/digraph-ambiguous-ancestor.txt")));
        SAP sap = new SAP(digraph);
        */
        /* Reading in digraph9.txt here */
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        // SAP.DeluxBFS deluxBFS = sap.new DeluxBFS(digraph,Arrays.asList(7,4));
        System.out.println("******************Testing getAncestorII ******************************");
        System.out.println("ancestor between 7 and 6 in digraph9 should be 6: " + sap.getAncestorII(7, 6));
        StdOut.println("The minimum distance between 7 and 6 should be 1: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 6 and 7 in digraph9 should be 6: " + sap.getAncestorII(6, 7));
        System.out.println("The minimum distance between 6 and 7 should be 1: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 3 in digraph9 should be 3: " + sap.getAncestorII(7, 3));
        StdOut.println("The minimum distance between 7 and 3 should be 2: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 3 and 7 in digraph9 should be 3: " + sap.getAncestorII(3, 7));
        System.out.println("The minimum distance between 6 and 7 should be 2: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 2 in digraph9 should be 2: " + sap.getAncestorII(7, 2));
        StdOut.println("The minimum distance between 7 and 2 should be 3: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 2 and 7 in digraph9 should be 2: " + sap.getAncestorII(2, 7));
        System.out.println("The minimum distance between 2 and 7 should be 3: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 0 in digraph9 should be 6: " + sap.getAncestorII(7, 0));
        StdOut.println("The minimum distance between 7 and 0 should be 2: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 0 and 7 in digraph9 should be 6: " + sap.getAncestorII(0, 7));
        System.out.println("The minimum distance between 0 and 7 should be 2: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 1 in digraph9 should be 3: " + sap.getAncestorII(7, 1));
        StdOut.println("The minimum distance between 7 and 1 should be 3: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 1 and 7 in digraph9 should be 3: " + sap.getAncestorII(1, 7));
        System.out.println("The minimum distance between 1 and 7 should be 3: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 4 in digraph9 should be 4: " + sap.getAncestorII(7, 4));
        StdOut.println("The minimum distance between 7 and 4 should be 3: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 4 and 7 in digraph9 should be 4: " + sap.getAncestorII(4, 7));
        System.out.println("The minimum distance between 4 and 7 should be 3: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 5 in digraph9 should be 4: " + sap.getAncestorII(7, 5));
        StdOut.println("The minimum distance between 7 and 5 should be 4: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 5 and 7 in digraph9 should be 4: " + sap.getAncestorII(5, 7));
        System.out.println("The minimum distance between 5 and 7 should be 4: " + sap.minDistance);


        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 8 in digraph9 should be -1: " + sap.getAncestorII(7, 8));
        StdOut.println("The minimum distance between 7 and 8 should be 2147483647: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 8 and 7 in digraph9 should be -1: " + sap.getAncestorII(8, 7));
        System.out.println("The minimum distance between 8 and 7 should be 2147483647: " + sap.minDistance);

        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 7 in digraph9 should be 7: " + sap.getAncestorII(7, 7));
        StdOut.println("The minimum distance between 7 and 7 should be 0: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 8 and 8 in digraph9 should be 8: " + sap.getAncestorII(8, 8));
        System.out.println("The minimum distance between 8 and 8 should be 0: " + sap.minDistance);

        System.out.println("********************************* Testing getAncestorImproved() here ******************");
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 6 in digraph9 should be 6: " + sap.ancestor(7, 6));
        StdOut.println("The minimum distance between 7 and 6 should be 1: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 6 and 7 in digraph9 should be 6: " + sap.ancestor(6, 7));
        System.out.println("The minimum distance between 6 and 7 should be 1: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 3 in digraph9 should be 3: " + sap.ancestor(7, 3));
        StdOut.println("The minimum distance between 7 and 3 should be 2: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 3 and 7 in digraph9 should be 3: " + sap.ancestor(3, 7));
        System.out.println("The minimum distance between 3 and 7 should be 2: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 2 in digraph9 should be 2: " + sap.ancestor(7, 2));
        StdOut.println("The minimum distance between 7 and 2 should be 3: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 2 and 7 in digraph9 should be 2: " + sap.ancestor(2, 7));
        System.out.println("The minimum distance between 2 and 7 should be 3: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 0 in digraph9 should be 6: " + sap.ancestor(7, 0));
        StdOut.println("The minimum distance between 7 and 0 should be 2: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 0 and 7 in digraph9 should be 6: " + sap.ancestor(0, 7));
        System.out.println("The minimum distance between 0 and 7 should be 2: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 1 in digraph9 should be 3: " + sap.ancestor(7, 1));
        StdOut.println("The minimum distance between 7 and 1 should be 3: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 1 and 7 in digraph9 should be 3: " + sap.ancestor(1, 7));
        System.out.println("The minimum distance between 1 and 7 should be 3: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 4 in digraph9 should be 4: " + sap.ancestor(7, 4));
        StdOut.println("The minimum distance between 7 and 4 should be 3: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 4 and 7 in digraph9 should be 4: " + sap.ancestor(4, 7));
        System.out.println("The minimum distance between 4 and 7 should be 3: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 5 in digraph9 should be 4: " + sap.ancestor(7, 5));
        StdOut.println("The minimum distance between 7 and 5 should be 4: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 5 and 7 in digraph9 should be 4: " + sap.ancestor(5, 7));
        System.out.println("The minimum distance between 5 and 7 should be 4: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 8 in digraph9 should be -1: " + sap.ancestor(7, 8));
        StdOut.println("The minimum distance between 7 and 8 should be 2147483647: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 8 and 7 in digraph9 should be -1: " + sap.ancestor(8, 7));
        System.out.println("The minimum distance between 8 and 7 should be 2147483647: " + sap.minDistance);
        System.out.println();

        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 7 in digraph9 should be 7: " + sap.ancestor(7, 7));
        StdOut.println("The minimum distance between 7 and 7 should be 0: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 8 and 8 in digraph9 should be 8: " + sap.ancestor(8, 8));
        System.out.println("The minimum distance between 8 and 8 should be 0: " + sap.minDistance);
        System.out.println();

        /********************************* Reading in digraph25.txt here ******************/
        digraph = new Digraph(new In("digraph25.txt"));
        sap = new SAP(digraph);
        System.out.print("The path between 2 and 0 should be: [ 0 2 ] ");
        System.out.print("[");
        for (int i : sap.getPath(2, 0)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(2, 0));
        System.out.println();
        System.out.print("The path between 1 and 2 should be [0 1 2] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(1, 2)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(1, 2));
        System.out.println();
        System.out.print("The path between 3 and 4 should be [1 3 4] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(3, 4)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(3, 4));
        System.out.println();
        System.out.print("The path between 4 and 3 should be [1 3 4] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(4, 3)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(4, 3));
        System.out.println();
        System.out.print("The path between 5 and 6 should be [5 2 6] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(5, 6)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(5, 6));
        System.out.println();
        System.out.print("The path between 6 and 5 should be [ 2 5  6] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(6, 5)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(6, 5));
        System.out.println();
        System.out.print("The path between 4 and 6 should be: [  0 1 2 4 6 ] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(4, 6)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(4, 6));
        System.out.println();
        System.out.print("The path between 1 and 6 should be: [  0 1 2 6 ] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(1, 6)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(1, 6));
        System.out.println();
        System.out.print("The path between 17 and 24 should be: [  5 10 12 17 20 24 ] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(17, 24)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(17, 24));
        System.out.println();
        System.out.print("The path between 23 and 24 should be: [ 24 23 20 ] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(23, 24)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();
        System.out.println("And the ancestor is : " + sap.ancestor(23, 24));
        System.out.println();
        System.out.print("The path between 11 and 4 should be: [  11 5 4 2 1 0 ] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(11, 4)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(11, 4));
        System.out.println();
        System.out.print("The path between 17 and 19 should be: [ 17 5 10 12 19 ] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(17, 19)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(17, 19));
        System.out.println();
        System.out.print("The path between 17 and 17 should be: [ 17 ] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(17, 17)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        sap = new SAP(digraph);
        System.out.println("And the ancestor is : " + sap.ancestor(17, 17));
        System.out.println();
        sap = new SAP(digraph);
        List<Integer> one = new ArrayList<>(Arrays.asList(13, 23, 24));
        List<Integer> two = new ArrayList<>(Arrays.asList(6, 16, 17));
        System.out.println("==========================================================================================");
        System.out.println("The shortest common ancestor in above sets should be 3, and it is: " + sap.ancestor(one, two));
        System.out.println("==========================================================================================");
        digraph = new Digraph(new In(new File("src/main/resources/digraph1.txt")));
        sap = new SAP(digraph);
        System.out.print("The path between 10 and 4 should be: [ 4 1 5 10 ] ");
        System.out.print("[");
        for (int i : sap.getPath(4, 10)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();

        sap = new SAP(digraph);
        System.out.print("The path between 7 and 2 should be: [ 0 1 2 3 7 ] ");
        System.out.print("[");
        for (int i : sap.getPath(7, 2)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();

        sap = new SAP(digraph);
        System.out.println("ancestor should return 1 for values 3 and 11: " + sap.ancestor(3, 11));
        System.out.println("********************************* Ambiguous tests ***************************************");
        digraph = new Digraph(new In(new File("src/main/resources/digraph-ambiguous-ancestor.txt")));
        sap = new SAP(digraph);
        System.out.print("The shortest path between 1 and 2 - in ambiguous-ancestor is [1 2]");
        System.out.print("[");
        // test 1 and 2 for ambiguous-ancestor
        for (int i : sap.getPath(1, 2)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(1, 2));
        System.out.println();
        System.out.print("The shortest path between 0 and 2 - in ambiguous-ancestor is [0 1 2]");
        System.out.print("[");
        // test 0 and 2 for ambiguous-ancestor
        sap = new SAP(digraph);
        for (int i : sap.getPath(0, 2)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(0, 2));
        System.out.println();
        System.out.print("The shortest path between 9 and 5 - in ambiguous-ancestor is [] - an empty set. There is not path : ");
        System.out.print("[");
        // find the shortest path between 9 and 5 for ambiguous-ancestor
        sap = new SAP(digraph);
        for (int i : sap.getPath(9, 5)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();
        System.out.print("The path between 27 and 0 should be: Exception ");
        sap = new SAP(digraph);
        System.out.print("[");
        try {
            for (int i : sap.getPath(27, 0)) {
                System.out.print(" " + i + " ");
            }
            System.out.println("]");
            System.out.println();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        digraph = new Digraph(new In(new File("src/main/resources/simplecycle.txt")));
        sap = new SAP(digraph);
        System.out.println("Expecting this to be true for simplecycle.txt: " + sap.hasCycle);
    }
}
