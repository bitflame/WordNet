import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;


public class DeluxBFSTesting {
    int from = 9;
    int to = 5;



    public void doTheTest(Digraph G) {
        Queue<Integer> fromQueue = new Queue<>();
        Queue<Integer> toQueue   =new Queue<>();
        DeluxBFS fromBFS = new DeluxBFS(G,from);
        DeluxBFS toBFS = new DeluxBFS(G,to);

    }

    public static void main(String[] args) {
        DeluxBFSTesting deluxBFSTesting = new DeluxBFSTesting();
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        deluxBFSTesting.doTheTest(G);
    }
}
