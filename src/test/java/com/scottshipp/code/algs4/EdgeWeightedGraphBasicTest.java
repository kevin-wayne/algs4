package com.scottshipp.code.algs4;

import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EdgeWeightedGraphBasicTest {

    EdgeWeightedGraph graph;

    @Before
    public void setup() {
        In in = new In("tinyEWG.txt");
        graph = new EdgeWeightedGraph(in);
    }

    @Test
    public void test1() {
        ShortestPath shortestPath = new ShortestPath(graph, 4);
        assertEquals(0.81, shortestPath.getShortestDistance(3), 0.01);
        assertEquals(0.93, shortestPath.getShortestDistance(6), 0.01);
    }



}
