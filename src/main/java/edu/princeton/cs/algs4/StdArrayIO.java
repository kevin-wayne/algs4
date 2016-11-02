/******************************************************************************
 *  Compilation:  javac StdArrayIO.java
 *  Execution:    java StdArrayIO < input.txt
 *  Dependencies: StdOut.java
 *  Data files:    http://introcs.cs.princeton.edu/java/22library/tinyDouble1D.txt
 *                 http://introcs.cs.princeton.edu/java/22library/tinyDouble2D.txt
 *                 http://introcs.cs.princeton.edu/java/22library/tinyBoolean2D.txt
 *
 *  A library for reading in 1D and 2D arrays of integers, doubles,
 *  and booleans from standard input and printing them out to
 *  standard output.
 *
 *  % more tinyDouble1D.txt 
 *  4
 *    .000  .246  .222  -.032
 *
 *  % more tinyDouble2D.txt 
 *  4 3 
 *    .000  .270  .000 
 *    .246  .224 -.036 
 *    .222  .176  .0893 
 *   -.032  .739  .270 
 *
 *  % more tinyBoolean2D.txt 
 *  4 3 
 *    1 1 0
 *    0 0 0
 *    0 1 1
 *    1 1 1
 *
 *  % cat tinyDouble1D.txt tinyDouble2D.txt tinyBoolean2D.txt | java StdArrayIO
 *  4
 *    0.00000   0.24600   0.22200  -0.03200 
 *  
 *  4 3
 *    0.00000   0.27000   0.00000 
 *    0.24600   0.22400  -0.03600 
 *    0.22200   0.17600   0.08930 
 *    0.03200   0.73900   0.27000 
 *
 *  4 3
 *  1 1 0 
 *  0 0 0 
 *  0 1 1 
 *  1 1 1 
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;


/**
 *  <i>Standard array IO</i>. This class provides methods for reading
 *  in 1D and 2D arrays from standard input and printing out to 
 *  standard output.
 *  <p>
 *  For additional documentation, see
 *  <a href="http://introcs.cs.princeton.edu/22libary">Section 2.2</a> of
 *  <i>Computer Science: An Interdisciplinary Approach</i>
 *  by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class StdArrayIO {

    // it doesn't make sense to instantiate this class
    private StdArrayIO() { }

    /**
     * Reads a 1D array of doubles from standard input and returns it.
     *
     * @return the 1D array of doubles
     */
    public static double[] readDouble1D() {
        int n = StdIn.readInt();
        double[] a = new double[n];
        for (int i = 0; i < n; i++) {
            a[i] = StdIn.readDouble();
        }
        return a;
    }

    /**
     * Prints an array of doubles to standard output.
     *
     * @param a the 1D array of doubles
     */
    public static void print(double[] a) {
        int n = a.length;
        StdOut.println(n);
        for (int i = 0; i < n; i++) {
            StdOut.printf("%9.5f ", a[i]);
        }
        StdOut.println();
    }

        
    /**
     * Reads a 2D array of doubles from standard input and returns it.
     *
     * @return the 2D array of doubles
     */
    public static double[][] readDouble2D() {
        int m = StdIn.readInt();
        int n = StdIn.readInt();
        double[][] a = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = StdIn.readDouble();
            }
        }
        return a;
    }

    /**
     * Prints the 2D array of doubles to standard output.
     *
     * @param a the 2D array of doubles
     */
    public static void print(double[][] a) {
        int m = a.length;
        int n = a[0].length;
        StdOut.println(m + " " + n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                StdOut.printf("%9.5f ", a[i][j]);
            }
            StdOut.println();
        }
    }


    /**
     * Reads a 1D array of integers from standard input and returns it.
     *
     * @return the 1D array of integers
     */
    public static int[] readInt1D() {
        int n = StdIn.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = StdIn.readInt();
        }
        return a;
    }

    /**
     * Prints an array of integers to standard output.
     *
     * @param a the 1D array of integers
     */
    public static void print(int[] a) {
        int n = a.length;
        StdOut.println(n);
        for (int i = 0; i < n; i++) {
            StdOut.printf("%9d ", a[i]);
        }
        StdOut.println();
    }

        
    /**
     * Reads a 2D array of integers from standard input and returns it.
     *
     * @return the 2D array of integers
     */
    public static int[][] readInt2D() {
        int m = StdIn.readInt();
        int n = StdIn.readInt();
        int[][] a = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = StdIn.readInt();
            }
        }
        return a;
    }

    /**
     * Print a 2D array of integers to standard output.
     *
     * @param a the 2D array of integers
     */
    public static void print(int[][] a) {
        int m = a.length;
        int n = a[0].length;
        StdOut.println(m + " " + n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                StdOut.printf("%9d ", a[i][j]);
            }
            StdOut.println();
        }
    }


    /**
     * Reads a 1D array of booleans from standard input and returns it.
     *
     * @return the 1D array of booleans
     */
    public static boolean[] readBoolean1D() {
        int n = StdIn.readInt();
        boolean[] a = new boolean[n];
        for (int i = 0; i < n; i++) {
            a[i] = StdIn.readBoolean();
        }
        return a;
    }

    /**
     * Prints a 1D array of booleans to standard output.
     *
     * @param a the 1D array of booleans
     */
    public static void print(boolean[] a) {
        int n = a.length;
        StdOut.println(n);
        for (int i = 0; i < n; i++) {
            if (a[i]) StdOut.print("1 ");
            else      StdOut.print("0 ");
        }
        StdOut.println();
    }

    /**
     * Reads a 2D array of booleans from standard input and returns it.
     *
     * @return the 2D array of booleans
     */
    public static boolean[][] readBoolean2D() {
        int m = StdIn.readInt();
        int n = StdIn.readInt();
        boolean[][] a = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = StdIn.readBoolean();
            }
        }
        return a;
    }

   /**
     * Prints a 2D array of booleans to standard output.
     *
     * @param a the 2D array of booleans
     */
    public static void print(boolean[][] a) {
        int m = a.length;
        int n = a[0].length;
        StdOut.println(m + " " + n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (a[i][j]) StdOut.print("1 ");
                else         StdOut.print("0 ");
            }
            StdOut.println();
        }
    }


   /**
     * Unit tests {@code StdArrayIO}.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        // read and print an array of doubles
        double[] a = StdArrayIO.readDouble1D();
        StdArrayIO.print(a);
        StdOut.println();

        // read and print a matrix of doubles
        double[][] b = StdArrayIO.readDouble2D();
        StdArrayIO.print(b);
        StdOut.println();

        // read and print a matrix of doubles
        boolean[][] d = StdArrayIO.readBoolean2D();
        StdArrayIO.print(d);
        StdOut.println();
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
