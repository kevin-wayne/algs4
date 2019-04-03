/******************************************************************************
 *  Compilation:  javac BruteCollinearPoints.java
 *  Execution:    java BruteCollinearPoints
 *  Dependencies: Point.java LineSegment.java
 *
 *  Examines 4 points at a time and checks whether they all lie on
 *  the same line segment, returning all such line segments.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

package edu.assignment.collinear;

/**
 * To recognize line patterns in a given set of points using brute force.
 * <a href="http://coursera.cs.princeton.edu/algs4/assignments/collinear.html">
 * Pattern Recognition</a>
 * @author vahbuna
 */
public class BruteCollinearPoints {
    /**
     * first points of all the segments.
     */
    private Point[] pointsA;

    /**
     * last points of all the segments.
     */
    private Point[] pointsB;

    private int size;

    private final LineSegment[] lineSegments;
    /**
     * Finds all line segments containing 4 points.
     * @param point (x,y)
     */
    public BruteCollinearPoints(final Point[] point) {
        if (point == null) {
            throw new IllegalArgumentException();
        }

        int index = 0;
        size = 2;
        pointsA = new Point[size];
        pointsB = new Point[size];

        for (int i = 0; i < point.length; i++) {
            if (point[i] == null) {
                throw new IllegalArgumentException();
            }

            for (int j = i + 1; j < point.length; j++) {
                if (point[j] == null || point[j].compareTo(point[i]) == 0) {
                    throw new IllegalArgumentException();
                }

                double slopeij = point[i].slopeTo(point[j]);
                for (int k = j + 1; k < point.length; k++) {
                    if (point[k] == null || point[k].compareTo(point[i]) == 0) {
                        throw new IllegalArgumentException();
                    }
                    double slopeik = point[i].slopeTo(point[k]);
                    if (Double.compare(slopeij, slopeik) != 0) {
                        continue;
                    }

                    for (int m = k + 1; m < point.length; m++) {
                        if (point[m] == null
                                || point[m].compareTo(point[i]) == 0) {
                            throw new IllegalArgumentException();
                        }

                        double slopeil = point[i].slopeTo(point[m]);
                        if (Double.compare(slopeil, slopeik) == 0) {
                            Point pointMin = point[i];
                            Point pointMax = point[i];
                            if (point[j].compareTo(pointMin) < 0) {
                                pointMin = point[j];
                            } else if (point[j].compareTo(pointMax) > 0) {
                                pointMax = point[j];
                            }

                            if (point[k].compareTo(pointMin) < 0) {
                                pointMin = point[k];
                            } else if (point[k].compareTo(pointMax) > 0) {
                                pointMax = point[k];
                            }

                            if (point[m].compareTo(pointMin) < 0) {
                                pointMin = point[m];
                            } else if (point[m].compareTo(pointMax) > 0) {
                                pointMax = point[m];
                            }

                            if (!isPresent(pointMin, pointMax, index)) {
                                pointsA[index] = pointMin;
                                pointsB[index++] = pointMax;
                                if (index == size) {
                                    resize(2 * size, index);
                                }
                            }
                        }
                    }
                }
            }
        }

        lineSegments = new LineSegment[index];
        for (int i = 0; i < index; i++) {
            LineSegment temp = new LineSegment(pointsA[i], pointsB[i]);
            lineSegments[i] = temp;
            pointsA[i] = null;
            pointsB[i] = null;
        }
    }

    /**
     * Increase the array size.
     * @param capacity new size
     * @param previousSize old capacity
     */
    private void resize(final int capacity, final int previousSize) {
        Point[] temp = new Point[capacity];
        for (int i = 0; i < previousSize; i++) {
            temp[i] = pointsA[i];
        }

        pointsA = temp;

        temp = new Point[capacity];
        for (int i = 0; i < previousSize; i++) {
            temp[i] = pointsB[i];
        }

        pointsB = temp;
        size = capacity;
    }

    /**
     * check if line already present in the list.
     * @param pointA first point
     * @param pointB last point
     * @param index number of elements in the array
     * @return true or false
     */
    private boolean isPresent(final Point pointA, final Point pointB,
            final int index) {
        for (int i = 0; i < index; i++) {
            if (pointsA[i].compareTo(pointA) == 0
                    && pointsB[i].compareTo(pointB) == 0) {
                return true;
            }
        }
        return false;
    }
    /**
     * the number of line segments.
     * @return count
     */
    public int numberOfSegments()  {
        return lineSegments.length;
    }

    /**
     * the line segments.
     * @return the segments found
     */
    public LineSegment[] segments() {
        return lineSegments.clone();
    }
}
