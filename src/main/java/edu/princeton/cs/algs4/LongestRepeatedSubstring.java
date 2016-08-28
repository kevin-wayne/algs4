/******************************************************************************
 *  Compilation:  javac LongestRepeatedSubstring.java
 *  Execution:    java LongestRepeatedSubstring < file.txt
 *  Dependencies: StdIn.java SuffixArray.java
 *  Data files:   http://algs4.cs.princeton.edu/63suffix/tale.txt
 *                http://algs4.cs.princeton.edu/63suffix/tinyTale.txt
 *                http://algs4.cs.princeton.edu/63suffix/mobydick.txt
 *  
 *  Reads a text string from stdin, replaces all consecutive blocks of
 *  whitespace with a single space, and then computes the longest
 *  repeated substring in that text using a suffix array.
 * 
 *  % java LongestRepeatedSubstring < tinyTale.txt 
 *  'st of times it was the '
 *
 *  % java LongestRepeatedSubstring < mobydick.txt
 *  ',- Such a funny, sporty, gamy, jesty, joky, hoky-poky lad, is the Ocean, oh! Th'
 * 
 *  % java LongestRepeatedSubstring
 *  aaaaaaaaa
 *  'aaaaaaaa'
 *
 *  % java LongestRepeatedSubstring
 *  abcdefg
 *  ''
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The {@code LongestRepeatedSubstring} class provides a {@link SuffixArray}
 *  client for computing the longest repeated substring of a string that
 *  appears at least twice. The repeated substrings may overlap (but must
 *  be distinct).
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/63suffix">Section 6.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *  <p>
 *  See also {@link LongestCommonSubstring}.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class LongestRepeatedSubstring {

    // Do not instantiate.
    private LongestRepeatedSubstring() { }

    /**
     * Returns the longest common string of the two specified strings.
     *
     * @param  s one string
     * @param  t the other string
     * @return the longest common string that appears as a substring
     */

    /**
     * Returns the longest repeated substring of the specified string.
     *
     * @param  text the string
     * @return the longest repeated substring that appears in {@code text};
     *         the empty string if no such string
     */
    public static String lrs(String text) {
        int n = text.length();
        SuffixArray sa = new SuffixArray(text);
        String lrs = "";
        for (int i = 1; i < n; i++) {
            int length = sa.lcp(i);
            if (length > lrs.length()) {
                // lrs = sa.select(i).substring(0, length);
                lrs = text.substring(sa.index(i), sa.index(i) + length);
            }
        }
        return lrs;
    }

    /**
     * Unit tests the {@code lrs()} method.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String text = StdIn.readAll().replaceAll("\\s+", " ");
        StdOut.println("'" + lrs(text) + "'");
    }
}

/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
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
