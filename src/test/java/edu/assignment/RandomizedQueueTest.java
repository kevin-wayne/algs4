
package edu.assignment;

import java.util.Iterator;
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
        while(x.hasNext()) {
            System.out.println(x.next());
        }
                
    }
}
