import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Misc {
    RedBlackBST<Integer, String> st;
    int[] ids;
    String[] data;
    In in;
    Queue<Integer> fromQueue, toQueue;
    boolean[] marked;
    Stack<Integer> fromStack, toStack;
    boolean[] onFromStack, onToStack;
    boolean proceed = true;
    int INFINITY = Integer.MAX_VALUE;
    Digraph digraph;
    int minDistance, ancestor, n;

    public Misc(int[] ids, String[] data) {
        in = new In("digraph3.txt");
        digraph = new Digraph(in);
        n = digraph.V();
        st = new RedBlackBST<>();
        for (int i = 0; i < data.length; i++) {
            st.put(ids[i], data[i]);
        }
    }

    private void setupDefaultDataStructures() {
        fromQueue = new Queue<>();
        toQueue = new Queue<>();
        marked = new boolean[n];
        fromStack = new Stack<>();
        toStack = new Stack<>();
        onFromStack = new boolean[n];
        onToStack = new boolean[n];
    }

    public void pritnAllDb() {
        for (int i : st.keys()) {
            for (String s : st.get(i).split(" ")) {
                if (s.equals("worm")) System.out.println("found  worm with id: " + i);
            }
        }
    }

    private int testMethod(int s, int d, DeluxeBFS sBFS, DeluxeBFS dBFS) {
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
        if (currentDistance != INFINITY) {
            minDistance = currentDistance;
        } else {
            minDistance = -1;
            ancestor = -1;
        }
        proceed = true;
        return minDistance;
    }

    private int updateCurrentDistance(int v, int currentDistance, DeluxeBFS sBS, DeluxeBFS dBS) {
        int distance = sBS.distTo(v) + dBS.distTo(v);
        if (distance < currentDistance) {
            currentDistance = distance;
            ancestor = v;
        }
        return currentDistance;
    }

    public static void main(String[] args) {
        String[] data = {"worm", "worm", "worm", "worm louse insect dirt_ball"};
        int[] ids = {81679, 81680, 81681, 81682};
        Misc misc = new Misc(ids, data);
        In in = new In("digraph3.txt");
        Digraph digraph = new Digraph(in);
        DeluxeBFS vBFDS;
        DeluxeBFS wBFDS;
        System.out.printf("Looking at the autograder test that failed using Graph 3 ...\n");
        in = new In("digraph3.txt");
        digraph = new Digraph(in);
        List<Integer> sources = new ArrayList<>(Arrays.asList(0, 2, 3, 7, 10));
        List<Integer> destinations = new ArrayList<>(Arrays.asList(1, 7, 12));
        vBFDS = new DeluxeBFS(digraph, sources);
        wBFDS = new DeluxeBFS(digraph, destinations);
        int distance = misc.INFINITY;
        int currentDistance = misc.INFINITY;
        int currentAncestor = -1;
        for (int i : sources) {
            for (int j : destinations) {
                misc.setupDefaultDataStructures();
                distance = misc.testMethod(i, j, vBFDS, wBFDS);
                if ((distance != -1) && (currentDistance == misc.INFINITY || distance < currentDistance)) {
                    currentDistance = distance;
                    currentAncestor = misc.ancestor;
                }
            }
        }
        System.out.printf("current ancestor= %d current distance: %d\n", currentAncestor, currentDistance);

        System.out.printf("path to 13 from sources BFS: \n");
        for (int x : vBFDS.pathTo(13)) System.out.printf(" %d", x);
        System.out.printf("\n path to 23 from sources BFS: ");
        for (int z : vBFDS.pathTo(23)) System.out.printf(" %d", z);
        System.out.printf("\n path to 24 from sources BFS: ");
        for (int z : vBFDS.pathTo(24)) System.out.printf(" %d", z);
        System.out.printf("\npath to 13 from destinations BFS: \n");
        if (wBFDS.hasPathTo(13)) {
            for (int q : wBFDS.pathTo(13)) System.out.printf(" %d", q);
        } else System.out.printf(" - ");
        System.out.printf("\npath to 23 from destinations BFS: \n");
        if (wBFDS.hasPathTo(23)) {
            for (int y : wBFDS.pathTo(23)) System.out.printf(" %d", y);
        } else System.out.printf(" - ");
        System.out.printf("\n");
        System.out.printf("\npath to 24 from destinations BFS: \n");
        if (wBFDS.hasPathTo(24)) {
            for (int y : wBFDS.pathTo(24)) System.out.printf(" %d", y);
        } else System.out.printf(" - ");
        System.out.printf("\n");
        System.out.printf(" distance to 3 from destinations: %d\n", wBFDS.distTo(3) + vBFDS.distTo(3));


        misc.pritnAllDb();
    }
}
