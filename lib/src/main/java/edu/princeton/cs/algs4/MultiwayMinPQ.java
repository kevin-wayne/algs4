/******************************************************************************
 *  Compilation: javac MultiwayMinPQ.java   
 *  Execution:
 *  
 *  A multiway heap.
 *  
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.Iterator;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 *  The MultiwayMinPQ class represents a priority queue of generic keys.
 *  It supports the usual insert and delete-the-minimum operations.
 *  It also supports methods for peeking at the minimum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  It is possible to build the priority queue using a Comparator.
 *  If not, the natural order relation between the keys will be used.
 *  
 *  This implementation uses a multiway heap.
 *  For simplified notations, logarithm in base d will be referred as log-d
 *  The delete-the-minimum operation takes time proportional to d*log-d(n)
 *  The insert takes time proportional to log-d(n)
 *  The is-empty, min-key and size operations take constant time.
 *  Constructor takes time proportional to the specified capacity.
 *
 *  @author Tristan Claverie
 */
public class MultiwayMinPQ<Key> implements Iterable<Key> {
	private final int d; 				//Dimension of the heap
	private int n;						//Number of keys currently in the heap
	private int order;					//Number of levels of the tree
	private Key[] keys;					//Array of keys
	private final Comparator<Key> comp;	//Comparator over the keys
	
	
    /**
     * Initializes an empty priority queue
     * Worst case is O(d)
     *
     * @param  d dimension of the heap
     * @throws java.lang.IllegalArgumentException if {@code d < 2}
     */
	public MultiwayMinPQ(int d) {
		if (d < 2) throw new IllegalArgumentException("Dimension should be 2 or over");
		this.d = d;
		order = 1;
		keys = (Key[]) new Comparable[d << 1];
		comp = new MyComparator();
	}
	
    /**
     * Initializes an empty priority queue
     * Worst case is O(d)
     *
     * @param  d dimension of the heap
     * @param  comparator a Comparator over the keys
     * @throws java.lang.IllegalArgumentException if {@code d < 2}
     */
	public MultiwayMinPQ(Comparator<Key> comparator, int d) {
		if (d < 2) throw new IllegalArgumentException("Dimension should be 2 or over");
		this.d = d;
		order = 1;
		keys = (Key[]) new Comparable[d << 1];
		comp = comparator;
	}
	
    /**
     * Initializes a priority queue with given indexes
     * Worst case is O(n*log-d(n))
     *
     * @param  d dimension of the heap
     * @param  a an array of keys
     * @throws java.lang.IllegalArgumentException if {@code d < 2}
     */
	public MultiwayMinPQ(Key[] a, int d) {
		if (d < 2) throw new IllegalArgumentException("Dimension should be 2 or over");
		this.d = d;
		order = 1;
		keys = (Key[]) new Comparable[d << 1];
		comp = new MyComparator();
		for (Key key : a) insert(key);
	}
	
    /**
     * Initializes a priority queue with given indexes
     * Worst case is O(a*log-d(n))
     *
     * @param  d dimension of the heap
     * @param  comparator a Comparator over the keys
     * @param  a an array of keys
     * @throws java.lang.IllegalArgumentException if {@code d < 2}
     */
	public MultiwayMinPQ(Comparator<Key> comparator, Key[] a, int d) {
		if (d < 2) throw new IllegalArgumentException("Dimension should be 2 or over");
		this.d = d;
		order = 1;
		keys = (Key[]) new Comparable[d << 1];
		comp = comparator;
		for (Key key : a) insert(key);
	}

        /**
	 * Whether the priority queue is empty
	 * Worst case is O(1)
	 * @return true if the priority queue is empty, false if not
	 */
	public boolean isEmpty() {
		return n == 0;
	}

	/**
	 * Number of elements currently on the priority queue
	 * Worst case is O(1)
	 * @return the number of elements on the priority queue
	 */
	public int size() {
		return n;
	}

	/**
	 * Puts a Key on the priority queue
	 * Worst case is O(log-d(n))
	 * @param key a Key
	 */
	public void insert(Key key) {
		keys[n+d] = key;
		swim(n++);
		if (n == keys.length-d) {
			resize(getN(order+1)+d);
			order++;
		}
	}

