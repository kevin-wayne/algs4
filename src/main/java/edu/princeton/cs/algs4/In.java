/******************************************************************************
 *  Compilation:  javac In.java
 *  Execution:    java In   (basic test --- see source for required files)
 *  Dependencies: none
 *
 *  Reads in data of various types from standard input, files, and URLs.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
// import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *  <i>Input</i>. This class provides methods for reading strings
 *  and numbers from standard input, file input, URLs, and sockets. 
 *  <p>
 *  The Locale used is: language = English, country = US. This is consistent
 *  with the formatting conventions with Java floating-point literals,
 *  command-line arguments (via {@link Double#parseDouble(String)})
 *  and standard output. 
 *  <p>
 *  For additional documentation, see 
 *  <a href="http://introcs.cs.princeton.edu/31datatype">Section 3.1</a> of
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i> 
 *  by Robert Sedgewick and Kevin Wayne.
 *  <p>
 *  Like {@link Scanner}, reading a token also consumes preceding Java
 *  whitespace, reading a full line consumes
 *  the following end-of-line delimeter, while reading a character consumes
 *  nothing extra. 
 *  <p>
 *  Whitespace is defined in {@link Character#isWhitespace(char)}. Newlines
 *  consist of \n, \r, \r\n, and Unicode hex code points 0x2028, 0x2029, 0x0085;
 *  see <tt><a href="http://www.docjar.com/html/api/java/util/Scanner.java.html">
 *  Scanner.java</a></tt> (NB: Java 6u23 and earlier uses only \r, \r, \r\n).
 *
 *  @author David Pritchard
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public final class In {
    
    ///// begin: section (1 of 2) of code duplicated from In to StdIn.
    
    // assume Unicode UTF-8 encoding
    private static final String CHARSET_NAME = "UTF-8";

    // assume language = English, country = US for consistency with System.out.
    private static final Locale LOCALE = Locale.US;

    // the default token separator; we maintain the invariant that this value 
    // is held by the scanner's delimiter between calls
    private static final Pattern WHITESPACE_PATTERN
        = Pattern.compile("\\p{javaWhitespace}+");

    // makes whitespace characters significant 
    private static final Pattern EMPTY_PATTERN
        = Pattern.compile("");

    // used to read the entire input. source:
    // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
    private static final Pattern EVERYTHING_PATTERN
        = Pattern.compile("\\A");

    //// end: section (1 of 2) of code duplicated from In to StdIn.

    private Scanner scanner;

   /**
     * Initializes an input stream from standard input.
     */
    public In() {
        scanner = new Scanner(new BufferedInputStream(System.in), CHARSET_NAME);
        scanner.useLocale(LOCALE);
    }

   /**
     * Initializes an input stream from a socket.
     *
     * @param  socket the socket
     */
    public In(java.net.Socket socket) {
        try {
            InputStream is = socket.getInputStream();
            scanner = new Scanner(new BufferedInputStream(is), CHARSET_NAME);
            scanner.useLocale(LOCALE);
        }
        catch (IOException ioe) {
            System.err.println("Could not open " + socket);
        }
    }

   /**
     * Initializes an input stream from a URL.
     *
     * @param  url the URL
     */
    public In(URL url) {
        try {
            URLConnection site = url.openConnection();
            InputStream is     = site.getInputStream();
            scanner            = new Scanner(new BufferedInputStream(is), CHARSET_NAME);
            scanner.useLocale(LOCALE);
        }
        catch (IOException ioe) {
            System.err.println("Could not open " + url);
        }
    }

   /**
     * Initializes an input stream from a file.
     *
     * @param  file the file
     */
    public In(File file) {
        try {
            scanner = new Scanner(file, CHARSET_NAME);
            scanner.useLocale(LOCALE);
        }
        catch (IOException ioe) {
            System.err.println("Could not open " + file);
        }
    }


   /**
     * Initializes an input stream from a filename or web page name.
     *
     * @param  s the filename or web page name
     */
    public In(String s) {
        try {
            // first try to read file from local file system
            File file = new File(s);
            if (file.exists()) {
                scanner = new Scanner(file, CHARSET_NAME);
                scanner.useLocale(LOCALE);
                return;
            }

            // next try for files included in jar
            URL url = getClass().getResource(s);

            // or URL from web
            if (url == null) {
                url = new URL(s);
            }

            URLConnection site = url.openConnection();

            // in order to set User-Agent, replace above line with these two
            // HttpURLConnection site = (HttpURLConnection) url.openConnection();
            // site.addRequestProperty("User-Agent", "Mozilla/4.76");

            InputStream is     = site.getInputStream();
            scanner            = new Scanner(new BufferedInputStream(is), CHARSET_NAME);
            scanner.useLocale(LOCALE);
        }
        catch (IOException ioe) {
            System.err.println("Could not open " + s);
        }
    }

    /**
     * Initializes an input stream from a given {@link Scanner} source; use with 
     * <tt>new Scanner(String)</tt> to read from a string.
     * <p>
     * Note that this does not create a defensive copy, so the
     * scanner will be mutated as you read on. 
     *
     * @param  scanner the scanner
     */
    public In(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Returns true if this input stream exists.
     *
     * @return <tt>true</tt> if this input stream exists; <tt>false</tt> otherwise
     */
    public boolean exists()  {
        return scanner != null;
    }
    
    ////  begin: section (2 of 2) of code duplicated from In to StdIn,
    ////  with all methods changed from "public" to "public static".

   /**
     * Returns true if input stream is empty (except possibly whitespace).
     * Use this to know whether the next call to {@link #readString()}, 
     * {@link #readDouble()}, etc will succeed.
     *
     * @return <tt>true</tt> if this input stream is empty (except possibly whitespace);
     *         <tt>false</tt> otherwise
     */
    public boolean isEmpty() {
        return !scanner.hasNext();
    }

   /**
     * Returns true if this input stream has a next line.
     * Use this to know whether the next call to {@link #readLine()} will succeed.
     * <p>
     * Functionally equivalent to {@link #hasNextChar()}.
     *
     * @return <tt>true</tt> if this input stream has a next line;
     *         <tt>false</tt> otherwise
     */
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /**
     * Returns true if this input stream is empty (including whitespace).
     * Use this to know  whether the next call to {@link #readChar()} will succeed.
     * <p>
     * Functionally equivalent to {@link #hasNextLine()}.
     *
     * @return <tt>true</tt> if this input stream is empty (including whitespace);
     *         <tt>false</tt> otherwise
     */
    public boolean hasNextChar() {
        scanner.useDelimiter(EMPTY_PATTERN);
        boolean result = scanner.hasNext();
        scanner.useDelimiter(WHITESPACE_PATTERN);
        return result;
    }


   /**
     * Reads and returns the next line in this input stream.
     *
     * @return the next line in this input stream; <tt>null</tt> if no such line
     */
    public String readLine() {
        String line;
        try {
            line = scanner.nextLine();
        }
        catch (NoSuchElementException e) {
            line = null;
        }
        return line;
    }

    /**
     * Reads and returns the next character in this input stream.
     *
     * @return the next character in this input stream
     */
    public char readChar() {
        scanner.useDelimiter(EMPTY_PATTERN);
        String ch = scanner.next();
        assert ch.length() == 1 : "Internal (Std)In.readChar() error!"
            + " Please contact the authors.";
        scanner.useDelimiter(WHITESPACE_PATTERN);
        return ch.charAt(0);
    }  


   /**
     * Reads and returns the remainder of this input stream, as a string.
     *
     * @return the remainder of this input stream, as a string
     */
    public String readAll() {
        if (!scanner.hasNextLine())
            return "";

        String result = scanner.useDelimiter(EVERYTHING_PATTERN).next();
        // not that important to reset delimeter, since now scanner is empty
        scanner.useDelimiter(WHITESPACE_PATTERN); // but let's do it anyway
        return result;
    }


   /**
     * Reads the next token from this input stream and returns it as a <tt>String</tt>.
     *
     * @return the next <tt>String</tt> in this input stream
     */
    public String readString() {
        return scanner.next();
    }

   /**
     * Reads the next token from this input stream, parses it as a <tt>int</tt>,
     * and returns the <tt>int</tt>.
     *
     * @return the next <tt>int</tt> in this input stream
     */
    public int readInt() {
        return scanner.nextInt();
    }

   /**
     * Reads the next token from this input stream, parses it as a <tt>double</tt>,
     * and returns the <tt>double</tt>.
     *
     * @return the next <tt>double</tt> in this input stream
     */
    public double readDouble() {
        return scanner.nextDouble();
    }

   /**
     * Reads the next token from this input stream, parses it as a <tt>float</tt>,
     * and returns the <tt>float</tt>.
     *
     * @return the next <tt>float</tt> in this input stream
     */
    public float readFloat() {
        return scanner.nextFloat();
    }

   /**
     * Reads the next token from this input stream, parses it as a <tt>long</tt>,
     * and returns the <tt>long</tt>.
     *
     * @return the next <tt>long</tt> in this input stream
     */
    public long readLong() {
        return scanner.nextLong();
    }

   /**
     * Reads the next token from this input stream, parses it as a <tt>short</tt>,
     * and returns the <tt>short</tt>.
     *
     * @return the next <tt>short</tt> in this input stream
     */
    public short readShort() {
        return scanner.nextShort();
    }

   /**
     * Reads the next token from this input stream, parses it as a <tt>byte</tt>,
     * and returns the <tt>byte</tt>.
     * <p>
     * To read binary data, use {@link BinaryIn}.
     *
     * @return the next <tt>byte</tt> in this input stream
     */
    public byte readByte() {
        return scanner.nextByte();
    }

    /**
     * Reads the next token from this input stream, parses it as a <tt>boolean</tt>
     * (interpreting either <tt>"true"</tt> or <tt>"1"</tt> as <tt>true</tt>,
     * and either <tt>"false"</tt> or <tt>"0"</tt> as <tt>false</tt>).
     *
     * @return the next <tt>boolean</tt> in this input stream
     */
    public boolean readBoolean() {
        String s = readString();
        if (s.equalsIgnoreCase("true"))  return true;
        if (s.equalsIgnoreCase("false")) return false;
        if (s.equals("1"))               return true;
        if (s.equals("0"))               return false;
        throw new InputMismatchException();
    }

    /**
     * Reads all remaining tokens from this input stream and returns them as
     * an array of strings.
     *
     * @return all remaining tokens in this input stream, as an array of strings
     */
    public String[] readAllStrings() {
        // we could use readAll.trim().split(), but that's not consistent
        // since trim() uses characters 0x00..0x20 as whitespace
        String[] tokens = WHITESPACE_PATTERN.split(readAll());
        if (tokens.length == 0 || tokens[0].length() > 0)
            return tokens;
        String[] decapitokens = new String[tokens.length-1];
        for (int i = 0; i < tokens.length-1; i++)
            decapitokens[i] = tokens[i+1];
        return decapitokens;
    }

    /**
     * Reads all remaining lines from this input stream and returns them as
     * an array of strings.
     *
     * @return all remaining lines in this input stream, as an array of strings
     */
    public String[] readAllLines() {
        ArrayList<String> lines = new ArrayList<String>();
        while (hasNextLine()) {
            lines.add(readLine());
        }
        return lines.toArray(new String[0]);
    }


    /**
     * Reads all remaining tokens from this input stream, parses them as integers,
     * and returns them as an array of integers.
     *
     * @return all remaining lines in this input stream, as an array of integers
     */
    public int[] readAllInts() {
        String[] fields = readAllStrings();
        int[] vals = new int[fields.length];
        for (int i = 0; i < fields.length; i++)
            vals[i] = Integer.parseInt(fields[i]);
        return vals;
    }

    /**
     * Reads all remaining tokens from this input stream, parses them as doubles,
     * and returns them as an array of doubles.
     *
     * @return all remaining lines in this input stream, as an array of doubles
     */
    public double[] readAllDoubles() {
        String[] fields = readAllStrings();
        double[] vals = new double[fields.length];
        for (int i = 0; i < fields.length; i++)
            vals[i] = Double.parseDouble(fields[i]);
        return vals;
    }
    
    ///// end: section (2 of 2) of code duplicated from In to StdIn */

   /**
     * Closes this input stream.
     */
    public void close() {
        scanner.close();  
    }

    /**
     * Reads all integers from a file and returns them as
     * an array of integers.
     *
     * @param      filename the name of the file
     * @return     the integers in the file
     * @deprecated Replaced by <tt>new In(filename)</tt>.{@link #readAllInts()}.
     */
    public static int[] readInts(String filename) {
        return new In(filename).readAllInts();
    }

   /**
     * Reads all doubles from a file and returns them as
     * an array of doubles.
     *
     * @param      filename the name of the file
     * @return     the doubles in the file
     * @deprecated Replaced by <tt>new In(filename)</tt>.{@link #readAllDoubles()}.
     */
    public static double[] readDoubles(String filename) {
        return new In(filename).readAllDoubles();
    }

   /**
     * Reads all strings from a file and returns them as
     * an array of strings.
     *
     * @param      filename the name of the file
     * @return     the strings in the file
     * @deprecated Replaced by <tt>new In(filename)</tt>.{@link #readAllStrings()}.
     */
    public static String[] readStrings(String filename) {
        return new In(filename).readAllStrings();
    }

    /**
     * Reads all integers from standard input and returns them
     * an array of integers.
     *
     * @return     the integers on standard input
     * @deprecated Replaced by {@link StdIn#readAllInts()}.
     */
    public static int[] readInts() {
        return new In().readAllInts();
    }

   /**
     * Reads all doubles from standard input and returns them as
     * an array of doubles.
     *
     * @return     the doubles on standard input
     * @deprecated Replaced by {@link StdIn#readAllDoubles()}.
     */
    public static double[] readDoubles() {
        return new In().readAllDoubles();
    }

   /**
     * Reads all strings from standard input and returns them as
     *  an array of strings.
     *
     * @return     the strings on standard input
     * @deprecated Replaced by {@link StdIn#readAllStrings()}.
     */
    public static String[] readStrings() {
        return new In().readAllStrings();
    }
    
   /**
     * Unit tests the <tt>In</tt> data type.
     */
    public static void main(String[] args) {
        In in;
        String urlName = "http://introcs.cs.princeton.edu/stdlib/InTest.txt";

        // read from a URL
        System.out.println("readAll() from URL " + urlName);
        System.out.println("---------------------------------------------------------------------------");
        try {
            in = new In(urlName);
            System.out.println(in.readAll());
        }
        catch (Exception e) {
            System.out.println(e);
        }
        System.out.println();

        // read one line at a time from URL
        System.out.println("readLine() from URL " + urlName);
        System.out.println("---------------------------------------------------------------------------");
        try {
            in = new In(urlName);
            while (!in.isEmpty()) {
                String s = in.readLine();
                System.out.println(s);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        System.out.println();

        // read one string at a time from URL
        System.out.println("readString() from URL " + urlName);
        System.out.println("---------------------------------------------------------------------------");
        try {
            in = new In(urlName);
            while (!in.isEmpty()) {
                String s = in.readString();
                System.out.println(s);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        System.out.println();


        // read one line at a time from file in current directory
        System.out.println("readLine() from current directory");
        System.out.println("---------------------------------------------------------------------------");
        try {
            in = new In("./InTest.txt");
            while (!in.isEmpty()) {
                String s = in.readLine();
                System.out.println(s);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        System.out.println();


        // read one line at a time from file using relative path
        System.out.println("readLine() from relative path");
        System.out.println("---------------------------------------------------------------------------");
        try {
            in = new In("../stdlib/InTest.txt");
            while (!in.isEmpty()) {
                String s = in.readLine();
                System.out.println(s);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        System.out.println();

        // read one char at a time
        System.out.println("readChar() from file");
        System.out.println("---------------------------------------------------------------------------");
        try {
            in = new In("InTest.txt");
            while (!in.isEmpty()) {
                char c = in.readChar();
                System.out.print(c);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        System.out.println();
        System.out.println();

        // read one line at a time from absolute OS X / Linux path
        System.out.println("readLine() from absolute OS X / Linux path");
        System.out.println("---------------------------------------------------------------------------");
        in = new In("/n/fs/introcs/www/java/stdlib/InTest.txt");
        try {
            while (!in.isEmpty()) {
                String s = in.readLine();
                System.out.println(s);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        System.out.println();


        // read one line at a time from absolute Windows path
        System.out.println("readLine() from absolute Windows path");
        System.out.println("---------------------------------------------------------------------------");
        try {
            in = new In("G:\\www\\introcs\\stdlib\\InTest.txt");
            while (!in.isEmpty()) {
                String s = in.readLine();
                System.out.println(s);
            }
            System.out.println();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        System.out.println();

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
