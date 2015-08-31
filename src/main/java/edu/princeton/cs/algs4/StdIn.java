/******************************************************************************
 *  Compilation:  javac StdIn.java
 *  Execution:    java StdIn   (interactive test of basic functionality)
 *  Dependencies: none
 *
 *  Reads in data of various types from standard input.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *  The <tt>StdIn</tt> class provides static methods for reading strings
 *  and numbers from standard input. See 
 *  <a href="http://introcs.cs.princeton.edu/15inout">Section 1.5</a> of
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i>
 *  by Robert Sedgewick and Kevin Wayne.
 *  <p>
 *  For uniformity across platforms, this class uses <tt>Locale.US</tt>
 *  for the locale and <tt>"UTF-8"</tt> for the character-set encoding.
 *  The English language locale is consistent with the formatting conventions
 *  for Java floating-point literals, command-line arguments
 *  (via {@link Double#parseDouble(String)}) and standard output.
 *  <p>
 *  Like {@link Scanner}, reading a <em>token</em> also consumes preceding Java
 *  whitespace; reading a line consumes the following end-of-line
 *  delimeter; reading a character consumes nothing extra. 
 *  <p>
 *  Whitespace is defined in {@link Character#isWhitespace(char)}. Newlines
 *  consist of \n, \r, \r\n, and Unicode hex code points 0x2028, 0x2029, 0x0085;
 *  see <tt><a href="http://www.docjar.com/html/api/java/util/Scanner.java.html">
 *  Scanner.java</a></tt> (NB: Java 6u23 and earlier uses only \r, \r, \r\n).
 *  <p>
 *  See {@link In} for a version that handles input from files, URLs,
 *  and sockets.
 *  <p>
 *  Note that Java's UTF-8 encoding does not recognize the optional byte-order
 *  mask. If the input begins with the optional byte-order mask, <tt>StdIn</tt>
 *  will have an extra character <tt>uFEFF</tt> at the beginning.
 *  For details, see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4508058.
 *
 *  @author David Pritchard
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public final class StdIn {

    /*** begin: section (1 of 2) of code duplicated from In to StdIn. */
    
    // assume Unicode UTF-8 encoding
    private static final String CHARSET_NAME = "UTF-8";

    // assume language = English, country = US for consistency with System.out.
    private static final Locale LOCALE = Locale.US;

    // the default token separator; we maintain the invariant that this value
    // is held by the scanner's delimiter between calls
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{javaWhitespace}+");

    // makes whitespace characters significant
    private static final Pattern EMPTY_PATTERN = Pattern.compile("");

    // used to read the entire input
    private static final Pattern EVERYTHING_PATTERN = Pattern.compile("\\A");

    /*** end: section (1 of 2) of code duplicated from In to StdIn. */

    private static Scanner scanner;
 
    // it doesn't make sense to instantiate this class
    private StdIn() { }

    //// begin: section (2 of 2) of code duplicated from In to StdIn,
    //// with all methods changed from "public" to "public static"

   /**
     * Returns true if standard input is empty (except possibly for whitespace).
     * Use this method to know whether the next call to {@link #readString()}, 
     * {@link #readDouble()}, etc will succeed.
     * @return <tt>true</tt> if standard input is empty (except possibly
     *     for whitespae); <tt>false</tt> otherwise
     */
    public static boolean isEmpty() {
        return !scanner.hasNext();
    }

   /**
     * Returns true if standard input has a next line.
     * Use this method to know whether the
     * next call to {@link #readLine()} will succeed. <p> Functionally
     * equivalent to {@link #hasNextChar()}.
     * @return true if standard input is empty, and false otherwise
     */
    public static boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /**
     * Returns true if standard input is empty (including whitespace).
     * Use this to know whether the next call to {@link #readChar()} will succeed.
     * <p>Functionally equivalent to {@link #hasNextLine()}.
     * @return true if standard input is empty, and false otherwise
     */
    public static boolean hasNextChar() {
        scanner.useDelimiter(EMPTY_PATTERN);
        boolean result = scanner.hasNext();
        scanner.useDelimiter(WHITESPACE_PATTERN);
        return result;
    }


   /**
     * Reads and returns the next line, excluding the line separator if present.
     * @return the next line, excluding the line separator if present
     */
    public static String readLine() {
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
     * Reads and returns the next character.
     * @return the next character
     */
    public static char readChar() {
        scanner.useDelimiter(EMPTY_PATTERN);
        String ch = scanner.next();
        assert ch.length() == 1 : "Internal (Std)In.readChar() error!"
            + " Please contact the authors.";
        scanner.useDelimiter(WHITESPACE_PATTERN);
        return ch.charAt(0);
    }  


   /**
     * Reads and returns the remainder of the input, as a string.
     * @return the remainder of the input, as a string
     */
    public static String readAll() {
        if (!scanner.hasNextLine())
            return "";

        String result = scanner.useDelimiter(EVERYTHING_PATTERN).next();
        // not that important to reset delimeter, since now scanner is empty
        scanner.useDelimiter(WHITESPACE_PATTERN); // but let's do it anyway
        return result;
    }


   /**
     * Reads the next token  and returns the <tt>String</tt>.
     * @return the next <tt>String</tt>
     */
    public static String readString() {
        return scanner.next();
    }

   /**
     * Reads the next token from standard input, parses it as an integer, and returns the integer.
     * @return the next integer on standard input
     * @throws InputMismatchException if the next token cannot be parsed as an <tt>int</tt>
     */
    public static int readInt() {
        return scanner.nextInt();
    }

   /**
     * Reads the next token from standard input, parses it as a double, and returns the double.
     * @return the next double on standard input
     * @throws InputMismatchException if the next token cannot be parsed as a <tt>double</tt>
     */
    public static double readDouble() {
        return scanner.nextDouble();
    }

   /**
     * Reads the next token from standard input, parses it as a float, and returns the float.
     * @return the next float on standard input
     * @throws InputMismatchException if the next token cannot be parsed as a <tt>float</tt>
     */
    public static float readFloat() {
        return scanner.nextFloat();
    }

   /**
     * Reads the next token from standard input, parses it as a long integer, and returns the long integer.
     * @return the next long integer on standard input
     * @throws InputMismatchException if the next token cannot be parsed as a <tt>long</tt>
     */
    public static long readLong() {
        return scanner.nextLong();
    }

   /**
     * Reads the next token from standard input, parses it as a short integer, and returns the short integer.
     * @return the next short integer on standard input
     * @throws InputMismatchException if the next token cannot be parsed as a <tt>short</tt>
     */
    public static short readShort() {
        return scanner.nextShort();
    }

   /**
     * Reads the next token from standard input, parses it as a byte, and returns the byte.
     * @return the next byte on standard input
     * @throws InputMismatchException if the next token cannot be parsed as a <tt>byte</tt>
     */
    public static byte readByte() {
        return scanner.nextByte();
    }

    /**
     * Reads the next token from standard input, parses it as a boolean,
     * and returns the boolean.
     * @return the next boolean on standard input
     * @throws InputMismatchException if the next token cannot be parsed as a <tt>boolean</tt>:
     *    <tt>true</tt> or <tt>1</tt> for true, and <tt>false</tt> or <tt>0</tt> for false,
     *    ignoring case
     */
    public static boolean readBoolean() {
        String s = readString();
        if (s.equalsIgnoreCase("true"))  return true;
        if (s.equalsIgnoreCase("false")) return false;
        if (s.equals("1"))               return true;
        if (s.equals("0"))               return false;
        throw new InputMismatchException();
    }

    /**
     * Reads all remaining tokens from standard input and returns them as an array of strings.
     * @return all remaining tokens on standard input, as an array of strings
     */
    public static String[] readAllStrings() {
        // we could use readAll.trim().split(), but that's not consistent
        // because trim() uses characters 0x00..0x20 as whitespace
        String[] tokens = WHITESPACE_PATTERN.split(readAll());
        if (tokens.length == 0 || tokens[0].length() > 0)
            return tokens;

        // don't include first token if it is leading whitespace
        String[] decapitokens = new String[tokens.length-1];
        for (int i = 0; i < tokens.length - 1; i++)
            decapitokens[i] = tokens[i+1];
        return decapitokens;
    }

    /**
     * Reads all remaining lines from standard input and returns them as an array of strings.
     * @return all remaining lines on standard input, as an array of strings
     */
    public static String[] readAllLines() {
        ArrayList<String> lines = new ArrayList<String>();
        while (hasNextLine()) {
            lines.add(readLine());
        }
        return lines.toArray(new String[0]);
    }

    /**
     * Reads all remaining tokens from standard input, parses them as integers, and returns
     * them as an array of integers.
     * @return all remaining integers on standard input, as an array
     * @throws InputMismatchException if any token cannot be parsed as an <tt>int</tt>
     */
    public static int[] readAllInts() {
        String[] fields = readAllStrings();
        int[] vals = new int[fields.length];
        for (int i = 0; i < fields.length; i++)
            vals[i] = Integer.parseInt(fields[i]);
        return vals;
    }

    /**
     * Reads all remaining tokens from standard input, parses them as doubles, and returns
     * them as an array of doubles.
     * @return all remaining doubles on standard input, as an array
     * @throws InputMismatchException if any token cannot be parsed as a <tt>double</tt>
     */
    public static double[] readAllDoubles() {
        String[] fields = readAllStrings();
        double[] vals = new double[fields.length];
        for (int i = 0; i < fields.length; i++)
            vals[i] = Double.parseDouble(fields[i]);
        return vals;
    }
    
    //// end: section (2 of 2) of code duplicated from In to StdIn
    
    
    // do this once when StdIn is initialized
    static {
        resync();
    }

    /**
     * If StdIn changes, use this to reinitialize the scanner.
     */
    private static void resync() {
        setScanner(new Scanner(new java.io.BufferedInputStream(System.in), CHARSET_NAME));
    }
    
    private static void setScanner(Scanner scanner) {
        StdIn.scanner = scanner;
        StdIn.scanner.useLocale(LOCALE);
    }

   /**
     * Reads all remaining tokens, parses them as integers, and returns
     * them as an array of integers.
     * @return all remaining integers, as an array
     * @throws InputMismatchException if any token cannot be parsed as an <tt>int</tt>
     * @deprecated Replaced by {@link #readAllInts()}.
     */
    public static int[] readInts() {
        return readAllInts();
    }

   /**
     * Reads all remaining tokens, parses them as doubles, and returns
     * them as an array of doubles.
     * @return all remaining doubles, as an array
     * @throws InputMismatchException if any token cannot be parsed as a <tt>double</tt>
     * @deprecated Replaced by {@link #readAllDoubles()}.
     */
    public static double[] readDoubles() {
        return readAllDoubles();
    }

   /**
     * Reads all remaining tokens and returns them as an array of strings.
     * @return all remaining tokens, as an array of strings
     * @deprecated Replaced by {@link #readAllStrings()}.
     */
    public static String[] readStrings() {
        return readAllStrings();
    }


    /**
     * Interactive test of basic functionality.
     */
    public static void main(String[] args) {

        System.out.println("Type a string: ");
        String s = StdIn.readString();
        System.out.println("Your string was: " + s);
        System.out.println();

        System.out.println("Type an int: ");
        int a = StdIn.readInt();
        System.out.println("Your int was: " + a);
        System.out.println();

        System.out.println("Type a boolean: ");
        boolean b = StdIn.readBoolean();
        System.out.println("Your boolean was: " + b);
        System.out.println();

        System.out.println("Type a double: ");
        double c = StdIn.readDouble();
        System.out.println("Your double was: " + c);
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
