
package edu.updates.algs;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anu
 */
public class ReservoirSamplingTest {
    private ReservoirSampling sample;

    @Before
    public void setUp() {
        sample = new ReservoirSampling(1);
    }

    @Test
    public void testDistribution() {
        double[] counts = new double[2];
        for (int i = 0; i < 1000; i++) {
            sample.add("A");
            sample.add("B");
            sample.add("A");
            sample.add("A");
            sample.add("B");
            String[] answer = sample.getSamples();
            if (answer[0] == "A") {
                counts[0] += 1;
            } else {
                counts[1] += 1;
            }
            sample = new ReservoirSampling(1);
        }
        double gStats = 2 * counts[0] * Math.log(counts[0] / 600.0);
        gStats += 2 * counts[1] * Math.log(counts[1] / 400.0);
        assertTrue(gStats < 5); // don't know the p-value
    }
}
