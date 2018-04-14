/******************************************************************************
 *  Compilation:  javac FarthestPair.java
 *  Execution:    java FarthestPair < input.txt
 *  Dependencies: GrahamScan.java Point2D.java
 *  Data files:   https://algs4.cs.princeton.edu/99hull/rs1423.txt
 *                https://algs4.cs.princeton.edu/99hull/kw1260.txt
 *  
 *  Given a set of n points in the plane, find the farthest pair
 *  (equivalently, compute the diameter of the set of points).
 *
 *  Computes the convex hull of the set of points and using the
 *  rotating calipers method to find all antipodal point pairs
 *  and the farthest pair.
 *
 *  % java FarthestPair < input100.txt
 *  42697.98170874122 from (32011.0, 3140.0) to (822.0, 32301.0)
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The {@code FarthestPair} data type computes the farthest pair of points
 *  in a set of <em>n</em> points in the plane and provides accessor methods
 *  for getting the farthest pair of points and the distance between them.
 *  The distance between two points is their Euclidean distance.
 *  <p>
 *  This implementation computes the convex hull of the set of points and
 *  uses the rotating calipers method to find all antipodal point pairs
 *  and the farthest pair.
 *  It runs in O(<em>n</em> log <em>n</em>) time in the worst case and uses
 *  O(<em>N</em>) extra space.
 *  See also {@link ClosestPair} and {@link GrahamScan}.
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/99hull">Section 9.9</a> of
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
     * @throws IllegalArgumentException if {@code points} is {@code null} or if any
     *         entry in {@code points[]} is {@code null}
     */
    public FarthestPair(Point2D[] points) {
        if (points == null) throw new IllegalArgumentException("constructor argument is null");
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException("array element " + i + " is null");
        }

        GrahamScan graham = new GrahamScan(points);

        // single point
        if (points.length <= 1) return;

        // number of points on the hull
        int m = 0;
        for (Point2D p : graham.hull())
            m++;

        // the hull, in counterclockwise order hull[1] to hull[m]
        Point2D[] hull = new Point2D[m+1];
        m = 1;
        for (Point2D p : graham.hull()) {
            hull[m++] = p;
        }
        m--;

        // all points are equal
        if (m == 1) return;

        // points are collinear
        if (m == 2) {
            best1 = hull[1];
            best2 = hull[2];
            bestDistanceSquared = best1.distanceSquaredTo(best2);
            return;
        }

        // k = farthest vertex from edge from hull[1] to hull[m]
        int k = 2;
        while (Point2D.area2(hull[m], hull[1], hull[k+1]) > Point2D.area2(hull[m], hull[1], hull[k])) {
            k++;
        }

        int j = k;
        for (int i = 1; i <= k && j <= m; i++) {
            // StdOut.println("hull[i] + " and " + hull[j] + " are antipodal");
            if (hull[i].distanceSquaredTo(hull[j]) > bestDistanceSquared) {
                best1 = hull[i];
                best2 = hull[j];
                bestDistanceSquared = hull[i].distanceSquaredTo(hull[j]);
            }
            while ((j < m) && Point2D.area2(hull[i], hull[i+1], hull[j+1]) > Point2D.area2(hull[i], hull[i+1], hull[j])) {
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
     * Returns one of the points in the farthest pair of points.
     *
     * @return one of the two points in the farthest pair of points;
     *         {@code null} if no such point (because there are fewer than 2 points)
     */
    public Point2D either() {
        return best1;
    }

    /**
     * Returns the other point in the farthest pair of points.
     *
     * @return the other point in the farthest pair of points
     *         {@code null} if no such point (because there are fewer than 2 points)
     */
    public Point2D other() {
        return best2;
    }

    /**
     * Returns the Eucliden distance between the farthest pair of points.
     * This quantity is also known as the <em>diameter</em> of the set of points.
     *
     * @return the Euclidean distance between the farthest pair of points
     *         {@code Double.POSITIVE_INFINITY} if no such pair of points
     *         exist (because there are fewer than 2 points)
     */
    public double distance() {
        return Math.sqrt(bestDistanceSquared);
    }

   /**
     * Unit tests the {@code FarthestPair} data type.
     * Reads in an integer {@code n} and {@code n} points (specified by
     * their <em>x</em>- and <em>y</em>-coordinates) from standard input;
     * computes a farthest pair of points; and prints the pair to standard
     * output.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        int n = StdIn.readInt();
        Point2D[] points = new Point2D[n];
        for (int i = 0; i < n; i++) {
            int x = StdIn.readInt();
            int y = StdIn.readInt();
            points[i] = new Point2D(x, y);
        }
        FarthestPair farthest = new FarthestPair(points);
        StdOut.println(farthest.distance() + " from " + farthest.either() + " to " + farthest.other());
    }

}

/******************************************************************************
 *  Copyright 2002-2018, Robert Sedgewick and Kevin Wayne.
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
