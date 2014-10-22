/**
 * created 09.04.2008
 */
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Marc Woerlein <woerlein@informatik.uni-erlangen.de>
 * @author Silvia Schreier <sisaschr@stud.informatik.uni-erlangen.de>
 * @author Andreas Kumlehn <andreas.kumlehn@cs.fau.de>
 */
public class Game {

	/**
	 * @uml.property  name="numberOfCookies"
	 */
	private Integer numberOfCookies;

	/**
	 * @uml.property  name="players"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private Player[] players;

	/**
	 * @uml.property  name="playersToCookies"
	 * @uml.associationEnd  qualifier="name:java.lang.String java.lang.Integer"
	 */
	private Map<String, Integer> playersToCookies;

	/**
	 * @uml.property  name="mainFrame"
	 * @uml.associationEnd  
	 */
	private JFrame mainFrame;

	/**
	 * @uml.property  name="cookieField"
	 * @uml.associationEnd  
	 */
	private JTextField cookieField;

	public Game(final String[] names, int numberOfCookies) {
		// initialize properties
		players = new Player[names.length];
		playersToCookies = new HashMap<String, Integer>(names.length);
		this.numberOfCookies = numberOfCookies;

		try {
			synchronized (playersToCookies) {
				javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						final JFrame mainFrame = new JFrame();
						Game.this.mainFrame = mainFrame;
						final JPanel mainPanel = new JPanel();
						mainPanel.setLayout(new BoxLayout(mainPanel,
								BoxLayout.PAGE_AXIS));
						mainFrame.setContentPane(mainPanel);
						mainFrame.setTitle("GrabTheCookies");
						mainFrame
								.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

						// add first row to show remaining cookies
						JPanel cookiePanel = new JPanel();
						Game.this.cookieField = new JTextField();
						cookieField.setEditable(false);
						cookieField.setText(String
								.valueOf(Game.this.numberOfCookies));
						
						final JLabel label = new JLabel("Remaining: ");
						label.setLabelFor(cookieField);

						cookiePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
						cookiePanel.add(label);
						cookiePanel.add(cookieField);
						mainPanel.add(cookiePanel);

						// add rows displaying the players
						for (int i = 0; i < names.length; i++) {
							players[i] = new Player(Game.this, names[i]);
							playersToCookies.put(names[i], 0);
							mainPanel.add(players[i].getPanel());
						}

						// display the whole frame
						mainFrame.pack();
						mainFrame.setLocationRelativeTo(null);
						mainFrame.setCursor(Cursor
								.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						mainFrame.setVisible(true);
					}
				});
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	public Player[] getPlayers() {
		synchronized (players) {
			return players;
		}
	}

	public boolean grabCookie(String name) {
		synchronized (this.numberOfCookies) {
			if (!this.isFinished()) {
				this.numberOfCookies--;
				// System.out.println("Remaining cookies: " +
				// this.numberOfCookies);
				cookieField.setText(numberOfCookies.toString());

				int previous = this.playersToCookies.get(name);
				this.playersToCookies.put(name, previous + 1);

				if (this.numberOfCookies == 0) {
					//try to determine the winner
					int maxCookies = Collections.max(this.playersToCookies
							.values());
					List<String> winners = new LinkedList<String>();
					for (Entry<String, Integer> entry : this.playersToCookies
							.entrySet()) {
						if (entry.getValue() == maxCookies) {
							winners.add(entry.getKey());
						}
					}
					
					//display winner or add cookies
					if (winners.size() == 1) {
						// System.out.println(winners.get(0) +
						// " is the winner with " + maxCookies);
						JOptionPane.showMessageDialog(mainFrame, winners.get(0)
								+ " is the winner with " + maxCookies);
					} else {
						System.out
								.println("We have more than one winner! Over time with extra cookies!");
						this.numberOfCookies = 50;
					}
				}

				cookieField.revalidate();
				mainFrame.validate();
				return true;
			}
			return false;
		}
	}

	public synchronized boolean isFinished() {
		return this.numberOfCookies == 0;
	}
}
