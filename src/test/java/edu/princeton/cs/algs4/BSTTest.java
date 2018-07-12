package edu.princeton.cs.algs4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BSTTest {
    private static Logger logger = Logger.getLogger(BSTTest.class.toString());

    private BST<String, Integer> st;
    @BeforeEach
    void setup() {
        // populate BST with tinyST

        st = new BST<>();
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
        assertEquals("S 0 E 12 X 7 A 8 R 3 C 4 H 5 M 9 L 11 P 10 ", sb.toString());
    }

//    @Test
}
