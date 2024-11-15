/******************************************************************************
 *  Compilation:  javac Transaction.java
 *  Execution:    java Transaction
 *  Dependencies: StdOut.java
 *
 *  Data type for commercial transactions.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.Arrays;
import java.util.Comparator;

/**
 *  The {@code Transaction} class is an immutable data type to encapsulate a
 *  commercial transaction with a customer name, date, and amount.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/12oop">Section 1.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Transaction implements Comparable<Transaction> {
    private final String  who;      // customer
    private final Date    when;     // date
    private final double  amount;   // amount


    /**
     * Initializes a new transaction from the given arguments.
     *
     * @param  who the person involved in this transaction
     * @param  when the date of this transaction
     * @param  amount the amount of this transaction
     * @throws IllegalArgumentException if {@code amount}
     *         is {@code Double.NaN}, {@code Double.POSITIVE_INFINITY},
     *         or {@code Double.NEGATIVE_INFINITY}
     */
    public Transaction(String who, Date when, double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount))
            throw new IllegalArgumentException("Amount cannot be NaN or infinite");
        this.who    = who;
        this.when   = when;
        this.amount = amount;
    }

    /**
     * Initializes a new transaction by parsing a string of the form NAME DATE AMOUNT.
     *
     * @param  transaction the string to parse
     * @throws IllegalArgumentException if {@code amount}
     *         is {@code Double.NaN}, {@code Double.POSITIVE_INFINITY},
     *         or {@code Double.NEGATIVE_INFINITY}
     */
    public Transaction(String transaction) {
        String[] a = transaction.split("\\s+");
        who    = a[0];
        when   = new Date(a[1]);
        amount = Double.parseDouble(a[2]);
        if (Double.isNaN(amount) || Double.isInfinite(amount))
            throw new IllegalArgumentException("Amount cannot be NaN or infinite");
    }

    /**
     * Returns the name of the customer involved in this transaction.
     *
     * @return the name of the customer involved in this transaction
     */
    public String who() {
        return who;
    }

    /**
     * Returns the date of this transaction.
     *
     * @return the date of this transaction
     */
    public Date when() {
        return when;
    }

    /**
     * Returns the amount of this transaction.
     *
     * @return the amount of this transaction
     */
    public double amount() {
        return amount;
    }

    /**
     * Returns a string representation of this transaction.
     *
     * @return a string representation of this transaction
     */
    @Override
    public String toString() {
        return String.format("%-10s %10s %8.2f", who, when, amount);
    }

    /**
     * Compares two transactions by amount.
     *
     * @param  that the other transaction
     * @return { a negative integer, zero, a positive integer}, depending
     *         on whether the amount of this transaction is { less than,
     *         equal to, or greater than } the amount of that transaction
     */
    public int compareTo(Transaction that) {
        return Double.compare(this.amount, that.amount);
    }

    /**
     * Compares this transaction to the specified object.
     *
     * @param  other the other transaction
     * @return true if this transaction is equal to {@code other}; false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Transaction that = (Transaction) other;
        return (this.amount == that.amount) && (this.who.equals(that.who))
                                            && (this.when.equals(that.when));
    }


    /**
     * Returns a hash code for this transaction.
     *
     * @return a hash code for this transaction
     */
    public int hashCode() {
        int hash = 1;
        hash = 31*hash + who.hashCode();
        hash = 31*hash + when.hashCode();
        hash = 31*hash + ((Double) amount).hashCode();
        return hash;
        // return Objects.hash(who, when, amount);
    }

    /**
     * Compares two transactions by customer name.
     */
    public static class WhoOrder implements Comparator<Transaction> {

        @Override
        public int compare(Transaction v, Transaction w) {
            return v.who.compareTo(w.who);
        }
    }

    /**
     * Compares two transactions by date.
     */
    public static class WhenOrder implements Comparator<Transaction> {

        @Override
        public int compare(Transaction v, Transaction w) {
            return v.when.compareTo(w.when);
        }
    }

    /**
     * Compares two transactions by amount.
     */
    public static class HowMuchOrder implements Comparator<Transaction> {

        @Override
        public int compare(Transaction v, Transaction w) {
            return Double.compare(v.amount, w.amount);
        }
    }


    /**
     * Unit tests the {@code Transaction} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        Transaction[] a = new Transaction[4];
        a[0] = new Transaction("Turing   6/17/1990  644.08");
        a[1] = new Transaction("Tarjan   3/26/2002 4121.85");
        a[2] = new Transaction("Knuth    6/14/1999  288.34");
        a[3] = new Transaction("Dijkstra 8/22/2007 2678.40");

        StdOut.println("Unsorted");
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();

        StdOut.println("Sort by date");
        Arrays.sort(a, new Transaction.WhenOrder());
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();

        StdOut.println("Sort by customer");
        Arrays.sort(a, new Transaction.WhoOrder());
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();

        StdOut.println("Sort by amount");
        Arrays.sort(a, new Transaction.HowMuchOrder());
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();
    }

}




/******************************************************************************
 *  Copyright 2002-2022, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
