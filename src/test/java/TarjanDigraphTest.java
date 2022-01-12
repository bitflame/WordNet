import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class TarjanDigraphTest {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        TarjansAlg tarjansAlg = new TarjansAlg(digraph);
        System.out.printf("should be 0 4 3\n");
        for (int i: tarjansAlg.findSccs()) {
            System.out.printf("%d ",i);
        }

    }
}
