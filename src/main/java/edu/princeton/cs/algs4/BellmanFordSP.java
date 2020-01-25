/******************************************************************************
 *  Compilation:  javac BellmanFordSP.java
 *  Execution:    java BellmanFordSP filename.txt s
 *  Dependencies: EdgeWeightedDigraph.java DirectedEdge.java Queue.java
 *                EdgeWeightedDirectedCycle.java
 *  Data files:   https://algs4.cs.princeton.edu/44sp/tinyEWDn.txt
 *                https://algs4.cs.princeton.edu/44sp/mediumEWDnc.txt
 *
 *  Bellman-Ford shortest path algorithm. Computes the shortest path tree in
 *  edge-weighted digraph G from vertex s, or finds a negative cost cycle
 *  reachable from s.
 *
 *  % java BellmanFordSP tinyEWDn.txt 0
 *  0 to 0 ( 0.00)  
 *  0 to 1 ( 0.93)  0->2  0.26   2->7  0.34   7->3  0.39   3->6  0.52   6->4 -1.25   4->5  0.35   5->1  0.32
 *  0 to 2 ( 0.26)  0->2  0.26   
 *  0 to 3 ( 0.99)  0->2  0.26   2->7  0.34   7->3  0.39   
 *  0 to 4 ( 0.26)  0->2  0.26   2->7  0.34   7->3  0.39   3->6  0.52   6->4 -1.25   
 *  0 to 5 ( 0.61)  0->2  0.26   2->7  0.34   7->3  0.39   3->6  0.52   6->4 -1.25   4->5  0.35
 *  0 to 6 ( 1.51)  0->2  0.26   2->7  0.34   7->3  0.39   3->6  0.52   
 *  0 to 7 ( 0.60)  0->2  0.26   2->7  0.34   
 *
 *  % java BellmanFordSP tinyEWDnc.txt 0
 *  4->5  0.35
 *  5->4 -0.66
 *
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The {@code BellmanFordSP} class represents a data type for solving the
 *  single-source shortest paths problem in edge-weighted digraphs with
 *  no negative cycles. 
 *  The edge weights can be positive, negative, or zero.
 *  This class finds either a shortest path from the source vertex <em>s</em>
 *  to every other vertex or a negative cycle reachable from the source vertex.
 *  <p>
 *  This implementation uses a queue-based implementation of 
 *  the Bellman-Ford-Moore algorithm.
 *  The constructor takes &Theta;(<em>V</em> (<em>V</em> + <em>E</em>)) time
 *  in the worst case, where <em>V</em> is the number of vertices and
 *  <em>E</em> is the number of edges. In practice, it performs much better.
 *  Each instance method takes &Theta;(1) time.
 *  It uses &Theta;(<em>V</em>) extra space (not including the
 *  edge-weighted digraph).
 *  <p>
 *  For additional documentation,    
 *  see <a href="https://algs4.cs.princeton.edu/44sp">Section 4.4</a> of    
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne. 
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class BellmanFordSP {
    private double[] distTo;               // distTo[v] = distance  of shortest s->v path
    private DirectedEdge[] edgeTo;         // edgeTo[v] = last edge on shortest s->v path
    private boolean[] onQueue;             // onQueue[v] = is v currently on the queue?
    private Queue<Integer> queue;          // queue of vertices to relax
    private int cost;                      // number of calls to relax()
    private Iterable<DirectedEdge> cycle;  // negative cycle (or null if no such cycle)

    /**
     * Computes a shortest paths tree from {@code s} to every other vertex in
     * the edge-weighted digraph {@code G}.
     * @param G the acyclic digraph
     * @param s the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public BellmanFordSP(EdgeWeightedDigraph G, int s) {
        distTo  = new double[G.V()];
        edgeTo  = new DirectedEdge[G.V()];
        onQueue = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // Bellman-Ford algorithm
        queue = new Queue<Integer>();
        queue.enqueue(s);
        onQueue[s] = true;
        while (!queue.isEmpty() && !hasNegativeCycle()) {
            int v = queue.dequeue();
            onQueue[v] = false;
            relax(G, v);
        }

        assert check(G, s);
    }

    // relax vertex v and put other endpoints on queue if changed
    private void relax(EdgeWeightedDigraph G, int v) {
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
                if (!onQueue[w]) {
                    queue.enqueue(w);
                    onQueue[w] = true;
                }
            }
            if (++cost % G.V() == 0) {
                findNegativeCycle();
                if (hasNegativeCycle()) return;  // found a negative cycle
            }
        }
    }

    /**
     * Is there a negative cycle reachable from the source vertex {@code s}?
     * @return {@code true} if there is a negative cycle reachable from the
     *    source vertex {@code s}, and {@code false} otherwise
     */
    public boolean hasNegativeCycle() {
        return cycle != null;
    }

    /**
     * Returns a negative cycle reachable from the source vertex {@code s}, or {@code null}
     * if there is no such cycle.
     * @return a negative cycle reachable from the soruce vertex {@code s} 
     *    as an iterable of edges, and {@code null} if there is no such cycle
     */
    public Iterable<DirectedEdge> negativeCycle() {
        return cycle;
    }

    // by finding a cycle in predecessor graph
    private void findNegativeCycle() {
        int V = edgeTo.length;
        EdgeWeightedDigraph spt = new EdgeWeightedDigraph(V);
        for (int v = 0; v < V; v++)
            if (edgeTo[v] != null)
                spt.addEdge(edgeTo[v]);

        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(spt);
        cycle = finder.cycle();
    }

    /**
     * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
     * @param  v the destination vertex
     * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
     *         {@code Double.POSITIVE_INFINITY} if no such path
     * @throws UnsupportedOperationException if there is a negative cost cycle reachable
     *         from the source vertex {@code s}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public double distTo(int v) {
        validateVertex(v);
        if (hasNegativeCycle())
            throw new UnsupportedOperationException("Negative cost cycle exists");
        return distTo[v];
    }

    /**
     * Is there a path from the source {@code s} to vertex {@code v}?
     * @param  v the destination vertex
     * @return {@code true} if there is a path from the source vertex
     *         {@code s} to vertex {@code v}, and {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source {@code s} to vertex {@code v}.
     * @param  v the destination vertex
     * @return a shortest path from the source {@code s} to vertex {@code v}
     *         as an iterable of edges, and {@code null} if no such path
     * @throws UnsupportedOperationException if there is a negative cost cycle reachable
     *         from the source vertex {@code s}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<DirectedEdge> pathTo(int v) {
        validateVertex(v);
        if (hasNegativeCycle())
            throw new UnsupportedOperationException("Negative cost cycle exists");
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }

    // check optimality conditions: either 
    // (i) there exists a negative cycle reacheable from s
    //     or 
    // (ii)  for all edges e = v->w:            distTo[w] <= distTo[v] + e.weight()
    // (ii') for all edges e = v->w on the SPT: distTo[w] == distTo[v] + e.weight()
    private boolean check(EdgeWeightedDigraph G, int s) {

        // has a negative cycle
        if (hasNegativeCycle()) {
            double weight = 0.0;
            for (DirectedEdge e : negativeCycle()) {
                weight += e.weight();
            }
            if (weight >= 0.0) {
                System.err.println("error: weight of negative cycle = " + weight);
                return false;
            }
        }

        // no negative cycle reachable from source
        else {

            // check that distTo[v] and edgeTo[v] are consistent
            if (distTo[s] != 0.0 || edgeTo[s] != null) {
                System.err.println("distanceTo[s] and edgeTo[s] inconsistent");
                return false;
            }
            for (int v = 0; v < G.V(); v++) {
                if (v == s) continue;
                if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                    System.err.println("distTo[] and edgeTo[] inconsistent");
                    return false;
                }
            }

            // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
            for (int v = 0; v < G.V(); v++) {
                for (DirectedEdge e : G.adj(v)) {
                    int w = e.to();
                    if (distTo[v] + e.weight() < distTo[w]) {
                        System.err.println("edge " + e + " not relaxed");
                        return false;
                    }
                }
            }

            // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
            for (int w = 0; w < G.V(); w++) {
                if (edgeTo[w] == null) continue;
                DirectedEdge e = edgeTo[w];
                int v = e.from();
                if (w != e.to()) return false;
                if (distTo[v] + e.weight() != distTo[w]) {
                    System.err.println("edge " + e + " on shortest path not tight");
                    return false;
                }
            }
        }

        StdOut.println("Satisfies optimality conditions");
        StdOut.println();
        return true;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Unit tests the {@code BellmanFordSP} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        int s = Integer.parseInt(args[1]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);

        BellmanFordSP sp = new BellmanFordSP(G, s);

        // print negative cycle
        if (sp.hasNegativeCycle()) {
            for (DirectedEdge e : sp.negativeCycle())
                StdOut.println(e);
        }

        // print shortest paths
        else {
            for (int v = 0; v < G.V(); v++) {
                if (sp.hasPathTo(v)) {
                    StdOut.printf("%d to %d (%5.2f)  ", s, v, sp.distTo(v));
                    for (DirectedEdge e : sp.pathTo(v)) {
                        StdOut.print(e + "   ");
                    }
                    StdOut.println();
                }
                else {
                    StdOut.printf("%d to %d           no path\n", s, v);
                }
            }
        }

    }

}

/******************************************************************************
 *  Copyright 2002-2020, Robert Sedgewick and Kevin Wayne.
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
