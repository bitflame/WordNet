import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordNetTest {
WordNet wordNet = new WordNet("synsets15.txt","hypernyms15Path.txt");
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
        assertEquals("a",wordNet.sap("a","a"));
    }
}