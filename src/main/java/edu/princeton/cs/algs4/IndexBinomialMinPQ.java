/******************************************************************************
 *  Compilation: javac IndexBinomialMinPQ.java
 *  Execution:
 *  
 *  An index binomial heap.
 *  
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  The IndexBinomialMinPQ class represents an indexed priority queue of generic keys.
 *  It supports the usual insert and delete-the-minimum operations,
 *  along with delete and change-the-key methods. 
 *  In order to let the client refer to keys on the priority queue,
 *  an integer between 0 and N-1 is associated with each key ; the client
 *  uses this integer to specify which key to delete or change.
 *  It also supports methods for peeking at the minimum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  
 *  This implementation uses a binomial heap along with an array to associate
 *  keys with integers in the given range.
 *  The insert, delete-the-minimum, delete, change-key, decrease-key,
 *  increase-key and size operations take logarithmic time.
 *  The is-empty, min-index, min-key, and key-of operations take constant time.
 *  Construction takes time proportional to the specified capacity.
 *
 *  @author Tristan Claverie
 */
public class IndexBinomialMinPQ<Key> implements Iterable<Integer> {
	private Node<Key> head;    			//Head of the list of roots
	private Node<Key>[] nodes; 			//Array of indexed Nodes of the heap
	private int n;			   		//Maximum size of the tree
	private final Comparator<Key> comparator;	//Comparator over the keys
	
	//Represents a node of a Binomial Tree
	private class Node<Key> {
		Key key;				//Key contained by the Node
		int order;				//The order of the Binomial Tree rooted by this Node
		int index;				//Index associated with the Key
		Node<Key> parent;			//parent of this Node
		Node<Key> child, sibling;		//child and sibling of this Node
	}
	
    /**
     * Initializes an empty indexed priority queue with indices between {@code 0} to {@code N-1}
     * Worst case is O(n)
     * @param N number of keys in the priority queue, index from {@code 0} to {@code N-1}
     * @throws java.lang.IllegalArgumentException if {@code N < 0}
     */
	public IndexBinomialMinPQ(int N) {
		if (N < 0) throw new IllegalArgumentException("Cannot create a priority queue of negative size");
		comparator = new MyComparator();
		nodes = (Node<Key>[]) new Node[N];
		this.n = N;
	}
	
    /**
     * Initializes an empty indexed priority queue with indices between {@code 0} to {@code N-1}
     * Worst case is O(n)
     * @param N number of keys in the priority queue, index from {@code 0} to {@code N-1}
     * @param comparator a Comparator over the keys
     * @throws java.lang.IllegalArgumentException if {@code N < 0}
     */
	public IndexBinomialMinPQ(int N, Comparator<Key> comparator) {
		if (N < 0) throw new IllegalArgumentException("Cannot create a priority queue of negative size");
		this.comparator = comparator;
		nodes = (Node<Key>[]) new Node[N];
		this.n = N;
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
	 * Does the priority queue contains the index i ?
	 * Worst case is O(1)
	 * @param i an index
	 * @throws java.lang.IllegalArgumentException if the specified index is invalid
	 * @return true if i is on the priority queue, false if not
	 */
	public boolean contains(int i) {
		if (i < 0 || i >= n) throw new IllegalArgumentException();
		else return nodes[i] != null;
	}

	/**
	 * Number of elements currently on the priority queue
	 * Worst case is O(log(n))
	 * @return the number of elements on the priority queue
	 */
	public int size() {
		int result = 0, tmp;
		for (Node<Key> node = head; node != null; node = node.sibling) {
			if (node.order > 30) { throw new ArithmeticException("The number of elements cannot be evaluated, but the priority queue is still valid."); }
			tmp =  1 << node.order;
			result |= tmp;
		}
		return result;
	}

	/**
	 * Associates a key with an index
	 * Worst case is O(log(n))
	 * @param i an index
	 * @param key a Key associated with i
	 * @throws java.lang.IllegalArgumentException if the specified index is invalid
	 * @throws java.lang.IllegalArgumentException if the index is already in the queue
	 */
	public void insert(int i, Key key) {
		if (i < 0 || i >= n) throw new IllegalArgumentException();
		if (contains(i)) throw new IllegalArgumentException("Specified index is already in the queue");
		Node<Key> x = new Node<Key>();
		x.key = key;
		x.index = i;
		x.order = 0;
		nodes[i] = x;
		IndexBinomialMinPQ<Key> H = new IndexBinomialMinPQ<Key>();
		H.head = x;
		head = union(H).head;
	}

	/**
	 * Gets the index associated with the minimum key
	 * Worst case is O(log(n))
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the index associated with the minimum key
	 */
	
	public int minIndex() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		Node<Key> min = head;
		Node<Key> current = head;
		while (current.sibling != null) {
			min = (greater(min.key, current.sibling.key)) ? current.sibling : min;
			current = current.sibling;
		}
		return min.index;
	}

	/**
	 * Gets the minimum key currently in the queue
	 * Worst case is O(log(n))
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the minimum key currently in the priority queue
	 */
	
