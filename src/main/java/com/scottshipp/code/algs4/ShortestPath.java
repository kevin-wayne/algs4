package com.scottshipp.code.algs4;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;

import java.util.*;

public class ShortestPath {

    private EdgeWeightedGraph graph;
    private Map<Integer, Vertex<Integer>> vertices;
    private PriorityQueue<Vertex> pq;

    class Vertex<K> implements Comparable<Vertex<K>> {
        private final K key;
        private double distance;

        Vertex(K vertexNumber) {
            this.key = vertexNumber;
            this.distance = Double.POSITIVE_INFINITY;
        }

        public double getShortestDistanceCalculatedSoFar() {
            return distance;
        }

        public void updateDistance(double newDistance) {
            this.distance = newDistance;
        }

        public K key() {
            return key;
        }

        @Override
        public int compareTo(Vertex<K> other) {
            Comparator<Vertex<K>> comparator = Comparator.comparing(Vertex::getShortestDistanceCalculatedSoFar);
            return comparator.compare(this, other);
        }
    }

    public ShortestPath(EdgeWeightedGraph graph, int startVertexKey) {
        this.graph = graph;
        vertices = new HashMap<>();
        pq = new PriorityQueue<>();
        initialize(graph, startVertexKey);
        generateShortestPathsFromStartVertex();
    }

    private void initialize(EdgeWeightedGraph graph, int startVertexKey) {
        for(int key = 0; key < graph.V(); key++) {
            Vertex<Integer> vertex = new Vertex<>(key);
            if(key == startVertexKey) {
                vertex.updateDistance(0.0);
                pq.add(vertex);
            }
            vertices.put(key, vertex);
        }
    }

    private void generateShortestPathsFromStartVertex() {
        while(!pq.isEmpty()) {
            Vertex<Integer> currentVertex = pq.poll();
            for(Edge edge : graph.adj(currentVertex.key())) {
                relax(currentVertex, edge);
            }
        }
    }

    private void relax(Vertex<Integer> currentVertex, Edge edge) {
        double distanceToCurrVert = currentVertex.getShortestDistanceCalculatedSoFar();
        int adjacentVertexKey = edge.other(currentVertex.key());
        Vertex<Integer> adjacentVertex = vertices.get(adjacentVertexKey);
        double knownShortestDistanceToAdj = adjacentVertex.getShortestDistanceCalculatedSoFar();
        double distanceToAdjVertFromCurrent = distanceToCurrVert + edge.weight();

        if(knownShortestDistanceToAdj > distanceToAdjVertFromCurrent) {
            adjacentVertex.distance = distanceToAdjVertFromCurrent;
            if(!pq.contains(adjacentVertex)) {
                pq.add(adjacentVertex);
            }
        }
    }

    public double getShortestDistance(int destination) {
        if(destination > graph.V()) {
            throw new NoSuchElementException("There is no vertex with value " + destination + " in the graph.");
        }
        return vertices.get(destination).getShortestDistanceCalculatedSoFar();
    }

}
