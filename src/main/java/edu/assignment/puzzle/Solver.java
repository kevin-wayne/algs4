/******************************************************************************
 *  Compilation:  javac Solver.java
 *  Execution:    java edu.assignment.puzzle.Solver
 *  Dependencies: In.java MinPQ.java StdOut.java Queue.java
 *  Data files:   src/main/resources/data/puzzle
 *
 *  Implements <a href="http://en.wikipedia.org/wiki/A*_search_algorithm">
 *  A* search algorithm</a>
 ******************************************************************************/

package edu.assignment.puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 *  This class generates game tree for 8 puzzle.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html">Programming Assignment 4</a>
 *  of Coursera, Algorithms Part I
 *
 *  @author vahbuna
 */
public class Solver {

    /**
     * list of boards leading to the solution.
     */
    private final Stack<Board> solution;

    /**
     * class to identify the next board in the A* search.
     */
    private class SearchNode implements Comparable<SearchNode> {
        private final  Board current;
        private final int moves;
        private final Board previous;
        private final int currentManhattan;

        SearchNode(final Board currentBoard) {
            this.current = currentBoard;
            moves = 0;
            previous = null;
            currentManhattan = currentBoard.manhattan();
        }

        SearchNode(final Board currentBoard, final Board previousBoard,
                final int totalMoves) {
            this.current = currentBoard;
            this.moves = totalMoves;
            this.previous = previousBoard;
            currentManhattan = currentBoard.manhattan();
        }

        @Override
        public int compareTo(final SearchNode that) {
            if ((currentManhattan + moves)
                    == (that.currentManhattan + that.moves)) {
                // return current.hamming() - that.current.hamming();
                return currentManhattan - that.currentManhattan;
            }

            return (currentManhattan + moves
                    - that.currentManhattan - that.moves);
        }
    }

    /**
     * find a solution to the initial board (using the A* algorithm).
     * @param initial board
     */
    public Solver(final Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        solution = new Stack<>();
        Stack<SearchNode> visited = new Stack<>();
        Board evilTwin = initial.twin();
        MinPQ<SearchNode> initialPQ = new MinPQ<>();
        MinPQ<SearchNode> evilPQ = new MinPQ<>();
        initialPQ.insert(new SearchNode(initial));
        evilPQ.insert(new SearchNode(evilTwin));

        while (!initialPQ.isEmpty() && !evilPQ.isEmpty()) {
            SearchNode current = initialPQ.delMin();
            SearchNode twin = evilPQ.delMin();
            visited.push(current);
            if (current.current.isGoal() || twin.current.isGoal()) {
                break;
            }
            Board previous = current.previous;
            Board previousTwin = twin.previous;

            for (Board temp: current.current.neighbors()) {
                if (!temp.equals(previous)) {
                    initialPQ.insert(new SearchNode(temp,
                            current.current, current.moves + 1));
                }
            }
            for (Board temp: twin.current.neighbors()) {
                if (!temp.equals(previousTwin)) {
                    evilPQ.insert(new SearchNode(temp,
                            twin.current, twin.moves + 1));
                }
            }
        }

        SearchNode answer = visited.pop();
        if (answer.current.isGoal()) {
            solution.push(answer.current);
            while (!visited.isEmpty()) {
                SearchNode temp = visited.pop();
                if (temp.moves + 1 == answer.moves
                        && temp.current == answer.previous) {
                    solution.push(temp.current);
                    answer = temp;
                }
            }
        }
    }

    /**
     * is the initial board solvable?
     * @return true or false
     */
    public boolean isSolvable() {
        return solution.size() > 0;
    }

    /**
     * min number of moves to solve initial board; -1 if unsolvable.
     * @return number of moves
     */
    public int moves() {
        return solution.size() - 1;
    }

    /**
     * sequence of boards in a shortest solution; null if unsolvable.
     * @return board list
     */
    public Iterable<Board> solution() {
        if (solution.size() == 0) {
            return null;
        }
        return solution;
    }

    /**
     * solve a slider puzzle.
     * @param args input puzzle
     */
    public static void main(final String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }
}
