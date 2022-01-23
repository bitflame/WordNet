import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.Iterator;

public class SAP {
    private boolean hasCycle = false;
    private final Digraph digraphDFCopy;
    private int ancestor;
    private int minDistance = INFINITY;
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
    private int lengthSource;
    private int lengthDestination;

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
        // System.out.println("Calculating the distance between : " + v + " " + w);
        if (v == lengthSource && w == lengthDestination) return minDistance;
        lengthSource = v;
        lengthDestination = w;
        if (v == w) {
            return 0;
        }
        if ((digraphDFCopy.indegree(from) == 0 && digraphDFCopy.outdegree(from) == 0) || (digraphDFCopy.indegree(to) == 0 &&
                digraphDFCopy.outdegree(to) == 0)) return minDistance;
        fromPathLoop = false;
        toPathLoop = false;
        n = digraphDFCopy.V();
        fromMarked = new boolean[n];
        toMarked = new boolean[n];
        fromEdgeTo = new int[n];
        toEdgeTo = new int[n];
        fromDistTo = new int[n];
        toDistTo = new int[n];
        for (int i = 0; i < n; i++) {
            toEdgeTo[i] = -1;
            fromEdgeTo[i] = -1;
        }
        minDistance = calculateMinDistance(v, w);
        return minDistance;
    }

    // length of the shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Iterable value to SAP.length() can not be null.");
        int distance = INFINITY;
        // System.out.printf("sap triggers ancestor() with iterables ");
        Iterator<Integer> i = v.iterator();
        Iterator<Integer> j = w.iterator();
        while (i.hasNext()) {
            int source = i.next();
            while (j.hasNext()) {
                int destination = j.next();
                length(source, destination);
                if (minDistance < distance) distance = minDistance;
            }
            j = w.iterator();
        }
        minDistance = distance;
        return minDistance;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (this.from == v && this.to == w) return ancestor;
        from = v;
        to = w;
        if (v == w) {
            return ancestor = v;
        }
        ancestor = -1;
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

        minDistance = INFINITY;
        for (int i : v) {
            for (int j : w) {
                if (length(i, j) < minDistance)
                    ancestor(i, j);
            }
        }
        return ancestor;
    }

    private int calculateMinDistance(int f, int t) {
        int currentDistance = INFINITY;
        lengthSource = f;
        lengthDestination = t;
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
        while (!(fromPathLoop && toPathLoop)) {
            if (fromQueue.isEmpty() && toQueue.isEmpty()) break;
            if ((fromPathLoop == true && currentDistance == INFINITY) || (toPathLoop == true && currentDistance == INFINITY))
                break;
            if (!fromQueue.isEmpty()) v = fromQueue.dequeue();
            if (!toQueue.isEmpty()) w = toQueue.dequeue();
            for (int j : digraphDFCopy.adj(v)) {
                if (fromMarked[j]) {
                    fromPathLoop = true;
                    if (currentDistance != INFINITY)
                        currentDistance = Math.min(currentDistance, toDistTo[j] + fromDistTo[v] + 1);
                }
                if (fromEdgeTo[j] != v) {
                    fromEdgeTo[j] = v;
                    fromDistTo[j] = fromDistTo[v] + 1;
                    // } else if (!toMarked[j] && !fromMarked[j]) {
                }
                if (!toMarked[j] && !fromMarked[j]) {
                    fromQueue.enqueue(j);
                }
                if (!fromMarked[j]) {
                    fromMarked[j] = true;
                }
                if (fromMarked[j] && toMarked[j]) {
                    currentDistance = Math.min(currentDistance, toDistTo[j] + fromDistTo[v] + 1);
                }

            }
            for (int k : digraphDFCopy.adj(w)) {
                if (toMarked[k]) {
                    toPathLoop = true;
                    if (currentDistance != INFINITY)
                        currentDistance = Math.min(currentDistance, fromDistTo[k] + toDistTo[w] + 1);
                }
                if (toEdgeTo[k] != w) {
                    toEdgeTo[k] = w;
                    toDistTo[k] = toDistTo[w] + 1;
                }
                if (!toMarked[k] && !fromMarked[k]) {
                    toQueue.enqueue(k);
                }
                if (!toMarked[k]) {
                    toMarked[k] = true;
                }
                if (fromMarked[k] && toMarked[k]) {
                    currentDistance = Math.min(currentDistance, fromDistTo[k] + toDistTo[w] + 1);
                }

            }
        }
        if (currentDistance == 0) currentDistance = 1;
        else if (currentDistance == INFINITY) {
            currentDistance = -1;
        }
        return currentDistance;
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
        int currentDistance = INFINITY;
        while (!(fromPathLoop && toPathLoop)) {
            if (fromQueue.isEmpty() && toQueue.isEmpty()) break;
            if ((fromPathLoop == true && currentDistance == INFINITY) || (toPathLoop == true && currentDistance == INFINITY))
                break;
            if (!fromQueue.isEmpty()) v = fromQueue.dequeue();
            if (!toQueue.isEmpty()) w = toQueue.dequeue();
            for (int j : digraphDFCopy.adj(v)) {
                // keep going until you hit a loop in both queues, or you run out of nodes to process
                if (fromMarked[j]) {
                    // in a self loop
                    fromPathLoop = true;
                    if (currentDistance != INFINITY && currentDistance > toDistTo[j] + fromDistTo[v] + 1) ancestor = j;
                }
                if (fromEdgeTo[j] != v) {
                    fromEdgeTo[j] = v;
                    fromDistTo[j] = fromDistTo[v] + 1;
                }
                if (!toMarked[j] && !fromMarked[j]) {
                    fromQueue.enqueue(j);
                }
                if (!fromMarked[j]) {
                    fromMarked[j] = true;
                }
                if (fromMarked[j] && toMarked[j]) {
                    if (toDistTo[j] + fromDistTo[v] + 1 < currentDistance) {
                        ancestor = j;
                        currentDistance = toDistTo[j] + fromDistTo[v] + 1;
                    }
                }
            }

            for (int k : digraphDFCopy.adj(w)) {
                if (toMarked[k]) {
                    toPathLoop = true;
                    if (currentDistance != INFINITY && currentDistance > (fromDistTo[k] + toDistTo[w] + 1))
                        ancestor = k;
                }
                if (toEdgeTo[k] != w) {
                    toEdgeTo[k] = w;
                    toDistTo[k] = toDistTo[w] + 1;
                }
                if (!toMarked[k] && !fromMarked[k]) {
                    toQueue.enqueue(k);
                }
                if (!toMarked[k]) {
                    toMarked[k] = true;
                }
                if (fromMarked[k] && toMarked[k]) {
                    if ((fromDistTo[k] + toDistTo[w] + 1) < currentDistance) {
                        ancestor = k;
                        currentDistance = fromDistTo[k] + toDistTo[w] + 1;
                    }
                }
            }
        }
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
