import java.util.LinkedList;

/**
 * 
 * Strategy used by the snakes to survive and grow
 * 
 * @author Georg Dotzler<georg.dotzler@cs.fau.de>
 *
 */
public interface Strategy {
	
	/**
	 * Returns the next attempted move of the snake
	 * 
	 * @param possibleFields all unoccupied fields that are possible destinations
	 * @param board the complete board of the game
	 * @return
	 */
	public Field decideField(LinkedList<Field> possibleFields, Board board);
}
