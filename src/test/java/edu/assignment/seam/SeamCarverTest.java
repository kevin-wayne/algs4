package edu.assignment.seam;

import edu.princeton.cs.algs4.Picture;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Before;

/**
 *
 * @author vahbuna
 */
public class SeamCarverTest {
    private SeamCarver seam;

    @Before
    public void init() {
        Picture picture = new Picture("/Users/anu/Projects/algs4/target/classes"
                + "/data/seam/6x5.png");
        seam = new SeamCarver(picture);
    }

    @Test
    public void testHeight() {
        assertEquals(5, seam.height());
    }

    @Test
    public void testWidth() {
        assertEquals(6, seam.width());
    }

    @Test(expected = IllegalArgumentException.class)
    public void incorrectPixel() {
        seam.energy(-1, 0);
    }

    @Test
    public void testEnergy() {
        assertEquals(0, Double.compare(151.0231770292229, seam.energy(2, 1)));
    }

    @Test
    public void vertSeam() {
        int[] answer = new int[] {3, 4, 3, 2, 3};
        assertArrayEquals(answer, seam.findVerticalSeam());
    }

    @Test
    public void horizSeam() {
        int[] answer = new int[] {1, 2, 1, 2, 1, 0};
        assertArrayEquals(answer, seam.findHorizontalSeam());
    }

    @Test
    public void removeSeam() {
        int[] answer = new int[] {3, 4, 3, 2, 3};
        seam.removeVerticalSeam(answer);
    }

    @Test
    public void removeHorizSeam() {
        int[] answer = new int[] {1, 2, 1, 2, 1, 0};
        seam.removeHorizontalSeam(answer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidSeam() {
        int[] answer = new int[] {-1, 0, 1, 0, -1};
        seam.removeVerticalSeam(answer);
    }
}
