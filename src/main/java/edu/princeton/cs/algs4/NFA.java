/******************************************************************************
 *  Compilation:  javac NFA.java
 *  Execution:    java NFA regexp text
 *  Dependencies: Stack.java Bag.java Digraph.java DirectedDFS.java
 *
 *  % java NFA "(A*B|AC)D" AAAABD
 *  true
 *
 *  % java NFA "(A*B|AC)D" AAAAC
 *  false
 *
 *  % java NFA "(a|(bc)*d)*" abcbcd
 *  true
 *
 *  % java NFA "(a|(bc)*d)*" abcbcbcdaaaabcbcdaaaddd
 *  true
 *
 *  Remarks
 *  -----------
 *  The following features are not supported:
 *    - The + operator
 *    - Multiway or
 *    - Metacharacters in the text
 *    - Character classes.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The <tt>NFA</tt> class provides a data type for creating a
 *  <em>nondeterministic finite state automaton</em> (NFA) from a regular
 *  expression and testing whether a given string is matched by that regular
 *  expression.
 *  It supports the following operations: <em>concatenation</em>,
 *  <em>closure</em>, <em>binary or</em>, and <em>parentheses</em>.
 *  It does not support <em>mutiway or</em>, <em>character classes</em>,
 *  <em>metacharacters</em> (either in the text or pattern),
 *  <em>capturing capabilities</em>, <em>greedy</em> or <em>relucantant</em>
 *  modifiers, and other features in industrial-strength implementations
 *  such as {@link java.util.regex.Pattern} and {@link java.util.regex.Matcher}.
 *  <p>
 *  This implementation builds the NFA using a digraph and a stack
 *  and simulates the NFA using digraph search (see the textbook for details).
 *  The constructor takes time proportional to <em>M</em>, where <em>M</em>
 *  is the number of characters in the regular expression.
 *  The <em>recognizes</em> method takes time proportional to <em>M N</em>,
 *  where <em>N</em> is the number of characters in the text.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/54regexp">Section 5.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class NFA { 

    private Digraph G;         // digraph of epsilon transitions
    private String regexp;     // regular expression
    private int M;             // number of characters in regular expression

    /**
     * Initializes the NFA from the specified regular expression.
     *
     * @param  regexp the regular expression
     */
    public NFA(String regexp) {
        this.regexp = regexp;
        M = regexp.length();
        Stack<Integer> ops = new Stack<Integer>(); 
        G = new Digraph(M+1); 
        for (int i = 0; i < M; i++) { 
            int lp = i; 
            if (regexp.charAt(i) == '(' || regexp.charAt(i) == '|') 
                ops.push(i); 
            else if (regexp.charAt(i) == ')') {
                int or = ops.pop(); 

                // 2-way or operator
                if (regexp.charAt(or) == '|') { 
                    lp = ops.pop();
                    G.addEdge(lp, or+1);
                    G.addEdge(or, i);
                }
                else if (regexp.charAt(or) == '(')
                    lp = or;
                else assert false;
            } 

            // closure operator (uses 1-character lookahead)
            if (i < M-1 && regexp.charAt(i+1) == '*') { 
                G.addEdge(lp, i+1); 
                G.addEdge(i+1, lp); 
            } 
            if (regexp.charAt(i) == '(' || regexp.charAt(i) == '*' || regexp.charAt(i) == ')') 
                G.addEdge(i, i+1);
        }
        if (ops.size() != 0)
            throw new IllegalArgumentException("Invalid regular expression");
    } 

    /**
     * Returns true if the text is matched by the regular expression.
     * 
     * @param  txt the text
     * @return <tt>true</tt> if the text is matched by the regular expression,
     *         <tt>false</tt> otherwise
     */
    public boolean recognizes(String txt) {
        DirectedDFS dfs = new DirectedDFS(G, 0);
        Bag<Integer> pc = new Bag<Integer>();
        for (int v = 0; v < G.V(); v++)
            if (dfs.marked(v)) pc.add(v);

        // Compute possible NFA states for txt[i+1]
        for (int i = 0; i < txt.length(); i++) {
            if (txt.charAt(i) == '*' || txt.charAt(i) == '|' || txt.charAt(i) == '(' || txt.charAt(i) == ')')
                throw new IllegalArgumentException("text contains the metacharacter '" + txt.charAt(i) + "'");

            Bag<Integer> match = new Bag<Integer>();
            for (int v : pc) {
                if (v == M) continue;
                if ((regexp.charAt(v) == txt.charAt(i)) || regexp.charAt(v) == '.')
                    match.add(v+1); 
            }
            dfs = new DirectedDFS(G, match); 
            pc = new Bag<Integer>();
            for (int v = 0; v < G.V(); v++)
                if (dfs.marked(v)) pc.add(v);

            // optimization if no states reachable
            if (pc.size() == 0) return false;
        }

        // check for accept state
        for (int v : pc)
            if (v == M) return true;
        return false;
    }

    /**
     * Unit tests the <tt>NFA</tt> data type.
     */
    public static void main(String[] args) {
        String regexp = "(" + args[0] + ")";
        String txt = args[1];
        NFA nfa = new NFA(regexp);
        StdOut.println(nfa.recognizes(txt));
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
