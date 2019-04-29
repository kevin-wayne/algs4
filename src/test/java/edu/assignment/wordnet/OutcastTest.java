
package edu.assignment.wordnet;

import static org.junit.Assert.assertEquals;
import edu.princeton.cs.algs4.In;
import org.junit.Test;


/**
 *
 * @author vahbuna
 */
public class OutcastTest {

    @Test
    public void testOutcast() {
        WordNet wordnet = new WordNet("data/wordnet/synsets.txt",
        "data/wordnet/hypernyms.txt");

        Outcast outcast = new Outcast(wordnet);
        In in = new In("data/wordnet/outcast11.txt");
        String[] nouns = in.readAllStrings();
        assertEquals("potato", outcast.outcast(nouns));
    }

}
