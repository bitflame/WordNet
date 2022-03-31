import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class Digraph3Test {
    public static void main(String[] args) {
        System.out.println("----------------------Running Digraph3Test---------------------");
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        int distance = sap.length(10, 9);
        if (distance != 1) throw new AssertionError("The length between 10 and 9 should be 1, but it is: " + distance);
        int ancestor = sap.ancestor(10, 9);
        if (ancestor != 10)
            throw new AssertionError("The ancestor between 10 and 9 should be 10, but it is : " + ancestor);
        distance = sap.length(9, 10);
        if (distance != 1) throw new AssertionError("The length between 9 and 10 should be 1, but it is: " + distance);
        ancestor = sap.ancestor(9, 10);
        if (ancestor != 10)
            throw new AssertionError("The ancestor between 9 and 10 should be 10, but it is: " + ancestor);
        distance = sap.length(13, 14);
        if (distance != 1) throw new AssertionError("The length between 13 and 14 should be 1, but it is: " + distance);
        ancestor = sap.ancestor(13, 14);
        if (ancestor != 14)
            throw new AssertionError("The ancestor between 13, and 14 should be 14, but it is " + ancestor);
        distance = sap.length(14, 13);
        if (distance != 1) throw new AssertionError("The length between 14 and 13 should be 1, but it is: " + distance);
        ancestor = sap.ancestor(14, 13);
        if (ancestor != 14)
            throw new AssertionError("The ancestor between 14 and 13 should be 14, but it is: " + ancestor);
        distance = sap.length(13, 0);
        if (distance != 2) throw new AssertionError("The length between 13 and 0 should be 2, but it is: " + distance);
        ancestor = sap.ancestor(13, 0);
        if (ancestor != 0) throw new AssertionError("The ancestor between 13 and 0 should be 0 but it is: " + ancestor);
        distance = sap.length(0, 13);
        if (distance != 2) throw new AssertionError("The length between 0 and 13 should 2, but it is: " + distance);
        ancestor = sap.ancestor(0, 13);
        if (ancestor != 0)
            throw new AssertionError("The ancestor between 0 and 13 should be 0, but it is: " + ancestor);
        distance = sap.length(13, 12);
        if (distance != 4) throw new AssertionError("The length between 13 and 12 should be 4, but it is: " + distance);
        ancestor = sap.ancestor(13, 12);
        if (ancestor != 11)
            throw new AssertionError("The ancestor between 13 and 12 should be 11, but it is" + ancestor);
        distance = sap.length(12, 13);
        if (distance != 4) throw new AssertionError("The length between 12 and 13 should be 4, but it is: " + distance);
        ancestor = sap.ancestor(12, 13);
        if (ancestor != 11)
            throw new AssertionError("The ancestor between 12, and 13 should be 11, but it is: " + ancestor);
        distance = sap.length(7, 10);
        if (distance != 3) throw new AssertionError("The length between 10, and 7 shuld be 3, but it is: " + distance);
        ancestor = sap.ancestor(7, 10);
        if (ancestor != 8)
            throw new AssertionError("The ancestor between 7, and 10 should be 8, but it is: " + ancestor);
        distance = sap.length(10, 7);
        if (distance != 3) throw new AssertionError("The length between 10, and 7 shuld be 3, but it is: " + distance);
        ancestor = sap.ancestor(10, 7);
        if (ancestor != 8)
            throw new AssertionError("The ancestor between 7 and 10 should be 8, but it is: " + ancestor);
        distance = sap.length(5, 12);
        if (distance != -1)
            throw new AssertionError("5 and 12 are not connected, as such the length between them should be -1, but it is: " + distance);
        ancestor = sap.ancestor(5, 11);
        if (ancestor != -1)
            throw new AssertionError("The value of ancestor between 5 and 11 should be -1 since they are not connected, but it is: " + ancestor);
        ancestor = sap.ancestor(12, 7);
        if (ancestor != 8)
            System.out.printf("Ancestor for 12, and 7 in Graph3 should be 8, but it is: %d \n", ancestor);
        else System.out.printf("Test 1 ancestor for Graph3 passed.");
        distance = sap.length(12,7) ;
        if (distance!=2) System.out.printf("Shortest distance between 12, and 7 in Graph3 should be 2, but it is: %d\n", distance);
        else System.out.printf("Test 1 shortest distance for Graph3 passed.");
    }
}
