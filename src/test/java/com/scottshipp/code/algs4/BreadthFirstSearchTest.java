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

public class BreadthFirstSearchTest {
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
        BreadthFirstSearch bfs = new BreadthFirstSearch(graph, 0);
        List<Integer> path = bfs.pathTo(3);
        logger.info(path.toString());
        logger.info(Arrays.toString(IntStream.range(0, 12).toArray()));
        logger.info(Arrays.toString(bfs.paths()));
        assertTrue(path.contains(5), "Path contains 5");
        assertTrue(path.contains(3), "Path contains 3");
    }

}
