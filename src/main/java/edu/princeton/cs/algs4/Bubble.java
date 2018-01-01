/******************************************************************************
 *  Compilation:  javac Bubble.java
 *  Execution:    java  Bubble N
 *  Dependencies: StdOut.java
 *
 *  Read strings from standard input and bubblesort them.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The {@code Bubble} class provides static methods for sorting an
 *  array using bubble sort.
 *  <p>
 *  This implementation makes ~ 1/2 n^2 compares and exchanges in
 *  the worst case, so it is not suitable for sorting large arbitrary arrays.
 *  Bubble sort is seldom useful because it is substantially slower than
 *  insertion sort on most inputs. The one class of inputs where bubble sort
 *  might be faster than insertion sort is arrays for which only
 *  a few passes of bubble sort are needed. This includes sorted arrays,
 *  but it does not include most partially-sorted arrays; for example,
 *  bubble sort takes quadratic time to sort arrays of the form
 *  [n, 1, 2, 3, 4, ..., n-1], whereas insertion sort takes linear time on
 *  such inputs.
 *  <p>
 *  The sorting algorithm is stable and uses O(1) extra memory.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/21elementary">Section 2.1</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Bubble {

   // This class should not be instantiated.
    private Bubble() { }

    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    public static <Key extends Comparable<Key>> void sort(Key[] a) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            int exchanges = 0;
            for (int j = n-1; j > i; j--) {
                if (less(a[j], a[j-1])) {
                    exch(a, j, j-1);
                    exchanges++;
                }
            }
            if (exchanges == 0) break;
        }
    }

    // is v < w ?
    private static <Key extends Comparable<Key>> boolean less(Key v, Key w) {
        return v.compareTo(w) < 0;
    }

    // exchange a[i] and a[j]
    private static <Key extends Comparable<Key>> void exch(Key[] a, int i, int j) {
        Key swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

   // print array to standard output
    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            StdOut.println(a[i]);
        }
    }

    /**
     * Reads in a sequence of strings from standard input; bubble sorts them;
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        Bubble.sort(a);
        show(a);
    }
}
