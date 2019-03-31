/*****************************************************************************
 *  Compilation:  javac Deque.java
 *  Execution:    java edu.assignment.Deque
 *  Data files:   src/main/resources/data
 *
 *  Models a double-ended queue.
 *
 *****************************************************************************/

// package edu.assignment;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * For additional documentation, see.
 * <a href="http://coursera.cs.princeton.edu/algs4/assignments/queues.html">
 * Sprecifications</a>
 *
 * @param <Item> data type
 * @author Robert Sedgewick
 * @author Kevin Waynev
 * @author vahbuna
 */
public class Deque<Item> implements Iterable<Item> {

    /**
     * start of the list.
     */
    private Node<Item> start;

    // last element of the list.
    private Node<Item> end;

    private int size;

    /**
     * class to store an element.
     *
     * @param <Item>
     */
    private class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> previous;
    }

    /**
     * construct an empty deque.
     *
     */
    public Deque() {
        size = 0;
        start = null;
        end = null;
    }

    /**
     * is the deque empty?
     *
     * @return true or false
     */
    public boolean isEmpty() {
        return size < 1;
    }

    /**
     * return the number of items on the deque.
     *
     * @return count
     */
    public int size() {
        return size;
    }

    /**
     * add the item to the front.
     *
     * @param item object to add
     */
    public void addFirst(final Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node<Item> element = new Node<Item>();
        element.item = item;
        element.previous = null;
        element.next = start;
        if (size == 0) {
            end = element;
        } else {
            start.previous = element;
        }
        start = element;
        size++;
    }

    /**
     * add the item to the end.
     *
     * @param item object to add
     */
    public void addLast(final Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node<Item> element = new Node<Item>();
        element.item = item;
        element.next = null;
        element.previous = end;

        if (end == null) {
            start = element;
        } else {
            end.next = element;
        }
        end = element;
        size++;
    }

    /**
     * remove and return the item from the front.
     *
     * @return first element
     */
    public Item removeFirst() {
        if (start == null || size == 0) {
            throw new NoSuchElementException();
        }

        Item element = start.item;
        start = start.next;
        size--;
        if (size == 0) {
            end = null;
        } else {
            start.previous = null;
        }
        return element;
    }

    /**
     * remove and return the item from the end.
     *
     * @return last element
     */
    public Item removeLast() {
        if (start == null || size == 0) {
            throw new NoSuchElementException();
        }

        Item element = end.item;
        end = end.previous;

        if (end == null) {
            start = null;
        } else {
            end.next = null;
        }

        size--;
        return element;
    }

    /**
     * return an iterator over items in order from front to end.
     *
     * @return iterator
     */
    @Override
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {

        private Node<Item> current;

        public ListIterator() {
            current = start;
        }

        /**
         * If next element is present.
         *
         * @return true or false
         */
        public boolean hasNext() {
            return current != null;
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
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}
