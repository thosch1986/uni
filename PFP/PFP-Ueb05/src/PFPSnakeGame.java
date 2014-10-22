import java.util.LinkedList;

/**
 * A snake game with automatically controlled independent snakes
 * @author   Georg Dotzler<georg.dotzler@cs.fau.de>
 */
public interface PFPSnakeGame {

	/**
	 * @uml.property  name="board"
	 * @uml.associationEnd  
	 */
	public Board getBoard();
	
	/**
	 * Creates a runnable for each snake and executes each runnable in
	 * an extra thread.
	 */
	public void init();

	/**
	 * Kills a snake if there is no possible way out
	 * @param snakeID
	 */
	public void killMe(int snakeID);

	/**
	 * Starts a new round in which each snake can move to a new location.
	 * At the end of the round the consumed pickups are replaced.
	 */
	public void nextRound();
	/**
	 * Returns true if all snakes are dead and the game is finished.
	 * @return
	 */
	public boolean allSnakesDead();

	/**
	 * Returns true if the snake with the given snakeID is allowed to
	 * move in the currently running round, otherwise false.
	 * 
	 * Each snake is allowed to move once in each round of the game.
	 * @param snakeID
	 * @return
	 */
	public boolean moveAllowed(int snakeID);

	/**
	 * Returns all possible destinations for the head of the snake.
	 * A Snake of size 1 may turn in all four directions.
	 * @param body The fields in which the snake body parts lie
	 * @return
	 */
	public LinkedList<Field> getPossibleFields(LinkedList<Field> body);


	/**
	 * Moves the snake to the new field.
	 * @param field the destination of the snake head
	 * @param snakeID
	 * @return
	 */
	public boolean moveTo(Field field, int snakeID);
	
	/**
	 * Returns true if the snake has snatched a pickup in this round
	 * @param snakeID
	 * @return
	 */
	public boolean hasPickup(int snakeID);

	/**
	 * Removes the tail of the snake from the field.
	 * @param field
	 */
	public void leave(Field field);
	
	
	
}
