package com.scottshipp.code.algs4;

import edu.princeton.cs.algs4.In;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuickFindUFTest {
    private static Logger logger = Logger.getLogger("QuickFindUFTest");

    private QuickFindUF uf;

    @BeforeEach
    public void setup() {
        In in = new In("tinyUF.txt");
        int n = in.readInt();
        this.uf = new QuickFindUF(n);
        while (!in.isEmpty()) {
            int p = in.readInt();
            int q = in.readInt();
            if (uf.connected(p, q)) continue;
            uf.union(p, q);
            logger.info(p + " " + q);
        }
        logger.info(uf.count() + " components");
        logger.info(uf.toString());
    }

    @Test
    public void connectedComponents() {
        assertTrue(uf.count == 2);
    }
}
