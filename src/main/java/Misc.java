import edu.princeton.cs.algs4.RedBlackBST;

public class Misc {
    RedBlackBST<Integer, String> st;
    int[] ids;
    String[] data;

    public Misc(int[] ids, String[] data) {
        st = new RedBlackBST<>();
        for (int i = 0; i < data.length; i++) {
            st.put(ids[i], data[i]);
        }
    }

    public void pritnAllDb() {
        for (int i : st.keys()) {
            for (String s : st.get(i).split(" ")) {
                if (s.equals("worm")) System.out.println("found  worm with id: " + i);
            }
        }
    }

    public static void main(String[] args) {
        String[] data = {"worm", "worm", "worm", "worm louse insect dirt_ball"};
        int[] ids = {81679, 81680, 81681, 81682};
        Misc misc = new Misc(ids, data);
        misc.pritnAllDb();
    }
}
