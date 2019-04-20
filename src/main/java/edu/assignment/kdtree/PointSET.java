/******************************************************************************
 *  Compilation:  javac PointSET.java
 *  Execution:    java edu.assignment.kdtrees.PointSET
 *  Dependencies: Point2D.java RectHV.java
 *  Data files:   src/main/resources/data/kdtree
 *
 *  Implements a data type to represent a set of points in the unit square
 *  using a 2d-tree to support efficient range search
 *  and nearest-neighbor search
 ******************************************************************************/

package edu.assignment.kdtree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * This class is a brute force implementation.
 * <p>
 * For additional documentation,
 * see <a href="http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html">
 * Programming Assignment 5</a> of Coursera, Algorithms Part I
 *
 *  @author vahbuna
 */
public class PointSET {

    private final TreeSet<Point2D> points;
    /**
     * construct an empty set of points.
     */
    public PointSET() {
        points = new TreeSet<Point2D>();
    }

    /**
     * is the set empty?
     * @return true or false
     */
    public boolean isEmpty()   {
         return points.isEmpty();
    }

    /**
     * number of points in the set.
     * @return count
     */
    public int size() {
        return points.size();
    }

    /**
     * add the point to the set (if it is not already in the set).
     * @param p point
     */
    public void insert(final Point2D p) {
         if (p == null) {
            throw new IllegalArgumentException();
        }
        points.add(p);
    }

    /**
     * does the set contain point p?
     * @param p point
     * @return true or false
     */
    public boolean contains(final Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return points.contains(p);
    }

    /**
     * draw all points to standard draw.
     */
    public void draw() {
        for (Point2D p: points) {
            p.draw();
        }
    }

    /**
     * all points that are inside the rectangle (or on the boundary).
     * @param rect rectangular boundary
     * @return list of points
     */
    public Iterable<Point2D> range(final RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        ArrayList<Point2D> answer = new ArrayList<Point2D>();
        Iterator<Point2D> iter = points.iterator();
        while (iter.hasNext()) {
            Point2D next = iter.next();
            if (rect.contains(next)) {
                answer.add(next);
            }
        }
        return answer;
    }

    /**
     * a nearest neighbor in the set to point p; null if the set is empty.
     * @param p point
     * @return point
     */
    public Point2D nearest(final Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (points.isEmpty()) {
            return null;
        }

        double minDistance = points.first().distanceSquaredTo(p);
        Point2D minPoint = points.first();
        Iterator<Point2D> iter = points.iterator();
        while (iter.hasNext()) {
            Point2D next = iter.next();
            double temp = next.distanceSquaredTo(p);
            if (temp < minDistance) {
                minPoint = next;
                minDistance = temp;
            }
        }
        return minPoint;
    }
}
