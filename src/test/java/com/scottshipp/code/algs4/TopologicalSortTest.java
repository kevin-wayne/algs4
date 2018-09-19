package com.scottshipp.code.algs4;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static junit.framework.TestCase.assertEquals;

public class TopologicalSortTest {
    private static Logger logger = Logger.getLogger(TopologicalSortTest.class.toString());

    private Digraph graph;

    @BeforeEach
    public void setup() {
        In in = new In("anotherTinyDAG.txt");
        graph = new Digraph(in);
        logger.info(graph.toString());
    }

    @Test
    public void testTopoSortOfAnotherTinyDAG() {
        TopologicalSort topologicalSort = new TopologicalSort(graph, 0);
        StringBuilder sb = new StringBuilder();
        for(int i : topologicalSort.sorted()) {
            sb.append(i + " ");
        }
        assertEquals("3 6 0 5 2 1 4", sb.toString());
    }

}
