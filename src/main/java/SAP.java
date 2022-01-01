import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Arrays;

public class SAP {
    private boolean hasCycle = false;
    private final Digraph digraphDFCopy;
    private int ancestor = -1;
    private int minDistance = -1;
    private List<Integer> path;
    Stack<Integer> stack;

    boolean[] visited;
    int[] low;
    boolean[] onStack;


    private static class DeluxeBFS {
        private static final int INFINITY = Integer.MAX_VALUE;
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
    }

    // length of the shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        ancestor(v, w);
        return minDistance;
    }

    private boolean nodeExists(int h) {
        boolean exists = false;
        for (int i = 0; i < digraphDFCopy.V(); i++) {
            if (h == i) exists = true;
        }
        return exists;
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
        // StdOut.printf("from: %d to: %d v: %d w: %d at the beginning of call to ancestor. ", from, to, v, w);
        if (!nodeExists(v) || !nodeExists(w)) return ancestor;
        DeluxeBFS fromBFS = new DeluxeBFS(digraphDFCopy, v);
        DeluxeBFS toBFS = new DeluxeBFS(digraphDFCopy, w);
        List<Integer> fromList = new ArrayList<>();
        List<Integer> toList = new ArrayList<>();
        for (int i = 0; i < digraphDFCopy.V(); i++) {
            if (fromBFS.hasPathTo(i)) {
                fromList.add(i);
                // System.out.println("Here is the node: "+i+"Here is its distance from fromNode: "+fromBFS.distTo(i));
            }
            if (toBFS.hasPathTo(i)) {
                toList.add(i);
                // System.out.println("Here is the node: "+i+"Here is its distance to toNode: "+toBFS.distTo(i));
            }
        }
        // StdOut.printf("The size of from_list and to_list before sort: %d %d\n",fromList.size(),toList.size());
        fromList.sort(Comparator.comparingInt(fromBFS::distTo));
        toList.sort(Comparator.comparingInt(toBFS::distTo));

        int from;
        int to;
        int fromCounter = 0;
        int toCounter = 0;
        boolean[] onStack = new boolean[digraphDFCopy.V()];
        Stack<Integer> fromStack = new Stack<>();
        Stack<Integer> toStack = new Stack<>();
        for (int dist = 0; dist < digraphDFCopy.V(); dist++) {
            while (fromBFS.distTo(fromList.get(fromCounter)) == dist) {
                fromStack.push(fromList.get(fromCounter));
                onStack[fromList.get(fromCounter)] = true;
            }
            // if to equals from or anything before it, or from equals to or anything before it
            if (toBFS.distTo(toList.get(dist)) == dist) to = toList.get(dist);
        }
        return ancestor;
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Iterable value to SAP.ancestor() can not be null.");
        int distance = Integer.MAX_VALUE;
        int currentAncestor = -1;

        for (int i : v) {
            for (int j : w) {
                //StdOut.printf("Calling ancestor(%d, %d) ", i, j);
                ancestor(i, j);
                if (distance > minDistance) {
                    distance = minDistance;
                    currentAncestor = ancestor;
                }
            }
        }
        minDistance = distance;
        ancestor = currentAncestor;
        return ancestor;
    }

    private List<Integer> getPath(int from, int to) {
        ancestor(from, to);
        return path;
    }

    public int getAncestorII(int from, int to) {
        DeluxeBFS fromBFS = new DeluxeBFS(digraphDFCopy, from);
        DeluxeBFS toBFS = new DeluxeBFS(digraphDFCopy, to);
        List<Integer> fromList = new ArrayList<>();
        List<Integer> toList = new ArrayList<>();
        for (int i = 0; i < digraphDFCopy.V(); i++) {
            if (fromBFS.hasPathTo(i)) {
                fromList.add(i);
                // System.out.println("Here is the node: "+i+"Here is its distance from fromNode: "+fromBFS.distTo(i));
            }
            if (toBFS.hasPathTo(i)) {
                toList.add(i);
                // System.out.println("Here is the node: "+i+"Here is its distance to toNode: "+toBFS.distTo(i));
            }
        }
        // StdOut.printf("The size of from_list and to_list before sort: %d %d\n",fromList.size(),toList.size());
        fromList.sort(Comparator.comparingInt(fromBFS::distTo));
        toList.sort(Comparator.comparingInt(toBFS::distTo));
        path = new ArrayList<>();
        low = new int[digraphDFCopy.V()];
        visited = new boolean[digraphDFCopy.V()];
        onStack = new boolean[digraphDFCopy.V()];
        stack = new Stack<>();
        int next = 0;
        int dist = 0;
        while (dist < digraphDFCopy.V()) {

            while (fromBFS.distTo(fromList.iterator().next()) == dist) {
                next = fromList.iterator().next();
                fromList.remove(0);
                if (!visited[next]) visited[next] = true;
                else {
                    ancestor = next;
                    minDistance = fromBFS.distTo[next] + toBFS.distTo[next];
                    return ancestor;
                }
            }
            while (!toList.isEmpty() && toBFS.distTo(toList.iterator().next()) == dist) {
                next = toList.iterator().next();
                toList.remove(0);
                if (!visited[next]) visited[next] = true;
                else {
                    ancestor = next;
                    minDistance = fromBFS.distTo[next] + toBFS.distTo[next];
                    return ancestor;
                }
            }
            dist++;
        }
        return ancestor;
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
