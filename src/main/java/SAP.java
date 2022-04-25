import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;


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
    private DeluxeBFS fromBFS;
    private DeluxeBFS toBFS;
    private Cache cache;
    private Node node;

    private class Node {
        int source;
        int destination;
        int minimumDistance;
        int ancestor;
        Node next;
        int hash = -1;

        private Node(int src, int dest, int minDist, int ances) {
            source = src;
            destination = dest;
            minimumDistance = minDist;
            ancestor = ances;
        }

        private Node(int src, int dest) {
            source = src;
            destination = dest;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return source == node.source && destination == node.destination;
        }

        @Override
        public int hashCode() {
            // M = 97
            int hash = 17;
            hash = (31 * hash + ((Integer) source).hashCode() & 0x7fffffff) % 97;
            hash = (31 * hash + ((Integer) destination).hashCode() & 0x7fffffff) % 97;
            this.hash = hash;
            return hash;
        }
    }

    private class Cache {

        int M = 97; // table size
        private Node[] table;

        private Cache() {
            table = new Node[M];
        }

        private Node get(Integer source, Integer destination) {
            Node node = new Node(source, destination);
            if (table[node.hashCode()] == null) return null;
            else {
                for (Node x = table[node.hash]; x != null; x = x.next) {
                    if (x.source == source && x.destination == destination) return x;
                }
            }
            return null;
        }

        private void put(Node node) {
            if (table[node.hashCode()] == null) {
                Node first = node;
                table[node.hash] = first;
            } else {
                node.next = table[node.hash];
                table[node.hash] = node;
            }
        }
    }

    // constructor takes a digraph ( not necessarily a DAG )
    public SAP(Digraph digraph) {
        if (digraph == null) throw new IllegalArgumentException("Digraph value can not be null");
        digraphDFCopy = new Digraph(digraph);
        minDistance = -1;
        ancestor = -1;
        from = -1;
        to = -1;
        n = digraphDFCopy.V();
        proceed = true;
        cache = new Cache();
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
        if (from == v && to == w) return minDistance;
        if (from == w && to == v) return minDistance;
        if (v == w && v != -1) {
            minDistance = 0;
            ancestor = v;
            from = v;
            to = w;
            return minDistance;
        }
        from = v;
        to = w;
        // are the nodes already in cache?
        try {
            node = cache.get(from, to);
            minDistance = node.minimumDistance;
            ancestor = node.ancestor;
            return minDistance;
        } catch (NullPointerException e) {
            // if not in cache add the new values
            testMethod(v, w);
            node = new Node(from, to, minDistance, ancestor);
            cache.put(node);
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
            return minDistance;
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
        if (from == v && to == w) return ancestor;
        if (from == w && to == v) return ancestor;
        if (v == w && v != -1) {
            minDistance = 0;
            ancestor = v;
            from = v;
            to = w;
            return ancestor;
        }
        from = v;
        to = w;
        //testMethod(v, w);
        // are the nodes already in cache?
        try {
            node = cache.get(from, to);
            minDistance = node.minimumDistance;
            ancestor = node.ancestor;
            return ancestor;
        } catch (NullPointerException e) {
            // if not in cache add the new values
            testMethod(v, w);
            node = new Node(from, to, minDistance, ancestor);
            cache.put(node);
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
        fromQueue = new Queue<>();
        toQueue = new Queue<>();
        marked = new boolean[n];
        fromStack = new Stack<>();
        toStack = new Stack<>();
        onFromStack = new boolean[n];
        onToStack = new boolean[n];
    }

    private void updateDataStructures() {
        int i = 0;
        while (!fromStack.isEmpty()) {
            i = fromStack.pop();
            marked[i] = false;
            onFromStack[i] = false;
            if (!fromQueue.isEmpty()) fromQueue.dequeue();
        }
        i = 0;
        while (!toStack.isEmpty()) {
            i = toStack.pop();
            marked[i] = false;
            onToStack[i] = false;
            if (!toQueue.isEmpty()) toQueue.dequeue();
        }
        minDistance = -1;
        ancestor = -1;
    }

    private int updateCurrentDistance(int v, int currentDistance, DeluxeBFS sBS, DeluxeBFS dBS) {
        int fromDist = sBS.distTo(v);
        int toDist = dBS.distTo(v);
        if (fromDist > currentDistance && toDist > currentDistance) proceed = false;
        int distance = sBS.distTo(v) + dBS.distTo(v);
        if (distance < currentDistance) {
            currentDistance = distance;
            ancestor = v;
        }
        return currentDistance;
    }

    private int updateCurrentIterDistance(int v, int w, int currentDistance, DeluxeBFS sBS, DeluxeBFS dBS) {
        int fromDist = sBS.distTo(w);
        int toDist = dBS.distTo(w);
        if (fromDist > currentDistance && toDist > currentDistance) {
            proceed = false;
            return currentDistance;
        }
        int distance = fromDist + toDist;


        int fr = w;
        while (fromDist > 0) {
            fr = sBS.pathTo(w).iterator().next();
            fromDist--;
        }

        from = fr;
        int ds = w;
        while (toDist > 0) {
            ds = dBS.pathTo(w).iterator().next();
            toDist--;
        }
        to = ds;
        /* distance is the shortest distance of w to one of the nodes on each side, and w is an ancestor since it is
        * reachable from both sides */
        node = new Node(from,to,distance,w);
        /* add the node to the cache */
        cache.put(node);
        if (distance < currentDistance) {
            currentDistance = distance;
            ancestor = w;
        }
        return currentDistance;
    }

    private int testMethod(Iterable<Integer> s, Iterable<Integer> d) {
        int currentDistance = INFINITY;
        boolean matchedAll = true;
        for (int i : s) {
            for (int j : d) {
                try {
                    node = cache.get(i, j);
                    if (currentDistance < node.minimumDistance) {
                        currentDistance = node.minimumDistance;
                        from = node.source;
                        to = node.destination;
                        minDistance = node.minimumDistance;
                        ancestor = node.ancestor;
                    }
                } catch (NullPointerException e) {
                    matchedAll = false;
                }
            }
        }
        // if all the nodes were in cache, minimum distance should be updated. Just return it. Otherwise currentDistance
        // should have a value we can start with
        if (matchedAll) return minDistance;
        try {
            fromBFS = fromBFS.updateSources(digraphDFCopy, s);
            toBFS = toBFS.updateSources(digraphDFCopy, d);
            updateDataStructures();
        } catch (NullPointerException e) {
            fromBFS = new DeluxeBFS(digraphDFCopy, s);
            toBFS = new DeluxeBFS(digraphDFCopy, d);
            setupDefaultDataStructures();
        } catch (IllegalArgumentException e) {
            ancestor = -1;
            minDistance = -1;
            from = -1;
            to = -1;
            return minDistance;
        }

        proceed = true;
        for (int i : s) {
            fromQueue.enqueue(i);
            marked[i] = true;
            fromStack.push(i);
            onFromStack[i] = true;
        }
        for (int j : d) {
            if (onFromStack[j]) {
                from = j;
                to = j;
                minDistance = 0;
                ancestor = j;
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

            while (!fromQueue.isEmpty() && fromBFS.distTo(fromQueue.peek()) < distanceFromSourceCounter) {
                v = fromQueue.dequeue();
                for (int w : digraphDFCopy.adj(v)) {
                    if (!marked[w]) {
                        fromQueue.enqueue(w);
                        fromStack.push(w);
                        onFromStack[w] = true;
                        marked[w] = true;
                    } else if (toBFS.hasPathTo(w)) {
                        currentDistance = updateCurrentIterDistance(v, w, currentDistance, fromBFS, toBFS);
                    }
                }
            }
            if (!proceed) break;
            while (!toQueue.isEmpty() && toBFS.distTo(toQueue.peek()) < distanceFromSourceCounter) {
                v = toQueue.dequeue();
                for (int w : digraphDFCopy.adj(v)) {
                    if (!marked[w]) {
                        toQueue.enqueue(w);
                        toStack.push(w);
                        onToStack[w] = true;
                        marked[w] = true;
                    } else if (fromBFS.hasPathTo(w)) {
                        currentDistance = updateCurrentIterDistance(v, w, currentDistance, fromBFS, toBFS);
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
        try {
            fromBFS = fromBFS.updateSource(digraphDFCopy, s);
            toBFS = toBFS.updateSource(digraphDFCopy, d);
            updateDataStructures();
        } catch (NullPointerException e) {
            fromBFS = new DeluxeBFS(digraphDFCopy, s);
            toBFS = new DeluxeBFS(digraphDFCopy, d);
            setupDefaultDataStructures();
        } catch (IllegalArgumentException e) {
            ancestor = -1;
            minDistance = -1;
            from = -1;
            to = -1;
            return minDistance;
        }
        int currentDistance = INFINITY;
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
            while (!fromQueue.isEmpty() && fromBFS.distTo(fromQueue.peek()) < distanceFromSourceCounter) {
                v = fromQueue.dequeue();
                for (int w : digraphDFCopy.adj(v)) {
                    if (!marked[w]) {
                        fromQueue.enqueue(w);
                        fromStack.push(w);
                        onFromStack[w] = true;
                        marked[w] = true;
                    } else if (onFromStack[w] && w == d) {
                        currentDistance = fromBFS.distTo(v) + 1;
                        ancestor = w;
                    } else if (toBFS.hasPathTo(w)) {
                        currentDistance = updateCurrentDistance(w, currentDistance, fromBFS, toBFS);
                    }
                }
            }
            if (!proceed) break;
            while (!toQueue.isEmpty() && toBFS.distTo(toQueue.peek()) < distanceFromSourceCounter) {
                v = toQueue.dequeue();
                for (int w : digraphDFCopy.adj(v)) {
                    if (!marked[w]) {
                        toQueue.enqueue(w);
                        marked[w] = true;
                        toStack.push(w);
                        onToStack[w] = true;
                    } else if (onToStack[w] && w == s) {
                        // if it is onStack you might have to compare distances to the node with the path it came from
                        currentDistance = toBFS.distTo(v) + 1;
                        ancestor = w;
                    } else if (fromBFS.hasPathTo(w)) {
                        currentDistance = updateCurrentDistance(w, currentDistance, fromBFS, toBFS);
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