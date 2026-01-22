/******************************************************************************
 *  Compilation:  javac TimSort.java
 *  Execution:    java TimSort < input.txt
 *  Dependencies: StdOut.java StdIn.java Insertion.java
 *  Data files:   https://algs4.cs.princeton.edu/23quicksort/tiny.txt
 *                https://algs4.cs.princeton.edu/23quicksort/words3.txt
 *
 *  A hybrid sorting algorithm derived from merge sort and insertion sort.
 *  It was designed to perform well on many kinds of real-world data.
 *
 *  % more tiny.txt
 *  S O R T E X A M P L E
 *
 *  % java TimSort < tiny.txt
 *  A E E L M O P R S T X                 [ one string per line ]
 *
 *  % more words3.txt
 *  bed bug dad yes zoo ... all bad yet
 *
 *  % java TimSort < words3.txt
 *  all bad bed bug dad ... yes yet zoo    [ one string per line ]
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 * The {@code TimSort} class provides static methods for sorting an
 * array using the TimSort algorithm, a hybrid stable sorting algorithm
 * derived from merge sort and insertion sort. It is designed to perform
 * efficiently on many kinds of real-world data.
 * <p>
 * This implementation takes &Theta;(<em>n</em> log <em>n</em>) time
 * to sort an array of length <em>n</em> in the worst case, but often performs
 * better on partially sorted arrays or arrays with certain predictable patterns.
 * The exact number of comparisons depends on the structure of the input data.
 * </p>
 * <p>
 * TimSort is stable and uses &Theta;(<em>n</em>) extra memory (not including the
 * input array). It is well-suited for large datasets and is used in the Java standard
 * library for sorting objects.
 * </p>
 * <p>
 * For additional documentation on the algorithm, see
 * <a href="https://en.wikipedia.org/wiki/Timsort">Timsort on Wikipedia</a>.
 * </p>
 *
 * @author Max Loshak
 */
public class TimSort {
    static int MIN_RUN = 32;  // default minimum size of a run

    // This class should not be instantiated.
    private TimSort() { }

    /**
     * Computes the minimum length of a run to be sorted using insertion sort in Timsort.
     *
     * @param n The length of the array or subarray to be sorted.
     * @return The minimum run length. Runs shorter than this length will be sorted using insertion sort.
     *
     * <p>The method calculates the minimum run length by considering the bit representation of the array
     * length (n). It repeatedly right-shifts n by 1 until n is less than {@code MIN_MERGE}. During each
     * shift, if the least significant bit of n is 1, it is 'or-ed' into the result (r). The method returns
     * the sum of the remaining value of n and r. This approach ensures a balance between the number of runs
     * and their size, optimizing the performance of Timsort.</p>
     *
     * <p>Time Complexity: O(log n), where n is the length of the array or subarray to be sorted.</p>
     */
    public static int runLength(int n)
    {
        assert n >= 0;
        int r = 0;
        while (n >= MIN_RUN) {
            r |= (n & 1);
            n >>= 1;
        }
        return n + r;
    }

    /**
     * Merges two sorted subarrays of {@code arr}.
     * The first subarray is {@code arr[l..m]} and the second subarray is {@code arr[m+1..r]}.
     * After the merge, the subarray {@code arr[l..r]} is sorted.
     *
     * @param arr The array of Comparable elements to be merged.
     * @param l   The starting index of the first subarray to be merged.
     * @param m   The ending index of the first subarray. This also separates the
     *            two subarrays to be merged, with the second subarray starting at {@code m + 1}.
     * @param r   The ending index of the second subarray to be merged.
     *
     * <p>This method performs the merge operation in the merge sort algorithm. It
     * combines two contiguous, sorted subarrays within the larger array into a single
     * sorted subarray. This is achieved by comparing the elements of the subarrays
     * and copying them back into the original array in sorted order. Additional
     * arrays are used to hold the elements of the subarrays during the merge process.</p>
     *
     * <p>Time Complexity: O(n), where n is the number of elements in the merged subarray.</p>
     */
    public static void merge(Comparable[] arr, int l, int m, int r)
    {
        int size1 = m - l + 1, size2 = r - m;
        Comparable[] left = new Comparable[size1];
        Comparable[] right = new Comparable[size2];
        System.arraycopy(arr, l, left, 0, size1);
        for (int x = 0; x < size2; x++) {
            right[x] = arr[m + 1 + x];
        }
        int i = 0;
        int j = 0;
        int k = l;

        // merging into larger subarray after comparing
        while (i < size1 && j < size2) {
            if (!less(right[j],left[i])) {
                arr[k] = left[i];
                i++;
            }
            else {
                arr[k] = right[j];
                j++;
            }
            k++;
        }
        // Copy remaining elements of left and right, if any
        while (i < size1) {
            arr[k] = left[i];
            k++;
            i++;
        }
        while (j < size2) {
            arr[k] = right[j];
            k++;
            j++;
        }
    }

    /**
     * Rearranges the full array in ascending order, using the natural order.
     * @param arr the array to be sorted
     */
    public static void sort(Comparable[] arr) {
        int n = arr.length;
        sort(arr, 0, n);
        assert isSorted(arr);
    }

    /**
     * Iterative Timsort function to sort the part of array [..,lo,...,hi,..] in ascending order.
     * @param arr the array to be sorted
     * @param lo left endpoint (inclusive)
     * @param hi right endpoint (exclusive)
     *
     * <p>This method implements the Timsort algorithm, which is optimized for real-world data
     * that often contains ordered subsequences. It first sorts small segments of the array
     * using insertion sort and then efficiently merges these segments. The merging of segments
     * is done in a manner that optimizes the number of comparisons and moves, making Timsort
     * faster and more efficient than traditional mergesort on real-world data.</p>
     *
     * <p>Time Complexity: O(n log n), where n is the number of elements in the range [lo, hi).</p>
     */
    public static void sort(Comparable[] arr, int lo, int hi)
    {
        int minRun = runLength(MIN_RUN);

        // Sort individual subarrays of size RUN
        for (int i = lo; i < hi; i += minRun) {
            Insertion.sort(
                    arr, i,
                    Math.min((i + MIN_RUN - 1), (hi)));
        }

        // Start merging from size RUN (32). It will merge to form size 64, 128 and so on
        for (int size = minRun; size < hi; size = 2 * size) {

            for (int left = 0; left < hi; left += 2 * size) {
                int mid = left + size - 1;
                int right = Math.min((left + 2 * size - 1),
                        (hi - 1));

                // Merge arr[left.....mid] and arr[mid+1....right]
                if (mid < right)
                    merge(arr, left, mid, right);
            }
        }

        assert isSorted(arr, lo, hi - 1);
    }


    /***************************************************************************
     *  Helper sorting functions.
     ***************************************************************************/

    // is v < w ?
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }


    /***************************************************************************
     *  Check if array is sorted - useful for debugging.
     ***************************************************************************/
    private static boolean isSorted(Comparable[] a) {
        return isSorted(a, 0, a.length - 1);
    }

    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++)
            if (less(a[i], a[i-1])) return false;
        return true;
    }


    // print array to standard output
    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            StdOut.println(a[i]);
        }
    }

    /**
     * Reads in a sequence of strings from standard input; heapsorts them;
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        TimSort.sort(a);
        show(a);
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
