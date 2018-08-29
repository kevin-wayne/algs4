/******************************************************************************
 *  Compilation:  javac GrayscalePicture.java
 *  Execution:    java GrayscalePicture imagename
 *  Dependencies: none
 *
 *  Data type for manipulating individual pixels of a grayscale image. The
 *  original image can be read from a file in JPEG, GIF, or PNG format, or the
 *  user can create a blank image of a given dimension. Includes methods for
 *  displaying the image in a window on the screen or saving to a file.
 *
 *  % java GrayscalePicture mandrill.jpg
 *
 *  Remarks
 *  -------
 *   - pixel (x, y) is column x and row y, where (0, 0) is upper left
 *
 *   - uses BufferedImage.TYPE_INT_RGB because BufferedImage.TYPE_BYTE_GRAY
 *     seems to do some undesirable olor correction when calling getRGB() and
 *     setRGB()
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
 *  This class provides methods for manipulating individual pixels of
 *  a grayscale image.
 *  The original image can be read from a {@code PNG}, {@code GIF},
 *  or {@code JPEG} file or the user can create a blank image of a given dimension.
 *  This class includes methods for displaying the image in a window on
 *  the screen or saving it to a file.
 *  <p>
 *  Pixel (<em>col</em>, <em>row</em>) is column <em>col</em> and row <em>row</em>.
 *  By default, the origin (0, 0) is the pixel in the top-left corner,
 *  which is a common convention in image processing.
 *  The method {@link #setOriginLowerLeft()} change the origin to the lower left.
 *  <p>
 *  The {@code get()} and {@code set()} methods use {@link Color} objects to get
 *  or set the color of the specified pixel. The {@link Color} objects are converted
 *  to grayscale if they have different values for the R, G, and B channels.
 *  The {@code getGrayscale()} and {@code setGrayscale()} methods use an
 *  8-bit {@code int} to encode the grayscale value, thereby avoiding the need to
 *  create temporary {@code Color} objects.
 *  <p>
 *  A <em>W</em>-by-<en>H</em> picture uses ~ 4 <em>W H</em> bytes of memory,
 *  since the color of each pixel is encoded as a 32-bit <code>int</code>
 *  (even though, in principle, only ~ <em>W H</em> bytes are needed).
 *  <p>
 *  For additional documentation, see
 *  <a href="https://introcs.cs.princeton.edu/31datatype">Section 3.1</a> of
 *  <i>Computer Science: An Interdisciplinary Approach</i>
 *  by Robert Sedgewick and Kevin Wayne.
 *  See {@link Picture} for a version that supports 32-bit RGB color images.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public final class GrayscalePicture implements ActionListener {
    private BufferedImage image;               // the rasterized image
    private JFrame frame;                      // on-screen view
    private String filename;                   // name of file
    private boolean isOriginUpperLeft = true;  // location of origin
    private final int width, height;           // width and height

   /**
     * Creates a {@code width}-by-{@code height} picture, with {@code width} columns
     * and {@code height} rows, where each pixel is black.
     *
     * @param width the width of the picture
     * @param height the height of the picture
     * @throws IllegalArgumentException if {@code width} is negative
     * @throws IllegalArgumentException if {@code height} is negative
     */
    public GrayscalePicture(int width, int height) {
        if (width  < 0) throw new IllegalArgumentException("width must be non-negative");
        if (height < 0) throw new IllegalArgumentException("height must be non-negative");
        this.width  = width;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

   /**
     * Creates a new grayscale picture that is a deep copy of the argument picture.
     *
     * @param  picture the picture to copy
     * @throws IllegalArgumentException if {@code picture} is {@code null}
     */
    public GrayscalePicture(GrayscalePicture picture) {
        if (picture == null) throw new IllegalArgumentException("constructor argument is null");

        width  = picture.width();
        height = picture.height();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        filename = picture.filename;
        isOriginUpperLeft = picture.isOriginUpperLeft;
        for (int col = 0; col < width(); col++)
            for (int row = 0; row < height(); row++)
                image.setRGB(col, row, picture.image.getRGB(col, row));
    }

   /**
     * Creates a grayscale picture by reading an image from a file or URL.
     *
     * @param  filename the name of the file (.png, .gif, or .jpg) or URL.
     * @throws IllegalArgumentException if cannot read image
     * @throws IllegalArgumentException if {@code filename} is {@code null}
     */
    public GrayscalePicture(String filename) {
        if (filename == null) throw new IllegalArgumentException("constructor argument is null");
        this.filename = filename;
        try {
            // try to read from file in working directory
            File file = new File(filename);
            if (file.isFile()) {
                image = ImageIO.read(file);
            }

            // now try to read from file in same directory as this .class file
            else {
                URL url = getClass().getResource(filename);
                if (url == null) {
                    url = new URL(filename);
                }
                image = ImageIO.read(url);
            }

            if (image == null) {
                throw new IllegalArgumentException("could not read image file: " + filename);
            }

            width  = image.getWidth(null);
            height = image.getHeight(null);

            // convert to grayscale inplace
            for (int col = 0; col < width; col++) {
                for (int row = 0; row < height; row++) {
                    Color color = new Color(image.getRGB(col, row));
                    Color gray = toGray(color);
                    image.setRGB(col, row, gray.getRGB());
                }
            }
        }
        catch (IOException ioe) {
            throw new IllegalArgumentException("could not open image file: " + filename, ioe);
        }
    }

     // Returns a grayscale version of the given color as a Color object.
    private static Color toGray(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int y = (int) (Math.round(0.299*r + 0.587*g + 0.114*b));
        return new Color(y, y, y);
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
    public void show() {

        // create the GUI for viewing the image if needed
        if (frame == null) {
            frame = new JFrame();

            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            menuBar.add(menu);
            JMenuItem menuItem1 = new JMenuItem(" Save...   ");
            menuItem1.addActionListener(this);
            // use getMenuShortcutKeyMaskEx() in Java 10 (getMenuShortcutKeyMask() deprecated)
            menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                                     Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            menu.add(menuItem1);
            frame.setJMenuBar(menuBar);



            frame.setContentPane(getJLabel());
            // f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            if (filename == null) frame.setTitle(width + "-by-" + height);
            else                  frame.setTitle(filename);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        }

        // draw
        frame.repaint();
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

    private void validateGrayscaleValue(int gray) {
        if (gray < 0 || gray >= 256)
            throw new IllegalArgumentException("grayscale value must be between 0 and 255");
    }

   /**
     * Returns the grayscale value of pixel ({@code col}, {@code row}) as a {@link java.awt.Color}.
     *
     * @param col the column index
     * @param row the row index
     * @return the grayscale value of pixel ({@code col}, {@code row})
     * @throws IllegalArgumentException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     */
    public Color get(int col, int row) {
        validateColumnIndex(col);
        validateRowIndex(row);
        Color color = new Color(image.getRGB(col, row));
        return toGray(color);
    }

   /**
     * Returns the grayscale value of pixel ({@code col}, {@code row}) as an {@code int}
     * between 0 and 255.
     * Using this method can be more efficient than {@link #get(int, int)} because
     * it does not create a {@code Color} object.
     *
     * @param col the column index
     * @param row the row index
     * @return the 8-bit integer representation of the grayscale value of pixel ({@code col}, {@code row})
     * @throws IllegalArgumentException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     */
    public int getGrayscale(int col, int row) {
        validateColumnIndex(col);
        validateRowIndex(row);
        if (isOriginUpperLeft) return image.getRGB(col, row) & 0xFF;
        else                   return image.getRGB(col, height - row - 1) & 0xFF;
    }

   /**
     * Sets the color of pixel ({@code col}, {@code row}) to the given grayscale value.
     *
     * @param col the column index
     * @param row the row index
     * @param color the color (converts to grayscale if color is not a shade of gray)
     * @throws IllegalArgumentException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     * @throws IllegalArgumentException if {@code color} is {@code null}
     */
    public void set(int col, int row, Color color) {
        validateColumnIndex(col);
        validateRowIndex(row);
        if (color == null) throw new IllegalArgumentException("color argument is null");
        Color gray = toGray(color);
        image.setRGB(col, row, gray.getRGB());
    }

   /**
     * Sets the color of pixel ({@code col}, {@code row}) to the given grayscale value
     * between 0 and 255.
     *
     * @param col the column index
     * @param row the row index
     * @param gray the 8-bit integer representation of the grayscale value
     * @throws IllegalArgumentException unless both {@code 0 <= col < width} and {@code 0 <= row < height}
     */
    public void setGrayscale(int col, int row, int gray) {
        validateColumnIndex(col);
        validateRowIndex(row);
        validateGrayscaleValue(gray);
        int rgb = gray | (gray << 8) | (gray << 16);
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
        GrayscalePicture that = (GrayscalePicture) other;
        if (this.width()  != that.width())  return false;
        if (this.height() != that.height()) return false;
        for (int col = 0; col < width(); col++)
            for (int row = 0; row < height(); row++)
                if (this.getGrayscale(col, row) != that.getGrayscale(col, row)) return false;
        return true;
    }

   /**
     * Returns a string representation of this picture.
     * The result is a <code>width</code>-by-<code>height</code> matrix of pixels,
     * where the grayscale value of a pixel is an integer between 0 and 255.
     *
     * @return a string representation of this picture
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(width +"-by-" + height + " grayscale picture (grayscale values given in hex)\n");
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int gray = 0;
                if (isOriginUpperLeft) gray = 0xFF & image.getRGB(col, row);
                else                   gray = 0xFF & image.getRGB(col, height - row - 1);
                sb.append(String.format("%3d ", gray));
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

   /**
     * Saves the picture to a file in either PNG or JPEG format.
     * The filetype extension must be either .png or .jpg.
     *
     * @param name the name of the file
     * @throws IllegalArgumentException if {@code name} is {@code null}
     */
    public void save(String name) {
        if (name == null) throw new IllegalArgumentException("argument to save() is null");
        save(new File(name));
        filename = name;
    }

   /**
     * Saves the picture to a file in a PNG or JPEG image format.
     *
     * @param  file the file
     * @throws IllegalArgumentException if {@code file} is {@code null}
     */
    public void save(File file) {
        if (file == null) throw new IllegalArgumentException("argument to save() is null");
        filename = file.getName();
        if (frame != null) frame.setTitle(filename);
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);
        if ("jpg".equalsIgnoreCase(suffix) || "png".equalsIgnoreCase(suffix)) {
            try {
                ImageIO.write(image, suffix, file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Error: filename must end in .jpg or .png");
        }
    }

   /**
     * Opens a save dialog box when the user selects "Save As" from the menu.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        FileDialog chooser = new FileDialog(frame,
                             "Use a .png or .jpg extension", FileDialog.SAVE);
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
        GrayscalePicture picture = new GrayscalePicture(args[0]);
        StdOut.printf("%d-by-%d\n", picture.width(), picture.height());
        GrayscalePicture copy = new GrayscalePicture(picture);
        picture.show();
        copy.show();
        while (!StdIn.isEmpty()) {
            int row = StdIn.readInt();
            int col = StdIn.readInt();
            int gray = StdIn.readInt();
            picture.setGrayscale(row, col, gray);
            StdOut.println(picture.get(row, col));
            StdOut.println(picture.getGrayscale(row, col));
        }
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
