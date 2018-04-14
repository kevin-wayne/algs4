/******************************************************************************
 *  Compilation:  javac GaussianElimination.java
 *  Execution:    java GaussianElimination m n
 *  Dependencies: StdOut.java
 * 
 *  Gaussian elimination with partial pivoting for m-by-n system.
 *
 *  % java GaussianElimination m n
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
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The {@code GaussianElimination} data type provides methods
 *  to solve a linear system of equations <em>Ax</em> = <em>b</em>,
 *  where <em>A</em> is an <em>m</em>-by-<em>n</em> matrix
 *  and <em>b</em> is a length <em>n</em> vector.
 *  <p>
 *  This is a bare-bones implementation that uses Gaussian elimination
 *  with partial pivoting.
 *  See <a href = "https://algs4.cs.princeton.edu/99scientific/GaussianEliminationLite.java.html">GaussianEliminationLite.java</a>
 *  for a stripped-down version that assumes the matrix <em>A</em> is square
 *  and nonsingular. See {@link GaussJordanElimination} for an alternate
 *  implementation that uses Gauss-Jordan elimination.
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
public class GaussianElimination {
    private static final double EPSILON = 1e-8;

    private final int m;      // number of rows
    private final int n;      // number of columns
    private double[][] a;     // m-by-(n+1) augmented matrix

    /**
     * Solves the linear system of equations <em>Ax</em> = <em>b</em>,
     * where <em>A</em> is an <em>m</em>-by-<em>n</em> matrix and <em>b</em>
     * is a length <em>m</em> vector.
     *
     * @param  A the <em>m</em>-by-<em>n</em> constraint matrix
     * @param  b the length <em>m</em> right-hand-side vector
     * @throws IllegalArgumentException if the dimensions disagree, i.e.,
     *         the length of {@code b} does not equal {@code m}
     */
    public GaussianElimination(double[][] A, double[] b) {
        m = A.length;
        n = A[0].length;

        if (b.length != m) throw new IllegalArgumentException("Dimensions disagree");

        // build augmented matrix
        a = new double[m][n+1];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                a[i][j] = A[i][j];
        for (int i = 0; i < m; i++)
            a[i][n] = b[i];

        forwardElimination();

        assert certifySolution(A, b);
    }

    // forward elimination
    private void forwardElimination() {
        for (int p = 0; p < Math.min(m, n); p++) {

            // find pivot row using partial pivoting
            int max = p;
            for (int i = p+1; i < m; i++) {
                if (Math.abs(a[i][p]) > Math.abs(a[max][p])) {
                    max = i;
                }
            }

            // swap
            swap(p, max);

            // singular or nearly singular
            if (Math.abs(a[p][p]) <= EPSILON) {
                continue;
            }

            // pivot
            pivot(p);
        }
    }

    // swap row1 and row2
    private void swap(int row1, int row2) {
        double[] temp = a[row1];
        a[row1] = a[row2];
        a[row2] = temp;
    }

    // pivot on a[p][p]
    private void pivot(int p) {
        for (int i = p+1; i < m; i++) {
            double alpha = a[i][p] / a[p][p];
            for (int j = p; j <= n; j++) {
                a[i][j] -= alpha * a[p][j];
            }
        }
    }

    /**
     * Returns a solution to the linear system of equations <em>Ax</em> = <em>b</em>.
     *      
     * @return a solution <em>x</em> to the linear system of equations
     *         <em>Ax</em> = <em>b</em>; {@code null} if no such solution
     */
    public double[] primal() {
        // back substitution
        double[] x = new double[n];
        for (int i = Math.min(n-1, m-1); i >= 0; i--) {
            double sum = 0.0;
            for (int j = i+1; j < n; j++) {
                sum += a[i][j] * x[j];
            }

            if (Math.abs(a[i][i]) > EPSILON)
                x[i] = (a[i][n] - sum) / a[i][i];
            else if (Math.abs(a[i][n] - sum) > EPSILON)
                return null;
        }

        // redundant rows
        for (int i = n; i < m; i++) {
            double sum = 0.0;
            for (int j = 0; j < n; j++) {
                sum += a[i][j] * x[j];
            }
            if (Math.abs(a[i][n] - sum) > EPSILON)
                return null;
        }
        return x;
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


    // check that Ax = b
    private boolean certifySolution(double[][] A, double[] b) {
        if (!isFeasible()) return true;
        double[] x = primal();
        for (int i = 0; i < m; i++) {
            double sum = 0.0;
            for (int j = 0; j < n; j++) {
                sum += A[i][j] * x[j];
            }
            if (Math.abs(sum - b[i]) > EPSILON) {
                StdOut.println("not feasible");
                StdOut.println("b[" + i + "] = " + b[i] + ", sum = " + sum);
                return false;
            }
        }
        return true;
    }


    /**
     * Unit tests the {@code GaussianElimination} data type.
     */
    private static void test(String name, double[][] A, double[] b) {
        StdOut.println("----------------------------------------------------");
        StdOut.println(name);
        StdOut.println("----------------------------------------------------");
        GaussianElimination gaussian = new GaussianElimination(A, b);
        double[] x = gaussian.primal();
        if (gaussian.isFeasible()) {
            for (int i = 0; i < x.length; i++) {
                StdOut.printf("%.6f\n", x[i]);
            }
        }
        else {
            StdOut.println("System is infeasible");
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
        test("test 1 (3-by-3 system, nonsingular)", A, b);
    }

    // 3-by-3 nonsingular system
    private static void test2() {
        double[][] A = {
            {  1, -3,   1 },
            {  2, -8,   8 },
            { -6,  3, -15 }
        };
        double[] b = { 4, -2, 9 };
        test("test 2 (3-by-3 system, nonsingular)", A, b);
    }

    // 5-by-5 singular: no solutions
    private static void test3() {
        double[][] A = {
            {  2, -3, -1,  2,  3 },
            {  4, -4, -1,  4, 11 },
            {  2, -5, -2,  2, -1 },
            {  0,  2,  1,  0,  4 },
            { -4,  6,  0,  0,  7 },
        };
        double[] b = { 4, 4, 9, -6, 5 };
        test("test 3 (5-by-5 system, no solutions)", A, b);
    }

    // 5-by-5 singular: infinitely many solutions
    private static void test4() {
        double[][] A = {
            {  2, -3, -1,  2,  3 },
            {  4, -4, -1,  4, 11 },
            {  2, -5, -2,  2, -1 },
            {  0,  2,  1,  0,  4 },
            { -4,  6,  0,  0,  7 },
        };
        double[] b = { 4, 4, 9, -5, 5 };
        test("test 4 (5-by-5 system, infinitely many solutions)", A, b);
    }

    // 3-by-3 singular: no solutions
    private static void test5() {
        double[][] A = {
            {  2, -1,  1 },
            {  3,  2, -4 },
            { -6,  3, -3 },
        };
        double[] b = { 1, 4, 2 };
        test("test 5 (3-by-3 system, no solutions)", A, b);
    }

    // 3-by-3 singular: infinitely many solutions
    private static void test6() {
        double[][] A = {
            {  1, -1,  2 },
            {  4,  4, -2 },
            { -2,  2, -4 },
        };
        double[] b = { -3, 1, 6 };
        test("test 6 (3-by-3 system, infinitely many solutions)", A, b);
    }

    // 4-by-3 full rank and feasible system
    private static void test7() {
        double[][] A = {
            { 0, 1,  1 },
            { 2, 4, -2 },
            { 0, 3, 15 },
            { 2, 8, 14 }
        };
        double[] b = { 4, 2, 36, 42 };
        test("test 7 (4-by-3 system, full rank)", A, b);
    }

    // 4-by-3 full rank and infeasible system
    private static void test8() {
        double[][] A = {
            { 0, 1,  1 },
            { 2, 4, -2 },
            { 0, 3, 15 },
            { 2, 8, 14 }
        };
        double[] b = { 4, 2, 36, 40 };
        test("test 8 (4-by-3 system, no solution)", A, b);
    }

    // 3-by-4 full rank system
    private static void test9() {
        double[][] A = {
            {  1, -3,   1,  1 },
            {  2, -8,   8,  2 },
            { -6,  3, -15,  3 }
        };
        double[] b = { 4, -2, 9 };
        test("test 9 (3-by-4 system, full rank)", A, b);
    }

    /**
     * Unit tests the {@code GaussianElimination} data type.
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
        test7();
        test8();
        test9();

        // n-by-n random system
        int n = Integer.parseInt(args[0]);
        double[][] A = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                A[i][j] = StdRandom.uniform(1000);
        double[] b = new double[n];
        for (int i = 0; i < n; i++)
            b[i] = StdRandom.uniform(1000);

        test(n + "-by-" + n + " (probably nonsingular)", A, b);
    }

}

/******************************************************************************
 *  Copyright 2002-2018, Robert Sedgewick and Kevin Wayne.
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
