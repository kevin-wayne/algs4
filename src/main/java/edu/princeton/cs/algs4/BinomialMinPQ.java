/******************************************************************************
 *  Compilation: javac BinomialMinPQ.java
 *  Execution:
 *  
 *  A binomial heap.
 *  
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.Iterator;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 *  The BinomialMinPQ class represents a priority queue of generic keys.
 *  It supports the usual insert and delete-the-minimum operations, 
 *  along with the merging of two heaps together.
 *  It also supports methods for peeking at the minimum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  It is possible to build the priority queue using a Comparator.
 *  If not, the natural order relation between the keys will be used.
 *  
 *  This implementation uses a binomial heap.
 *  The insert, delete-the-minimum, union, min-key
 *  and size operations take logarithmic time.
 *  The is-empty and constructor operations take constant time.
 * 
 *  @author Tristan Claverie
 */
public class BinomialMinPQ<Key> implements Iterable<Key> {
	private Node head;    				//head of the list of roots
	private final Comparator<Key> comp;	//Comparator over the keys
	
	//Represents a Node of a Binomial Tree
	private class Node {
		Key key;						//Key contained by the Node
		int order;						//The order of the Binomial Tree rooted by this Node
		Node child, sibling;			//child and sibling of this Node
	}
	
	/**
	 * Initializes an empty priority queue
	 * Worst case is O(1)
	 */
	public BinomialMinPQ() {
		comp = new MyComparator();
	}
	
	/**
	 * Initializes an empty priority queue using the given Comparator
	 * Worst case is O(1)
	 * @param C a comparator over the keys
	 */
	public BinomialMinPQ(Comparator<Key> C) {
		comp = C;
	}
	
	/**
	 * Initializes a priority queue with given keys
	 * Worst case is O(n*log(n))
	 * @param a an array of keys
	 */
	public BinomialMinPQ(Key[] a) {
		comp = new MyComparator();
		for (Key k : a) insert(k);
	}
	
	/**
	 * Initializes a priority queue with given keys using the given Comparator
	 * Worst case is O(n*log(n))
	 * @param C a comparator over the keys
	 * @param a an array of keys
	 */
	public BinomialMinPQ(Comparator<Key> C, Key[] a) {
		comp = C;
		for (Key k : a) insert(k);
	}

	/**
	 * Whether the priority queue is empty
	 * Worst case is O(1)
	 * @return true if the priority queue is empty, false if not
	 */
	public boolean isEmpty() {
		return head == null;
	}

	/**
	 * Number of elements currently on the priority queue
	 * Worst case is O(log(n))
	 * @throws java.lang.ArithmeticException if there are more than 2^63-1 elements in the queue
	 * @return the number of elements on the priority queue
	 */
	public int size() {
		int result = 0, tmp;
		for (Node node = head; node != null; node = node.sibling) {
			if (node.order > 30) { throw new ArithmeticException("The number of elements cannot be evaluated, but the priority queue is still valid."); }
			tmp = 1 << node.order;
			result |= tmp;
		}
		return result;
	}

	/**
	 * Puts a Key in the heap
	 * Worst case is O(log(n))
	 * @param key a Key
	 */
	public void insert(Key key) {
		Node x = new Node();
		x.key = key;
		x.order = 0;
		BinomialMinPQ<Key> H = new BinomialMinPQ<Key>(); //The Comparator oh the H heap is not used
		H.head = x;
		this.head = this.union(H).head;
	}

