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
 *  <i>Computer Science: An Interdisciplinary Approach</i>
 *  by Robert Sedgewick and Kevin Wayne.
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
    default void mousePressed(double x, double y) {
        // does nothing by default
    }

    /**
     * Invoked when the mouse has been dragged.
     *
     * @param x the x-coordinate of the mouse
     * @param y the y-coordinate of the mouse
     */
    default void mouseDragged(double x, double y) {
        // does nothing by default
    }

    /**
     * Invoked when the mouse has been released.
     *
     * @param x the x-coordinate of the mouse
     * @param y the y-coordinate of the mouse
     */
    default void mouseReleased(double x, double y) {
        // does nothing by default
    }

    /**
     * Invoked when the mouse has been clicked (pressed and released).
     * A mouse click is triggered only if the user presses a mouse button
     * and then releases it quickly, without moving the mouse.
     * It does not work with touch events.
     * The {@link mousePressed} method is generally preferred for
     * detecting mouse clicks.
     *
     * @param x the x-coordinate of the mouse
     * @param y the y-coordinate of the mouse
     */
    default void mouseClicked(double x, double y) {
        // does nothing by default
    }

    /**
     * Invoked when a key has been typed.
     *
     * @param c the character typed
     */
    default void keyTyped(char c) {
        // does nothing by default
    }

    /**
     * Invoked when a key has been pressed.
     *
     * @param keycode the key combination pressed
     */
    default void keyPressed(int keycode) {
        // does nothing by default
    }

    /**
     * Invoked when a key has been released.
     *
     * @param keycode the key combination released
     */
    default void keyReleased(int keycode) {
        // does nothing by default
    }

    /**
     * Gets called at regular time intervals.
     */
    default void update() {
        // does nothing by default
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
