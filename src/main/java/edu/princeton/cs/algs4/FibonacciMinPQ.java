/******************************************************************************
 *  Compilation: javac FibonacciMinPQ.java
 *  Execution:
 *
 *  A Fibonacci heap.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.Iterator;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Comparator;

/*
 *  The FibonacciMinPQ class represents a priority queue of generic keys.
 *  It supports the usual insert and delete-the-minimum operations, 
 *  along with the merging of two heaps together.
 *  It also supports methods for peeking at the minimum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  It is possible to build the priority queue using a Comparator.
 *  If not, the natural order relation between the keys will be used.
 *  
 *  This implementation uses a Fibonacci heap.
 *  The delete-the-minimum operation takes amortized logarithmic time.
 *  The insert, min-key, is-empty, size, union and constructor take constant time.
 *
 *  @author Tristan Claverie
 */
public class FibonacciMinPQ<Key> implements Iterable<Key> {
	private Node head;					//Head of the circular root list
	private Node min;					//Minimum Node of the root list
	private int size;					//Number of keys in the heap
	private final Comparator<Key> comp;	//Comparator over the keys
	private HashMap<Integer, Node> table = new HashMap<Integer, Node>(); //Used for the consolidate operation
	
	//Represents a Node of a tree
	private class Node {
		Key key;						//Key of this Node
		int order;						//Order of the tree rooted by this Node
		Node prev, next;				//Siblings of this Node
		Node child;						//Child of this Node
	}
	
	/**
	 * Initializes an empty priority queue
	 * Worst case is O(1)
	 * @param C a Comparator over the Keys
	 */
	public FibonacciMinPQ(Comparator<Key> C) {
		comp = C;
	}
	
    /**
     * Initializes an empty priority queue
     * Worst case is O(1)
     */
	public FibonacciMinPQ() {
		comp = new MyComparator();
	}
	
	/**
	 * Initializes a priority queue with given keys
	 * Worst case is O(n)
	 * @param a an array of keys
	 */
	public FibonacciMinPQ(Key[] a) {
		comp = new MyComparator();
		for (Key k : a) insert(k);
	}
	
	/**
	 * Initializes a priority queue with given keys
	 * Worst case is O(n)
	 * @param C a comparator over the keys
	 * @param a an array of keys
	 */
	public FibonacciMinPQ(Comparator<Key> C, Key[] a) {
		comp = C;
		for (Key k : a) insert(k);
	}

	/**
	 * Whether the priority queue is empty
	 * Worst case is O(1)
	 * @return true if the priority queue is empty, false if not
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Number of elements currently on the priority queue
	 * Worst case is O(1)
	 * @return the number of elements on the priority queue
	 */
	public int size() {
		return size;
	}

	/**
	 * Insert a key in the queue
	 * Worst case is O(1)
	 * @param key a Key
	 */
	public void insert(Key key) {
		Node x = new Node();
		x.key = key;
		size++;
		head = insert(x, head);
		if (min == null) min = head;
		else 			 min = (greater(min.key, key)) ? head : min;
	}

