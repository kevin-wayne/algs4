/******************************************************************************
 *  Compilation:  javac FarthestPair.java
 *  Execution:    java FarthestPair < input.txt
 *  Dependencies: GrahamScan.java Point2D.java
 *  
 *  Given a set of N points in the plane, find the farthest pair
 *  (equivalently, compute the diameter of the set of points).
 *
 *  Computes the convex hull of the set of points and using the
 *  rotating calipers method to find all antipodal point pairs
 *  and the farthest pair.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The <tt>FarthestPair</tt> data type computes the farthest pair of points
 *  in a set of <em>N</em> points in the plane and provides accessor methods
 *  for getting the farthest pair of points and the distance between them.
 *  The distance between two points is their Euclidean distance.
 *  <p>
 *  This implementation computes the convex hull of the set of points and
 *  uses the rotating calipers method to find all antipodal point pairs
 *  and the farthest pair.
 *  It runs in O(<em>N</em> log <em>N</em>) time in the worst case and uses
 *  O(<em>N</em>) extra space.
 *  See also {@link ClosestPair} and {@link GrahamScan}.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/99hull">Section 9.9</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class FarthestPair {

    // farthest pair of points and distance
    private Point2D best1, best2;
    private double bestDistanceSquared = Double.NEGATIVE_INFINITY;

    /**
     * Computes the farthest pair of points in the specified array of points.
     *
     * @param  points the array of points
     * @throws NullPointerException if <tt>points</tt> is <tt>null</tt> or if any
     *         entry in <tt>points[]</tt> is <tt>null</tt>
     */
    public FarthestPair(Point2D[] points) {
        GrahamScan graham = new GrahamScan(points);

        // single point
        if (points.length <= 1) return;

        // number of points on the hull
        int M = 0;
        for (Point2D p : graham.hull())
            M++;

        // the hull, in counterclockwise order
        Point2D[] hull = new Point2D[M+1];
        int m = 1;
        for (Point2D p : graham.hull()) {
            hull[m++] = p;
        }

        // all points are equal
        if (M == 1) return;

        // points are collinear
        if (M == 2) {
            best1 = hull[1];
            best2 = hull[2];
            bestDistanceSquared = best1.distanceSquaredTo(best2);
            return;
        }

        // k = farthest vertex from edge from hull[1] to hull[M]
        int k = 2;
        while (Point2D.area2(hull[M], hull[k+1], hull[1]) > Point2D.area2(hull[M], hull[k], hull[1])) {
            k++;
        }

        int j = k;
        for (int i = 1; i <= k; i++) {
            // StdOut.println("hull[i] + " and " + hull[j] + " are antipodal");
            if (hull[i].distanceSquaredTo(hull[j]) > bestDistanceSquared) {
                best1 = hull[i];
                best2 = hull[j];
                bestDistanceSquared = hull[i].distanceSquaredTo(hull[j]);
            }
            while ((j < M) && Point2D.area2(hull[i], hull[j+1], hull[i+1]) > Point2D.area2(hull[i], hull[j], hull[i+1])) {
                j++;
                // StdOut.println(hull[i] + " and " + hull[j] + " are antipodal");
                double distanceSquared = hull[i].distanceSquaredTo(hull[j]);
                if (distanceSquared > bestDistanceSquared) {
                    best1 = hull[i];
                    best2 = hull[j];
                    bestDistanceSquared = hull[i].distanceSquaredTo(hull[j]);
                }
            }
        }
    }

    /**
     * Returns one of the points in the closest pair of points.
     *
     * @return one of the two points in the closest pair of points;
     *         <tt>null</tt> if no such point (because there are fewer than 2 points)
     */
    public Point2D either() {
        return best1;
    }

    /**
     * Returns the other point in the closest pair of points.
     *
     * @return the other point in the closest pair of points
     *         <tt>null</tt> if no such point (because there are fewer than 2 points)
     */
    public Point2D other() {
        return best2;
    }

    /**
     * Returns the Eucliden distance between the closest pair of points.
     * This quantity is also known as the <em>diameter</em> of the set of points.
     *
     * @return the Euclidean distance between the closest pair of points
     *         <tt>Double.POSITIVE_INFINITY</tt> if no such pair of points
     *         exist (because there are fewer than 2 points)
     */
    public double distance() {
        return Math.sqrt(bestDistanceSquared);
    }

   /**
     * Unit tests the <tt>FarthestPair</tt> data type.
     * Reads in an integer <tt>N</tt> and <tt>N</tt> points (specified by
     * their <em>x</em>- and <em>y</em>-coordinates) from standard input;
     * computes a farthest pair of points; and prints the pair to standard
     * output.
     */
    public static void main(String[] args) {
        int N = StdIn.readInt();
        Point2D[] points = new Point2D[N];
        for (int i = 0; i < N; i++) {
            int x = StdIn.readInt();
            int y = StdIn.readInt();
            points[i] = new Point2D(x, y);
        }
        FarthestPair farthest = new FarthestPair(points);
        StdOut.println(farthest.distance() + " from " + farthest.either() + " to " + farthest.other());
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
