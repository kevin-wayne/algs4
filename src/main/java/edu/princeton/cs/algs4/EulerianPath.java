/******************************************************************************
 *  Compilation:  javac EulerianPath.java
 *  Execution:    java EulerianPath V E
 *  Dependencies: Graph.java Stack.java StdOut.java
 *
 *  Find an Eulerian path in a graph, if one exists.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The {@code EulerianPath} class represents a data type
 *  for finding an Eulerian path in a graph.
 *  An <em>Eulerian path</em> is a path (not necessarily simple) that
 *  uses every edge in the graph exactly once.
 *  <p>
 *  This implementation uses a nonrecursive depth-first search.
 *  The constructor takes &Theta;(<em>E</em> + <em>V</em>) time in the worst
 *  case, where <em>E</em> is the number of edges and <em>V</em> is
 *  the number of vertices.
 *  Each instance method takes &Theta;(1) time.
 *  It uses &Theta;(<em>E</em> + <em>V</em>) extra space in the worst case
 *  (not including the digraph).
 *  <p>
 *  To compute Eulerian cycles in graphs, see {@link EulerianCycle}.
 *  To compute Eulerian cycles and paths in digraphs, see
 *  {@link DirectedEulerianCycle} and {@link DirectedEulerianPath}.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/41graph">Section 4.1</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Nate Liu
 */
public class EulerianPath {
    private Stack<Integer> path = null;   // Eulerian path; null if no suh path

    // an undirected edge, with a field to indicate whether the edge has already been used
    private static class Edge {
        private final int v;
        private final int w;
        private boolean isUsed;

        public Edge(int v, int w) {
            this.v = v;
            this.w = w;
            isUsed = false;
        }

        // returns the other vertex of the edge
        public int other(int vertex) {
            if      (vertex == v) return w;
            else if (vertex == w) return v;
            else throw new IllegalArgumentException("Illegal endpoint");
        }
    }

    /**
     * Computes an Eulerian path in the specified graph, if one exists.
     *
     * @param graph the graph
     */
    public EulerianPath(Graph graph) {

        // find vertex from which to start potential Eulerian path:
        // a vertex v with odd degree(v) if it exits;
        // otherwise a vertex with degree(v) > 0
        int oddDegreeVertices = 0;
        int s = nonIsolatedVertex(graph);
        for (int v = 0; v < graph.V(); v++) {
            if (graph.degree(v) % 2 != 0) {
                oddDegreeVertices++;
                s = v;
            }
        }

        // graph can't have an Eulerian path
        // (this condition is needed for correctness)
        if (oddDegreeVertices > 2) return;

        // special case for graph with zero edges (has a degenerate Eulerian path)
        if (s == -1) s = 0;

        // create local view of adjacency lists, to iterate one vertex at a time
        // the helper Edge data type is used to avoid exploring both copies of an edge v-w
        Queue<Edge>[] adj = (Queue<Edge>[]) new Queue[graph.V()];
        for (int v = 0; v < graph.V(); v++)
            adj[v] = new Queue<Edge>();

        for (int v = 0; v < graph.V(); v++) {
            int selfLoops = 0;
            for (int w : graph.adj(v)) {
                // careful with self loops
                if (v == w) {
                    if (selfLoops % 2 == 0) {
                        Edge e = new Edge(v, w);
                        adj[v].enqueue(e);
                        adj[w].enqueue(e);
                    }
                    selfLoops++;
                }
                else if (v < w) {
                    Edge e = new Edge(v, w);
                    adj[v].enqueue(e);
                    adj[w].enqueue(e);
                }
            }
        }

        // initialize stack with any non-isolated vertex
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(s);

        // greedily search through edges in iterative DFS style
        path = new Stack<Integer>();
        while (!stack.isEmpty()) {
            int v = stack.pop();
            while (!adj[v].isEmpty()) {
                Edge edge = adj[v].dequeue();
                if (edge.isUsed) continue;
                edge.isUsed = true;
                stack.push(v);
                v = edge.other(v);
            }
            // push vertex with no more leaving edges to path
            path.push(v);
        }

        // check if all edges are used
        if (path.size() != graph.E() + 1)
            path = null;

        assert certifySolution(graph);
    }

