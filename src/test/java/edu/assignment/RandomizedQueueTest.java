
package edu.assignment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vahbuna
 */
public class RandomizedQueueTest {
    private RandomizedQueue<Integer> queue;

    @Before
    public void setUp() {
        queue = new RandomizedQueue<Integer>();
    }

    @Test
    public void testIterator() {
        queue.enqueue(3);
        queue.enqueue(13);
        queue.enqueue(31);
        queue.enqueue(39);
        queue.enqueue(93);

        Iterator<Integer> x = queue.iterator();
        List<Integer> unique = new ArrayList<Integer>();
        while(x.hasNext()) {
            Integer temp = x.next();
            if(unique.contains(temp.intValue()))
                fail();
            else
                unique.add(temp.intValue());
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testSample() {
        queue.sample();
    }

    @Test
    public void enQdQs() {
        queue.enqueue(3);
        queue.enqueue(13);
        queue.enqueue(31);
        queue.enqueue(39);
        queue.enqueue(93);
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
    }
}
