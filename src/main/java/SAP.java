import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.Iterator;

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
    //private int[] fromEdgeTo;
    //private int[] toEdgeTo;
    private int[] edgeTo;
    private int[] fromDistTo;
    private int[] toDistTo;
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean fromPathLoop = false;
    private boolean toPathLoop = false;

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
        if (v == from && w == to) return minDistance;
        from = v;
        to = w;
        if (v == w) {
            ancestor = v;
            return 0;
        }
        if ((digraphDFCopy.indegree(from) == 0 && digraphDFCopy.outdegree(from) == 0) || (digraphDFCopy.indegree(to) == 0 &&
                digraphDFCopy.outdegree(to) == 0)) {
            ancestor = -1;
            return minDistance = -1;

        }
        fromPathLoop = false;
        toPathLoop = false;
        n = digraphDFCopy.V();
        fromMarked = new boolean[n];
        toMarked = new boolean[n];
        // fromEdgeTo = new int[n];
        // toEdgeTo = new int[n];
        edgeTo = new int[n];
        for (int i = 0; i < n; i++) {
            edgeTo[i] = -1;
        }
        fromDistTo = new int[n];
        toDistTo = new int[n];
        minDistance = lockStepBFS(v, w);
        return minDistance;
    }

    // length of the shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Iterable value to SAP.length() can not be null.");
        int currentDistance = 0;
        int prevDistance = INFINITY;
        // System.out.printf("sap triggers ancestor() with iterables ");
        Iterator<Integer> i = v.iterator();
        Iterator<Integer> j = w.iterator();
        while (i.hasNext()) {
            int source = i.next();
            while (j.hasNext()) {
                int destination = j.next();
                currentDistance = length(source, destination);
                // System.out.printf("Current Distance: %d \n", currentDistance);
                if (currentDistance != -1 && currentDistance < prevDistance) {
                    prevDistance = currentDistance;
                }
            }
            j = w.iterator();
        }
        // System.out.printf("Here is the last value in previous distance: %d\n" , prevDistance);
        minDistance = prevDistance;
        return minDistance;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        // System.out.println("Calculating the ancestor between : " + v + " " + w);
        if (this.from == v && this.to == w) return ancestor;
        from = v;
        to = w;
        if (v == w) {
            return ancestor = v;
        }
        if ((digraphDFCopy.indegree(from) == 0 && digraphDFCopy.outdegree(from) == 0) || (digraphDFCopy.indegree(to) == 0 &&
                digraphDFCopy.outdegree(to) == 0)) {
            minDistance = -1;
            return ancestor = -1;
        }

        fromPathLoop = false;
        toPathLoop = false;
        n = digraphDFCopy.V();
        fromMarked = new boolean[n];
        toMarked = new boolean[n];
        // fromEdgeTo = new int[n];
        // toEdgeTo = new int[n];
        edgeTo = new int[n];
        for (int i = 0; i < n; i++) {
            edgeTo[i] = -1;
        }
        fromDistTo = new int[n];
        toDistTo = new int[n];
        lockStepBFS(from, to);
        return ancestor;
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Iterable value to SAP.ancestor() can not be null.");

        int len = 0;
        int prevLen = INFINITY;
        int currentAncestor = 0;
        Iterator<Integer> i = v.iterator();
        Iterator<Integer> j = w.iterator();
        while (i.hasNext()) {
            int source = i.next();
            while (j.hasNext()) {
                int destination = j.next();
                len = length(source, destination);
                if (len != -1 && len < prevLen) {
                    currentAncestor = ancestor;
                    prevLen = len;
                }
            }
            j = w.iterator();
        }
        ancestor = currentAncestor;
        minDistance = prevLen;
        return ancestor;
    }

    private int lockStepBFS(int f, int t) {
        /* todo - you can use digraph indegree to set edgeTo f and t maybe */
        Queue<Integer> fromQueue = new Queue<>();
        Queue<Integer> toQueue = new Queue<>();
        fromQueue.enqueue(f);
        toQueue.enqueue(t);
        fromMarked[f] = true;
        fromDistTo[f] = 0;
        toMarked[t] = true;
        toDistTo[t] = 0;
        int currentDistance = INFINITY;
        while (!(fromPathLoop && toPathLoop)) {
            if (fromQueue.isEmpty() && toQueue.isEmpty()) break;
            if ((fromPathLoop == true && currentDistance == INFINITY) || (toPathLoop == true && currentDistance == INFINITY))
                break;
            if (!fromQueue.isEmpty()) {
                int v = fromQueue.dequeue();
                // System.out.printf("Here is v: %d w: %d \n", v, w);
                for (int j : digraphDFCopy.adj(v)) {
                    // keep going until you hit a loop in both queues, or you run out of nodes to process
                  /*  if (edgeTo[j] != v && v != 0 && j != f) {
                        edgeTo[j] = v;
                        int temp = fromDistTo[v];
                        fromDistTo[j] = temp + 1;
                    } else if (edgeTo[j] == 0 && v == 0) {
                        edgeTo[j] = v;
                        int temp = fromDistTo[v];
                        fromDistTo[j] = temp + 1;
                    } */
                    if (edgeTo[j] != v && j != f) {
                        edgeTo[j] = v;
                        int temp = fromDistTo[v];
                        fromDistTo[j] = temp + 1;
                    }
                    // for j prevNode = v, edgeNode = w, ancestor = j, and f and t are the same
                    if (fromMarked[j]) {
                        // in a self loop
                        // System.out.println("Hit a self loop in j block for " + f + " and" + t);
                        fromPathLoop = true;
                        /* ancestor or the node that points to it, should be pointed to by one side starting from the
                        source or destination */
                        int w = edgeTo[j];
                        edgeTo[j] = v;
                        fromDistTo[j] = fromDistTo[v] + 1;
                        boolean one = testEdgeTo(j, f);
                        boolean two = testEdgeTo(w, f);
                        boolean three = testEdgeTo(j, t);
                        boolean four = testEdgeTo(w, t);
                        boolean hasPath = ((one || two) && (three || four));
                        if (currentDistance != INFINITY && currentDistance > (toDistTo[j] + fromDistTo[v] + 1) && hasPath) {
                            // System.out.println("Hit a self loop in j block, and updated the minDistance for: " + f + " and " + t);
                            ancestor = j;
                            currentDistance = toDistTo[j] + fromDistTo[v] + 1;
                        }

                    }
                    if (!toMarked[j] && !fromMarked[j]) {
                        fromQueue.enqueue(j);
                    }
                    if (!fromMarked[j]) {
                        fromMarked[j] = true;
                    }
                    if (fromMarked[j] && toMarked[j]) {
                        if (currentDistance > (toDistTo[j] + fromDistTo[v] + 1)) {
                            ancestor = j;
                            currentDistance = toDistTo[j] + fromDistTo[v] + 1;
                        }
                        edgeTo[j] = v;
                        fromDistTo[j] = fromDistTo[v] + 1;
                    }
                }
            }
            if (!toQueue.isEmpty()) {
                int w = toQueue.dequeue();
                for (int k : digraphDFCopy.adj(w)) {
                  /*  if (edgeTo[k] != w && w != 0 && k != w) {
                        edgeTo[k] = w;
                        int temp = toDistTo[w];
                        toDistTo[k] = temp + 1;
                    } else if (edgeTo[k] == 0 && w == 0) {
                        edgeTo[k] = w;
                        int temp = toDistTo[w];
                        toDistTo[k] = temp + 1;
                    } */
                    if (edgeTo[k] != w && k != w) {
                        edgeTo[k] = w;
                        int temp = toDistTo[w];
                        toDistTo[k] = temp + 1;
                    }
                    if (toMarked[k]) {
                        toPathLoop = true;
                        // System.out.println("Hit a self loop in k block");
                        if (currentDistance != INFINITY && currentDistance > (fromDistTo[k] + toDistTo[w] + 1)) {
                            // System.out.println("Hit a self loop in k block, and updated the minDistance. ");
                            ancestor = k;
                            currentDistance = fromDistTo[k] + toDistTo[w] + 1;
                        }
                        edgeTo[k] = w;
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
                        edgeTo[k] = w;
                        toDistTo[k] = toDistTo[w] + 1;
                    }
                }
            }
        }
        if (currentDistance == INFINITY) {
            minDistance = -1;
            ancestor = -1;
            return minDistance;
        } else minDistance = currentDistance;
        return currentDistance;
    }

    // testEdgeTo(ancestor, x) and preAncestor'sNode like v, and w to the other end
    public boolean testEdgeTo(int ancestor, int destination) {
        //  System.out.printf("inside testEdge for " + from + " and " + to);
        if (ancestor == destination) return true;
        int i = ancestor;
        int counter = 0;
        for (; i != destination && counter < n; i = edgeTo[i]) {
            if (i == -1) break;
            counter++;
            //System.out.print(" " + i);
        }
        // if (i != -1) System.out.print(" " + i);
        return (i == destination);
    }

    public int getAncestor(int a, int b) {
        return b;
    }

    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In("digraph9.txt"));
        SAP sap = new SAP(digraph);
        int length = sap.length(0, 5);
        if (length != 4)
            throw new AssertionError("The value of length between 0,5 in digraph9 should be 4, but it is: " + length);
        int ancestor = sap.ancestor(0, 5);
        if (ancestor != 4) throw new AssertionError("The ancestor of 0, and 5 should be 4, but it is: " + ancestor);

    }
}
