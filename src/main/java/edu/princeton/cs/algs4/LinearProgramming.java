/******************************************************************************
 *  Compilation:  javac LinearProgramming.java
 *  Execution:    java LinearProgramming m n
 *  Dependencies: StdOut.java
 *
 *  Given an m-by-n matrix A, an m-length vector b, and an
 *  n-length vector c, solve the  LP { max cx : Ax <= b, x >= 0 }.
 *  Assumes that b >= 0 so that x = 0 is a basic feasible solution.
 *
 *  Creates an (m+1)-by-(n+m+1) simplex tableaux with the
 *  RHS in column m+n, the objective function in row m, and
 *  slack variables in columns m through m+n-1.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The {@code LinearProgramming} class represents a data type for solving a
 *  linear program of the form { max cx : Ax &le; b, x &ge; 0 }, where A is a m-by-n
 *  matrix, b is an m-length vector, and c is an n-length vector. For simplicity,
 *  we assume that A is of full rank and that b &ge; 0 so that x = 0 is a basic
 *  feasible soution.
 *  <p>
 *  The data type supplies methods for determining the optimal primal and
 *  dual solutions.
 *  <p>
 *  This is a bare-bones implementation of the <em>simplex algorithm</em>.
 *  It uses Bland's rule to determing the entering and leaving variables.
 *  It is not suitable for use on large inputs.
 *  <p>
 *  This computes correct results if all arithmetic performed is
 *  without floating-point rounding error or arithmetic overflow.
 *  In practice, there will be floating-point rounding error
 *  and this implementation is not robust in the presence of
 *  such errors.
 *  <p>
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/65reductions">Section 6.5</a>
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class LinearProgramming {
    private static final double EPSILON = 1.0E-10;
    private double[][] a;   // tableaux
    private int m;          // number of constraints
    private int n;          // number of original variables

    private int[] basis;    // basis[i] = basic variable corresponding to row i
                            // only needed to print out solution, not book

    /**
     * Determines an optimal solution to the linear program
     * { max cx : Ax &le; b, x &ge; 0 }, where A is a m-by-n
     * matrix, b is an m-length vector, and c is an n-length vector.
     *
     * @param  A the <em>m</em>-by-<em>b</em> matrix
     * @param  b the <em>m</em>-length RHS vector
     * @param  c the <em>n</em>-length cost vector
     * @throws IllegalArgumentException unless {@code b[i] >= 0} for each {@code i}
     * @throws ArithmeticException if the linear program is unbounded
     */
    public LinearProgramming(double[][] A, double[] b, double[] c) {
        m = b.length;
        n = c.length;
        for (int i = 0; i < m; i++)
            if (!(b[i] >= 0)) throw new IllegalArgumentException("RHS must be nonnegative");

        a = new double[m+1][n+m+1];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                a[i][j] = A[i][j];
        for (int i = 0; i < m; i++)
            a[i][n+i] = 1.0;
        for (int j = 0; j < n; j++)
            a[m][j] = c[j];
        for (int i = 0; i < m; i++)
            a[i][m+n] = b[i];

        basis = new int[m];
        for (int i = 0; i < m; i++)
            basis[i] = n + i;

        solve();

        // check optimality conditions
        assert check(A, b, c);
    }

    // run simplex algorithm starting from initial BFS
    private void solve() {
        while (true) {

            // find entering column q
            int q = bland();
            if (q == -1) break;  // optimal

            // find leaving row p
            int p = minRatioRule(q);
            if (p == -1) throw new ArithmeticException("Linear program is unbounded");

            // pivot
            pivot(p, q);

            // update basis
            basis[p] = q;
        }
    }

    // lowest index of a non-basic column with a positive cost
    private int bland() {
        for (int j = 0; j < m+n; j++)
            if (a[m][j] > 0) return j;
        return -1;  // optimal
    }

   // index of a non-basic column with most positive cost
    private int dantzig() {
        int q = 0;
        for (int j = 1; j < m+n; j++)
            if (a[m][j] > a[m][q]) q = j;

        if (a[m][q] <= 0) return -1;  // optimal
        else return q;
    }

    // find row p using min ratio rule (-1 if no such row)
    // (smallest such index if there is a tie)
    private int minRatioRule(int q) {
        int p = -1;
        for (int i = 0; i < m; i++) {
            // if (a[i][q] <= 0) continue;
            if (a[i][q] <= EPSILON) continue;
            else if (p == -1) p = i;
            else if ((a[i][m+n] / a[i][q]) < (a[p][m+n] / a[p][q])) p = i;
        }
        return p;
    }

    // pivot on entry (p, q) using Gauss-Jordan elimination
    private void pivot(int p, int q) {

        // everything but row p and column q
        for (int i = 0; i <= m; i++)
            for (int j = 0; j <= m+n; j++)
                if (i != p && j != q) a[i][j] -= a[p][j] * a[i][q] / a[p][q];

        // zero out column q
        for (int i = 0; i <= m; i++)
            if (i != p) a[i][q] = 0.0;

        // scale row p
        for (int j = 0; j <= m+n; j++)
            if (j != q) a[p][j] /= a[p][q];
        a[p][q] = 1.0;
    }

    /**
     * Returns the optimal value of this linear program.
     *
     * @return the optimal value of this linear program
     *
     */
    public double value() {
        return -a[m][m+n];
    }

    /**
     * Returns the optimal primal solution to this linear program.
     *
     * @return the optimal primal solution to this linear program
     */
    public double[] primal() {
        double[] x = new double[n];
        for (int i = 0; i < m; i++)
            if (basis[i] < n) x[basis[i]] = a[i][m+n];
        return x;
    }

    /**
     * Returns the optimal dual solution to this linear program
     *
     * @return the optimal dual solution to this linear program
     */
    public double[] dual() {
        double[] y = new double[m];
        for (int i = 0; i < m; i++)
            y[i] = -a[m][n+i];
        return y;
    }


    // is the solution primal feasible?
    private boolean isPrimalFeasible(double[][] A, double[] b) {
        double[] x = primal();

        // check that x >= 0
        for (int j = 0; j < x.length; j++) {
            if (x[j] < 0.0) {
                StdOut.println("x[" + j + "] = " + x[j] + " is negative");
                return false;
            }
        }

        // check that Ax <= b
        for (int i = 0; i < m; i++) {
            double sum = 0.0;
            for (int j = 0; j < n; j++) {
                sum += A[i][j] * x[j];
            }
            if (sum > b[i] + EPSILON) {
                StdOut.println("not primal feasible");
                StdOut.println("b[" + i + "] = " + b[i] + ", sum = " + sum);
                return false;
            }
        }
        return true;
    }

    // is the solution dual feasible?
    private boolean isDualFeasible(double[][] A, double[] c) {
        double[] y = dual();

        // check that y >= 0
        for (int i = 0; i < y.length; i++) {
            if (y[i] < 0.0) {
                StdOut.println("y[" + i + "] = " + y[i] + " is negative");
                return false;
            }
        }

        // check that yA >= c
        for (int j = 0; j < n; j++) {
            double sum = 0.0;
            for (int i = 0; i < m; i++) {
                sum += A[i][j] * y[i];
            }
            if (sum < c[j] - EPSILON) {
                StdOut.println("not dual feasible");
                StdOut.println("c[" + j + "] = " + c[j] + ", sum = " + sum);
                return false;
            }
        }
        return true;
    }

    // check that optimal value = cx = yb
    private boolean isOptimal(double[] b, double[] c) {
        double[] x = primal();
        double[] y = dual();
        double value = value();

        // check that value = cx = yb
        double value1 = 0.0;
        for (int j = 0; j < x.length; j++)
            value1 += c[j] * x[j];
        double value2 = 0.0;
        for (int i = 0; i < y.length; i++)
            value2 += y[i] * b[i];
        if (Math.abs(value - value1) > EPSILON || Math.abs(value - value2) > EPSILON) {
            StdOut.println("value = " + value + ", cx = " + value1 + ", yb = " + value2);
            return false;
        }

        return true;
    }

    private boolean check(double[][]A, double[] b, double[] c) {
        return isPrimalFeasible(A, b) && isDualFeasible(A, c) && isOptimal(b, c);
    }

    // print tableaux
    private void show() {
        StdOut.println("m = " + m);
        StdOut.println("n = " + n);
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= m+n; j++) {
                StdOut.printf("%7.2f ", a[i][j]);
                // StdOut.printf("%10.7f ", a[i][j]);
            }
            StdOut.println();
        }
        StdOut.println("value = " + value());
        for (int i = 0; i < m; i++)
            if (basis[i] < n) StdOut.println("x_" + basis[i] + " = " + a[i][m+n]);
        StdOut.println();
    }


    private static void test(double[][] A, double[] b, double[] c) {
        LinearProgramming lp;
        try {
            lp = new LinearProgramming(A, b, c);
        }
        catch (ArithmeticException e) {
            System.out.println(e);
            return;
        }

        StdOut.println("value = " + lp.value());
        double[] x = lp.primal();
        for (int i = 0; i < x.length; i++)
            StdOut.println("x[" + i + "] = " + x[i]);
        double[] y = lp.dual();
        for (int j = 0; j < y.length; j++)
            StdOut.println("y[" + j + "] = " + y[j]);
    }

    private static void test1() {
        double[][] A = {
            { -1,  1,  0 },
            {  1,  4,  0 },
            {  2,  1,  0 },
            {  3, -4,  0 },
            {  0,  0,  1 },
        };
        double[] c = { 1, 1, 1 };
        double[] b = { 5, 45, 27, 24, 4 };
        test(A, b, c);
    }


    // x0 = 12, x1 = 28, opt = 800
    private static void test2() {
        double[] c = {  13.0,  23.0 };
        double[] b = { 480.0, 160.0, 1190.0 };
        double[][] A = {
            {  5.0, 15.0 },
            {  4.0,  4.0 },
            { 35.0, 20.0 },
        };
        test(A, b, c);
    }

    // unbounded
    private static void test3() {
        double[] c = { 2.0, 3.0, -1.0, -12.0 };
        double[] b = {  3.0,   2.0 };
        double[][] A = {
            { -2.0, -9.0,  1.0,  9.0 },
            {  1.0,  1.0, -1.0, -2.0 },
        };
        test(A, b, c);
    }

    // degenerate - cycles if you choose most positive objective function coefficient
    private static void test4() {
        double[] c = { 10.0, -57.0, -9.0, -24.0 };
        double[] b = {  0.0,   0.0,  1.0 };
        double[][] A = {
            { 0.5, -5.5, -2.5, 9.0 },
            { 0.5, -1.5, -0.5, 1.0 },
            { 1.0,  0.0,  0.0, 0.0 },
        };
        test(A, b, c);
    }


    /**
     * Unit tests the {@code LinearProgramming} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        StdOut.println("----- test 1 --------------------");
        test1();
        StdOut.println();

        StdOut.println("----- test 2 --------------------");
        test2();
        StdOut.println();

        StdOut.println("----- test 3 --------------------");
        test3();
        StdOut.println();

        StdOut.println("----- test 4 --------------------");
        test4();
        StdOut.println();

        StdOut.println("----- test random ---------------");
        int m = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);
        double[] c = new double[n];
        double[] b = new double[m];
        double[][] A = new double[m][n];
        for (int j = 0; j < n; j++)
            c[j] = StdRandom.uniformInt(1000);
        for (int i = 0; i < m; i++)
            b[i] = StdRandom.uniformInt(1000);
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                A[i][j] = StdRandom.uniformInt(100);
        LinearProgramming lp = new LinearProgramming(A, b, c);
        test(A, b, c);
    }

}

/******************************************************************************
 *  Copyright 2002-2022, Robert Sedgewick and Kevin Wayne.
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
