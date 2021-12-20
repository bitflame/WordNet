import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.Arrays;

public class SAP {
    private boolean hasCycle = false;
    private final Digraph digraphDFCopy;
    private int ancestor = -1;
    private int from;
    private int to;
    private List<Integer> shortPath;
    private boolean[] onStack;
    private boolean[] marked;
    private boolean stop = false;
    private int minDistance = Integer.MAX_VALUE;
    List<Integer> path;
    private class DeluxBFS {
        private static final int INFINITY = Integer.MAX_VALUE;
        private boolean[] marked;
        private int[] edgeTo;
        private int[] distTo;


        public DeluxBFS(Digraph G, int s) {
            marked = new boolean[G.V()];
            distTo = new int[G.V()];
            edgeTo = new int[G.V()];

            for (int v = 0; v < G.V(); v++)
                distTo[v] = INFINITY;
            validateVertex(s);
            bfs(G, s);
        }

        public DeluxBFS(Digraph G, Iterable<Integer> sources) {
            marked = new boolean[G.V()];
            distTo = new int[G.V()];
            edgeTo = new int[G.V()];

            for (int v = 0; v < G.V(); v++)
                distTo[v] = INFINITY;
            validateVertices(sources);
            bfs(G, sources);
        }


        private void bfs(Digraph G, int s) {
            Queue<Integer> q = new Queue<Integer>();
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
            Queue<Integer> q = new Queue<Integer>();
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
            Stack<Integer> path = new Stack<Integer>();
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
            for (int v : vertices) {
                if (v < 0 || v >= V) {
                    throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
                }
            }
        }
    }

    private class Node {
        private final int id;
        private final Node prevNode;
        private final int movesTaken;
        private final int movesRemaining;

        public Node(int id, Node prevNode, int taken, int remaining) {
            this.id = id;
            this.prevNode = prevNode;
            this.movesTaken = taken;
            this.movesRemaining = remaining;
        }
    }

    // constructor takes a digraph ( not necessarily a DAG )
    public SAP(Digraph digraph) {
        if (digraph == null) throw new IllegalArgumentException("Digraph value can not be null");
        DirectedCycle cycleFinder = new DirectedCycle(digraph);
        onStack = new boolean[digraph.V()];
        marked = new boolean[digraph.V()];
        if (cycleFinder.hasCycle()) {
            hasCycle = true;
        }
        digraphDFCopy = digraph;
    }

