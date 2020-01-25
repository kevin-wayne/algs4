/******************************************************************************
 *  Compilation:  javac Polynomial.java
 *  Execution:    java Polynomial
 *
 *  Polynomials with integer coefficients.
 *
 *  % java Polynomial
 *  zero(x)     = 0
 *  p(x)        = 4x^3 + 3x^2 + 2x + 1
 *  q(x)        = 3x^2 + 5
 *  p(x) + q(x) = 4x^3 + 6x^2 + 2x + 6
 *  p(x) * q(x) = 12x^5 + 9x^4 + 26x^3 + 18x^2 + 10x + 5
 *  p(q(x))     = 108x^6 + 567x^4 + 996x^2 + 586
 *  p(x) - p(x) = 0
 *  0 - p(x)    = -4x^3 - 3x^2 - 2x - 1
 *  p(3)        = 142
 *  p'(x)       = 12x^2 + 6x + 2
 *  p''(x)      = 24x + 6
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The {@code Polynomial} class represents a polynomial with integer
 *  coefficients.
 *  Polynomials are immutable: their values cannot be changed after they
 *  are created.
 *  It includes methods for addition, subtraction, multiplication, composition,
 *  differentiation, and evaluation.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/99scientific">Section 9.9</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Polynomial {
    private int[] coef;   // coefficients p(x) = sum { coef[i] * x^i }
    private int degree;   // degree of polynomial (-1 for the zero polynomial)

    /**
     * Initializes a new polynomial a x^b
     * @param a the leading coefficient
     * @param b the exponent
     * @throws IllegalArgumentException if {@code b} is negative
     */
    public Polynomial(int a, int b) {
        if (b < 0) {
            throw new IllegalArgumentException("exponent cannot be negative: " + b);
        }
        coef = new int[b+1];
        coef[b] = a;
        reduce();
    }

    // pre-compute the degree of the polynomial, in case of leading zero coefficients
    // (that is, the length of the array need not relate to the degree of the polynomial)
    private void reduce() {
        degree = -1;
        for (int i = coef.length - 1; i >= 0; i--) {
            if (coef[i] != 0) {
                degree = i;
                return;
            }
        }
    }

    /**
     * Returns the degree of this polynomial.
     * @return the degree of this polynomial, -1 for the zero polynomial.
     */
    public int degree() {
        return degree;
    }

    /**
     * Returns the sum of this polynomial and the specified polynomial.
     *
     * @param  that the other polynomial
     * @return the polynomial whose value is {@code (this(x) + that(x))}
     */
    public Polynomial plus(Polynomial that) {
        Polynomial poly = new Polynomial(0, Math.max(this.degree, that.degree));
        for (int i = 0; i <= this.degree; i++) poly.coef[i] += this.coef[i];
        for (int i = 0; i <= that.degree; i++) poly.coef[i] += that.coef[i];
        poly.reduce();
        return poly;
    }

    /**
     * Returns the result of subtracting the specified polynomial
     * from this polynomial.
     *
     * @param  that the other polynomial
     * @return the polynomial whose value is {@code (this(x) - that(x))}
     */
    public Polynomial minus(Polynomial that) {
        Polynomial poly = new Polynomial(0, Math.max(this.degree, that.degree));
        for (int i = 0; i <= this.degree; i++) poly.coef[i] += this.coef[i];
        for (int i = 0; i <= that.degree; i++) poly.coef[i] -= that.coef[i];
        poly.reduce();
        return poly;
    }

    /**
     * Returns the product of this polynomial and the specified polynomial.
     * Takes time proportional to the product of the degrees.
     * (Faster algorithms are known, e.g., via FFT.)
     *
     * @param  that the other polynomial
     * @return the polynomial whose value is {@code (this(x) * that(x))}
     */
    public Polynomial times(Polynomial that) {
        Polynomial poly = new Polynomial(0, this.degree + that.degree);
        for (int i = 0; i <= this.degree; i++)
            for (int j = 0; j <= that.degree; j++)
                poly.coef[i+j] += (this.coef[i] * that.coef[j]);
        poly.reduce();
        return poly;
    }

    /**
     * Returns the composition of this polynomial and the specified
     * polynomial.
     * Takes time proportional to the product of the degrees.
     * (Faster algorithms are known, e.g., via FFT.)
     *
     * @param  that the other polynomial
     * @return the polynomial whose value is {@code (this(that(x)))}
     */
    public Polynomial compose(Polynomial that) {
        Polynomial poly = new Polynomial(0, 0);
        for (int i = this.degree; i >= 0; i--) {
            Polynomial term = new Polynomial(this.coef[i], 0);
            poly = term.plus(that.times(poly));
        }
        return poly;
    }


    /**       
     * Compares this polynomial to the specified polynomial.
     *       
     * @param  other the other polynoimal
     * @return {@code true} if this polynomial equals {@code other};
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Polynomial that = (Polynomial) other;
        if (this.degree != that.degree) return false;
        for (int i = this.degree; i >= 0; i--)
            if (this.coef[i] != that.coef[i]) return false;
        return true;
    }

    /**
     * Returns the result of differentiating this polynomial.
     *
     * @return the polynomial whose value is {@code this'(x)}
     */
    public Polynomial differentiate() {
        if (degree == 0) return new Polynomial(0, 0);
        Polynomial poly = new Polynomial(0, degree - 1);
        poly.degree = degree - 1;
        for (int i = 0; i < degree; i++)
            poly.coef[i] = (i + 1) * coef[i + 1];
        return poly;
    }

    /**
     * Returns the result of evaluating this polynomial at the point x.
     *
     * @param  x the point at which to evaluate the polynomial
     * @return the integer whose value is {@code (this(x))}
     */
    public int evaluate(int x) {
        int p = 0;
        for (int i = degree; i >= 0; i--)
            p = coef[i] + (x * p);
        return p;
    }

    /**
     * Compares two polynomials by degree, breaking ties by coefficient of leading term.
     *
     * @param  that the other point
     * @return the value {@code 0} if this polynomial is equal to the argument
     *         polynomial (precisely when {@code equals()} returns {@code true});
     *         a negative integer if this polynomialt is less than the argument
     *         polynomial; and a positive integer if this polynomial is greater than the
     *         argument point
     */
    public int compareTo(Polynomial that) {
        if (this.degree < that.degree) return -1;
        if (this.degree > that.degree) return +1;
        for (int i = this.degree; i >= 0; i--) {
            if (this.coef[i] < that.coef[i]) return -1;
            if (this.coef[i] > that.coef[i]) return +1;
        }
        return 0;
    }

    /**
     * Return a string representation of this polynomial.
     * @return a string representation of this polynomial in the format
     *         4x^5 - 3x^2 + 11x + 5
     */
    @Override
    public String toString() {
        if      (degree == -1) return "0";
        else if (degree ==  0) return "" + coef[0];
        else if (degree ==  1) return coef[1] + "x + " + coef[0];
        String s = coef[degree] + "x^" + degree;
        for (int i = degree - 1; i >= 0; i--) {
            if      (coef[i] == 0) continue;
            else if (coef[i]  > 0) s = s + " + " + (coef[i]);
            else if (coef[i]  < 0) s = s + " - " + (-coef[i]);
            if      (i == 1) s = s + "x";
            else if (i >  1) s = s + "x^" + i;
        }
        return s;
    }

    /**
     * Unit tests the polynomial data type.
     *
     * @param args the command-line arguments (none)
     */
    public static void main(String[] args) { 
        Polynomial zero = new Polynomial(0, 0);

        Polynomial p1   = new Polynomial(4, 3);
        Polynomial p2   = new Polynomial(3, 2);
        Polynomial p3   = new Polynomial(1, 0);
        Polynomial p4   = new Polynomial(2, 1);
        Polynomial p    = p1.plus(p2).plus(p3).plus(p4);   // 4x^3 + 3x^2 + 1

        Polynomial q1   = new Polynomial(3, 2);
        Polynomial q2   = new Polynomial(5, 0);
        Polynomial q    = q1.plus(q2);                     // 3x^2 + 5


        Polynomial r    = p.plus(q);
        Polynomial s    = p.times(q);
        Polynomial t    = p.compose(q);
        Polynomial u    = p.minus(p);

        StdOut.println("zero(x)     = " + zero);
        StdOut.println("p(x)        = " + p);
        StdOut.println("q(x)        = " + q);
        StdOut.println("p(x) + q(x) = " + r);
        StdOut.println("p(x) * q(x) = " + s);
        StdOut.println("p(q(x))     = " + t);
        StdOut.println("p(x) - p(x) = " + u);
        StdOut.println("0 - p(x)    = " + zero.minus(p));
        StdOut.println("p(3)        = " + p.evaluate(3));
        StdOut.println("p'(x)       = " + p.differentiate());
        StdOut.println("p''(x)      = " + p.differentiate().differentiate());
    }
}

/******************************************************************************
 *  Copyright 2002-2020, Robert Sedgewick and Kevin Wayne.
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
