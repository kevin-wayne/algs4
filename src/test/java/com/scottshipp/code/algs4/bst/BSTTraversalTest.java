package com.scottshipp.code.algs4.bst;

import edu.princeton.cs.algs4.In;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BSTTraversalTest {
    private static Logger logger = Logger.getLogger(BSTTraversalTest.class.toString());

    private BinarySearchTree<String, Integer> st;
    @BeforeEach
    void setup() {
        st = new BinarySearchTree<>();
        // populate BST with tinyST
        In in = new In("anotherTinyST.txt");
        for (int i = 0; !in.isEmpty(); i++) {
            String key = in.readString();
            st.put(key, i);
        }

    }

    void alternateSetup() {
        st = new BinarySearchTree<>();
        In in = new In("tinyST.txt");
        for (int i = 0; !in.isEmpty(); i++) {
            String key = in.readString();
            st.put(key, i);
        }
    }

    @Test
    public void testInitialTreePopulation() {
        StringBuilder sb = new StringBuilder();
        for (String s : st.levelOrder()) {
            sb.append(s + " " + st.get(s) + " ");
        }
        logger.info(sb.toString());
        assertEquals("A 7 T 1 R 2 X 6 E 11 M 8 L 10 P 9 ", sb.toString());
    }

    @Test
    public void testLevelOrderTraversal() {
        StringBuilder sb = new StringBuilder();
        for (String s : st.levelOrder()) {
            sb.append(s + " ");
        }
        logger.info(sb.toString());
        assertEquals("A T R X E M L P ", sb.toString());
    }

    @Test
    public void testLevelOrderTraversal2() {
        alternateSetup();
        StringBuilder sb = new StringBuilder();
        for (String s : st.levelOrder()) {
            sb.append(s + " ");
        }
        logger.info(sb.toString());
        assertEquals("S E X A R C H M L P ", sb.toString());
    }

    @Test
    public void testZigZagTraversal() {
        StringBuilder sb = new StringBuilder();
        for (String s : st.zigZagOrder()) {
            sb.append(s + " ");
        }
        logger.info(sb.toString());
        assertEquals("A T R X E M P L ", sb.toString());
    }

    @Test
    public void testZigZagTraversal2() {
        alternateSetup();
        StringBuilder sb = new StringBuilder();
        for (String s : st.zigZagOrder()) {
            sb.append(s + " ");
        }
        logger.info(sb.toString());
        assertEquals("S X E A R H C M P L ", sb.toString());
    }

    @Test
    public void testReverseZigZagTraversal() {
        StringBuilder sb = new StringBuilder();
        for (String s : st.reverseZigZagOrder()) {
            sb.append(s + " ");
        }
        logger.info(sb.toString());
        assertEquals("L P M E X R T A ", sb.toString());
    }

    @Test
    public void testAnotherReverseZigZagTraversal() {
        alternateSetup();
        StringBuilder sb = new StringBuilder();
        for (String s : st.reverseZigZagOrder()) {
            sb.append(s + " ");
        }
        logger.info(sb.toString());
        assertEquals("L P M C H R A E X S ", sb.toString());
    }

    @Test
    public void testMirroredInOrder() {
        StringBuilder sb = new StringBuilder();
        for (String s : st.mirroredInOrder()) {
            sb.append(s + " ");
        }
        logger.info(sb.toString());
        assertEquals("X T R P M L E A ", sb.toString());
    }

    @Test
    public void testInOrderTraversal() {
        StringBuilder sb = new StringBuilder();
        for (String s : st.inOrder()) {
            sb.append(s + " ");
        }
        logger.info(sb.toString());
        assertEquals("A E L M P R T X ", sb.toString());
    }
}

