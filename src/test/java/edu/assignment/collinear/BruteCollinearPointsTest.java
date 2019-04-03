/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.assignment.collinear;

import edu.princeton.cs.algs4.In;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vahbuna
 */
public class BruteCollinearPointsTest {

    @Test
    public void sixInputs() {
        In in = new In("data/collinear/input6.txt");
        int n = in.readInt();
        assertEquals(6, n);
        Point[] points = new Point[n];
        int i = 0;
        while (!in.isEmpty()) {
            int p = in.readInt();
            int q = in.readInt();
            points[i++] = new Point(p, q);
        }
        BruteCollinearPoints temp = new BruteCollinearPoints(points);
        assertEquals(3, temp.numberOfSegments());
        LineSegment[] answer = temp.segments();
        for (int j = 0; j < answer.length; j++) {
            System.out.println(answer[j].toString());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullList() {
        BruteCollinearPoints temp = new BruteCollinearPoints(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullPoint() {
        Point one = new Point(3, 4);
        Point[] points = new Point[2];
        points[0] = one;
        points[1] = null;
        BruteCollinearPoints temp = new BruteCollinearPoints(points);
    }

    @Test(expected = IllegalArgumentException.class)
    public void duplicatePoints() {
        Point one = new Point(3, 4);
        Point two = new Point(3, 4);
        Point[] points = new Point[2];
        points[0] = one;
        points[1] = two;
        BruteCollinearPoints temp = new BruteCollinearPoints(points);
    }
}
