import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

public class SAP {
    private final Digraph digraphDFCopy;
    private int ancestor;
    private int minDistance;
    private int from;
    private int to;
    private static final int INFINITY = Integer.MAX_VALUE;
    private BreadthFirstDirectedPaths fromBFS;
    private BreadthFirstDirectedPaths toBFS;
    private Topological topological;
    private boolean keepSmallestAncestor = false;

    // constructor takes a digraph ( not necessarily a DAG )
    public SAP(Digraph digraph) {
        if (digraph == null) throw new IllegalArgumentException("Digraph value can not be null");
        digraphDFCopy = new Digraph(digraph);
        topological = new Topological(digraphDFCopy);
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
        fromBFS = new BreadthFirstDirectedPaths(digraphDFCopy, from);
        toBFS = new BreadthFirstDirectedPaths(digraphDFCopy, to);
        lockStepBFS(from, to);
        return minDistance;
    }

    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int count = 0;
        for (Integer v : vertices) {
            count++;
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
        if (v == null || w == null) throw new IllegalArgumentException("both lists can not be null");
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            ancestor = -1;
            minDistance = -1;
            return minDistance;
        }
        validateVertices(v);
        validateVertices(w);
        fromBFS = new BreadthFirstDirectedPaths(digraphDFCopy, v);
        toBFS = new BreadthFirstDirectedPaths(digraphDFCopy, w);
        keepSmallestAncestor = true;
        for (int i : v) {
            for (int j : w) {
                from = i;
                to = j;
                lockStepBFS(i, j);
            }
        }
        keepSmallestAncestor = false;
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
        lockStepBFS(from, to);
        return ancestor;
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException("both lists can not be null");
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            ancestor = -1;
            minDistance = -1;
            return ancestor;
        }
        validateVertices(v);
        validateVertices(w);
        fromBFS = new BreadthFirstDirectedPaths(digraphDFCopy, v);
        toBFS = new BreadthFirstDirectedPaths(digraphDFCopy, w);
        keepSmallestAncestor = true;
        for (int i : v) {
            for (int j : w) {
                from = i;
                to = j;
                lockStepBFS(from, to);
            }
        }
        keepSmallestAncestor = false;
        return ancestor;
    }

    private void lockStepBFS(int v, int w) {
        //from = v;
        //to = w;
        int prevAncestor = ancestor;
        int prevMinDistance = minDistance;
        int currentDistance = INFINITY;
        int distance = 0;
        if (topological.isDAG()) {
            for (int node : topological.order()) {
                if (fromBFS.hasPathTo(node) && toBFS.hasPathTo(node)) {
                    distance = fromBFS.distTo(node) + toBFS.distTo(node);
                    if (distance < currentDistance) {
                        currentDistance = distance;
                        ancestor = node;
                        minDistance = distance;
                    }
                }
            }
        } else for (int node = 0; node < digraphDFCopy.V(); node++) {
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
            if (keepSmallestAncestor) {
                ancestor = prevAncestor;
                minDistance = prevMinDistance;
            } else {
                ancestor = -1;
                minDistance = -1;
            }
        }
    }

    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In("digraph3.txt"));
        SAP sap = new SAP(digraph);
    }
}