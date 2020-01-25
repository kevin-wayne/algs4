/******************************************************************************
 *  Compilation:  javac Count.java
 *  Execution:    java Count alpha < input.txt
 *  Dependencies: Alphabet.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/50strings/abra.txt
 *                https://algs4.cs.princeton.edu/50strings/pi.txt
 *
 *  Create an alphabet specified on the command line, read in a 
 *  sequence of characters over that alphabet (ignoring characters
 *  not in the alphabet), computes the frequency of occurrence of
 *  each character, and print out the results.
 *
 *  %  java Count ABCDR < abra.txt 
 *  A 5
 *  B 2
 *  C 1
 *  D 1
 *  R 2
 *
 *  % java Count 0123456789 < pi.txt
 *  0 99959
 *  1 99757
 *  2 100026
 *  3 100230
 *  4 100230
 *  5 100359
 *  6 99548
 *  7 99800
 *  8 99985
 *  9 100106
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;


/**
 *  The {@code Count} class provides an {@link Alphabet} client for reading
 *  in a piece of text and computing the frequency of occurrence of each
 *  character over a given alphabet.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/55compress">Section 5.5</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Count {

    // Do not instantiate.
    private Count() { }

    /**
     * Reads in text from standard input; calculates the frequency of
     * occurrence of each character over the alphabet specified as a
     * commmand-line argument; and prints the frequencies to standard
     * output.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        Alphabet alphabet = new Alphabet(args[0]);
        final int R = alphabet.radix();
        int[] count = new int[R];
        while (StdIn.hasNextChar()) {
            char c = StdIn.readChar();
            if (alphabet.contains(c))
                count[alphabet.toIndex(c)]++;
        }
        for (int c = 0; c < R; c++)
            StdOut.println(alphabet.toChar(c) + " " + count[c]);
    }
}


/******************************************************************************
 *  Copyright 2002-2020, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
