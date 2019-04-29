
package edu.assignment.wordnet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author vahbuna
 */
public class WordNetTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullSynset() {
        WordNet wordnet = new WordNet(null,
                "data/wordnet/hypernyms100-subgraph.txt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHypernym() {
        WordNet wordnet = new WordNet("data/wordnet/synsets100-subgraph.txt",
        null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRoot() {
        WordNet wordnet = new WordNet("data/wordnet/synsets3.txt",
        "data/wordnet/hypernyms3InvalidTwoRoots.txt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCycle() {
        WordNet wordnet = new WordNet("data/wordnet/synsets6.txt",
        "data/wordnet/hypernyms6InvalidCycle.txt");
    }

    /**
     * read a synset and a hypernet file.
     */
    @Test
    public void allOk() {
        WordNet wordnet = new WordNet("data/wordnet/synsets100-subgraph.txt",
                "data/wordnet/hypernyms100-subgraph.txt");
    }

    /**
     * if string is a valid noun.
     */
    @Test
    public void isNounTest() {
        WordNet wordnet = new WordNet("data/wordnet/synsets100-subgraph.txt",
                "data/wordnet/hypernyms100-subgraph.txt");

        assertTrue(wordnet.isNoun("CRP"));
        assertFalse(wordnet.isNoun("CRP1"));
    }
}
