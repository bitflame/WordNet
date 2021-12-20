import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordNetTest {
    WordNet wordNet = new WordNet("synsets15.txt", "hypernyms15Path.txt");
    WordNet wordNet1 = new WordNet("synsets.txt", "hypernyms.txt");
    WordNet wordNet3 = new WordNet("synsets15.txt", "hypernyms15Tree.txt");

    @Test
    void nouns() {
    }

    @Test
    void isNoun() {
        assertEquals(true, wordNet1.isNoun("entity"));
    }

    @Test
    void distance() {
        assertEquals(11, wordNet1.distance("quadrangle", "mountain_devil"));
        assertEquals(5, wordNet1.distance("worm", "bird"));
    }

    @Test
    void sap() {
        assertEquals("a", wordNet.sap("a", "a"));
        assertEquals("b", wordNet.sap("a", "b"));
        assertEquals("", wordNet.sap("a", "o"));
        assertEquals("animal animate_being beast brute creature fauna", wordNet1.sap("worm", "bird"));
        assertEquals("whole unit", wordNet1.sap("quadrangle", "mountain_devil"));
    }

    @Test
    void sap3() {
        assertEquals("k", wordNet3.sap("k", "o"));
        assertEquals("b", wordNet3.sap("d", "e"));
        assertEquals("f", wordNet3.sap("j", "k"));
        assertEquals("b", wordNet3.sap("h", "j"));
    }
}