/******************************************************************************
 *  Compilation:  javac StdPicture.java
 *  Execution:    java StdPicture filename.jpg
 *  Dependencies: Picture.java
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;


/**
 *  The {@code StdPicture} class provides static methods for manipulating
 *  the individual pixels of an image using the RGB color model.
 *  You can either initialize a blank image (of a given dimension) or read an
 *  image in a supported file format (typically JPEG, PNG, GIF, TIFF, and BMP).
 *  This class also includes methods for displaying the image in a window
 *  and saving it to a file.
 *
 *  <p>
 *  <b>Use in the curriculum.</b>
 *  The {@code StdPicture} class is intended for early usage in the
 *  curriculum, before objects.
 *  The {@link Picture} class is an object-oriented version that supports
 *  manipulating multiple pictures at the same time.
 *
 *  <p>
 *  <b>Getting started.</b>
 *  To use this class, you must have {@code StdPicture} in your Java classpath.
 *  Here are three possible ways to do this:
 *  <ul>
 *  <li> If you ran our autoinstaller, use the commands
 *  {@code javac-introcs} and {@code java-introcs} (or {@code javac-algs4}
 *  and {@code java-algs4}) when compiling and executing. These commands
 *  add {@code stdlib.jar} (or {@code algs4.jar}) to the Java classpath, which
 *  provides access to {@code StdPicture}.
 *
 *  <li> Download <a href = "https://introcs.cs.princeton.edu/java/code/stdlib.jar">stdlib.jar</a>
 *  (or <a href = "https://algs4.cs.princeton.edu/code/algs4.jar">algs4.jar</a>)
 *  and add it to the Java classpath.
 *
 *  <li> Download <a href = "https://introcs.cs.princeton.edu/java/stdlib/StdPicture.java">StdPicture.java</a>
 *  and
 *  <a href = "https://introcs.cs.princeton.edu/java/stdlib/Picture.java">Picture.java</a>
 *  and put them in the working directory.
 *  </ul>
 *
 *  <p>
 *  As a test, cut-and-paste the following short program into your editor:
 *  <pre>
 *   public class TestStdPicture {
 *       public static void main(String[] args) {
 *           StdPicture.read("https://introcs.cs.princeton.edu/java/stdlib/mandrill.jpg");
 *           StdPicture.show();
 *       }
 *   }
 *  </pre>
 *  <p>
 *  If you compile and execute the program, you should see a picture of a mandrill
 *  (a colorful monkey native to west-central Africa) in a window.
 *
 *  <p>
 *  <b>Anatomy of an image.</b>
 *  An image is a <em>width</em>-by-<em>height</em> grid of pixels, with pixel (0, 0)
 *  in the upper-left corner.
 *  Each pixel has a color that is represented using the <em>RGB color model</em>,
 *  which specifies the levels of <em>red</em> (R), <em>green</em> (G), and <em>blue</em> (B)
 *  on an integer scale from 0 to 255.
 *
 *  <blockquote>
 *  <img src = "https://introcs.cs.princeton.edu/java/stdlib/AnatomyImage.png" width = 200 alt = "anatomy of an image">
 *  </blockquote>
 *
 *  <p>
 *  <b>Initializing the picture.</b>
 *  You can use the following methods to initialize the picture:
 *  <ul>
 *  <li> {@link #read(String filename)}
 *  <li> {@link #init(int width, int height)}
 *  </ul>
 *  <p>
 *  The first method reads an image in a supported file format
 *  (typically JPEG, PNG, GIF, TIFF, and BMP)
 *  and initializes the picture to that image.
 *  The second method initializes a <em>width</em>-by-<em>height</em>
 *  picture, with each pixel black.
 *
 *  <p>
 *  <b>Getting and setting the colors of the individual pixels.</b>
 *  You can use the following methods to retrieve the RGB components
 *  of a specified pixel:
 *  <ul>
 *  <li> {@link #getRed(int col, int row)}
 *  <li> {@link #getGreen(int col, int row)}
 *  <li> {@link #getBlue(int col, int row)}
 *  <li> {@link #setRGB(int col, int row, int r, int g, int b)}
 *  </ul>
 *  <p>
 *  The first three methods return the red, green, and blue components of pixel
 *  (<em>col</em>, <em>row</em>). Each component is an integer between 0 and 255,
 *  with 0 corresponding to the absence of that component and 255 corresponding
 *  to full intensity of that component.
 *  The last method sets the red, green, and blue components of pixel
 *  (<em>col</em>, <em>row</em>) to the specified values.
 *
 *  <p><b>Iterating over the pixels.</b>
 *  A common operation in image processing is to iterate over and process
 *  all of the pixels in an image.
 *  Here is a prototypical example that converts a color image
 *  to grayscale, using the NTSC formula
 *  <em>Y</em> = 0.299<em>r</em> + 0.587<em>g</em> + 0.114<em>b</em>.
 *  Note that if the red, green, and blue components are all equal,
 *  then the color is a shade of gray.
 *  <pre>
 *  StdPicture.read("https://introcs.cs.princeton.edu/java/stdlib/mandrill.jpg");
 *  for (int col = 0; col &lt; StdPicture.width(); col++) {
 *      for (int row = 0; row &lt; StdPicture.height(); row++) {
 *          int r = StdPicture.getRed(col, row);
 *          int g = StdPicture.getGreen(col, row);
 *          int b = StdPicture.getBlue(col, row);
 *          int y = (int) (Math.round(0.299*r + 0.587*g + 0.114*b));
 *          StdPicture.setRGB(col, row, y, y, y);
 *      }
 *  }
 *  StdPicture.show();
 *  </pre>
 *
 *  <p><b>Transparency.</b>
 *  The {@code StdPicture} class supports transparent images, using the
 *  ARGB color model. The following methods are useful for this:
 *  <ul>
 *  <li> {@link #getAlpha(int col, int row)}
 *  <li> {@link #setARGB(int col, int row, int a, int r, int g, int b)}
 *  </ul>
 *  <p>
 *  The first method gets the alpha component of pixel (<em>col</em>, <em>row</em>).
 *  The second methods sets the alpha, red, green, and bluecomponents of
 *  pixel (<em>col</em>, <em>row</em>).
 *  The alpha value defines the transparency of a color, with 0 corresponding to
 *  completely transparent and 255 to completely opaque. If transparency is not
 *  explicitly used, the alpha value is 255.
 *
 *  <p><b>Saving files.</b>
 *  The {@code StdPicture} class supports writing images to a supported
 *  file format (typically JPEG, PNG, GIF, TIFF, and BMP).
 *  You can save the picture to a file two method:
 *  <ul>
 *  <li> {@link #save(String filename)}
 *  </ul>
 *
 *  <p>Alternatively, you can save the picture interactively
 *  by using the menu option <em>File → Save</em> from the picture window.
 *
 *  <p><b>File formats.</b>
 *  The {@code StdPicture} class supports reading and writing images to any of the
 *  file formats supported by {@link javax.imageio} (typically JPEG, PNG,
 *  GIF, TIFF, and BMP).
 *  The file extensions corresponding to JPEG, PNG, GIF, TIFF, and BMP,
 *  are {@code .jpg}, {@code .png}, {@code .gif}, {@code .tif},
 *  and {@code .bmp}, respectively.
 *  The file formats JPEG and BMP do not support transparency.
 *
 *  <p><b>Memory usage.</b>
 *  A <em>W</em>-by-<em>H</em> picture uses ~ 4 <em>W H</em> bytes of memory,
 *  since the color of each pixel is encoded as a 32-bit <code>int</code>.
 *
 *  <p><b>Additional documentation.</b>
 *  For additional documentation, see
 *  <a href="https://introcs.cs.princeton.edu/31datatype">Section 3.1</a> of
 *  <i>Computer Science: An Interdisciplinary Approach</i>
 *  by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public final class StdPicture {

    // the default picture width and height
    private static final int DEFAULT_SIZE = 512;

    // the underlying picture
    private static Picture picture = new Picture(DEFAULT_SIZE, DEFAULT_SIZE);

    // singleton pattern: client can't instantiate
    private StdPicture() { }

   /**
     * Initializes a {@code width}-by-{@code height} picture, with {@code width} columns
     * and {@code height} rows, where each pixel is black.
     *
     * @param width the width of the picture
     * @param height the height of the picture
     * @throws IllegalArgumentException if {@code width} is negative or zero
     * @throws IllegalArgumentException if {@code height} is negative or zero
     */
    public static void init(int width, int height) {
        if (picture.isVisible()) {
            hide();
            picture = new Picture(width, height);
            show();
        }
        else {
            picture = new Picture(width, height);
        }
    }

   /**
     * Initializes the picture by reading a JPEG, PNG, GIF, BMP, or TIFF image
     * from a file or URL.
     * The filetype extension must be {@code .jpg}, {@code .png}, {@code .gif},
     * {@code .bmp}, or {@code .tif}.
     *
     * @param  filename the name of the file or URL
     * @throws IllegalArgumentException if cannot read image
     * @throws IllegalArgumentException if {@code name} is {@code null}
     */
    public static void read(String filename) {
        Picture newPicture = new Picture(filename);

        // same dimension, so copy pixels instead of using new Picture and GUI
        if (newPicture.width() == picture.width() && newPicture.height() == newPicture.height()) {
            for (int col = 0; col < picture.width(); col++) {
                for (int row = 0; row < picture.height(); row++) {
                    picture.setARGB(col, row, newPicture.getARGB(col, row));
                }
            }
        }

        // different dimension, so need to use new Picture and GUI
        else if (picture.isVisible()) {
            hide();
            picture = newPicture;
            show();
        }

        // no GUI, so use the new Picture
        else {
            picture = newPicture;
        }
    }

   /**
     * Is the window containing the picture visible?
     * @return {@code true} if the picture is visible, and {@code false} otherwise
     */
    public static boolean isVisible() {
        return picture.isVisible();
    }

   /**
     * Displays the picture in a window on the screen.
     */
    public static void show() {
        picture.show();
    }

   /**
     * Hides the window containing the picture.
     */
    public static void hide() {
        picture.hide();
    }

    /**
     * Pauses for t milliseconds. This method is intended to support computer animation.
     * @param t number of milliseconds
     * @throws IllegalArgumentException if {@code t} is negative
     */
    public static void pause(int t) {
        if (t < 0) throw new IllegalArgumentException("argument must be non-negative");
        try {
            Thread.sleep(t);
        }
        catch (InterruptedException e) {
            System.out.println("Error sleeping");
        }
    }

   /**
     * Returns the height of the picture.
     *
     * @return the height of the picture (in pixels)
     */
    public static int height() {
        return picture.height();
    }

   /**
     * Returns the width of the picture.
     *
     * @return the width of the picture (in pixels)
     */
    public static int width() {
        return picture.width();
    }

   /**
     * Returns the alpha component of the color of pixel ({@code col}, {@code row}).
     *
     * @param col the column index
     * @param row the row index
     * @return the alpha component of the color of pixel ({@code col}, {@code row})
     * @throws IndexOutOfBoundsException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     */
    public static int getAlpha(int col, int row) {
        int rgb = picture.getARGB(col, row);
        return (rgb >> 24) & 0xFF;
    }

   /**
     * Returns the red component of the color of pixel ({@code col}, {@code row}).
     *
     * @param col the column index
     * @param row the row index
     * @return the red component of the color of pixel ({@code col}, {@code row})
     * @throws IndexOutOfBoundsException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     */
    public static int getRed(int col, int row) {
        int rgb = picture.getARGB(col, row);
        return (rgb >> 16) & 0xFF;
    }

   /**
     * Returns the green component of the color of pixel ({@code col}, {@code row}).
     *
     * @param col the column index
     * @param row the row index
     * @return the green component of the color of pixel ({@code col}, {@code row})
     * @throws IndexOutOfBoundsException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     */
    public static int getGreen(int col, int row) {
        int rgb = picture.getARGB(col, row);
        return (rgb >> 8) & 0xFF;
    }

   /**
     * Returns the blue component of the color of pixel ({@code col}, {@code row}).
     *
     * @param col the column index
     * @param row the row index
     * @return the blue component of the color of pixel ({@code col}, {@code row})
     * @throws IndexOutOfBoundsException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     */
    public static int getBlue(int col, int row) {
        int rgb = picture.getARGB(col, row);
        return (rgb >> 0) & 0xFF;
    }

   /**
     * Returns the ARGB color of pixel ({@code col}, {@code row}).
     *
     * @param col the column index
     * @param row the row index
     * @return the ARGB color of pixel ({@code col}, {@code row} as a 32-bit integer
     * @throws IndexOutOfBoundsException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     */
    public static int getARGB(int col, int row) {
        int argb = picture.getARGB(col, row);
        return argb;
    }

   /**
     * Sets the color of pixel ({@code col}, {@code row}) to the given RGB color using
     * red, green, and blue components. The alpha component is set to 255 (no transparency).
     *
     * @param col the column index
     * @param row the row index
     * @param r the red component of the color
     * @param g the green component of the color
     * @param b the blue component of the color
     * @throws IndexOutOfBoundsException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     * @throws IllegalArgumentException unless {@code 0 <= r < 256}, {@code 0 <= g < 256},
     *                                  and {@code 0 <= b < 256}.
     */
    public static void setRGB(int col, int row, int r, int g, int b) {
        validateComponent(r, "red");
        validateComponent(g, "green");
        validateComponent(b, "blue");
        int a = 255;
        int argb = (a << 24) | (r << 16) | (g << 8) | (b << 0);
        picture.setARGB(col, row, argb);
    }

   /**
     * Sets the color of pixel ({@code col}, {@code row}) to the given ARGB color using
     * alpha, red, green, and blue components.
     *
     * @param col the column index
     * @param row the row index
     * @param a the alpha component of the color
     * @param r the red component of the color
     * @param g the green component of the color
     * @param b the blue component of the color
     * @throws IndexOutOfBoundsException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     * @throws IllegalArgumentException unless {@code 0 <= a < 256}, {@code 0 <= r < 256},
     *                                         {@code 0 <= g < 256}, and {@code 0 <= b < 256}.
     */
    public static void setARGB(int col, int row, int a, int r, int g, int b) {
        validateComponent(a, "alpha");
        validateComponent(r, "red");
        validateComponent(g, "green");
        validateComponent(b, "blue");

        // internally represented using ARGB, not RGBA
        int argb = (a << 24) | (r << 16) | (g << 8) | (b << 0);
        picture.setARGB(col, row, argb);
    }

   /**
     * Sets the color of pixel ({@code col}, {@code row}) to the given RGB color.
     *
     * @param col the column index
     * @param row the row index
     * @param rgb the RGB color, represented as a 24-bit integer
     * @throws IndexOutOfBoundsException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     * @throws IllegalArgumentException unless {@code 0 <= rgb < 2^24}.
     */
