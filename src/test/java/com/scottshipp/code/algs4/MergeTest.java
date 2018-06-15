package com.scottshipp.code.algs4;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MergeTest {
    private static Logger logger = Logger.getLogger("MergeTest");

    @Test
    void sortFullyReversedStringArray() {
        String[] a = new String[26];
        for(char i = 'z', j = 'a'; i >= 'a'; i--, j++) {
            a[j - 'a'] = Character.toString(i);
        }
        logger.info("BEFORE:  " + Arrays.toString(a));
        new Merge(a).sort();
        logger.info("AFTER: " + Arrays.toString(a));
        assertIsSorted(a);
    }

    @Test
    void sortRandomIntegerArray() {
        Integer[] input = new Integer[100];
        for(int i = 0; i < 100; i++) {
            input[i] = (int)(Math.random() * 100);
        }
        logger.info("BEFORE:  " + Arrays.toString(input));
        new Merge(input).sort();
        logger.info("AFTER: " + Arrays.toString(input));
        new Merge(input).sort();
        assertIsSorted(input);
    }

    private static <T extends Comparable<T>> void assertIsSorted(T[] input) {
        T previous = null;
        for(T t : input) {
            if(previous == null) {
                previous = t;
                continue;
            }
            assertTrue(t.compareTo(previous) >= 0, "The value " + t + " should be greater than the value " + previous + ".");
        }
    }
}
