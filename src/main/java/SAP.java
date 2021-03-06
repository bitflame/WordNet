import edu.princeton.cs.algs4.*;


public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final Digraph digraphDFCopy;
    private int ancestor;
    private int minDistance;
    private int from;
    private int to;
    private Stack<Integer> fromStack;
    private Stack<Integer> toStack;
    private boolean[] onFromStack;
    private boolean[] onToStack;
    private Queue<Integer> fromQueue;
    private Queue<Integer> toQueue;
    private final int n;
    private boolean proceed;
    private boolean[] marked;
    private Topological topological;

    // constructor takes a digraph ( not necessarily a DAG )
    public SAP(Digraph digraph) {
        if (digraph == null) throw new IllegalArgumentException("Digraph value can not be null");
        digraphDFCopy = new Digraph(digraph);
        topological = new Topological(digraphDFCopy);
        minDistance = -1;
        ancestor = -1;
        from = -1;
        to = -1;
        n = digraphDFCopy.V();
        proceed = true;
        //setupDefaultDataStructures();
    }


    // length of the shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        // System.out.println("length(): Calculating the distance between : " + v + " " + w);
        if ((v < 0) || (v >= n))
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if ((w < 0) || (w >= n))
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if ((digraphDFCopy.indegree(v) == 0 && digraphDFCopy.outdegree(v) == 0) || (digraphDFCopy.indegree(w) == 0 && digraphDFCopy.outdegree(w) == 0)) {
            return -1;
        }
        if (v == w && v != -1 && w != -1) {
            minDistance = 0;
            ancestor = v;
            from = v;
            to = w;
            return minDistance;
        } else if (this.from == v && this.to == w) {
            return minDistance;
        } else if (this.from == w && this.to == v) {
            return minDistance;
        } else {
            from = v;
            to = w;
            // lockStepBFS(v, w, fromBFS, toBFS);
            testMethod(v, w);
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
            //throw new IllegalArgumentException("Both lists should contain at least one value.");
            from = -1;
            to = -1;
            ancestor = -1;
            minDistance = -1;
        }
        testMethod(v, w);
        return minDistance;
    }


    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        // System.out.println("Calculating the ancestor between : " + v + " " + w);
        if ((v < 0) || (v >= n))
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if ((w < 0) || (w >= n))
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if ((digraphDFCopy.indegree(v) == 0 && digraphDFCopy.outdegree(v) == 0) || (digraphDFCopy.indegree(w) == 0 && digraphDFCopy.outdegree(w) == 0)) {
            return -1;
        }
        if (v == w && v != -1 && w != -1) {
            ancestor = w;
            minDistance = 0;
            return ancestor;
        } else if (this.from == v && this.to == w) {
            return ancestor;
        } else if (this.from == w && this.to == v) {
            from = v;
            to = w;
            return ancestor;
        } else {
            from = v;
            to = w;
            // lockStepBFS(v, w, fromBFS, toBFS);
            testMethod(v, w);
        }
        return ancestor;
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            from = -1;
            to = -1;
            ancestor = -1;
            minDistance = -1;
            return minDistance;
        }
        testMethod(v, w);
        return ancestor;
    }


    private void setupDefaultDataStructures() {
        minDistance = -1;
        ancestor = -1;
        fromQueue = new Queue<Integer>();
        toQueue = new Queue<Integer>();
        marked = new boolean[n];
        fromStack = new Stack<>();
        toStack = new Stack<>();
        onFromStack = new boolean[n];
        onToStack = new boolean[n];
    }

    private int updateCurrentDistance(int v, int currentDistance, BreadthFirstDirectedPaths sBS, BreadthFirstDirectedPaths dBS) {
        int distance = sBS.distTo(v) + dBS.distTo(v);
        if (distance < currentDistance) {
            currentDistance = distance;
            ancestor = v;
        }
        return currentDistance;
    }

    private int updateCurrentIterDistance(int v, int w, int currentDistance, BreadthFirstDirectedPaths sBS, BreadthFirstDirectedPaths dBS) {
        int fromDist = sBS.distTo(w);
        int toDist = dBS.distTo(w);
        int distance = fromDist + toDist;
        if (distance < currentDistance) {
            Stack<Integer> tempStack = fromStack;
            currentDistance = distance;
            // todo - Need to update from and to within test method. this way produces the wrong result
            ancestor = w;
            // from = ancestor;
            int fr = ancestor;
            while (sBS.distTo(fr) > 0 && !tempStack.isEmpty()) {
                //fr = tempStack.pop();
                fr = tempStack.pop();
            }
            // from is supposed to have path to ancestor not the other way
            from = fr;
            tempStack = toStack;
            // to = ancestor;
            int ds = ancestor;
            //while (tempStack.size() > 0 && toDist != 0) {
            while (dBS.distTo(ds) > 0 && !tempStack.isEmpty()) {
                // to = tempStack.pop();
                ds = tempStack.pop();
                // ds = toStack.peek();
                //toDist--;
            }
            to = ds;
        }
        return currentDistance;
    }

    // fr - from ds- destination frBFS, dsBFS, currDist
    private int updateIterativeDistance(int fr, int ds, int currDist, int currAncestor, BreadthFirstDirectedPaths frBFS,
                                        BreadthFirstDirectedPaths dsBFS) {
        int distance = dsBFS.distTo(fr) + frBFS.distTo(ds);
        if (distance < currDist) {
            currDist = distance;
            from = fr;
            to = ds;
            ancestor = currAncestor;
        }
        return currDist;
    }

    private int lockStepBFSIteratives(Iterable<Integer> s, Iterable<Integer> d) {
        BreadthFirstDirectedPaths iSBFS;
        BreadthFirstDirectedPaths iDBFS;
        try {
            iSBFS = new BreadthFirstDirectedPaths(digraphDFCopy, s);
            iDBFS = new BreadthFirstDirectedPaths(digraphDFCopy, d);
        } catch (IllegalArgumentException e) {
            ancestor = -1;
            minDistance = -1;
            from = -1;
            to = -1;
            return minDistance;
        }
        int distanceToNode = 1;
        int currentDistance = INFINITY;
        setupDefaultDataStructures();
        for (int i : s) {
            marked[i] = true;
            fromQueue.enqueue(i);
            fromStack.push(i);
            onFromStack[i] = true;
        }
        for (int j : d) {
            toQueue.enqueue(j);
            marked[j] = true;
            toStack.push(j);
            onToStack[j] = true;
            if (onFromStack[j]) {
                currentDistance = updateIterativeDistance(j, j, j, currentDistance, iSBFS, iDBFS); // should return 0
                from = j;
                to = j;
                ancestor = j;
                minDistance = currentDistance;
                return minDistance;
            }
        }
        int fr = fromQueue.dequeue();
        int ds = toQueue.dequeue();
        if (iSBFS.hasPathTo(ds)) updateIterativeDistance(fr, ds, currentDistance, ds, iSBFS, iDBFS);
        if (iDBFS.hasPathTo(fr)) updateIterativeDistance(fr, ds, currentDistance, fr, iSBFS, iDBFS);
        while (!fromQueue.isEmpty() && !toQueue.isEmpty()) {
            for (int i : digraphDFCopy.adj(fr)) {
                if (!marked[i]) {
                    marked[i] = true;
                    fromQueue.enqueue(i);
                    fromStack.push(i);
                    onFromStack[i] = true;
                }
            }

        }
        return -1; // not finished yet
    }

    private int testMethod(Iterable<Integer> s, Iterable<Integer> d) {
        BreadthFirstDirectedPaths iSBFS;
        BreadthFirstDirectedPaths iDBFS;
        try {
            iSBFS = new BreadthFirstDirectedPaths(digraphDFCopy, s);
            iDBFS = new BreadthFirstDirectedPaths(digraphDFCopy, d);
        } catch (IllegalArgumentException e) {
            ancestor = -1;
            minDistance = -1;
            from = -1;
            to = -1;
            return minDistance;
        }

        setupDefaultDataStructures();
        int currentDistance = INFINITY;
        proceed = true;
        for (int i : s) {
            fromQueue.enqueue(i);
            marked[i] = true;
            fromStack.push(i);
            onFromStack[i] = true;
        }
        for (int j : d) {
            if (onFromStack[j]){
                minDistance=0;
                ancestor=j;
                return minDistance;
            }
            toQueue.enqueue(j);
            marked[j] = true;
            toStack.push(j);
            onToStack[j] = true;
        }
        int v;
        int distanceFromSourceCounter = 1;
        while (proceed) {
            while (!fromQueue.isEmpty() && iSBFS.distTo(fromQueue.peek()) < distanceFromSourceCounter) {
                v = fromQueue.dequeue();
                for (int w : digraphDFCopy.adj(v)) {
                    if (!marked[w]) {
                        fromQueue.enqueue(w);
                        fromStack.push(w);
                        onFromStack[w] = true;
                        marked[w] = true;
                    } else if (iDBFS.hasPathTo(w)) {

                        currentDistance = updateCurrentIterDistance(v, w, currentDistance, iSBFS, iDBFS);
                    }
                }
            }
            if (!proceed) break;
            while (!toQueue.isEmpty() && iDBFS.distTo(toQueue.peek()) < distanceFromSourceCounter) {
                v = toQueue.dequeue();
                for (int w : digraphDFCopy.adj(v)) {
                    if (!marked[w]) {
                        toQueue.enqueue(w);
                        toStack.push(w);
                        onToStack[w] = true;
                        marked[w] = true;
                    } else if (iSBFS.hasPathTo(w)) {

                        currentDistance = updateCurrentIterDistance(v, w, currentDistance, iSBFS, iDBFS);
                    }
                }
            }
            if (fromQueue.isEmpty() && toQueue.isEmpty()) break;
            distanceFromSourceCounter++;
        }
        proceed = true;
        if (currentDistance != INFINITY) minDistance = currentDistance;
        else {
            minDistance = -1;
            ancestor = -1;
        }
        return minDistance;
    }

    private int testMethod(int s, int d) {
        BreadthFirstDirectedPaths sBFS = new BreadthFirstDirectedPaths(digraphDFCopy, s);
        BreadthFirstDirectedPaths dBFS = new BreadthFirstDirectedPaths(digraphDFCopy, d);
        setupDefaultDataStructures();
        int currentDistance = INFINITY;
        if (s == d) {
            minDistance = 0;
            ancestor = s;
            return minDistance;
        }
        proceed = true;
        fromQueue.enqueue(s);
        marked[s] = true;
        fromStack.push(s);
        onFromStack[s] = true;
        toQueue.enqueue(d);
        toStack.push(d);
        onToStack[d] = true;
        marked[d] = true;
        int v;
        int distanceFromSourceCounter = 1;
        while (proceed) {
            // if both have routes to it and distance is minimum
            while (!fromQueue.isEmpty() && sBFS.distTo(fromQueue.peek()) < distanceFromSourceCounter) {
                v = fromQueue.dequeue();
                for (int w : digraphDFCopy.adj(v)) {
                    if (!marked[w]) {
                        fromQueue.enqueue(w);
                        fromStack.push(w);
                        onFromStack[w] = true;
                        marked[w] = true;
                    } else if (onFromStack[w] && w == d) {
                        currentDistance = sBFS.distTo(v) + 1;
                        ancestor = w;
                    } else if (dBFS.hasPathTo(w)) {
                        currentDistance = updateCurrentDistance(w, currentDistance, sBFS, dBFS);
                    }
                }
            }
            if (!proceed) break;
            while (!toQueue.isEmpty() && dBFS.distTo(toQueue.peek()) < distanceFromSourceCounter) {
                v = toQueue.dequeue();
                for (int w : digraphDFCopy.adj(v)) {
                    if (!marked[w]) {
                        toQueue.enqueue(w);
                        marked[w] = true;
                        toStack.push(w);
                        onToStack[w] = true;
                    } else if (onToStack[w] && w == s) {
                        // if it is onStack you might have to compare distances to the node with the path it came from
                        currentDistance = dBFS.distTo(v) + 1;
                        ancestor = w;
                    } else if (sBFS.hasPathTo(w)) {
                        currentDistance = updateCurrentDistance(w, currentDistance, sBFS, dBFS);
                    }
                }
            }
            if (fromQueue.isEmpty() && toQueue.isEmpty()) break;
            distanceFromSourceCounter++;
        }

        proceed = true;
        if (currentDistance != INFINITY) minDistance = currentDistance;
        else {
            minDistance = -1;
            ancestor = -1;
        }
        return minDistance;
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