import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

public class SAP {
    private boolean hasCycle = false;
    private final Digraph digraphDFCopy;
    private int ancestor;
    private int minDistance;
    private int from;
    private int to;
    private int n;
    private boolean[] fromMarked;
    private boolean[] toMarked;
    private int[] fromEdgeTo;
    private int[] toEdgeTo;
    private int[] fromDistTo;
    private int[] toDistTo;
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean fromPathLoop = false;
    private boolean toPathLoop = false;
    private boolean fromBridge = false;
    private boolean toBridge = false;

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
            return ancestor = v;
        }
        ancestor = -1;
        minDistance = -1;
        this.from = v;
        this.to = w;
        if ((digraphDFCopy.indegree(from) == 0 && digraphDFCopy.outdegree(from) == 0) || (digraphDFCopy.indegree(to) == 0 &&
                digraphDFCopy.outdegree(to) == 0))
            return ancestor;
        fromPathLoop = false;
        toPathLoop = false;
        n = digraphDFCopy.V();
        fromMarked = new boolean[n];
        toMarked = new boolean[n];
        fromEdgeTo = new int[n];
        toEdgeTo = new int[n];
        fromDistTo = new int[n];
        toDistTo = new int[n];
        lockStepBFS(from, to);
        return ancestor;
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Iterable value to SAP.ancestor() can not be null.");
        int distance = Integer.MAX_VALUE;
        int currentAncestor = -1;
        // System.out.printf("sap triggers ancestor() with iterables ");
        for (int i : v) {
            for (int j : w) {
                // StdOut.printf("Calling ancestor(%d, %d) ", i, j);
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

    private int getAncestorII(int from, int to) {
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
        lockStepBFS(from, to);
        return ancestor;
    }

    private int getLengthII(int f, int t) {
        if (f != from && t != to) {
            getAncestorII(f, t);
        }
        return minDistance;
    }

    private void lockStepBFS(int f, int t) {
        Queue<Integer> fromQueue = new Queue<>();
        Queue<Integer> toQueue = new Queue<>();
        fromQueue.enqueue(f);
        toQueue.enqueue(t);
        fromMarked[f] = true;
        fromDistTo[f] = 0;
        toMarked[t] = true;
        toDistTo[t] = 0;
        int w = -1;
        int v = -1;
        minDistance = INFINITY;
        while ((!fromQueue.isEmpty() && !toQueue.isEmpty()) || (fromPathLoop && toPathLoop)) {
            if (!fromQueue.isEmpty()) v = fromQueue.dequeue();
            if (!toQueue.isEmpty()) w = toQueue.dequeue();
            for (int j : digraphDFCopy.adj(v)) {
                // keep going until you hit a loop in both queues or you run out of nodes to process
                if (fromMarked[j]) {// you are in a self loop
                    ancestor = -1;
                    minDistance = -1;
                    fromPathLoop = true;
                } else if (!toMarked[j]) {
                    fromEdgeTo[j] = v;
                    fromMarked[j] = true;
                    fromDistTo[j] = fromDistTo[v] + 1;
                    fromQueue.enqueue(j);
                } else if (toEdgeTo[j] != w) {
                    // you hit one of the other guys previously traversed nodes
                    if (minDistance > 1 + fromDistTo[v]) {
                        ancestor = j;
                        minDistance = 1 + fromDistTo[v];
                    }
                    fromBridge = true;
                } else {
                    ancestor = j;
                    minDistance = Math.min(minDistance, toDistTo[j] + fromDistTo[v] + 1);
                }
            }

            for (int k : digraphDFCopy.adj(w)) {
                if (toMarked[k]) {
                    // you are in a self loop
                    ancestor = -1;
                    minDistance = -1;
                    toPathLoop = true;
                } else if (!fromMarked[k]) {
                    toEdgeTo[k] = w;
                    toMarked[k] = true;
                    toDistTo[k] = toDistTo[w] + 1;
                    toQueue.enqueue(k);
                } else if (fromEdgeTo[k] != v && fromEdgeTo[k] != 0) {
                    // only update the ancestor if you have a shorter minDistance
                    if (minDistance > (toDistTo[toEdgeTo[k]] + 1 + fromDistTo[v] + 1)) {
                        ancestor = k;
                        minDistance = (toDistTo[toEdgeTo[k]] + 1 + fromDistTo[v] + 1);
                    }
                } else {
                    ancestor = k;
                    minDistance = Math.min(minDistance, fromDistTo[k] + toDistTo[w] + 1);
                }
            }
        }
        if (minDistance == 0) minDistance = 1;
    }

    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In("digraph1.txt"));
        SAP sap = new SAP(digraph);
        int length = sap.length(4, 1);
        if (length != 1)
            throw new AssertionError("The value of length between 4,1 in digraph1 should be 1, but it is: " + length);
        digraph = new Digraph(new In("digraph2.txt"));
        sap = new SAP(digraph);
        length = sap.length(4, 1);
        if (length != 3)
            throw new AssertionError("The value of length between nodes 4,1 in digraph2 should be 3, but it is: " + length);

    }
}
