/******************************************************************************
 *  Compilation:  javac DrawListener.java
 *  Execution:    none
 *  Dependencies: none
 *
 *  Interface that accompanies Draw.java.
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  <i>DrawListener</i>. This interface provides a basic capability for
 *  responding to keyboard in mouse events from {@link Draw} via callbacks.
 *  You can see some examples in
 *  <a href="https://introcs.cs.princeton.edu/java/36inheritance">Section 3.6</a>.
 *
 *  <p>
 *  For additional documentation, see
 *  <a href="https://introcs.cs.princeton.edu/31datatype">Section 3.1</a> of
 *  <i>Computer Science: An Interdisciplinary Approach</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public interface DrawListener {

    /**
     * Invoked when the mouse has been pressed.
     *
     * @param x the x-coordinate of the mouse
     * @param y the y-coordinate of the mouse
     */
    void mousePressed(double x, double y);

    /**
     * Invoked when the mouse has been dragged.
     *
     * @param x the x-coordinate of the mouse
     * @param y the y-coordinate of the mouse
     */
    void mouseDragged(double x, double y);

    /**
     * Invoked when the mouse has been released.
     *
     * @param x the x-coordinate of the mouse
     * @param y the y-coordinate of the mouse
     */
    void mouseReleased(double x, double y);

    /**
     * Invoked when the mouse has been clicked (pressed and released).
     *
     * @param x the x-coordinate of the mouse
     * @param y the y-coordinate of the mouse
     */
    void mouseClicked(double x, double y);

    /**
     * Invoked when a key has been typed.
     *
     * @param c the character typed
     */
    void keyTyped(char c);

    /**
     * Invoked when a key has been pressed.
     *
     * @param keycode the key combination pressed
     */
    void keyPressed(int keycode);

    /**
     * Invoked when a key has been released.
     *
     * @param keycode the key combination released
     */
    void keyReleased(int keycode);
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
