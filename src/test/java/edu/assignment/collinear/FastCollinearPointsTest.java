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
public class FastCollinearPointsTest {

    @Test(expected = IllegalArgumentException.class)
    public void nullList() {
        FastCollinearPoints temp = new FastCollinearPoints(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullPoint() {
        Point one = new Point(3, 4);
        Point[] points = new Point[2];
        points[0] = one;
        points[1] = null;
        FastCollinearPoints temp = new FastCollinearPoints(points);
    }

    @Test(expected = IllegalArgumentException.class)
    public void duplicatePoints() {
        Point one = new Point(3, 4);
        Point two = new Point(3, 4);
        Point[] points = new Point[2];
        points[0] = one;
        points[1] = two;
        FastCollinearPoints temp = new FastCollinearPoints(points);
    }

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
        FastCollinearPoints temp = new FastCollinearPoints(points);
        assertEquals(1, temp.numberOfSegments());
    }

    @Test
    public void eightInputs() {
        In in = new In("data/collinear/input8.txt");
        int n = in.readInt();
        assertEquals(8, n);
        Point[] points = new Point[n];
        int i = 0;
        while (!in.isEmpty()) {
            int p = in.readInt();
            int q = in.readInt();
            points[i++] = new Point(p, q);
        }

        FastCollinearPoints temp = new FastCollinearPoints(points);
        assertEquals(2, temp.numberOfSegments());
    }

    @Test
    public void equiDistInputs() {
        In in = new In("data/collinear/equidistant.txt");
        int n = in.readInt();
        assertEquals(15, n);
        Point[] points = new Point[n];
        int i = 0;
        while (!in.isEmpty()) {
            int p = in.readInt();
            int q = in.readInt();
            points[i++] = new Point(p, q);
        }

        FastCollinearPoints temp = new FastCollinearPoints(points);
        assertEquals(4, temp.numberOfSegments());
        //LineSegment[] answer = temp.segments();
        //for (int j = 0; j < answer.length; j++) {
        //    System.out.println(answer[j].toString());
        //}
    }

    @Test
    public void horizontalInputs() {
        In in = new In("data/collinear/horizontal5.txt");
        int n = in.readInt();
        assertEquals(20, n);
        Point[] points = new Point[n];
        int i = 0;
        while (!in.isEmpty()) {
            int p = in.readInt();
            int q = in.readInt();
            points[i++] = new Point(p, q);
        }

        FastCollinearPoints temp = new FastCollinearPoints(points);
        assertEquals(5, temp.numberOfSegments());
        LineSegment[] answer = temp.segments();
        for (int j = 0; j < answer.length; j++) {
            System.out.println(answer[j].toString());
        }
    }

    @Test
    public void noLines() {
        In in = new In("data/collinear/random23.txt");
        int n = in.readInt();
        assertEquals(23, n);
        Point[] points = new Point[n];
        int i = 0;
        while (!in.isEmpty()) {
            int p = in.readInt();
            int q = in.readInt();
            points[i++] = new Point(p, q);
        }

        FastCollinearPoints temp = new FastCollinearPoints(points);
        assertEquals(0, temp.numberOfSegments());
        LineSegment[] answer = temp.segments();
    }
}
