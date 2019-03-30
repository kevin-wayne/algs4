/******************************************************************************
 *  Compilation:  javac PercolationStats.java
 *  Execution:    java edu.assignment.PercolationStats 200 100
 *  Dependencies: StdRandom.java StdStats.java
 *  Data files:   src/main/resources/percolation
 *
 *  Estimates the value of the percolation threshold via Monte Carlo simulation.
 *
 * % java edu.assignment.PercolationStats 200 100
 * mean                    = 0.5929934999999997
 * stddev                  = 0.00876990421552567
 * 95% confidence interval = [0.5912745987737567, 0.5947124012262428]
 *
 ******************************************************************************/

package edu.assignment;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Monte Carlo simulation. To estimate the percolation threshold, consider
 * the following computational experiment:
 *
 *  Initialize all sites to be blocked.
 *  Repeat the following until the system percolates:
 *      Choose a site uniformly at random among all blocked sites.
 *      Open the site.
 *  The fraction of sites that are opened when the system percolates provides
 *  an estimate of the percolation threshold.
 *
 * @author Robert Sedgewick
 * @author Kevin Waynev
 * @author vahbuna
 */

public class PercolationStats {

    /**
     * number of trials.
     */
    private final int T;

    /**
     * Mean of all trials.
     */
    private final double mean;

    /**
     * Standard deviation of all trials.
     */
    private final double stddev;
    /**
     * perform trials independent experiments on an n-by-n grid.
     * @param n grid size
     * @param trials number of trials
     */
    public PercolationStats(final int n, final int trials) {
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        final double[] results = new double[trials];
        T = trials;
        // final int seed = 100;
        // StdRandom.setSeed(seed);

        for (int i = 0; i < T; i++) {
            Percolation grid = new Percolation(n);
            while (!grid.percolates()) {
                grid.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
            }
            results[i] = 1.0 * grid.numberOfOpenSites() / (n * n);
        }
        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);
    }

    /**
     * sample mean of percolation threshold.
     * @return mean
     */
    public double mean() {
        return mean;
    }

    /**
     * sample standard deviation of percolation threshold.
     * @return standard deviation
     */
    public double stddev() {
        return stddev;
    }

    /**
     * low  endpoint of 95% confidence interval.
     * @return lower confidence interval
     */
    public double confidenceLo() {
        final double threshold =  1.96;
        return mean - threshold * stddev() / Math.sqrt(T);
    }

    /**
     * high endpoint of 95% confidence interval.
     * @return higher confidence interval
     */
    public double confidenceHi() {
        final double threshold =  1.96;
        return mean + threshold * stddev() / Math.sqrt(T);
    }

    /**
     * test client (described below).
     * @param args input
     */
    public static void main(final String[] args) {
        int n;
        int t;
        if (args.length == 2) {
            n = Integer.parseInt(args[0]);
            t = Integer.parseInt(args[1]);
            PercolationStats tests = new PercolationStats(n, t);
            System.out.println("mean                    = " + tests.mean());
            System.out.println("stddev                  = " + tests.stddev());
            System.out.println("95% confidence interval = ["
                    + tests.confidenceLo()
                    + ", "
                    + tests.confidenceHi()
                    + "]");
        }
    }
}