	/**
	 * Gets the minimum key currently in the queue
	 * Worst case is O(1)
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the minimum key currently in the priority queue
	 */
	public Key minKey() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		return keys[d];
	}

	/**
	 * Deletes the minimum key
	 * Worst case is O(d*log-d(n))
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the minimum key
	 */
	public Key delMin() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		exch(0, --n);
		sink(0);
		Key min = keys[n+d];
		keys[n+d] = null;
		int number = getN(order-2);
		if(order > 1 && n == number)  {
			resize(number+(int)Math.pow(d, order-1)+d);
			order--;
		}
		return min;
	}
	
	/***************************
	 * General helper functions
	 **************************/
	
	//Compares two keys
	private boolean greater(int x, int y) {
		int i = x+d, j = y+d;
		if (keys[i] == null) return false;
		if (keys[j] == null) return true;
		return comp.compare(keys[i], keys[j]) > 0;
	}
	
	//Exchanges the position of two keys
	private void exch(int x, int y) {
		int i = x+d, j = y+d;
		Key swap = keys[i];
		keys[i] = keys[j];
		keys[j] = swap;
	}
	
	//Gets the maximum number of keys in the heap, given the number of levels of the tree
	private int getN(int order) {
		return (1-((int)Math.pow(d, order+1)))/(1-d);
	}
	
	/***************************
	 * Functions for moving upward or downward
	 **************************/
	
	//Moves upward
	private void swim(int i) {
		if (i > 0 && greater((i-1)/d, i)) {
			exch(i, (i-1)/d);
			swim((i-1)/d);
		}
	}
	
	//Moves downward
	private void sink(int i) {
		int child = d*i+1;
		if (child >= n) return;
		int min = minChild(i);
		while (min < n && greater(i, min)) {
			exch(i, min);
			i = min;
			min = minChild(i);
		}
	}
	
	/***************************
	 * Deletes the minimum child
	 **************************/
	
	//Return the minimum child of i
	private int minChild(int i) {
		int loBound = d*i+1, hiBound = d*i+d;
		int min = loBound;
		for (int cur = loBound; cur <= hiBound; cur++) {
			if (cur < n && greater(min, cur)) min = cur;
		}
		return min;
	}
	
	/***************************
	 * Resize the priority queue
	 **************************/
	
	//Resizes the array containing the keys
	//If the heap is full, it adds one floor
	//If the heap has two floors empty, it removes one
	private void resize(int N) {
		Key[] array = (Key[]) new Comparable[N];
		for (int i = 0; i < Math.min(keys.length, array.length); i++) {
			array[i] = keys[i];
			keys[i] = null;
		}
		keys = array;
	}
	
	/***************************
	 * Iterator
	 **************************/
	
	/**
	 * Gets an Iterator over the keys in the priority queue in ascending order
	 * The Iterator does not implement the remove() method
	 * iterator() : Worst case is O(n)
	 * next() : 	Worst case is O(d*log-d(n))
	 * hasNext() : 	Worst case is O(1)
	 * @return an Iterator over the keys in the priority queue in ascending order
	 */
	
	public Iterator<Key> iterator() {
		return new MyIterator();
	}
	
	//Constructs an Iterator over the keys in linear time
	private class MyIterator implements Iterator<Key> {
		MultiwayMinPQ<Key> data;
		
		public MyIterator() {
			data = new MultiwayMinPQ<Key>(comp, d);
			data.keys = (Key[]) new Comparable[keys.length];
			data.n = n;
			for (int i = 0; i < keys.length; i++) {
				data.keys[i] = keys[i];
			}
		}

		public boolean hasNext() {
			return !data.isEmpty();
		}
		
		public Key next() {
                        if (!hasNext()) throw new NoSuchElementException();
			return data.delMin();
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	/***************************
	 * Comparator
	 **************************/
	
	//default Comparator
	private class MyComparator implements Comparator<Key> {
		@Override
		public int compare(Key key1, Key key2) {
			return ((Comparable<Key>) key1).compareTo(key2);
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
