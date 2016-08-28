/******************************************************************************
 *  Compilation:  javac AcyclicSP.java
 *  Execution:    java AcyclicSP V E
 *  Dependencies: EdgeWeightedDigraph.java DirectedEdge.java Topological.java
 *  Data files:   http://algs4.cs.princeton.edu/44sp/tinyEWDAG.txt
 *
 *  Computes shortest paths in an edge-weighted acyclic digraph.
 *
 *  % java AcyclicSP tinyEWDAG.txt 5
 *  5 to 0 (0.73)  5->4  0.35   4->0  0.38   
 *  5 to 1 (0.32)  5->1  0.32   
 *  5 to 2 (0.62)  5->7  0.28   7->2  0.34   
 *  5 to 3 (0.61)  5->1  0.32   1->3  0.29   
 *  5 to 4 (0.35)  5->4  0.35   
 *  5 to 5 (0.00)  
 *  5 to 6 (1.13)  5->1  0.32   1->3  0.29   3->6  0.52   
 *  5 to 7 (0.28)  5->7  0.28   
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The {@code AcyclicSP} class represents a data type for solving the
 *  single-source shortest paths problem in edge-weighted directed acyclic
 *  graphs (DAGs). The edge weights can be positive, negative, or zero.
 *  <p>
 *  This implementation uses a topological-sort based algorithm.
 *  The constructor takes time proportional to <em>V</em> + <em>E</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  Afterwards, the {@code distTo()} and {@code hasPathTo()} methods take
 *  constant time and the {@code pathTo()} method takes time proportional to the
 *  number of edges in the shortest path returned.
 *  <p>
 *  For additional documentation,    
 *  see <a href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of    
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne. 
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class AcyclicSP {
    private double[] distTo;         // distTo[v] = distance  of shortest s->v path
    private DirectedEdge[] edgeTo;   // edgeTo[v] = last edge on shortest s->v path


    /**
     * Computes a shortest paths tree from {@code s} to every other vertex in
     * the directed acyclic graph {@code G}.
     * @param G the acyclic digraph
     * @param s the source vertex
     * @throws IllegalArgumentException if the digraph is not acyclic
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public AcyclicSP(EdgeWeightedDigraph G, int s) {
        distTo = new double[G.V()];
        edgeTo = new DirectedEdge[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // visit vertices in toplogical order
        Topological topological = new Topological(G);
        if (!topological.hasOrder())
            throw new IllegalArgumentException("Digraph is not acyclic.");
        for (int v : topological.order()) {
            for (DirectedEdge e : G.adj(v))
                relax(e);
        }
    }

    // relax edge e
    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
        }       
    }

    /**
     * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
     * @param v the destination vertex
     * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
     *    {@code Double.POSITIVE_INFINITY} if no such path
     */
    public double distTo(int v) {
        return distTo[v];
    }

    /**
     * Is there a path from the source vertex {@code s} to vertex {@code v}?
     * @param v the destination vertex
     * @return {@code true} if there is a path from the source vertex
     *    {@code s} to vertex {@code v}, and {@code false} otherwise
     */
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
     * @param v the destination vertex
     * @return a shortest path from the source vertex {@code s} to vertex {@code v}
     *    as an iterable of edges, and {@code null} if no such path
     */
    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }


    /**
     * Unit tests the {@code AcyclicSP} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        int s = Integer.parseInt(args[1]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);

        // find shortest path from s to each other vertex in DAG
        AcyclicSP sp = new AcyclicSP(G, s);
        for (int v = 0; v < G.V(); v++) {
            if (sp.hasPathTo(v)) {
                StdOut.printf("%d to %d (%.2f)  ", s, v, sp.distTo(v));
                for (DirectedEdge e : sp.pathTo(v)) {
                    StdOut.print(e + "   ");
                }
                StdOut.println();
            }
            else {
                StdOut.printf("%d to %d         no path\n", s, v);
            }
        }
    }
}

/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
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
