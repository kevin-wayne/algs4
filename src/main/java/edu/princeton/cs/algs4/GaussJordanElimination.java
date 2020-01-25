/******************************************************************************
 *  Compilation:  javac GaussJordanElimination.java
 *  Execution:    java GaussJordanElimination n
 *  Dependencies: StdOut.java
 * 
 *  Finds a solutions to Ax = b using Gauss-Jordan elimination with partial
 *  pivoting. If no solution exists, find a solution to yA = 0, yb != 0,
 *  which serves as a certificate of infeasibility.
 *
 *  % java GaussJordanElimination
 *  -1.000000
 *  2.000000
 *  2.000000
 *
 *  3.000000
 *  -1.000000
 *  -2.000000
 * 
 *  System is infeasible
 *
 *  -6.250000
 *  -4.500000
 *  0.000000
 *  0.000000
 *  1.000000
 *
 *  System is infeasible
 *
 *  -1.375000
 *  1.625000
 *  0.000000
 *
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The {@code GaussJordanElimination} data type provides methods
 *  to solve a linear system of equations <em>Ax</em> = <em>b</em>,
 *  where <em>A</em> is an <em>n</em>-by-<em>n</em> matrix
 *  and <em>b</em> is a length <em>n</em> vector.
 *  If no solution exists, it finds a solution <em>y</em> to
 *  <em>yA</em> = 0, <em>yb</em> &ne; 0, which
 *  which serves as a certificate of infeasibility.
 *  <p>
 *  This implementation uses Gauss-Jordan elimination with partial pivoting.
 *  See {@link GaussianElimination} for an implementation that uses
 *  Gaussian elimination (but does not provide the certificate of infeasibility).
 *  For an industrial-strength numerical linear algebra library,
 *  see <a href = "http://math.nist.gov/javanumerics/jama/">JAMA</a>. 
 *  <p>
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/99scientific">Section 9.9</a>
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class GaussJordanElimination {
    private static final double EPSILON = 1e-8;

    private final int n;      // n-by-n system
    private double[][] a;     // n-by-(n+1) augmented matrix

    // Gauss-Jordan elimination with partial pivoting
    /**
     * Solves the linear system of equations <em>Ax</em> = <em>b</em>,
     * where <em>A</em> is an <em>n</em>-by-<em>n</em> matrix and <em>b</em>
     * is a length <em>n</em> vector.
     *
     * @param  A the <em>n</em>-by-<em>n</em> constraint matrix
     * @param  b the length <em>n</em> right-hand-side vector
     */
    public GaussJordanElimination(double[][] A, double[] b) {
        n = b.length;

        // build augmented matrix
        a = new double[n][n+n+1];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                a[i][j] = A[i][j];

        // only needed if you want to find certificate of infeasibility (or compute inverse)
        for (int i = 0; i < n; i++)
            a[i][n+i] = 1.0;

        for (int i = 0; i < n; i++)
            a[i][n+n] = b[i];

        solve();

        assert certifySolution(A, b);
    }

    private void solve() {

        // Gauss-Jordan elimination
        for (int p = 0; p < n; p++) {
            // show();

            // find pivot row using partial pivoting
            int max = p;
            for (int i = p+1; i < n; i++) {
                if (Math.abs(a[i][p]) > Math.abs(a[max][p])) {
                    max = i;
                }
            }

            // exchange row p with row max
            swap(p, max);

            // singular or nearly singular
            if (Math.abs(a[p][p]) <= EPSILON) {
                continue;
                // throw new ArithmeticException("Matrix is singular or nearly singular");
            }

            // pivot
            pivot(p, p);
        }
        // show();
    }

    // swap row1 and row2
    private void swap(int row1, int row2) {
        double[] temp = a[row1];
        a[row1] = a[row2];
        a[row2] = temp;
    }


    // pivot on entry (p, q) using Gauss-Jordan elimination
    private void pivot(int p, int q) {

        // everything but row p and column q
        for (int i = 0; i < n; i++) {
            double alpha = a[i][q] / a[p][q];
            for (int j = 0; j <= n+n; j++) {
                if (i != p && j != q) a[i][j] -= alpha * a[p][j];
            }
        }

        // zero out column q
        for (int i = 0; i < n; i++)
            if (i != p) a[i][q] = 0.0;

        // scale row p (ok to go from q+1 to n, but do this for consistency with simplex pivot)
        for (int j = 0; j <= n+n; j++)
            if (j != q) a[p][j] /= a[p][q];
        a[p][q] = 1.0;
    }

    /**
     * Returns a solution to the linear system of equations <em>Ax</em> = <em>b</em>.
     *      
     * @return a solution <em>x</em> to the linear system of equations
     *         <em>Ax</em> = <em>b</em>; {@code null} if no such solution
     */
    public double[] primal() {
        double[] x = new double[n];
        for (int i = 0; i < n; i++) {
            if (Math.abs(a[i][i]) > EPSILON)
                x[i] = a[i][n+n] / a[i][i];
            else if (Math.abs(a[i][n+n]) > EPSILON)
                return null;
        }
        return x;
    }

    /**
     * Returns a solution to the linear system of equations <em>yA</em> = 0,
     * <em>yb</em> &ne; 0.
     *      
     * @return a solution <em>y</em> to the linear system of equations
     *         <em>yA</em> = 0, <em>yb</em> &ne; 0; {@code null} if no such solution
     */
    public double[] dual() {
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            if ((Math.abs(a[i][i]) <= EPSILON) && (Math.abs(a[i][n+n]) > EPSILON)) {
                for (int j = 0; j < n; j++)
                    y[j] = a[i][n+j];
                return y;
            }
        }
        return null;
    }

    /**
     * Returns true if there exists a solution to the linear system of
     * equations <em>Ax</em> = <em>b</em>.
     *      
     * @return {@code true} if there exists a solution to the linear system
     *         of equations <em>Ax</em> = <em>b</em>; {@code false} otherwise
     */
    public boolean isFeasible() {
        return primal() != null;
    }

    // print the tableaux
    private void show() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                StdOut.printf("%8.3f ", a[i][j]);
            }
            StdOut.printf("| ");
            for (int j = n; j < n+n; j++) {
                StdOut.printf("%8.3f ", a[i][j]);
            }
            StdOut.printf("| %8.3f\n", a[i][n+n]);
        }
        StdOut.println();
    }


    // check that Ax = b or yA = 0, yb != 0
    private boolean certifySolution(double[][] A, double[] b) {

        // check that Ax = b
        if (isFeasible()) {
            double[] x = primal();
            for (int i = 0; i < n; i++) {
                double sum = 0.0;
                for (int j = 0; j < n; j++) {
                    sum += A[i][j] * x[j];
                }
                if (Math.abs(sum - b[i]) > EPSILON) {
                    StdOut.println("not feasible");
                    StdOut.printf("b[%d] = %8.3f, sum = %8.3f\n", i, b[i], sum);
                    return false;
                }
            }
            return true;
        }

        // or that yA = 0, yb != 0
        else {
            double[] y = dual();
            for (int j = 0; j < n; j++) {
                double sum = 0.0;
                for (int i = 0; i < n; i++) {
                    sum += A[i][j] * y[i];
                }
                if (Math.abs(sum) > EPSILON) {
                    StdOut.println("invalid certificate of infeasibility");
                    StdOut.printf("sum = %8.3f\n", sum);
                    return false;
                }
            }
            double sum = 0.0;
            for (int i = 0; i < n; i++) {
                sum += y[i] * b[i];
            }
            if (Math.abs(sum) < EPSILON) {
                StdOut.println("invalid certificate of infeasibility");
                StdOut.printf("yb  = %8.3f\n", sum);
                return false;
            }
            return true;
        }
    }


    private static void test(String name, double[][] A, double[] b) {
        StdOut.println("----------------------------------------------------");
        StdOut.println(name);
        StdOut.println("----------------------------------------------------");
        GaussJordanElimination gaussian = new GaussJordanElimination(A, b);
        if (gaussian.isFeasible()) {
            StdOut.println("Solution to Ax = b");
            double[] x = gaussian.primal();
            for (int i = 0; i < x.length; i++) {
                StdOut.printf("%10.6f\n", x[i]);
            }
        }
        else {
            StdOut.println("Certificate of infeasibility");
            double[] y = gaussian.dual();
            for (int j = 0; j < y.length; j++) {
                StdOut.printf("%10.6f\n", y[j]);
            }
        }
        StdOut.println();
        StdOut.println();
    }


    // 3-by-3 nonsingular system
    private static void test1() {
        double[][] A = {
            { 0, 1,  1 },
            { 2, 4, -2 },
            { 0, 3, 15 }
        };
        double[] b = { 4, 2, 36 };
        test("test 1", A, b);
    }

    // 3-by-3 nonsingular system
    private static void test2() {
        double[][] A = {
            {  1, -3,   1 },
            {  2, -8,   8 },
            { -6,  3, -15 }
        };
        double[] b = { 4, -2, 9 };
        test("test 2", A, b);
    }

    // 5-by-5 singular: no solutions
    // y = [ -1, 0, 1, 1, 0 ]
    private static void test3() {
        double[][] A = {
            {  2, -3, -1,  2,  3 },
            {  4, -4, -1,  4, 11 },
            {  2, -5, -2,  2, -1 },
            {  0,  2,  1,  0,  4 },
            { -4,  6,  0,  0,  7 },
        };
        double[] b = { 4, 4, 9, -6, 5 };
        test("test 3", A, b);
    }

    // 5-by-5 singluar: infinitely many solutions
    private static void test4() {
        double[][] A = {
            {  2, -3, -1,  2,  3 },
            {  4, -4, -1,  4, 11 },
            {  2, -5, -2,  2, -1 },
            {  0,  2,  1,  0,  4 },
            { -4,  6,  0,  0,  7 },
        };
        double[] b = { 4, 4, 9, -5, 5 };
        test("test 4", A, b);
    }

    // 3-by-3 singular: no solutions
    // y = [ 1, 0, 1/3 ]
    private static void test5() {
        double[][] A = {
            {  2, -1,  1 },
            {  3,  2, -4 },
            { -6,  3, -3 },
        };
        double[] b = { 1, 4, 2 };
        test("test 5", A, b);
    }

    // 3-by-3 singular: infinitely many solutions
    private static void test6() {
        double[][] A = {
            {  1, -1,  2 },
            {  4,  4, -2 },
            { -2,  2, -4 },
        };
        double[] b = { -3, 1, 6 };
        test("test 6 (infinitely many solutions)", A, b);
    }

    /**
     * Unit tests the {@code GaussJordanElimination} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        test1();
        test2();
        test3();
        test4();
        test5();
        test6();

        // n-by-n random system (likely full rank)
        int n = Integer.parseInt(args[0]);
        double[][] A = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                A[i][j] = StdRandom.uniform(1000);
        double[] b = new double[n];
        for (int i = 0; i < n; i++)
            b[i] = StdRandom.uniform(1000);
        test("random " + n + "-by-" + n + " (likely full rank)", A, b);


        // n-by-n random system (likely infeasible)
        A = new double[n][n];
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n; j++)
                A[i][j] = StdRandom.uniform(1000);
        for (int i = 0; i < n-1; i++) {
            double alpha = StdRandom.uniform(11) - 5.0;
            for (int j = 0; j < n; j++) {
                A[n-1][j] += alpha * A[i][j];
            }
        }
        b = new double[n];
        for (int i = 0; i < n; i++)
            b[i] = StdRandom.uniform(1000);
        test("random " + n + "-by-" + n + " (likely infeasible)", A, b);
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
