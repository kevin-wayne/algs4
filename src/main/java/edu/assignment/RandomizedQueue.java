/*****************************************************************************
 *  Compilation:  javac RandomizedQueue.java
 *  Execution:    java edu.assignment.RandomizedQueue
 *  Dependencies: StdRandom.java
 *  Data files:   src/main/resources/data
 *
 *  Models a randomized queue .
 *
 *****************************************************************************/

// package edu.assignment;

import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A randomized queue is similar to a stack or queue, except that the item
 * removed is chosen uniformly at random from items in the data structure. For
 * additional documentation, see:
 * <a href="http://coursera.cs.princeton.edu/algs4/assignments/queues.html">
 * Sprecifications</a>
 *
 * @param <Item> data type
 * @author Robert Sedgewick
 * @author Kevin Waynev
 * @author vahbuna
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    /**
     * start of the list.
     */
    private Node<Item> start;

    private int size;

    /**
     * class to store an element.
     *
     * @param <Item> data type
     */
    private class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

    /**
     * construct an empty randomized queue.
     *
     */
    public RandomizedQueue() {
        size = 0;
        start = null;
    }


    /**
     * is the randomized queue empty?
     *
     * @return true or false
     */
    public boolean isEmpty() {
        return size < 1;
    }

    /**
     * return the number of items on the randomized queue.
     * @return length of queue
     */
    public int size() {
        return size;
    }

    /**
     * add the item.
     * @param item to add
     */
    public void enqueue(final Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node<Item> element = new Node<Item>();
        element.item = item;
        element.next = null;

        if (start == null) {
            start = element;
        } else {
            element.next = start;
            start = element;
        }

        size++;
    }

    /**
     * remove and return a random item.
     * @return any item
     */
    public Item dequeue() {
        if (start == null || size == 0) {
            throw new NoSuchElementException();
        }

        int position = StdRandom.uniform(size);
        Item element;
        Node<Item> current = start;
        if (position == 0) {
            element = current.item;
            start = current.next;
        } else {
            position--;
            while (position > 0 && current.next.next != null) {
                current = current.next;
                position--;
            }
            element = current.next.item;
            current.next = current.next.next;
        }
        size--;
        return element;
    }

    /**
     * return a random item (but do not remove it).
     * @return any item without removing
     */
    public Item sample() {
        if (start == null || size == 0) {
            throw new NoSuchElementException();
        }

        int position = StdRandom.uniform(size);
        Node<Item> current = start;
        while (position > 0) {
            current = current.next;
            position--;
        }
        return current.item;
    }

    /**
     * return an independent iterator over items in random order.
     * @return iterator
     */
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private int count;
        public ListIterator() {
            count = size();
        }

        /**
         * If next element is present.
         *
         * @return true or false
         */
        public boolean hasNext() {
            return count > 0;
        }

        /**
         * Remove an element.
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * get next element.
         *
         * @return next element
         */
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = sample();
            count--;
            return item;
        }
    }
}
