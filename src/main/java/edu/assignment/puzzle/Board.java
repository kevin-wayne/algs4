/******************************************************************************
 *  Compilation:  javac Board.java
 *  Execution:    java edu.assignment.puzzle.Board
 *  Dependencies: StdRandom.java Queue.java
 *  Data files:   src/main/resources/data/puzzle
 *
 *  Models an
 * <a href="https://en.wikipedia.org/wiki/15_puzzle">8-puzzle</a> board
 ******************************************************************************/

package edu.assignment.puzzle;

import edu.princeton.cs.algs4.Queue;
import java.util.ArrayList;

/**
 *  This class provides APIs for the puzzle board.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html">Programming Assignment 4</a>
 *  of Coursera, Algorithms Part I
 *
 *  @author vahbuna
 */
public class Board {
    /**
     * this board.
     */
    private final int[] board;

    /**
     * length of side of the board.
     */
    private final int dimension;
    /**
     * sum of manhattan distances of the board.
     */
    private int manhattan;

    /**
     * hamming score of the board.
     */
    private int hamming;
    /**
     * construct a board from an n-by-n array of blocks.
     * where blocks[i][j] = block in row i, column j
     * @param blocks n x n arrangement of numbers till n^2 - 1
     */
    public Board(final int[][] blocks) {
        dimension = blocks.length;
        board = new int[dimension * dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                board[dimension * i + j] = blocks[i][j];
            }
        }
        computeDistances();
    }

    /**
     * board dimension n.
     * @return dimension
     */
    public int dimension() {
        return dimension;
    }

    /**
     * number of blocks out of place.
     * @return hamming number
     */
    public int hamming() {
        return hamming;
    }

    /**
     * Compute Manhattan distances between blocks and goal.
     * Compute hamming distance of the board.
     */
    private void computeDistances() {
        int sum = 0;
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0 || (board[i] - 1) == i) {
                continue;
            }
            count++;
            int currentRow = (board[i] - 1) / dimension;
            int expectedRow = i / dimension;
            int currentCol = (board[i] - 1) % dimension;
            int expectedCol = i % dimension;
            sum += Math.abs(currentRow - expectedRow)
                    + Math.abs(currentCol - expectedCol);
        }
        this.manhattan = sum;
        this.hamming = count;
    }

    /**
     * Manhattan distance of the board.
     * @return manhattan sum
     */
    public int manhattan() {
        return this.manhattan;
    }

    /**
     * is this board the goal board?
     * @return true or false
     */
    public boolean isGoal() {
        return hamming() == 0;
    }

    /**
     * a board that is obtained by exchanging any pair of blocks.
     * @return twin board
     */
    public Board twin() {
        int[][] twinBoard = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                twinBoard[i][j] = board[dimension * i + j];
            }
        }

        for (int i = 0; i < board.length - 1; i++) {
            int j = i + 1;
            if (twinBoard[i / dimension][i % dimension] != 0
                    && twinBoard[j / dimension][j % dimension] != 0) {
                int temp = twinBoard[i / dimension][i % dimension];
                twinBoard[i / dimension][i % dimension]
                        = twinBoard[j / dimension][j % dimension];
                twinBoard[j / dimension][j % dimension] = temp;
                break;
            }
        }
        return new Board(twinBoard);
    }

    /**
     * does this board equal y?
     * @param y other object
     * @return
     */
    @Override
    public boolean equals(final Object y) {
        if (y == this) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (this.getClass() != y.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (that.dimension() != this.dimension()) {
            return false;
        }

        int max = this.dimension * this.dimension;
        for (int i = 0; i < max; i++) {
            if (this.board[i] != that.board[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * all neighboring boards.
     * @return new boards
     */
    public Iterable<Board> neighbors() {
        Queue<Board> neighbours = new Queue<>();
        int[][] newBoard = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                newBoard[i][j] = board[dimension * i + j];
            }
        }

        int zero = -1;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) {
                zero = i;
                break;
            }
        }

        ArrayList<Integer> indices = new ArrayList<>();
        if (zero >= dimension) {
            indices.add(zero - dimension);
        }

        if (zero % dimension > 0) {
            indices.add(zero - 1);
        }

        if (zero / dimension < dimension - 1) {
            indices.add(zero + dimension);
        }

        if (zero % dimension < dimension - 1) {
            indices.add(zero + 1);
        }

        for (int index: indices) {
            newBoard[zero / dimension][zero % dimension] = board[index];
            newBoard[index / dimension][index % dimension] = board[zero];
            neighbours.enqueue(new Board(newBoard));
            newBoard[index / dimension][index % dimension] = board[index];
        }

        return neighbours;
    }

    /**
     * string representation of this board.
     * @return board as string
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension);
        for (int i = 0; i < board.length; i++) {
            if (i  % dimension == 0) {
                s.append("\n");
            }
            s.append(String.format("%2d ", board[i]));
        }
        return s.toString();
    }
}
