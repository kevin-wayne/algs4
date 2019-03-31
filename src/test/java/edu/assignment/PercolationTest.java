package edu.assignment;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.In;

/**
 *
 * @author vahbuna
 */
public class PercolationTest {
    private Percolation grid;
   
    @Before
    public void setUp() {
        grid = new Percolation(10);
    }
    
    @Test
    public void openExtremeNodes() {
        grid.open(1, 1);
        
        assertTrue(grid.isFull(1, 1));
        grid.open(1, 10);
        grid.open(10, 1);
        assertFalse(grid.isFull(10, 1));
        grid.open(10, 10);
        assertEquals(4, grid.numberOfOpenSites());
        assertFalse(grid.percolates());
    }  
    
    @Test
    public void openOneNode() {
        grid.open(1, 1);
        grid.open(1, 1);
        assertEquals(1, grid.numberOfOpenSites());
        
        assertFalse(grid.percolates());
    }  
    
    @Test
    public void percolationTest() {
        for(int i=1; i <11; i++) {
            grid.open(i, 1);    
        }
        assertEquals(10, grid.numberOfOpenSites());
        assertTrue(grid.percolates());
    }
    
    @Test
    public void inputFifty() { 
        // following code taken from PercolationVisualizer.java
        In in = new In("data/percolation/input50.txt");      // input file
        int n = in.readInt();         // n-by-n percolation system
        Percolation perc = new Percolation(n);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        assertEquals(1412, perc.numberOfOpenSites());
        assertTrue(perc.percolates());
    }
    
    @Test
    public void inputTwentyFull() { 
        // following code taken from PercolationVisualizer.java
        In in = new In("data/percolation/input20.txt");      // input file
        int n = in.readInt();         // n-by-n percolation system
        Percolation perc = new Percolation(n);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        assertEquals(250, perc.numberOfOpenSites());
        assertTrue(perc.percolates());
        assertFalse(perc.isFull(18, 1));
    }
}
