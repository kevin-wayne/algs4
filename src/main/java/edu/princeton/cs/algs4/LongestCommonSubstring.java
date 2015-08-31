/******************************************************************************
 *  Compilation:  javac LongestCommonSubstring.java
 *  Execution:    java  LongestCommonSubstring file1.txt file2.txt
 *  Dependencies: SuffixArray.java StdOut.java In.java
 *  
 *  Reads in two text strings, replaces all consecutive blocks of
 *  whitespace with a single space, and then computes the longest
 *  common substring.
 *
 *  Assumes that the character '\1' does not appear in either text.
 *  Perhaps, search for a character that does not appear in either text
 *  (and make sure SuffixArray.java doesn't choose the same one).
 * 
 *  % java LongestCommonSubstring tale.txt mobydick.txt
 *  ' seemed on the point of being '
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;


public class LongestCommonSubstring {

    // Do not instantiate.
    private LongestCommonSubstring() { }

    public static void main(String[] args) {

        // read in two string from two files
        In in1 = new In(args[0]);
        In in2 = new In(args[1]);
        String text1 = in1.readAll().trim().replaceAll("\\s+", " ");
        String text2 = in2.readAll().trim().replaceAll("\\s+", " ");
        int N1 = text1.length();
        // int N2 = text2.length();

        // concatenate two string with intervening '\1'
        String text  = text1 + '\1' + text2;
        int N  = text.length();

        // compute suffix array of concatenated text
        SuffixArray suffix = new SuffixArray(text);

        // search for longest common substring
        String lcs = "";
        for (int i = 1; i < N; i++) {

            // adjacent suffixes both from first text string
            if (suffix.index(i) < N1 && suffix.index(i-1) < N1) continue;

            // adjacent suffixes both from secondt text string
            if (suffix.index(i) > N1 && suffix.index(i-1) > N1) continue;

            // check if adjacent suffixes longer common substring
            int length = suffix.lcp(i);
            if (length > lcs.length()) {
                lcs = text.substring(suffix.index(i), suffix.index(i) + length);
            }
        }

        // print out longest common substring
        StdOut.println(lcs.length());
        StdOut.println("'" + lcs + "'");
    }
}

/******************************************************************************
 *  Copyright 2002-2015, Robert Sedgewick and Kevin Wayne.
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
