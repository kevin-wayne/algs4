/*****************************************************************************
 *  Compilation:  javac Permutation.java
 *  Execution:    java edu.assignment.Permutation
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   src/main/resources/data
 *
 * Takes an integer k as a command-line argument;
 * reads in a sequence of strings from standard input and prints
 * exactly k of them, uniformly at random.
 * % more distinct.txt
 * A B C D E F G H I
 *
 * % java Permutation 3 < distinct.txt
 * C
 * G
 * A
 *
 *****************************************************************************/

// package edu.assignment;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 *
 * @author vahbuna
 */
public class Permutation {
    public static void main(final String[] args) {
        if (args.length == 1) {
            int k = Integer.parseInt(args[0]);
            int count = k;
            RandomizedQueue<String> ranq = new RandomizedQueue<String>();
            while (count > 0 && !StdIn.isEmpty()) {
                    ranq.enqueue(StdIn.readString());
                    count--;
            }
            if (count == 0) {
                while (!StdIn.isEmpty()) {
                    if (k > 0) {
                        StdOut.println(ranq.dequeue());
                        ranq.enqueue(StdIn.readString());
                        k--;
                    } else {
                        StdIn.readString();
                    }
                }
            }
            while (k > 0) {
                StdOut.println(ranq.dequeue());
                k--;
            }
        }
    }
}