    // length of the shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return minDistance;
    }

    // length of the shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Iterable value to SAP.length() can not be null.");
        ancestor(v,w);
        return minDistance;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        //StdOut.printf("from: %d to: %d v: %d w: %d at the beginning of call to ancestor. ", from, to, v, w);
        DeluxBFS fromBFS = new DeluxBFS(digraphDFCopy, v);
        DeluxBFS toBFS = new DeluxBFS(digraphDFCopy, w);
        List<Integer> fromList = new ArrayList<>();
        List<Integer> toList = new ArrayList<>();
        for (int i = 0; i < digraphDFCopy.V(); i++) {
            if (fromBFS.hasPathTo(i)) {
                fromList.add(i);
                //System.out.println("Here is the node: "+i+"Here is its distance from fromNode: "+fromBFS.distTo(i));
            }
            if (toBFS.hasPathTo(i)) {
                toList.add(i);
                //System.out.println("Here is the node: "+i+"Here is its distance to toNode: "+toBFS.distTo(i));
            }
        }
        //StdOut.printf("The size of from_list and to_list before sort: %d %d\n",fromList.size(),toList.size());
        Collections.sort(fromList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (fromBFS.distTo(o1) > fromBFS.distTo(o2)) return 1;
                if (fromBFS.distTo(o2) > fromBFS.distTo(o1)) return -1;
                return 0;
            }
        });
        Collections.sort(toList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (toBFS.distTo(o1) > toBFS.distTo(o2)) return 1;
                if (toBFS.distTo(o2) > toBFS.distTo(o1)) return -1;
                return 0;
            }
        });
        /*StdOut.printf("The size of from_list and to_list after sort: %d %d\n",fromList.size(),toList.size());
        StdOut.println("Here is everything that is in fromList: ");
        for (int i : fromList) {
            StdOut.println(" Node: " + i + " Node distance from fromNode:" + fromBFS.distTo(i));
            if (i==20743) StdOut.println("ancestor is in the from list");
        }
        StdOut.println("Here is everything that is in toList: ");
        for (int j : toList) {
            StdOut.println(" Ndoe: " + j + " Node distance from toNode: " + toBFS.distTo(j));
            if (j==20743) StdOut.println("ancestor is in the to list");
        }*/
       path = new ArrayList<>();
        int i = 0, counter = 0;
        while (counter < fromList.size() && counter < toList.size()) {
            i = fromList.get(counter);
            for (int k = 0; k < toList.size(); k++) {
                if (i==toList.get(k)){
                    minDistance = fromBFS.distTo(i) + toBFS.distTo(i);
                    //StdOut.println("minDistance was just changed to :" + minDistance + " for i value of: " + i);
                    ancestor = i;
                    path = new ArrayList<>();
                    for (int j : fromBFS.pathTo(i)) {
                        path.add(j);
                    }
                    for (int j : toBFS.pathTo(i)) {
                        if (path.contains(j)) ancestor = j;
                        else path.add(j);
                    }
                    //StdOut.printf("Updated minDistance, and ancestor. The value of i is: %d The value of minDistance is: %d\n", i, minDistance);
                    return ancestor;
                }
            }
            /* j = toList.get(counter);
            StdOut.printf("checking %d and %d. Distance of %d and %d\n", i, j,fromBFS.distTo(i),toBFS.distTo(j));
            if (i == j) {
                minDistance = fromBFS.distTo(i) + toBFS.distTo(j);
                StdOut.println("minDistance was just changed to :" + minDistance + " for i value of: " + i);
                ancestor = i;
                path = new ArrayList<>();
                for (int k : fromBFS.pathTo(i)) {
                    path.add(k);
                }
                for (int k : toBFS.pathTo(j)) {
                    if (path.contains(k)) ancestor = k;
                    else path.add(k);
                }
                StdOut.printf("Updated minDistance, and ancestor. The value of i is: %d The value of minDistance is: %d\n", i, minDistance);
                return ancestor;
            } */
            counter++;
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
        return path;
    }

    // lock-step bfs attempt
    private int getAncestorII(int from, int to) {
        // create two breath first searches
        DeluxBFS fromBFS = new DeluxBFS(digraphDFCopy, from);
        DeluxBFS toBFS = new DeluxBFS(digraphDFCopy, to);
        int[] distances = new int[digraphDFCopy.V()];
        for (int i = 0; i < distances.length; i++) {
            distances[i] = -1;
        }
        for (int i = 0; i < digraphDFCopy.V(); i++) {
            if (fromBFS.hasPathTo(i) && toBFS.hasPathTo(i)) {
                distances[i] = fromBFS.distTo(i) + toBFS.distTo(i);
            }
        }
        int counter = 0;

        while (counter < distances.length) {
            if (distances[counter] != -1 && distances[counter] < minDistance) {
                minDistance = distances[counter];
                ancestor = counter;
            }
            counter++;
        }
        // minDistance = minDistance - 2;
        return ancestor;
    }

    // improved lock-step bfs attempt
    private int getAncestorImproved(int from, int to) {
        /* Topological topological = new Topological(digraphDFCopy);
        assert topological.hasOrder() : "The graph is not a DAG. ";
        for (int v : topological.order()) {
            if (fromBFS.hasPathTo(v) && marked[v]) {
                if (marked[v]) {
                    minDistance = fromBFS.distTo(v) + toBFS.distTo(v);
                    ancestor = v;
                    return ancestor;
                }
                marked[v] = true;
            }
            if (toBFS.hasPathTo(v) && marked[v]) {
                // if it is marked already, it must've been by the node on the other end
                minDistance = fromBFS.distTo(v) + toBFS.distTo(v);
                ancestor = v;
                return ancestor;
            }
            marked[v] = true;
        } */
        marked[from] = true;
        marked[to] = true;
        DeluxBFS fromBFS = new DeluxBFS(digraphDFCopy, from);
        DeluxBFS toBFS = new DeluxBFS(digraphDFCopy, to);
        MinPQ<Integer> fromQueue = new MinPQ<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                // number of moves the parent has made plus 1 plus the number moves I have to take from where I am
                if (fromBFS.distTo(o1) > fromBFS.distTo(o2))
                    return 1;
                else if (fromBFS.distTo(o2) > fromBFS.distTo(o1))
                    return -1;
                return 0;
            }
        });
        MinPQ<Integer> toQueue = new MinPQ<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (toBFS.distTo(o1) > toBFS.distTo(o2))
                    return 1;
                else if (toBFS.distTo(o2) > toBFS.distTo(o1))
                    return -1;
                return 0;
            }
        });
        for (int i = 0; i < digraphDFCopy.V(); i++) {
            fromQueue.insert(i);
            toQueue.insert(i);
        }
        int f = fromQueue.delMin();
        int t = toQueue.delMin();
        int counter = 0;
        while (f != t && counter < digraphDFCopy.V()) {
            if (!fromQueue.isEmpty()) f = fromQueue.delMin();
            if (!toQueue.isEmpty()) t = toQueue.delMin();
            counter++;
        }
        if (f == t) {
            ancestor = f;
            minDistance = fromBFS.distTo(f) + toBFS.distTo(t);// may have to subtract two
        }
        return ancestor;
        /*
        int distance = 1;
        int i=0;
        while(!fromQueue.isEmpty()){
            i = fromQueue.delMin();
            if (toBFS.hasPathTo(i)){
                minDistance = fromBFS.distTo(i) + toBFS.distTo(i);
                ancestor = i;
                return ancestor;
            }
            marked[i]=true;
        }*/
        /*
        while (distance < digraphDFCopy.V()) {
            for (int i = 0; i < digraphDFCopy.V(); i++) {
                if (fromBFS.hasPathTo(i) && marked[i]) {
                    minDistance = fromBFS.distTo(i) + toBFS.distTo(i);
                    ancestor = i;
                    return ancestor;
                }
                if (fromBFS.distTo(i) == distance) {
                    if (marked[i]) {
                        minDistance = fromBFS.distTo(i) + toBFS.distTo(i);
                        ancestor = i;
                        return ancestor;
                    }
                    marked[i] = true;
                }

            }
            for (int i = 0; i < digraphDFCopy.V(); i++) {
                if (toBFS.hasPathTo(i) && marked[i]) {
                    minDistance = fromBFS.distTo(i) + toBFS.distTo(i);
                    ancestor = i;
                    return ancestor;
                }
                if (toBFS.distTo(i) == distance) {
                    if (marked[i]) {
                        minDistance = fromBFS.distTo(i) + toBFS.distTo(i);
                        ancestor = i;
                        return ancestor;
                    }
                    marked[i] = true;
                }
            }
            distance++;
        } */

    }


    private List<Integer> extractPath(Node minF, Node minT, int match) {
        List<Integer> path = new ArrayList<>();
        ancestor = match; // ancestor should be the first match
        while (minF != null && minF.prevNode != null) {
            if (!path.contains(minF.id)) {
                path.add(minF.id);
            }
            minF = minF.prevNode;
        }
        while (minT.prevNode != null) {
            if (!path.contains(minT.id)) {
                path.add(minT.id);
            }
            minT = minT.prevNode;
        }
        if (!path.contains(from)) path.add(from);
        if (!path.contains(to)) path.add(to);
        return path;
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
        //SAP.DeluxBFS deluxBFS = sap.new DeluxBFS(digraph,Arrays.asList(7,4));
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
        System.out.println("ancestor between 7 and 6 in digraph9 should be 6: " + sap.getAncestorImproved(7, 6));
        StdOut.println("The minimum distance between 7 and 6 should be 1: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 6 and 7 in digraph9 should be 6: " + sap.getAncestorImproved(6, 7));
        System.out.println("The minimum distance between 6 and 7 should be 1: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 3 in digraph9 should be 3: " + sap.getAncestorImproved(7, 3));
        StdOut.println("The minimum distance between 7 and 3 should be 2: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 3 and 7 in digraph9 should be 3: " + sap.getAncestorImproved(3, 7));
        System.out.println("The minimum distance between 3 and 7 should be 2: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 2 in digraph9 should be 2: " + sap.getAncestorImproved(7, 2));
        StdOut.println("The minimum distance between 7 and 2 should be 3: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 2 and 7 in digraph9 should be 2: " + sap.getAncestorImproved(2, 7));
        System.out.println("The minimum distance between 2 and 7 should be 3: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 0 in digraph9 should be 6: " + sap.getAncestorImproved(7, 0));
        StdOut.println("The minimum distance between 7 and 0 should be 2: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 0 and 7 in digraph9 should be 6: " + sap.getAncestorImproved(0, 7));
        System.out.println("The minimum distance between 0 and 7 should be 2: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 1 in digraph9 should be 3: " + sap.getAncestorImproved(7, 1));
        StdOut.println("The minimum distance between 7 and 1 should be 3: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 1 and 7 in digraph9 should be 3: " + sap.getAncestorImproved(1, 7));
        System.out.println("The minimum distance between 1 and 7 should be 3: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 4 in digraph9 should be 4: " + sap.getAncestorImproved(7, 4));
        StdOut.println("The minimum distance between 7 and 4 should be 3: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 4 and 7 in digraph9 should be 4: " + sap.getAncestorImproved(4, 7));
        System.out.println("The minimum distance between 4 and 7 should be 3: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 5 in digraph9 should be 4: " + sap.getAncestorImproved(7, 5));
        StdOut.println("The minimum distance between 7 and 5 should be 4: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 5 and 7 in digraph9 should be 4: " + sap.getAncestorImproved(5, 7));
        System.out.println("The minimum distance between 5 and 7 should be 4: " + sap.minDistance);
        System.out.println();
        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 8 in digraph9 should be -1: " + sap.getAncestorImproved(7, 8));
        StdOut.println("The minimum distance between 7 and 8 should be 2147483647: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 8 and 7 in digraph9 should be -1: " + sap.getAncestorImproved(8, 7));
        System.out.println("The minimum distance between 8 and 7 should be 2147483647: " + sap.minDistance);
        System.out.println();

        sap = new SAP(digraph);
        System.out.println("ancestor between 7 and 7 in digraph9 should be 7: " + sap.getAncestorImproved(7, 7));
        StdOut.println("The minimum distance between 7 and 7 should be 0: " + sap.minDistance);
        sap = new SAP(digraph);
        System.out.println("ancestor between 8 and 8 in digraph9 should be 8: " + sap.getAncestorImproved(8, 8));
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
