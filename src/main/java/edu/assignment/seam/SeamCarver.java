/******************************************************************************
 *  Compilation:  javac SeamCarver.java
 *  Execution:    java SeamCarver
 *  Dependencies:Color.java  Picture.java LineSegment.java
 *
 *  Seam-carving is a content-aware image resizing technique where the image
 *  is reduced in size by one pixel of height (or width) at a time.
 *
 *  Discovered in 2007 by Shai Avidan and Ariel Shamir.
 *
 ******************************************************************************/
package edu.assignment.seam;

import edu.princeton.cs.algs4.Picture;
import java.util.ArrayList;

/**
 * Algorithm to resize an image by removing less important pixels.
 * <p>
 * For additional documentation,
 * see <a href="http://coursera.cs.princeton.edu/algs4/assignments/seam.html">
 * Programming Assignment 2</a> of Coursera, Algorithms Part II
 * @author vahbuna
 */
public class SeamCarver {
    private double[][] energies;
    private int[][] rgbs;
    private int width;
    private int height;
    /**
     * create a seam carver object based on the given picture.
     * @param picture input image
     */
    public SeamCarver(final Picture picture) {
        width = picture.width();
        height = picture.height();
        energies = new double[width][height];
        rgbs = new int[width][height];

        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                rgbs[col][row] = picture.getRGB(col, row);
            }
        }
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                energies[col][row] = calculateEnergy(col, row);
            }
        }
    }

    /**
     * current picture.
     * @return stored picture
     */
    public Picture picture() {
        Picture image = new Picture(width, height);
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                image.setRGB(col, row, rgbs[col][row]);
            }
        }
        return image;
    }

    /**
     * width of current picture.
     * @return width
     */
    public int width() {
        return this.width;
    }

    /**
     * height of current picture.
     * @return height
     */
    public int height() {
        return this.height;
    }

    /**
     * energy of pixel at column x and row y.
     * @param x input
     * @param y input
     * @return energy
     */
    public  double energy(final int x, final int y) {
        if (x < 0 || y < 0 || x >= width() || y >= height()) {
            throw new java.lang.IllegalArgumentException();
        }

        return energies[x][y];
    }

    /**
     * energy of pixel at column x and row y.
     * @param x input
     * @param y input
     * @return energy
     */
    private double calculateEnergy(final int x, final int y) {
        double energy = 1000d;

        if (x > 0 && x < width - 1 && y > 0 && y < height - 1) {
            energy = calculatePartialEnergy(rgbs[x - 1][y],
                    rgbs[x + 1][y]);
            energy +=  calculatePartialEnergy(rgbs[x][y - 1],
                    rgbs[x][y + 1]);
            energy = Math.sqrt(energy);
        }
        return energy;
    }

    /**
     * helper function for calculate energy.
     * @param colorA color of first pixel
     * @param colorB color of second pixel
     * @return energy value
     */
    private double calculatePartialEnergy(final int colorA, final int colorB) {
        return Math.pow(((colorA >> 16) & 0xFF)
                - ((colorB >> 16) & 0xFF), 2)
                + Math.pow(((colorA >>  8) & 0xFF)
                        - ((colorB >>  8) & 0xFF), 2)
                + Math.pow((colorA & 0xFF) - (colorB & 0xFF), 2);
    }
    /**
     * sequence of indices for horizontal seam.
     * @return array
     */
    public   int[] findHorizontalSeam() {
       int rows = height();
        int cols = width();
        int[] edgeTo = new int[rows * cols];
        double[] distTo = new double[rows * cols];
        int minIdx = -1;
        double minDist = Double.POSITIVE_INFINITY;
        for (int i = 0; i < distTo.length; i++) {
            distTo[i] = Double.POSITIVE_INFINITY;
        }

        for (int i = 0; i < distTo.length; i += cols) {
            distTo[i] = energy(i % cols, i / cols);
        }

        for (int i = 0; i < cols - 1; i++) {
            for (int j = i; j < distTo.length; j += cols) {
                for (int node : horizontalEdges(j)) {
                    if (Double.compare(distTo[node],
                            distTo[j] + energy(node % cols, node / cols)) > 0) {
                        distTo[node] = distTo[j] + energy(node % cols,
                                node / cols);
                        edgeTo[node] = j;
                    }
                }
            }
        }

        for (int i = cols - 1; i < distTo.length; i += cols) {
            if (Double.compare(distTo[i], minDist) < 0) {
                minDist = distTo[i];
                minIdx = i;
            }
        }
        int[] answer = new int[cols];
        for (int i = cols - 1; i >= 0; i--) {
            answer[i] = minIdx / cols;
            minIdx = edgeTo[minIdx];
        }
        return answer;
    }

    /**
     * sequence of indices for vertical seam.
     * @return array
     */
    public   int[] findVerticalSeam() {
        int rows = height();
        int cols = width();
        int[] edgeTo = new int[rows * width()];
        double[] distTo = new double[rows * width()];
        int minIdx = -1;
        double minDist = Double.POSITIVE_INFINITY;
        for (int i = 0; i < distTo.length; i++) {
            distTo[i] = Double.POSITIVE_INFINITY;
        }

        for (int i = 0; i < distTo.length; i++) {
            if (i < cols) {
                distTo[i] = energy(i % cols, i / cols);
            }
            for (int node : verticalEdges(i)) {
                if (Double.compare(distTo[node],
                        distTo[i] + energy(node % cols, node / cols)) > 0) {
                    distTo[node] = distTo[i] + energy(node % cols, node / cols);
                    edgeTo[node] = i;
                }
            }
        }

        for (int i = distTo.length - 1; i >= distTo.length - cols; i--) {
            if (Double.compare(distTo[i], minDist) < 0) {
                minDist = distTo[i];
                minIdx = i;
            }
        }
        int[] answer = new int[rows];
        for (int i = rows - 1; i >= 0; i--) {
            answer[i] = minIdx % cols;
            minIdx = edgeTo[minIdx];
        }
        return answer;
    }

    /**
     * compute possible vertical edges.
     * @param node input
     * @return list of edges
     */
    private ArrayList<Integer> verticalEdges(final int node) {
        int rows = height();
        int cols = width();
        ArrayList<Integer> nodes = new ArrayList<>();
        if (node / cols < rows - 1) {
            nodes.add(node + cols);
            if (node % cols > 0) {
                nodes.add(node + cols - 1);
            }

            if (node % cols < cols - 1) {
                nodes.add(node + cols + 1);
            }
        }
        return nodes;
    }

    /**
     * Compute horizontal edges for a given node.
     * @param node input
     * @return list of edges
     */
    private ArrayList<Integer> horizontalEdges(final int node) {
        int rows = height();
        int cols = width();
        ArrayList<Integer> nodes = new ArrayList<>();
        if (node % cols < cols - 1) {
            nodes.add(node + 1);
            if (node / cols > 0) {
                nodes.add(node - cols + 1);
            }

            if (node / cols < rows - 1) {
                nodes.add(node + cols + 1);
            }
        }
        return nodes;
    }

    /**
     * remove horizontal seam from current picture.
     * @param seam input
     */
    public void removeHorizontalSeam(final int[] seam) {
        checkForErrors(seam);

        if (this.height <= 1 || seam.length != this.width) {
            throw new java.lang.IllegalArgumentException();
        }
        double[][]newEnergies = new double[width][height - 1];
        int[][] newRgbs = new int[width][height - 1];
        for (int i = 0; i < seam.length; i++) {
            int newrow = 0;
            for (int row = 0; row < height; row++) {
                if (seam[i] != row) {
                    newRgbs[i][newrow] = rgbs[i][row];
                    newEnergies[i][newrow] = energies[i][row];
                    newrow++;
                }
            }
        }
        height--;

        rgbs = newRgbs;
        energies = newEnergies;

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] - 1 >= 0) {
                energies[i][seam[i] - 1] = calculateEnergy(i, seam[i] - 1);
            }
            energies[i][seam[i]] = calculateEnergy(i, seam[i]);
        }
    }

    /**
     * remove vertical seam from current picture.
     * @param seam input
     */
    public    void removeVerticalSeam(final int[] seam) {
        checkForErrors(seam);

        if (this.width <= 1 || seam.length != this.height) {
            throw new java.lang.IllegalArgumentException();
        }

        double[][]newEnergies = new double[width - 1][height];
        int[][] newRgbs = new int[width - 1][height];
        for (int i = 0; i < seam.length; i++) {
            int newcol = 0;
            for (int col = 0; col < width; col++) {
                if (seam[i] != col) {
                    newRgbs[newcol][i] = rgbs[col][i];
                    newEnergies[newcol][i] = energies[col][i];
                    newcol++;
                }
            }
        }
        width--;

        rgbs = newRgbs;
        energies = newEnergies;

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] - 1 >= 0) {
                energies[seam[i] - 1][i] = calculateEnergy(seam[i] - 1, i);
            }
            if (seam[i] >= 0 && seam[i] < width) {
                energies[seam[i]][i] = calculateEnergy(seam[i], i);
            }
        }
    }

    /**
     * validate arguments for seam removal.
     * @param seam array
     */
    private void checkForErrors(final int[] seam) {
        if (seam == null) {
            throw new java.lang.IllegalArgumentException();
        }

        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i] - seam[i - 1]) > 1d) {
                throw new java.lang.IllegalArgumentException();
            }
        }
    }
}

