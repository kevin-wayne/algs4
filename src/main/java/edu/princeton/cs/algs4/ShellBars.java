/******************************************************************************
 *  Compilation:  javac ShellBars.java
 *  Execution:    java ShellBars N
 *  Dependencies: StdDraw.java
 *
 *  Shellsor N random real numbers between 0 and 1, visualizing
 *  the results by ploting bars with heights proportional to the values.
 *
 *  % java ShellBars 150
 *
 *  Note: should probably update to draw bars using filledRectangle()
 *  instead of drawing lines.
 *
 ******************************************************************************/
package edu.princeton.cs.algs4;

import java.awt.Font;

public class ShellBars {
    private static final int FF = 4;


    public static void sort(double[] a) {
        int n = a.length;
        int k = 1;
        int h = 1;
        while (h < n/3) {
            h = 3*h + 1;
            k++;
        }
        show(a, k, "input");

        while (h >= 1) {
            for (int i = h; i < n; i++) {
                for (int j = i; j >= h && less(a[j], a[j-h]); j -= h) {
                    exch(a, j, j-h);
                }
            }
            if (h < n) show(a, --k, h + "-sorted");
            h = h/3;
        }
    }

    private static void show(double[] a, int k, String message) {
        for (int i = 0; i < a.length; i++) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.line(i, FF*k, i, FF*k + a[i]*(FF-1));
        }
        StdDraw.setPenColor(StdDraw.BOOK_RED);
        StdDraw.text(0, FF*k - 0.3, message);
    }

    private static boolean less(double v, double w) {
        return v < w;
    }

    private static void exch(double[] a, int i, int j) {
        double t = a[i];
        a[i] = a[j];
        a[j] = t;
    }


    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        double[] a = new double[n];
        for (int i = 0; i < n; i++)
          a[i] = StdRandom.uniform(0.0, 1.0);
        int k = (int) Math.round(Math.log(n) / Math.log(3));  // number of h-increment values

        StdDraw.enableDoubleBuffering();

        StdDraw.setCanvasSize(756, 900);
        StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 9));
        StdDraw.setXscale(-1, n+1);
        StdDraw.setYscale(-1, FF*(k+1));
        StdDraw.setPenRadius(0.005);
        sort(a);
        StdDraw.show();
    }
}