	/**
	 * Gets the minimum key currently in the queue
	 * Worst case is O(1)
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the minimum key currently in the priority queue
	 */
	public Key minKey() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		return min.key;
	}

	/**
	 * Deletes the minimum key
	 * Worst case is O(log(n)) (amortized)
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the minimum key
	 */
	public Key delMin() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		head = cut(min, head);
		Node x = min.child;
		Key key = min.key;
		min.key = null;
		if (x != null) {
			head = meld(head, x);
			min.child = null;
		}
		size--;
		if (!isEmpty()) consolidate();
		else 			min = null;
		return key;
	}
	
	/**
	 * Merges two heaps together
	 * This operation is destructive
	 * Worst case is O(1)
	 * @param that a Fibonacci heap
	 * @return the union of the two heaps
	 */
	public FibonacciMinPQ<Key> union(FibonacciMinPQ<Key> that) {
		this.head = meld(head, that.head);
		this.min = (greater(this.min.key, that.min.key)) ? that.min : this.min;
		this.size = this.size+that.size;
		return this;
	}
	
	/*************************************
	 * General helper functions
	 ************************************/
	
	//Compares two keys
	private boolean greater(Key n, Key m) {
		if (n == null) return false;
		if (m == null) return true;
		return comp.compare(n,m) > 0;
	}
	
	//Assuming root1 holds a greater key than root2, root2 becomes the new root
	private void link(Node root1, Node root2) {
		root2.child = insert(root1, root2.child);
		root2.order++;
	}
	
	/*************************************
	 * Function for consolidating all trees in the root list
	 ************************************/
	
	//Coalesce the roots, thus reshapes the tree
	private void consolidate() {
		table.clear();
		Node x = head;
		int maxOrder = 0;
		min = head;
		Node y = null; Node z = null;
		do {
			y = x;
			x = x.next;
			z = table.get(y.order);
			while (z != null) {
				table.remove(y.order);
				if (greater(y.key, z.key)) {
					link(y, z);
					y = z;
				} else {
					link(z, y);
				}
				z = table.get(y.order);
			}
			table.put(y.order, y);
			if (y.order > maxOrder) maxOrder = y.order;
		} while (x != head);
		head = null;
		for (Node n : table.values()) {
			if (n != null) {
				min = greater(min.key, n.key) ? n : min;
				head = insert(n, head);
			}
		}
	}
	
	/*************************************
	 * General helper functions for manipulating circular lists
	 ************************************/
	
	//Inserts a Node in a circular list containing head, returns a new head
	private Node insert(Node x, Node head) {
		if (head == null) {
			x.prev = x;
			x.next = x;
		} else {
			head.prev.next = x;
			x.next = head;
			x.prev = head.prev;
			head.prev = x;
		}
		return x;
	}
	
	//Removes a tree from the list defined by the head pointer
	private Node cut(Node x, Node head) {
		if (x.next == x) {
			x.next = null;
			x.prev = null;
			return null;
		} else {
			x.next.prev = x.prev;
			x.prev.next = x.next;
			Node res = x.next;
			x.next = null;
			x.prev = null;
			if (head == x)  return res;
			else 			return head;
		}
	}
	
	//Merges two root lists together
	private Node meld(Node x, Node y) {
		if (x == null) return y;
		if (y == null) return x;
		x.prev.next = y.next;
		y.next.prev = x.prev;
		x.prev = y;
		y.next = x;
		return x;
	}
	
	/*************************************
	 * Iterator
	 ************************************/
	
	/**
	 * Gets an Iterator over the Keys in the priority queue in ascending order
	 * The Iterator does not implement the remove() method
	 * iterator() : Worst case is O(n)
	 * next() : 	Worst case is O(log(n)) (amortized)
	 * hasNext() : 	Worst case is O(1)
	 * @return an Iterator over the Keys in the priority queue in ascending order
	 */
	
	public Iterator<Key> iterator() {
		return new MyIterator();
	}
	
	private class MyIterator implements Iterator<Key> {
		private FibonacciMinPQ<Key> copy;
		
		
		//Constructor takes linear time
		public MyIterator() {
			copy = new FibonacciMinPQ<Key>(comp);
			insertAll(head);
		}
		
		private void insertAll(Node head) {
			if (head == null) return;
			Node x = head;
			do {
				copy.insert(x.key);
				insertAll(x.child);
				x = x.next;
			} while (x != head);
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		public boolean hasNext() {
			return !copy.isEmpty();
		}
		
		//Takes amortized logarithmic time
		public Key next() {
			if (!hasNext()) throw new NoSuchElementException();
			return copy.delMin();
		}
	}
	
	/*************************************
	 * Comparator
	 ************************************/
	
	//default Comparator
	private class MyComparator implements Comparator<Key> {
		@Override
		public int compare(Key key1, Key key2) {
			return ((Comparable<Key>) key1).compareTo(key2);
		}
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
