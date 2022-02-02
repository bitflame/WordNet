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
    private int[] edgeTo;
    private int[] fromDistTo;
    private int[] toDistTo;
    private static final int INFINITY = Integer.MAX_VALUE;
    //private boolean fromPathLoop = false;
    //private boolean toPathLoop = false;
    private boolean print = false;

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
        // System.out.println("length(): Calculating the distance between : " + v + " " + w);
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
        //fromPathLoop = false;
        //toPathLoop = false;
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

        //fromPathLoop = false;
        //toPathLoop = false;
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
        // System.out.printf("lockStepBFS(): Here is f: %d t: %d \n", f, t);

        while (!(fromQueue.isEmpty() && toQueue.isEmpty())) {
            if (!fromQueue.isEmpty()) {
                int v = fromQueue.dequeue();
                if (print) System.out.printf("took %d from fromQueue \n", v);
                for (int j : digraphDFCopy.adj(v)) {
                    // keep going until you hit a loop in both queues, or you run out of nodes to process
                    if (!fromMarked[j]) {
                        fromMarked[j] = true;
                        edgeTo[j] = v;
                        int temp = fromDistTo[v];
                        fromDistTo[j] = temp + 1;
                        fromQueue.enqueue(j);
                    }
                    // for j prevNode = v, edgeNode = w, ancestor = j, and f and t are the same
                    else if (fromMarked[j]) {
                        // in a self loop
                        if (print) System.out.println("Hit a self loop in j block for " + f + " and" + t);
                        /* ancestor or the node that points to it, should be pointed to by one side starting from the
                        source or destination */
                        int w = edgeTo[j];
                        //edgeTo[j] = v;
                        // fromDistTo[j] = fromDistTo[v];
                        if (fromDistTo[j] != -1) fromDistTo[j] = Math.min(fromDistTo[j], fromDistTo[v]);
                        boolean one = testEdgeTo(j, f);
                        boolean two = testEdgeTo(w, f);
                        boolean three = testEdgeTo(j, t);
                        boolean four = testEdgeTo(w, t);
                        boolean hasPath = ((one || two) && (three || four));
                        int fromDist = 0;
                        if (fromDistTo[j] > 0) fromDist = fromDistTo[v] + 1;
                        int toDist = 0;
                        if (toDistTo[j] > 0) toDist = toDistTo[j];
                        if (print)
                            System.out.printf("Found an ancestor in the looped from match for f: %d t: %d : ancestor: %d fromDist = %d toDist = %d currentDist= %d hasPath: %b \n", f, t, ancestor, fromDist, toDist, currentDistance, hasPath);
                        if (print) System.out.printf("one: %b two: %b three: %b four: %b: ", one, two, three, four);
                        if (currentDistance > (toDist + fromDist) && hasPath) {
                            if (print)
                                System.out.println("Hit a self loop in j block, and updated the minDistance for f: %d t: %d" + f + " and " + t);
                            currentDistance = toDist + fromDist;
                            ancestor = j;
                        }
                        /* If one queue is empty and the new nodes have routes to both sides, both the ancestor and Minimum Distance should be updated . As of now, 12 is
                         * not counted as an ancestor, and then when 8 comes along, the old data seems to be used. From the digraph3 example */
                    }
                    if (fromMarked[j] && toMarked[j]) {
                        int fromDist = 0;
                        if (fromDistTo[v] > 0) fromDist = fromDistTo[v];
                        int toDist = 0;
                        if (toDistTo[j] > 0) toDist = toDistTo[j];
                        if (print)
                            System.out.printf("Found an ancestor in the normal from match for f: %d t: %d ancestor: %d fromDist = %d toDist = %d currentDist= %d \n", f, t, ancestor, fromDist, toDist, currentDistance);
                        if (currentDistance > (toDist + fromDist + 1)) {
                            ancestor = j;
                            currentDistance = toDist + fromDist + 1;
                            if (print)
                                System.out.printf("lockStepBfs(): updated the ancestor to %d and Current Distance to: %d in J block for f: %d, and t: %d\n", ancestor, currentDistance, f, t);
                        }
                    }
                }
            }
            if (!toQueue.isEmpty()) {
                int w = toQueue.dequeue();
                if (print) System.out.printf("took %d from toQueue \n", w);
                for (int k : digraphDFCopy.adj(w)) {
                    if (!toMarked[k]) {
                        toMarked[k] = true;
                        edgeTo[k] = w;
                        if (print) System.out.printf("added %d to toQueue ", k);
                        int temp = toDistTo[w];
                        toDistTo[k] = temp + 1;
                        toQueue.enqueue(k);
                    } else if (toMarked[k]) {
                        //toPathLoop = true;
                        if (print) System.out.println("lockStepBfs(): Hit a self loop in k block");
                        int v = edgeTo[k];
                        //edgeTo[k] = w;
                        toDistTo[k] = toDistTo[w];
                        if (toDistTo[k] != -1) toDistTo[k] = Math.min(toDistTo[k], toDistTo[w]);
                        /* I think I should do: if (toDistTo[k]!=-1) toDistTo[k]=Math.Min(toDistTo[k], toDistTo[w]) and create the mirror of it for the loop above */
                        boolean one = testEdgeTo(k, f);
                        boolean two = testEdgeTo(v, f);
                        boolean three = testEdgeTo(k, t);
                        boolean four = testEdgeTo(v, t);
                        boolean hasPath = ((one || two) && (three || four));
                        int fromDist = 0;
                        if (fromDistTo[k] > 0) fromDist = fromDistTo[k];
                        int toDist = 0;
                        if (toDistTo[k] > 0) toDist = toDistTo[w] + 1;
                        if (print)
                            System.out.printf("Found a potential ancestor in the looped to match for: from: %d, to: %d, fromDist: %d, toDist: %d, currentDist: %d hasPath: %b\n", f, t, fromDist, toDist, currentDistance, hasPath);
                        if (currentDistance != INFINITY && currentDistance > (fromDist + toDist + 1) && hasPath) {
                            ancestor = k;
                            currentDistance = fromDist + toDist;
                            if (print)
                                System.out.printf("lockStepBfs(): Hit a self loop in k block, for f: %d and t: %d and updated the currentDistance to: %d . The new ancestor is: %d\n", f, t, currentDistance, ancestor);
                        }
                        edgeTo[k] = w;
                    }
                    if (fromMarked[k] && toMarked[k]) {
                        int fromDist = 0;
                        if (fromDistTo[k] > 0) fromDist = fromDistTo[k];
                        int toDist = 0;
                        if (toDistTo[w] > 0) toDist = toDistTo[w];
                        if (print)
                            System.out.printf("Found a potential ancestor in the normal to match for: from: %d, to: %d, fromDist: %d, toDist: %d, currentDist: %d\n", f, t, fromDist, toDist, currentDistance);
                        if ((fromDist + toDist + 1) < currentDistance) {
                            ancestor = k;
                            currentDistance = fromDist + toDist + 1;
                            if (print)
                                System.out.printf("lockStepBfs(): updated the ancestor to %d and Minimum Distance to: %d in K block for f: %d f: %d\n", ancestor, minDistance, f, t);
                        }
                    }
                }
            }
        }
        // while (!(fromPathLoop && toPathLoop)) {
        // if (fromQueue.isEmpty() && toQueue.isEmpty()) break;
        // if ((fromPathLoop == true && currentDistance == INFINITY) || (toPathLoop == true && currentDistance == INFINITY)) break;
        // }
        // System.out.printf("Here is how many items left in fromQueue: %d and toQueue: %d", fromQueue.size(), toQueue.size());
        if (currentDistance == INFINITY) {
            // System.out.println("setting minDistance to -1 becuase currentDistance is INFINITY ");
            minDistance = -1;
            ancestor = -1;
            return minDistance;
        } else minDistance = currentDistance;
        return currentDistance;
    }

    // testEdgeTo(ancestor, x) and preAncestor'sNode like v, and w to the other end
    private boolean testEdgeTo(int ancestor, int destination) {
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

    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In("digraph3.txt"));
        SAP sap = new SAP(digraph);
        System.out.printf("%b\n", sap.testEdgeTo(11, 12));
        System.out.printf("%b\n", sap.testEdgeTo(11, 8));
    }
}
