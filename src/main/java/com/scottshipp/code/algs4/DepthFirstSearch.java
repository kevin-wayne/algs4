package com.scottshipp.code.algs4;

import edu.princeton.cs.algs4.Graph;

import java.util.ArrayList;
import java.util.List;

public class DepthFirstSearch {

    private int startVertex;
    private boolean marked[];
    private int edgeTo[];

    public DepthFirstSearch(Graph G, int startVertex) {
        this.startVertex = startVertex;
        this.marked = new boolean[G.V()];
        this.edgeTo = new int[G.V()];
        this.edgeTo[startVertex] = startVertex;
        dfs(G, startVertex);
    }

    private void dfs(Graph G, int currentVertex) {
        marked[currentVertex] = true;
        for(int vertex : G.adj(currentVertex)) {
            if(!marked[vertex]) {
                edgeTo[vertex] = currentVertex;
                dfs(G, vertex);
            }
        }
    }

    public boolean marked(int v) {
        return marked[v];
    }

    public int[] paths() {
        return edgeTo;
    }

    public List<Integer> pathTo(int destination) {
        List<Integer> path = new ArrayList<>();
        int v = destination;
        while(v != startVertex) {
            path.add(v);
            v = edgeTo[v];
        }
        path.add(edgeTo[v]);
        return path;
    }
}
