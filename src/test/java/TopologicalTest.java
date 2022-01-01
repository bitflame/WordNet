import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

public class TopologicalTest {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        Topological topological = new Topological(digraph);
        if (topological.hasOrder()) {
            for (int i : topological.order()) {
                System.out.printf("%d ", i);
            }
        } else System.out.printf("Seems the graph does not have order. ");
    }
}
