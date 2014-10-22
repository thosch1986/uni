import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * A snake game with automatically controlled independent snakes
 * @author  Georg Dotzler<georg.dotzler@cs.fau.de>
 */
public class PFPSnakeGameImpl implements PFPSnakeGame{

	private final static int SPEED = 100;
	private final static int SNAKES = 10; // 100
	private final static int FIELD_DIMENSION = 20;
	
	/**
	 * @uml.property  name="numberOfSnakes"
	 */
	private final int numberOfSnakes;
	
	/**
	 * @uml.property  name="gridWindow"
	 * @uml.associationEnd  readOnly="true"
	 */
	private GridWindow gridWindow;
	/**
	 * @uml.property  name="currentSnakes"
	 */
	private AtomicInteger currentSnakes; 
	/**
	 * @uml.property  name="snakeMoved"
	 */
	private AtomicIntegerArray snakeMoved;
	/**
	 * @uml.property  name="board"
	 * @uml.associationEnd  readOnly="true"
	 */
	private Board board;

	/**
	 * @uml.property  name="getPickup"
	 */
	private boolean getPickup[];
	/**
	 * @uml.property  name="killed"
	 */
	private boolean killed[];

	public PFPSnakeGameImpl(int numberOfSnakes, int dimension) {
		this.numberOfSnakes = numberOfSnakes;
		this.board= new Board(dimension);
		this.gridWindow = new GridWindow("PFP - Snake", board, numberOfSnakes);
		this.currentSnakes = new AtomicInteger(numberOfSnakes);
		this.snakeMoved = new AtomicIntegerArray(numberOfSnakes);
		this.getPickup = new boolean[numberOfSnakes];
		this.killed = new boolean[numberOfSnakes];

		for (int i = 0; i < numberOfSnakes; i++) {
			snakeMoved.set(i, 0);
		}
		for (int i = 0; i < numberOfSnakes/2; i++) {
			board.putAtRandomPosition(Field.PICKUP_FIELD);
		}

	}

	
	public Board getBoard() {
		return board;
	}
	
	/**
	 * Creates a runnable for each snake and executes each runnable in
	 * an extra thread.
	 */
	public void init() {
				
		Thread[] thread = new Thread[numberOfSnakes];
		//Runnable[] run = new Runnable[numberOfSnakes];
		
		for (int i=0; i < numberOfSnakes; i++) {
			thread[i] = new Thread(new PFPSnake(i, this, board.putAtRandomPosition(i), new SimpleStrategy()));
			thread[i].start();
		}

	}

	/**
	 * Kills a snake if there is no possible way out
	 * @param snakeID
	 */
	public void killMe(int snakeID) {
		killed[snakeID] = true;
		currentSnakes.decrementAndGet();
		snakeMoved.set(snakeID,1);
		System.err.println("Snake "+snakeID+" is dead!");
	}

	/**
	 * Starts a new round in which each snake can move to a new location.
	 * At the end of the round the consumed pickups are replaced.
	 */
	public void nextRound() {
		gridWindow.paintComponent();

		//check if all snakes moved in this round
		boolean finished = false;
		while (!finished) {
			finished = true;
			for (int i = 0; i < snakeMoved.length(); i++) {
				finished &= (snakeMoved.get(i) == 1);
			}
			Thread.yield();
		}

		//all snakes moved, replace consumed pickups
		synchronized(getPickup) {
			for (int i = 0; i < getPickup.length; i++) {
				if (getPickup[i]) {
					board.putAtRandomPosition(Field.PICKUP_FIELD);
				}

			}
		}

		//reset all data for the next round
		for (int i = 0; i < snakeMoved.length(); i++) {
			if (!killed[i]) {
				snakeMoved.set(i, 0);
			}
			getPickup[i] = false;
		}
	}

	/**
	 * Returns true if all snakes are dead and the game is finished.
	 * @return
	 */
	public boolean allSnakesDead() {
		return currentSnakes.get() == 0;
	}

	public boolean moveAllowed(int snakeID) {
		return (snakeMoved.get(snakeID)==0);
	}

	/**
	 * Returns all possible destinations for the head of the snake.
	 * A Snake of size 1 may turn in all four directions.
	 * @param body The fields in which the snake body parts lie
	 * @return
	 */
	public LinkedList<Field> getPossibleFields(LinkedList<Field> body) {
		LinkedList<Field> result = new LinkedList<Field>();
		LinkedList<Field> neighbours = board.getNeighbors(body.getFirst());
		if (body.size()!=1) {
			neighbours.remove(body.get(1));
		}
		for (Field f: neighbours) {
			if (f.getCurrentObject()==Field.FREE_FIELD || f.getCurrentObject()==Field.PICKUP_FIELD) {
				result.add(f);
			}
		}
		return result;
	}


	/**
	 * Moves the snake to the new field.
	 * @param field the destination of the snake head
	 * @param snakeID
	 * @return
	 */
	public boolean moveTo(Field field, int snakeID) {
		
		if (moveAllowed(snakeID) == true) {
			// getPossibleField();
		}
		snakeMoved.set(snakeID,1);
		return true;
	} else {
		return false;
	}
	
	/**
	 * Returns true if the snake has snatched a pickup in this round
	 * @param snakeID
	 * @return
	 */
	public boolean hasPickup(int snakeID) {
		return getPickup[snakeID];
	}

	/**
	 * Removes the tail of the snake from the field.
	 * @param field
	 */
	public void leave(Field field) {
		field.clear();
	}
	
	private void runLoop() {	
		while (true) {
			if (allSnakesDead()) {
				break;
			}
			nextRound();
			try {
				Thread.sleep(SPEED);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void startGame() {
		init();
		runLoop();
		System.out.println("Your Snakes died.");
	}
	
	public static void main(String[] args) {
		PFPSnakeGameImpl game = new PFPSnakeGameImpl(SNAKES,FIELD_DIMENSION);
		game.startGame();
		
	}

}
