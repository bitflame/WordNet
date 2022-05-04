import edu.princeton.cs.algs4.*;

import java.util.*;


public class AutoGraderTests {
    In in;
    Digraph digraph;
    Digraph digraph1;
    Digraph digraph2;
    SAP sap;
    SAP sap1;
    SAP sap2;
    int shortestDistance;
    int ancestor;
    List<Integer> v;
    List<Integer> w;
    Topological topological;

    private void testRandomDigraph() {
        in = new edu.princeton.cs.algs4.In("digraph-wordnet.txt");
        digraph = new edu.princeton.cs.algs4.Digraph(in);
        sap = new SAP(digraph);
        in = new In("digraph5.txt");
        digraph = new Digraph(in);
        sap1 = new SAP(digraph);
        List<Iterable> sources = new ArrayList<>();
        List<Iterable> destinations = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            sources.add(List.of(StdRandom.uniform(0, 82191)));
            destinations.add(List.of(StdRandom.uniform(0, 82191)));
        }
        Iterator<Iterable> var1 = sources.iterator();
        Iterator<Iterable> var2 = destinations.iterator();
        int counter = 0;
        while (var1.hasNext()) {
            //System.out.printf(" %s", var1.next());
            //System.out.printf(" %s", var2.next());
            sap.length(var1.next(), var2.next());
            counter++;
            if (counter % 10 == 0) if (sap1.length(13, 21) != 4) break;
        }
        System.out.printf("Random Test completed without any issues.\n");
    }

    private void testDigraphWordNet() {
        StdOut.println("----------------------- Running AutoGrader Tests for DigraphWordNet int testDigraphWordNet()--------------------------");
        /*in = new In("myGraph5.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        System.out.printf("minimum distance: %d, ancestor: %d\n", sap.length(4, 1), sap.ancestor(4, 1));
        v = new ArrayList<>(Arrays.asList(4));
        w = new ArrayList<>(Arrays.asList(1, 2, 3, 0));
        System.out.printf("myGraph5 iterative  shortest distance: %d, ancestor: %d\n", sap.length(v, w), sap.ancestor(v, w));
        System.out.printf("minimum distance: %d, ancestor: %d\n", sap.length(4, 1), sap.ancestor(4, 1));*/

        in = new In("digraph-wordnet.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);

        v = new ArrayList<>(Arrays.asList(64451, 35205, 22477, 14493));
        w = new ArrayList<>(Arrays.asList(25327, 21385, 40151, 32530));
        ancestor = sap.ancestor(v, w);
        if (ancestor != 32530)
            System.out.printf("The first Iterables test of wordnet should have ancestor value of 32530, but the actual value is: %d\n", ancestor);
        else System.out.printf("The ancestor value for the first iterables test in wordnet passed.\n");
        shortestDistance = sap.length(v, w);
        if (shortestDistance != 1)
            System.out.printf("The first iterables test of wordnet should have a minimum distance of 1, but the actual value is: %d\n", shortestDistance);
        else System.out.printf("The shortest distance value for the first iterables test in wordnet passed.\n");

        v = new ArrayList<>(Arrays.asList(3464, 8331, 23405, 23889, 72520));
        w = new ArrayList<>(Arrays.asList(9675, 44260, 65806, 80452));
        shortestDistance = sap.length(v, w);
        if (shortestDistance != 8)
            System.out.printf("The second iterables test of wordnet should have a minimum distance of 8, but the actual value is: %d\n", shortestDistance);
        else System.out.printf("The shortest distance value for the second iterables test in wordnet passed.\n");
        ancestor = sap.ancestor(v, w);
        if (ancestor != 77444)
            System.out.printf("The second Iterables test of wordnet should have ancestor value of 77444, but the actual value is: %d\n", ancestor);
        else System.out.printf("The ancestor value for the second iterables test in wordnet passed.\n");
/*

        shortestDistance = sap.optimizedLength(v, w);

        if (shortestDistance != 8)
            System.out.printf("The Test of optimizedLength() with first value in cache in wordnet should have a minimum distance of 8, but the actual value is: %d\n", shortestDistance);
        else
            System.out.printf("The shortest distance value for the second iterables using optimizedLength with first value in cache test in wordnet passed.\n");
        sap = new SAP(digraph);
        sap.length(3464, 9675);
        sap.length(3464, 44260);
        sap.length(3464, 65806);
        sap.length(3464, 80452);
        ancestor = sap.optimizedAncestor(v, w);
        if (ancestor != 77444)
            System.out.printf("The test of optimizedAncestor with first value in cache in wordnet should have ancestor value of 77444, but the actual value is: %d\n", ancestor);
        else
            System.out.printf("The ancestor value for the first iterables using optimizedAncestor() with 2nd value in cache test in wordnet passed.\n");

        sap = new SAP(digraph);
        sap.length(8331, 9675);
        sap.length(8331, 44260);
        sap.length(8331, 65806);
        sap.length(8331, 80452);
        shortestDistance = sap.optimizedLength(v, w);
        if (shortestDistance != 8)
            System.out.printf("The Test of optimizedLength() with 2nd value in cache in wordnet should have a minimum distance of 8, but the actual value is: %d\n", shortestDistance);
        else
            System.out.printf("The shortest distance value for the second iterables using optimizedLength with 2nd value in cache test in wordnet passed.\n");
        sap = new SAP(digraph);
        sap.length(8331, 9675);
        sap.length(8331, 44260);
        sap.length(8331, 65806);
        sap.length(8331, 80452);
        ancestor = sap.optimizedAncestor(v, w);
        if (ancestor != 77444)
            System.out.printf("The test of optimizedAncestor with second value in cache in wordnet should have ancestor value of 77444, but the actual value is: %d\n", ancestor);
        else
            System.out.printf("The ancestor value for the first iterables using optimizedAncestor() with 2nd value in cache test in wordnet passed.\n");

        System.out.printf("(3464, 9675) shortest distance: %d ancestor: %d\n", sap.length(3464, 9675), sap.ancestor(3464, 9675));
        System.out.printf("(3464, 44260) shortest distance: %d ancestor: %d\n", sap.length(3464, 44260), sap.ancestor(3464, 44260));
        System.out.printf("(3464, 65806) shortest distance: %d ancestor: %d\n", sap.length(3464, 65806), sap.ancestor(3464, 65806));
        System.out.printf("(3464, 80452) shortest distance: %d ancestor: %d\n", sap.length(3464, 80452), sap.ancestor(3464, 80452));

        System.out.printf("(8331, 9675) shortest distance: %d ancestor: %d\n", sap.length(8331, 9675), sap.ancestor(8331, 9675));
        System.out.printf("(8331, 44260) shortest distance: %d ancestor: %d\n", sap.length(8331, 44260), sap.ancestor(8331, 44260));
        System.out.printf("(8331, 65806) shortest distance: %d ancestor: %d\n", sap.length(8331, 65806), sap.ancestor(8331, 65806));
        System.out.printf("(8331, 80452) shortest distance: %d ancestor: %d\n", sap.length(8331, 80452), sap.ancestor(8331, 80452));

        System.out.printf("(23405, 9675) shortest distance: %d ancestor: %d\n", sap.length(23405, 9675), sap.ancestor(23405, 9675));
        System.out.printf("(23405, 44260) shortest distance: %d ancestor: %d\n", sap.length(23405, 44260), sap.ancestor(23405, 44260));
        System.out.printf("(23405, 65806) shortest distance: %d ancestor: %d\n", sap.length(23405, 65806), sap.ancestor(23405, 65806));
        System.out.printf("(23405, 80452) shortest distance: %d ancestor: %d\n", sap.length(23405, 80452), sap.ancestor(23405, 80452));

        System.out.printf("(23889, 9675) shortest distance: %d ancestor: %d\n", sap.length(23889, 9675), sap.ancestor(23889, 9675));
        System.out.printf("(23889, 44260) shortest distance: %d ancestor: %d\n", sap.length(23889, 44260), sap.ancestor(23889, 44260));
        System.out.printf("(23889, 65806) shortest distance: %d ancestor: %d\n", sap.length(23889, 65806), sap.ancestor(23889, 65806));
        System.out.printf("(23889, 80452) shortest distance: %d ancestor: %d\n", sap.length(23889, 80452), sap.ancestor(23889, 80452));

        System.out.printf("(72520, 9675) shortest distance: %d ancestor: %d\n", sap.length(72520, 9675), sap.ancestor(72520, 9675));
        System.out.printf("(72520, 44260) shortest distance: %d ancestor: %d\n", sap.length(72520, 44260), sap.ancestor(72520, 44260));
        System.out.printf("(72520, 65806) shortest distance: %d ancestor: %d\n", sap.length(72520, 65806), sap.ancestor(72520, 65806));
        System.out.printf("(72520, 80452) shortest distance: %d ancestor: %d\n", sap.length(72520, 80452), sap.ancestor(72520, 80452));

        System.out.printf("(14493, 25327) shortest distance: %d ancestor: %d\n", sap.length(14493, 25327), sap.ancestor(14493, 25327));
        System.out.printf("(14493, 21385) shortest distance: %d ancestor: %d\n", sap.length(14493, 21385), sap.ancestor(14493, 21385));
        System.out.printf("(14493, 40151) shortest distance: %d ancestor: %d\n", sap.length(14493, 40151), sap.ancestor(14493, 40151));
        System.out.printf("(64451, 25327) shortest distance: %d ancestor: %d\n", sap.length(64451, 25327), sap.ancestor(64451, 25327));
       shortestDistance = sap.length(64451, 25327);
        if (shortestDistance != 15)
            System.out.printf("shortest distance between 64451, and 25327 in wordnet should be 15, but it is: %d \n", shortestDistance);
        ancestor = sap.ancestor(64451, 25327);
        System.out.printf("Ancestor between 64451 and 25327 is: %d\n", ancestor);

        shortestDistance = sap.length(64451, 21385);
        System.out.printf("Shortest distance between 64451, and 21385 is: %d\n", shortestDistance);
        ancestor = sap.ancestor(64451, 21385);
        System.out.printf("Ancestor between 64451, and 21385 is: %d\n", ancestor);
        shortestDistance = sap.length(64451, 40151);
        System.out.printf("Shortest distance between 64451, and 40151 is: %d\n", shortestDistance);
        ancestor = sap.ancestor(64451, 40151);
        System.out.printf("Ancestor between 64451, and 40151 is: %d\n", ancestor);
        shortestDistance = sap.length(35205, 25327);
        System.out.printf("Shortest distance between 35205, and 25327 is: %d\n", shortestDistance);
        ancestor = sap.ancestor(35205, 25327);
        System.out.printf("Ancestor between 35205, and 25327 is: %d\n", ancestor);
        shortestDistance = sap.length(35205, 21385);
        System.out.printf("Shortest distance between 35205, and 21385 is: %d\n", shortestDistance);
        ancestor = sap.ancestor(35205, 21385);
        System.out.printf("Ancestor between 35205, and 21385 is: %d\n", ancestor);
        shortestDistance = sap.length(35205, 40151);
        System.out.printf("Shortest distance between 35205, and 40151 is: %d\n", shortestDistance);
        ancestor = sap.ancestor(35205, 40151);
        System.out.printf("Ancestor between 35205, and 40151 is: %d\n", ancestor);
        shortestDistance = sap.length(22477, 25327);
        System.out.printf("Shortest distance between 22477, and 25327 is: %d\n", shortestDistance);
        ancestor = sap.ancestor(22477, 25327);
        System.out.printf("Ancestor between 22477, and 25327 is: %d\n", ancestor);
        shortestDistance = sap.length(22477, 21385);
        System.out.printf("Shortest distance between 22477, and 21385 is: %d\n", shortestDistance);
        ancestor = sap.ancestor(22477, 21385);
        System.out.printf("Ancestor between 22477, and 21385 is: %d\n", ancestor);
        shortestDistance = sap.length(22477, 40151);
        System.out.printf("Shortest distance between 22477, and 40151 is: %d\n", shortestDistance);
        ancestor = sap.ancestor(22477, 40151);
        System.out.printf("Ancestor between 22477, and 40151 is: %d\n", ancestor);
        shortestDistance = sap.length(14493, 4356);
        System.out.printf("Shortest distance between 14493, and 4356 is: %d\n", shortestDistance);
        ancestor = sap.ancestor(14493, 4356);
        System.out.printf("Ancestor between 14493, and 4356 is: %d\n", ancestor);
        shortestDistance = sap.length(21460, 4356);
        System.out.printf("Shortest distance between 21460, and 4356 is: %d\n", shortestDistance);
        ancestor = sap.ancestor(21460, 4356);
        System.out.printf("Ancestor between 21460, and 4356 is: %d\n", ancestor);

        StdOut.println("ancestor for 64451 and 25327 is: " + ancestor);
        shortestDistance = sap.length(35205, 21385);
        if (shortestDistance != 17)
            System.out.printf("shortest distance between 35205, and 21385 in wordnet should be 17, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(35205, 21385);
        StdOut.println("ancestor for 35205 and 21385 is: " + ancestor);
        shortestDistance = sap.length(53712, 61827);
        if (shortestDistance != 10)
            System.out.printf("shortest distance between 53712, and 61827 in wordnet should be 10, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(53712, 61827);
        StdOut.println("ancestor for 53712 and 61827 is: " + ancestor);
        shortestDistance = sap.length(45826, 73382);
        if (shortestDistance != 14)
            System.out.printf("shortest distance between 45826, and 73382 in wordnet should be 14, but it is: %d\n ", shortestDistance);
        ancestor = sap.ancestor(45826, 73382);
        StdOut.println("ancestor for 45826 and 73382 is: " + ancestor);
        shortestDistance = sap.length(2657, 55738);
        if (shortestDistance != 15)
            System.out.printf("shortest distance between 2657, and 55738 in wordnet should be 15, but it is: %d\n ", shortestDistance);
        shortestDistance = sap.length(22477, 40151);
        if (shortestDistance != 12)
            System.out.printf("The minimum distance between 22477, and 40151 should be 12, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 1 minimum distance between 22477, and 40151 in WordNet passed.\n");
        ancestor = sap.ancestor(22477, 40151);
        if (ancestor != 60600)
            System.out.printf("The ancestor between 22477, and 40151 should be 60600, but it is: %d\n", ancestor);
        else System.out.printf("Test 1 ancestor between 22477, and 40151 in WordNet passed.\n");
        ancestor = sap.ancestor(2657, 55738);
        StdOut.println("ancestor for 2657 and 55738 is: \n" + ancestor);
        List<Integer> sources = new ArrayList<>(Arrays.asList(17798, 19186, 32838, 38602, 46105, 48396, 54151, 61313, 65881, 69296, 81702));
        List<Integer> destinations = new ArrayList<>(Arrays.asList(14155, 23556, 63400));
        shortestDistance = sap.length(sources, destinations);
        if (shortestDistance != 7)
            System.out.printf("The expected answer for wordnet iterables is 7, but we get: %d\n", shortestDistance);
        shortestDistance = sap.length(sources, destinations);
        if (shortestDistance != 7)
            System.out.printf("The expected answer for wordnet iterables is 7, but we get: %d\n", shortestDistance);
        ancestor = sap.ancestor(sources, destinations);
        if (ancestor != 38003)
            System.out.printf("The expected ancestor for wordnet iterable is 38003, but we get: %d\n", ancestor);
        ancestor = sap.ancestor(sources, destinations);
        if (ancestor != 38003)
            System.out.printf("The expected ancestor for wordnet iterable is 38003, but we get: %d\n", ancestor); */

    }

    private void testSubgraphs() {
        StdOut.println("--------------------------- Running AutoGrader Tests for SubGraphs ------------------------------");
        WordNet wordNet = new WordNet("synsets100-subgraph.txt", "hypernyms100-subgraph.txt");
        StdOut.println("Expecting an error after passing a hypernym file that is invalid because it has two roots.");
        try {
            wordNet = new WordNet("synsets3.txt", "hypernyms3InvalidTwoRoots.txt");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void testDigraph1() {
        StdOut.println("---------------------------------- Running AutoGrader Tests for Digraph 1 ----------------------------------");
        in = new In("digraph1.txt");
        digraph = new Digraph(in);
        topological = new Topological(digraph);
        System.out.printf("Here are Graph1's nodes in topological order: \n");
        for (int i : topological.order()) {
            System.out.printf(" %d", i);
        }
        System.out.printf("\n");
        sap = new SAP(digraph);
        // even though node 6 is not in the digraph1 input file distance should be 0 - according to autograder feedback
        shortestDistance = sap.length(6, 6);
        if (shortestDistance != 0)
            System.out.printf("since there is no node 6 in Graph 1 the distance between nodes 6 and 6 should be -1 , but it is: %d\n ", shortestDistance);
        // even though node 6 is not in the digraph1 input file ancestor should be 6 - according to autograder feedback
        ancestor = sap.ancestor(6, 6);
        if (ancestor != 6)
            System.out.printf("since there is no node 6 in Graph 1 the ancestor for it should be -1, but it is: %d\n", ancestor);
        shortestDistance = sap.length(3, 3);
        if (shortestDistance != 0)
            System.out.printf("shortest distance between 3, and 3 should be 0, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(3, 3);
        if (ancestor != 3)
            System.out.printf("The value of ancestor between 3 and 3 should be 3, but it is: %d\n", ancestor);
        ancestor = sap.ancestor(2, 6);
        if (ancestor != -1) System.out.printf("The ancestor between 2 and 6 in graph 1 should be -1, and the actual" +
                "value is: %d\n", ancestor);
        shortestDistance = sap.length(2, 6);
        if (shortestDistance != -1)
            System.out.printf("The shortest distance between nodes 2 and 6 should be -1, but the actual " +
                    "value is: %d\n", shortestDistance);
        shortestDistance = sap.length(3, 8);
        if (shortestDistance != 1)
            System.out.printf("The value of length between 3 and 8 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(3, 8);
        if (ancestor != 3)
            System.out.printf("The value of ancestor between 3 and 8 should be 3, but it is: %d\n", ancestor);
        // 2, 6 should be in the cache
        ancestor = sap.ancestor(2, 6);
        if (ancestor != -1) System.out.printf("The ancestor between 2 and 6 in graph 1 should be -1, and the actual" +
                "value is: %d\n", ancestor);
        shortestDistance = sap.length(2, 6);
        if (shortestDistance != -1)
            System.out.printf("The shortest distance between nodes 2 and 6 should be -1, but the actual " +
                    "value is: %d\n", shortestDistance);
        // 3, 8 should be in the cache
        shortestDistance = sap.length(3, 8);
        if (shortestDistance != 1)
            System.out.printf("The value of length between 3 and 8 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(3, 8);
        if (ancestor != 3)
            System.out.printf("The value of ancestor between 3 and 8 should be 3, but it is: %d\n", ancestor);

        shortestDistance = sap.length(8, 3);
        if (shortestDistance != 1)
            System.out.printf("The value of length between 8 and 3 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(8, 3);
        if (ancestor != 3)
            System.out.printf("The ancestor between 8 and 3 in Graph 1 should be 3, but it is: %d\n", ancestor);
        ancestor = sap.ancestor(3, 7);
        if (ancestor != 3)
            System.out.printf("The value of ancestor between 3 and 7 should be 3, but it is: %d\n", ancestor);
        shortestDistance = sap.length(3, 7);
        if (shortestDistance != 1)
            System.out.printf("The value of length between 7 and 3 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(7, 3);
        if (ancestor != 3)
            System.out.printf("The value of ancestor between 7 and 3 should be 3, but it is: %d\n", ancestor);
        shortestDistance = sap.length(7, 3);
        if (shortestDistance != 1)
            System.out.printf("The shortest distance between 7 and 3 in Graph 3 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(8, 1);
        if (ancestor != 1)
            System.out.printf("The value of ancestor between 8 and 1 should be 1, but it is: %d\n", ancestor);
        shortestDistance = sap.length(8, 1);
        if (shortestDistance != 2)
            System.out.printf("The value of length between 8 and 1 should be 2, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(3, 1);
        if (ancestor != 1)
            System.out.printf("The value of ancestor between 3 and 1 should be 1, but it is: %d\n", ancestor);
        shortestDistance = sap.length(3, 1);
        if (shortestDistance != 1)
            System.out.printf("The value of length between 3 and 1 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(1, 3);
        if (ancestor != 1)
            System.out.printf("The value of ancestor between 1 and 3 in Graph 1 should be 1, but it is: %d\n", ancestor);
        shortestDistance = sap.length(1, 3);
        if (shortestDistance != 1)
            System.out.printf("The value of shortest distance between nodes 1 and 3 in Graph 3 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(7, 1);
        if (ancestor != 1)
            System.out.printf("The value of ancestor between 7 and 1 should be 1, but it is: %d\n", ancestor);
        shortestDistance = sap.length(7, 1);
        if (shortestDistance != 2)
            System.out.printf("The value of length between 7 and 1 should be 2, but it is: %d\n", shortestDistance);
        shortestDistance = sap.length(1, 1);
        if (shortestDistance != 0)
            System.out.printf("The value of length between 1 and 1 should be 0, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(1, 1);
        if (ancestor != 1) System.out.printf("The ancestor for 1, and 1 should be 1, but it is: %d\n ", ancestor);
        shortestDistance = sap.length(3, 11);
        if (shortestDistance != 4)
            System.out.printf("The value of length between 3 and 11 should be 4, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(3, 11);
        if (ancestor != 1)
            System.out.printf("The value of ancestor of 3 and 11 should be 1, but it is: %d\n", shortestDistance);
        shortestDistance = sap.length(9, 12);
        if (shortestDistance != 3)
            System.out.printf("The value of length between the nodes 9 and 12 should be 3, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(9, 12);
        if (ancestor != 5)
            System.out.printf("The value of ancestor between 9 and 12 should be 5, but it is: %d\n", ancestor);
        shortestDistance = sap.length(7, 2);
        if (shortestDistance != 4)
            System.out.printf("The value of length between the nodes 7, and 2 should be 4, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(7, 2);
        if (ancestor != 0)
            System.out.printf("The value of ancestor between 7 and 11 should be 0, but it is: %d\n", ancestor);
        shortestDistance = sap.length(1, 6);
        if (shortestDistance != -1)
            System.out.printf("The value of length between 1 and 6 should be -1 since they are not connected but it is: %d\n",
                    shortestDistance);
        ancestor = sap.ancestor(1, 6);
        if (ancestor != -1)
            System.out.printf("The value of ancestor between 1 and 6 should be -1 since they are not connected, but it is: %d\n", ancestor);

        v = new ArrayList<>(Arrays.asList(7));
        w = new ArrayList<>(Arrays.asList(8));
        ancestor = sap.ancestor(w, v);
        shortestDistance = sap.length(w, v);
        ancestor = sap.ancestor(v, w);
        shortestDistance = sap.length(v, w);


        v = new ArrayList<>(Arrays.asList(1));
        w = new ArrayList<>(Arrays.asList(7, 8));

        ancestor = sap.ancestor(v, w);
        shortestDistance = sap.length(v, w);

        v = new ArrayList<>(Arrays.asList(3));
        w = new ArrayList<>(Arrays.asList(8, 7, 1));
// todo - this is where from is set to 1 and ancestor to 3 but it should be the other way around
        ancestor = sap.ancestor(w, v);
        shortestDistance = sap.length(w, v);

        v = new ArrayList<>(Arrays.asList(1, 9, 10)); // todo - This might have a similar problem
        w = new ArrayList<>(Arrays.asList(5));
        ancestor = sap.ancestor(v, w);
        shortestDistance = sap.length(v, w);


        ancestor = sap.ancestor(v, w);
        if (ancestor != 5)
            System.out.printf("The expected ancestor value for iterables of Graph 3 should be either 5, or 1, but the " +
                    "actual value is: %d\n", ancestor);
        shortestDistance = sap.length(v, w);
        if (shortestDistance != 1)
            System.out.printf("The expected shortest distance for iterables of Graph 3 is 1, but the actual value is: %d\n",
                    shortestDistance);


        shortestDistance = sap.length(8, 3);
        if (shortestDistance != 1)
            System.out.printf("The value of length between 8 and 3 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(8, 3);
        if (ancestor != 3)
            System.out.printf("The ancestor between 8 and 3 in Graph 1 should be 3, but it is: %d\n", ancestor);

        ancestor = sap.ancestor(w, v);
        if (ancestor != 1)
            System.out.printf("The expected ancestor value for iterables sets of Graph 3 should be 3 or 1, but the actual " +
                    "value is: %d\n", ancestor);
        shortestDistance = sap.length(w, v);
        if (shortestDistance != 1)
            System.out.printf("The expected shortest distance for the iterables of Graph 3 is 1, but the" +
                    "actual value is: %d\n", shortestDistance);
        ancestor = sap.ancestor(3, 7);
        if (ancestor != 3)
            System.out.printf("The value of ancestor between 3 and 7 should be 3, but it is: %d\n", ancestor);
        shortestDistance = sap.length(3, 7);
        if (shortestDistance != 1)
            System.out.printf("The value of length between 7 and 3 should be 1, but it is: %d\n", shortestDistance);
        v = new ArrayList<>(Arrays.asList(8));
        w = new ArrayList<>(Arrays.asList(3));
        shortestDistance = sap.length(v, w);
        if (shortestDistance != sap.length(v, w))
            System.out.printf("The shortest distance of Iterables sets should be 1, but it is: %d\n  ", shortestDistance);
        ancestor = sap.ancestor(v, w);
        if (ancestor != 3)
            System.out.printf("Ancestor between iterables sets of Graph 1 should be 3, but it is: %d\n", ancestor);
        try {
            shortestDistance = sap.length(-1, 0);
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.printf(illegalArgumentException.getMessage());
        }
        try {
            ancestor = sap.ancestor(0, 13);
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.printf("Error message for non-exitant value: %s\n", illegalArgumentException.getMessage());
        }
        try {
            ancestor = sap.ancestor(13, 0);
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.printf("Error message for non-exitant value: %s\n", illegalArgumentException.getMessage());
        }
        v = new ArrayList<>(Arrays.asList(0));
        w = new ArrayList<>(Arrays.asList(3, 1));
        shortestDistance = sap.length(v, w);
        ancestor = sap.ancestor(v, w);
    }

    private void testDigraph2() {
        StdOut.println("---------------------------------- Running AutoGrader Tests for Digraph 2 ----------------------------------");
        in = new In("digraph2.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        topological = new Topological(digraph);
        System.out.printf("Here are Graph2's nodes in topological order: \n");
        for (int i : topological.order()) {
            System.out.printf(" %d", i);
        }
        System.out.printf("\n");
        v = new ArrayList<>(Arrays.asList(0));
        w = new ArrayList<>(Arrays.asList(4));
        shortestDistance = sap.length(v, w);
        if (shortestDistance != 2)
            System.out.printf("minimum distance between iterables sets in Graph2 should be 2, but the actual value is: %d\n", shortestDistance);
        else System.out.printf("Test 1's shortest distance value of iterables in Graph 2 passed.\n");
        ancestor = sap.ancestor(v, w);
        if (ancestor != 0)
            System.out.printf("The ancestor value for iterables sets in Graph2 should be 0, but the actual value is: %d\n", ancestor);
        else System.out.printf("Test 1's ancestor value of iterables in Graph 2 passed.\n");
        // test the reverse
        shortestDistance = sap.length(w, v);
        if (shortestDistance != 2)
            System.out.printf("minimum distance between iterables sets in Graph2 should be 2, but the actual value is: %d\n", shortestDistance);
        else System.out.printf("Test 2's shortest distance value of iterables in Graph 2 passed.\n");
        ancestor = sap.ancestor(w, v);
        if (ancestor != 0)
            System.out.printf("The ancestor value for iterables sets in Graph2 should be 0, but the actual value is: %d\n", ancestor);
        else System.out.printf("Test 2's ancestor value of iterables in Graph 2 passed.\n");
        v = new ArrayList<>(Arrays.asList(4));
        w = new ArrayList<>(Arrays.asList(1));
        // test the reverse also
        shortestDistance = sap.length(v, w);
        if (shortestDistance != 3)
            System.out.printf("minimum distance between iterables sets in Graph2 should be 3, but the actual value is: %d\n", shortestDistance);
        else System.out.printf("Test 3's shortest distance value of iterables in Graph 2 passed.\n");
        ancestor = sap.ancestor(v, w);
        if (ancestor != 0)
            System.out.printf("The ancestor value for iterables sets in Graph2 should be 0, but the actual value is: %d\n", ancestor);
        else System.out.printf("Test 3's ancestor value of iterables in Graph 2 passed.\n");
        shortestDistance = sap.length(w, v);
        if (shortestDistance != 3)
            System.out.printf("minimum distance between iterables sets in Graph2 should be 3, but the actual value is: %d\n", shortestDistance);
        else System.out.printf("Test 4's shortest distance value of iterables in Graph 2 passed.\n");
        ancestor = sap.ancestor(w, v);
        if (ancestor != 0)
            System.out.printf("The ancestor value for iterables sets in Graph2 should be 0, but the actual value is: %d\n", ancestor);
        else System.out.printf("Test 4's ancestor value of iterables in Graph 2 passed.\n");
        shortestDistance = sap.length(4, 1);
        if (shortestDistance != 3)
            System.out.printf("Distance between 4, and 1 should be 3, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(4, 1);
        if (ancestor != 0)
            System.out.printf("The value of ancestor between 4 and 1 should be 0, but it is: %d\n", ancestor);
        shortestDistance = sap.length(4, 0);
        if (shortestDistance != 2)
            System.out.printf("Distance between 4, and 0 should be 2, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(4, 0);
        if (ancestor != 0)
            System.out.printf("The value of ancestor between 4, and 0 should be 0, but it is: %d\n", shortestDistance);
        shortestDistance = sap.length(1, 1);
        if (shortestDistance != 0)
            System.out.printf("The shortest distance for the same point should be 0, but it is: %d\n", shortestDistance);
        shortestDistance = sap.length(1, 5);
        if (shortestDistance != 2)
            System.out.printf("The shortest distance between 1 and 5 should be 2, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(1, 5);
        if (ancestor != 0)
            System.out.printf("The ancestor for nodes 1 and 5 should be 0, but it is: %d\n", ancestor);
        shortestDistance = sap.length(3, 4);
        if (shortestDistance != 1)
            System.out.printf("Shortest distance between nodes 3 and 4 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(3, 4);
        if (ancestor != 4)
            System.out.printf("Ancestor between nodes 3 and 4 for graph2 should be 4, but it is: %d\n", ancestor);
        shortestDistance = sap.length(4, 3);
        if (shortestDistance != 1)
            System.out.printf("The distance between 4 and 3 in Graph 2 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(4, 3);
        if (ancestor != 4)
            System.out.printf("The ancestor for nodes 4 and 3 in Graph 2 should be 4, but it is : %d\n", ancestor);

    }

    private void testDigraph3() {
        StdOut.println("----------------------------------Running AutoGrader Tests for Digraph 3 ----------------------------------");
        in = new In("digraph3.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        topological = new Topological(digraph);
        System.out.printf("Here are Graph3's nodes in topological order: \n");
        try {
            for (int i : topological.order()) {
                System.out.printf(" %d", i);
            }
        } catch (NullPointerException e) {
            StdOut.println(e.getMessage());
        }

        System.out.printf("\n");
        ancestor = sap.ancestor(12, 7);
        if (ancestor != 8)
            System.out.printf("Ancestor value of Test 1 for 12, and 7 in Graph3 should be 8, but it is: %d \n", ancestor);
        else System.out.printf("Test 1 ancestor for Graph3 passed.\n");
        shortestDistance = sap.length(12, 7);
        if (shortestDistance != 2)
            System.out.printf("Shortest distance of Test 1 between 12, and 7 in Graph3 should be 2, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 1 shortest distance for Graph3 passed.\n");
        v = new ArrayList<>(Arrays.asList(12));
        w = new ArrayList<>(Arrays.asList(7));
        if (ancestor != 8)
            System.out.printf("Ancestor value of iterative version of test 1 for 12, and 7 in Graph3 should be 8, but it is: %d \n", ancestor);
        else System.out.printf("ancestor value for Test 1's iterative version in Graph3 passed.\n");
        shortestDistance = sap.length(12, 7);
        if (shortestDistance != 2)
            System.out.printf("Iterative version of Test 1's Shortest distance between 12, and 7 in Graph3 should be 2, but it is: %d\n", shortestDistance);
        else System.out.printf("Iterative version of Test 1 shortest distance for Graph3 passed.\n");
        // all the tests in Graph3 are also done with spock in the other project, and I did the iterables version also
        shortestDistance = sap.length(10, 7);
        if (shortestDistance != 3)
            System.out.printf("Test 2 Shortest Distance between 10 and 7 in Graph 3 should be 3, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 2's shortest distance in Graph 3 passed.\n");
        ancestor = sap.ancestor(10, 7);
        if (ancestor != 10)
            System.out.printf("Test 2 - The value of ancestor between 10 and 7 in Graph 3 should be 10, but it is: %d\n", ancestor);
        else System.out.printf("Test 2's ancestor value in Graph3 passed.\n");


        shortestDistance = sap.length(7, 13);
        if (shortestDistance != 6)
            System.out.printf("Distance between 7 and 13 should be 6, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(7, 13);
        if (ancestor != 8) System.out.printf("The ancestor between 7 and 13 should be 8, but it is: %d\n ", ancestor);
        shortestDistance = sap.length(5, 14);
        if (shortestDistance != -1)
            System.out.printf("Distance between 5 and 14 should be -1 since the two nodes are not connected, but it is: " +
                    "%d\n", shortestDistance);
        ancestor = sap.ancestor(5, 14);
        if (ancestor != -1)
            System.out.printf("The value of ancestor between 5 and 14 should be -1, but it is: %d\n", ancestor);
        shortestDistance = sap.length(2, 13);
        if (shortestDistance != -1)
            System.out.printf("Distance between 2 and 13 should be -1 since the two nodes are not connected, but it is: %d\n ", shortestDistance);
        ancestor = sap.ancestor(2, 13);
        if (ancestor != -1)
            System.out.printf("The value of ancestor between 2 and 13 should be -1, but it is: %d\n", ancestor);
        shortestDistance = sap.length(10, 3);
        if (shortestDistance != -1)
            System.out.printf("Distance between 10 and 3 should be -1 since the two nodes are not connected, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(10, 3);
        if (ancestor != -1)
            System.out.printf("The value of ancestor between 10 and 3 should be -1, but it is: %d\n", ancestor);
        ancestor = sap.ancestor(7, 11);
        if (ancestor != 8)
            System.out.printf("The value of ancestor between 7 and 11 should be 8, but it is: %d\n", ancestor);
        shortestDistance = sap.length(7, 11);
        if (shortestDistance != 3)
            System.out.printf("The distance between 7 and 11 should be 3, but it is: %d\n", shortestDistance);
        shortestDistance = sap.length(2, 3);
        if (shortestDistance != 1)
            System.out.printf("The distance between 2 and 3 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(2, 3);
        if (ancestor != 3)
            System.out.printf("The value of ancestor between 2 and 3 should be 3, but it is: %d\n", ancestor);
        shortestDistance = sap.length(10, 9);
        if (shortestDistance != 1)
            System.out.printf("The distance between 10 and 9 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(10, 9);
        if (ancestor != 10)
            System.out.printf("The value of ancestor between 10 and 9 should be 10, but it is: %d\n", ancestor);
        shortestDistance = sap.length(12, 13);
        if (shortestDistance != 4)
            System.out.printf("The distance between 12 and 13 should be 4, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(12, 13);
        if (ancestor != 12)
            System.out.printf("The value of ancestor between 12 and 13 should be 12, but it is: %d\n", ancestor);
        ancestor = sap.ancestor(10, 11);
        if (ancestor != 11)
            System.out.printf("The value of ancestor between 10 and 11 should be 11, but it is: %d\n", ancestor);
        shortestDistance = sap.length(10, 11);
        if (shortestDistance != 1)
            System.out.printf("The distance between 10 and 11 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(10, 11);
        if (ancestor != 11)
            System.out.printf("The value of ancestor between 11 and 10 should be 11, but it is: %d\n", shortestDistance);
        shortestDistance = sap.length(11, 10);
        if (shortestDistance != 1)
            System.out.printf("The distance between 11 and 10 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(11, 10);
        if (ancestor != 11)
            System.out.printf("The value of ancestor between 11 and 10 should be 11, but it is: %d\n", ancestor);
        shortestDistance = sap.length(8, 13);
        if (shortestDistance != 5)
            System.out.printf("The distance between 8 and 13 should be 5, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(8, 13);
        if (ancestor != 8)
            System.out.printf("The value of ancestor between 8 and 13 should be 8, but it is: %d\n", ancestor);
        shortestDistance = sap.length(9, 2);
        if (shortestDistance != -1)
            System.out.printf("The distance between 9 and 2 should be -1 since they are not " +
                    "connected, but it actually is: %d\n", shortestDistance);
        ancestor = sap.ancestor(9, 2);
        if (ancestor != -1)
            System.out.printf("The ancestor between the nodes 9 and 2 should be -1 since they are not " +
                    "connected, but it actually comes up as: %d\n", ancestor);
        shortestDistance = sap.length(10, 14);
        if (shortestDistance != 3)
            System.out.printf("The distance between 10 and 14 should be 3, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(10, 14);
        if (ancestor != 11)
            System.out.printf("The ancestor of 10, and 14 should be node 11, but it is: %d\n ", ancestor);
        shortestDistance = sap.length(2, 4);
        if (shortestDistance != 2)
            System.out.printf("The distance between 2 and 4 should be 2, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(2, 4);
        if (ancestor != 4) System.out.printf("The ancestor of 2 and 4 should be 2, but it is: %d\n", ancestor);
        ancestor = sap.ancestor(14, 9);
        if (ancestor != 11)
            System.out.printf("The ancestor between 14 and 9 for graph 3 should be 11, but it is: %d\n", ancestor);
        shortestDistance = sap.length(14, 9);
        if (shortestDistance != 4)
            System.out.printf("The shortest distance between nodes 14 and 9 should be 4, but it is: %d\n  ", shortestDistance);
        shortestDistance = sap.length(13, 11);
        if (shortestDistance != 3)
            System.out.printf("The distance between nodes 13 and 11 in graph 3 should be 3, but" +
                    "it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(13, 11);
        if (ancestor != 11)
            System.out.printf("The ancestor for nodes 13 and 11 in graph 3 should be 11, but it is: %d\n", ancestor);
        shortestDistance = sap.length(13, 12);
        if (shortestDistance != 4)
            System.out.printf("The distance between the nodes 13 and 12 in graph 3 should be 4, but" +
                    "it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(13, 12);
        if (ancestor != 12)
            System.out.printf("The ancestor for nodes 13 and 12 in graph 3 should be 12, but it is: %d\n", ancestor);
        // 8,11
        shortestDistance = sap.length(8, 11);
        if (shortestDistance != 2) System.out.printf("The distance between the nodes 8 and 11 in graph 3 should be 2," +
                "but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(8, 11);
        if (ancestor != 8)
            System.out.printf("The ancestor between nodes 8, and 11 in graph 3 should be 8, but it is: %d\n", ancestor);
        // 8,12
        shortestDistance = sap.length(8, 12);
        if (shortestDistance != 1)
            System.out.printf("The distance between the nodes 8 and 12 in graph 3 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(8, 12);
        if (ancestor != 8)
            System.out.printf("The ancestor for the nodes 8 and 12 in graph 3 should be 8, but it is: %d\n", ancestor);
        v = new ArrayList<>(java.util.Arrays.asList(13, 8));
        w = new java.util.ArrayList<>(java.util.Arrays.asList(11, 12));
        shortestDistance = sap.length(v, w);
        if (shortestDistance != 1)
            StdOut.println("Expecting the shortest distance for two iterables to be 1, and it is: " + shortestDistance);
        shortestDistance = sap.length(7, 4);
        if (shortestDistance != -1)
            System.out.printf("Expecting the shortest distance for nodes from different components " +
                    "to be -1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(7, 4);
        if (ancestor != -1) System.out.printf("Expected the ancestor for nodes from different components in graph 3 " +
                "to be -1, but it is: %d\n", ancestor);
        v = new ArrayList<>(java.util.Arrays.asList(7));
        w = new java.util.ArrayList<>(java.util.Arrays.asList(4));
        shortestDistance = sap.length(v, w);
        if (shortestDistance != -1)
            StdOut.println("Expecting the shortest distance for two iterables to be -1, and it is: " + shortestDistance);
        ancestor = sap.ancestor(v, w);
        if (ancestor != -1)
            StdOut.println("Expecting the ancestor for two iterables to be -1, and it is: " + ancestor);

    }

    private void testDigraph4() {
        StdOut.println("----------------------------------Running AutoGrader Tests for Digraph 4 ----------------------------------");
        in = new In("digraph4.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        topological = new Topological(digraph);
        System.out.printf("Here are Graph4's nodes in topological order: \n");
        for (int i : topological.order()) {
            System.out.printf(" %d", i);
        }
        System.out.printf("\n");
        shortestDistance = sap.length(1, 4);
        if (shortestDistance != 3)
            System.out.printf("The distance between 1 and 4 should be 3, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(1, 4);
        if (ancestor != 4)
            System.out.printf("The value of ancestor between 1 and 4 should be 4, but it is: %d\n", ancestor);
        shortestDistance = sap.length(4, 1);
        if (shortestDistance != 3)
            System.out.printf("he distance between 4 and 1 should be 3, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(4, 1);
        if (ancestor != 4)
            System.out.printf("The value of ancestor between 4 and 1 should be 4, but it is: %d\n", ancestor);
        shortestDistance = sap.length(9, 3);
        if (shortestDistance != 6)
            System.out.printf("The distance between 9 and 3 should be 6, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(9, 3);
    }

    private void testDigraph5() {
        StdOut.println("----------------------------------Running AutoGrader Tests for Digraph 5 ----------------------------------");
        in = new In("digraph5.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        topological = new Topological(digraph);
        System.out.printf("Here are Graph5's nodes in topological order: \n");
        for (int i : topological.order()) {
            System.out.printf(" %d", i);
        }
        System.out.printf("\n");
        shortestDistance = sap.length(17, 21);
        if (shortestDistance != 5)
            System.out.printf("The distance between 17 and 21 should be 5, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 1 of Graph5 shortest distance passed. \n");
        ancestor = sap.ancestor(17, 21);
        if (ancestor != 21)
            System.out.printf("ancestor between 17, and 21 ing Graph5 should be 21, but it is : %d\n", ancestor);
        else System.out.printf("Test 1 of Graph5 ancestor passed.\n");
        shortestDistance = sap.length(14, 21);
        if (shortestDistance != 8)
            System.out.printf("The distance between 14 and 21 should be 8, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 2 of Graph5 shortest distance passed.\n");
        ancestor = sap.ancestor(14, 21);
        if (ancestor != 21)
            System.out.printf("The distance between 14 and 21 in Graph5 should be 21, but it is: %d\n", ancestor);
        else System.out.printf("Test 2 of Graph5 ancestor passed.\n");
        shortestDistance = sap.length(9, 20);
        if (shortestDistance != 3)
            System.out.printf("The distance between 9 and 20 should be 3, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 3 of Graph5 shortest distance passed.\n");
        ancestor = sap.ancestor(9, 20);
        if (ancestor != 9)
            System.out.printf("The ancestor between 9, and 20 in graph5 should be 20, but it is: %d\n", ancestor);
        else System.out.printf("Test 3 of Graph5 ancestor passed.\n");
        ancestor = sap.ancestor(10, 12);
        if (ancestor != 10) System.out.printf("The value of ancestor should be 10, and it is: %d\n ", ancestor);
        else System.out.printf("Test 4 of Graph5 ancestor passed.\n");
        shortestDistance = sap.length(10, 12);
        if (shortestDistance != 2)
            System.out.printf("The distance between 10 and 12 should be 2, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 4 of Graph5 shortest distance passed.\n");
        shortestDistance = sap.length(21, 0);
        if (shortestDistance != 1)
            System.out.printf("The distance between 21, and 0 should be 1, and it is: %d\n", shortestDistance);
        else System.out.printf("Test 5 of Graph5 shortest distance passed.\n");
        ancestor = sap.ancestor(21, 0);
        if (ancestor != 0)
            System.out.printf("ancestor between 21 and 0 in Graph5 should be 0, but it is: %d\n", ancestor);
        else System.out.printf("Test 5 of Graph5 ancestor passed.\n");
        shortestDistance = sap.length(7, 12);
        if (shortestDistance != 5)
            System.out.printf("The distance between 7 and 12 should be 5, but it is: %d\n ", shortestDistance);
        else System.out.printf("Test 6 of Graph5 shortest distance passed.\n");
        ancestor = sap.ancestor(7, 12);
        if (ancestor != 10)
            System.out.printf("The ancestor for nodes 7 and 12 in graph 5 should be 13, but it is: %d\n", ancestor);
        else System.out.printf("Test 6 of Graph5 ancestor passed.\n");
        shortestDistance = sap.length(13, 21);
        if (shortestDistance != 4)
            System.out.printf("The distance between nodes 13, and 21 should be 4, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 7 of Graph5 shortest distance passed.\n");
        ancestor = sap.ancestor(13, 21);
        if (ancestor != 10)
            System.out.printf("The ancestor for nodes 13, and 21 in graph 3 should be 10, but it is: %d\n", ancestor);
        else System.out.printf("Test 7 of Graph5 ancestor passed.\n");
        shortestDistance = sap.length(7, 8);
        if (shortestDistance != 1)
            System.out.printf("The distance between nodes 7 and 8 should be 1, but it is: %d\n ", shortestDistance);
        else System.out.printf("Test 8 of Graph5 shortest distance passed.\n");
        ancestor = sap.ancestor(7, 8);
        if (ancestor != 8)
            System.out.printf("The ancestor for the nodes 7 and 8 in graph 5 should be 8, but it is: %d\n", ancestor);
        else System.out.printf("Test 8 of Graph5 ancestor passed.\n");
        System.out.printf("Testing Iterables in Graph5");
        List<Integer> sources = new ArrayList<>(Arrays.asList(7, 7));
        List<Integer> destinations = new ArrayList<>(Arrays.asList(8, 8));
        ancestor = sap.ancestor(sources, destinations);
        if (ancestor != 8)
            System.out.printf("The ancestor for nodes 7 and 8 in graph 5 should be 8, but it is: %d\n ", ancestor);
        shortestDistance = sap.length(sources, destinations);
        if (shortestDistance != 1)
            System.out.printf("The distance between nodes 7 and 8 should be 1, but it is: %d\n ", shortestDistance);
        ancestor = sap.ancestor(13, 21);
        if (ancestor != 10)
            System.out.printf("ancestor for 13, and 21 in Graph5 should be 9, but it is: %d\n", ancestor);
        else System.out.printf("Test 9 ancestor for Graph5 passed.\n");
        shortestDistance = sap.length(13, 21);
        if (shortestDistance != 4)
            System.out.printf("shortest distance between 13, and 21 in Graph5 should be 4, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 9 for Graph5 shortest distance passed.\n");
//        sources = new ArrayList<>(Arrays.asList(7));
//        destinations = new ArrayList<>(Arrays.asList(8));
//        for (int i = 0; i < 100; i++) {
//            if (sap.length(13, 13) != 0) throw new AssertionError("Distance between the same nodes isn't zero.");
//            System.out.printf(" length = %d ancestor = %d \n", sap.length(7, 8), sap.ancestor(7, 8));
//            System.out.printf(" Iterables ancestor = %d Iterables length = %d \n", sap.ancestor(sources, destinations), sap.length(sources, destinations));
//        }
        in = new In("digraph3.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        // sources = new ArrayList<>(Arrays.asList(10, 12, 4, 3, 9));
        // destinations = new ArrayList<>(Arrays.asList(5, 3, 2));
        sources = new ArrayList<>(Arrays.asList(0, 2, 3, 7, 10));
        destinations = new ArrayList<>(Arrays.asList(1, 7, 12));
        shortestDistance = sap.length(sources, destinations);
        if (shortestDistance != 0)
            System.out.printf("Expecting the shortest distance value of two iterable sets of Graph 5" +
                    "to be 0, since the number 7 is in both sets. But the actual value we get is: %d\n", shortestDistance);
        else System.out.printf("Test 10 of two sets of the iterables from Graph5 passed the shortest distance test.\n");
        ancestor = sap.ancestor(sources, destinations);
        if (ancestor != 7)
            System.out.printf("Expecting ancestor value of 7 for Graph 5's Iterables set with at least one pair not connected, " +
                    "but the actual value we get = %d\n", ancestor);
        else System.out.printf("Test 10 of two sets of the iterables from Graph5 passed the ancestor test.\n");

    }

    private void testDigraph6() {
        StdOut.println("----------------------------------Running AutoGrader Tests for Digraph 6  ----------------------------------");
        in = new In("digraph6.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        topological = new Topological(digraph);
        System.out.printf("Here are Graph6's nodes in topological order: \n");
        for (int i : topological.order()) {
            System.out.printf(" %d", i);
        }
        System.out.printf("\n");
        shortestDistance = sap.length(0, 5);
        if (shortestDistance != 5)
            System.out.printf("The distance between 0 and 5 should be 5, but it is: %d\n", shortestDistance);
        shortestDistance = sap.length(7, 3);
        if (shortestDistance != 1)
            System.out.printf("The distance between 7 and 3 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(7, 3);
        if (ancestor != 3)
            System.out.printf("The ancestor  between 7 and 3 should be 3, but it is: %d\n", ancestor);
        shortestDistance = sap.length(7, 7);
        if (shortestDistance != 0)
            System.out.printf("The distance between 7, and 7 should be 0, but it is: %d\n", shortestDistance);
        shortestDistance = sap.length(1, 7);
        if (shortestDistance != 3)
            System.out.printf("The distance between 1 and 7 should be 3, but it is: %d\n", shortestDistance);
        shortestDistance = sap.length(4, 4);
        if (shortestDistance != 0)
            System.out.printf("The distance between the same node is should be 0, but it is: %d\n", shortestDistance);
        shortestDistance = sap.length(4, 6);
        if (shortestDistance != 3) System.out.printf("Shortest distance between nodes 4, and 6 is Graph6 should 3.\n");
        else System.out.printf("Test 6 Shortest distance passed.\n");
        ancestor = sap.ancestor(4, 6);
        if (ancestor != 4)
            System.out.printf("Test 6: The value of ancestor in Graph6 between nodes 4, and 6 should be 4, but it is: %d\n", ancestor);
        else System.out.printf("Test 6 of Graph6 passed.\n");
    }

    private void testDigraph9() {
        StdOut.println("----------------------------------Running AutoGrader Tests for Digraph 9 ----------------------------------");
        in = new In("digraph9.txt");
        digraph = new Digraph(in);
        topological = new Topological(digraph);
        System.out.printf("Here are Graph9's nodes in topological order: \n");
        try {
            for (int i : topological.order()) {
                System.out.printf(" %d", i);
            }
        } catch (NullPointerException e) {
            StdOut.println(e.getMessage());
        }
        System.out.printf("\n");
        sap = new SAP(digraph);
        v = new ArrayList<>(Arrays.asList(8, 0));
        w = new ArrayList<>(Arrays.asList(7, 5));
        ancestor = sap.ancestor(v, w);
        if (ancestor != 8)
            System.out.printf("Ancestor for this iterables instance of Graph 9, should be 8, but it is: %d\n", ancestor);
        else System.out.printf("Test 1's ancestor value for this iterables instance in Graph 9 passed.\n");
        shortestDistance = sap.length(v, w);
        if (shortestDistance != 1)
            System.out.printf("The shortest distance between the nodes of this iterables instance should be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 1's minimum distance value for this iterables instance in Graph 9 passed.\n");
        ancestor = sap.ancestor(w, v);
        if (ancestor != 8)
            System.out.printf("Ancestor for this iterables instance of Graph 9, should be 8, but it is: %d\n", ancestor);
        else System.out.printf("Test 2's ancestor value for iterables instance in Graph 9  passed.\n");
        shortestDistance = sap.length(5, 8);
        if (shortestDistance != 1)
            System.out.printf("The distance between 5 and 8 in Graph 9  should be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 2's Shortest distance test of nodes 5, and 8 in Graph 9 passed.\n");
        ancestor = sap.ancestor(5, 8);
        if (ancestor != 8)
            System.out.printf("Ancestor between 5 and 8  in Graph 9 was expected to be 8, but it is: %d\n", ancestor);
        shortestDistance = sap.length(5, 4);
        if (shortestDistance != 1)
            System.out.printf("The distance between 5 and 4  in Graph 9 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(5, 4);
        if (ancestor != 4)
            System.out.printf("Ancestor between 5 and 4  in Graph 9 was expected to be 4, but it is: %d\n", ancestor);
        shortestDistance = sap.length(8, 5);
        if (shortestDistance != 1)
            System.out.printf("The distance between node 8 and 5 in Graph 9 should be 1, but it is: %d\n", shortestDistance);

        shortestDistance = sap.length(4, 1);
        if (shortestDistance != 1)
            System.out.printf("The distance between 4 and 1  in Graph 9 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(4, 1);
        if (ancestor != 1)
            System.out.printf("Ancestor between 4 and 1  in Graph 9 was expected to be 8, but it is: %d\n", ancestor);

        shortestDistance = sap.length(3, 4);
        if (shortestDistance != 1)
            System.out.printf("The distance between 3 and 4  in Graph 9 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(3, 4);
        if (ancestor != 4)
            System.out.printf("Ancestor between 3 and 4  in Graph 9 was expected to be 4, but it is: %d\n", ancestor);
        shortestDistance = sap.length(3, 2);
        if (shortestDistance != 1)
            System.out.printf("The distance between 3 and 2  in Graph 9 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(3, 2);
        if (ancestor != 2)
            System.out.printf("Ancestor between 3 and 2  in Graph 9 was expected to be 2, but it is: %d\n", ancestor);
        shortestDistance = sap.length(3, 0);
        if (shortestDistance != 1)
            System.out.printf("The distance between 3 and 0  in Graph 9 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(3, 0);
        if (ancestor != 0)
            System.out.printf("Ancestor between 3 and 0  in Graph 9 was expected to be 0, but it is: %d\n", ancestor);
        shortestDistance = sap.length(1, 3);
        if (shortestDistance != 1)
            System.out.printf("The distance between 1 and 3  in Graph 9 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(1, 3);
        if (ancestor != 3)
            System.out.printf("Ancestor between 1 and 3 in Graph 9  was expected to be 3, but it is: %d\n", ancestor);
        shortestDistance = sap.length(0, 6);
        if (shortestDistance != 1)
            System.out.printf("The distance between 0, and 6  in Graph 9 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(0, 6);
        if (ancestor != 6)
            System.out.printf("Ancestor between 0 and 6  in Graph 9 was expected to be 6, but it is: %d\n", ancestor);
        shortestDistance = sap.length(6, 3);
        if (shortestDistance != 1)
            System.out.printf("The distance between 6, and 3  in Graph 9 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(6, 3);
        if (ancestor != 3)
            System.out.printf("Ancestor between 6 and 3  in Graph 9 was expected to be 3, but it is: %d\n", ancestor);
        shortestDistance = sap.length(7, 6);
        if (shortestDistance != 1)
            System.out.printf("The distance between 7, and 6  in Graph 9 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(7, 6);
        if (ancestor != 6)
            System.out.printf("Ancestor between 7, and 6  in Graph 9 was expected to be 6, but it is: %d\n", ancestor);
        shortestDistance = sap.length(7, 8);
        if (shortestDistance != -1)
            System.out.printf("The distance between 7 and 8  in Graph 9 should be -1, but it is: %d\n", shortestDistance);
        shortestDistance = sap.length(7, 4);
        if (shortestDistance != 3)
            System.out.printf("The distance between 7, and 4  in Graph 9 should be 3, but it is: %d\n", shortestDistance);
        shortestDistance = sap.length(4, 0);
        if (shortestDistance != 3)
            System.out.printf("The distance between 4, and 0  in Graph 9 should be 3, but it is: %d\n", shortestDistance);
        shortestDistance = sap.length(7, 3);
        if (shortestDistance != 2)
            System.out.printf("The distance between 7, and 3  in Graph 9 should be 2, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(4, 0);
        if (ancestor != 4)
            System.out.printf("The ancestor between 4 and 0  in Graph 9 should be 4, but it is: %d\n", shortestDistance);
        shortestDistance = sap.length(4, 3);
        if (shortestDistance != 1)
            System.out.printf("The distance between 4, and 3  in Graph 9 should be 1, but it is: %d\n", shortestDistance);
        shortestDistance = sap.length(0, 3);
        if (shortestDistance != 1)
            System.out.printf("The distance between 0, and 3  in Graph 9 should be 1, but it is: %d\n", shortestDistance);
        ancestor = sap.ancestor(0, 3);
        if (ancestor != 0)
            System.out.printf("The ancestor between 0, and 3  in Graph 9 should be 0, but it is: %d\n", ancestor);
        shortestDistance = sap.length(0, 5);
        if (shortestDistance != 4)
            System.out.printf("The distance between 0 and 5 should be 4, but it is: %d\n", shortestDistance);

        v = new ArrayList<>(Arrays.asList(8));
        w = new ArrayList<>(Arrays.asList(7));
        shortestDistance = sap.length(v, w);
        if (shortestDistance != -1)
            System.out.printf("The shortest distance between 7 and 8 in Graph 9 is expected to be -1, but the" +
                    "actual value is: %d\n", shortestDistance);
        ancestor = sap.ancestor(v, w);
        if (ancestor != -1) System.out.printf("Ancestor between nodes 7 and 8 in Graph 9 should be -1, but the actual" +
                "value is: %d\n ", ancestor);
        shortestDistance = sap.length(w, v);
        if (shortestDistance != -1)
            System.out.printf("The shortest distance between 8 and 7 in Graph 9 is expected to be -1, but the" +
                    "actual value is: %d\n", shortestDistance);
        ancestor = sap.ancestor(w, v);
        if (ancestor != -1) System.out.printf("Ancestor between nodes 7 and 8 in Graph 9 should be -1, but the actual" +
                "value is: %d\n ", ancestor);

        // 6, 3 minimum distance = 1, ancesotry = 3
        v = new ArrayList<>(Arrays.asList(5));
        w = new ArrayList<>(Arrays.asList(8));
        ancestor = sap.ancestor(v, w);
        if (ancestor != 8)
            System.out.printf("Ancestor of this instance of iterables should be 8, but it is : %d\n", ancestor);
        shortestDistance = sap.length(v, w);
        if (shortestDistance != 1)
            System.out.printf("The distance of this instance of iterables in Graph 5 should be 1, but " +
                    "it is: %d\n", shortestDistance);
        shortestDistance = sap.length(1, 8);
        if (shortestDistance != -1)
            System.out.printf("MindDistance between 1, and 8 should be -1, but it is: %d\n", shortestDistance);

        v = new ArrayList<>(Arrays.asList(5, 1));
        w = new ArrayList<>(Arrays.asList(8, 3));
        shortestDistance = sap.length(v, w);
        ancestor = sap.ancestor(v, w);
    }

    private void createMultipleObjects() {
        StdOut.println("-----------------------------Running AutoGrader Tests for Creating Two Objects -------------------");
        in = new In("digraph1.txt");
        digraph1 = new Digraph(in);
        in = new In("digraph2.txt");
        digraph2 = new Digraph(in);
        sap1 = new SAP(digraph1);
        sap2 = new SAP(digraph2);
        //testDigraph1(sap1);
        //testDigraph2(sap2);
    }

    private void testIterables() {
        StdOut.println("------------------ Running AutoGrader Tests for testIterables with WordNet -------------------");
        in = new In("digraph-wordnet.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        v = new ArrayList<>(List.of());
        w = new ArrayList<>(List.of(3464, 8331, 23405, 23889, 72520));
        shortestDistance = sap.length(v, w);
        if (shortestDistance != -1) System.out.printf("The shortest distance of two Iterables one of which has not " +
                "elements should be -1, but it is: %d\n", shortestDistance);
        v = new ArrayList<>(List.of(9675, 44260, 65806, 80452));
        w = new ArrayList<>(List.of());
        shortestDistance = sap.length(v, w);
        if (shortestDistance != -1) System.out.printf("The shortest distance of two Iterables one of which has not " +
                "elements should be -1, but it is: %d\n", shortestDistance);
        v = new ArrayList<>(List.of());
        w = new ArrayList<>(List.of());
        shortestDistance = sap.length(v, w);
        if (shortestDistance != -1) System.out.printf("The shortest distance of two Iterables one of which has not " +
                "elements should be -1, but it is: %d\n", shortestDistance);
        v = null;
        try {
            shortestDistance = sap.length(v, w);
        } catch (IllegalArgumentException ie) {
            System.out.printf("IllegalArgumentException for length() was thrown successfully when v is null : %s\n", ie.getMessage());
        }
        try {
            ancestor = sap.ancestor(v, w);
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.printf("IllegalArgumentException for ancestor() was thrown successfully when v is null : %s\n", illegalArgumentException.getMessage());
        }
        v = new ArrayList<>(List.of(0, 7, 9, 12));
        w = null;
        try {
            ancestor = sap.ancestor(v, w);
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.printf("IllegalArgumentException for ancestor() was thrown successfully when w is null : %s\n ",
                    illegalArgumentException.getMessage());
        }
        try {
            shortestDistance = sap.length(v, w);
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.printf("IllegalArgumentException for length() was thrown successfully when w is null: %s\n",
                    illegalArgumentException.getMessage());
        }
        w = null;
        v = null;
        try {
            ancestor = sap.ancestor(v, w);
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.printf("IllegalArgumentException for ancestor() was thrown successfully when both v and w are null : %s\n ",
                    illegalArgumentException.getMessage());
        }
        try {
            shortestDistance = sap.length(v, w);
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.printf("IllegalArgumentException for length() was thrown successfully when both v and w are null: %s\n",
                    illegalArgumentException.getMessage());
        }
        v = new ArrayList<>(java.util.Arrays.asList(64451, 35205, 53712, 45826, 2657));
        w = new java.util.ArrayList<>(java.util.Arrays.asList(23325, 21385, 61827, 73382, 55738));
        shortestDistance = sap.length(v, w);
        if (shortestDistance != 10)
            StdOut.println("Expecting the shortest distance for two iterables to be 10, and it is: " + shortestDistance);
        ancestor = sap.ancestor(v, w);
        if (ancestor != 60600)
            StdOut.println("Expecting the ancestor for the iterables to be 60600, and it is: " + ancestor);
        v = new ArrayList<>(Arrays.asList(29512, 32740, 61319));
        w = new ArrayList<>(Arrays.asList(15444, 18922, 22643, 29177, 40477, 43377, 45624, 48461, 71821, 73221, 75631));
        shortestDistance = sap.length(v, w);
        if (shortestDistance != 3)
            StdOut.println("Expecting the shortest distance for two iterables to be 3, but it is: " + shortestDistance);

    }

    private void troubleShooting() {
        StdOut.println("--------------- Running the troubleshooting method to test iterables in Graph 5 -------------");
        in = new In("digraph5.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        Iterable<Integer> sources = new ArrayList<>(Arrays.asList(13, 7, 21, 10, 9, 14, 17));
        Iterable<Integer> destinations = new ArrayList<>(Arrays.asList(21, 12, 0, 12, 20));
        System.out.printf("Test #1 results; Expected Iterables ancestor is 21; actual value = %d Expected Iterables " +
                "minimum distance is 0 actual value = %d\n", sap.ancestor(sources, destinations), sap.length(sources, destinations));
        sources = new ArrayList<>(Arrays.asList(9, 14, 17));
        destinations = new ArrayList<>(Arrays.asList(12, 20, 21));
        ancestor = sap.ancestor(sources, destinations);
        shortestDistance = sap.length(sources, destinations);
        System.out.printf("Test #2 results; Expected Iterables ancestor is 12, or 9; actual value = %d Expected Iterables minimum distance is 2; " +
                "actual value = %d\n", ancestor, shortestDistance);
        ancestor = sap.ancestor(13, 21);
        if (ancestor != 10)
            System.out.printf("Ancestor for nodes 13, and 21 in graph 5 should be 10, but it is: %d\n", ancestor);
        else
            System.out.printf("Tested the single pair version of ancestor, right after the iterables version, and it passed for Graph5 nodes 13 and 21\n");
        shortestDistance = sap.length(13, 21);
        if (shortestDistance != 4)
            System.out.printf("shortest distance for nodes 13, and 21 in graph 5 should be 4, but it is: %d\n", shortestDistance);
        else
            System.out.printf("Test the single pair version of length, right after the iterables version, and it passed for Graph5 nodes 13 and 21\n");
        System.out.printf("Node 10 in the following test has distance of 0 from one endpoint, and 1 from the other. This " +
                "is in an effort to reproduce what the autograder is reporting.\n");
        sources = new ArrayList<>(Arrays.asList(9, 14, 15));
        destinations = new ArrayList<>(Arrays.asList(9, 10, 20, 21, 17, 7));
        System.out.printf("Test #3 results; Expected Iterables ancestor is 9; actual value = %d Expected Iterables minimum distance is 0; " +
                "actual value = %d\n", sap.ancestor(sources, destinations), sap.length(sources, destinations));
        // also try graph 3 with slightly different cycle and graph 25 without a cycle
        in = new In("digraph25.txt");
        System.out.printf("Testing Digraph25\n");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        sources = new ArrayList<>(Arrays.asList(5, 2, 4));
        destinations = new ArrayList<>(Arrays.asList(5, 21, 15, 14, 13, 17));
        System.out.printf("Test #4 - Expecting ancestor of 5, and minimum distance of 0; actual ancestor = %d actual distance is: %d\n",
                sap.ancestor(sources, destinations), sap.length(sources, destinations));
        // Trying graph 3
        System.out.printf("Testing Digraph 3.\n");
        in = new In("digraph3.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        sources = new ArrayList<>(Arrays.asList(8, 14, 11));
        destinations = new ArrayList<>(Arrays.asList(0, 13, 8, 9, 10, 12));
        System.out.printf("Test #5 - Expecting ancestor of 8, and distance of 0 for Graph 3; actual acestor = %d actual distance is: %d\n",
                sap.ancestor(sources, destinations), sap.length(sources, destinations));
//        sources = new ArrayList<>(Arrays.asList(-1,8,14,11));
//        destinations = new ArrayList<>(Arrays.asList(8,13,0,9,10,12));
//        System.out.printf("Passed -1 - %d\n",sap.ancestor(sources,destinations));
    }

    private void testMyGraphs() {
        StdOut.println("--------------- Testing my own Graphs -------------");
        System.out.printf("My Graph1 Tests\n");
        in = new In("myGraph1.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        shortestDistance = sap.length(0, 1);
        if (shortestDistance != 1)
            System.out.printf("The distance between 0 and 1 iwith 0 pointing towards 1 should be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 1 length passed. \n");
        ancestor = sap.ancestor(0, 1);
        if (ancestor != 1)
            System.out.printf("The ancestor between nodes 0 and 1 with 0 pointing towards 1 should be 1, but it is: %d\n", ancestor);
        else System.out.printf("Test 1 ancestor passed. \n");
        shortestDistance = sap.length(1, 0);
        if (shortestDistance != 1)
            System.out.printf("The distance between 1 and 0 with 0 pointing towards 1 should be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 2 length passed. \n");
        ancestor = sap.ancestor(1, 0);
        if (ancestor != 1)
            System.out.printf("The ancestor between 1, 0 with 0 pointing towards 1 should be 1, but it is: %d\n ", ancestor);
        else System.out.printf("Test 2 ancestor passed. \n");
        shortestDistance = sap.length(2, 0);
        if (shortestDistance != -1)
            System.out.printf("The distance between 2 and 0 when 2 is not connected to any other nodes should be -1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 3 length passed.\n");
        ancestor = sap.ancestor(2, 0);
        if (ancestor != -1)
            System.out.printf("The ancestor for 2, and 0 when 2 is not connected to any other nodes should be -1, but it is: %d\n", ancestor);
        else System.out.printf("Test 3 ancestor passed.\n");
        shortestDistance = sap.length(0, 1);
        if (shortestDistance != 1)
            System.out.printf("The distance between 0 and 1 iwith 0 pointing towards 1 should be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 1 length passed. \n");
        ancestor = sap.ancestor(0, 1);
        if (ancestor != 1)
            System.out.printf("The ancestor between nodes 0 and 1 with 0 pointing towards 1 should be 1, but it is: %d\n", ancestor);
        else System.out.printf("Test 1 ancestor passed. \n");
        shortestDistance = sap.length(1, 0);
        if (shortestDistance != 1)
            System.out.printf("The distance between 1 and 0 with 0 pointing towards 1 should be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 2 length passed. \n");
        ancestor = sap.ancestor(1, 0);
        if (ancestor != 1)
            System.out.printf("The ancestor between 1, 0 with 0 pointing towards 1 should be 1, but it is: %d\n ", ancestor);
        else System.out.printf("Test 2 ancestor passed. \n");
        System.out.printf("\n");
        System.out.printf("My Graph2 Tests\n");
        in = new In("myGraph2.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        shortestDistance = sap.length(0, 1);
        if (shortestDistance != 1)
            System.out.printf("The distance between 0 and 1 with 1 pointing towards 0 should be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 4 length passed. \n");
        ancestor = sap.ancestor(0, 1);
        if (ancestor != 0)
            System.out.printf("The ancestor between nodes 0 and 1 with 1 pointing towards 0 should be 0, but it is: %d\n", ancestor);
        else System.out.printf("Test 4 ancestor passed. \n");
        shortestDistance = sap.length(1, 0);
        if (shortestDistance != 1)
            System.out.printf("The distance between 1 and 0 with 1 pointing towards 0 should be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 5 length passed. \n");
        ancestor = sap.ancestor(1, 0);
        if (ancestor != 0)
            System.out.printf("The ancestor between 1, 0 with 1 pointing towards 0 should be 0, but it is: %d\n ", ancestor);
        else System.out.printf("Test 5 ancestor passed. \n");
        shortestDistance = sap.length(0, 2);
        if (shortestDistance != -1)
            System.out.printf("The distance between 0 and 2 both pointing away from ancestor should be -1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 6 length passed.\n");
        ancestor = sap.ancestor(0, 2);
        if (ancestor != -1)
            System.out.printf("The ancestor of nodes 0 and 2 when both are pointing away from a common ancestor should be -1, but it is: %d\n", ancestor);
        else System.out.printf("Test 6 ancestor passed.\n");
        System.out.printf("\n");
        System.out.printf(" myGraph3 tests\n ");
        in = new In("myGraph3.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        ancestor = sap.ancestor(2, 0);
        if (ancestor != 1)
            System.out.printf("The ancestor between nodes 2 and 0 in myGraph3 should be 1, but is: %d\n", ancestor);
        else System.out.printf("Test 7 ancestor passed.\n");
        shortestDistance = sap.length(2, 0);
        if (shortestDistance != 2)
            System.out.printf("shortest distance value between nodes 2 and 0 should be 2, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 7 length passed\n");
        ancestor = sap.ancestor(2, 3);
        if (ancestor != 3)
            System.out.printf("The ancestor between nodes 2 and 3 in myGraph3 should be 3, but it is: %d\n", ancestor);
        else System.out.printf("Test 8 ancestor passed.\n");
        shortestDistance = sap.length(2, 3);
        if (shortestDistance != 2)
            System.out.printf("The shortest distance value between nodes 2 and 3 in myGraph3 should be 2, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 8 shortest distance passed.\n");
        ancestor = sap.ancestor(0, 3);
        if (ancestor != 3)
            System.out.printf("The ancestor between nodes 0 and 3 in myGraph3 should be 3, but it is: %d\n", ancestor);
        else System.out.printf("Test 9 ancestor passed.\n");
        if (shortestDistance != 2)
            System.out.printf("The shortest distance value between the nodes 0, and 3 should be 2, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 9 shortest distance passed.\n");
        ancestor = sap.ancestor(0, 1);
        if (ancestor != 1)
            System.out.printf("The ancestor value between 0, and 1 in myGraph3 should be 1, but it is: %d\n", ancestor);
        else System.out.printf("Test 10 ancestor passed.\n");
        shortestDistance = sap.length(0, 1);
        if (shortestDistance != 1)
            System.out.printf("shortest distance value between nodes 0 and 1 should be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 10 shortest distance passed.\n");
        ancestor = sap.ancestor(2, 1);
        if (ancestor != 1)
            System.out.printf("The ancestor value between 2, and 1 in myGraph3 should be 1, but is: %d\n", ancestor);
        else System.out.printf("Test 11 ancestor passed.\n");
        shortestDistance = sap.length(2, 1);
        if (shortestDistance != 1)
            System.out.printf("shortest distance value between the nodes 2 and 1 should be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 11 shortest distance passed.\n");
        System.out.printf("\n");
        System.out.printf(" myGraph4 tests - testing cycles \n ");
        in = new In("myGraph4.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        ancestor = sap.ancestor(0, 1);
        if (ancestor != 1)
            System.out.printf("Ancestor between 0 and 1 in myGraph4 should be 1, but it is:%d\n", ancestor);
        else System.out.printf("Test 12 ancestor passed.\n");
        shortestDistance = sap.length(0, 1);
        if (shortestDistance != 1)
            System.out.printf("shortest distance between nodes 0 and 1 in myGraph4 should be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 12 shortest distance passed.\n");
        ancestor = sap.ancestor(1, 2);
        if (ancestor != 2)
            System.out.printf("Ancestor between nodes 1 and 2 in myGraph4 should be 2, but it is: %d\n", ancestor);
        else System.out.printf("Test 13 ancestor passed.\n");
        shortestDistance = sap.length(1, 2);
        if (shortestDistance != 1)
            System.out.printf("shortest distance between nodes 1 and 2 in myGraph4 should be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 13 shortest distance passed.\n");
        ancestor = sap.ancestor(2, 0);
        if (ancestor != 0)
            System.out.printf("Ancestor value between 2, and 0 in myGraph4 should be 0, but it is: %d\n", ancestor);
        else System.out.printf("Test 14 ancestor passed.\n");
        shortestDistance = sap.length(2, 0);
        if (shortestDistance != 1)
            System.out.printf("shortest distance between nodes 2 and 0 in myGraph4 should be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 14 shortest distance passed.\n");
        ancestor = sap.ancestor(0, 2);
        if (ancestor != 0)
            System.out.printf("ancestor for nodes 0, 2 in myGraph4 should be 0, but it is: %d\n", ancestor);
        else System.out.printf("Test 15 ancestor passed.\n");
        shortestDistance = sap.length(0, 2);
        if (shortestDistance != 1)
            System.out.printf("shortest distance between nodes 0 and 2 in myGraph4 should be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 15 shortest distance passed.\n");
        ancestor = sap.ancestor(1, 0);
        if (ancestor != 1) System.out.printf("ancestor for 1,0 in myGraph4 should be 1, but it is: %d\n", ancestor);
        else System.out.printf("Test 16 ancestor passed.\n");
        shortestDistance = sap.length(1, 0);
        if (shortestDistance != 1)
            System.out.printf("shortest distance between nodes 1 and 0 in myGraph4 should be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 16 shortest distance passed.\n");
        ancestor = sap.ancestor(2, 1);
        if (ancestor != 2)
            System.out.printf("ancestor between nodes 2 and 1 in myGraph4 should be 2, but it is: %d\n", ancestor);
        else System.out.printf("Test 17 ancestor passed\n");
        shortestDistance = sap.length(2, 1);
        if (shortestDistance != 1)
            System.out.printf("shortest distance between 2, and 1 in myGraph4 should be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 17 shortest distance passed.\n");
        // 0,3
        ancestor = sap.ancestor(0, 3);
        if (ancestor != 3)
            System.out.printf("ancestor for nodes 0 and 3 in myGraph4 should be 3, but it is: %d\n ", ancestor);
        else System.out.printf("Test 18 ancestor passed.\n");
        shortestDistance = sap.length(0, 3);
        if (shortestDistance != 1)
            System.out.printf("shortest distance between nodes 0 and 3 in myGraph4 should be 2, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 18 shortest distance passed.\n");
        ancestor = sap.ancestor(3, 0);
        if (ancestor != 3)
            System.out.printf("ancestor for nodes 3 and 0 in myGraph4 should be 3, but it is: %d\n ", ancestor);
        else System.out.printf("Test 19 ancestor passed.\n");
        shortestDistance = sap.length(3, 0);
        if (shortestDistance != 1)
            System.out.printf("shortest distance between 3 and 0 in myGraph4 should be 2, but it is: %d\n  ", shortestDistance);
        else System.out.printf("Test 19 shortest distance passed.\n");
        ancestor = sap.ancestor(2, 4);
        if (ancestor != 4) System.out.printf("ancestor for 2,4 in myGraph4 should be 4, but it is: %d\n ", ancestor);
        else System.out.printf("Test 20 ancestor passed.\n");
        shortestDistance = sap.length(2, 4);
        if (shortestDistance != 1)
            System.out.printf("shortest distance between 2, and 4 should be 1 but it is: %d\n", shortestDistance);
        else System.out.printf("Test 20 shortest distance passed.\n");
        ancestor = sap.ancestor(4, 2);
        if (ancestor != 4)
            System.out.printf("ancestor between 4,and 2 in myGraph4 should be 4, but it is: %d\n ", ancestor);
        else System.out.printf("Test 21 ancestor passed.\n");
        shortestDistance = sap.length(4, 2);
        if (shortestDistance != 1)
            System.out.printf("shortest distance between 4, and 2 in myGraph4 should be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 21 shortest distance passed.\n");

        System.out.printf(" ***********starting myGraph5 tests - testing cycles************************************** \n ");
        in = new In("myGraph5.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        v = new ArrayList<>(Arrays.asList(0));
        w = new ArrayList<>(Arrays.asList(2, 1));
        ancestor = sap.ancestor(v, w);
        if (ancestor != 2)
            System.out.printf("ancestor value of this iterables sets in myGraph5 should be 1, but the actual value is: %d\n", ancestor);
        else System.out.printf("Test 4's ancestor result for iterables sets in myGraph 5 passed.\n");
        shortestDistance = sap.length(v, w);
        if (shortestDistance != 1)
            System.out.printf("Minimum distance of this iterables set for myGraph5 should be 1, but the actual value" +
                    "is: %d\n", shortestDistance);
        else System.out.printf("Test 4's shortest distance result for iterables in myGraph5 passed.\n");
        ancestor = sap.ancestor(0, 1);
        if (ancestor != 1)
            System.out.printf("ancestor between 0,and 1 in myGraph5 should be 1, but it is: %d\n ", ancestor);
        else System.out.printf("Test 1's ancestor result in myGraph 5 ancestor passed.\n");
        shortestDistance = sap.length(0, 1);
        if (shortestDistance != 1)
            System.out.printf("minimum distance between 0,and 1 in myGraph5 should be 1, but it is: %d\n ", shortestDistance);
        else System.out.printf("Test 1's minimum distance result in myGraph 5 passed.\n");
        ancestor = sap.ancestor(1, 2);
        if (ancestor != 2)
            System.out.printf("ancestor between 1,and 2 in myGraph5 should be 2, but it is: %d\n ", ancestor);
        else System.out.printf("Test 2's ancestor result in myGraph 5 ancestor passed.\n");
        shortestDistance = sap.length(1, 2);
        if (shortestDistance != 1)
            System.out.printf("minimum distance between 1,and 2 in myGraph5 should be 1, but it is: %d\n ", ancestor);
        else System.out.printf("Test 2's minimum distance result in myGraph 5 passed.\n");
        ancestor = sap.ancestor(0, 2);
        if (ancestor != 2)
            System.out.printf("ancestor between 0,and 2 in myGraph5 should be 2, but it is: %d\n ", ancestor);
        else System.out.printf("Test 3's ancestor result in myGraph 5 ancestor passed.\n");
        shortestDistance = sap.length(0, 2);
        if (shortestDistance != 1)
            System.out.printf("minimum distance between 0,and 2 in myGraph5 should be 1, but it is: %d\n ", ancestor);
        else System.out.printf("Test 3's minimum distance result in myGraph 5 passed.\n");
        v = new ArrayList<>(Arrays.asList(0));
        w = new ArrayList<>(Arrays.asList(1, 2));
        ancestor = sap.ancestor(v, w);
        if (ancestor != 1)
            System.out.printf("ancestor value of this iterables sets in myGraph5 should be 1, but the actual value is: %d\n", ancestor);
        else System.out.printf("Test 4's ancestor result for iterables sets in myGraph 5 passed.\n");
        shortestDistance = sap.length(v, w);
        if (shortestDistance != 1)
            System.out.printf("Minimum distance of this iterables set for myGraph5 should be 1, but the actual value" +
                    "is: %d\n", shortestDistance);
        else System.out.printf("Test 4's shortest distance result for iterables in myGraph5 passed.\n");
        System.out.printf("-----------------------------------------myGraph6 Tests------------------------\n");
        in = new In("myGraph6.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        shortestDistance = sap.length(0, 1);
        if (shortestDistance != 1) System.out.printf("The shortest distance value in myGraph6 between 0 and 1 should " +
                "be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 1 of myGraph6 passed.\n");
        ancestor = sap.ancestor(0, 1);
        if (ancestor != 1) System.out.printf("The ancestor value in myGrph6 should be 1, but it is: %d\n", ancestor);
        else System.out.printf("Test 1 of myGraph6 passed.\n");
        shortestDistance = sap.length(1, 0);
        if (shortestDistance != 1) System.out.printf("The shortest distance value in myGraph6 between 1 and 0 should " +
                "be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 2 of myGraph6 passed.\n");
        ancestor = sap.ancestor(1, 0);
        if (ancestor != 0) System.out.printf("The ancestor value in myGrph6 should be 0, but it is: %d\n", ancestor);
        else System.out.printf("Test 2 of myGraph6 passed.\n");
        System.out.printf("-----------------------------------------myGraph7 Tests------------------------------------\n");
        in = new In("myGraph7.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        shortestDistance = sap.length(0, 1);
        if (shortestDistance != 1) System.out.printf("The shortest distance value in myGraph7 between 0 and 1 should " +
                "be 2, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 1 of myGraph7 passed.\n");
        ancestor = sap.ancestor(0, 1);
        if (ancestor != 0)
            System.out.printf("The ancestor value in myGraph7 Test 1 should be 1, but it is: %d\n", ancestor);
        else System.out.printf("Test 1 of myGraph7 passed.\n");

        shortestDistance = sap.length(1, 0);
        if (shortestDistance != 1)
            System.out.printf("The shortest distance value in myGraph7 Test 2 between 1 and 0 should " +
                    "be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 2 of myGraph7 passed.\n");
        ancestor = sap.ancestor(1, 0);
        if (ancestor != 0)
            System.out.printf("The ancestor value in myGraph7 Test 2 should be 0, but it is: %d\n", ancestor);
        else System.out.printf("Test 2 of myGraph7 passed.\n");

        System.out.printf("-----------------------------------------myGraph8 Tests------------------------------------\n");
        in = new In("myGraph8.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        shortestDistance = sap.length(0, 2);
        if (shortestDistance != 1)
            System.out.printf("The shortest distance value in myGraph8 Test 1 between 0 and 2 should " +
                    "be 2, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 1 of myGraph8 passed.\n");
        ancestor = sap.ancestor(0, 2);
        if (ancestor != 2)
            System.out.printf("The ancestor value in myGraph8 Test 1 should be 2, but it is: %d\n", ancestor);
        else System.out.printf("The ancestor Test 1 of myGraph8 passed.\n");
        shortestDistance = sap.length(2, 0);
        if (shortestDistance != 1)
            System.out.printf("The shortest distance value in myGraph8 Test 2 between 2 and 0 should " +
                    "be 2, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 2 of myGraph8 passed.\n");
        ancestor = sap.ancestor(2, 0);
        if (ancestor != 2)
            System.out.printf("The ancestor value in myGraph8 Test 2 should be 2, but it is: %d\n", ancestor);
        else System.out.printf("The ancestor value for Test 2 of myGraph8 passed.\n");
        System.out.printf("-----------------------------------------myGraph9 Tests------------------------------------\n");
        in = new In("myGraph9.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        v = new ArrayList<>(Arrays.asList(0, 1));
        w = new ArrayList<>(Arrays.asList(2));
        shortestDistance = sap.length(v, w);
        if (shortestDistance != 1)
            System.out.printf("The shortest distance value in myGraph9 Test 1 should be 1, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 1 of myGraph9 passed.\n");
        ancestor = sap.ancestor(v, w);
        if (ancestor != 1)
            System.out.printf("The ancestor value between two iterable sets in Test 1 of myGraph9 should be 1, but it is: %d\n ", ancestor);
        else System.out.printf("Test 1 ancestor value of myGraph9 passed.\n");
        ancestor = sap.ancestor(2, 1);
        if (ancestor != 1)
            System.out.printf("The ancestor value in myGraph8 Test 1 should be 1, but it is: %d\n", ancestor);
        else System.out.printf("Test 2 ancestor value in myGraph9 passed.\n");
        shortestDistance = sap.length(2, 1);
        if (shortestDistance != 2)
            System.out.printf("The shortest distance between the nodes 2, and 1 should be 2, but it is: %d\n", shortestDistance);
        else System.out.printf("Test 2 the minimum distance in myGraph9 passed.\n");

    }

    private void repeatedTests() {
        in = new In("digraph-wordnet.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        System.out.printf(" %d\n", sap.ancestor(12, 1));
        System.out.printf(" %d\n", sap.length(12, 1));
        List<Integer> sources = new ArrayList<>(Arrays.asList(12));
        List<Integer> destinations = new ArrayList<>(Arrays.asList(1));
        System.out.printf(" %d\n", sap.ancestor(sources, destinations));
        System.out.printf(" %d\n", sap.length(sources, destinations));
        for (int i = 0; i < 5000; i++) {
            if ((sap.ancestor(sources, destinations) != 34093) || (sap.length(sources, destinations) != 2)) {
                System.out.printf(" Got a different value");
                break;
            }
            sap.length(12, 1);
            sap.ancestor(12, 1);
        }
    }

    private class State {
        public int getSource() {
            return source;
        }

        public int getDestination() {
            return destination;
        }

        public int getMinDis() {
            return minDis;
        }

        public int getAncestor() {
            return ancestor;
        }

        int source;
        int destination;
        int minDis;
        int ancestor;

        public State(int source, int destination, int minDis, int ancestor) {
            this.source = source;
            this.destination = destination;
            this.minDis = minDis;
            this.ancestor = ancestor;
        }
    }

    private void updateIterativeLists(int n) {
        // System.out.printf("Updated iterative lists\n");
        int source;
        int destination;
        v = new ArrayList<>();
        w = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            source = StdRandom.uniform(0, n);
            destination = StdRandom.uniform(0, n);
            v.add(source);
            w.add(destination);
        }
    }

    /* private void iterativeTests() throws AssertionError {
        try {
            File myObj = new File("filename.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.out.printf("Starting iterative tests \n");
        in = new In("digraph-wordnet.txt");
        Digraph digraph = new Digraph(in);
        SAP sap = new SAP(digraph);
        HashMap<Integer, State> map = new HashMap<>();
        State state;
        int source;
        int destination;
        int n = digraph.V();
        System.out.printf("size of digraph is: %d\n", n);
        for (int i = 0; i < 82190; i++) {
            destination = StdRandom.uniform(0, n);
            state = new State(i, destination, sap.length(i, destination), sap.ancestor(i, destination));
            map.put(i, state);
        }
        System.out.printf("Created the hash map\n");
        State currentState;
        int ancestor;
        int minDistance;
        for (int i : map.keySet()) {
            currentState = map.get(i);
            ancestor = sap.ancestor(i, currentState.destination);
            minDistance = sap.length(i, currentState.destination);
            if (currentState.ancestor != ancestor || currentState.minDis != minDistance)
                throw new AssertionError("Iterative Tests Test 1 - should be source=" + currentState.source + " destination = " + currentState.destination +
                        "ancestor = " + currentState.ancestor + "minDistance =" + currentState.minDis +
                        " but actual ancestor= " + ancestor + "actual minimum distance=" + minDistance);
        }
        System.out.printf("done with test 1.\n");
        updateIterativeLists(n);
        System.out.printf("Created sources and destinations for iterables.\n");
        int counter = 0;
        while (counter < 100000) {
            int[] indeces = new int[10];
            for (int j = 0; j < 10; j++) {
                int i = StdRandom.uniform(0, map.size());
                indeces[j] = i;
                currentState = map.get(i);
                // calling the iterative version of ancestor
                ancestor = sap.ancestor(v, w);
                // calling the integer version of ancestor
                ancestor = sap.ancestor(i, currentState.destination);
                // calling the iterative version of length()
                shortestDistance = sap.length(v, w);
                // calling the integer version of length()
                shortestDistance = sap.length(i, currentState.destination);
                if (currentState.ancestor != ancestor && currentState.minDis != shortestDistance) {
                    try {
                        FileWriter myWriter = new FileWriter("filename.txt");
                        System.out.printf("Found a discrepancy and going to try to write the data to the file.\n");
                        myWriter.write("The following are the source/destination pairs that were called before the error occurred: ");
                        for (int k = 0; k < j; k++) {
                            myWriter.write("from: " + currentState.source + " to: " + currentState.destination + " expected ancestor: " + currentState.ancestor + " expected minimum distance: " + currentState.minDis);
                        }
                        myWriter.write("The line below is the pair that was miscalculated: ");
                        myWriter.write("from: " + i + " to: " + currentState.destination + " expected ancestor: " + currentState.ancestor +
                                " expected mindistance" + currentState.minDis + " actual ancestor: " + ancestor + " actual minimum distance: " + shortestDistance);
                        myWriter.write("Here is the list of items in the iterables lists when the error occurred");
                        for (int m : v) {
                            for (int s : w) {
                                myWriter.write("from: " + m + " to:" + s);
                            }
                        }
                        myWriter.close();
                        System.out.println("Successfully wrote to the file.");
                    } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }
                }
                updateIterativeLists(n);
            }
            counter++;
        }

        throw new AssertionError("Iterative Tests Test 1 - should be source=" + currentState.source + " destination = " + currentState.destination +
                "ancestor = " + currentState.ancestor + "minDistance =" + currentState.minDis +
                " but actual ancestor= " + ancestor + "actual minimum distance=" + minDistance);
        sap.ancestor(sources, destinations);
        sap.ancestor(sources, destinations);
    }
*/

    private void hypernyms100subgraphTest() {
        System.out.printf("---------------------hypernyms100subgraphTest() Tests-------------------------------\n");
        in = new In("hypernyms100-subgraph.txt");
        Digraph digraphDFCopy = new Digraph(100);
        int index = 0;
        while (in.hasNextLine()) {
            index++;
            String[] a = in.readLine().split(",");
            for (int i = 0; i < a.length - 1; i++) {
                digraphDFCopy.addEdge(Integer.parseInt(a[0]), Integer.parseInt(a[i + 1]));
            }
        }// check for cycles
        DirectedCycle cycleFinder = new DirectedCycle(digraphDFCopy);
        if (cycleFinder.hasCycle()) {
            throw new IllegalArgumentException("The input to the constructor does not correspond to a rooted DAG - cycle detected");
        }
        if (Math.abs(index - 100) > 1) {
            throw new IllegalArgumentException("The input to the constructor does not correspond to a rooted DAG - Graph Not rooted");
        }
        sap = new SAP(digraphDFCopy);
        shortestDistance = sap.length(53, 23);
        if (shortestDistance != 1)
            System.out.printf("The shortest distance between nodes 53, and 23 in hypernymys100-subgraph should be 1, but we get: %d\n", shortestDistance);
        else System.out.printf("Test 1 for shortest distance in hypernyms 100 passed.\n");

        System.out.printf("Trying length() in reverse.\n");
        ancestor = sap.ancestor(23, 53);
        if (ancestor != 53)
            System.out.printf("Ancestor of nodes 53 and 23 in hypernyms100subgraph should be 53, (even in reverse) but it is: %d\n", ancestor);
        else System.out.printf("Test 1 for ancestor (with nodes reversed) in hypernyms 100 passed.\n");

        ancestor = sap.ancestor(53, 23);
        if (ancestor != 53)
            System.out.printf("Ancestor of nodes 53 and 23 in hypernyms100subgraph should be 53, but it is: %d\n", ancestor);
        else System.out.printf("Test 1 for ancestor in hypernyms 100 passed.\n");

        shortestDistance = sap.length(90, 0);
        if (shortestDistance != 3)
            System.out.printf("The shortest distance between nodes 0, and 90 should be 3, but we get: %d\n", shortestDistance);
        else System.out.printf("Test 2 for shortest distance in hypernyms 100 passed.\n");
        ancestor = sap.ancestor(90, 0);
        if (ancestor != 26)
            System.out.printf("Ancestor of nodes 90 and 0 in hypernyms100subgraph should be 26, but it is: %d\n", ancestor);
        else System.out.printf("Test 2 for ancestor in hypernyms 100 passed.\n");

        shortestDistance = sap.length(53, 76);
        if (shortestDistance != 2)
            System.out.printf("The shortest distance between nodes 53, and 76 in hypernymys100-subgraph should be 2, but we get: %d\n", shortestDistance);
        else System.out.printf("Test 3 for shortest distance in hypernyms 100 passed.\n");
        ancestor = sap.ancestor(53, 76);
        if (ancestor != 60)
            System.out.printf("Ancestor of nodes 53 and 76 in hypernyms100subgraph should be 60 (or 76), but it is: %d\n", ancestor);
        else System.out.printf("Test 3 for ancestor in hypernyms 100 passed.\n");

        shortestDistance = sap.length(19, 91);
        if (shortestDistance != 7)
            System.out.printf("The shortest distance between nodes 19, and 91 in hypernymys100-subgraph should be 7, but we get: %d\n", shortestDistance);
        else System.out.printf("Test 4 for shortest distance in hypernyms 100 passed.\n");
        ancestor = sap.ancestor(19, 91);
        if (ancestor != 60)
            System.out.printf("Ancestor of nodes 19 and 91 in hypernyms100subgraph should be 60 , but it is: %d\n", ancestor);
        else System.out.printf("Test 4 for ancestor in hypernyms 100 passed.\n");
        // testing 16, and 19
        shortestDistance = sap.length(19, 16);
        if (shortestDistance != 6)
            System.out.printf("The shortest distance between nodes 19, and 16 in hypernymys100-subgraph should be 6, but we get: %d\n", shortestDistance);
        else System.out.printf("Test 5 for shortest distance in hypernyms 100 passed.\n");
        ancestor = sap.ancestor(19, 16);
        if (ancestor != 60)
            System.out.printf("Ancestor of nodes 19 and 16 in hypernyms100subgraph should be 60 , but it is: %d\n", ancestor);
        else System.out.printf("Test 5 for ancestor in hypernyms 100 passed.\n");

        // testing 16, and 19
        shortestDistance = sap.length(19, 60);
        if (shortestDistance != 5)
            System.out.printf("The shortest distance between nodes 19, and 60 in hypernymys100-subgraph should be 5, but we get: %d\n", shortestDistance);
        else System.out.printf("Test 6 for shortest distance in hypernyms 100 passed.\n");
        ancestor = sap.ancestor(19, 60);
        if (ancestor != 60)
            System.out.printf("Ancestor of nodes 19 and 60 in hypernyms100subgraph should be 60 , but it is: %d\n", ancestor);
        else System.out.printf("Test 6 for ancestor in hypernyms 100 passed.\n");

        // testing 19, and 76
        shortestDistance = sap.length(19, 76);
        if (shortestDistance != 4)
            System.out.printf("The shortest distance between nodes 19, and 76 in hypernymys100-subgraph should be 4, but we get: %d\n", shortestDistance);
        else System.out.printf("Test 7 for shortest distance in hypernyms 100 passed.\n");
        ancestor = sap.ancestor(19, 76);
        if (ancestor != 76)
            System.out.printf("Ancestor of nodes 19 and 76 in hypernyms100subgraph should be 76 , but it is: %d\n", ancestor);
        else System.out.printf("Test 7 for ancestor in hypernyms 100 passed.\n");
    }

    private void singleWordNetTests() {
        // Exception in thread "main" java.lang.AssertionError: Iterative Tests Test 1 - should be source=1285
        // destination = 58083ancestor = 57333minDistance =12 but actual ancestor= 42539actual minimum distance=4
        System.out.printf("---------------------singleWordNetTests() Tests-------------------------------\n");
        System.out.printf("************Starting iterative tests in singleWordNetTests() method********** \n");
        in = new In("digraph-wordnet.txt");
        Digraph digraph = new Digraph(in);
        SAP sap = new SAP(digraph);

        shortestDistance = sap.length(1285, 5803);
        if (shortestDistance != 12) System.out.printf("Expecting 12, but getting: %d\n", shortestDistance);
        else System.out.printf("singleWordNetTests Test #1 for shortest distance passed.\n");
        ancestor = sap.ancestor(1285, 5803);
        if (ancestor != 57333) System.out.printf("Expecting 57333, but getting: %d\n", ancestor);
        else System.out.printf("singleWordNetTests Test #1 for ancestor passed.\n");
    }

    private void tinyDGTests() {
        System.out.printf("-----------------------------------------TinyDG Tests------------------------------------\n");
        in = new In("tinyDG.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        shortestDistance = sap.length(5, 4);
        if (shortestDistance != 1)
            System.out.printf("Test 1 of tinyDG Graph minimum distance between 5, and 4 should be 1, but" +
                    "the actual value is: %d\n", shortestDistance);
        else System.out.printf("Test 1 of tinyDG Graph for minimum distance  passed.\n");
        ancestor = sap.ancestor(5, 4);
        if (ancestor != 4)
            System.out.printf("Test 1 of tinyDG Graph ancestor between 5, and 4 should be 4, but it is: %d\n", ancestor);
        else System.out.printf("Test 1 of tinyDG Graph for ancestor passed.\n");

        shortestDistance = sap.length(4, 5);
        if (shortestDistance != 2)
            System.out.printf("Test 2 of tinyDG Graph minimum distance between 4, and 5 should be 2, " +
                    "but the actual value is: %d\n", shortestDistance);
        else System.out.printf("Test 2 of tinyDG Graph minimum distance passed.\n");
        ancestor = sap.ancestor(4, 5);
        if (ancestor != 5)
            System.out.printf("Test 2 of tinyDG Graph ancestor between 4, and 5 should be 5, but it is: %d\n", ancestor);
        else System.out.printf("Test 2 of tinyDG Graph for ancestor passed.");
        v = new ArrayList<>(Arrays.asList(1, 7, 10));
        w = new ArrayList<>(Arrays.asList(4));
        shortestDistance = sap.length(v, w);
        if (shortestDistance != 2)
            System.out.printf("Test 3 shortest distance value in tinyDG Graph from a set of values to node 4 should be 2, but it is: %d\n ", shortestDistance);
        else System.out.printf("Test 3 shortest distance value passed.\n");
        ancestor = sap.ancestor(v, w);
        if (ancestor != 4)
            System.out.printf("Test 3 ancestor value in tinyDG Graph from a set of values to node 4 should be 4, but it is: %d\n", ancestor);
        else System.out.printf("Test 3 ancestor value passed.\n");
    }

    public static void main(String[] args) {
        AutoGraderTests autoGraderTests = new AutoGraderTests();
        autoGraderTests.testDigraphWordNet();
        autoGraderTests.singleWordNetTests();
        autoGraderTests.testDigraph1();
        autoGraderTests.testDigraph2();
        autoGraderTests.testDigraph3();
        autoGraderTests.testDigraph4();
        autoGraderTests.testDigraph5();
        autoGraderTests.testDigraph6();
        autoGraderTests.testDigraph9();
        autoGraderTests.testMyGraphs();
        autoGraderTests.createMultipleObjects();
        autoGraderTests.testIterables();
        autoGraderTests.testRandomDigraph();
        autoGraderTests.troubleShooting();
        autoGraderTests.repeatedTests();
        autoGraderTests.hypernyms100subgraphTest();
        autoGraderTests.tinyDGTests();
    }
}
