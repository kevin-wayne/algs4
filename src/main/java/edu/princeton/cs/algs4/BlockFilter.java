/******************************************************************************
 *  Compilation:  javac BlockFilter.java
 *  Execution:    java BlockFilter blocklist.txt < input.txt
 *  Dependencies: SET In.java StdIn.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/35applications/tinyTale.txt
 *                https://algs4.cs.princeton.edu/35applications/blocklist.txt
 *
 *  Read in a blocklist of words from a file. Then read in a list of
 *  words from standard input and print out all those words that
 *  are not in the first file.
 *
 *  % more tinyTale.txt
 *  it was the best of times it was the worst of times
 *  it was the age of wisdom it was the age of foolishness
 *  it was the epoch of belief it was the epoch of incredulity
 *  it was the season of light it was the season of darkness
 *  it was the spring of hope it was the winter of despair
 *
 *  % more list.txt
 *  was it the of
 *
 *  % java BlockFilter list.txt < tinyTale.txt
 *  best times worst times
 *  age wisdom age foolishness
 *  epoch belief epoch incredulity
 *  season light season darkness
 *  spring hope winter despair
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The {@code BlockFilter} class provides a client for reading in a <em>blocklist</em>
 *  of words from a file; then, reading in a sequence of words from standard input,
 *  printing out each word that <em>does not</em> appear in the file.
 *  It is useful as a test client for various symbol table implementations.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/35applications">Section 3.5</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class BlockFilter {

    // Do not instantiate.
    private BlockFilter() { }

    public static void main(String[] args) {
        SET<String> set = new SET<String>();

        // read in strings and add to set
        In in = new In(args[0]);
        while (!in.isEmpty()) {
            String word = in.readString();
            set.add(word);
        }

        // read in string from standard input, printing out all exceptions
        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            if (!set.contains(word))
                StdOut.println(word);
        }
    }
}

/******************************************************************************
 *  Copyright 2002-2022, Robert Sedgewick and Kevin Wayne.
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
