/******************************************************************************
 *  Compilation:  javac BinaryOut.java
 *  Execution:    java BinaryOut
 *  Dependencies: none
 *
 *  Write binary data to an output stream, either one 1-bit boolean,
 *  one 8-bit char, one 32-bit int, one 64-bit double, one 32-bit float,
 *  or one 64-bit long at a time. The output stream can be standard
 *  output, a file, an OutputStream or a Socket.
 *
 *  The bytes written are not aligned.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 *  <i>Binary output</i>. This class provides methods for converting
 *  primtive type variables (<tt>boolean</tt>, <tt>byte</tt>, <tt>char</tt>,
 *  <tt>int</tt>, <tt>long</tt>, <tt>float</tt>, and <tt>double</tt>)
 *  to sequences of bits and writing them to an output stream.
 *  The output stream can be standard output, a file, an OutputStream or a Socket.
 *  Uses big-endian (most-significant byte first).
 *  <p>
 *  The client must <tt>flush()</tt> the output stream when finished writing bits.
 *  <p>
 *  The client should not intermixing calls to <tt>BinaryOut</tt> with calls
 *  to <tt>Out</tt>; otherwise unexpected behavior will result.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public final class BinaryOut {

    private BufferedOutputStream out;  // the output stream
    private int buffer;                // 8-bit buffer of bits to write out
    private int n;                     // number of bits remaining in buffer


   /**
     * Initializes a binary output stream from standard output.
     */
    public BinaryOut() {
        out = new BufferedOutputStream(System.out);
    }

   /**
     * Initializes a binary output stream from an <tt>OutputStream</tt>.
     * @param os the <tt>OutputStream</tt>
     */
    public BinaryOut(OutputStream os) {
        out = new BufferedOutputStream(os);
    }

   /**
     * Initializes a binary output stream from a file.
     * @param filename the name of the file
     */
    public BinaryOut(String filename) {
        try {
            OutputStream os = new FileOutputStream(filename);
            out = new BufferedOutputStream(os);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

   /**
     * Initializes a binary output stream from a socket.
     * @param socket the socket
     */
    public BinaryOut(Socket socket) {
        try {
            OutputStream os = socket.getOutputStream();
            out = new BufferedOutputStream(os);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


   /**
     * Writes the specified bit to the binary output stream.
     * @param x the bit
     */
    private void writeBit(boolean x) {
        // add bit to buffer
        buffer <<= 1;
        if (x) buffer |= 1;

        // if buffer is full (8 bits), write out as a single byte
        n++;
        if (n == 8) clearBuffer();
    } 

   /**
     * Writes the 8-bit byte to the binary output stream.
     * @param x the byte
     */
    private void writeByte(int x) {
        assert x >= 0 && x < 256;

        // optimized if byte-aligned
        if (n == 0) {
            try {
                out.write(x);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        // otherwise write one bit at a time
        for (int i = 0; i < 8; i++) {
            boolean bit = ((x >>> (8 - i - 1)) & 1) == 1;
            writeBit(bit);
        }
    }

    // write out any remaining bits in buffer to the binary output stream, padding with 0s
    private void clearBuffer() {
        if (n == 0) return;
        if (n > 0) buffer <<= (8 - n);
        try {
            out.write(buffer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        n = 0;
        buffer = 0;
    }

   /**
     * Flushes the binary output stream, padding 0s if number of bits written so far
     * is not a multiple of 8.
     */
    public void flush() {
        clearBuffer();
        try {
            out.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

   /**
     * Closes and flushes the binary output stream.
     * Once it is closed, bits can no longer be written.
     */
    public void close() {
        flush();
        try {
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


   /**
     * Writes the specified bit to the binary output stream.
     * @param x the <tt>boolean</tt> to write
     */
    public void write(boolean x) {
        writeBit(x);
    } 

   /**
     * Writes the 8-bit byte to the binary output stream.
     * @param x the <tt>byte</tt> to write.
     */
    public void write(byte x) {
        writeByte(x & 0xff);
    }

   /**
     * Writes the 32-bit int to the binary output stream.
     * @param x the <tt>int</tt> to write
     */
    public void write(int x) {
        writeByte((x >>> 24) & 0xff);
        writeByte((x >>> 16) & 0xff);
        writeByte((x >>>  8) & 0xff);
        writeByte((x >>>  0) & 0xff);
    }

   /**
     * Writes the r-bit int to the binary output stream.
     *
     * @param  x the <tt>int</tt> to write
     * @param  r the number of relevant bits in the char
     * @throws IllegalArgumentException unless <tt>r</tt> is between 1 and 32
     * @throws IllegalArgumentException unless <tt>x</tt> is between 0 and 2<sup>r</sup> - 1
     */
    public void write(int x, int r) {
        if (r == 32) {
            write(x);
            return;
        }
        if (r < 1 || r > 32) throw new IllegalArgumentException("Illegal value for r = " + r);
        if (x >= (1 << r))   throw new IllegalArgumentException("Illegal " + r + "-bit char = " + x);
        for (int i = 0; i < r; i++) {
            boolean bit = ((x >>> (r - i - 1)) & 1) == 1;
            writeBit(bit);
        }
    }


   /**
     * Writes the 64-bit double to the binary output stream.
     * @param x the <tt>double</tt> to write
     */
    public void write(double x) {
        write(Double.doubleToRawLongBits(x));
    }

   /**
     * Writes the 64-bit long to the binary output stream.
     * @param x the <tt>long</tt> to write
     */
    public void write(long x) {
        writeByte((int) ((x >>> 56) & 0xff));
        writeByte((int) ((x >>> 48) & 0xff));
        writeByte((int) ((x >>> 40) & 0xff));
        writeByte((int) ((x >>> 32) & 0xff));
        writeByte((int) ((x >>> 24) & 0xff));
        writeByte((int) ((x >>> 16) & 0xff));
        writeByte((int) ((x >>>  8) & 0xff));
        writeByte((int) ((x >>>  0) & 0xff));
    }

   /**
     * Writes the 32-bit float to the binary output stream.
     * @param x the <tt>float</tt> to write
     */
    public void write(float x) {
        write(Float.floatToRawIntBits(x));
    }

   /**
     * Write the 16-bit int to the binary output stream.
     * @param x the <tt>short</tt> to write.
     */
    public void write(short x) {
        writeByte((x >>>  8) & 0xff);
        writeByte((x >>>  0) & 0xff);
    }

   /**
     * Writes the 8-bit char to the binary output stream.
     *
     * @param  x the <tt>char</tt> to write
     * @throws IllegalArgumentException unless <tt>x</tt> is betwen 0 and 255
     */
    public void write(char x) {
        if (x < 0 || x >= 256) throw new IllegalArgumentException("Illegal 8-bit char = " + x);
        writeByte(x);
    }

   /**
     * Writes the r-bit char to the binary output stream.
     *
     * @param  x the <tt>char</tt> to write
     * @param  r the number of relevant bits in the char
     * @throws IllegalArgumentException unless <tt>r</tt> is between 1 and 16
     * @throws IllegalArgumentException unless <tt>x</tt> is between 0 and 2<sup>r</sup> - 1
     */
    public void write(char x, int r) {
        if (r == 8) {
            write(x);
            return;
        }
        if (r < 1 || r > 16) throw new IllegalArgumentException("Illegal value for r = " + r);
        if (x >= (1 << r))   throw new IllegalArgumentException("Illegal " + r + "-bit char = " + x);
        for (int i = 0; i < r; i++) {
            boolean bit = ((x >>> (r - i - 1)) & 1) == 1;
            writeBit(bit);
        }
    }

   /**
     * Writes the string of 8-bit characters to the binary output stream.
     *
     * @param  s the <tt>String</tt> to write
     * @throws IllegalArgumentException if any character in the string is not
     *         between 0 and 255
     */
    public void write(String s) {
        for (int i = 0; i < s.length(); i++)
            write(s.charAt(i));
    }


   /**
     * Writes the String of r-bit characters to the binary output stream.
     * @param  s the <tt>String</tt> to write
     * @param  r the number of relevants bits in each character
     * @throws IllegalArgumentException unless r is between 1 and 16
     * @throws IllegalArgumentException if any character in the string is not
     *         between 0 and 2<sup>r</sup> - 1
     */
    public void write(String s, int r) {
        for (int i = 0; i < s.length(); i++)
            write(s.charAt(i), r);
    }


   /**
     * Test client. Read bits from standard input and write to the file
     * specified on command line.
     */
    public static void main(String[] args) {

        // create binary output stream to write to file
        String filename = args[0];
        BinaryOut out = new BinaryOut(filename);
        BinaryIn  in  = new BinaryIn();

        // read from standard input and write to file
        while (!in.isEmpty()) {
            char c = in.readChar();
            out.write(c);
        }
        out.flush();
    }

}

/******************************************************************************
 *  Copyright 2002-2015, Robert Sedgewick and Kevin Wayne.
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
