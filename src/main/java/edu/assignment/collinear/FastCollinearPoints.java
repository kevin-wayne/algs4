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
    private LineSegment[] lineSegments;

    private class Data implements Comparable<Data> {
        private final Point pointA;
        private final Point pointB;
        private final double slope;

        Data(final Point pointa, final Point pointb, final double slope) {
            pointA = pointa;
            pointB = pointb;
            this.slope = slope;
        }

        @Override
        public int compareTo(final Data that) {
            if (Double.compare(this.slope, that.slope) == 0
                    && pointB.compareTo(that.pointB) == 0) {
                return pointA.compareTo(that.pointA);
            } else if (Double.compare(this.slope, that.slope) == 0) {
                return pointB.compareTo(that.pointB);
            }

            return Double.compare(this.slope, that.slope);
        }
    }

    public FastCollinearPoints(final Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }

        final ArrayList<Data> dataPoints = new ArrayList<Data>();
        Arrays.sort(points);

        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(points[i - 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }

        for (int i = 0; i <= points.length - 4; i++) {
            Arrays.sort(points, i + 1, points.length, points[i].slopeOrder());
            for (int j = points.length - 1; j >= i + 3; j--) {
                if (isEqual(points[i], points[j],
                        points[j - 1], points[j - 2])) {
                    double slope = points[i].slopeTo(points[j]);
                    Data temp = new Data(points[i], points[j], slope);
                    dataPoints.add(temp);
                    j = j - 3;
                    while (j >= i + 3
                            && Double.compare(slope,
                                    points[i].slopeTo(points[j])) == 0) {
                        j--;
                    }
                    j++;
                }
            }
            Arrays.sort(points, i + 1, points.length);
        }
        Arrays.sort(dataPoints.toArray());
        lineSegments = new LineSegment[dataPoints.size()];
        if (!dataPoints.isEmpty()) {
            Data comp = null;
            int index = 0;
            for (int i = 0; i < dataPoints.size() - 1; i++) {
                Data done = dataPoints.get(i);
                Data dtwo = dataPoints.get(i + 1);
                if (comp != null && comp.pointB.compareTo(dtwo.pointB) == 0
                        && Double.compare(comp.slope, dtwo.slope) == 0) {
                    ;
                } else if (done.pointB.compareTo(dtwo.pointB) == 0
                        && Double.compare(done.slope, dtwo.slope) == 0) {
                    lineSegments[index++] = new LineSegment(done.pointA,
                        done.pointB);
                    comp = done;
                } else if (comp == null) {
                    lineSegments[index++] = new LineSegment(done.pointA,
                        done.pointB);
                    comp = null;
                }
            }

            Data done = dataPoints.get(dataPoints.size() - 1);
            if (comp == null) {
                lineSegments[index++] = new LineSegment(done.pointA,
                        done.pointB);
            }
            resize(index);
        }
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
        return lineSegments.length;
    }

    /**
     * the line segments.
     * @return the segments found
     */
    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    /**
     * update the array size.
     * @param capacity new size
     */
    private void resize(final int capacity) {
        LineSegment[] temp = new LineSegment[capacity];
        for (int i = 0; i < capacity; i++) {
            temp[i] = lineSegments[i];
        }
        lineSegments = temp;
    }
}
