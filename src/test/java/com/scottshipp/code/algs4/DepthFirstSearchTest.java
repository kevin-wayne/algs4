package com.scottshipp.code.algs4;

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DepthFirstSearchTest {
    private static Logger logger = Logger.getLogger(DepthFirstSearch.class.toString());

    private Graph graph;

    @BeforeEach
    public void setup() {
        In in = new In("tinyG.txt");
        graph = new Graph(in);
        logger.info(graph.toString());
    }

    @Test
    public void testPathExists() {
        DepthFirstSearch dfs = new DepthFirstSearch(graph, 0);
        List<Integer> path = dfs.pathTo(3);
        logger.info(path.toString());
        logger.info(Arrays.toString(IntStream.range(0, 12).toArray()));
        logger.info(Arrays.toString(dfs.paths()));
        assertTrue(path.contains(6), "Path contains 6");
        assertTrue(path.contains(0), "Path contains 0");
        assertTrue(path.contains(4), "Path contains 4");
    }
}
