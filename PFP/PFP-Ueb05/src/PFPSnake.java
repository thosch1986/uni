import java.util.LinkedList;

/**
 * A snake in the game
 * @author  Georg Dotzler<georg.dotzler@cs.fau.de>
 */
public class PFPSnake implements Runnable{

	/**
	 * @uml.property  name="session"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private PFPSnakeGame session;
	/**
	 * @uml.property  name="snakeID"
	 */
	private final int snakeID;
	/**
	 * @uml.property  name="body"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="Field"
	 */
	private LinkedList<Field> body = new LinkedList<Field>();
	/**
	 * @uml.property  name="strategy"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Strategy strategy;
	
	public PFPSnake(int snakeID, PFPSnakeGame session, Field head, Strategy strategy) {
		this.snakeID = snakeID;
		this.session = session;
		this.body.add(head);
		this.strategy = strategy;
	}
	
	@Override
	public void run() {
		while (!session.allSnakesDead()){
			//we can only move once in each round
			while (!session.moveAllowed(snakeID)) {
				//moved before
				Thread.yield();
			}
			boolean success = false;
			Field decision = null;
			while (!success) {
				LinkedList<Field> possibleFields = session.getPossibleFields(body);
				if (possibleFields.size() == 0) {
					session.killMe(snakeID);
					return;
				}
				decision = strategy.decideField(possibleFields, session.getBoard());
				success = session.moveTo(decision, snakeID);
			}
			body.addFirst(decision);
			
			//move tail forward if we did not consume a pickup
			if (!session.hasPickup(snakeID)) {
				Field last = body.removeLast();
				session.leave(last);
			}
			
		}
	}
	
	
}
