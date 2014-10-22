/**
 * created 30.03.2008
 */
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Marc Woerlein<woerlein@informatik.uni-erlangen.de>
 * @author Silvia Schreier<sisaschr@stud.informatik.uni-erlangen.de>
 * @author Andreas Kumleh<andreas.kumlehn@cs.fau.de>
 */
public class Player {

	/**
	 * @uml.property  name="runnable"
	 */
	private final Runnable runnable;
	/**
	 * @uml.property  name="field"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	final JTextField field;
	/**
	 * @uml.property  name="panel"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private final JPanel panel;
	/**
	 * @uml.property  name="finished"
	 */
	private boolean finished;
	/**
	 * @uml.property  name="counter"
	 */
	private Integer counter;

	Player(final Game game, final String name) {
		//initialize properties
		this.panel = new JPanel();
		this.counter = 0;
		this.field = new JTextField();
		
		//layout and fill the textfield
		this.field.setEditable(false);
		this.field.setText(this.counter < 10 ? "0"+String.valueOf(this.counter) : String.valueOf(this.counter));
		
		final JLabel label = new JLabel(name + ": ");
		label.setLabelFor(this.field);

		this.panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.panel.add(label);
		this.panel.add(this.field);

		//create runnable to grab cookies
		this.runnable = new Runnable() {
			public void run() {
				if (!finished && game.grabCookie(name)) {
					counter++;
					field.setText(counter < 10 ? "0"+String.valueOf(counter) : String.valueOf(counter));
					field.revalidate();
					//System.out.println(name + " got " + counter + " cookies!");
				}
				else {
					finished = true;
				}
			}
		};
	}

	/**
	 * @return
	 * @uml.property  name="panel"
	 */
	JPanel getPanel() {
		return panel;
	}

	public void grab() {
		try {
			javax.swing.SwingUtilities.invokeLater(runnable);
			Thread.sleep((int) (2000 * Math.random()));
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}
}