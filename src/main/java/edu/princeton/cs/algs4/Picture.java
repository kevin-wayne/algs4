/******************************************************************************
 *  Compilation:  javac Picture.java
 *  Execution:    java Picture filename.jpg
 *  Dependencies: none
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;


/**
 *  <p><b>Overview.</b>
 *  The {@code Picture} class provides a basic capability for manipulating
 *  the individual pixels of an image.
 *  You can either create a blank image (of a given dimension) or read an
 *  image in a supported file format (typically JPEG, PNG, GIF TIFF, and BMP).
 *  This class also includes methods for displaying the image in a window
 *  and saving it to a file.
 *
 *  <p>
 *  <b>Use in the curriculum.</b>
 *  The {@code Picture} class is intended for use in the
 *  curriculum once objects are introduced.
 *  The {@link StdPicture} class is intended for earlier use in
 *  the curriculum, before objects (but it can support only one
 *  picture at a time).
 *  See {@link GrayscalePicture} for a version that supports
 *  grayscale images.
 *
 *  <p>
 *  <b>Getting started.</b>
 *  To use this class, you must have {@code Picture} in your Java classpath.
 *  Here are three possible ways to do this:
 *  <ul>
 *  <li> If you ran our autoinstaller, use the commands
 *  {@code javac-introcs} and {@code java-introcs} (or {@code javac-algs4}
 *  and {@code java-algs4}) when compiling and executing. These commands
 *  add {@code stdlib.jar} (or {@code algs4.jar}) to the Java classpath, which
 *  provides access to {@code Picture}.
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
 *   public class TestPicture {
 *       public static void main(String[] args) {
 *           Picture picture = new Picture("https://introcs.cs.princeton.edu/java/stdlib/mandrill.jpg");
 *           picture.show();
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
 *  <b>Creating pictures.</b>
 *  You can use the following constructors to create new {@code Picture} objects:
 *  <ul>
 *  <li> {@link #Picture(String filename)}
 *  <li> {@link #Picture(int width, int height)}
 *  </ul>
 *  <p>
 *  The first constructor read an image in a supported file format
 *  (typically JPEG, PNG, GIF TIFF, and BMP)
 *  and initializes the picture to that image.
 *  The second constructor creates a <em>width</em>-by-<em>height</em> picture,
 *  with each pixel black.
 *
 *  <p>
 *  <b>Getting and setting the colors of the individual pixels.</b>
 *  You can use the following methods to get and set the color of a
 *  specified pixel:
 *  <ul>
 *  <li> {@link #get(int col, int row)}
 *  <li> {@link #set(int col, int row, Color color)}
 *  </ul>
 *  <p>
 *  The first method returns the color of pixel (<em>col</em>, <em>row</em>)
 *  as a {@code Color} object.
 *  The second method set the color of pixel (<em>col</em>, <em>row</em>) to
 *  the specified color.
 *
 *  <p><b>Iterating over the pixels.</b>
 *  A common operation in image processing is to iterate over and process
 *  all of the pixels in an image.
 *  Here is a prototypical example that creates a grayscale version of a color image,
 *  using the NTSC formula
 *  <em>Y</em> = 0.299<em>r</em> + 0.587<em>g</em> + 0.114<em>b</em>.
 *  Note that if the red, green, and blue components of an RGB color
 *  are all equal, the color is a shade of gray.
 *  <pre>
 *  Picture picture  = new Picture("https://introcs.cs.princeton.edu/java/stdlib/mandrill.jpg");
 *  Picture grayscale = new Picture(picture.width(), picture.height());
 *  for (int col = 0; col &lt; picture.width(); col++) {
 *      for (int row = 0; row &lt; picture.height(); row++) {
 *          Color color = picture.get(col, row);
 *          int r = color.getRed();
 *          int g = color.getGreen();
 *          int b = color.getBlue();
 *          int y = (int) (Math.round(0.299*r + 0.587*g + 0.114*b));
 *          Color gray = new Color(y, y, y);
 *          grayscale.set(col, row, gray);
 *      }
 *  }
 *  picture.show();
 *  grayscale.show();
 *  </pre>
 *
 *  <p><b>Saving files.</b>
 *  The {@code Picture} class supports writing images to a supported
 *  file format (typically JPEG, PNG, GIF TIFF, and BMP).
 *  Note that some file formats (such as JPEG and BMP) do not support
 *  transparency.
 *  You can save the picture to a file using these two methods:
 *  <ul>
 *  <li> {@link #save(String filename)}
 *  <li> {@link #save(File file)}
 *  </ul>
 *
 *  <p>Alternatively, you can save the picture interactively
 *  by using the menu option <em>File → Save</em> from the picture window.
 *
 *  <p><b>Transparency.</b>
 *  Both the {@link Color} and {@code Picture} classes support
 *  transparency, using the <em>alpha channel</em>.
 *  The alpha value defines the transparency of a color, with 0 corresponding to
 *  completely transparent and 255 to completely opaque. If transparency is not
 *  explicitly used, all alpha values are 255.
 *
 *  <p><b>32-bit color.</b>
 *  Sometimes it is more convenient (or efficient) to manipulate the
 *  color of a pixel as a single 32-bit integers instead of four 8-bit components.
 *  The following methods support this:
 *  <ul>
 *  <li> {@link #getRGB(int col, int row)}
 *  <li> {@link #setRGB(int col, int row, int rgb)}
 *  </ul>
 *  <p>
 *  The red (R), green (G), and blue (B) components
 *  are encoded using the least significant 24 bits.
 *  Given a 32-bit {@code int} encoding the color, the following code extracts
 *  the ARGB components:
 * <blockquote><pre>
 *  int a = (rgb &gt;&gt; 24) &amp; 0xFF;
 *  int r = (rgb &gt;&gt; 16) &amp; 0xFF;
 *  int g = (rgb &gt;&gt;  8) &amp; 0xFF;
 *  int b = (rgb &gt;&gt;  0) &amp; 0xFF;
 *  </pre></blockquote>
 *  Given the ARGB components (8-bits each) of a color,
 *  the following statement packs it into a 32-bit {@code int}:
 *  <blockquote><pre>
 *  int argb = (a &lt;&lt; 24) | (r &lt;&lt; 16) | (g &lt;&lt; 8) | (b &lt;&lt; 0);
 *  </pre></blockquote>
 *
 *  <p><b>Coordinates.</b>
 *  Pixel (<em>col</em>, <em>row</em>) is column <em>col</em> and row <em>row</em>.
 *  By default, the origin (0, 0) is the pixel in the upper-left corner.
 *  These are common conventions in image processing and consistent with Java's
 *  {@link java.awt.image.BufferedImage} data type. The following
 *  two methods allow you to change this convention:
 *  <ul>
 *  <li> {@link #setOriginLowerLeft()}
 *  <li> {@link #setOriginUpperLeft()}
 *  </ul>
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
public final class Picture implements ActionListener {
    private BufferedImage image;               // the rasterized image
    private JFrame frame;                      // on-screen view
    private String title;                      // window title (typically the name of the file)
    private boolean isOriginUpperLeft = true;  // location of origin
    private boolean isVisible = false;         // is the frame visible?
    private final int width, height;           // width and height

   /**
     * Creates a {@code width}-by-{@code height} picture, with {@code width} columns
     * and {@code height} rows, where each pixel is black.
     *
     * @param width the width of the picture
     * @param height the height of the picture
     * @throws IllegalArgumentException if {@code width} is negative or zero
     * @throws IllegalArgumentException if {@code height} is negative or zero
     */
    public Picture(int width, int height) {
        if (width  <= 0) throw new IllegalArgumentException("width must be positive");
        if (height <= 0) throw new IllegalArgumentException("height must be positive");
        this.width  = width;
        this.height = height;
        this.title = width + "-by-" + height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

   /**
     * Creates a new picture that is a deep copy of the argument picture.
     *
     * @param  picture the picture to copy
     * @throws IllegalArgumentException if {@code picture} is {@code null}
     */
    public Picture(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("constructor argument is null");

        width  = picture.width();
        height = picture.height();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        title = picture.title;
        isOriginUpperLeft = picture.isOriginUpperLeft;
        for (int col = 0; col < width(); col++)
            for (int row = 0; row < height(); row++)
                image.setRGB(col, row, picture.image.getRGB(col, row));
    }

   /**
     * Creates a picture by reading a JPEG, PNG, or GIF image from a file or URL.
     * The filetype extension must be {@code .jpg}, {@code .png}, or {@code .gif}.
     *
     * @param  filename the name of the file or URL
     * @throws IllegalArgumentException if cannot read image
     * @throws IllegalArgumentException if {@code name} is {@code null}
     */
    public Picture(String filename) {
        if (filename == null) throw new IllegalArgumentException("constructor argument is null");
        if (filename.length() == 0) throw new IllegalArgumentException("constructor argument is the empty string");

        title = filename;
        try {
            // try to read from file in working directory
            File file = new File(filename);
            if (file.isFile()) {
                title = file.getName();
                image = ImageIO.read(file);
            }

            else {

                // resource relative to .class file
                URL url = getClass().getResource(filename);

                // resource relative to classloader root
                if (url == null) {
                    url = getClass().getClassLoader().getResource(filename);
                }

                // or URL from web or jar
                if (url == null) {
                    url = new URL(filename);
                }

                image = ImageIO.read(url);
            }

            if (image == null) {
                throw new IllegalArgumentException("could not read image: " + filename);
            }

            width  = image.getWidth(null);
            height = image.getHeight(null);

            // convert to ARGB if necessary
            if (image.getType() != BufferedImage.TYPE_INT_ARGB) {
                BufferedImage imageARGB = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                imageARGB.createGraphics().drawImage(image, 0, 0, null);
                image = imageARGB;
            }
        }
        catch (IOException ioe) {
            throw new IllegalArgumentException("could not open image: " + filename, ioe);
        }
    }

   /**
     * Creates a picture by reading the image from a JPEG, PNG, or GIF file.
     *
     * @param file the file
     * @throws IllegalArgumentException if cannot read image
     * @throws IllegalArgumentException if {@code file} is {@code null}
     */
    public Picture(File file) {
        if (file == null) throw new IllegalArgumentException("constructor argument is null");

        try {
            BufferedImage image = ImageIO.read(file);

            width  = image.getWidth(null);
            height = image.getHeight(null);
            title = file.getName();

            // convert to ARGB
            if (image.getType() != BufferedImage.TYPE_INT_RGB) {
                BufferedImage imageARGB = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                imageARGB.createGraphics().drawImage(image, 0, 0, null);
                image = imageARGB;
            }
        }
        catch (IOException ioe) {
            throw new IllegalArgumentException("could not open file: " + file, ioe);
        }
        if (image == null) {
            throw new IllegalArgumentException("could not read file: " + file);
        }

    }

   /**
     * Returns a {@link JLabel} containing this picture, for embedding in a {@link JPanel},
     * {@link JFrame} or other GUI widget.
     *
     * @return the {@code JLabel}
     */
    public JLabel getJLabel() {
        if (image == null) return null;         // no image available
        ImageIcon icon = new ImageIcon(image);
        return new JLabel(icon);
    }

   /**
     * Sets the origin to be the upper left pixel. This is the default.
     */
    public void setOriginUpperLeft() {
        isOriginUpperLeft = true;
    }

   /**
     * Sets the origin to be the lower left pixel.
     */
    public void setOriginLowerLeft() {
        isOriginUpperLeft = false;
    }

   /**
     * Displays the picture in a window on the screen.
     */

    // getMenuShortcutKeyMask() deprecated in Java 10 but its replacement
    // getMenuShortcutKeyMaskEx() is not available in Java 8
    @SuppressWarnings("deprecation")
    public void show() {

        // create the GUI for viewing the image if needed
        if (frame == null) {
            frame = new JFrame();

            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            menuBar.add(menu);
            JMenuItem menuItem1 = new JMenuItem(" Save...   ");
            menuItem1.addActionListener(this);
            menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                                     Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            menu.add(menuItem1);
            frame.setJMenuBar(menuBar);



            frame.setContentPane(getJLabel());
            // f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setTitle(title);
            frame.setResizable(false);
            frame.pack();
        }

        // draw
        frame.setVisible(true);
        isVisible = true;
        frame.repaint();
    }

   /**
     * Hides the window on the screen.
     */
    public void hide() {
        if (frame != null) {
            isVisible = false;
            frame.setVisible(false);
        }
    }

   /**
     * Is the window containing the picture visible?
     * @return {@code true} if the picture is visible, and {@code false} otherwise
     */
    public boolean isVisible() {
        return isVisible;
    }

   /**
     * Returns the height of the picture.
     *
     * @return the height of the picture (in pixels)
     */
    public int height() {
        return height;
    }

   /**
     * Returns the width of the picture.
     *
     * @return the width of the picture (in pixels)
     */
    public int width() {
        return width;
    }

    private void validateRowIndex(int row) {
        if (row < 0 || row >= height())
            throw new IllegalArgumentException("row index must be between 0 and " + (height() - 1) + ": " + row);
    }

    private void validateColumnIndex(int col) {
        if (col < 0 || col >= width())
            throw new IllegalArgumentException("column index must be between 0 and " + (width() - 1) + ": " + col);
    }

   /**
     * Returns the color of pixel ({@code col}, {@code row}) as a {@link java.awt.Color}.
     *
     * @param col the column index
     * @param row the row index
     * @return the color of pixel ({@code col}, {@code row})
     * @throws IllegalArgumentException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     */
    public Color get(int col, int row) {
        validateColumnIndex(col);
        validateRowIndex(row);
        int argb = getRGB(col, row);
        return new Color(argb, true);
    }

   /**
     * Returns the color of pixel ({@code col}, {@code row}) as an {@code int}.
     * Using this method can be more efficient than {@link #get(int, int)} because
     * it does not create a {@code Color} object.
     *
     * @param col the column index
     * @param row the row index
     * @return the integer representation of the color of pixel ({@code col}, {@code row})
     * @throws IllegalArgumentException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     */
    public int getRGB(int col, int row) {
        validateColumnIndex(col);
        validateRowIndex(row);
        if (isOriginUpperLeft) return image.getRGB(col, row);
        else                   return image.getRGB(col, height - row - 1);
    }

   /**
     * Sets the color of pixel ({@code col}, {@code row}) to given color.
     *
     * @param col the column index
     * @param row the row index
     * @param color the color
     * @throws IllegalArgumentException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     * @throws IllegalArgumentException if {@code color} is {@code null}
     */
    public void set(int col, int row, Color color) {
        validateColumnIndex(col);
        validateRowIndex(row);
        if (color == null) throw new IllegalArgumentException("color argument is null");
        int rgb = color.getRGB();
        setRGB(col, row, rgb);
    }

   /**
     * Sets the color of pixel ({@code col}, {@code row}) to given color.
     *
     * @param col the column index
     * @param row the row index
     * @param rgb the integer representation of the color
     * @throws IllegalArgumentException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     */
    public void setRGB(int col, int row, int rgb) {
        validateColumnIndex(col);
        validateRowIndex(row);
        if (isOriginUpperLeft) image.setRGB(col, row, rgb);
        else                   image.setRGB(col, height - row - 1, rgb);
    }

   /**
     * Returns true if this picture is equal to the argument picture.
     *
     * @param other the other picture
     * @return {@code true} if this picture is the same dimension as {@code other}
     *         and if all pixels have the same color; {@code false} otherwise
     */
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Picture that = (Picture) other;
        if (this.width()  != that.width())  return false;
        if (this.height() != that.height()) return false;
        for (int col = 0; col < width(); col++)
            for (int row = 0; row < height(); row++)
                if (this.getRGB(col, row) != that.getRGB(col, row)) return false;
        return true;
    }

   /**
     * Returns a string representation of this picture.
     * The result is a <code>width</code>-by-<code>height</code> matrix of pixels,
     * where the color of a pixel is represented using 6 hex digits to encode
     * the red, green, and blue components.
     *
     * @return a string representation of this picture
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(width +"-by-" + height + " picture (RGB values given in hex)\n");
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int rgb = 0;
                if (isOriginUpperLeft) rgb = image.getRGB(col, row);
                else                   rgb = image.getRGB(col, height - row - 1);
                sb.append(String.format("#%06X ", rgb & 0xFFFFFF));
            }
            sb.append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * This operation is not supported because pictures are mutable.
     *
     * @return does not return a value
     * @throws UnsupportedOperationException if called
     */
    public int hashCode() {
        throw new UnsupportedOperationException("hashCode() is not supported because pictures are mutable");
    }

    // does this picture use transparency (i.e., alpha < 255 for some pixel)?
    private boolean hasAlpha() {
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                int argb = image.getRGB(col, row);
                int alpha =  (argb >> 24) & 0xFF;
                if (alpha != 255) return true;
            }
        }
        return false;
    }

   /**
     * Saves the picture to a file in a supported file format
     * (typically JPEG, PNG, GIF TIFF, and BMP).
     * If the file format does not support transparency (such as JPEG
     * or BMP), it will be converted to be opaque (with purely
     * transparent pixels converted to black).
     *
     * @param filename the name of the file
     * @throws IllegalArgumentException if {@code filename} is {@code null}
     * @throws IllegalArgumentException if {@code filename} is the empty string
     */
    public void save(String filename) {
        if (filename == null) throw new IllegalArgumentException("argument to save() is null");
        if (filename.length() == 0) throw new IllegalArgumentException("argument to save() is the empty string");
        File file = new File(filename);
        save(file);
    }

   /**
     * Saves the picture to a file in a supported format
     * (typically JPEG, PNG, GIF TIFF, and BMP).
     *
     * @param  file the file
     * @throws IllegalArgumentException if {@code file} is {@code null}
     */
    public void save(File file) {
        if (file == null) throw new IllegalArgumentException("argument to save() is null");
        title = file.getName();

        String suffix = title.substring(title.lastIndexOf('.') + 1);
        if (!title.contains(".")) suffix = "";

        try {
            // for formats that support transparency (e.g., PNG and GIF)
            if (ImageIO.write(image, suffix, file)) return;

            // for formats that don't support transparency (e.g., JPG and BMP)
            // create BufferedImage in RGB format and use white background
            BufferedImage imageRGB = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            imageRGB.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
            if (ImageIO.write(imageRGB, suffix, file)) return;

            System.out.printf("Error: the filetype '%s' is not supported\n", suffix);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

   /**
     * Opens a save dialog box when the user selects "Save As" from the menu.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        FileDialog chooser = new FileDialog(frame,
                             "The filetype extension must be either .jpg or .png", FileDialog.SAVE);
        chooser.setVisible(true);
        if (chooser.getFile() != null) {
            save(chooser.getDirectory() + File.separator + chooser.getFile());
        }
    }

   /**
     * Unit tests this {@code Picture} data type.
     * Reads a picture specified by the command-line argument,
     * and shows it in a window on the screen.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        System.out.printf("%d-by-%d\n", picture.width(), picture.height());
        picture.show();
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
