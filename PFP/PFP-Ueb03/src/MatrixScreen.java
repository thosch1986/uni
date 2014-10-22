/**
 * created 30.03.2008
 */
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Creates thread-safe pixel-based screen
 * 
 * @author Marc Woerlein<woerlein@informatik.uni-erlangen.de>
 * @author Georg Dotzler<georg.dotzler@informatik.uni-erlangen.de>
 */
public class MatrixScreen implements Screen{

	/**
	 * @uml.property  name="array" multiplicity="(0 -1)" dimension="2"
	 */
	public final Character[][] array;
	/**
	 * @uml.property  name="counter"
	 */
	private int counter;
	/* final *//**
	 * @uml.property  name="repaint"
	 */
	Runnable repaint;

	/**
	 * Constructor for Matrix (Exercise 3)
	 * 
	 * @param x
	 *            Number of colored areas in x-direction
	 * @param y
	 *            Number of colored areas in y-direction
	 */
	public MatrixScreen(final int x, final int y) {
		this(x, y, 20, "Matrix");
	}

	/**
	 * Constructor for a flexible pixel field
	 * 
	 * @param x
	 *            Number of colored areas in x-direction
	 * @param y
	 *            Number of colored areas in y-direction
	 * @param pSize
	 *            Side length of a colored area in pixels
	 * @param title
	 *            Window title
	 */
	public MatrixScreen(final int x, final int y, final double pSize,
			final String title) {
		this(x, y, pSize, title, new Character[x][y], x * y);
	}

	/** Constructor for window creation */
	private MatrixScreen(final int x, final int y, final double pSize,
			final String title, final Character[][] array, 
			final int counter) {
		this.array = array;
		this.counter = counter;
		try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					// create window
					final JFrame mainFrame = new JFrame();

					// create panel that displays the array as colored areas
					final JPanel colorPanel = new JPanel() {
						private static final long serialVersionUID = 1L;

						@Override
						protected void paintComponent(Graphics g) {
							super.paintComponent(g);

							final double xFac = ((double) getSize().width) / x;
							final double yFac = ((double) getSize().height) / y;
							g.setFont(new Font("Serif", Font.PLAIN, 20));
							for (int i = 0; i < x; i++) {
								for (int j = 0; j < y; j++) {
									boolean unset = true;
									synchronized (array) {
										unset = (array[i][j] == null);
										g.setColor(unset ? Color.WHITE
												: Color.BLACK);
									}
									if (!unset) {
										g.setColor(Color.BLACK);
										g.fillPolygon(new int[] { (int) (i * xFac),
										(int) (i * xFac),
										(int) ((i + 1) * xFac),
										(int) ((i + 1) * xFac) },
										new int[] { (int) (j * yFac),
												(int) ((j + 1) * yFac),
												(int) ((j + 1) * yFac),
												(int) (j * yFac) }, 4);
										char[] tmp = {array[i][j]};
										g.setColor(Color.GREEN);
										g.drawChars(tmp, 0, 1, (int)(i * xFac), (int)((j+1) * yFac));
									
									} else {
										g.setColor(Color.WHITE);
										g.fillPolygon(new int[] { (int) (i * xFac),
										(int) (i * xFac),
										(int) ((i + 1) * xFac),
										(int) ((i + 1) * xFac) },
										new int[] { (int) (j * yFac),
												(int) ((j + 1) * yFac),
												(int) ((j + 1) * yFac),
												(int) (j * yFac) }, 4);
									}
								}
							}
						}
					};
					colorPanel.setPreferredSize(new Dimension(
							(int) (pSize * x), (int) (pSize * y)));
					mainFrame.setContentPane(colorPanel);
					mainFrame.setTitle(title);
					mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

					// display window
					mainFrame.pack();
					mainFrame.setLocationRelativeTo(null);
					mainFrame.setCursor(Cursor
							.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					mainFrame.setVisible(true);

					// repaint window 
					repaint = new Runnable() {
						public void run() {
							mainFrame.repaint();
						}
					};
				}
			});
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the point(x,y) to the specified character
	 * 
	 * @param x
	 * @param y
	 * @param character
	 */
	 public synchronized void setChar(final int x, final int y, final char character) {
		// for frequent thread changes
		Thread.yield();
		synchronized (array) {
			array[x][y] = character;
			counter--;
		}
		javax.swing.SwingUtilities.invokeLater(repaint);
	}

	/**
	 * @return <code>true</code>, if the number of colorize calls is equal or greater as the number of points
	 */
	public boolean finished() {
		synchronized (array) {
			return counter <= 0;
		}
	}

	/**
	 * checks, whether a point has an assigned character or not
	 * 
	 * @param x
	 * @param y
	 * @return <code>true</code>, if the the point (x,y) already has a color
	 */
	public boolean hasChar(final int x, final int y) {
		synchronized (array) {
			return array[x][y] != null;
		}
	}

	/**
	 * @return Number of points in x-direction
	 */
	public int getX() {
		synchronized (array) {
			return array.length;
		}
	}

	/**
	 * @return Number of points in y-direction
	 */
	public int getY() {
		synchronized (array) {
			return array.length > 0 ? array[0].length : 0;
		}
	}

	/**
	 * To display the pixel image in a separate window 
	 * 
	 * @param array
	 *            The pixels of the window
	 * @param title
	 *            Title of the new window
	 */
	public static void show(final Character[][] array, final String title) {
		new MatrixScreen(array.length, array[0].length, 1, title, array, 0);
	}

}
