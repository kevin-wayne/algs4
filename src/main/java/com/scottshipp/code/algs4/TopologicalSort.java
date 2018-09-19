package com.scottshipp.code.algs4;

import edu.princeton.cs.algs4.Digraph;

import java.util.Deque;
import java.util.LinkedList;

public class TopologicalSort {

    private int startVertex;
    private boolean marked[];
    private Deque<Integer> reversePost;

    public TopologicalSort(Digraph G, int startVertex) {
        this.startVertex = startVertex;
        this.marked = new boolean[G.V()];
        reversePost = new LinkedList<>();
        for(int v = 0; v < G.V(); v++) {
            if(!marked[v]) {
                sort(G, v);
            }
        }

    }

    private void sort(Digraph G, int currentVertex) {
        marked[currentVertex] = true;
        for(int vertex : G.adj(currentVertex)) {
            if(!marked[vertex]) {
                sort(G, vertex);
            }
        }
        reversePost.push(currentVertex);
    }

    public Iterable<Integer> sorted() {
        return reversePost;
    }

}
