import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordNetTest {
    WordNet wordNet = new WordNet("synsets15.txt", "hypernyms15Path.txt");
    WordNet wordNet2 = new WordNet("synsets.txt", "hypernyms.txt");
    WordNet wordNet3 = new WordNet("synsets15.txt", "hypernyms15Tree.txt");

    @Test
    void nouns() {
    }

    @Test
    void isNoun() {
    }

    @Test
    void distance() {
    }

    @Test
    void sap() {
        assertEquals("a", wordNet.sap("a", "a"));
        assertEquals("b", wordNet.sap("a", "b"));
        assertEquals("o", wordNet.sap("a", "o"));
    }

    @Disabled
    @Test
    void sap2() {
        assertEquals("animal animate_being beast brute creature fauna", wordNet2.sap("worm", "bird"));
    }

    @Test
    void sap3() {
        assertEquals("k",wordNet3.sap("k","o"));
        assertEquals("b",wordNet3.sap("d","e"));
        assertEquals("f",wordNet3.sap("j","k"));
        assertEquals("b",wordNet3.sap("h","j"));
    }
}