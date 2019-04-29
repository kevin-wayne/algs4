/******************************************************************************
 *  Compilation:  javac Outcast.java
 *  Execution:    java edu.assignment.wordnet.Outcast
 *  Data files:   src/main/resources/data/wordnet
 *
 ******************************************************************************/
package edu.assignment.wordnet;

/**
 * Identify the outcast from a list of WordNet nouns.
 * <p>
 * For additional documentation,
 * see <a href="http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html">
 * Programming Assignment 1</a> of Coursera, Algorithms Part II
 *
 *  @author vahbuna
 */
public class Outcast {
    private final WordNet wn;
    /**
     * constructor takes a WordNet object.
     * @param wordnet input
     */
    public Outcast(final WordNet wordnet) {
        wn = wordnet;
    }

    /**
     * given an array of WordNet nouns, return an outcast.
     * @param nouns input
     * @return outcast
     */
    public String outcast(final String[] nouns) {
        int maxLength = Integer.MIN_VALUE;
        int outcast = 0;
        for (int i = 0; i < nouns.length; i++) {
            int length = 0;
            for (String noun : nouns) {
                length += wn.distance(nouns[i], noun);
            }
            if (length > maxLength) {
                    maxLength = length;
                    outcast = i;
            }
        }
        return nouns[outcast];
    }
}
