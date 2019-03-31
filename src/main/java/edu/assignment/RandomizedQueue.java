/*****************************************************************************
 *  Compilation:  javac RandomizedQueue.java
 *  Execution:    java edu.assignment.RandomizedQueue
 *  Dependencies: StdRandom.java
 *  Data files:   src/main/resources/data
 *
 *  Models a randomized queue .
 *
 *****************************************************************************/

package edu.assignment;

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
     * last element of the list.
     */
    private int last;

    /**
     * queue to store an element.
     */
    private Item[] queue;

    /**
     * construct an empty randomized queue.
     *
     */
    public RandomizedQueue() {
        final int size = 2;
        queue = (Item[]) new Object[size];
        last = 0;
    }


    /**
     * is the randomized queue empty?
     *
     * @return true or false
     */
    public boolean isEmpty() {
        return last < 1;
    }

    /**
     * return the number of items on the randomized queue.
     * @return length of queue
     */
    public int size() {
        return last;
    }

    /**
     * add the item.
     * @param item to add
     */
    public void enqueue(final Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        queue[last++] = item;

        if (last == queue.length) {
            resize(2 * queue.length);
        }
    }

    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < last; i++) {
            temp[i] = queue[i];
        }
        queue = temp;
    }

    /**
     * remove and return a random item.
     * @return any item
     */
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int position = StdRandom.uniform(size());
        Item element = queue[position];
        queue[position] = queue[last - 1];
        queue[last - 1] = null;
        last--;
        if (last == queue.length / 4) {
            resize(queue.length / 2);
        }
        return element;
    }

    /**
     * return a random item (but do not remove it).
     * @return any item without removing
     */
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return queue[StdRandom.uniform(size())];
    }

    /**
     * return an independent iterator over items in random order.
     * @return iterator
     */
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Item[] queueCopy;
        private int count;
        public ListIterator() {
            queueCopy = (Item[]) new Object[size()];
            for (int i = 0; i < size(); i++) {
                queueCopy[i] = queue[i];
            }
            count  = size();
        }

        /**
         * If next element is present.
         *
         * @return true or false
         */
        @Override
        public boolean hasNext() {
            return count > 0;
        }

        /**
         * Remove an element.
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * get next element.
         *
         * @return next element
         */
        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int position = StdRandom.uniform(count);
            Item temp = queueCopy[position];
            queueCopy[position] = queueCopy[count - 1];
            queueCopy[count - 1] = temp;
            count--;
            return temp;
        }
    }
}