	public Key minKey() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		Node<Key> min = head;
		Node<Key> current = head;
		while (current.sibling != null) {
			min = (greater(min.key, current.sibling.key)) ? current.sibling : min;
			current = current.sibling;
		}
		return min.key;
	}

	/**
	 * Deletes the minimum key
	 * Worst case is O(log(n))
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the index associated with the minimum key
	 */
	
	public int delMin() {
		if(isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		Node<Key> min = eraseMin();
		Node<Key> x = (min.child == null) ? min : min.child;
		if (min.child != null) {
			min.child = null;
			Node<Key> prevx = null, nextx = x.sibling;
			while (nextx != null) {
				x.parent = null; // for garbage collection
				x.sibling = prevx;
				prevx = x;
				x = nextx;nextx = nextx.sibling;
			}
			x.parent = null;
			x.sibling = prevx;
			IndexBinomialMinPQ<Key> H = new IndexBinomialMinPQ<Key>();
			H.head = x;
			head = union(H).head;
		}
		return min.index;
	}

	/**
	 * Gets the key associated with index i
	 * Worst case is O(1)
	 * @param i an index
	 * @throws java.lang.IllegalArgumentException if the specified index is invalid
	 * @throws java.lang.IllegalArgumentException if the index is not in the queue
	 * @return the key associated with index i
	 */
	
	public Key keyOf(int i) {
		if (i < 0 || i >= n) throw new IllegalArgumentException();
		if (!contains(i)) throw new IllegalArgumentException("Specified index is not in the queue");
		return nodes[i].key;
	}

	/**
	 * Changes the key associated with index i to the given key
	 * Worst case is O(log(n))
	 * @param i an index
	 * @param key the key to associate with i
	 * @throws java.lang.IllegalArgumentException if the specified index is invalid
	 * @throws java.lang.IllegalArgumentException if the index has no key associated with
	 */
	
	public void changeKey(int i, Key key) {
		if (i < 0 || i >= n) 		throw new IllegalArgumentException();
		if (!contains(i))			throw new IllegalArgumentException("Specified index is not in the queue");
		if (greater(nodes[i].key, key))  decreaseKey(i, key);
		else 							 increaseKey(i, key);
	}

	/**
	 * Decreases the key associated with index i to the given key
	 * Worst case is O(log(n))
	 * @param i an index
	 * @param key the key to associate with i
	 * @throws java.lang.IllegalArgumentException if the specified index is invalid
	 * @throws java.util.NoSuchElementException if the index has no key associated with
	 * @throws java.lang.IllegalArgumentException if the given key is greater than the current key
	 */
	
	public void decreaseKey(int i, Key key) {
		if (i < 0 || i >= n) 		throw new IllegalArgumentException();
		if (!contains(i))			throw new NoSuchElementException("Specified index is not in the queue");
		if (greater(key, nodes[i].key))  throw new IllegalArgumentException("Calling with this argument would not decrease the key");
		Node<Key> x = nodes[i];
		x.key = key;
		swim(i);
	}

	/**
	 * Increases the key associated with index i to the given key
	 * Worst case is O(log(n))
	 * @param i an index
	 * @param key the key to associate with i
	 * @throws java.lang.IllegalArgumentException if the specified index is invalid
	 * @throws java.util.NoSuchElementException if the index has no key associated with
	 * @throws java.lang.IllegalArgumentException if the given key is lower than the current key
	 */
	
	public void increaseKey(int i, Key key) {
		if (i < 0 || i >= n) 		throw new IllegalArgumentException();
		if (!contains(i))			throw new NoSuchElementException("Specified index is not in the queue");
		if (greater(nodes[i].key, key))  throw new IllegalArgumentException("Calling with this argument would not increase the key");
		delete(i);
		insert(i, key);
	}

	/**
	 * Deletes the key associated the given index
	 * Worst case is O(log(n))
	 * @param i an index
	 * @throws java.lang.IllegalArgumentException if the specified index is invalid
	 * @throws java.util.NoSuchElementException if the given index has no key associated with
	 */
	
	public void delete(int i) {
		if (i < 0 || i >= n) 		throw new IllegalArgumentException();
		if (!contains(i))			throw new NoSuchElementException("Specified index is not in the queue");
		toTheRoot(i);
		Node<Key> x = erase(i);
		if (x.child != null) {
			Node<Key> y = x;
			x = x.child;
			y.child = null;
			Node<Key> prevx = null, nextx = x.sibling;
			while (nextx != null) {
				x.parent = null;
				x.sibling = prevx;
				prevx = x;
				x = nextx; nextx = nextx.sibling;
			}
			x.parent = null;
			x.sibling = prevx;
			IndexBinomialMinPQ<Key> H = new IndexBinomialMinPQ<Key>();
			H.head = x;
			head = union(H).head;
		}
	}
	
	/*************************************************
	 * General helper functions
	 ************************************************/
	
	//Compares two keys
	private boolean greater(Key n, Key m) {
		if (n == null) return false;
		if (m == null) return true;
		return comparator.compare(n, m) > 0;
	}
	
	//Exchanges the positions of two nodes
	private void exchange(Node<Key> x, Node<Key> y) {
		Key tempKey = x.key; x.key = y.key; y.key = tempKey;
		int tempInt = x.index; x.index = y.index; y.index = tempInt;
		nodes[x.index] = x;
		nodes[y.index] = y;
	}
	
	//Assuming root1 holds a greater key than root2, root2 becomes the new root
	private void link(Node<Key> root1, Node<Key> root2) {
		root1.sibling = root2.child;
		root1.parent = root2;
		root2.child = root1;
		root2.order++;
	}
	
	/*************************************************
	 * Functions for moving upward
	 ************************************************/
	
	//Moves a Node upward
	private void swim(int i) {
		Node<Key> x = nodes[i];
		Node<Key> parent = x.parent;
		if (parent != null && greater(parent.key, x.key)) {
			exchange(x, parent);
			swim(i);
		}
	}
	
	//The key associated with i becomes the root of its Binomial Tree,
	//regardless of the order relation defined for the keys
	private void toTheRoot(int i) {
		Node<Key> x = nodes[i];
		Node<Key> parent = x.parent;
		if (parent != null) {
			exchange(x, parent);
			toTheRoot(i);
		}
	}
	
	/**************************************************
	 * Functions for deleting a key
	 *************************************************/
	
	//Assuming the key associated with i is in the root list,
	//deletes and return the node of index i
	private Node<Key> erase(int i) {
		Node<Key> reference = nodes[i];
		Node<Key> x = head;
		Node<Key> previous = null;
		while (x != reference) {
			previous = x;
			x = x.sibling;
		}
		previous.sibling = x.sibling;
		if (x == head) head = head.sibling;
		nodes[i] = null;
		return x;
	}
	
	//Deletes and return the node containing the minimum key
	private Node<Key> eraseMin() {
		Node<Key> min = head;
		Node<Key> previous = null;
		Node<Key> current = head;
		while (current.sibling != null) {
			if (greater(min.key, current.sibling.key)) {
				previous = current;
				min = current.sibling;
			}
			current = current.sibling;
		}
		previous.sibling = min.sibling;
		if (min == head) head = min.sibling;
		nodes[min.index] = null;
		return min;
	}
	
	/**************************************************
	 * Functions for inserting a key in the heap
	 *************************************************/
	
	//Merges two root lists into one, there can be up to 2 Binomial Trees of same order
	private Node<Key> merge(Node<Key> h, Node<Key> x, Node<Key> y) {
		if (x == null && y == null) return h;
		else if (x == null) 		h.sibling = merge(y, null, y.sibling);
		else if (y == null) 		h.sibling = merge(x, x.sibling, null);
		else if (x.order < y.order) h.sibling = merge(x, x.sibling, y);
		else 						h.sibling = merge(y, x, y.sibling);
		return h;
	}
	
	//Merges two Binomial Heaps together and returns the resulting Binomial Heap
	//It destroys the two Heaps in parameter, which should not be used any after.
	//To guarantee logarithmic time, this function assumes the arrays are up-to-date
	private IndexBinomialMinPQ<Key> union(IndexBinomialMinPQ<Key> heap) {
		this.head = merge(new Node<Key>(), this.head, heap.head).sibling;
		Node<Key> x = this.head;
		Node<Key> prevx = null, nextx = x.sibling;
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
	
	/******************************************************************
	 * Constructor
	 *****************************************************************/
	
	//Creates an empty heap
	//The comparator is instanciated because it needs to,
	//but won't be used by any heap created by this constructor
	private IndexBinomialMinPQ() { comparator = null; }
	
	/******************************************************************
	 * Iterator
	 *****************************************************************/
	
	/**
	 * Gets an Iterator over the indexes in the priority queue in ascending order
	 * The Iterator does not implement the remove() method
	 * iterator() : Worst case is O(n)
	 * next() : 	Worst case is O(log(n))
	 * hasNext() : 	Worst case is O(1)
	 * @return an Iterator over the indexes in the priority queue in ascending order
	 */
	
	public Iterator<Integer> iterator() {
		return new MyIterator();
	}
	
	private class MyIterator implements Iterator<Integer> {
		IndexBinomialMinPQ<Key> data;
		
		//Constructor clones recursively the elements in the queue
		//It takes linear time
		public MyIterator() {
			data = new IndexBinomialMinPQ<Key>(n, comparator);
			data.head = clone(head, null);
		}
		
		private Node<Key> clone(Node<Key> x, Node<Key> parent) {
			if (x == null) return null;
			Node<Key> node = new Node<Key>();
			node.index = x.index;
			node.key = x.key;
			data.nodes[node.index] = node;
			node.parent = parent;
			node.sibling = clone(x.sibling, parent);
			node.child = clone(x.child, node);
			return node;
		}
		
		public boolean hasNext() {
			return !data.isEmpty();
		}
		
		public Integer next() {
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
 *  Copyright 2002-2020, Robert Sedgewick and Kevin Wayne.
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
