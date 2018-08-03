package com.scottshipp.code.algs4.bst;

import edu.princeton.cs.algs4.In;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BSTFindTest {
    private static Logger logger = Logger.getLogger(BSTTraversalTest.class.toString());

    private BinarySearchTree<String, Integer> st;

    void setup(String fileName) {
        st = new BinarySearchTree<>();
        In in = new In(fileName);
        for (int i = 0; !in.isEmpty(); i++) {
            String key = in.readString();
            st.put(key, i);
        }

    }

    @Test
    public void findMin() {
        setup("tinyST.txt");
        assertEquals("A", st.findMin());
    }

    @Test
    public void findSecondLeast1() {
        setup("tinyST.txt");
        assertEquals("C", st.findSecondLeast());
    }

    @Test
    public void findSecondLeast2() {
        setup("anotherTinyST.txt");
        assertEquals("E", st.findSecondLeast());
    }

}
