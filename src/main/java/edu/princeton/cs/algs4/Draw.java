/******************************************************************************
 *  Compilation:  javac Draw.java
 *  Execution:    java Draw
 *  Dependencies: none
 *
 *  Drawing library. This class provides a basic capability for creating
 *  drawings with your programs. It uses a simple graphics model that
 *  allows you to create drawings consisting of points, lines, and curves
 *  in a window on your computer and to save the drawings to a file.
 *  This is the object-oriented version of standard draw; it supports
 *  multiple indepedent drawing windows.
 *
 *  Todo
 *  ----
 *    -  Add support for gradient fill, etc.
 *
 *  Remarks
 *  -------
 *    -  don't use AffineTransform for rescaling since it inverts
 *       images and strings
 *    -  careful using setFont in inner loop within an animation -
 *       it can cause flicker
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 *  <i>Draw</i>. This class provides a basic capability for
 *  creating drawings with your programs. It uses a simple graphics model that
 *  allows you to create drawings consisting of points, lines, and curves
 *  in a window on your computer and to save the drawings to a file.
 *  This is the object-oriented version of standard draw; it supports
 *  multiple indepedent drawing windows.
 *  <p>
 *  For additional documentation, see <a href="http://introcs.cs.princeton.edu/31datatype">Section 3.1</a> of
 *  <i>Computer Science: An Interdisciplinary Approach</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public final class Draw implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

    /**
     *  The color black.
     */
    public static final Color BLACK = Color.BLACK;

    /**
     *  The color blue.
     */
    public static final Color BLUE = Color.BLUE;

    /**
     *  The color cyan.
     */
    public static final Color CYAN = Color.CYAN;

    /**
     *  The color dark gray.
     */
    public static final Color DARK_GRAY = Color.DARK_GRAY;

    /**
     *  The color gray.
     */
    public static final Color GRAY = Color.GRAY;

    /**
     *  The color green.
     */
    public static final Color GREEN  = Color.GREEN;

    /**
     *  The color light gray.
     */
    public static final Color LIGHT_GRAY = Color.LIGHT_GRAY;

    /**
     *  The color magenta.
     */
    public static final Color MAGENTA = Color.MAGENTA;

    /**
     *  The color orange.
     */
    public static final Color ORANGE = Color.ORANGE;

    /**
     *  The color pink.
     */
    public static final Color PINK = Color.PINK;

    /**
     *  The color red.
     */
    public static final Color RED = Color.RED;

    /**
     *  The color white.
     */
    public static final Color WHITE = Color.WHITE;

    /**
     *  The color yellow.
     */
    public static final Color YELLOW = Color.YELLOW;

    /**
     * Shade of blue used in Introduction to Programming in Java.
     * It is Pantone 300U. The RGB values are approximately (9, 90, 166).
     */
    public static final Color BOOK_BLUE = new Color(9, 90, 166);

    /**
     * Shade of light blue used in Introduction to Programming in Java.
     * The RGB values are approximately (103, 198, 243).
     */
    public static final Color BOOK_LIGHT_BLUE = new Color(103, 198, 243);
    
    /**
     * Shade of red used in <em>Algorithms, 4th edition</em>.
     * It is Pantone 1805U. The RGB values are approximately (150, 35, 31).
     */
    public static final Color BOOK_RED = new Color(150, 35, 31);

    // default colors
    private static final Color DEFAULT_PEN_COLOR   = BLACK;
    private static final Color DEFAULT_CLEAR_COLOR = WHITE;

    // boundary of drawing canvas, 0% border
    private static final double BORDER = 0.0;
    private static final double DEFAULT_XMIN = 0.0;
    private static final double DEFAULT_XMAX = 1.0;
    private static final double DEFAULT_YMIN = 0.0;
    private static final double DEFAULT_YMAX = 1.0;

    // default canvas size is SIZE-by-SIZE
    private static final int DEFAULT_SIZE = 512;

    // default pen radius
    private static final double DEFAULT_PEN_RADIUS = 0.002;

    // default font
    private static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 16);

    // current pen color
    private Color penColor;

    // canvas size
    private int width  = DEFAULT_SIZE;
    private int height = DEFAULT_SIZE;

    // current pen radius
    private double penRadius;

    // show we draw immediately or wait until next show?
    private boolean defer = false;

    private double xmin, ymin, xmax, ymax;

    // name of window
    private String name = "Draw";

    // for synchronization
    private final Object mouseLock = new Object();
    private final Object keyLock = new Object();

    // current font
    private Font font;

    // the JLabel for drawing
    private JLabel draw;

    // double buffered graphics
    private BufferedImage offscreenImage, onscreenImage;
    private Graphics2D offscreen, onscreen;

    // the frame for drawing to the screen
    private JFrame frame = new JFrame();

    // mouse state
    private boolean mousePressed = false;
    private double mouseX = 0;
    private double mouseY = 0;

    // keyboard state
    private final LinkedList<Character> keysTyped = new LinkedList<Character>();
    private final TreeSet<Integer> keysDown = new TreeSet<Integer>();

    // event-based listeners
    private final ArrayList<DrawListener> listeners = new ArrayList<DrawListener>();


    /**
     * Initializes an empty drawing object with the given name.
     *
     * @param name the title of the drawing window.
     */
    public Draw(String name) {
        this.name = name;
        init();
    }

    /**
     * Initializes an empty drawing object.
     */
    public Draw() {
        init();
    }

    private void init() {
        if (frame != null) frame.setVisible(false);
        frame = new JFrame();
        offscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        onscreenImage  = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        offscreen = offscreenImage.createGraphics();
        onscreen  = onscreenImage.createGraphics();
        setXscale();
        setYscale();
        offscreen.setColor(DEFAULT_CLEAR_COLOR);
        offscreen.fillRect(0, 0, width, height);
        setPenColor();
        setPenRadius();
        setFont();
        clear();

        // add antialiasing
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                                                  RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        offscreen.addRenderingHints(hints);

        // frame stuff
        ImageIcon icon = new ImageIcon(onscreenImage);
        draw = new JLabel(icon);

        draw.addMouseListener(this);
        draw.addMouseMotionListener(this);

        frame.setContentPane(draw);
        frame.addKeyListener(this);    // JLabel cannot get keyboard focus
        frame.setResizable(false);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);            // closes all windows
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);      // closes only current window
        frame.setTitle(name);
        frame.setJMenuBar(createMenuBar());
        frame.pack();
        frame.requestFocusInWindow();
        frame.setVisible(true);
    }


    /**
     * Sets the upper-left hand corner of the drawing window to be (x, y), where (0, 0) is upper left.
     *
     * @param  x the number of pixels from the left
     * @param  y the number of pixels from the top
     * @throws IllegalArgumentException if the width or height is 0 or negative
     */
    public void setLocationOnScreen(int x, int y) {
        if (x <= 0 || y <= 0) throw new IllegalArgumentException();
        frame.setLocation(x, y);
    }

    /**
     * Sets the default close operation.
     *
     * @param  value the value, typically {@code JFrame.EXIT_ON_CLOSE}
     *         (close all windows) or {@code JFrame.DISPOSE_ON_CLOSE}
     *         (close current window)
     */
    public void setDefaultCloseOperation(int value) {
        frame.setDefaultCloseOperation(value);
    }
       

    /**
     * Sets the canvas (drawing area) to be <em>width</em>-by-<em>height</em> pixels.
     * This also erases the current drawing and resets the coordinate system, pen radius,
     * pen color, and font back to their default values.
     * Ordinarly, this method is called once, at the very beginning of a program.
     *
     * @param  canvasWidth the width as a number of pixels
     * @param  canvasHeight the height as a number of pixels
     * @throws IllegalArgumentException unless both {@code canvasWidth}
     *         and {@code canvasHeight} are positive
     */
    public void setCanvasSize(int canvasWidth, int canvasHeight) {
        if (canvasWidth < 1 || canvasHeight < 1) {
            throw new IllegalArgumentException("width and height must be positive");
        }
        width = canvasWidth;
        height = canvasHeight;
        init();
    }


    // create the menu bar (changed to private)
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        JMenuItem menuItem1 = new JMenuItem(" Save...   ");
        menuItem1.addActionListener(this);
        menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menu.add(menuItem1);
        return menuBar;
    }


   /***************************************************************************
    *  User and screen coordinate systems.
    ***************************************************************************/

    /**
     * Sets the x-scale to be the default (between 0.0 and 1.0).
     */
    public void setXscale() {
        setXscale(DEFAULT_XMIN, DEFAULT_XMAX);
    }

    /**
     * Sets the y-scale to be the default (between 0.0 and 1.0).
     */
    public void setYscale() {
        setYscale(DEFAULT_YMIN, DEFAULT_YMAX);
    }

    /**
     * Sets the x-scale.
     *
     * @param min the minimum value of the x-scale
     * @param max the maximum value of the x-scale
     */
    public void setXscale(double min, double max) {
        double size = max - min;
        xmin = min - BORDER * size;
        xmax = max + BORDER * size;
    }

    /**
     * Sets the y-scale.
     *
     * @param min the minimum value of the y-scale
     * @param max the maximum value of the y-scale
     */
    public void setYscale(double min, double max) {
        double size = max - min;
        ymin = min - BORDER * size;
        ymax = max + BORDER * size;
    }

    // helper functions that scale from user coordinates to screen coordinates and back
    private double  scaleX(double x) { return width  * (x - xmin) / (xmax - xmin); }
    private double  scaleY(double y) { return height * (ymax - y) / (ymax - ymin); }
    private double factorX(double w) { return w * width  / Math.abs(xmax - xmin);  }
    private double factorY(double h) { return h * height / Math.abs(ymax - ymin);  }
    private double   userX(double x) { return xmin + x * (xmax - xmin) / width;    }
    private double   userY(double y) { return ymax - y * (ymax - ymin) / height;   }


    /**
     * Clears the screen to the default color (white).
     */
    public void clear() {
        clear(DEFAULT_CLEAR_COLOR);
    }

    /**
     * Clears the screen to the given color.
     *
     * @param color the color to make the background
     */
    public void clear(Color color) {
        offscreen.setColor(color);
        offscreen.fillRect(0, 0, width, height);
        offscreen.setColor(penColor);
        draw();
    }

    /**
     * Gets the current pen radius.
     *
     * @return the current pen radius
     */
    public double getPenRadius() {
        return penRadius;
    }

    /**
     * Sets the pen size to the default (.002).
     */
    public void setPenRadius() {
        setPenRadius(DEFAULT_PEN_RADIUS);
    }

    /**
     * Sets the radius of the pen to the given size.
     *
     * @param  r the radius of the pen
     * @throws IllegalArgumentException if r is negative
     */
    public void setPenRadius(double r) {
        if (r < 0) throw new IllegalArgumentException("pen radius must be positive");
        penRadius = r * DEFAULT_SIZE;
        BasicStroke stroke = new BasicStroke((float) penRadius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        // BasicStroke stroke = new BasicStroke((float) penRadius);
        offscreen.setStroke(stroke);
    }

    /**
     * Gets the current pen color.
     *
     * @return the current pen color
     */
    public Color getPenColor() {
        return penColor;
    }

    /**
     * Sets the pen color to the default color (black).
     */
    public void setPenColor() {
        setPenColor(DEFAULT_PEN_COLOR);
    }

    /**
     * Sets the pen color to the given color.
     *
     * @param color the color to make the pen
     */
    public void setPenColor(Color color) {
        penColor = color;
        offscreen.setColor(penColor);
    }

    /**
     * Sets the pen color to the given RGB color.
     *
     * @param  red the amount of red (between 0 and 255)
     * @param  green the amount of green (between 0 and 255)
     * @param  blue the amount of blue (between 0 and 255)
     * @throws IllegalArgumentException if the amount of red, green, or blue are outside prescribed range
     */
    public void setPenColor(int red, int green, int blue) {
        if (red   < 0 || red   >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
        if (green < 0 || green >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
        if (blue  < 0 || blue  >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
        setPenColor(new Color(red, green, blue));
    }


    /**
     * Turns on xor mode.
     */
    public void xorOn() {
        offscreen.setXORMode(DEFAULT_CLEAR_COLOR);
    }

    /**
     * Turns off xor mode.
     */
    public void xorOff() {
        offscreen.setPaintMode();
    }

    /**
     * Gets the current {@code JLabel} for use in some other GUI.
     *
     * @return the current {@code JLabel}
     */
    public JLabel getJLabel() {
        return draw;
    }

    /**
     * Gets the current font.
     *
     * @return the current font
     */
    public Font getFont() {
        return font;
    }

    /**
     * Sets the font to the default font (sans serif, 16 point).
     */
    public void setFont() {
        setFont(DEFAULT_FONT);
    }

    /**
     * Sets the font to the given value.
     *
     * @param font the font
     */
    public void setFont(Font font) {
        this.font = font;
    }


   /***************************************************************************
    *  Drawing geometric shapes.
    ***************************************************************************/

    /**
     * Draws a line from (x0, y0) to (x1, y1).
     *
     * @param x0 the x-coordinate of the starting point
     * @param y0 the y-coordinate of the starting point
     * @param x1 the x-coordinate of the destination point
     * @param y1 the y-coordinate of the destination point
     */
    public void line(double x0, double y0, double x1, double y1) {
        offscreen.draw(new Line2D.Double(scaleX(x0), scaleY(y0), scaleX(x1), scaleY(y1)));
        draw();
    }

    /**
     * Draws one pixel at (x, y).
     *
     * @param x the x-coordinate of the pixel
     * @param y the y-coordinate of the pixel
     */
    private void pixel(double x, double y) {
        offscreen.fillRect((int) Math.round(scaleX(x)), (int) Math.round(scaleY(y)), 1, 1);
    }

    /**
     * Draws a point at (x, y).
     *
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     */
    public void point(double x, double y) {
        double xs = scaleX(x);
        double ys = scaleY(y);
        double r = penRadius;
        // double ws = factorX(2*r);
        // double hs = factorY(2*r);
        // if (ws <= 1 && hs <= 1) pixel(x, y);
        if (r <= 1) pixel(x, y);
        else offscreen.fill(new Ellipse2D.Double(xs - r/2, ys - r/2, r, r));
        draw();
    }

    /**
     * Draws a circle of radius r, centered on (x, y).
     *
     * @param  x the x-coordinate of the center of the circle
     * @param  y the y-coordinate of the center of the circle
     * @param  r the radius of the circle
     * @throws IllegalArgumentException if the radius of the circle is negative
     */
    public void circle(double x, double y, double r) {
        if (r < 0) throw new IllegalArgumentException("circle radius can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    /**
     * Draws a filled circle of radius r, centered on (x, y).
     *
     * @param  x the x-coordinate of the center of the circle
     * @param  y the y-coordinate of the center of the circle
     * @param  r the radius of the circle
     * @throws IllegalArgumentException if the radius of the circle is negative
     */
    public void filledCircle(double x, double y, double r) {
        if (r < 0) throw new IllegalArgumentException("circle radius can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }


    /**
     * Draws an ellipse with given semimajor and semiminor axes, centered on (x, y).
     *
     * @param  x the x-coordinate of the center of the ellipse
     * @param  y the y-coordinate of the center of the ellipse
     * @param  semiMajorAxis is the semimajor axis of the ellipse
     * @param  semiMinorAxis is the semiminor axis of the ellipse
     * @throws IllegalArgumentException if either of the axes are negative
     */
    public void ellipse(double x, double y, double semiMajorAxis, double semiMinorAxis) {
        if (semiMajorAxis < 0) throw new IllegalArgumentException("ellipse semimajor axis can't be negative");
        if (semiMinorAxis < 0) throw new IllegalArgumentException("ellipse semiminor axis can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*semiMajorAxis);
        double hs = factorY(2*semiMinorAxis);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    /**
     * Draws an ellipse with given semimajor and semiminor axes, centered on (x, y).
     * @param  x the x-coordinate of the center of the ellipse
     * @param  y the y-coordinate of the center of the ellipse
     * @param  semiMajorAxis is the semimajor axis of the ellipse
     * @param  semiMinorAxis is the semiminor axis of the ellipse
     * @throws IllegalArgumentException if either of the axes are negative
     */
    public void filledEllipse(double x, double y, double semiMajorAxis, double semiMinorAxis) {
        if (semiMajorAxis < 0) throw new IllegalArgumentException("ellipse semimajor axis can't be negative");
        if (semiMinorAxis < 0) throw new IllegalArgumentException("ellipse semiminor axis can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*semiMajorAxis);
        double hs = factorY(2*semiMinorAxis);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    /**
     * Draws an arc of radius r, centered on (x, y), from angle1 to angle2 (in degrees).
     *
     * @param  x the x-coordinate of the center of the circle
     * @param  y the y-coordinate of the center of the circle
     * @param  r the radius of the circle
     * @param  angle1 the starting angle. 0 would mean an arc beginning at 3 o'clock.
     * @param  angle2 the angle at the end of the arc. For example, if
     *         you want a 90 degree arc, then angle2 should be angle1 + 90.
     * @throws IllegalArgumentException if the radius of the circle is negative
     */
    public void arc(double x, double y, double r, double angle1, double angle2) {
        if (r < 0) throw new IllegalArgumentException("arc radius can't be negative");
        while (angle2 < angle1) angle2 += 360;
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Arc2D.Double(xs - ws/2, ys - hs/2, ws, hs, angle1, angle2 - angle1, Arc2D.OPEN));
        draw();
    }

    /**
     * Draws a square of side length 2r, centered on (x, y).
     *
     * @param  x the x-coordinate of the center of the square
     * @param  y the y-coordinate of the center of the square
     * @param  r radius is half the length of any side of the square
     * @throws IllegalArgumentException if r is negative
     */
    public void square(double x, double y, double r) {
        if (r < 0) throw new IllegalArgumentException("square side length can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    /**
     * Draws a filled square of side length 2r, centered on (x, y).
     *
     * @param  x the x-coordinate of the center of the square
     * @param  y the y-coordinate of the center of the square
     * @param  r radius is half the length of any side of the square
     * @throws IllegalArgumentException if r is negative
     */
    public void filledSquare(double x, double y, double r) {
        if (r < 0) throw new IllegalArgumentException("square side length can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }


    /**
     * Draws a rectangle of given half width and half height, centered on (x, y).
     *
     * @param  x the x-coordinate of the center of the rectangle
     * @param  y the y-coordinate of the center of the rectangle
     * @param  halfWidth is half the width of the rectangle
     * @param  halfHeight is half the height of the rectangle
     * @throws IllegalArgumentException if halfWidth or halfHeight is negative
     */
    public void rectangle(double x, double y, double halfWidth, double halfHeight) {
        if (halfWidth  < 0) throw new IllegalArgumentException("half width can't be negative");
        if (halfHeight < 0) throw new IllegalArgumentException("half height can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*halfWidth);
        double hs = factorY(2*halfHeight);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    /**
     * Draws a filled rectangle of given half width and half height, centered on (x, y).
     *
     * @param  x the x-coordinate of the center of the rectangle
     * @param  y the y-coordinate of the center of the rectangle
     * @param  halfWidth is half the width of the rectangle
     * @param  halfHeight is half the height of the rectangle
     * @throws IllegalArgumentException if halfWidth or halfHeight is negative
     */
    public void filledRectangle(double x, double y, double halfWidth, double halfHeight) {
        if (halfWidth  < 0) throw new IllegalArgumentException("half width can't be negative");
        if (halfHeight < 0) throw new IllegalArgumentException("half height can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*halfWidth);
        double hs = factorY(2*halfHeight);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    /**
     * Draws a polygon with the given (x[i], y[i]) coordinates.
     *
     * @param x an array of all the x-coordindates of the polygon
     * @param y an array of all the y-coordindates of the polygon
     */
    public void polygon(double[] x, double[] y) {
        int n = x.length;
        GeneralPath path = new GeneralPath();
        path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
        for (int i = 0; i < n; i++)
            path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
        path.closePath();
        offscreen.draw(path);
        draw();
    }

    /**
     * Draws a filled polygon with the given (x[i], y[i]) coordinates.
     *
     * @param x an array of all the x-coordindates of the polygon
     * @param y an array of all the y-coordindates of the polygon
     */
    public void filledPolygon(double[] x, double[] y) {
        int n = x.length;
        GeneralPath path = new GeneralPath();
        path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
        for (int i = 0; i < n; i++)
            path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
        path.closePath();
        offscreen.fill(path);
        draw();
    }



   /***************************************************************************
    *  Drawing images.
    ***************************************************************************/

    private static BufferedImage getImage(String filename) {

        // from a file or URL
        try {
            URL url = new URL(filename);
            return ImageIO.read(url);
        } 
        catch (IOException e) {
            // ignore
        }

        // in case file is inside a .jar (classpath relative to StdDraw)
        try {
            URL url = StdDraw.class.getResource(filename);
            return ImageIO.read(url);
        } 
        catch (IOException e) {
            // ignore
        }

        // in case file is inside a .jar (classpath relative to root of jar)
        try {
            URL url = StdDraw.class.getResource("/" + filename);
            return ImageIO.read(url);
        } 
        catch (IOException e) {
            // ignore
        }
        throw new IllegalArgumentException("image " + filename + " not found");
    }

    /**
     * Draws picture (gif, jpg, or png) centered on (x, y).
     *
     * @param  x the center x-coordinate of the image
     * @param  y the center y-coordinate of the image
     * @param  filename the name of the image/picture, e.g., "ball.gif"
     * @throws IllegalArgumentException if the image is corrupt
     * @throws IllegalArgumentException if {@code filename} is {@code null}
     */
    public void picture(double x, double y, String filename) {
        if (filename == null) throw new IllegalArgumentException("filename argument is null");
        BufferedImage image = getImage(filename);
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = image.getWidth();
        int hs = image.getHeight();
        if (ws < 0 || hs < 0) throw new IllegalArgumentException("image " + filename + " is corrupt");

        offscreen.drawImage(image, (int) Math.round(xs - ws/2.0), (int) Math.round(ys - hs/2.0), null);
        draw();
    }

    /**
     * Draws picture (gif, jpg, or png) centered on (x, y),
     * rotated given number of degrees.
     *
     * @param  x the center x-coordinate of the image
     * @param  y the center y-coordinate of the image
     * @param  filename the name of the image/picture, e.g., "ball.gif"
     * @param  degrees is the number of degrees to rotate counterclockwise
     * @throws IllegalArgumentException if the image is corrupt
     * @throws IllegalArgumentException if {@code filename} is {@code null}
     */
    public void picture(double x, double y, String filename, double degrees) {
        if (filename == null) throw new IllegalArgumentException("filename argument is null");
        BufferedImage image = getImage(filename);
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = image.getWidth();
        int hs = image.getHeight();
        if (ws < 0 || hs < 0) throw new IllegalArgumentException("image " + filename + " is corrupt");

        offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        offscreen.drawImage(image, (int) Math.round(xs - ws/2.0), (int) Math.round(ys - hs/2.0), null);
        offscreen.rotate(Math.toRadians(+degrees), xs, ys);

        draw();
    }

    /**
     * Draws picture (gif, jpg, or png) centered on (x, y), rescaled to w-by-h.
     *
     * @param  x the center x coordinate of the image
     * @param  y the center y coordinate of the image
     * @param  filename the name of the image/picture, e.g., "ball.gif"
     * @param  w the width of the image
     * @param  h the height of the image
     * @throws IllegalArgumentException if the image is corrupt
     * @throws IllegalArgumentException if {@code filename} is {@code null}
     */
    public void picture(double x, double y, String filename, double w, double h) {
        if (filename == null) throw new IllegalArgumentException("filename argument is null");
        Image image = getImage(filename);
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(w);
        double hs = factorY(h);
        if (ws < 0 || hs < 0) throw new IllegalArgumentException("image " + filename + " is corrupt");
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else {
            offscreen.drawImage(image, (int) Math.round(xs - ws/2.0),
                                       (int) Math.round(ys - hs/2.0),
                                       (int) Math.round(ws),
                                       (int) Math.round(hs), null);
        }
        draw();
    }


    /**
     * Draws picture (gif, jpg, or png) centered on (x, y), rotated
     * given number of degrees, rescaled to w-by-h.
     *
     * @param  x the center x-coordinate of the image
     * @param  y the center y-coordinate of the image
     * @param  filename the name of the image/picture, e.g., "ball.gif"
     * @param  w the width of the image
     * @param  h the height of the image
     * @param  degrees is the number of degrees to rotate counterclockwise
     * @throws IllegalArgumentException if the image is corrupt
     * @throws IllegalArgumentException if {@code filename} is {@code null}
     */
    public void picture(double x, double y, String filename, double w, double h, double degrees) {
        if (filename == null) throw new IllegalArgumentException("filename argument is null");
        Image image = getImage(filename);
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(w);
        double hs = factorY(h);
        if (ws < 0 || hs < 0) throw new IllegalArgumentException("image " + filename + " is corrupt");
        if (ws <= 1 && hs <= 1) pixel(x, y);

        offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        offscreen.drawImage(image, (int) Math.round(xs - ws/2.0),
                                   (int) Math.round(ys - hs/2.0),
                                   (int) Math.round(ws),
                                   (int) Math.round(hs), null);
        offscreen.rotate(Math.toRadians(+degrees), xs, ys);

        draw();
    }


   /***************************************************************************
    *  Drawing text.
    ***************************************************************************/

    /**
     * Writes the given text string in the current font, centered on (x, y).
     *
     * @param x the center x-coordinate of the text
     * @param y the center y-coordinate of the text
     * @param s the text
     */
    public void text(double x, double y, String s) {
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = metrics.stringWidth(s);
        int hs = metrics.getDescent();
        offscreen.drawString(s, (float) (xs - ws/2.0), (float) (ys + hs));
        draw();
    }

    /**
     * Writes the given text string in the current font, centered on (x, y) and
     * rotated by the specified number of degrees.
     *
     * @param x the center x-coordinate of the text
     * @param y the center y-coordinate of the text
     * @param s the text
     * @param degrees is the number of degrees to rotate counterclockwise
     */
    public void text(double x, double y, String s, double degrees) {
        double xs = scaleX(x);
        double ys = scaleY(y);
        offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        text(x, y, s);
        offscreen.rotate(Math.toRadians(+degrees), xs, ys);
    }

    /**
     * Writes the given text string in the current font, left-aligned at (x, y).
     *
     * @param x the x-coordinate of the text
     * @param y the y-coordinate of the text
     * @param s the text
     */
    public void textLeft(double x, double y, String s) {
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        // int ws = metrics.stringWidth(s);
        int hs = metrics.getDescent();
        offscreen.drawString(s, (float) xs, (float) (ys + hs));
        draw();
    }


    /**
     * Displays on screen, pause for {@code t} milliseconds, and turn on
     * <em>animation mode</em>.
     * Subsequent calls to drawing methods such as {@code line()}, {@code circle()},
     * and {@code square()} will not be displayed on screen until the next call to {@code show()}.
     * This is useful for producing animations (clear the screen, draw a bunch of shapes,
     * display on screen for a fixed amount of time, and repeat). It also speeds up
     * drawing a huge number of shapes (call {@code show(0)} to defer drawing
     * on screen, draw the shapes, and call {@code show(0)} to display them all
     * on screen at once).
     *
     * @param t number of milliseconds
     */
    public void show(int t) {
        defer = false;
        draw();
        try {
            Thread.sleep(t);
        }
        catch (InterruptedException e) {
            System.out.println("Error sleeping");
        }
        defer = true;
    }


    /**
     * Displays on-screen and turn off animation mode.
     * Subsequent calls to drawing methods such as {@code line()}, {@code circle()},
     * and {@code square()} will be displayed on screen when called. This is the default.
     */
    public void show() {
        defer = false;
        draw();
    }

    // draw onscreen if defer is false
    private void draw() {
        if (defer) return;
        onscreen.drawImage(offscreenImage, 0, 0, null);
        frame.repaint();
    }

    /**
     * Saves this drawing to a file.
     *
     * @param  filename the name of the file (with suffix png, jpg, or gif)
     */
    public void save(String filename) {
        File file = new File(filename);
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);

        // png files
        if ("png".equalsIgnoreCase(suffix)) {
            try {
                ImageIO.write(offscreenImage, suffix, file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        // need to change from ARGB to RGB for jpeg
        // reference: http://archives.java.sun.com/cgi-bin/wa?A2=ind0404&L=java2d-interest&D=0&P=2727
        else if ("jpg".equalsIgnoreCase(suffix)) {
            WritableRaster raster = offscreenImage.getRaster();
            WritableRaster newRaster;
            newRaster = raster.createWritableChild(0, 0, width, height, 0, 0, new int[] {0, 1, 2});
            DirectColorModel cm = (DirectColorModel) offscreenImage.getColorModel();
            DirectColorModel newCM = new DirectColorModel(cm.getPixelSize(),
                                                          cm.getRedMask(),
                                                          cm.getGreenMask(),
                                                          cm.getBlueMask());
            BufferedImage rgbBuffer = new BufferedImage(newCM, newRaster, false,  null);
            try {
                ImageIO.write(rgbBuffer, suffix, file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        else {
            System.out.println("Invalid image file type: " + suffix);
        }
    }


    /**
     * This method cannot be called directly.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        FileDialog chooser = new FileDialog(frame, "Use a .png or .jpg extension", FileDialog.SAVE);
        chooser.setVisible(true);
        String filename = chooser.getFile();
        if (filename != null) {
            save(chooser.getDirectory() + File.separator + chooser.getFile());
        }
    }



   /***************************************************************************
    *  Event-based interactions.
    ***************************************************************************/

    /**
     * Adds a {@link DrawListener} to listen to keyboard and mouse events.
     *
     * @param listener the {\tt DrawListener} argument
     */
    public void addListener(DrawListener listener) {
        // ensure there is a window for listenting to events
        show();
        listeners.add(listener);
        frame.addKeyListener(this);
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
        frame.setFocusable(true); 
    }




   /***************************************************************************
    *  Mouse interactions.
    ***************************************************************************/

    /**
     * Returns true if the mouse is being pressed.
     *
     * @return {@code true} if the mouse is being pressed;
     *         {@code false} otherwise
     */
    public boolean mousePressed() {
        synchronized (mouseLock) {
            return mousePressed;
        }
    }

    /**
     * Returns the x-coordinate of the mouse.
     * @return the x-coordinate of the mouse
     */
    public double mouseX() {
        synchronized (mouseLock) {
            return mouseX;
        }
    }

    /**
     * Returns the y-coordinate of the mouse.
     *
     * @return the y-coordinate of the mouse
     */
    public double mouseY() {
        synchronized (mouseLock) {
            return mouseY;
        }
    }



    /**
     * This method cannot be called directly.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // this body is intentionally left empty
    }

    /**
     * This method cannot be called directly.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        // this body is intentionally left empty
    }

    /**
     * This method cannot be called directly.
     */
    @Override
    public void mouseExited(MouseEvent e) {
        // this body is intentionally left empty
    }

    /**
     * This method cannot be called directly.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        synchronized (mouseLock) {
            mouseX = userX(e.getX());
            mouseY = userY(e.getY());
            mousePressed = true;
        }
        if (e.getButton() == MouseEvent.BUTTON1) {
            for (DrawListener listener : listeners)
                listener.mousePressed(userX(e.getX()), userY(e.getY()));
        }

    }

    /**
     * This method cannot be called directly.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        synchronized (mouseLock) {
            mousePressed = false;
        }
        if (e.getButton() == MouseEvent.BUTTON1) {
            for (DrawListener listener : listeners)
                listener.mouseReleased(userX(e.getX()), userY(e.getY()));
        }
    }

    /**
     * This method cannot be called directly.
     */
    @Override
    public void mouseDragged(MouseEvent e)  {
        synchronized (mouseLock) {
            mouseX = userX(e.getX());
            mouseY = userY(e.getY());
        }
        // doesn't seem to work if a button is specified
        for (DrawListener listener : listeners)
            listener.mouseDragged(userX(e.getX()), userY(e.getY()));
    }

    /**
     * This method cannot be called directly.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        synchronized (mouseLock) {
            mouseX = userX(e.getX());
            mouseY = userY(e.getY());
        }
    }


   /***************************************************************************
    *  Keyboard interactions.
    ***************************************************************************/

    /**
     * Returns true if the user has typed a key.
     *
     * @return {@code true} if the user has typed a key; {@code false} otherwise
     */
    public boolean hasNextKeyTyped() {
        synchronized (keyLock) {
            return !keysTyped.isEmpty();
        }
    }

    /**
     * The next key typed by the user.
     *
     * @return the next key typed by the user
     */
    public char nextKeyTyped() {
        synchronized (keyLock) {
            return keysTyped.removeLast();
        }
    }

   /**
     * Returns true if the keycode is being pressed.
     * <p>
     * This method takes as an argument the keycode (corresponding to a physical key).
     * It can handle action keys (such as F1 and arrow keys) and modifier keys
     * (such as shift and control).
     * See {@link KeyEvent} for a description of key codes.
     *
     * @param  keycode the keycode to check
     * @return {@code true} if {@code keycode} is currently being pressed;
     *         {@code false} otherwise
     */
    public boolean isKeyPressed(int keycode) {
        synchronized (keyLock) {
            return keysDown.contains(keycode);
        }
    }

    /**
     * This method cannot be called directly.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        synchronized (keyLock) {
            keysTyped.addFirst(e.getKeyChar());
        }

        // notify all listeners
        for (DrawListener listener : listeners)
            listener.keyTyped(e.getKeyChar());
    }

    /**
     * This method cannot be called directly.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        synchronized (keyLock) {
            keysDown.add(e.getKeyCode());
        }

        // notify all listeners
        for (DrawListener listener : listeners)
            listener.keyPressed(e.getKeyCode());
    }

    /**
     * This method cannot be called directly.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        synchronized (keyLock) {
            keysDown.remove(e.getKeyCode());
        }

        // notify all listeners
        for (DrawListener listener : listeners)
            listener.keyPressed(e.getKeyCode());
    }




    /**
     * Test client.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        // create one drawing window
        Draw draw1 = new Draw("Test client 1");
        draw1.square(.2, .8, .1);
        draw1.filledSquare(.8, .8, .2);
        draw1.circle(.8, .2, .2);
        draw1.setPenColor(Draw.MAGENTA);
        draw1.setPenRadius(.02);
        draw1.arc(.8, .2, .1, 200, 45);


        // create another one
        Draw draw2 = new Draw("Test client 2");
        draw2.setCanvasSize(900, 200);
        // draw a blue diamond
        draw2.setPenRadius();
        draw2.setPenColor(Draw.BLUE);
        double[] x = { .1, .2, .3, .2 };
        double[] y = { .2, .3, .2, .1 };
        draw2.filledPolygon(x, y);

        // text
        draw2.setPenColor(Draw.BLACK);
        draw2.text(0.2, 0.5, "bdfdfdfdlack text");
        draw2.setPenColor(Draw.WHITE);
        draw2.text(0.8, 0.8, "white text");
    }

}

/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
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
