/******************************************************************************
 *  Compilation:  javac WordNet.java
 *  Execution:    java edu.assignment.wordnet.WordNet
 *  Dependencies: Digraph.java In.java
 *  Data files:   src/main/resources/data/wordnet
 *
 *  Implements an immutable data type WordNet.
 ******************************************************************************/
package edu.assignment.wordnet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is synset digraph of wordnet.
 * <p>
 * For additional documentation,
 * see <a href="http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html">
 * Programming Assignment 1</a> of Coursera, Algorithms Part II
 *
 *  @author vahbuna
 */
public class WordNet {

    private final HashMap<String, List<Integer>> nounIdMap = new HashMap<>();
    private final HashMap<Integer, String> idSynsetMap = new HashMap<>();
    private final Digraph dag;
    /**
     * constructor takes the name of the two input files.
     * @param synsets file path
     * @param hypernyms file path
     */
    public WordNet(final String synsets, final String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new java.lang.IllegalArgumentException();
        }
        In in = new In(synsets);
        while (in.hasNextLine()) {
            String line = in.readLine();

            String[] fields = line.split(",");
            int id = Integer.parseInt(fields[0]);
            String[] nouns = fields[1].split(" ");
            idSynsetMap.put(id, fields[1]);

            for (int i = 0; i < nouns.length; i++) {
                List<Integer> ids = nounIdMap.getOrDefault(nouns[i],
                        new ArrayList<Integer>());
                ids.add(id);
                nounIdMap.put(nouns[i], ids);
            }
        }

        dag = new Digraph(idSynsetMap.size());
        in = new In(hypernyms);

        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] ids = line.split(",");
            for (int i = 1; i < ids.length; i++) {
                dag.addEdge(Integer.parseInt(ids[0]), Integer.parseInt(ids[i]));
            }
        }

        DirectedCycle cycle = new DirectedCycle(dag);
        if (cycle.hasCycle()) {
            throw new java.lang.IllegalArgumentException();
        }

        long roots = idSynsetMap.keySet().stream()
                .filter((id) -> (dag.outdegree(id) == 0))
                .count();

        if (roots != 1) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    /**
     * Return all WordNet nouns.
     * @return nouns.
     */
    public Iterable<String> nouns() {
       return nounIdMap.keySet();
    }

    /**
     * is the word a WordNet noun?
     * @param word input
     * @return true or false
     */
    public boolean isNoun(final String word) {
        if (word == null) {
            throw new java.lang.IllegalArgumentException();
        }
       return nounIdMap.containsKey(word);
    }

    /**
     * distance between nounA and nounB (defined below).
     * @param nounA first input
     * @param nounB second input
     * @return distance
     */
    public int distance(final String nounA, final String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException();
        }

        SAP sap = new SAP(dag);
        return sap.length(nounIdMap.get(nounA), nounIdMap.get(nounB));
    }

    /**
     * a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB in a shortest ancestral path (defined below).
     * @param nounA input
     * @param nounB input
     * @return common ancestor
     */
    public String sap(final String nounA, final String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException();
        }

        SAP sap = new SAP(dag);
        int ancestorId = sap.ancestor(nounIdMap.get(nounA),
                nounIdMap.get(nounB));
        return idSynsetMap.get(ancestorId);
    }
}
