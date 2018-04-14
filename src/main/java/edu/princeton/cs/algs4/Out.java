/******************************************************************************
 *  Compilation:  javac Out.java
 *  Execution:    java Out
 *  Dependencies: none
 *
 *  Writes data of various types to: stdout, file, or socket.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;

/**
 *  This class provides methods for writing strings and numbers to
 *  various output streams, including standard output, file, and sockets.
 *  <p>
 *  For additional documentation, see
 *  <a href="https://introcs.cs.princeton.edu/31datatype">Section 3.1</a> of
 *  <i>Computer Science: An Interdisciplinary Approach</i>
 *  by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Out {

    // force Unicode UTF-8 encoding; otherwise it's system dependent
    private static final String CHARSET_NAME = "UTF-8";

    // assume language = English, country = US for consistency with In
    private static final Locale LOCALE = Locale.US;

    private PrintWriter out;

   /**
     * Initializes an output stream from a {@link OutputStream}.
     *
     * @param  os the {@code OutputStream}
     */
    public Out(OutputStream os) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET_NAME);
            out = new PrintWriter(osw, true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

   /**
     * Initializes an output stream from standard output.
     */
    public Out() {
        this(System.out);
    }

   /**
     * Initializes an output stream from a socket.
     *
     * @param  socket the socket
     */
    public Out(Socket socket) {
        try {
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET_NAME);
            out = new PrintWriter(osw, true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

   /**
     * Initializes an output stream from a file.
     *
     * @param  filename the name of the file
     */
    public Out(String filename) {
        try {
            OutputStream os = new FileOutputStream(filename);
            OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET_NAME);
            out = new PrintWriter(osw, true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

   /**
     * Closes the output stream.
     */
    public void close() {
        out.close();
    }

   /**
     * Terminates the current line by printing the line-separator string.
     */
    public void println() {
        out.println();
    }

   /**
     * Prints an object to this output stream and then terminates the line.
     *
     * @param x the object to print
     */
    public void println(Object x) {
        out.println(x);
    }

   /**
     * Prints a boolean to this output stream and then terminates the line.
     *
     * @param x the boolean to print
     */
    public void println(boolean x) {
        out.println(x);
    }

   /**
     * Prints a character to this output stream and then terminates the line.
     *
     * @param x the character to print
     */
    public void println(char x) {
        out.println(x);
    }

   /**
     * Prints a double to this output stream and then terminates the line.
     *
     * @param x the double to print
     */
    public void println(double x) {
        out.println(x);
    }

   /**
     * Prints a float to this output stream and then terminates the line.
     *
     * @param x the float to print
     */
    public void println(float x) {
        out.println(x);
    }

   /**
     * Prints an integer to this output stream and then terminates the line.
     *
     * @param x the integer to print
     */
    public void println(int x) {
        out.println(x);
    }

   /**
     * Prints a long to this output stream and then terminates the line.
     *
     * @param x the long to print
     */
    public void println(long x) {
        out.println(x);
    }

   /**
     * Prints a byte to this output stream and then terminates the line.
     * <p>
     * To write binary data, see {@link BinaryOut}.
     *
     * @param x the byte to print
     */
    public void println(byte x) {
        out.println(x);
    }



   /**
     * Flushes this output stream.
     */
    public void print() {
        out.flush();
    }

   /**
     * Prints an object to this output stream and flushes this output stream.
     * 
     * @param x the object to print
     */
    public void print(Object x) {
        out.print(x);
        out.flush();
    }

   /**
     * Prints a boolean to this output stream and flushes this output stream.
     * 
     * @param x the boolean to print
     */
    public void print(boolean x) {
        out.print(x);
        out.flush();
    }

   /**
     * Prints a character to this output stream and flushes this output stream.
     * 
     * @param x the character to print
     */
    public void print(char x) {
        out.print(x);
        out.flush();
    }

   /**
     * Prints a double to this output stream and flushes this output stream.
     * 
     * @param x the double to print
     */
    public void print(double x) {
        out.print(x);
        out.flush();
    }

   /**
     * Prints a float to this output stream and flushes this output stream.
     * 
     * @param x the float to print
     */
    public void print(float x) {
        out.print(x);
        out.flush();
    }

   /**
     * Prints an integer to this output stream and flushes this output stream.
     * 
     * @param x the integer to print
     */
    public void print(int x) {
        out.print(x);
        out.flush();
    }

   /**
     * Prints a long integer to this output stream and flushes this output stream.
     * 
     * @param x the long integer to print
     */
    public void print(long x) {
        out.print(x);
        out.flush();
    }

   /**
     * Prints a byte to this output stream and flushes this output stream.
     * 
     * @param x the byte to print
     */
    public void print(byte x) {
        out.print(x);
        out.flush();
    }

   /**
     * Prints a formatted string to this output stream, using the specified format
     * string and arguments, and then flushes this output stream.
     *
     * @param format the format string
     * @param args   the arguments accompanying the format string
     */
    public void printf(String format, Object... args) {
        out.printf(LOCALE, format, args);
        out.flush();
    }

   /**
     * Prints a formatted string to this output stream, using the specified
     * locale, format string, and arguments, and then flushes this output stream.
     *
     * @param locale the locale
     * @param format the format string
     * @param args   the arguments accompanying the format string
     */
    public void printf(Locale locale, String format, Object... args) {
        out.printf(locale, format, args);
        out.flush();
    }


   /**
     * A test client.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        Out out;

        // write to stdout
        out = new Out();
        out.println("Test 1");
        out.close();

        // write to a file
        out = new Out("test.txt");
        out.println("Test 2");
        out.close();
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
