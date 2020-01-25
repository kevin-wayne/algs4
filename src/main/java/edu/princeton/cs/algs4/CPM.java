/******************************************************************************
 *  Compilation:  javac CPM.java
 *  Execution:    java CPM < input.txt
 *  Dependencies: EdgeWeightedDigraph.java AcyclicDigraphLP.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/44sp/jobsPC.txt
 *
 *  Critical path method.
 *
 *  % java CPM < jobsPC.txt
 *   job   start  finish
 *  --------------------
 *     0     0.0    41.0
 *     1    41.0    92.0
 *     2   123.0   173.0
 *     3    91.0   127.0
 *     4    70.0   108.0
 *     5     0.0    45.0
 *     6    70.0    91.0
 *     7    41.0    73.0
 *     8    91.0   123.0
 *     9    41.0    70.0
 *  Finish time:   173.0
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The {@code CPM} class provides a client that solves the
 *  parallel precedence-constrained job scheduling problem
 *  via the <em>critical path method</em>. It reduces the problem
 *  to the longest-paths problem in edge-weighted DAGs.
 *  It builds an edge-weighted digraph (which must be a DAG)
 *  from the job-scheduling problem specification,
 *  finds the longest-paths tree, and computes the longest-paths
 *  lengths (which are precisely the start times for each job).
 *  <p>
 *  This implementation uses {@link AcyclicLP} to find a longest
 *  path in a DAG.
 *  The program takes &Theta;(<em>V</em> + <em>E</em>) time in
 *  the worst case, where <em>V</em> is the number of jobs and
 *  <em>E</em> is the number of precedence constraints.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class CPM {

    // this class cannot be instantiated
    private CPM() { }

    /**
     *  Reads the precedence constraints from standard input
     *  and prints a feasible schedule to standard output.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        // number of jobs
        int n = StdIn.readInt();

        // source and sink
        int source = 2*n;
        int sink   = 2*n + 1;

        // build network
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(2*n + 2);
        for (int i = 0; i < n; i++) {
            double duration = StdIn.readDouble();
            G.addEdge(new DirectedEdge(source, i, 0.0));
            G.addEdge(new DirectedEdge(i+n, sink, 0.0));
            G.addEdge(new DirectedEdge(i, i+n,    duration));

            // precedence constraints
            int m = StdIn.readInt();
            for (int j = 0; j < m; j++) {
                int precedent = StdIn.readInt();
                G.addEdge(new DirectedEdge(n+i, precedent, 0.0));
            }
        }

        // compute longest path
        AcyclicLP lp = new AcyclicLP(G, source);

        // print results
        StdOut.println(" job   start  finish");
        StdOut.println("--------------------");
        for (int i = 0; i < n; i++) {
            StdOut.printf("%4d %7.1f %7.1f\n", i, lp.distTo(i), lp.distTo(i+n));
        }
        StdOut.printf("Finish time: %7.1f\n", lp.distTo(sink));
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
