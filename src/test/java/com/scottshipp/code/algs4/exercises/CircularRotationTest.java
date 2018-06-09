package com.scottshipp.code.algs4.exercises;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CircularRotationTest {

    @Test
    public void testTwoStringsTrue() {
        assertTrue(CircularRotation.isRotation("ACTGACG", "TGACGAC"));
    }

    @Test
    public void testTwoStringsFalse() {
        assertFalse(CircularRotation.isRotation("ACTGACG", "TGACGAG"));
    }
}
