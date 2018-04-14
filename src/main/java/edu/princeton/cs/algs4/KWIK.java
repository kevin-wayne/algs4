/******************************************************************************
 *  Compilation:  javac KWIK.java
 *  Execution:    java KWIK file.txt
 *  Dependencies: StdIn.java StdOut.java In.java SuffixArray.java
 *  Data files:   https://algs4.cs.princeton.edu/63suffix/tale.txt
 *                https://algs4.cs.princeton.edu/63suffix/mobydick.txt 
 *
 *  Keyword-in-context search.
 *
 *  %  java KWIK tale.txt 15
 *  majesty
 *   most gracious majesty king george th
 *  rnkeys and the majesty of the law fir
 *  on against the majesty of the people 
 *  se them to his majestys chief secreta
 *  h lists of his majestys forces and of
 *
 *  the worst
 *  w the best and the worst are known to y
 *  f them give me the worst first there th
 *  for in case of the worst is a friend in
 *  e roomdoor and the worst is over then a
 *  pect mr darnay the worst its the wisest
 *  is his brother the worst of a bad race 
 *  ss in them for the worst of health for 
 *   you have seen the worst of her agitati
 *  cumwented into the worst of luck buuust
 *  n your brother the worst of the bad rac
 *   full share in the worst of the day pla
 *  mes to himself the worst of the strife 
 *  f times it was the worst of times it wa
 *  ould hope that the worst was over well 
 *  urage business the worst will be over i
 *  clesiastics of the worst world worldly 
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The {@code KWIK} class provides a {@link SuffixArray} client for computing
 *  all occurrences of a keyword in a given string, with surrounding context.
 *  This is known as <em>keyword-in-context search</em>.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/63suffix">Section 6.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class KWIK {

    // Do not instantiate.
    private KWIK() { }

    /**
     * Reads a string from a file specified as the first
     * command-line argument; read an integer k specified as the
     * second command line argument; then repeatedly processes
     * use queries, printing all occurrences of the given query
     * string in the text string with k characters of surrounding
     * context on either side.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        int context = Integer.parseInt(args[1]);

        // read in text
        String text = in.readAll().replaceAll("\\s+", " ");
        int n = text.length();

        // build suffix array
        SuffixArray sa = new SuffixArray(text);

        // find all occurrences of queries and give context
        while (StdIn.hasNextLine()) {
            String query = StdIn.readLine();
            for (int i = sa.rank(query); i < n; i++) {
                int from1 = sa.index(i);
                int to1   = Math.min(n, from1 + query.length());
                if (!query.equals(text.substring(from1, to1))) break;
                int from2 = Math.max(0, sa.index(i) - context);
                int to2   = Math.min(n, sa.index(i) + context + query.length());
                StdOut.println(text.substring(from2, to2));
            }
            StdOut.println();
        }
    } 
} 

/******************************************************************************
 *  Copyright 2002-2018, Robert Sedgewick and Kevin Wayne.
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
