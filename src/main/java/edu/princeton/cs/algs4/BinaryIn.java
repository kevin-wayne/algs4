/******************************************************************************
 *  Compilation:  javac BinaryIn.java
 *  Execution:    java BinaryIn input output
 *  Dependencies: none
 *
 *  This library is for reading binary data from an input stream.
 *
 *  % java BinaryIn https://introcs.cs.princeton.edu/java/cover.png output.png
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.NoSuchElementException;

/**
 *  <i>Binary input</i>. This class provides methods for reading
 *  in bits from a binary input stream, either
 *  one bit at a time (as a {@code boolean}),
 *  8 bits at a time (as a {@code byte} or {@code char}),
 *  16 bits at a time (as a {@code short}),
 *  32 bits at a time (as an {@code int} or {@code float}), or
 *  64 bits at a time (as a {@code double} or {@code long}).
 *  <p>
 *  The binary input stream can be from standard input, a filename,
 *  a URL name, a Socket, or an InputStream.
 *  <p>
 *  All primitive types are assumed to be represented using their
 *  standard Java representations, in big-endian (most significant
 *  byte first) order.
 *  <p>
 *  The client should not intermix calls to {@code BinaryIn} with calls
 *  to {@code In}; otherwise unexpected behavior will result.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public final class BinaryIn {
    private static final int EOF = -1;   // end of file

    private BufferedInputStream in;      // the input stream
    private int buffer;                  // one character buffer
    private int n;                       // number of bits left in buffer

   /**
     * Initializes a binary input stream from standard input.
     */
    public BinaryIn() {
        in = new BufferedInputStream(System.in);
        fillBuffer();
    }

   /**
     * Initializes a binary input stream from an {@code InputStream}.
     *
     * @param is the {@code InputStream} object
     */
    public BinaryIn(InputStream is) {
        in = new BufferedInputStream(is);
        fillBuffer();
    }

   /**
     * Initializes a binary input stream from a socket.
     *
     * @param socket the socket
     */
    public BinaryIn(Socket socket) {
        try {
            InputStream is = socket.getInputStream();
            in = new BufferedInputStream(is);
            fillBuffer();
        }
        catch (IOException ioe) {
            System.err.println("Could not open " + socket);
        }
    }

   /**
     * Initializes a binary input stream from a URL.
     *
     * @param url the URL
     */
    public BinaryIn(URL url) {
        try {
            URLConnection site = url.openConnection();
            InputStream is     = site.getInputStream();
            in = new BufferedInputStream(is);
            fillBuffer();
        }
        catch (IOException ioe) {
            System.err.println("Could not open " + url);
        }
    }

   /**
     * Initializes a binary input stream from a filename or URL name.
     *
     * @param name the name of the file or URL
     */
    public BinaryIn(String name) {

        try {
            // first try to read file from local file system
            File file = new File(name);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                in = new BufferedInputStream(fis);
                fillBuffer();
                return;
            }

            // next try for files included in jar
            URL url = getClass().getResource(name);

            // or URL from web
            if (url == null) {
                url = new URL(name);
            }

            URLConnection site = url.openConnection();
            InputStream is     = site.getInputStream();
            in = new BufferedInputStream(is);
            fillBuffer();
        }
        catch (IOException ioe) {
            System.err.println("Could not open " + name);
        }
    }

    private void fillBuffer() {
        try {
            buffer = in.read();
            n = 8;
        }
        catch (IOException e) {
            System.err.println("EOF");
            buffer = EOF;
            n = -1;
        }
    }

    /**
     * Returns true if this binary input stream exists.
     *
     * @return {@code true} if this binary input stream exists;
     *         {@code false} otherwise
     */
    public boolean exists()  {
        return in != null;
    }

   /**
     * Returns true if this binary input stream is empty.
     *
     * @return {@code true} if this binary input stream is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return buffer == EOF;
    }

   /**
     * Reads the next bit of data from this binary input stream and return as a boolean.
     *
     * @return the next bit of data from this binary input stream as a {@code boolean}
     * @throws NoSuchElementException if this binary input stream is empty
     */
    public boolean readBoolean() {
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");
        n--;
        boolean bit = ((buffer >> n) & 1) == 1;
        if (n == 0) fillBuffer();
        return bit;
    }

   /**
     * Reads the next 8 bits from this binary input stream and return as an 8-bit char.
     *
     * @return the next 8 bits of data from this binary input stream as a {@code char}
     * @throws NoSuchElementException if there are fewer than 8 bits available
     */
    public char readChar() {
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");

        // special case when aligned byte
        if (n == 8) {
            int x = buffer;
            fillBuffer();
            return (char) (x & 0xff);
        }

        // combine last N bits of current buffer with first 8-N bits of new buffer
        int x = buffer;
        x <<= (8 - n);
        int oldN = n;
        fillBuffer();
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");
        n = oldN;
        x |= (buffer >>> n);
        return (char) (x & 0xff);
        // the above code doesn't quite work for the last character if N = 8
        // because buffer will be -1
    }


   /**
     * Reads the next <em>r</em> bits from this binary input stream and return
     * as an <em>r</em>-bit character.
     *
     * @param  r number of bits to read
     * @return the next {@code r} bits of data from this binary input streamt as a {@code char}
     * @throws NoSuchElementException if there are fewer than {@code r} bits available
     * @throws IllegalArgumentException unless {@code 1 <= r <= 16}
     */
    public char readChar(int r) {
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
     * Reads the remaining bytes of data from this binary input stream and return as a string.
     *
     * @return the remaining bytes of data from this binary input stream as a {@code String}
     * @throws NoSuchElementException if this binary input stream is empty or if the number of bits
     *         available is not a multiple of 8 (byte-aligned)
     */
    public String readString() {
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");

        StringBuilder sb = new StringBuilder();
        while (!isEmpty()) {
            char c = readChar();
            sb.append(c);
        }
        return sb.toString();
    }


   /**
     * Reads the next 16 bits from this binary input stream and return as a 16-bit short.
     *
     * @return the next 16 bits of data from this binary input stream as a {@code short}
     * @throws NoSuchElementException if there are fewer than 16 bits available
     */
    public short readShort() {
        short x = 0;
        for (int i = 0; i < 2; i++) {
            char c = readChar();
            x <<= 8;
            x |= c;
        }
        return x;
    }

   /**
     * Reads the next 32 bits from this binary input stream and return as a 32-bit int.
     *
     * @return the next 32 bits of data from this binary input stream as a {@code int}
     * @throws NoSuchElementException if there are fewer than 32 bits available
     */
    public int readInt() {
        int x = 0;
        for (int i = 0; i < 4; i++) {
            char c = readChar();
            x <<= 8;
            x |= c;
        }
        return x;
    }

   /**
     * Reads the next <em>r</em> bits from this binary input stream return
     * as an <em>r</em>-bit int.
     *
     * @param  r number of bits to read
     * @return the next {@code r} bits of data from this binary input stream as a {@code int}
     * @throws NoSuchElementException if there are fewer than r bits available
     * @throws IllegalArgumentException unless {@code 1 <= r <= 32}
     */
    public int readInt(int r) {
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
     * Reads the next 64 bits from this binary input stream and return as a 64-bit long.
     *
     * @return the next 64 bits of data from this binary input stream as a {@code long}
     * @throws NoSuchElementException if there are fewer than 64 bits available
     */
    public long readLong() {
        long x = 0;
        for (int i = 0; i < 8; i++) {
            char c = readChar();
            x <<= 8;
            x |= c;
        }
        return x;
    }

   /**
     * Reads the next 64 bits from this binary input stream and return as a 64-bit double.
     *
     * @return the next 64 bits of data from this binary input stream as a {@code double}
     * @throws NoSuchElementException if there are fewer than 64 bits available
     */
    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

   /**
     * Reads the next 32 bits from this binary input stream and return as a 32-bit float.
     *
     * @return the next 32 bits of data from this binary input stream as a {@code float}
     * @throws NoSuchElementException if there are fewer than 32 bits available
     */
    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }


   /**
     * Reads the next 8 bits from this binary input stream and return as an 8-bit byte.
     *
     * @return the next 8 bits of data from this binary input stream as a {@code byte}
     * @throws NoSuchElementException if there are fewer than 8 bits available
     */
    public byte readByte() {
        char c = readChar();
        return (byte) (c & 0xff);
    }

   /**
     * Unit tests the {@code BinaryIn} data type.
     * Reads the name of a file or URL (first command-line argument)
     * and writes it to a file (second command-line argument).
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        BinaryIn  in  = new BinaryIn(args[0]);
        BinaryOut out = new BinaryOut(args[1]);

        // read one 8-bit char at a time
        while (!in.isEmpty()) {
            char c = in.readChar();
            out.write(c);
        }
        out.flush();
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
