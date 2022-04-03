import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

import java.util.Iterator;


public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final Digraph digraphDFCopy;
    private int ancestor;
    private int minDistance;
    private int iterablesMinimumDistance;
    private int iterablesAncestor;
    private int from;
    private int to;
    private BreadthFirstDirectedPaths fromBFS;
    private BreadthFirstDirectedPaths toBFS;
    private Queue<Integer> fromQueue;
    private Queue<Integer> toQueue;
    private final int n;
    private boolean proceed;
    private boolean[] marked;

    // constructor takes a digraph ( not necessarily a DAG )
    public SAP(Digraph digraph) {
        if (digraph == null) throw new IllegalArgumentException("Digraph value can not be null");
        digraphDFCopy = new Digraph(digraph);
        minDistance = -1;
        iterablesMinimumDistance = -1;
        ancestor = -1;
        iterablesAncestor = -1;
        from = 0;
        to = 0;
        n = digraphDFCopy.V();
        proceed = true;
        setupDefaultDataStructures();
    }


    // length of the shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        // System.out.println("length(): Calculating the distance between : " + v + " " + w);
        if ((v < 0) || (v >= n))
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if ((w < 0) || (w >= n))
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");

        if (v == w) {
            minDistance = 0;
            ancestor = v;
            from = v;
            to = w;
            return minDistance;
        } else if (this.from == v && this.to == w) {
            return minDistance;
        } else if (this.from == w && this.to == v) {
            from = w;
            to = v;
            return minDistance;
        } else {
            from = v;
            to = w;
            fromBFS = new BreadthFirstDirectedPaths(digraphDFCopy, from);
            toBFS = new BreadthFirstDirectedPaths(digraphDFCopy, to);
            setupDefaultDataStructures();
            lockStepBFS();
        }
        return minDistance;
    }

    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }

        for (Integer v : vertices) {
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
        validateVertices(v);
        validateVertices(w);
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            iterablesAncestor = -1;
            iterablesMinimumDistance = -1;
            return iterablesMinimumDistance;
        }
        fromBFS = new BreadthFirstDirectedPaths(digraphDFCopy, v);
        toBFS = new BreadthFirstDirectedPaths(digraphDFCopy, w);
        int subsetDitance = INFINITY;
        int currentDistance = -1;
        int subsetAncestor = -1;
        //int prevDistance = minDistance;
        //int prevAncestor = ancestor;
        // System.out.printf("before the run ancestor = %d, minDistance = %d\n", ancestor, minDistance);
        for (int i : v) {
            for (int j : w) {
                // System.out.printf("i= %d, j= %d ", i, j);
                setupDefaultDataStructures();
                currentDistance = lockstepBFS(i, j, fromBFS, toBFS);
                if (subsetDitance > currentDistance) {
                    subsetAncestor = iterablesAncestor;
                    subsetDitance = currentDistance;
                    // System.out.printf("message from inside SAP: For nodes %d and %d ShortestDistance=%d \n", i, j, minDistance);
                }
                // System.out.printf(" SubsetAncestor= %d, SbsetDistance= %d\n", subsetAncestor, subsetDitance);
            }
        }
        // System.out.printf("after the run ancestor = %d, minDistance = %d\n", ancestor, minDistance);
        if (subsetDitance != INFINITY) {
            iterablesAncestor = subsetAncestor;
            iterablesMinimumDistance = subsetDitance;
        } else {
            iterablesAncestor = -1;
            iterablesMinimumDistance = -1;
        }
        return iterablesMinimumDistance;
    }

    // updates iterablesAncestor, and returns iterablesMinimumDistance
    private int lockstepBFS(int i, int j, BreadthFirstDirectedPaths fBS, BreadthFirstDirectedPaths tBS) {
        proceed = true;
        int currentDistance = INFINITY;
        fromQueue.enqueue(i);
        marked[i] = true;
        toQueue.enqueue(j);
        marked[j] = true;
        int v = 0;
        int distanceFromSourceCounter = 1;
        Iterator<Integer> var1;
        while (proceed) {
            while (!fromQueue.isEmpty() && fBS.distTo(fromQueue.peek()) < distanceFromSourceCounter) {
                v = fromQueue.dequeue();
                var1 = digraphDFCopy.adj(v).iterator();
                while (var1.hasNext()) {
                    int w = var1.next();
                    if (!marked[w]) {
                        fromQueue.enqueue(w);

                        marked[w] = true;
                    } else if (tBS.hasPathTo(w))
                        currentDistance = updateCurrentIterablesDistance(w, currentDistance, fBS, tBS);
                }
            }
            if (!proceed) break;
            while (!toQueue.isEmpty() && tBS.distTo(toQueue.peek()) < distanceFromSourceCounter) {
                v = toQueue.dequeue();
                var1 = digraphDFCopy.adj(v).iterator();
                while (var1.hasNext()) {
                    int w = var1.next();
                    if (!marked[w]) {
                        toQueue.enqueue(w);
                        marked[w] = true;
                    } else if (fBS.hasPathTo(w))
                        currentDistance = updateCurrentIterablesDistance(w, currentDistance, fBS, tBS);
                }
            }
            if (fromQueue.isEmpty() && toQueue.isEmpty()) break;
            distanceFromSourceCounter++;
        }
        if (currentDistance == INFINITY) {
            iterablesAncestor = -1;
            iterablesMinimumDistance = -1;
        } else {
            iterablesMinimumDistance = currentDistance;
        }
        proceed = true;
        return iterablesMinimumDistance;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        // System.out.println("Calculating the ancestor between : " + v + " " + w);
        if ((v < 0) || (v >= n))
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if ((w < 0) || (w >= n))
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if (v == w) {
            ancestor = w;
            minDistance = 0;
            from = v;
            to = w;
            return ancestor;
        } else if (this.from == v && this.to == w) {
            return ancestor;
        } else if (this.from == w && this.to == v) {
            from = w;
            to = v;
            return ancestor;
        } else {
            from = v;
            to = w;
            fromBFS = new BreadthFirstDirectedPaths(digraphDFCopy, from);
            toBFS = new BreadthFirstDirectedPaths(digraphDFCopy, to);
            setupDefaultDataStructures();
            lockStepBFS();
        }
        return ancestor;
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            iterablesAncestor = -1;
            iterablesMinimumDistance = -1;
            return iterablesAncestor;
        }
        fromBFS = new BreadthFirstDirectedPaths(digraphDFCopy, v);
        toBFS = new BreadthFirstDirectedPaths(digraphDFCopy, w);
        int ancestorSetDistance = INFINITY;
        int ancestorSetAncestor = -1;
        int currentDistance = -1;
        for (int i : v) {
            for (int j : w) {

                setupDefaultDataStructures();
                currentDistance = lockstepBFS(i, j, fromBFS, toBFS);
                if (ancestorSetDistance > currentDistance) {
                    ancestorSetDistance = currentDistance;
                    ancestorSetAncestor = iterablesAncestor;
                }
            }
        }

        if (ancestorSetDistance != INFINITY) {
            iterablesAncestor = ancestorSetAncestor;
            iterablesMinimumDistance = ancestorSetDistance;
        } else {
            iterablesAncestor = -1;
            iterablesMinimumDistance = -1;
        }
        return iterablesAncestor;
    }


    private void setupDefaultDataStructures() {
        fromQueue = new Queue<>();
        toQueue = new Queue<>();
        marked = new boolean[n];
    }

    private int updateCurrentIterablesDistance(int v, int currentDistance, BreadthFirstDirectedPaths fBS, BreadthFirstDirectedPaths tBS) {
        int distance = fBS.distTo(v) + tBS.distTo(v);
        if (distance < currentDistance) {
            currentDistance = distance;
            iterablesAncestor = v;
        }
        return currentDistance;
    }

    private int updateCurrentDistance(int v, int currentDistance) {
        int distance = fromBFS.distTo(v) + toBFS.distTo(v);
        if (distance > 0 && distance < currentDistance) {
            currentDistance = distance;
            ancestor = v;
        }
        if ((fromBFS.distTo(v) > currentDistance && toBFS.distTo(v) > currentDistance) || (distance == 1)) {
            proceed = false;
        }
        return currentDistance;
    }

    private void lockStepBFS() {
        proceed = true;
        int currentDistance = INFINITY;
        fromQueue.enqueue(from);

        marked[from] = true;

        toQueue.enqueue(to);

        marked[to] = true;

        int v = 0;

        int distanceFromSourceCounter = 1;
        Iterator<Integer> var1;
        while (proceed) {
            while (!fromQueue.isEmpty() && fromBFS.distTo(fromQueue.peek()) < distanceFromSourceCounter) {
                v = fromQueue.dequeue();
                if (v == to) {
                    int temp = fromBFS.distTo(v);
                    if (temp < currentDistance) {
                        currentDistance = temp;
                        ancestor = v;
                    }
                }
                var1 = digraphDFCopy.adj(v).iterator();
                while (var1.hasNext()) {
                    int w = var1.next();
                    if (!marked[w]) {
                        fromQueue.enqueue(w);

                        marked[w] = true;
                    } else if (toBFS.hasPathTo(w)) currentDistance = updateCurrentDistance(w, currentDistance);
                }
            }
            if (!proceed) break;
            while (!toQueue.isEmpty() && toBFS.distTo(toQueue.peek()) < distanceFromSourceCounter) {
                v = toQueue.dequeue();
                if (v == from) {
                    int temp = toBFS.distTo(v);
                    if (temp < currentDistance) {
                        currentDistance = temp;
                        ancestor = v;
                    }
                }
                var1 = digraphDFCopy.adj(v).iterator();
                while (var1.hasNext()) {
                    int w = var1.next();
                    if (!marked[w]) {
                        toQueue.enqueue(w);
                        marked[w] = true;
                    } else if (fromBFS.hasPathTo(w))
                        currentDistance = updateCurrentDistance(w, currentDistance);
                }
            }
            if (fromQueue.isEmpty() && toQueue.isEmpty()) break;
            distanceFromSourceCounter++;
        }
        if (currentDistance == INFINITY) {
            ancestor = -1;
            minDistance = -1;
        } else {
            minDistance = currentDistance;
        }
        proceed = true;
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
        System.out.printf("Expected ancestor: 8. Actual ancestor: %d\n", sap.ancestor(8, 14));
        System.out.printf("Test28 - (14, 8) expecting 4, getting: %d\n", sap.length(14, 8));
        System.out.printf("Expected ancestor: 8. Actual ancestor: %d\n", sap.ancestor(14, 8));

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
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(4, 1));
        System.out.printf("Test 10 - (2, 0) expecting 4, getting: %d\n", sap.length(2, 0));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(2, 0));
        System.out.printf("Test 11 - (0, 2) expecting 4, getting: %d\n", sap.length(0, 2));
        System.out.printf("Expected ancestor: 0. Actual ancestor: %d\n", sap.ancestor(0, 2));
    }
}