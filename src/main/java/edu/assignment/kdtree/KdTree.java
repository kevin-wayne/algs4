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

/**
 * This class is a 2d-tree implementation.
 * <p>
 * For additional documentation,
 * see <a href="http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html">
 * Programming Assignment 5</a> of Coursera, Algorithms Part I
 *
 *  @author vahbuna
 */
public class KdTree {

    private Node root;

    private int size;


    private static class Node {
        /**
         * the point.
         */
        private final Point2D p;

        /**
         * the axis-aligned rectangle corresponding to this node.
         */
        private RectHV rect;

        /**
         * the left/bottom subtree.
         */
        private Node lb;

        /**
         * the right/top subtree.
         */
        private Node rt;

        Node(final Point2D p, final int orientation, final double xmin,
                final double ymin, final double xmax, final double ymax) {
            this.p = p;
            lb = null;
            rt = null;
            if (orientation > 0) {
                rect = new RectHV(p.x(), ymin, p.x(), ymax);
            } else {
                rect = new RectHV(xmin, p.y(), xmax, p.y());
            }
        }
    }

    /**
     * construct an empty set of points.
     */
    public KdTree() {
        size = 0;
        root = null;
    }

    /**
     * is the set empty?
     * @return true or false
     */
    public boolean isEmpty()   {
         return size <= 0;
    }

    /**
     * number of points in the set.
     * @return count
     */
    public int size() {
        return size;
    }

    /**
     * add the point to the set (if it is not already in the set).
     * @param p point
     */
    public void insert(final Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        root = put(root, p, 1, 0, 0, 1, 1); // 1 for horizontal
                                // -1 for vertical
    }

    private Node put(final Node start, final Point2D point,
            final int orientation, final double xmin, final double ymin,
            final double xmax, final double ymax) {

        if (start == null) {
            size++;
            return new Node(point, orientation, xmin, ymin, xmax, ymax);
        }

        if (start.p.compareTo(point) == 0) {
            return start;
        }

        int cmp = orientationCmp(start.p, point, orientation);

        if (cmp < 0) {
            if (orientation > 0) {
            start.lb = put(start.lb, point, -1 * orientation, xmin, ymin,
                start.p.x(), ymax);
            } else {
            start.lb = put(start.lb, point, -1 * orientation, xmin, ymin,
                xmax, start.p.y());
            }
        } else {
            if (orientation > 0) {
                start.rt = put(start.rt, point, -1 * orientation, start.p.x(),
                    ymin, xmax, ymax);
            } else {
                start.rt = put(start.rt, point, -1 * orientation, xmin,
                    start.p.y(), xmax, ymax);
            }
        }
        return start;
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

        return get(root, p, 1) != null;
    }

    private Node get(final Node start, final Point2D point,
            final int orientation) {
        if (start == null) {
            return null;
        }
        if (start.p.compareTo(point) == 0) {
            return start;
        }

        int cmp = orientationCmp(start.p, point, orientation);

        if (cmp < 0) {
            return get(start.lb, point, -1 * orientation);
        } else {
            return get(start.rt, point, -1 * orientation);
        }
    }
    /**
     * draw all points to standard draw.
     */
    public void draw() {
        recursiveDraw(root);
    }

    /**
     * draw points along with splits.
     * @param start node
     */
    private void recursiveDraw(final Node start) {
        if (start == null) {
            return;
        }

        start.p.draw();
        start.rect.draw();

        recursiveDraw(start.lb);
        recursiveDraw(start.rt);
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

        return find(root, rect, 1);
    }

    /**
     * recursively find all the points within the given rectangle.
     * @param start start node
     * @param rect rectangle
     * @param orientation orientation of split
     * @return number of points
     */
    private ArrayList<Point2D> find(final Node start, final RectHV rect,
            final int orientation) {
        if (start == null) {
            return new ArrayList<Point2D>(1);
        }

        if (start.rect.intersects(rect)) {
            ArrayList<Point2D> answer = find(start.lb,
                    rect, -1 * orientation);
            answer.addAll(find(start.rt, rect, -1 * orientation));

            if (rect.contains(start.p)) {
                answer.add(start.p);
            }
            return answer;
        } else {
            int cmp;
            if (orientation > 0) {
                cmp = Double.compare(rect.xmin(), start.p.x());
            } else {
                 cmp = Double.compare(rect.ymin(), start.p.y());
            }

            if (cmp < 0) {
                return find(start.lb, rect, -1 * orientation);
            } else {
                return find(start.rt, rect, -1 * orientation);
            }
        }
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

        if (isEmpty()) {
            return null;
        }

        return near(root, p, root.p, 1, root.p.distanceSquaredTo(p));
    }

    /**
     * recursively search for a point.
     * @param start point in space being compared
     * @param key point
     * @param nearPoint nearest point
     * @param orientation horizontal or vertical
     * @param minDistance minimum distance calculated this far
     * @return the nearest point
     */
    private Point2D near(final Node start, final Point2D key, Point2D nearPoint,
            final int orientation, double minDistance) {

        double temp = start.p.distanceSquaredTo(key);
        if (Double.compare(temp, minDistance) < 0) {
            nearPoint = start.p;
            minDistance = temp;
        }
        int cmp = orientationCmp(start.p, key, orientation);

        Node first = start.lb;
        Node second = start.rt;

        if (cmp > 0) {
            first = start.rt;
            second = start.lb;
        }
        if (first != null) {
            nearPoint = near(first, key, nearPoint, -1 * orientation,
                    minDistance);
            minDistance = nearPoint.distanceSquaredTo(key);
        }

        if (second != null && Double.compare(start.rect.distanceSquaredTo(key),
                minDistance) < 0) {
            return near(second, key, nearPoint, -1 * orientation, minDistance);
        }
        return nearPoint;
    }

    /**
     * compare points based on orientation.
     * @param start point in space
     * @param p key point
     * @param orientation horizontal or vertical
     * @return -1, 0, 1
     */
    private int orientationCmp(final Point2D start, final Point2D p,
            final int orientation) {
        if (orientation > 0) {
            return Double.compare(p.x(), start.x());
        }
        return Double.compare(p.y(), start.y());
    }
}