/*
    public static void setRGB(int col, int row, int rgb) {
        int a = 255;
        int argb = (a << 24) | (rgb);
        picture.setARGB(col, row, argb);
    }
*/

   /**
     * Sets the color of pixel ({@code col}, {@code row}) to the given ARGB color.
     *
     * @param col the column index
     * @param row the row index
     * @param argb the ARGB color, represented as a 32-bit integer
     * @throws IndexOutOfBoundsException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     */
    public static void setARGB(int col, int row, int argb) {
        picture.setARGB(col, row, argb);
    }


    /**
     * Sets the title of this picture.
     * @param title the title
     * @throws IllegalArgumentException if {@code title} is {@code null}
     */
    public static void setTitle(String title) {
        picture.setTitle(title);
    }

   /**
     * Saves the picture to a file in a supported file format
     * (typically JPEG, PNG, GIF, TIFF, and BMP).
     * The filetype extension must be {@code .jpg}, {@code .png}, {@code .gif},
     * {@code .bmp}, or {@code .tif}.
     * If the file format does not support transparency (such as JPEG
     * or BMP), it will be converted to be opaque (with purely
     * transparent pixels converted to black).
     *
     * @param filename the name of the file
     * @throws IllegalArgumentException if {@code filename} is {@code null}
     * @throws IllegalArgumentException if {@code filename} is the empty string
     */
    public static void save(String filename) {
        picture.save(filename);
    }

    private static void validateComponent(int val, String name) {
        if (val < 0 || val >= 256) {
            throw new IllegalArgumentException(name + " must be between 0 and 255");
        }
    }

   /**
     * Unit tests this {@code StdPicture} data type.
     * Reads a picture specified by the command-line argument,
     * and shows it in a window on the screen.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        StdPicture.read(args[0]);
        System.out.printf("%d-by-%d\n", picture.width(), picture.height());
        StdPicture.show();
    }

}

/******************************************************************************
 *  Copyright 2002-2025, Robert Sedgewick and Kevin Wayne.
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
