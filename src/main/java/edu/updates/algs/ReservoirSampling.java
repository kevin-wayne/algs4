/******************************************************************************
 *  Compilation:  javac ReservoirSampling.java
 *  Execution:    java edu.updates.algs.ReservoirSampling k < list.txt
 *  Dependencies: StdIn.java StdRandom.java
 *  Data files:   src/main/resources/data/queues
 *
 *  Reads in a list of strings and prints <em>k</em> uniformly chosen
 *  strings using Algorithm R under the assumption that Math.uniform()
 *  generates independent and uniformly distributed numbers between
 *  0 and n.
 *
 *  % java org.updates.algs.ReservoirSampling 1 < duplicates.txt
 *  AA
 *
 ******************************************************************************/
package edu.updates.algs;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

/**
 * More info at <a href="https://en.wikipedia.org/wiki/Reservoir_sampling#Algorithm_R">Algorithm R</a>.
 * @author vahbuna
 */
public class ReservoirSampling {

    private String[] reservoir;

    /**
     * index to the last data added.
     */
    private int index;

    /**
     * count of incoming data.
     */
    private int dataCount;

    public ReservoirSampling(final int k) {
        reservoir = new String[k];
        index = 0;
        dataCount = 0;
    }

    /**
     * Add the incoming data to the reservoir.
     * @param data input
     */
    public void add(final String data) {
        dataCount++;
        if (index < reservoir.length) {
            reservoir[index++] = data;
        } else {
            int j = StdRandom.uniform(dataCount);
            if (j < reservoir.length) {
                reservoir[j] = data;
            }
        }
    }

    /**
     * Print to standard output.
     */
    public void displaySample() {
        for (int i = 0; i < index; i++) {
            System.out.println(reservoir[i]);
        }
    }

    /**
     * data sampled till now in the reservoir.
     * @return reservoir
     */
    public String[] getSamples() {
        return reservoir;
    }

    public static void main(final String[] args) {
        if (args.length == 1) {
            int k = Integer.parseInt(args[0]);
            ReservoirSampling answer = new ReservoirSampling(k);
            while (!StdIn.isEmpty()) {
                answer.add(StdIn.readString());
            }
            answer.displaySample();
        }
    }
}
