/******************************************************************************
 *  Compilation:  javac FastCollinearPoints.java
 *  Execution:    java FastCollinearPoints.java
 *  Dependencies: Point.java LineSegment.java
 *
 *  Examines 4 (or more) points at a time and checks whether they all lie on
 *  the same line segment, returning all such line segments.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

package edu.assignment.collinear;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author vahbuna
 */
public class FastCollinearPoints {

    /**
     * collinear lines.
     */
    private final LineSegment[] lineArray;


    public FastCollinearPoints(final Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        Point[] newPoints = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            newPoints[i] = points[i];
        }

        Arrays.sort(newPoints);

        for (int i = 1; i < newPoints.length; i++) {
            if (newPoints[i].compareTo(newPoints[i - 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }

        ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();
        for (int i = 0; i < newPoints.length; i++) {
            Point reference = newPoints[i];
            Arrays.sort(newPoints, 0, newPoints.length, reference.slopeOrder());
            int j = newPoints.length - 1;
            while (j >= 2) {
                if (isEqual(reference, newPoints[j],
                        newPoints[j - 1], newPoints[j - 2])) {
                    Point max = newPoints[j];
                    double slope = reference.slopeTo(max);
                    int k = j - 3;
                    while (k >= 0
                            && Double.compare(slope,
                                    reference.slopeTo(newPoints[k])) == 0) {
                        k--;
                    }
                    if (reference.compareTo(newPoints[k + 1]) < 0) {
                        lineSegments.add(new LineSegment(reference, max));
                    }
                    j = k + 1;
                }
                j--;
            }
            Arrays.sort(newPoints, 0, newPoints.length);
        }
        lineArray = lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }

    /**
     * Check if four points are collinear.
     * @param a point 1
     * @param b point 2
     * @param c point 3
     * @param d point 4
     * @return collinearity
     */
    private boolean isEqual(final Point a, final Point b, final Point c,
            final Point d) {
        return (Double.compare(a.slopeTo(b), a.slopeTo(c)) == 0
                && Double.compare(a.slopeTo(c), a.slopeTo(d)) == 0);
    }

    /**
     * the number of line segments.
     * @return count
     */
    public int numberOfSegments()  {
        return lineArray.length;
    }

    /**
     * the line segments.
     * @return the segments found
     */
    public LineSegment[] segments() {
        return lineArray.clone();
    }
}
