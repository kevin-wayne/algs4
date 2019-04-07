
package edu.assignment.puzzle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author anu
 */
public class SolverTest {
   @Test
   public void solvableTest() {
       Solver solver = new Solver(
               new Board(
                       PuzzleChecker.readFile("data/puzzle/puzzle3x3-10.txt")));
       assertTrue(solver.isSolvable());
       assertEquals(10, solver.moves());
   }

   @Test
   public void notSolvableTest() {
       Solver solver = new Solver(
               new Board(
                       PuzzleChecker.readFile(
                               "data/puzzle/puzzle3x3-unsolvable1.txt")));
       assertFalse(solver.isSolvable());
       assertNull(solver.solution());
   }
}
