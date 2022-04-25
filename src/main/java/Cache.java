import edu.princeton.cs.algs4.StdRandom;

public class Cache {
    private class Node {
        int source;
        int destination;
        int minimumDistance;
        int ancestor;
        Node next;
        int hash = -1;

        public int getHash() {

            return hash;
        }

        public Node(int src, int dest, int minDist, int ances) {
            source = src;
            destination = dest;
            minimumDistance = minDist;
            ancestor = ances;
        }

        public Node(int src, int dest) {
            source = src;
            destination = dest;
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

    int M = 97; // table size
    private Node[] table;
    Node first;

    public Cache() {
        table = new Node[M];
        for (int i = 0; i < M; i++) {

            table[i] = first;
        }
    }

    public Node get(Integer source, Integer destination) {
        Node node = new Node(source, destination);
        if (table[node.hashCode()] == null) return null;
        else {
            for (Node x = table[node.hash]; x != null; x = x.next) {
                if (x.source == source && x.destination == destination) return x;
            }
        }
        return null;
    }

    public void put(Node node) {
        if (table[node.hashCode()] == null) {
            Node first = node;
            table[node.hash] = first;
        } else {
            node.next = table[node.hash];
            table[node.hash] = node;
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
//        for (int i = 0; i < 10; i++) {
//            node = new Cache().new Node(13775, 76841);
//            System.out.printf("%d\n", node.hashCode());
//        }
        // test the cache by adding 10 items and retrieving the right ones
        node = new Cache().new Node(41269, 66612, 4, 45);
        cache.put(node);
        node = new Cache().new Node(58965, 31069);
        cache.put(node);
        node = new Cache().new Node(77176, 52050);
        cache.put(node);

        Node n = cache.get(41269, 66612);

        System.out.printf("from cache: source: %d, destination: %d, minDist: %d, ancestor: %d\n ", n.source, n.destination, n.minimumDistance, n.ancestor);
        try {
            System.out.printf("%d %d %d %d\n", cache.get(22222, 54168).source, cache.get(22222, 54168).destination, cache.get(22222, 54168).minimumDistance,
                    cache.get(22222, 54168).ancestor);
        } catch (NullPointerException e) {
            System.out.printf("cache does not have this value. \n", e.getMessage());
        }


        try {
            Node temp = cache.get(41269, 66612);
            System.out.printf("Here is source: %d, destination: %d, minimum distance: %d, and ancestor: %d\n", temp.source, temp.destination, temp.minimumDistance, temp.ancestor);
        } catch (NullPointerException e) {
            System.out.printf(e.getMessage(), "\n");
        }

    }
}
