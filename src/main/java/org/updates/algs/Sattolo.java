/******************************************************************************
 *  Compilation:  javac Sattolo.java
 *  Execution:    java org.updates.algs.Sattolo < list.txt
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   src/main/resources/data/cards.txt
 *  
 *  Reads in a list of strings and prints a uniformly random cycle
 *  using Sattolo's algorithm under the assumption that Math.random()
 *  generates independent and uniformly distributed numbers between
 *  0 and 1.
 *
 *  %  echo 0 1 2 3 4 | java org.updates.algs.Sattolo
 *  1
 *  2
 *  4
 *  0
 *  3
 *
 ******************************************************************************/
package org.updates.algs;

import edu.princeton.cs.algs4.StdIn;

/**
 *  The {@code Sattolo} class provides a client for reading a sequence 
 *  of <em>n</em> strings and computing a <em>uniformly random cycle</em>
 *  of length <em>n</em> using Sattolo's algorithm.
 *  This algorithm guarantees to produce a uniformly random cycle under
 *  the assumption that {@code Math.random()} generates independent and
 *  uniformly distributed numbers between 0 and 1.
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/11model">Section 1.1</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author vahbuna
 */
public class Sattolo {
    
    private static String [] read(String [] input) {
        String data[] = input;
        // read from standard input
        if(input.length == 0) { 
            data = StdIn.readAllStrings();
        }
        return data;
    }
    
    public static void main(String args []) {
        String [] data = read(args);
        
        //main algo
        int i = data.length;
        while (i > 1) {
            i--;
            int j = (int)(Math.random() * i);  // 0 <= j < i-1
            //swap
            String temp = data[i];
            data[i] = data[j];
            data[j] = temp;
        }
        
        for(i = data.length-1; i>=0; i--) {
            System.out.println(data[i]);
        }
    }
}
