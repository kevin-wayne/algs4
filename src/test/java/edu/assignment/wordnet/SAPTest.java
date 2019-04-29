/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.assignment.wordnet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author vahbuna
 */
public class SAPTest {

    /**
     * test shortest ancestral path.
     */
    @Test
    public void testLength() {
        In in = new In("data/wordnet/digraph2.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        assertEquals(2, sap.length(3, 5));
    }

    /**
     * find sap among multiple synsets.
     */
    @Test
    public void testSetLength() {
        In in = new In("data/wordnet/digraph25.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        ArrayList<Integer> v = new ArrayList<>();
        v.add(13);
        v.add(23);
        v.add(24);

        ArrayList<Integer> w = new ArrayList<>();
        w.add(6);
        w.add(16);
        w.add(17);
        assertEquals(4, sap.length(v, w));
    }

    @Test
    public void testAncestors() {
        In in = new In("data/wordnet/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        assertEquals(1, sap.ancestor(3, 11));
        assertEquals(5, sap.ancestor(9, 12));
        assertEquals(0, sap.ancestor(7, 2));
        assertEquals(-1, sap.ancestor(1, 6));
    }

    @Test
    public void testAncestor() {
        In in = new In("data/wordnet/digraph3.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        assertEquals(9, sap.ancestor(12, 9));
    }
}
