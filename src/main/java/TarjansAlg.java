import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.Stack;

public class TarjansAlg {
    private int n;
    private final Digraph digraphDFCopy;
    private int[] low;
    private int[] ids;
    private int id = 0;
    private boolean[] onStack;
    private boolean[] visited;
    private Stack<Integer> stack;
    private int sccCount = 0;
    public TarjansAlg(Digraph digraphDFCopy) {
        this.digraphDFCopy = digraphDFCopy;
    }

    public int[] findSccs() {
        n = digraphDFCopy.V();
        low = new int[n];
        onStack = new boolean[n];
        stack = new Stack<>();
        visited = new boolean[n];
        ids = new int[n];
        for (int i = 0; i < n; i++) {
            if (!visited[i]) dfs(i);
        }
        Bag<Integer> component = new Bag<>();
        SeparateChainingHashST<Integer, Bag<Integer>> sCcs = new SeparateChainingHashST<>();
        int j = 0;
        for (int i = 0; i < low.length; i++) {
            if (sCcs.get(low[i]) == null) {
                System.out.println("no list just yet");
                component = new Bag<>();
                component.add(i);
                sCcs.put(low[i], component);
            } else {
                component = sCcs.get(low[i]);
                component.add(i);
                sCcs.put(low[i], component);

            }

        }
        for (int i : sCcs.keys()) {
            System.out.printf("For key: %d %s", i, " the values are: ");
            for (int k : sCcs.get(i)) {
                System.out.print(" " + k);
            }
            System.out.println();
        }
        return low;
    }

    private void dfs(int i) {
        stack.push(i);
        onStack[i] = true;
        visited[i] = true;
        low[i] = ids[i] = id++;
        for (int j : digraphDFCopy.adj(i)) {
            if (!visited[j]) dfs(j);
            if (onStack[j]) low[i] = Math.min(low[i], low[j]);
        }
        /* If we at the start of a SCC empty the seen stack until we're back to the start of the SCC */
        if (ids[i] == low[i]) {
            for (int node : stack) {
                stack.pop();
                onStack[node] = false;
                low[node] = ids[i];
                if (node == i) break;
            }
            sccCount++;
        }
    }
}
