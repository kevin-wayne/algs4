/******************************************************************************
 *  Compilation:  javac StdStats.java
 *  Execution:    java StdStats < input.txt
 *  Dependencies: StdOut.java
 *
 *  Library of statistical functions.
 *
 *  The test client reads an array of real numbers from standard
 *  input, and computes the minimum, mean, maximum, and
 *  standard deviation.
 *
 *  The functions all throw a NullPointerException if the array
 *  passed in is null.
 *
 *  The floating-point functions all return NaN if any input is NaN.
 *
 *  Unlike Math.min() and Math.max(), the min() and max() functions
 *  do not differentiate between -0.0 and 0.0.
 *
 *  % more tiny.txt
 *  5
 *  3.0 1.0 2.0 5.0 4.0
 *
 *  % java StdStats < tiny.txt
 *         min   1.000
 *        mean   3.000
 *         max   5.000
 *     std dev   1.581
 *
 *  Should these funtions use varargs instead of array arguments?
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  <i>Standard statistics</i>. This class provides methods for computing
 *  statistics such as min, max, mean, sample standard deviation, and
 *  sample variance.
 *  <p>
 *  For additional documentation, see
 *  <a href="http://introcs.cs.princeton.edu/22library">Section 2.2</a> of
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i>
 *  by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public final class StdStats {

    private StdStats() { }

    /**
     * Returns the maximum value in the array.
     *
     * @param  a the array
     * @return the maximum value in the array <tt>a[]</tt>,
     *         <tt>Double.NEGATIVE_INFINITY</tt> if no such value
     */
    public static double max(double[] a) {
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < a.length; i++) {
            if (Double.isNaN(a[i])) return Double.NaN;
            if (a[i] > max) max = a[i];
        }
        return max;
    }

    /**
     * Returns the maximum value in the subarray.
     *
     * @param  a the array
     * @param  lo the left endpoint of the subarray (inclusive)
     * @param  hi the right endpoint of the subarray (inclusive)
     * @return the maximum value in the subarray <tt>a[lo..hi]</tt>,
     *         <tt>Double.NEGATIVE_INFINITY</tt> if no such value
     */
    public static double max(double[] a, int lo, int hi) {
        if (lo < 0 || hi >= a.length || lo > hi)
            throw new IndexOutOfBoundsException("Subarray indices out of bounds");
        double max = Double.NEGATIVE_INFINITY;
        for (int i = lo; i <= hi; i++) {
            if (Double.isNaN(a[i])) return Double.NaN;
            if (a[i] > max) max = a[i];
        }
        return max;
    }

    /**
     * Returns the maximum value in the array.
     *
     * @param  a the array
     * @return the maximum value in the array <tt>a[]</tt>,
     *         <tt>Integer.MIN_VALUE</tt> if no such value
     */
    public static int max(int[] a) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < a.length; i++) {
            if (a[i] > max) max = a[i];
        }
        return max;
    }

    /**
     * Returns the minimum value in the array.
     *
     * @param  a the array
     * @return the minimum value in the array <tt>a[]</tt>,
     *         <tt>Double.POSITIVE_INFINITY</tt> if no such value
     */
    public static double min(double[] a) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < a.length; i++) {
            if (Double.isNaN(a[i])) return Double.NaN;
            if (a[i] < min) min = a[i];
        }
        return min;
    }

    /**
     * Returns the minimum value in the subarray.
     *
     * @param  a the array
     * @param  lo the left endpoint of the subarray (inclusive)
     * @param  hi the right endpoint of the subarray (inclusive)
     * @return the maximum value in the subarray <tt>a[lo..hi]</tt>,
     *         <tt>Double.POSITIVE_INFINITY</tt> if no such value
     */
    public static double min(double[] a, int lo, int hi) {
        if (lo < 0 || hi >= a.length || lo > hi)
            throw new IndexOutOfBoundsException("Subarray indices out of bounds");
        double min = Double.POSITIVE_INFINITY;
        for (int i = lo; i <= hi; i++) {
            if (Double.isNaN(a[i])) return Double.NaN;
            if (a[i] < min) min = a[i];
        }
        return min;
    }

    /**
     * Returns the minimum value in the array.
     *
     * @param  a the array
     * @return the minimum value in the array <tt>a[]</tt>,
     *         <tt>Integer.MAX_VALUE</tt> if no such value
     */
    public static int min(int[] a) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < a.length; i++) {
            if (a[i] < min) min = a[i];
        }
        return min;
    }

    /**
     * Returns the average value in the array.
     *
     * @param  a the array
     * @return the average value in the array <tt>a[]</tt>,
     *         <tt>Double.NaN</tt> if no such value
     */
    public static double mean(double[] a) {
        if (a.length == 0) return Double.NaN;
        double sum = sum(a);
        return sum / a.length;
    }

    /**
     * Returns the average value in the subarray.
     *
     * @param a the array
     * @param lo the left endpoint of the subarray (inclusive)
     * @param hi the right endpoint of the subarray (inclusive)
     * @return the average value in the subarray <tt>a[lo..hi]</tt>,
     *         <tt>Double.NaN</tt> if no such value
     */
    public static double mean(double[] a, int lo, int hi) {
        int length = hi - lo + 1;
        if (lo < 0 || hi >= a.length || lo > hi)
            throw new IndexOutOfBoundsException("Subarray indices out of bounds");
        if (length == 0) return Double.NaN;
        double sum = sum(a, lo, hi);
        return sum / length;
    }

    /**
     * Returns the average value in the array.
     *
     * @param  a the array
     * @return the average value in the array <tt>a[]</tt>,
     *         <tt>Double.NaN</tt> if no such value
     */
    public static double mean(int[] a) {
        if (a.length == 0) return Double.NaN;
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum = sum + a[i];
        }
        return sum / a.length;
    }

    /**
     * Returns the sample variance in the array.
     *
     * @param  a the array
     * @return the sample variance in the array <tt>a[]</tt>,
     *         <tt>Double.NaN</tt> if no such value
     */
    public static double var(double[] a) {
        if (a.length == 0) return Double.NaN;
        double avg = mean(a);
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / (a.length - 1);
    }

    /**
     * Returns the sample variance in the subarray.
     *
     * @param  a the array
     * @param lo the left endpoint of the subarray (inclusive)
     * @param hi the right endpoint of the subarray (inclusive)
     * @return the sample variance in the subarray <tt>a[lo..hi]</tt>,
     *         <tt>Double.NaN</tt> if no such value
     */
    public static double var(double[] a, int lo, int hi) {
        int length = hi - lo + 1;
        if (lo < 0 || hi >= a.length || lo > hi)
            throw new IndexOutOfBoundsException("Subarray indices out of bounds");
        if (length == 0) return Double.NaN;
        double avg = mean(a, lo, hi);
        double sum = 0.0;
        for (int i = lo; i <= hi; i++) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / (length - 1);
    }

    /**
     * Returns the sample variance in the array.
     *
     * @param  a the array
     * @return the sample variance in the array <tt>a[]</tt>,
     *         <tt>Double.NaN</tt> if no such value
     */
    public static double var(int[] a) {
        if (a.length == 0) return Double.NaN;
        double avg = mean(a);
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / (a.length - 1);
    }

    /**
     * Returns the population variance in the array.
     *
     * @param  a the array
     * @return the population variance in the array <tt>a[]</tt>,
     *         <tt>Double.NaN</tt> if no such value
     */
    public static double varp(double[] a) {
        if (a.length == 0) return Double.NaN;
        double avg = mean(a);
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / a.length;
    }

    /**
     * Returns the population variance in the subarray.
     *
     * @param  a the array
     * @param lo the left endpoint of the subarray (inclusive)
     * @param hi the right endpoint of the subarray (inclusive)
     * @return the population variance in the subarray <tt>a[lo..hi]</tt>,
     *         <tt>Double.NaN</tt> if no such value
     */
    public static double varp(double[] a, int lo, int hi) {
        int length = hi - lo + 1;
        if (lo < 0 || hi >= a.length || lo > hi)
            throw new IndexOutOfBoundsException("Subarray indices out of bounds");
        if (length == 0) return Double.NaN;
        double avg = mean(a, lo, hi);
        double sum = 0.0;
        for (int i = lo; i <= hi; i++) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / length;
    }

    /**
     * Returns the sample standard deviation in the array.
     *
     * @param  a the array
     * @return the sample standard deviation in the array <tt>a[]</tt>,
     *         <tt>Double.NaN</tt> if no such value
     */
    public static double stddev(double[] a) {
        return Math.sqrt(var(a));
    }

    /**
     * Returns the sample standard deviation in the array.
     *
     * @param  a the array
     * @return the sample standard deviation in the array <tt>a[]</tt>,
     *         <tt>Double.NaN</tt> if no such value
     */
    public static double stddev(int[] a) {
        return Math.sqrt(var(a));
    }

    /**
     * Returns the sample standard deviation in the subarray.
     *
     * @param  a the array
     * @param lo the left endpoint of the subarray (inclusive)
     * @param hi the right endpoint of the subarray (inclusive)
     * @return the sample standard deviation in the subarray <tt>a[lo..hi]</tt>,
     *         <tt>Double.NaN</tt> if no such value
     */
    public static double stddev(double[] a, int lo, int hi) {
        return Math.sqrt(var(a, lo, hi));
    }


    /**
     * Returns the population standard deviation in the array.
     *
     * @param  a the array
     * @return the population standard deviation in the subarray <tt>a[lo]</tt>,
     *         <tt>Double.NaN</tt> if no such value
     */
    public static double stddevp(double[] a) {
        return Math.sqrt(varp(a));
    }

    /**
     * Returns the population standard deviation in the subarray.
     *
     * @param  a the array
     * @param lo the left endpoint of the subarray (inclusive)
     * @param hi the right endpoint of the subarray (inclusive)
     * @return the population standard deviation in the subarray <tt>a[lo..hi]</tt>,
     *         <tt>Double.NaN</tt> if no such value
     */
    public static double stddevp(double[] a, int lo, int hi) {
        return Math.sqrt(varp(a, lo, hi));
    }

    /**
     * Returns the sum of all values in the array.
     *
     * @param  a the array
     * @return the sum of all values in the array <tt>a[]</tt>,
     *         <tt>0.0</tt> if no such value
     */
    public static double sum(double[] a) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
        }
        return sum;
    }

    /**
     * Returns the sum of all values in the subarray.
     *
     * @param  a the array
     * @param lo the left endpoint of the subarray (inclusive)
     * @param hi the right endpoint of the subarray (inclusive)
     * @return the sum of all values in the subarray <tt>a[lo..hi]</tt>,
     *         <tt>0.0</tt> if no such value
     */
    public static double sum(double[] a, int lo, int hi) {
        if (lo < 0 || hi >= a.length || lo > hi)
            throw new IndexOutOfBoundsException("Subarray indices out of bounds");
        double sum = 0.0;
        for (int i = lo; i <= hi; i++) {
            sum += a[i];
        }
        return sum;
    }

    /**
     * Returns the sum of all values in the array.
     *
     * @param  a the array
     * @return the sum of all values in the array <tt>a[]</tt>,
     *         <tt>0.0</tt> if no such value
     */
    public static int sum(int[] a) {
        int sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
        }
        return sum;
    }

   /**
     * Plots the points (i, a[i]) to standard draw.
     *
     * @param a the array of values
     */
    public static void plotPoints(double[] a) {
        int N = a.length;
        StdDraw.setXscale(-1, N);
        StdDraw.setPenRadius(1.0 / (3.0 * N));
        for (int i = 0; i < N; i++) {
            StdDraw.point(i, a[i]);
        }
    }

   /**
     * Plots the line segments connecting points (i, a[i]) to standard draw.
     *
     * @param a the array of values
     */
    public static void plotLines(double[] a) {
        int N = a.length;
        StdDraw.setXscale(-1, N);
        StdDraw.setPenRadius();
        for (int i = 1; i < N; i++) {
            StdDraw.line(i-1, a[i-1], i, a[i]);
        }
    }

   /**
     * Plots the bars from (0, a[i]) to (i, a[i]) to standard draw.
     *
     * @param a the array of values
     */
    public static void plotBars(double[] a) {
        int N = a.length;
        StdDraw.setXscale(-1, N);
        for (int i = 0; i < N; i++) {
            StdDraw.filledRectangle(i, a[i]/2, .25, a[i]/2);
        }
    }


   /**
     * Unit tests <tt>StdStats</tt>.
     * Convert command-line arguments to array of doubles and call various methods.
     */
    public static void main(String[] args) {
        double[] a = StdArrayIO.readDouble1D();
        StdOut.printf("       min %10.3f\n", min(a));
        StdOut.printf("      mean %10.3f\n", mean(a));
        StdOut.printf("       max %10.3f\n", max(a));
        StdOut.printf("       sum %10.3f\n", sum(a));
        StdOut.printf("    stddev %10.3f\n", stddev(a));
        StdOut.printf("       var %10.3f\n", var(a));
        StdOut.printf("   stddevp %10.3f\n", stddevp(a));
        StdOut.printf("      varp %10.3f\n", varp(a));
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
