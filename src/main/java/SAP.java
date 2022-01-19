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
    private boolean[] marked;
    private boolean[] fromMarked;
    private boolean[] toMarked;
    private int[] fromEdgeTo;
    private int[] toEdgeTo;
    private int[] fromDistTo;
    private int[] toDistTo;
    private static final int INFINITY = Integer.MAX_VALUE;

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
        n = digraphDFCopy.V();
        marked = new boolean[n];
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
        while (!fromQueue.isEmpty() || !toQueue.isEmpty()) {
            if (!fromQueue.isEmpty()) v = fromQueue.dequeue();
            if (!toQueue.isEmpty()) w = toQueue.dequeue();
            for (int j : digraphDFCopy.adj(v)) {
                if (!marked[j]) {
                    fromEdgeTo[j] = v;
                    marked[j] = true;
                    fromDistTo[j] = fromDistTo[v] + 1;
                    fromQueue.enqueue(j);
                } else {
                    ancestor = j;
                    break;
                }
            }
            for (int k : digraphDFCopy.adj(w)) {
                if (!marked[k]) {
                    toEdgeTo[k] = w;
                    marked[k] = true;
                    toDistTo[k] = toDistTo[w] + 1;
                    toQueue.enqueue(k);
                } else {
                    ancestor = k;
                    break;
                }
            }
        }
        int fromDist = (fromDistTo[ancestor] < INFINITY) ? fromDistTo[ancestor] : 0;
        int toDist = (toDistTo[ancestor] < INFINITY) ? toDistTo[ancestor] : 0;
        minDistance = fromDist + toDist;
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
