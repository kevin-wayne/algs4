package com.scottshipp.code.algs4;

import edu.princeton.cs.algs4.Graph;

import java.util.*;

public class BreadthFirstSearch {
    private Deque<Integer> queue;
    private boolean[] visited;
    private int[] edgeTo;
    int startVertex;

    public BreadthFirstSearch(Graph g, int startV) {
        this.startVertex = startV;
        queue = new LinkedList<>();
        visited = new boolean[g.V()];
        edgeTo = new int[g.V()];
        queue.addFirst(startV);
        visited[startV] = true;
        bfs(g, startV);
    }

    private void bfs(Graph g, int startV) {
        while(!queue.isEmpty()) {
            int currV = queue.removeLast();
            for(int v : g.adj(currV)) {
                if(visited[v]) {
                    continue;
                }
                queue.addFirst(v);
                visited[v] = true;
                edgeTo[v] = currV;
            }
        }
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
