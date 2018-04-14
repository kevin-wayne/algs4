/******************************************************************************
 *  Compilation:  javac BinaryStdIn.java
 *  Execution:    java BinaryStdIn < input > output
 *  Dependencies: none             
 *  
 *  Supports reading binary data from standard input.
 *
 *  % java BinaryStdIn < input.jpg > output.jpg
 *  % diff input.jpg output.jpg
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 *  <i>Binary standard input</i>. This class provides methods for reading
 *  in bits from standard input, either one bit at a time (as a {@code boolean}),
 *  8 bits at a time (as a {@code byte} or {@code char}),
 *  16 bits at a time (as a {@code short}), 32 bits at a time
 *  (as an {@code int} or {@code float}), or 64 bits at a time (as a
 *  {@code double} or {@code long}).
 *  <p>
 *  All primitive types are assumed to be represented using their 
 *  standard Java representations, in big-endian (most significant
 *  byte first) order.
 *  <p>
 *  The client should not intermix calls to {@code BinaryStdIn} with calls
 *  to {@code StdIn} or {@code System.in};
 *  otherwise unexpected behavior will result.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public final class BinaryStdIn {
    private static final int EOF = -1;      // end of file

    private static BufferedInputStream in;  // input stream
    private static int buffer;              // one character buffer
    private static int n;                   // number of bits left in buffer
    private static boolean isInitialized;   // has BinaryStdIn been called for first time?

    // don't instantiate
    private BinaryStdIn() { }

    // fill buffer
    private static void initialize() {
        in = new BufferedInputStream(System.in);
        buffer = 0;
        n = 0;
        fillBuffer();
        isInitialized = true;
    }

    private static void fillBuffer() {
        try {
            buffer = in.read();
            n = 8;
        }
        catch (IOException e) {
            System.out.println("EOF");
            buffer = EOF;
            n = -1;
        }
    }

   /**
     * Close this input stream and release any associated system resources.
     */
    public static void close() {
        if (!isInitialized) initialize();
        try {
            in.close();
            isInitialized = false;
        }
        catch (IOException ioe) {
            throw new IllegalStateException("Could not close BinaryStdIn", ioe);
        }
    }

   /**
     * Returns true if standard input is empty.
     * @return true if and only if standard input is empty
     */
    public static boolean isEmpty() {
        if (!isInitialized) initialize();
        return buffer == EOF;
    }

   /**
     * Reads the next bit of data from standard input and return as a boolean.
     *
     * @return the next bit of data from standard input as a {@code boolean}
     * @throws NoSuchElementException if standard input is empty
     */
    public static boolean readBoolean() {
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");
        n--;
        boolean bit = ((buffer >> n) & 1) == 1;
        if (n == 0) fillBuffer();
        return bit;
    }

   /**
     * Reads the next 8 bits from standard input and return as an 8-bit char.
     * Note that {@code char} is a 16-bit type;
     * to read the next 16 bits as a char, use {@code readChar(16)}.
     *
     * @return the next 8 bits of data from standard input as a {@code char}
     * @throws NoSuchElementException if there are fewer than 8 bits available on standard input
     */
    public static char readChar() {
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");

        // special case when aligned byte
        if (n == 8) {
            int x = buffer;
            fillBuffer();
            return (char) (x & 0xff);
        }

        // combine last n bits of current buffer with first 8-n bits of new buffer
        int x = buffer;
        x <<= (8 - n);
        int oldN = n;
        fillBuffer();
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");
        n = oldN;
        x |= (buffer >>> n);
        return (char) (x & 0xff);
        // the above code doesn't quite work for the last character if n = 8
        // because buffer will be -1, so there is a special case for aligned byte
    }

   /**
     * Reads the next r bits from standard input and return as an r-bit character.
     *
     * @param  r number of bits to read.
     * @return the next r bits of data from standard input as a {@code char}
     * @throws NoSuchElementException if there are fewer than {@code r} bits available on standard input
     * @throws IllegalArgumentException unless {@code 1 <= r <= 16}
     */
    public static char readChar(int r) {
        if (r < 1 || r > 16) throw new IllegalArgumentException("Illegal value of r = " + r);

        // optimize r = 8 case
        if (r == 8) return readChar();

        char x = 0;
        for (int i = 0; i < r; i++) {
            x <<= 1;
            boolean bit = readBoolean();
            if (bit) x |= 1;
        }
        return x;
    }

   /**
     * Reads the remaining bytes of data from standard input and return as a string. 
     *
     * @return the remaining bytes of data from standard input as a {@code String}
     * @throws NoSuchElementException if standard input is empty or if the number of bits
     *         available on standard input is not a multiple of 8 (byte-aligned)
     */
    public static String readString() {
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");

        StringBuilder sb = new StringBuilder();
        while (!isEmpty()) {
            char c = readChar();
            sb.append(c);
        }
        return sb.toString();
    }


   /**
     * Reads the next 16 bits from standard input and return as a 16-bit short.
     *
     * @return the next 16 bits of data from standard input as a {@code short}
     * @throws NoSuchElementException if there are fewer than 16 bits available on standard input
     */
    public static short readShort() {
        short x = 0;
        for (int i = 0; i < 2; i++) {
            char c = readChar();
            x <<= 8;
            x |= c;
        }
        return x;
    }

   /**
     * Reads the next 32 bits from standard input and return as a 32-bit int.
     *
     * @return the next 32 bits of data from standard input as a {@code int}
     * @throws NoSuchElementException if there are fewer than 32 bits available on standard input
     */
    public static int readInt() {
        int x = 0;
        for (int i = 0; i < 4; i++) {
            char c = readChar();
            x <<= 8;
            x |= c;
        }
        return x;
    }

   /**
     * Reads the next r bits from standard input and return as an r-bit int.
     *
     * @param  r number of bits to read.
     * @return the next r bits of data from standard input as a {@code int}
     * @throws NoSuchElementException if there are fewer than {@code r} bits available on standard input
     * @throws IllegalArgumentException unless {@code 1 <= r <= 32}
     */
    public static int readInt(int r) {
        if (r < 1 || r > 32) throw new IllegalArgumentException("Illegal value of r = " + r);

        // optimize r = 32 case
        if (r == 32) return readInt();

        int x = 0;
        for (int i = 0; i < r; i++) {
            x <<= 1;
            boolean bit = readBoolean();
            if (bit) x |= 1;
        }
        return x;
    }

   /**
     * Reads the next 64 bits from standard input and return as a 64-bit long.
     *
     * @return the next 64 bits of data from standard input as a {@code long}
     * @throws NoSuchElementException if there are fewer than 64 bits available on standard input
     */
    public static long readLong() {
        long x = 0;
        for (int i = 0; i < 8; i++) {
            char c = readChar();
            x <<= 8;
            x |= c;
        }
        return x;
    }


   /**
     * Reads the next 64 bits from standard input and return as a 64-bit double.
     *
     * @return the next 64 bits of data from standard input as a {@code double}
     * @throws NoSuchElementException if there are fewer than 64 bits available on standard input
     */
    public static double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

   /**
     * Reads the next 32 bits from standard input and return as a 32-bit float.
     *
     * @return the next 32 bits of data from standard input as a {@code float}
     * @throws NoSuchElementException if there are fewer than 32 bits available on standard input
     */
    public static float readFloat() {
        return Float.intBitsToFloat(readInt());
    }


   /**
     * Reads the next 8 bits from standard input and return as an 8-bit byte.
     *
     * @return the next 8 bits of data from standard input as a {@code byte}
     * @throws NoSuchElementException if there are fewer than 8 bits available on standard input
     */
    public static byte readByte() {
        char c = readChar();
        return (byte) (c & 0xff);
    }
    
   /**
     * Test client. Reads in a binary input file from standard input and writes
     * it to standard output.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        // read one 8-bit char at a time
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            BinaryStdOut.write(c);
        }
        BinaryStdOut.flush();
    }
}

/******************************************************************************
 *  Copyright 2002-2018, Robert Sedgewick and Kevin Wayne.
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
