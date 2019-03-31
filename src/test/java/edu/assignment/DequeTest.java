package edu.assignment;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vahbuna
 */
public class DequeTest {
    private Deque<Integer> queue;

    @Before
    public void setUp() {
        queue = new Deque<Integer>();
    }

    @Test
    public void addIntegerTests() {
        assertEquals(0, queue.size());
        queue.addFirst(4);
        queue.addLast(14);

        assertEquals(new Integer(4), queue.removeFirst());
        assertEquals(new Integer(14), queue.removeFirst());

        assertEquals(0, queue.size());
    }

    @Test
    public void addLastIntegerTests() {
        assertEquals(0, queue.size());
        queue.addLast(4);
        queue.addLast(14);

        assertEquals(new Integer(4), queue.removeFirst());
        assertEquals(new Integer(14), queue.removeFirst());

        assertEquals(0, queue.size());
    }
}
