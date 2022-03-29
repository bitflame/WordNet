import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;
import edu.princeton.cs.algs4.Queue;

import java.util.Iterator;
import java.util.Stack;

public class SAP {
    private final Digraph digraphDFCopy;
    private int ancestor;
    private int minDistance;
    private int from;
    private int to;
    private static final int INFINITY = Integer.MAX_VALUE;
    private BreadthFirstDirectedPaths fromBFS;
    private BreadthFirstDirectedPaths toBFS;
    private Topological topological;
    private Queue<Integer> fromQueue;
    private Queue<Integer> toQueue;
    private boolean[] onFromStack;
    private boolean[] onToStack;
    private int[] id;
    private Stack<Integer> fromStack;
    private Stack<Integer> toStack;
    private int n;
    private boolean proceed;
    private boolean[] marked;
    private int idCounter = 0; // nodes that ids were modified - may have traverse the id[] if there is a loop
    private int fromIdCounter = 0; // nodes with id of from side - may have traverse the id[] if there is a loop
    private int toIdCounter = 0; // nodes with id of to side - may have traverse the id[] if there is a loop

    // constructor takes a digraph ( not necessarily a DAG )
    public SAP(Digraph digraph) {
        if (digraph == null) throw new IllegalArgumentException("Digraph value can not be null");
        digraphDFCopy = new Digraph(digraph);
        topological = new Topological(digraphDFCopy);
        minDistance = -1;
        ancestor = -1;
        from = 0;
        to = 0;
        n = digraphDFCopy.V();
        proceed = true;
    }