    /**
     * Returns the sequence of vertices on an Eulerian path.
     *
     * @return the sequence of vertices on an Eulerian path;
     *         {@code null} if no such path
     */
    public Iterable<Integer> path() {
        return path;
    }

    /**
     * Returns true if the graph has an Eulerian path.
     *
     * @return {@code true} if the graph has an Eulerian path;
     *         {@code false} otherwise
     */
    public boolean hasEulerianPath() {
        return path != null;
    }


    // returns any non-isolated vertex; -1 if no such vertex
    private static int nonIsolatedVertex(Graph graph) {
        for (int v = 0; v < graph.V(); v++)
            if (graph.degree(v) > 0)
                return v;
        return -1;
    }


    /**************************************************************************
     *
     *  The code below is solely for testing correctness of the data type.
     *
     **************************************************************************/

    // Determines whether a graph has an Eulerian path using necessary
    // and sufficient conditions (without computing the path itself):
    //    - degree(v) is even for every vertex, except for possibly two
    //    - the graph is connected (ignoring isolated vertices)
    // This method is solely for unit testing.
    private static boolean satisfiesNecessaryAndSufficientConditions(Graph graph) {
        if (graph.E() == 0) return true;

        // Condition 1: degree(v) is even except for possibly two
        int oddDegreeVertices = 0;
        for (int v = 0; v < graph.V(); v++)
            if (graph.degree(v) % 2 != 0)
                oddDegreeVertices++;
        if (oddDegreeVertices > 2) return false;

        // Condition 2: graph is connected, ignoring isolated vertices
        int s = nonIsolatedVertex(graph);
        BreadthFirstPaths bfs = new BreadthFirstPaths(graph, s);
        for (int v = 0; v < graph.V(); v++)
            if (graph.degree(v) > 0 && !bfs.hasPathTo(v))
                return false;

        return true;
    }

    // check that solution is correct
    private boolean certifySolution(Graph graph) {

        // internal consistency check
        if (hasEulerianPath() == (path() == null)) return false;

        // hashEulerianPath() returns correct value
        if (hasEulerianPath() != satisfiesNecessaryAndSufficientConditions(graph)) return false;

        // nothing else to check if no Eulerian path
        if (path == null) return true;

        // check that path() uses correct number of edges
        if (path.size() != graph.E() + 1) return false;

        // check that path() is a path in G
        // TODO

        return true;
    }


    private static void unitTest(Graph graph, String description) {
        StdOut.println(description);
        StdOut.println("-------------------------------------");
        StdOut.print(graph);

        EulerianPath euler = new EulerianPath(graph);

        StdOut.print("Eulerian path:  ");
        if (euler.hasEulerianPath()) {
            for (int v : euler.path()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        }
        else {
            StdOut.println("none");
        }
        StdOut.println();
    }


    /**
     * Unit tests the {@code EulerianPath} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);


        // Eulerian cycle
        Graph graph1 = GraphGenerator.eulerianCycle(V, E);
        unitTest(graph1, "Eulerian cycle");

        // Eulerian path
        Graph graph2 = GraphGenerator.eulerianPath(V, E);
        unitTest(graph2, "Eulerian path");

        // add one random edge
        Graph graph3 = new Graph(graph2);
        graph3.addEdge(StdRandom.uniformInt(V), StdRandom.uniformInt(V));
        unitTest(graph3, "one random edge added to Eulerian path");

        // self loop
        Graph graph4 = new Graph(V);
        int v4 = StdRandom.uniformInt(V);
        graph4.addEdge(v4, v4);
        unitTest(graph4, "single self loop");

        // single edge
        Graph graph5 = new Graph(V);
        graph5.addEdge(StdRandom.uniformInt(V), StdRandom.uniformInt(V));
        unitTest(graph5, "single edge");

        // empty graph
        Graph graph6 = new Graph(V);
        unitTest(graph6, "empty graph");

        // random graph
        Graph graph7 = GraphGenerator.simple(V, E);
        unitTest(graph7, "simple graph");
    }
}

/******************************************************************************
 *  Copyright 2002-2025, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
