/**
 * **************************************************************************** Compilation: javac
 * Merge.java Execution: java Merge < input.txt Dependencies: StdOut.java StdIn.java Data files:
 * https://algs4.cs.princeton.edu/22mergesort/tiny.txt
 * https://algs4.cs.princeton.edu/22mergesort/words3.txt
 *
 * <p>Sorts a sequence of strings from standard input using mergesort.
 *
 * <p>% more tiny.txt S O R T E X A M P L E
 *
 * <p>% java Merge < tiny.txt A E E L M O P R S T X [ one string per line ]
 *
 * <p>% more words3.txt bed bug dad yes zoo ... all bad yet
 *
 * <p>% java Merge < words3.txt all bad bed bug dad ... yes yet zoo [ one string per line ]
 *
 * <p>****************************************************************************
 */
package edu.princeton.cs.algs4;

/**
 * The {@code Merge} class provides static methods for sorting an array using mergesort.
 *
 * <p>For additional documentation, see <a href="https://algs4.cs.princeton.edu/22mergesort">Section
 * 2.2</a> of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne. For an optimized
 * version, see {@link MergeX}.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class Merge {

  // This class should not be instantiated.
  private Merge() {}

  /**
   * Rearranges the array in ascending order, using the natural order.
   *
   * @param a the array to be sorted
   */
  public static void sort(Comparable[] a) {
    Comparable[] aux = new Comparable[a.length];
    sort(a, aux, 0, a.length - 1);
    assert isSorted(a);
  }

  // mergesort a[lo..hi] using auxiliary array aux[lo..hi]
  private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi) {
    if (hi <= lo) return;
    int mid = lo + (hi - lo) / 2;
    sort(a, aux, lo, mid);
    sort(a, aux, mid + 1, hi);
    merge(a, aux, lo, mid, hi);
  }

  // stably merge a[lo .. mid] with a[mid+1 ..hi] using aux[lo .. hi]
  private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
    // precondition: a[lo .. mid] and a[mid+1 .. hi] are sorted subarrays
    assert isSorted(a, lo, mid);
    assert isSorted(a, mid + 1, hi);

    // copy to aux[]
    for (int k = lo; k <= hi; k++) {
      aux[k] = a[k];
    }

    // merge back to a[]
    int i = lo, j = mid + 1;
    for (int k = lo; k <= hi; k++) {
      if (i > mid) a[k] = aux[j++];
      else if (j > hi) a[k] = aux[i++];
      else if (less(aux[j], aux[i])) a[k] = aux[j++];
      else a[k] = aux[i++];
    }

    // postcondition: a[lo .. hi] is sorted
    assert isSorted(a, lo, hi);
  }



  /**
   * ************************************************************************* Helper sorting
   * function. *************************************************************************
   */

  // is v < w ?
  private static boolean less(Comparable v, Comparable w) {
    return v.compareTo(w) < 0;
  }

  /**
   * ************************************************************************* Check if array is
   * sorted - useful for debugging.
   * *************************************************************************
   */
  private static boolean isSorted(Comparable[] a) {
    return isSorted(a, 0, a.length - 1);
  }

  private static boolean isSorted(Comparable[] a, int lo, int hi) {
    for (int i = lo + 1; i <= hi; i++) if (less(a[i], a[i - 1])) return false;
    return true;
  }

  // print array to standard output
  private static void show(Comparable[] a) {
    for (int i = 0; i < a.length; i++) {
      StdOut.println(a[i]);
    }
  }

  /**
   * Reads in a sequence of strings from standard input; mergesorts them; and prints them to
   * standard output in ascending order.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    String[] a = StdIn.readAllStrings();
    Merge.sort(a);
    show(a);
  }
}

/**
 * **************************************************************************** Copyright 2002-2018,
 * Robert Sedgewick and Kevin Wayne.
 *
 * <p>This file is part of algs4.jar, which accompanies the textbook
 *
 * <p>Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne, Addison-Wesley Professional,
 * 2011, ISBN 0-321-57351-X. http://algs4.cs.princeton.edu
 *
 * <p>algs4.jar is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * <p>algs4.jar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * <p>You should have received a copy of the GNU General Public License along with algs4.jar. If
 * not, see http://www.gnu.org/licenses.
 * ****************************************************************************
 */
