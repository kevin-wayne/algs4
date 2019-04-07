
package edu.assignment.puzzle;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author vahbuna
 */
public class BoardTest {

    @Test
    public void dimensionTest() {
        Board ten =
                new Board(PuzzleChecker.readFile("data/puzzle/puzzle00.txt"));
        assertEquals(10, ten.dimension());
    }

    @Test
    public void solutionTest() {
        Board ten =
                new Board(PuzzleChecker.readFile("data/puzzle/puzzle00.txt"));
        assertTrue(ten.isGoal());
    }

    @Test
    public void hammingTest() {
        Board three = new Board(PuzzleChecker.
                readFile("data/puzzle/puzzle3x3-01.txt"));
        assertEquals(1, three.hamming());
    }

    @Test
    public void manhattanTest() {
        Board three = new Board(PuzzleChecker.
                readFile("data/puzzle/puzzle3x3-01.txt"));
        assertEquals(1, three.manhattan());
    }

    @Test
    public void twinTest() {
        Board three = new Board(PuzzleChecker.
                readFile("data/puzzle/puzzle3x3-01.txt"));
        Board twinThree = three.twin();
        assertFalse(three.equals(twinThree));
    }

    @Test
    public void neighboursTest() {
        Board three = new Board(PuzzleChecker.
                readFile("data/puzzle/puzzle3x3-01.txt"));
        ArrayList<Board> boards = new ArrayList<Board>();
        for (Board neighbour: three.neighbors()) {
            boards.add(neighbour);
        }

        assertEquals(3, boards.size());
        assertEquals(2, boards.get(0).hamming());
        assertEquals(2, boards.get(1).hamming());
        assertEquals(0, boards.get(2).hamming());
        assertTrue(boards.get(2).isGoal());
    }
}
