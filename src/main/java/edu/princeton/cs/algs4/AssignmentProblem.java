/******************************************************************************
 *  Compilation:  javac AssignmentProblem.java
 *  Execution:    java AssignmentProblem N
 *  Dependencies: DijkstraSP.java DirectedEdge.java
 *
 *  Solve an N-by-N assignment problem in N^3 log N time using the
 *  successive shortest path algorithm.
 *
 *  Assumes N-by-N cost matrix is nonnegative.
 *  TODO: remove this assumption
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The <tt>AssignmentProblem</tt> class represents a data type for computing
 *  an optimal solution to an <em>N</em>-by-<em>N</em> <em>assignment problem</em>.
 *  The assignment problem is to find a minimum weight matching in an
 *  edge-weighted complete bipartite graph.
 *  <p>
 *  The data type supplies methods for determining the optimal solution
 *  and the corresponding dual solution.
 *  <p>
 *  This implementation uses the <em>successive shortest paths algorithm</em>.
 *  The order of growth of the running time in the worst case is
 *  O(<em>N</em>^3 log <em>N</em>) to solve an <em>N</em>-by-<em>N</em>
 *  instance.
 *  <p>
 *  See also {@link WeightedBipartiteMatching}, which solves the problem
 *  in O(<em>E V</em> log <em>V</em>) time in the worst case
 *  for bipartite graphs with <em>V</em> vertices and <em>E</em> edges.
 *  <p>
 *  For additional documentation, see
 *  <a href="http://algs4.cs.princeton.edu/65reductions">Section 6.5</a>
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class AssignmentProblem {
    private static final int UNMATCHED = -1;

    private int N;              // number of rows and columns
    private double[][] weight;  // the N-by-N cost matrix
    private double[] px;        // px[i] = dual variable for row i
    private double[] py;        // py[j] = dual variable for col j
    private int[] xy;           // xy[i] = j means i-j is a match
    private int[] yx;           // yx[j] = i means i-j is a match

    /**
     * Determines an optimal solution to the assignment problem.
     *
     * @param  weight the <em>N</em>-by-<em>N</em> matrix of weights
     * @throws IllegalArgumentException unless all weights are nonnegative
     * @throws NullPointerException if <tt>weight</tt> is <tt>null</tt>
     */ 
    public AssignmentProblem(double[][] weight) {
        N = weight.length;
        this.weight = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (!(weight[i][j] >= 0.0))
                    throw new IllegalArgumentException("weights must be nonnegative");
                this.weight[i][j] = weight[i][j];
            }
        }

        // dual variables
        px = new double[N];
        py = new double[N];

        // initial matching is empty
        xy = new int[N];
        yx = new int[N];
        for (int i = 0; i < N; i++)
             xy[i] = UNMATCHED;
        for (int j = 0; j < N; j++)
             yx[j] = UNMATCHED;

        // add N edges to matching
        for (int k = 0; k < N; k++) {
            assert isDualFeasible();
            assert isComplementarySlack();
            augment();
        }
        assert certifySolution();
    }

    // find shortest augmenting path and upate
    private void augment() {

        // build residual graph
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(2*N+2);
        int s = 2*N, t = 2*N+1;
        for (int i = 0; i < N; i++) {
            if (xy[i] == UNMATCHED)
                G.addEdge(new DirectedEdge(s, i, 0.0));
        }
        for (int j = 0; j < N; j++) {
            if (yx[j] == UNMATCHED)
                G.addEdge(new DirectedEdge(N+j, t, py[j]));
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (xy[i] == j) G.addEdge(new DirectedEdge(N+j, i, 0.0));
                else            G.addEdge(new DirectedEdge(i, N+j, reducedCost(i, j)));
            }
        }

        // compute shortest path from s to every other vertex
        DijkstraSP spt = new DijkstraSP(G, s);

        // augment along alternating path
        for (DirectedEdge e : spt.pathTo(t)) {
            int i = e.from(), j = e.to() - N;
            if (i < N) {
                xy[i] = j;
                yx[j] = i;
            }
        }

        // update dual variables
        for (int i = 0; i < N; i++)
            px[i] += spt.distTo(i);
        for (int j = 0; j < N; j++)
            py[j] += spt.distTo(N+j);
    }

    // reduced cost of i-j
    private double reducedCost(int i, int j) {
        return weight[i][j] + px[i] - py[j];
    }

    /**
     * Returns the dual optimal value for the specified row.
     *
     * @param  i the row index
     * @return the dual optimal value for row <tt>i</tt>
     * @throws IndexOutOfBoundsException unless <tt>0 &le; i &lt; N</tt>
     *
     */
    // dual variable for row i
    public double dualRow(int i) {
        validate(i);
        return px[i];
    }

    /**
     * Returns the dual optimal value for the specified column.
     *
     * @param  j the column index
     * @return the dual optimal value for column <tt>j</tt>
     * @throws IndexOutOfBoundsException unless <tt>0 &le; j &lt; N</tt>
     *
     */
    public double dualCol(int j) {
        validate(j);
        return py[j];
    }

    /**
     * Returns the column associated with the specified row in the optimal solution.
     *
     * @param  i the row index
     * @return the column matched to row <tt>i</tt> in the optimal solution
     * @throws IndexOutOfBoundsException unless <tt>0 &le; i &lt; N</tt>
     *
     */
    public int sol(int i) {
        validate(i);
        return xy[i];
    }

    /**
     * Returns the total weight of the optimal solution
     *
     * @return the total weight of the optimal solution
     *
     */
    public double weight() {
        double total = 0.0;
        for (int i = 0; i < N; i++) {
            if (xy[i] != UNMATCHED)
                total += weight[i][xy[i]];
        }
        return total;
    }

    private void validate(int i) {
        if (i < 0 || i >= N) throw new IndexOutOfBoundsException();
    }


    /**************************************************************************
     *
     *  The code below is solely for testing correctness of the data type.
     *
     **************************************************************************/

    // check that dual variables are feasible
    private boolean isDualFeasible() {
        // check that all edges have >= 0 reduced cost
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (reducedCost(i, j) < 0) {
                    StdOut.println("Dual variables are not feasible");
                    return false;
                }
            }
        }
        return true;
    }

    // check that primal and dual variables are complementary slack
    private boolean isComplementarySlack() {

        // check that all matched edges have 0-reduced cost
        for (int i = 0; i < N; i++) {
            if ((xy[i] != UNMATCHED) && (reducedCost(i, xy[i]) != 0)) {
                StdOut.println("Primal and dual variables are not complementary slack");
                return false;
            }
        }
        return true;
    }

    // check that primal variables are a perfect matching
    private boolean isPerfectMatching() {

        // check that xy[] is a perfect matching
        boolean[] perm = new boolean[N];
        for (int i = 0; i < N; i++) {
            if (perm[xy[i]]) {
                StdOut.println("Not a perfect matching");
                return false;
            }
            perm[xy[i]] = true;
        }

        // check that xy[] and yx[] are inverses
        for (int j = 0; j < N; j++) {
            if (xy[yx[j]] != j) {
                StdOut.println("xy[] and yx[] are not inverses");
                return false;
            }
        }
        for (int i = 0; i < N; i++) {
            if (yx[xy[i]] != i) {
                StdOut.println("xy[] and yx[] are not inverses");
                return false;
            }
        }

        return true;
    }

    // check optimality conditions
    private boolean certifySolution() {
        return isPerfectMatching() && isDualFeasible() && isComplementarySlack();
    }

    /**
     * Unit tests the <tt>AssignmentProblem</tt> data type.
     * Takes a command-line argument N; creates a random N-by-N matrix;
     * solves the N-by-N assignment problem; and prints the optimal
     * solution.
     */
    public static void main(String[] args) {

        // create random N-by-N matrix
        int N = Integer.parseInt(args[0]);
        double[][] weight = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                weight[i][j] = 100 + StdRandom.uniform(900);
            }
        }

        // solve assignment problem
        AssignmentProblem assignment = new AssignmentProblem(weight);
        StdOut.printf("weight = %.0f\n", assignment.weight());
        StdOut.println();

        // print N-by-N matrix and optimal solution
        if (N <= 20) return;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (j == assignment.sol(i))
                    StdOut.printf("*%.0f ", weight[i][j]);
                else
                    StdOut.printf(" %.0f ", weight[i][j]);
            }
            StdOut.println();
        }
    }

}

/******************************************************************************
 *  Copyright 2002-2015, Robert Sedgewick and Kevin Wayne.
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
