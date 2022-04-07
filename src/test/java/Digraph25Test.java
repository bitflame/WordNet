import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Digraph25Test {
    static int INFINITY = Integer.MAX_VALUE;
    static Queue<Integer> fromQueue;
    static Queue<Integer> toQueue;
    static boolean[] marked;
    static int n;
    static Stack<Integer> fromStack;
    static Stack<Integer> toStack;
    static boolean[] onFromStack;
    static boolean[] onToStack;
    static int minDistance;
    static int ancestor;
    static boolean proceed;
    static In in = new In("digraph25.txt");
    static Digraph digraph = new Digraph(in);

    private static void setupDefaultDataStructures() {

        fromQueue = new Queue<>();
        toQueue = new Queue<>();
        n = digraph.V();
        marked = new boolean[n];
        fromStack = new Stack<>();
        toStack = new Stack<>();
        onFromStack = new boolean[n];
        onToStack = new boolean[n];
    }

    private static int updateCurrentDistance(int v, int currentDistance, BreadthFirstDirectedPaths sBS, BreadthFirstDirectedPaths dBS) {
        int distance = sBS.distTo(v) + dBS.distTo(v);
        if (distance < currentDistance) {
            currentDistance = distance;
            ancestor = v;
        }
        return currentDistance;
    }

    private static int testMethod(int s, int d, BreadthFirstDirectedPaths sBFS, BreadthFirstDirectedPaths dBFS) {
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
                for (int w : digraph.adj(v)) {
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
                for (int w : digraph.adj(v)) {
                    if (!marked[w]) {
                        toQueue.enqueue(w);
                        marked[w] = true;
                        toStack.push(w);
                        onToStack[w] = true;
                    } else if (onToStack[w] && w == s) {
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
        if (currentDistance != INFINITY) {
            minDistance = currentDistance;
        } else {
            minDistance = -1;
            ancestor = -1;
        }
        proceed = true;
        return minDistance;
    }

    public static void main(String[] args) {
        System.out.println("----------------------Running Digraph25Test---------------------");
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        int result = sap.length(13, 16);
        if (result != 4) throw new AssertionError("The length of the minimum distance between 13 and 16 " +
                "should be 4, but it is: " + result);
        result = sap.length(13, 6);
        if (result != 6) throw new AssertionError("The length of the minimum distance between 13 and 6 " +
                "should be 6, but is: " + result);
        List<Integer> sources = new ArrayList<>(Arrays.asList(13, 23, 24));
        List<Integer> destinations = new ArrayList<>(Arrays.asList(6, 16, 17));
        BreadthFirstDirectedPaths vBFDS = new BreadthFirstDirectedPaths(digraph, sources);
        BreadthFirstDirectedPaths wBFDS = new BreadthFirstDirectedPaths(digraph, destinations);
        int distance = INFINITY;
        int currentDistance = INFINITY;
        int currentAncestor = -1;
        for (int i : sources) {
            for (int j : destinations) {
                setupDefaultDataStructures();
                distance = testMethod(i, j, vBFDS, wBFDS);
                if ((distance != -1) && (currentDistance == INFINITY || distance < currentDistance)) {
                    currentDistance = distance;
                    currentAncestor = ancestor;
                }
            }
        }
        System.out.printf("current ancestor= %d current distance: %d\n", currentAncestor, currentDistance);
    }
}
