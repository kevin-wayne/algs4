/******************************************************************************
 *  Compilation:  javac LongestCommonSubstring.java
 *  Execution:    java  LongestCommonSubstring file1.txt file2.txt
 *  Dependencies: SuffixArray.java In.java StdOut.java
 *  
 *  Read in two text files and find the longest substring that
 *  appears in both texts.
 * 
 *  % java LongestCommonSubstring tale.txt mobydick.txt
 *  ' seemed on the point of being '
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The <tt>LongestCommonSubstring</tt> class provides a {@link SuffixArray}
 *  client for computing the longest common substring that appears in two
 *  given strings.
 *  <p>
 *  This implementation computes the suffix array of each string and applies a
 *  merging operation to determine the longest common substring.
 *  For an alternate implementation, see
 *  <a href = "http://algs4.cs.princeton.edu/63suffix/LongestCommonSubstringConcatenate.java.html">LongestCommonSubstringConcatenate.java</a>.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/63suffix">Section 6.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *  <p>
 *     
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class LongestCommonSubstring {

    // Do not instantiate.
    private LongestCommonSubstring() { }

    // return the longest common prefix of suffix s[p..] and suffix t[q..]
    private static String lcp(String s, int p, String t, int q) {
        int n = Math.min(s.length() - p, t.length() - q);
        for (int i = 0; i < n; i++) {
            if (s.charAt(p + i) != t.charAt(q + i))
                return s.substring(p, p + i);
        }
        return s.substring(p, p + n);
    }

    // compare suffix s[p..] and suffix t[q..]
    private static int compare(String s, int p, String t, int q) {
        int n = Math.min(s.length() - p, t.length() - q);
        for (int i = 0; i < n; i++) {
            if (s.charAt(p + i) != t.charAt(q + i))
                return s.charAt(p+i) - t.charAt(q+i);
        }
        if      (s.length() - p < t.length() - q) return -1;
        else if (s.length() - p > t.length() - q) return +1;
        else                                      return  0;
    }

    /**
     * Returns the longest common string of the two specified strings.
     *
     * @param  s one string
     * @param  t the other string
     * @return the longest common string that appears as a substring
     *         in both <tt>s</tt> and <tt>t</tt>; the empty string
     *         if no such string
     */
    public static String lcs(String s, String t) {
        SuffixArray suffix1 = new SuffixArray(s);
        SuffixArray suffix2 = new SuffixArray(t);

        // find longest common substring by "merging" sorted suffixes 
        String lcs = "";
        int i = 0, j = 0;
        while (i < s.length() && j < t.length()) {
            int p = suffix1.index(i);
            int q = suffix2.index(j);
            String x = lcp(s, p, t, q);
            if (x.length() > lcs.length()) lcs = x;
            if (compare(s, p, t, q) < 0) i++;
            else                         j++;
        }
        return lcs;
    }

    /**
     * Unit tests the <tt>lcs()</tt> method.
     * Reads in two strings from files specified as command-line arguments;
     * computes the longest common substring; and prints the results to
     * standard output.
     */
    public static void main(String[] args) {
        In in1 = new In(args[0]);
        In in2 = new In(args[1]);
        String s = in1.readAll().trim().replaceAll("\\s+", " ");
        String t = in2.readAll().trim().replaceAll("\\s+", " ");
        StdOut.println("'" + lcs(s, t) + "'");
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