    // length of the shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        // System.out.println("length(): Calculating the distance between : " + v + " " + w);
        if (v < 0 || v >= digraphDFCopy.V())
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if (w < 0 || w >= digraphDFCopy.V())
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if (this.from == v && this.to == w && v != w) return minDistance;
        if (v == w) {
            minDistance = 0;
            return minDistance;
        }
        from = v;
        to = w;
        fromBFS = new BreadthFirstDirectedPaths(digraphDFCopy, from);
        toBFS = new BreadthFirstDirectedPaths(digraphDFCopy, to);
        setupDefaultDataStructures();
        lockStepBFS();
        return minDistance;
    }

    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int count = 0;
        for (Integer v : vertices) {
            count++;
            if (v == null) {
                throw new IllegalArgumentException("vertex is null");
            }
            validateVertex(v);
        }
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= digraphDFCopy.V())
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (digraphDFCopy.V() - 1));
    }

    // length of the shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException("both lists can not be null");
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            ancestor = -1;
            minDistance = -1;
            return minDistance;
        }
        validateVertices(v);
        validateVertices(w);
        fromBFS = new BreadthFirstDirectedPaths(digraphDFCopy, v);
        toBFS = new BreadthFirstDirectedPaths(digraphDFCopy, w);
        setupDefaultDataStructures();
        int subsetDitance = INFINITY;
        int subsetAncestor = -1;
        int prevDistance = minDistance;
        int prevAncestor = ancestor;
        for (int i : v) {
            for (int j : w) {
                from = i;
                to = j;
                lockStepBFS();
                System.out.printf("message from inside SAP: For nodes %d and %d ShortestDistance=%d \n", i, j, minDistance);
                if (subsetDitance > minDistance && minDistance != -1) {
                    subsetDitance = minDistance;
                    subsetAncestor = ancestor;
                }
            }
        }
        if (subsetDitance == INFINITY) {
            ancestor = prevAncestor;
            minDistance = prevDistance;
        } else {
            ancestor = subsetAncestor;
            minDistance = subsetDitance;
        }
        return minDistance;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        // System.out.println("Calculating the ancestor between : " + v + " " + w);
        if (v < 0 || v >= digraphDFCopy.V())
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if (w < 0 || w >= digraphDFCopy.V())
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if (v < 0 || w < 0) throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if (v == w) {
            ancestor = w;
            return ancestor;
        }
        if (this.from == v && this.to == w && v != w) return ancestor;
        from = v;
        to = w;
        fromBFS = new BreadthFirstDirectedPaths(digraphDFCopy, from);
        toBFS = new BreadthFirstDirectedPaths(digraphDFCopy, to);
        setupDefaultDataStructures();
        lockStepBFS();
        return ancestor;
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException("list of iterables can not be null");
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            ancestor = -1;
            minDistance = -1;
            return ancestor;
        }
        validateVertices(v);
        validateVertices(w);
        fromBFS = new BreadthFirstDirectedPaths(digraphDFCopy, v);
        toBFS = new BreadthFirstDirectedPaths(digraphDFCopy, w);
        setupDefaultDataStructures();
        int ancestorSetDistance = INFINITY;
        int ancestorSetAncestor = -1;
        int previousDistance = minDistance;
        int previousAncestor = ancestor;
        for (int i : v) {
            for (int j : w) {
                from = i;
                to = j;
                lockStepBFS();
                if (ancestorSetDistance > minDistance && minDistance != -1) {
                    ancestorSetDistance = minDistance;
                    ancestorSetAncestor = ancestor;
                }
            }
        }
        if (ancestorSetAncestor == INFINITY) {
            ancestor = previousAncestor;
            minDistance = previousDistance;
        } else {
            ancestor = ancestorSetAncestor;
            minDistance = ancestorSetDistance;
        }
        return ancestor;
    }

    private int calculateDistance(int v, int currentDistance) {
        int distance = 0;
        if (fromBFS.hasPathTo(v) && toBFS.hasPathTo(v)) {
            // had to change below expression to && for graph 3 (14,9) using || causes node 10 trigger this too early
            if ((fromBFS.distTo(v) > currentDistance && toBFS.distTo(v) > currentDistance)) {
                proceed = false;
                return currentDistance;
            }
            distance = fromBFS.distTo(v) + toBFS.distTo(v);
            if (distance < currentDistance) {
                currentDistance = distance;
                ancestor = v;
            }
        }
        return currentDistance;
    }

    private int checkStackDistance(int w, boolean fromNode, boolean toNode) {
        int k = 0;
        if (fromNode) {
            while (fromStack.peek() != from) {
                k++;
                fromStack.pop();
            }
            k++;
            return k + toBFS.distTo(w);
        } else if (toNode) {
            while (toStack.peek() != to) {
                k++;
                toStack.pop();
            }
            k++;
            return k + fromBFS.distTo(w);
        }
        return INFINITY;
    }

    private void setupDefaultDataStructures() {
        fromQueue = new Queue<>();
        toQueue = new Queue<>();
        fromStack = new Stack<>();
        toStack = new Stack<>();
        onFromStack = new boolean[n];
        onToStack = new boolean[n];
        id = new int[n];
        for (int i : id) id[i] = -1;
        marked = new boolean[n];
        idCounter = 0;
    }

    private void lockStepBFS() {
        proceed = true;
        int currentDistance = INFINITY;
        fromQueue.enqueue(from);
        fromStack.push(from);
        marked[from] = true;
        onFromStack[from] = true;
        id[from] = from;
        idCounter++;
        toQueue.enqueue(to);
        toStack.push(to);
        marked[to] = true;
        onToStack[to] = true;
        id[to] = to;
        idCounter++;
        int v = 0;
        int stackDistance = 0;
        int distanceFromSourceCounter = 1;
        Iterator<Integer> var1;
        while (!fromQueue.isEmpty() && !toQueue.isEmpty() && proceed) {
            while (!fromQueue.isEmpty() && fromBFS.distTo(fromQueue.peek()) < distanceFromSourceCounter) {
                v = fromQueue.dequeue();
                if (id[v] == to) currentDistance = calculateDistance(v, currentDistance);
                var1 = digraphDFCopy.adj(v).iterator();
                while (var1.hasNext()) {
                    int w = var1.next();
                    if (!marked[w]) {
                        fromQueue.enqueue(w);
                        fromStack.push(w);
                        onFromStack[w] = true;
                        marked[w] = true;
                        id[w] = v;
                        idCounter++;
                    }
                }
            }
            if (!proceed) break;
            while (!toQueue.isEmpty() && toBFS.distTo(toQueue.peek()) < distanceFromSourceCounter) {
                v = toQueue.dequeue();
                if (id[v] == from) currentDistance = calculateDistance(v, currentDistance);
                var1 = digraphDFCopy.adj(v).iterator();
                while (var1.hasNext()) {
                    int w = var1.next();
                    if (!marked[w]) {
                        toQueue.enqueue(w);
                        toStack.push(w);
                        onToStack[w] = true;
                        marked[w] = true;
                        id[w] = to;
                        idCounter++;
                    }
                }
            }
            distanceFromSourceCounter++;
        }
        while (!fromQueue.isEmpty() && proceed) {
            v = fromQueue.dequeue();
            if (id[v] == to) currentDistance = calculateDistance(v, currentDistance);
            var1 = digraphDFCopy.adj(v).iterator();
            while (var1.hasNext()) {
                int w = var1.next();
                if (!marked[w]) {
                    fromQueue.enqueue(w);
                    fromStack.push(w);
                    onFromStack[w] = true;
                    marked[w] = true;
                    id[w] = from;
                    idCounter++;
                }
            }
        }
        while (!toQueue.isEmpty() && proceed) {
            while (!toQueue.isEmpty()) {
                v = toQueue.dequeue();
                if (id[v] == from) currentDistance = calculateDistance(v, currentDistance);
                var1 = digraphDFCopy.adj(v).iterator();
                while (var1.hasNext()) {
                    int w = var1.next();
                    if (!marked[w]) {
                        toQueue.enqueue(w);
                        toStack.push(w);
                        onToStack[w] = true;
                        marked[w] = true;
                        id[w] = v;
                        idCounter++;
                    }
                }
            }
        }
        if (currentDistance == INFINITY) {
            ancestor = -1;
            minDistance = -1;
        } else {
            minDistance = currentDistance;
        }
    }


    public static void main(String[] args) {
        System.out.printf("****************************************Testing digraph3 \n");
        Digraph digraph = new Digraph(new In("digraph3.txt"));
        SAP sap = new SAP(digraph);
        int minDist = sap.length(13, 14);
        if (minDist != 1) System.out.printf("Test 1 - (13, 14) expecting 1, getting: %d\n", minDist);
        else System.out.printf("Test 1 passed.\n");
        System.out.printf("Expected ancestor: 14. Actual ancestor: %d\n", sap.ancestor(13, 14));
        System.out.printf("Test 2 - (14, 13) expecting 1, getting: %d\n", sap.length(14, 13));
        System.out.printf("Expected ancestor: 14. Actual ancestor: %d\n", sap.ancestor(14, 13));
        System.out.printf("Test 3 - (13, 0) expecting 2, getting: %d\n", sap.length(13, 0));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(13, 0));
        System.out.printf("Test 4 - (0, 13) expecting 2, getting: %d\n", sap.length(0, 13));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(0, 13));
        System.out.printf("Test 5 - (14, 0) expecting 1, getting: %d\n", sap.length(14, 0));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(14, 0));
        System.out.printf("Test 6 - (0, 14) expecting 1, getting: %d\n", sap.length(0, 14));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(0, 14));
        System.out.printf("Test 7 - (13, 11) expecting 3, getting: %d\n", sap.length(13, 11));
        System.out.printf("Expected ancestor: 11. Actual ancestor: %d\n", sap.ancestor(13, 11));
        System.out.printf("Test 8 - (11, 13) expecting 3, getting: %d\n", sap.length(11, 13));
        System.out.printf("Expected ancestor: 11. Actual ancestor: %d\n", sap.ancestor(11, 13));
        System.out.printf("Test 9 - (14, 11) expecting 2, getting: %d\n", sap.length(14, 11));
        System.out.printf("Expected ancestor: 11. Actual ancestor: %d\n", sap.ancestor(14, 11));
        System.out.printf("Test10 - (11, 14) expecting 2, getting: %d\n", sap.length(11, 14));
        System.out.printf("Expected ancestor: 11. Actual ancestor: %d\n", sap.ancestor(11, 14));
        System.out.printf("Test11 - (14, 12) expecting 3, getting: %d\n", sap.length(14, 12));
        System.out.printf("Expected ancestor: 12. Actual ancestor: %d\n", sap.ancestor(14, 12));
        System.out.printf("Test12 - (12, 14) expecting 3, getting: %d\n", sap.length(12, 14));
        System.out.printf("Expected ancestor: 12. Actual ancestor: %d\n", sap.ancestor(12, 14));
        System.out.printf("Test13 - (14, 10) expecting 3, getting: %d\n", sap.length(14, 10));
        System.out.printf("Expected ancestor: 11. Actual ancestor: %d\n", sap.ancestor(14, 10));
        System.out.printf("Test14 - (10, 14) expecting 3, getting: %d\n", sap.length(10, 14));
        System.out.printf("Expected ancestor: 11. Actual ancestor: %d\n", sap.ancestor(10, 14));
        System.out.printf("Test15 - (14, 9) expecting 4, getting: %d\n", sap.length(14, 9));
        System.out.printf("Expected ancestor: 11. Actual ancestor: %d\n", sap.ancestor(14, 9));
        System.out.printf("Test16 - (9, 14) expecting 4, getting: %d\n", sap.length(9, 14));
        System.out.printf("Expected ancestor: 11. Actual ancestor: %d\n", sap.ancestor(9, 14));
        System.out.printf("Test17 - (13, 8) expecting 5, getting: %d\n", sap.length(13, 8));
        System.out.printf("Expected ancestor: 8. Actual ancestor: %d\n", sap.ancestor(13, 8));
        System.out.printf("Test18 - (8, 13) expecting 5, getting: %d\n", sap.length(8, 13));
        System.out.printf("Expected ancestor: 8. Actual ancestor: %d\n", sap.ancestor(8, 13));
        System.out.printf("Test19 - (14, 8) expecting 4, getting: %d\n", sap.length(14, 8));
        System.out.printf("Expected ancestor: 8. Actual ancestor: %d\n", sap.ancestor(14, 8));
        System.out.printf("Test20 - (8, 14) expecting 4, getting: %d\n", sap.length(8, 14));
        System.out.printf("Expected ancestor: 8. Actual ancestor: %d\n", sap.ancestor(8, 14));
        System.out.printf("Test21 - (7, 13) expecting 6, getting: %d\n", sap.length(7, 13));
        System.out.printf("Expected ancestor: 8. Actual ancestor: %d\n", sap.ancestor(7, 13));
        System.out.printf("Test22 - (13, 7) expecting 6, getting: %d\n", sap.length(13, 7));
        System.out.printf("Expected ancestor: 8. Actual ancestor: %d\n", sap.ancestor(13, 7));
        System.out.printf("Test23 - (1, 2) expecting 1, getting: %d\n", sap.length(1, 2));
        System.out.printf("Expected ancestor: 2. Actual ancestor: %d\n", sap.ancestor(1, 2));
        System.out.printf("Test24 - (1, 13) expecting -1, getting: %d\n", sap.length(1, 13));
        System.out.printf("Expected ancestor: -1. Actual ancestor: %d\n", sap.ancestor(1, 13));
        System.out.printf("Test25 - (9, 13) expecting 5, getting: %d\n", sap.length(9, 13));
        System.out.printf("Expected ancestor: 11. Actual ancestor: %d\n", sap.ancestor(9, 13));
        System.out.printf("Test26 - (13, 9) expecting 5, getting: %d\n", sap.length(13, 9));
        System.out.printf("Expected ancestor: 11. Actual ancestor: %d\n", sap.ancestor(13, 9));
        System.out.printf("Test27 - (8, 14) expecting 4, getting: %d\n", sap.length(8, 14));
        System.out.printf("Expected ancestor: 11. Actual ancestor: %d\n", sap.ancestor(8, 14));
        System.out.printf("Test28 - (14, 8) expecting 4, getting: %d\n", sap.length(14, 8));
        System.out.printf("Expected ancestor: 11. Actual ancestor: %d\n", sap.ancestor(14, 8));

        System.out.printf("****************************************Testing digraph1 \n");
        digraph = new Digraph(new In("digraph1.txt"));
        sap = new SAP(digraph);
        System.out.printf("Test 1 - (0, 2) expecting 1, getting: %d\n", sap.length(0, 2));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(0, 2));
        System.out.printf("Test 2 - (2, 0) expecting 1, getting: %d\n", sap.length(2, 0));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(2, 0));
        System.out.printf("Test 3 - (0, 1) expecting 1, getting: %d\n", sap.length(0, 1));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(0, 1));
        System.out.printf("Test 4 - (1, 0) expecting 1, getting: %d\n", sap.length(1, 0));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(1, 0));
        System.out.printf("Test 5 - (1, 2) expecting 2, getting: %d\n", sap.length(1, 2));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(1, 2));
        System.out.printf("Test 6 - (2, 1) expecting 2, getting: %d\n", sap.length(2, 1));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(2, 1));
        System.out.printf("Test 7 - (4, 0) expecting 2, getting: %d\n", sap.length(4, 0));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(4, 0));
        System.out.printf("Test 8 - (0, 4) expecting 2, getting: %d\n", sap.length(0, 4));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(0, 4));
        System.out.printf("Test 9 - (4, 2) expecting 3, getting: %d\n", sap.length(4, 2));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(4, 2));
        System.out.printf("Test 10 - (2, 4) expecting 3, getting: %d\n", sap.length(2, 4));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(2, 4));
        System.out.printf("Test 11 - (3, 5) expecting 2, getting: %d\n", sap.length(3, 5));
        System.out.printf("Expected ancestor: 1. Actual ancestor: %d\n", sap.ancestor(3, 5));
        System.out.printf("Test 12 - (5, 3) expecting 2, getting: %d\n", sap.length(5, 3));
        System.out.printf("Expected ancestor: 1. Actual ancestor: %d\n", sap.ancestor(5, 3));
        System.out.printf("Test 13 - (7, 11) expecting 5, getting: %d\n", sap.length(7, 11));
        System.out.printf("Expected ancestor: 1. Actual ancestor: %d\n", sap.ancestor(7, 11));
        System.out.printf("Test 14 - (11, 7) expecting 5, getting: %d\n", sap.length(11, 7));
        System.out.printf("Expected ancestor: 1. Actual ancestor: %d\n", sap.ancestor(11, 7));
        System.out.printf("Test 15 - (12, 4) expecting 4, getting: %d\n", sap.length(12, 4));
        System.out.printf("Expected ancestor: 1. Actual ancestor: %d\n", sap.ancestor(12, 4));
        System.out.printf("Test 16 - (12, 4) expecting 4, getting: %d\n", sap.length(12, 4));
        System.out.printf("Expected ancestor: 1. Actual ancestor: %d\n", sap.ancestor(12, 4));
        System.out.printf("Test 17 - (9, 1) expecting 2, getting: %d\n", sap.length(9, 1));
        System.out.printf("Expected ancestor: 1. Actual ancestor: %d\n", sap.ancestor(9, 1));
        System.out.printf("Test 18 - (1, 9) expecting 2, getting: %d\n", sap.length(1, 9));
        System.out.printf("Expected ancestor: 1. Actual ancestor: %d\n", sap.ancestor(1, 9));
        System.out.printf("Test 19 - (12, 0) expecting 4, getting: %d\n", sap.length(12, 0));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(12, 0));
        System.out.printf("Test 20 - (0, 12) expecting 4, getting: %d\n", sap.length(0, 12));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(0, 12));
        System.out.printf("****************************************Testing digraph2 \n");
        digraph = new Digraph(new In("digraph2.txt"));
        sap = new SAP(digraph);
        System.out.printf("Test 1 - (1, 0) expecting 1, getting: %d\n", sap.length(1, 0));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(1, 0));
        System.out.printf("Test 2 - (0, 1) expecting 1, getting: %d\n", sap.length(0, 1));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(0, 1));
        System.out.printf("Test 3 - (5, 0) expecting 1, getting: %d\n", sap.length(5, 0));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(5, 0));
        System.out.printf("Test 4 - (0, 5) expecting 1, getting: %d\n", sap.length(0, 5));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(0, 5));
        System.out.printf("Test 5 - (5, 1) expecting 2, getting: %d\n", sap.length(5, 1));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(5, 1));
        System.out.printf("Test 6 - (1, 5) expecting 2, getting: %d\n", sap.length(1, 5));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(1, 5));
        System.out.printf("Test 7 - (4, 0) expecting 2, getting: %d\n", sap.length(4, 0));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(4, 0));
        System.out.printf("Test 8 - (0, 4) expecting 2, getting: %d\n", sap.length(0, 4));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(0, 4));
        System.out.printf("Test 9 - (4, 1) expecting 3, getting: %d\n", sap.length(4, 1));
        System.out.printf("Expected ancestor: 4. Actual ancestor: %d\n", sap.ancestor(4, 1));
        System.out.printf("Test 10 - (2, 0) expecting 4, getting: %d\n", sap.length(2, 0));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(2, 0));
        System.out.printf("Test 11 - (0, 2) expecting 4, getting: %d\n", sap.length(0, 2));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(0, 2));
    }
}