/******************************************************************************
 *  Compilation:  javac Percolation.java
 *  Execution:    java edu.assignment.Percolation
 *  Dependencies: WeightedQuickUnionUF.java
 *  Data files:   src/main/resources/percolation
 *
 *  Models a percolation system using an n-by-n grid of sites.
 *
 ******************************************************************************/

package edu.assignment;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * The model. We model a percolation system using an n-by-n grid of sites.
 * Each site is either open or blocked.
 * A full site is an open site that can be connected to an open site
 * in the top row via a chain of neighboring (left, right, up, down) open sites.
 * We say the system percolates if there is a full site in the bottom row.
 * In other words, a system percolates if we fill all open sites connected to
 * the top row and that process fills some open site on the bottom row.
 * For additional documentation, see
 * <a href="http://coursera.cs.princeton.edu/algs4/assignments/percolation.html">
 * Sprecifications</a>
 *
 * @author Robert Sedgewick
 * @author Kevin Waynev
 * @author vahbuna
 */
public class Percolation {
    /**
     * N by N grid.
     */
    private final boolean[] grid;

    /**
     * grid length.
     */
    private final int gridLength;

    /**
     * to check if site is full.
     */
    private final WeightedQuickUnionUF topConnections;

    /**
     * To check if grid percolates.
     */
    private final WeightedQuickUnionUF allConnections;

    private int numberOfOpenSites = 0;

    /**
    * create n-by-n grid, with all sites blocked.
    * @param n size of grid
    */
    public Percolation(final int n) {
        if (n < 1) {
            throw new java.lang.IllegalArgumentException();
        }

        grid = new boolean[n * n];
        gridLength = n;
        // two extra for upper node and lower node
        topConnections = new WeightedQuickUnionUF(n * n + 1);
        allConnections = new WeightedQuickUnionUF(n * n + 2);
    }

    /**
     * Checks if row and column fall in the grid.
     * @param row row number
     * @param col column number
     * @return true if in range
     */
    private boolean isIndexValid(final int row, final int col) {
        return (row > 0 && col > 0 && row <= gridLength && col <= gridLength);
    }

    /**
     * open site (row, col) if it is not open already.
     * @param row row between 1 and N
     * @param col column between 1 and N
     */
    public void open(final int row, final int col) {
        if (!isIndexValid(row, col)) {
            throw new java.lang.IllegalArgumentException();
        }

        int index = gridLength * (row - 1) + col - 1;

        if (!grid[index]) {
            grid[index] = true;
            numberOfOpenSites++;
        }

        if (col + 1 <= gridLength && isOpen(row, col + 1))  {
            topConnections.union(index, gridLength * (row - 1) + col);
            allConnections.union(index, gridLength * (row - 1) + col);
        }

        if (col - 1 > 0 && isOpen(row, col - 1))  {
            topConnections.union(index, gridLength * (row - 1) + col - 2);
            allConnections.union(index, gridLength * (row - 1) + col - 2);
        }

        if (row + 1 <= gridLength && isOpen(row + 1, col))  {
            topConnections.union(index, gridLength * row + col - 1);
            allConnections.union(index, gridLength * row + col - 1);
        }

        if (row - 1 > 0 && isOpen(row - 1, col))  {
            topConnections.union(index, gridLength * (row - 2) + col - 1);
            allConnections.union(index, gridLength * (row - 2) + col - 1);
        }

        if (row == 1) {
            topConnections.union(index, gridLength * gridLength);
            allConnections.union(index, gridLength * gridLength);
        }

        if (row == gridLength) {
            allConnections.union(index, gridLength * gridLength + 1);
        }
    }

    /**
     * is site (row, col) open?
     * @param row row between 1 and N
     * @param col column between 1 and N
     * @return if site is open
     */
    public boolean isOpen(final int row, final int col) {
        if (!isIndexValid(row, col)) {
            throw new java.lang.IllegalArgumentException();
        }
        int index = gridLength * (row - 1) + col - 1;

        return grid[index];
    }

    /**
     * is site (row, col) full?
     * @param row row between 1 and N
     * @param col column between 1 and N
     * @return if a site is connected to the top
     */
    public boolean isFull(final int row, final int col) {
        if (!isIndexValid(row, col)) {
            throw new java.lang.IllegalArgumentException();
        }

        int index = gridLength * (row - 1) + col - 1;
        return topConnections.connected(index, gridLength * gridLength);
    }

    /**
     * number of open sites.
     * @return count
     */
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    /**
     * Does system percolates?
     * @return true
     */
    public boolean percolates() {
    return allConnections.connected(gridLength * gridLength,
            gridLength * gridLength + 1);
    }
}