	/**
	 * Get the minimum key currently in the queue
	 * Worst case is O(log(n))
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the minimum key currently in the priority queue
	 */
	public Key minKey() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		Node min = head;
		Node current = head;
		while (current.sibling != null) {
			min = (greater(min.key, current.sibling.key)) ? current : min;
			current = current.sibling;
		}
		return min.key;
	}

	/**
	 * Deletes the minimum key
	 * Worst case is O(log(n))
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the minimum key
	 */
	public Key delMin() {
		if(isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		Node min = eraseMin();
		Node x = (min.child == null) ? min : min.child;
		if (min.child != null) {
			min.child = null;
			Node prevx = null, nextx = x.sibling;
			while (nextx != null) {
				x.sibling = prevx;
				prevx = x;
				x = nextx;nextx = nextx.sibling;
			}
			x.sibling = prevx;
			BinomialMinPQ<Key> H = new BinomialMinPQ<Key>();
			H.head = x;
			head = union(H).head;
		}
		return min.key;
	}
	
	/**
	 * Merges two Binomial heaps together
	 * This operation is destructive
	 * Worst case is O(log(n))
	 * @param heap a Binomial Heap to be merged with the current heap
	 * @throws java.lang.IllegalArgumentException if the heap in parameter is null
	 * @return the union of two heaps
	 */
	public BinomialMinPQ<Key> union(BinomialMinPQ<Key> heap) {
		if (heap == null) throw new IllegalArgumentException("Cannot merge a Binomial Heap with null");
		this.head = merge(new Node(), this.head, heap.head).sibling;
		Node x = this.head;
		Node prevx = null, nextx = x.sibling;
		while (nextx != null) {
			if (x.order < nextx.order ||
			   (nextx.sibling != null && nextx.sibling.order == x.order)) {
				prevx = x; x = nextx;
			} else if (greater(nextx.key, x.key)) {
				x.sibling = nextx.sibling;
				link(nextx, x);
			} else {
				if (prevx == null) { this.head = nextx; }
				else { prevx.sibling = nextx; }
				link(x, nextx);
				x = nextx;
			}
			nextx = x.sibling;
		}
		return this;
	}
	
	/*************************************************
	 * General helper functions
	 ************************************************/
	
	//Compares two keys
	private boolean greater(Key n, Key m) {
		if (n == null) return false;
		if (m == null) return true;
		return comp.compare(n, m) > 0;
	}
	
	//Assuming root1 holds a greater key than root2, root2 becomes the new root
	private void link(Node root1, Node root2) {
		root1.sibling = root2.child;
		root2.child = root1;
		root2.order++;
	}
	
	//Deletes and return the node containing the minimum key
	private Node eraseMin() {
		Node min = head;
		Node previous = null;
		Node current = head;
		while (current.sibling != null) {
			if (greater(min.key, current.sibling.key)) {
				previous = current;
				min = current.sibling;
			}
			current = current.sibling;
		}
		previous.sibling = min.sibling;
		if (min == head) head = min.sibling;
		return min;
	}
	
	/**************************************************
	 * Functions for inserting a key in the heap
	 *************************************************/
	
	//Merges two root lists into one, there can be up to 2 Binomial Trees of same order
        private Node merge(Node h, Node x, Node y) {
            if (x == null && y == null) return h;
            else if (x == null) h.sibling = merge(y, null, y.sibling);
            else if (y == null) h.sibling = merge(x, x.sibling, null);
            else if (x.order < y.order) h.sibling = merge(x, x.sibling, y);
            else                        h.sibling = merge(y, x, y.sibling);
            return h;
	}
	
	/******************************************************************
	 * Iterator
	 *****************************************************************/
	
	/**
	 * Gets an Iterator over the keys in the priority queue in ascending order
	 * The Iterator does not implement the remove() method
	 * iterator() : Worst case is O(n)
	 * next() : 	Worst case is O(log(n))
	 * hasNext() : 	Worst case is O(1)
	 * @return an Iterator over the keys in the priority queue in ascending order
	 */
	public Iterator<Key> iterator() {
		return new MyIterator();
	}
	
	private class MyIterator implements Iterator<Key> {
		BinomialMinPQ<Key> data;
		
		//Constructor clones recursively the elements in the queue
		//It takes linear time
		public MyIterator() {
			data = new BinomialMinPQ<Key>(comp);
			data.head = clone(head, null);
		}
		
		private Node clone(Node x, Node parent) {
			if (x == null) return null;
			Node node = new Node();
			node.key = x.key;
			node.sibling = clone(x.sibling, parent);
			node.child = clone(x.child, node);
			return node;
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
