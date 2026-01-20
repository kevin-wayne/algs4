package edu.princeton.cs.algs4;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class StdStatsTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void testMaxDouble() {
        Assert.assertEquals(Double.NaN,
                StdStats.max(new double[]{Double.NaN}), 0.0);
        Assert.assertEquals(8.5,
                StdStats.max(new double[]{1.2, 3.3, 8.5, 2.3}), 0.0);
        Assert.assertEquals(3.3,
                StdStats.max(new double[]{1.2, 3.3, 8.5, 2.3}, 0, 2), 0.0);
        Assert.assertEquals(Double.NaN,
                StdStats.max(new double[]{Double.NaN, 3.3, 8.5, 2.3}, 0, 2),
                0.0);
    }

    @Test
    public void testMaxInt() {
        Assert.assertEquals(8,
                StdStats.max(new int[]{1, 3, 8, 2}), 0.0);
    }

    @Test
    public void testMinDouble() {
        Assert.assertEquals(Double.NaN,
                StdStats.min(new double[]{Double.NaN}), 0.0);
        Assert.assertEquals(1.2,
                StdStats.min(new double[]{1.2, 3.3, 8.5, 2.3}), 0.0);
        Assert.assertEquals(2.3,
                StdStats.min(new double[]{1.2, 3.3, 8.5, 2.3}, 1, 4), 0.0);
        Assert.assertEquals(Double.NaN,
                StdStats.min(new double[]{Double.NaN, 3.3, 8.5, 2.3}, 0, 2),
                0.0);
    }

    @Test
    public void testMinInt() {
        Assert.assertEquals(1, StdStats.min(new int[]{1, 3, 8, 2}), 0.0);
    }

    @Test
    public void testMeanDouble() {
        Assert.assertEquals(Double.NaN, StdStats.mean(new double[0]), 0.0);
        Assert.assertEquals(3.825,
                StdStats.mean(new double[]{1.2, 3.3, 8.5, 2.3}), 0.0);
        Assert.assertEquals(4.7,
                StdStats.mean(new double[]{1.2, 3.3, 8.5, 2.3}, 1, 4), 0.0);
        Assert.assertEquals(Double.NaN,
                StdStats.mean(new double[]{Double.NaN, 3.3}, 1, 1), 0.0);
    }

    @Test
    public void testMeanInt() {
        Assert.assertEquals(Double.NaN, StdStats.mean(new int[0]), 0.0);
        Assert.assertEquals(3.5, StdStats.mean(new int[]{1, 3, 8, 2}), 0.0);
    }

    @Test
    public void testVarDouble() {
        Assert.assertEquals(Double.NaN, StdStats.var(new double[0]), 0.0);
        Assert.assertEquals(10.449166666666668,
                StdStats.var(new double[]{1.2, 3.3, 8.5, 2.3}), 0.0);
        Assert.assertEquals(11.080000000000002,
                StdStats.var(new double[]{1.2, 3.3, 8.5, 2.3}, 1, 4), 0.0);
        Assert.assertEquals(Double.NaN,
                StdStats.var(new double[]{Double.NaN, 3.3}, 1, 1), 0.0);
    }

    @Test
    public void testVarInt() {
        Assert.assertEquals(Double.NaN, StdStats.var(new int[0]), 0.0);
        Assert.assertEquals(9.666666666666666,
                StdStats.var(new int[]{1, 3, 8, 2}), 0.0);
    }

    @Test
    public void testVarpDouble() {
        Assert.assertEquals(Double.NaN, StdStats.varp(new double[0]), 0.0);
        Assert.assertEquals(7.836875000000001,
                StdStats.varp(new double[]{1.2, 3.3, 8.5, 2.3}), 0.0);
        Assert.assertEquals(7.386666666666668,
                StdStats.varp(new double[]{1.2, 3.3, 8.5, 2.3}, 1, 4), 0.0);
        Assert.assertEquals(Double.NaN,
                StdStats.varp(new double[]{Double.NaN, 3.3}, 1, 1), 0.0);
    }

    @Test
    public void testStddevDouble() {
        Assert.assertEquals(3.2325170790989906,
                StdStats.stddev(new double[]{1.2, 3.3, 8.5, 2.3}), 0.0);
        Assert.assertEquals(3.3286633954186478,
                StdStats.stddev(new double[]{1.2, 3.3, 8.5, 2.3}, 1, 4), 0.0);
    }

    @Test
    public void testStddevInt() {
        Assert.assertEquals(3.1091263510296048,
                StdStats.stddev(new int[]{1, 3, 8, 2}), 0.0);
    }

    @Test
    public void testStddevpDouble() {
        Assert.assertEquals(2.799441908666797,
                StdStats.stddevp(new double[]{1.2, 3.3, 8.5, 2.3}), 0.0);
        Assert.assertEquals(2.7178422814186014,
                StdStats.stddevp(new double[]{1.2, 3.3, 8.5, 2.3}, 1, 4), 0.0);
    }

    @Test
    public void testValidateNotNullThrowException() {
        thrown.expect(IllegalArgumentException.class);
        StdStats.stddev(null, 1, 4);
    }

    @Test
    public void testValidateSubarrayIndicesThrowException() {
        thrown.expect(IllegalArgumentException.class);
        StdStats.stddev(new double[]{1.2, 3.3, 8.5, 2.3}, -1, 4);
    }
}
