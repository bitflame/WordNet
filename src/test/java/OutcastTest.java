import edu.princeton.cs.algs4.In;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OutcastTest {
    WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");
    Outcast outcast = new Outcast(wordNet);
    In in = new In("outcast5.txt");
    String[] nouns5 = in.readAllStrings();


    @Test
    void outcast() {
        Assertions.assertEquals("table",outcast.outcast(nouns5));
    }
}