import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A Window that draws a grid of Colors
 * @author  Georg Dotzler<georg.dotzler@cs.fau.de>
 */
public class GridWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final int SIZE = 320;
	/**
	 * @uml.property  name="colorMap"
	 * @uml.associationEnd  qualifier="valueOf:java.lang.Integer java.awt.Color"
	 */
	private Map<Integer, Color> colorMap;
	/**
	 * @uml.property  name="gPanel"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="this$0:GridWindow$GridPanel"
	 */
	public GridPanel gPanel;
	/**
	 * @uml.property  name="jPanel"
	 * @uml.associationEnd  readOnly="true"
	 */
	public JPanel jPanel;

	public GridWindow(String title, Board board, int threads) {
		Insets insets = frameAndCaptionSizes();

		this.setTitle(title);
		this.setSize(new Dimension(insets.left + insets.right + SIZE,
				insets.top + insets.bottom + SIZE));
		this.setLayout(null);

		GridPanel gp = new GridPanel(board);
		this.gPanel = gp;
		gp.setBounds(0, 0, SIZE, SIZE);
		gp.setBackground(Color.darkGray);
		this.add(gp);

		attachListenersAndHandlers();

		this.setVisible(true);
		
		//initialize colors for objects and snakes to display later
		colorMap = new HashMap<Integer, Color>();
		colorMap.put(Field.CONFLICT_FIELD,Color.BLACK);
		colorMap.put(Field.PICKUP_FIELD,Color.GREEN);
		colorMap.put(Field.FREE_FIELD,Color.WHITE);
		Random r = new Random(System.currentTimeMillis());
		for (int i = 0; i < threads; i++) {
			int red = r.nextInt(256);
			int blue = r.nextInt(256);
			int green = r.nextInt(256);
			colorMap.put(i, new Color(red,green,blue));
		}
	}
	
	public void paintComponent() {
		gPanel.paintComponent();
	}

	public static Insets frameAndCaptionSizes() {
		JFrame temp = new JFrame();
		temp.pack();
		Insets insets = temp.getInsets();
		temp = null;

		return insets;
	}

	protected void attachListenersAndHandlers() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Represents a very simple grid control. The Control stores a 2D-Array of Color objects (cells). When paintComponent() is called, the Grid will be redrawn.
	 */
	private class GridPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		/**
		 * @uml.property  name="board"
		 * @uml.associationEnd  
		 */
		private Board board;
		private GridPanel(Board board) {
			this.board = board;
		}

		private void drawGridLines(Graphics2D g2d) {
			g2d.setColor(Color.lightGray);

			final float step = (float) SIZE / board.getDimension();
			for (float x = 0; x < SIZE; x += step) {
				g2d.drawLine((int) x, 0, (int) x, SIZE);
			}
			for (float y = 0; y < SIZE; y += step) {
				g2d.drawLine(0, (int) y, SIZE, (int) y);
			}
		}
		
		private void drawCells(Graphics2D g2d) {
			final float step = (float) SIZE / board.getDimension();
			float xp = 0;
			for (int x = 0; x < board.getDimension(); x++) {
				float yp = 0;

				for (int y = 0; y < board.getDimension(); y++) {
					g2d.setColor(colorMap.get(board.getField(x, y).getCurrentObject()));
					g2d.fillRect((int) xp + 1, (int) yp + 1, (int) step - 1,
							(int) step - 1);
					
					if (board.getField(x, y).getCurrentObject()==Field.CONFLICT_FIELD) {
						g2d.setColor(Color.GREEN);
						char[] ch = {'x'};
						g2d.drawChars(ch, 0, 1, (int)(xp+step/4), (int)(yp + step-1));
					}
					yp += step;
				}

				xp += step;
			}
		}

		public void paintComponent() {
			Graphics2D g2d = (Graphics2D) this.getGraphics();

			drawCells(g2d);
			drawGridLines(g2d);
		}
	}
}
