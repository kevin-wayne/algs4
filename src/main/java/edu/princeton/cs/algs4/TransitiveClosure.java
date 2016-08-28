/******************************************************************************
 *  Compilation:  javac TransitiveClosure.java
 *  Execution:    java TransitiveClosure filename.txt
 *  Dependencies: Digraph.java DepthFirstDirectedPaths.java In.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *
 *  Compute transitive closure of a digraph and support
 *  reachability queries.
 *
 *  Preprocessing time: O(V(E + V)) time.
 *  Query time: O(1).
 *  Space: O(V^2).
 *
 *  % java TransitiveClosure tinyDG.txt
 *         0  1  2  3  4  5  6  7  8  9 10 11 12
 *  --------------------------------------------
 *    0:   T  T  T  T  T  T                     
 *    1:      T                                 
 *    2:   T  T  T  T  T  T                     
 *    3:   T  T  T  T  T  T                     
 *    4:   T  T  T  T  T  T                     
 *    5:   T  T  T  T  T  T                     
 *    6:   T  T  T  T  T  T  T        T  T  T  T
 *    7:   T  T  T  T  T  T  T  T  T  T  T  T  T
 *    8:   T  T  T  T  T  T  T  T  T  T  T  T  T
 *    9:   T  T  T  T  T  T           T  T  T  T
 *   10:   T  T  T  T  T  T           T  T  T  T
 *   11:   T  T  T  T  T  T           T  T  T  T
 *   12:   T  T  T  T  T  T           T  T  T  T
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The {@code TransitiveClosure} class represents a data type for 
 *  computing the transitive closure of a digraph.
 *  <p>
 *  This implementation runs depth-first search from each vertex.
 *  The constructor takes time proportional to <em>V</em>(<em>V</em> + <em>E</em>)
 *  (in the worst case) and uses space proportional to <em>V</em><sup>2</sup>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  <p>
 *  For large digraphs, you may want to consider a more sophisticated algorithm.
 *  <a href = "http://www.cs.hut.fi/~enu/thesis.html">Nuutila</a> proposes two
 *  algorithm for the problem (based on strong components and an interval representation)
 *  that runs in <em>E</em> + <em>V</em> time on typical digraphs.
 *
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class TransitiveClosure {
    private DirectedDFS[] tc;  // tc[v] = reachable from v

    /**
     * Computes the transitive closure of the digraph {@code G}.
     * @param G the digraph
     */
    public TransitiveClosure(Digraph G) {
        tc = new DirectedDFS[G.V()];
        for (int v = 0; v < G.V(); v++)
            tc[v] = new DirectedDFS(G, v);
    }

    /**
     * Is there a directed path from vertex {@code v} to vertex {@code w} in the digraph?
     * @param v the source vertex
     * @param w the target vertex
     * @return {@code true} if there is a directed path from {@code v} to {@code w},
     *    {@code false} otherwise
     */
    public boolean reachable(int v, int w) {
        return tc[v].marked(w);
    }

    /**
     * Unit tests the {@code TransitiveClosure} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);

        TransitiveClosure tc = new TransitiveClosure(G);

        // print header
        StdOut.print("     ");
        for (int v = 0; v < G.V(); v++)
            StdOut.printf("%3d", v);
        StdOut.println();
        StdOut.println("--------------------------------------------");

        // print transitive closure
        for (int v = 0; v < G.V(); v++) {
            StdOut.printf("%3d: ", v);
            for (int w = 0; w < G.V(); w++) {
                if (tc.reachable(v, w)) StdOut.printf("  T");
                else                    StdOut.printf("   ");
            }
            StdOut.println();
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
