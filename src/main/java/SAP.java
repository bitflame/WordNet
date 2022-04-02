import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

import java.util.Iterator;


public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final Digraph digraphDFCopy;
    private int ancestor;
    private int minDistance;
    private int from;
    private int to;
    private BreadthFirstDirectedPaths fromBFS;
    private BreadthFirstDirectedPaths toBFS;
    private Queue<Integer> fromQueue;
    private Queue<Integer> toQueue;
    private boolean[] onFromStack;
    private boolean[] onToStack;
    private Stack<Integer> fromStack;
    private Stack<Integer> toStack;
    private final int n;
    private boolean proceed;
    private boolean[] marked;
    result r;

    private class result {
        int ancestor;
        int minDistance;

        public result(int ancestor, int minDistance) {
            this.ancestor = ancestor;
            this.minDistance = minDistance;
        }

        public result(int ancestor) {
            this.ancestor = ancestor;
        }

        public int getAncestor() {
            return ancestor;
        }

        public void setAncestor(int ancestor) {
            this.ancestor = ancestor;
        }

        public void setMinDistance(int minDistance) {
            this.minDistance = minDistance;
        }

        public int getMinDistance() {
            return minDistance;
        }
    }

    // constructor takes a digraph ( not necessarily a DAG )
    public SAP(Digraph digraph) {
        if (digraph == null) throw new IllegalArgumentException("Digraph value can not be null");
        digraphDFCopy = new Digraph(digraph);
        minDistance = -1;
        ancestor = -1;
        from = 0;
        to = 0;
        this.r = new result(-1, -1);
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
            ancestor = -1;
            minDistance = -1;
            return minDistance;
        }
        fromBFS = new BreadthFirstDirectedPaths(digraphDFCopy, v);
        toBFS = new BreadthFirstDirectedPaths(digraphDFCopy, w);
        int subsetDitance = INFINITY;
        int subsetAncestor = -1;
        //int prevDistance = minDistance;
        //int prevAncestor = ancestor;
        for (int i : v) {
            for (int j : w) {
                if (i == from && j == to) {
                    continue;
                } else if (j == from && i == to) {
                    from = i;
                    to = j;
                    continue;
                } else if (i == j) {
                    subsetAncestor = j;
                    subsetDitance = 0;
                    from = i;
                    to = j;
                    continue;
                } else {
                    from = i;
                    to = j;
                    setupDefaultDataStructures();
                    lockStepBFS();
                    // System.out.printf("message from inside SAP: For nodes %d and %d ShortestDistance=%d \n", i, j, minDistance);
                    if (subsetDitance > minDistance) {
                        subsetDitance = minDistance;
                        subsetAncestor = ancestor;
                    }
                }
            }
        }
        if (subsetDitance != INFINITY) {
            ancestor = subsetAncestor;
            minDistance = subsetDitance;
        }

        return minDistance;
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
            ancestor = -1;
            minDistance = -1;
            return ancestor;
        }
        fromBFS = new BreadthFirstDirectedPaths(digraphDFCopy, v);
        toBFS = new BreadthFirstDirectedPaths(digraphDFCopy, w);
        int ancestorSetDistance = INFINITY;
        int ancestorSetAncestor = -1;
        for (int i : v) {
            for (int j : w) {
                if (i == from && j == to) {
                    continue;
                } else if (j == from && i == to) {
                    from = i;
                    to = j;
                    continue;
                } else if (i == j) {
                    ancestorSetAncestor = j;
                    ancestorSetDistance = 0;
                    from = i;
                    to = j;
                    continue;
                } else {
                    from = i;
                    to = j;
                    setupDefaultDataStructures();
                    lockStepBFS();
                    if (ancestorSetDistance > minDistance) {
                        ancestorSetDistance = minDistance;
                        ancestorSetAncestor = ancestor;
                    }
                }
            }
        }
        if (ancestorSetDistance != INFINITY) {
            ancestor = ancestorSetAncestor;
            minDistance = ancestorSetDistance;
        }
        return ancestor;
    }


    private void setupDefaultDataStructures() {
        fromQueue = new Queue<>();
        toQueue = new Queue<>();
        fromStack = new Stack<>();
        toStack = new Stack<>();
        onFromStack = new boolean[n];
        onToStack = new boolean[n];
        marked = new boolean[n];
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

    // counts the number of cycle nodes on fromStack or toStack
    private int countCycleNodes(int w, boolean fStack, boolean tStack) {
        int tempCounter = 0;
        if (fStack) {
            while (fromStack.peek() != w) {
                fromStack.pop();
                tempCounter++;
            }
            tempCounter++;
        } else if (tStack) {
            while (toStack.peek() != w) {
                toStack.pop();
                tempCounter++;
            }
            tempCounter++;
        }
        return tempCounter;
    }

    private void lockStepBFS() {
        proceed = true;
        int currentDistance = INFINITY;
        fromQueue.enqueue(from);
        fromStack.push(from);
        marked[from] = true;
        onFromStack[from] = true;
        toQueue.enqueue(to);
        toStack.push(to);
        marked[to] = true;
        onToStack[to] = true;
        int v = 0;
        int deduct = 0;
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
                        fromStack.push(w);
                        onFromStack[w] = true;
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
                        toStack.push(w);
                        onToStack[w] = true;
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