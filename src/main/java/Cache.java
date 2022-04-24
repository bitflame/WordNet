import edu.princeton.cs.algs4.StdRandom;

import java.util.Objects;

public class Cache {
    private class Node {
        int source;
        int destination;
        int minimumDistance;
        int ancestor;


        int hash = -1;

        public int getHash() {
            return hash;
        }

        public Node(int src, int dest) {
            source = src;
            destination = dest;
        }

        public Node(int src, int dest, int minDist, int anc) {
            source = src;
            destination = dest;
            minimumDistance = minDist;
            ancestor = anc;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public int getDestination() {
            return destination;
        }

        public void setDestination(int destination) {
            this.destination = destination;
        }

        public int getMinimumDistance() {
            return minimumDistance;
        }

        public void setMinimumDistance(int minimumDistance) {
            this.minimumDistance = minimumDistance;
        }

        public int getAncestor() {
            return ancestor;
        }

        public void setAncestor(int ancestor) {
            this.ancestor = ancestor;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return source == node.source && destination == node.destination;
        }

        @Override
        public int hashCode() {
            // M = 97
            int hash = 17;
            hash = (31 * hash + ((Integer) source).hashCode() & 0x7fffffff) % 97;
            hash = (31 * hash + ((Integer) destination).hashCode() & 0x7fffffff) % 97;
            this.hash = hash;
            return hash;
        }
    }

    public static void main(String[] args) {
        Cache cache = new Cache();
        Node node;
        for (int i = 0; i < 100; i++) {
            int source = StdRandom.uniform(0, 82192);
            int destination = StdRandom.uniform(0, 82192);
            node = new Cache().new Node(source, destination);
            System.out.printf("hash code for: (%d, %d) is: %d\n", source, destination, node.hashCode());
        }
        // hashing the same nodes should give the same hash value
        for (int i = 0; i < 10; i++) {
            node = new Cache().new Node(13775, 76841);
            System.out.printf("%d\n", node.hashCode());
        }

    }
}
