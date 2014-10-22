/**
 * A single field in the game grid. Snake bodies or a pickup can be placed on the field. 
 * @author  Georg Dotzler<georg.dotzler@cs.fau.de>
 */
public class Field {
	public static final int FREE_FIELD = -1;
	public static final int PICKUP_FIELD = -2;
	public static final int CONFLICT_FIELD = -3;
	
	/**
	 * @uml.property  name="objectType"
	 */
	private int objectType;
	/**
	 * @uml.property  name="x"
	 */
	public final int x;
	/**
	 * @uml.property  name="y"
	 */
	public final int y;
	
	public Field(int x, int y) {
		this.x = x;
		this.y = y;
		this.clear();
	}
	
	/**
	 * Places a new snake body part on the grid.
	 * If the field is already taken, the objectType is set to CONFLICT_FIELD.
	 * 
	 * @param id the id of the snake.
	 */
	public synchronized void putObject(int id) {
		if (this.objectType == CONFLICT_FIELD) {
			return;
		}
		if (this.objectType!= FREE_FIELD && this.objectType!= PICKUP_FIELD) {
			this.objectType = CONFLICT_FIELD;
			System.err.println("Conflict on field ("+x+", "+y+")!");
			return;
		}
		this.objectType = id;
	}
	
	/**
	 * Returns the id of the object found on the field.
	 * For a snake, the id of the snake is returned.
	 * For a free field, FREE_FIELD is returned
	 * For a pickup, PICKUP_FIELD is returned
	 * @return
	 */
	public int getCurrentObject() {
		return objectType;
	}
	
	/**
	 * Removes the snake body part from the field
	 */
	public synchronized void clear() {
		if (this.objectType!=CONFLICT_FIELD) {
			this.objectType = FREE_FIELD;
		}
	}
}

