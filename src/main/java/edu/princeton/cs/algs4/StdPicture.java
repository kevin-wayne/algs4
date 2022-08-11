/******************************************************************************
 *  Compilation:  javac StdPicture.java
 *  Execution:    java StdPicture filename.jpg
 *  Dependencies: Picture.java
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;


/**
 *  <p><b>Overview.</b>
 *  The {@code StdPicture} class provides a basic capability for manipulating
 *  the individual pixels of an image using the RGB color model.
 *  You can either create a blank image (of a given dimension) or read an
 *  image in a supported file format
 *  (typically JPEG, PNG, GIF TIFF, and BMP).
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
 *           StdPicture.create("https://introcs.cs.princeton.edu/java/stdlib/mandrill.jpg");
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
 *  The last methods set the red, green, and blue components of pixel
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
 *  StdPicture.create("https://introcs.cs.princeton.edu/java/stdlib/mandrill.jpg");
 *  for (int col = 0; col &lt; StdPicture.width(); col++) {
 *      for (int row = 0; row &lt; StdPicture.height(); row++) {
 *          int r = StdPicture.getRed(col, row);
 *          int g = StdPicture.getGreen(col, row);
 *          int b = StdPicture.getBlue(col, row);
 *          int y = (int) (Math.round(0.299*r + 0.587*g + 0.114*b));
 *          StdPicture.set(col, row, y, y, y);
 *      }
 *  }
 *  StdPicture.show();
 *  </pre>
 *
 *  <p><b>Transparency.</b>
 *  The {@code StdPicture} class supports transparent images, using the
 *  ARGB color model. The folowing methods are useful for this:
 *  <ul>
 *  <li> {@link #getAlpha(int col, int row)}
 *  <li> {@link #setARGB(int col, int row, int r, int g, int b, int a)}
 *  </ul>
 *  <p>
 *  The first method gets the alpha component of pixel (<em>col</em>, <em>row</em>).
 *  The second methods sets the red, green, blue, and alpha components of
 *  pixel (<em>col</em>, <em>row</em>).
 *  The alpha value defines the transparency of a color, with 0 corresponding to
 *  completely transparent and 255 to completely opaque. If transparency is not
 *  explicitly used, all alpha values are 255.
 *
 *  <p><b>File formats.</b>
 *  The {@code Picture} class supports reading and writing images in
 *  a supported format (typically JPEG, PNG, GIF TIFF, and BMP).
 *  Note that some file format (such as JPEG and BMP) do not support transparency.
 *  You can save the picture to a file using this method:
 *  <ul>
 *  <li> {@link #save(String filename)}
 *  </ul>
 *
 *  <p>Alternatively, you can save the picture interactively
 *  by using the menu option <em>File → Save</em> from the picture window.
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
     * Creates a {@code width}-by-{@code height} picture, with {@code width} columns
     * and {@code height} rows, where each pixel is black.
     *
     * @param width the width of the picture
     * @param height the height of the picture
     * @throws IllegalArgumentException if {@code width} is negative or zero
     * @throws IllegalArgumentException if {@code height} is negative or zero
     */
    public static void create(int width, int height) {
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
     * Creates a picture by reading an image from a file or URL.
     *
     * @param  filename the name of the file (.png, .gif, or .jpg) or URL.
     * @throws IllegalArgumentException if cannot read image
     * @throws IllegalArgumentException if {@code name} is {@code null}
     */
    public static void create(String filename) {
        if (picture.isVisible()) {
            hide();
            picture = new Picture(filename);
            show();
        }
        else {
            picture = new Picture(filename);
        }
    }

   /**
     * Displays the picture in a window on the screen.
     */
    public static void show() {
        picture.show();
    }

   /**
     * Hides the window on the screen containing the picture.
     */
    public static void hide() {
        picture.hide();
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
     * @throws IllegalArgumentException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     */
    public static int getAlpha(int col, int row) {
        int rgb = picture.getRGB(col, row);
        return (rgb >> 24) & 0xFF;
    }

   /**
     * Returns the red component of the color of pixel ({@code col}, {@code row}).
     *
     * @param col the column index
     * @param row the row index
     * @return the red component of the color of pixel ({@code col}, {@code row})
     * @throws IllegalArgumentException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     */
    public static int getRed(int col, int row) {
        int rgb = picture.getRGB(col, row);
        return (rgb >> 16) & 0xFF;
    }

   /**
     * Returns the green component of the color of pixel ({@code col}, {@code row}).
     *
     * @param col the column index
     * @param row the row index
     * @return the green component of the color of pixel ({@code col}, {@code row})
     * @throws IllegalArgumentException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     */
    public static int getGreen(int col, int row) {
        int rgb = picture.getRGB(col, row);
        return (rgb >> 8) & 0xFF;
    }

   /**
     * Returns the blue component of the color of pixel ({@code col}, {@code row}).
     *
     * @param col the column index
     * @param row the row index
     * @return the blue component of the color of pixel ({@code col}, {@code row})
     * @throws IllegalArgumentException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     */
    public static int getBlue(int col, int row) {
        int rgb = picture.getRGB(col, row);
        return (rgb >> 0) & 0xFF;
    }

   /**
     * Sets the color of pixel ({@code col}, {@code row}) to given color.
     *
     * @param col the column index
     * @param row the row index
     * @param r the red component of the color
     * @param g the green component of the color
     * @param b the blue component of the color
     * @throws IllegalArgumentException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     * @throws IllegalArgumentException unless {@code 0 <= r < 256}, {@code 0 <= g < 256},
     *                                  and {@code 0 <= b < 256}.
     */
    public static void setRGB(int col, int row, int r, int g, int b) {
        int a = 255;
        int rgb = (a << 24) | (r << 16) | (g << 8) | (b << 0);
        picture.setRGB(col, row, rgb);
    }

   /**
     * Sets the color of pixel ({@code col}, {@code row}) to given color.
     *
     * @param col the column index
     * @param row the row index
     * @param a the alpha component of the color
     * @param r the red component of the color
     * @param g the green component of the color
     * @param b the blue component of the color
     * @throws IllegalArgumentException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     * @throws IllegalArgumentException unless {@code 0 <= a < 256}, {@code 0 <= r < 256},
     *                                         {@code 0 <= g < 256}, and {@code 0 <= b < 256}.
     */
    public static void setARGB(int col, int row, int a, int r, int g, int b) {
        int rgb = (a << 24) | (r << 16) | (g << 8) | (b << 0);
        picture.setRGB(col, row, rgb);
    }

   /**
     * Saves the picture to a file in a supported format
     * (typically JPEG, PNG, GIF TIFF, and BMP).
     *
     * @param filename the name of the file
     * @throws IllegalArgumentException if {@code filename} is {@code null}
     * @throws IllegalArgumentException if {@code filename} is the empty string
     */
    public static void save(String filename) {
        picture.save(filename);
    }

   /**
     * Unit tests this {@code StdPicture} data type.
     * Reads a picture specified by the command-line argument,
     * and shows it in a window on the screen.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        StdPicture.create(args[0]);
        System.out.printf("%d-by-%d\n", picture.width(), picture.height());
        StdPicture.show();
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
