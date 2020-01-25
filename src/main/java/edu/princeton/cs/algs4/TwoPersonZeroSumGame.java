/******************************************************************************
 *  Compilation:  javac TwoPersonZeroSumGame.java
 *  Execution:    java TwoPersonZeroSumGame m n
 *  Dependencies: LinearProgramming.java StdOut.java
 *
 *  Solve an m-by-n two-person zero-sum game by reducing it to
 *  linear programming. Assuming A is a strictly positive payoff
 *  matrix, the optimal row and column player strategies are x* an y*,
 *  scaled to be probability distributions.
 *
 *  (P)  max  y^T 1         (D)  min   1^T x
 *       s.t  A^T y <= 1         s.t   A x >= 1
 *                y >= 0                 x >= 0
 *
 *  Row player is x, column player is y.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The {@code TwoPersonZeroSumGame} class represents a data type for
 *  computing optimal row and column strategies to two-person zero-sum games.
 *  <p>
 *  This implementation solves an <em>m</em>-by-<em>n</em> two-person
 *  zero-sum game by reducing it to a linear programming problem.
 *  Assuming the payoff matrix <em>A</em> is strictly positive, the
 *  optimal row and column player strategies x* and y* are obtained
 *  by solving the following primal and dual pair of linear programs,
 *  scaling the results to be probability distributions.
 *  <blockquote><pre>
 *  (P)  max  y^T 1           (D)  min   1^T x
 *       s.t  A^T y &le; 1         s.t   A x &ge; 1
 *                y &le; 0                 x &ge; 0
 *  </pre></blockquote>
 *  <p>
 *  If the payoff matrix <em>A</em> has any negative entries, we add
 *  the same constant to every entry so that every entry is positive.
 *  This increases the value of the game by that constant, but does not
 *  change solutions to the two-person zero-sum game.
 *  <p>
 *  This implementation is not suitable for large inputs, as it calls
 *  a bare-bones linear programming solver that is neither fast nor
 *  robust with respect to floating-point roundoff error.
 *  <p>
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/65reductions">Section 6.5</a>
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class TwoPersonZeroSumGame {
    private static final double EPSILON = 1E-8;

    private final int m;            // number of rows
    private final int n;            // number of columns
    private LinearProgramming lp;   // linear program solver
    private double constant;        // constant added to each entry in payoff matrix
                                    // (0 if all entries are strictly positive)
 
    /**
     * Determines an optimal solution to the two-sum zero-sum game
     * with the specified payoff matrix.
     *
     * @param  payoff the <em>m</em>-by-<em>n</em> payoff matrix
     */ 
    public TwoPersonZeroSumGame(double[][] payoff) {
        m = payoff.length;
        n = payoff[0].length;

        double[] c = new double[n];
        double[] b = new double[m];
        double[][] A = new double[m][n];
        for (int i = 0; i < m; i++)
            b[i] = 1.0;
        for (int j = 0; j < n; j++)
            c[j] = 1.0;

        // find smallest entry
        constant = Double.POSITIVE_INFINITY;
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                if (payoff[i][j] < constant)
                    constant = payoff[i][j];

        // add constant  to every entry to make strictly positive
        if (constant <= 0) constant = -constant + 1;
        else               constant = 0;
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                A[i][j] = payoff[i][j] + constant;

        lp = new LinearProgramming(A, b, c);

        assert certifySolution(payoff);
    }


    /**
     * Returns the optimal value of this two-person zero-sum game.
     *
     * @return the optimal value of this two-person zero-sum game
     *
     */
    public double value() {
        return 1.0 / scale() - constant;
    }


    // sum of x[j]
    private double scale() {
        double[] x = lp.primal();
        double sum = 0.0;
        for (int j = 0; j < n; j++)
            sum += x[j];
        return sum;
    }

    /**
     * Returns the optimal row strategy of this two-person zero-sum game.
     *
     * @return the optimal row strategy <em>x</em> of this two-person zero-sum game
     */
    public double[] row() {
        double scale = scale();
        double[] x = lp.primal();
        for (int j = 0; j < n; j++)
            x[j] /= scale;
        return x;
    }

    /**
     * Returns the optimal column strategy of this two-person zero-sum game.
     *
     * @return the optimal column strategy <em>y</em> of this two-person zero-sum game
     */
    public double[] column() {
        double scale = scale();
        double[] y = lp.dual();
        for (int i = 0; i < m; i++)
            y[i] /= scale;
        return y;
    }


    /**************************************************************************
     *
     *  The code below is solely for testing correctness of the data type.
     *
     **************************************************************************/

    // is the row vector x primal feasible?
    private boolean isPrimalFeasible() {
        double[] x = row();
        double sum = 0.0;
        for (int j = 0; j < n; j++) {
            if (x[j] < 0) {
                StdOut.println("row vector not a probability distribution");
                StdOut.printf("    x[%d] = %f\n", j, x[j]);
                return false;
            }
            sum += x[j];
        }
        if (Math.abs(sum - 1.0) > EPSILON) {
            StdOut.println("row vector x[] is not a probability distribution");
            StdOut.println("    sum = " + sum);
            return false;
        }
        return true;
    }

    // is the column vector y dual feasible?
    private boolean isDualFeasible() {
        double[] y = column();
        double sum = 0.0;
        for (int i = 0; i < m; i++) {
            if (y[i] < 0) {
                StdOut.println("column vector y[] is not a probability distribution");
                StdOut.printf("    y[%d] = %f\n", i, y[i]);
                return false;
            }
            sum += y[i];
        }
        if (Math.abs(sum - 1.0) > EPSILON) {
            StdOut.println("column vector not a probability distribution");
            StdOut.println("    sum = " + sum);
            return false;
        }
        return true;
    }

    // is the solution a Nash equilibrium?
    private boolean isNashEquilibrium(double[][] payoff) {
        double[] x = row();
        double[] y = column();
        double value = value();

        // given row player's mixed strategy, find column player's best pure strategy
        double opt1 = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < m; i++) {
            double sum = 0.0;
            for (int j = 0; j < n; j++) {
                sum += payoff[i][j] * x[j];
            }
            if (sum > opt1) opt1 = sum;
        }
        if (Math.abs(opt1 - value) > EPSILON) {
            StdOut.println("Optimal value = " + value);
            StdOut.println("Optimal best response for column player = " + opt1);
            return false;
        }

        // given column player's mixed strategy, find row player's best pure strategy
        double opt2 = Double.POSITIVE_INFINITY;
        for (int j = 0; j < n; j++) {
            double sum = 0.0;
            for (int i = 0; i < m; i++) {
                sum += payoff[i][j] * y[i];
            }
            if (sum < opt2) opt2 = sum;
        }
        if (Math.abs(opt2 - value) > EPSILON) {
            StdOut.println("Optimal value = " + value);
            StdOut.println("Optimal best response for row player = " + opt2);
            return false;
        }


        return true;
    }

    private boolean certifySolution(double[][] payoff) {
        return isPrimalFeasible() && isDualFeasible() && isNashEquilibrium(payoff);
    }


    private static void test(String description, double[][] payoff) {
        StdOut.println();
        StdOut.println(description);
        StdOut.println("------------------------------------");
        int m = payoff.length;
        int n = payoff[0].length;
        TwoPersonZeroSumGame zerosum = new TwoPersonZeroSumGame(payoff);
        double[] x = zerosum.row();
        double[] y = zerosum.column();

        StdOut.print("x[] = [");
        for (int j = 0; j < n-1; j++)
            StdOut.printf("%8.4f, ", x[j]);
        StdOut.printf("%8.4f]\n", x[n-1]);

        StdOut.print("y[] = [");
        for (int i = 0; i < m-1; i++)
            StdOut.printf("%8.4f, ", y[i]);
        StdOut.printf("%8.4f]\n", y[m-1]);
        StdOut.println("value =  " + zerosum.value());
        
    }

    // row = { 4/7, 3/7 }, column = { 0, 4/7, 3/7 }, value = 20/7
    // http://en.wikipedia.org/wiki/Zero-sum
    private static void test1() {
        double[][] payoff = {
            { 30, -10,  20 },
            { 10,  20, -20 }
        };
        test("wikipedia", payoff);
    }

    // skew-symmetric => value = 0
    // Linear Programming by Chvatal, p. 230
    private static void test2() {
        double[][] payoff = {
            {  0,  2, -3,  0 },
            { -2,  0,  0,  3 },
            {  3,  0,  0, -4 },
            {  0, -3,  4,  0 }
        };
        test("Chvatal, p. 230", payoff);
    }

    // Linear Programming by Chvatal, p. 234
    // row    = { 0, 56/99, 40/99, 0, 0, 2/99, 0, 1/99 }
    // column = { 28/99, 30/99, 21/99, 20/99 }
    // value  = 4/99
    private static void test3() {
        double[][] payoff = {
            {  0,  2, -3,  0 },
            { -2,  0,  0,  3 },
            {  3,  0,  0, -4 },
            {  0, -3,  4,  0 },
            {  0,  0, -3,  3 },
            { -2,  2,  0,  0 },
            {  3, -3,  0,  0 },
            {  0,  0,  4, -4 }
        };
        test("Chvatal, p. 234", payoff);
    }

    // Linear Programming by Chvatal, p. 236
    // row    = { 0, 2/5, 7/15, 0, 2/15, 0, 0, 0 }
    // column = { 2/3, 0, 0, 1/3 }
    // value  = -1/3
    private static void test4() {
        double[][] payoff = {
            {  0,  2, -1, -1 },
            {  0,  1, -2, -1 },
            { -1, -1,  1,  1 },
            { -1,  0,  0,  1 },
            {  1, -2,  0, -3 },
            {  1, -1, -1, -3 },
            {  0, -3,  2, -1 },
            {  0, -2,  1, -1 },
        };
        test("Chvatal p. 236", payoff);
    }

    // rock, paper, scissors
    // row    = { 1/3, 1/3, 1/3 }
    // column = { 1/3, 1/3, 1/3 }
    private static void test5() {
        double[][] payoff = {
            {  0, -1,  1 },
            {  1,  0, -1 },
            { -1,  1,  0 }
        };
        test("rock, paper, scisssors", payoff);
    }


    /**
     * Unit tests the {@code ZeroSumGameToLP} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
        test5();

        int m = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);
        double[][] payoff = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                payoff[i][j] = StdRandom.uniform(-0.5, 0.5);
        test("random " + m + "-by-" + n, payoff);
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
