import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;

public class SAP {
    private final Digraph digraphDFCopy;
    private int ancestor;
    private int minDistance;
    private int from;
    private int to;
    private static final int INFINITY = Integer.MAX_VALUE;
    private Stack<Integer> reversePost;
    private boolean[] marked;
    private int n;
    private BreadthFirstDirectedPaths fromBFS;
    private BreadthFirstDirectedPaths toBFS;

    // constructor takes a digraph ( not necessarily a DAG )
    public SAP(Digraph digraph) {
        if (digraph == null) throw new IllegalArgumentException("Digraph value can not be null");
        digraphDFCopy = new Digraph(digraph);
        n = digraphDFCopy.V();
        marked = new boolean[n];
        reversePost = new Stack<>();
        for (int v = 0; v < n; v++) {
            if (!marked[v]) dfs(v);
        }
    }

    private void dfs(int v) {
        marked[v] = true;
        for (int w : digraphDFCopy.adj(v))
            if (!marked[w]) dfs(w);
        reversePost.push(v);
    }

    private Iterable<Integer> reversePost() {
        return reversePost;
    }

    // length of the shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        // System.out.println("length(): Calculating the distance between : " + v + " " + w);
        if (v < 0 || v >= digraphDFCopy.V())
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if (w < 0 || w >= digraphDFCopy.V())
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if (v == from && w == to && v != w) return minDistance;
        from = v;
        to = w;
        if (v == w) {
            ancestor = v;
            minDistance = 0;
            return 0;
        }
        if ((digraphDFCopy.indegree(from) == 0 && digraphDFCopy.outdegree(from) == 0) || (digraphDFCopy.indegree(to) == 0 &&
                digraphDFCopy.outdegree(to) == 0)) {
            ancestor = -1;
            return minDistance = -1;
        }
        fromBFS = new BreadthFirstDirectedPaths(digraphDFCopy, v);
        toBFS = new BreadthFirstDirectedPaths(digraphDFCopy, w);
        lockStepBFS();
        return minDistance;
    }

    // length of the shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Iterable value to SAP.length() can not be null.\n");
        fromBFS = new BreadthFirstDirectedPaths(digraphDFCopy, v);
        toBFS = new BreadthFirstDirectedPaths(digraphDFCopy, w);
        lockStepBFS();
        return minDistance;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        // System.out.println("Calculating the ancestor between : " + v + " " + w);
        if (v < 0 || v >= digraphDFCopy.V())
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if (w < 0 || w >= digraphDFCopy.V())
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if (v < 0 || w < 0) throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if (this.from == v && this.to == w && v != w) return ancestor;
        from = v;
        to = w;
        if (v == w) {
            return ancestor = v;
        }
        if ((digraphDFCopy.indegree(from) == 0 && digraphDFCopy.outdegree(from) == 0) || (digraphDFCopy.indegree(to)
                == 0 && digraphDFCopy.outdegree(to) == 0)) {
            minDistance = -1;
            return ancestor = -1;
        }
        fromBFS = new BreadthFirstDirectedPaths(digraphDFCopy, from);
        toBFS = new BreadthFirstDirectedPaths(digraphDFCopy, to);
        lockStepBFS();
        return ancestor;
    }


    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Iterable value to SAP.ancestor() can not be null.\n");
        fromBFS = new BreadthFirstDirectedPaths(digraphDFCopy, v);
        toBFS = new BreadthFirstDirectedPaths(digraphDFCopy, w);
        lockStepBFS();
        return ancestor;
    }

    private void lockStepBFS() {
        int currentDistance = INFINITY;
        int distance = 0;
        for (int node : reversePost) {
            if (fromBFS.hasPathTo(node) && toBFS.hasPathTo(node)) {
                distance = fromBFS.distTo(node) + toBFS.distTo(node);
                if (distance < currentDistance) {
                    currentDistance = distance;
                    ancestor = node;
                    minDistance = distance;
                }
            }
        }
        if (currentDistance == INFINITY) {
            ancestor = -1;
            minDistance = -1;
        }
    }


    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In("digraph3.txt"));
        SAP sap = new SAP(digraph);
    }
}