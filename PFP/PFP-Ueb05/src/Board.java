import java.util.LinkedList;

/**
 * Game field with a n x n content grid.
 * @author  Georg Dotzler<georg.dotzler@cs.fau.de>
 */
public class Board {

	/**
	 * @uml.property  name="dimension"
	 */
	private final int dimension;

	/**
	 * @uml.property  name="content"
	 */
	private Field[][] content;

	public Board(int dimension) {
		this.dimension = dimension;
		content = new Field[dimension][dimension];
		for (int x = 0; x < dimension; x++) {
			for (int y = 0; y < dimension; y++) {
				content[x][y] = new Field(x, y);
			}
		}
	}

	/**
	 * @return   The number of fields in each dimension
	 * @uml.property  name="dimension"
	 */
	public int getDimension() {
		return dimension;
	}

	/**
	 * Returns the fields with the coordinates x and y. If the field is outside
	 * of the grid, null is returned.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Field getField(int x, int y) {
		if (x < 0 || y < 0 || x >= dimension || y >= dimension) {
			return null;
		} else {
			return content[x][y];
		}
	}

	/**
	 * Returns a list with the four neighbors (left, right, top, bottom) of a
	 * field
	 * 
	 * @param field
	 * @return
	 */
	public LinkedList<Field> getNeighbors(Field field) {
		LinkedList<Field> result = new LinkedList<Field>();
		Field left = this.getField(field.x - 1, field.y);
		Field right = this.getField(field.x + 1, field.y);
		Field top = this.getField(field.x, field.y + 1);
		Field bottom = this.getField(field.x, field.y - 1);
		if (left != null) {
			result.add(left);
		}
		if (right != null) {
			result.add(right);
		}
		if (top != null) {
			result.add(top);
		}
		if (bottom != null) {
			result.add(bottom);
		}
		return result;

	}
	
	/**
	 * Returns a random free field of the grid
	 * @objectType the object to be placed on the random position
	 * @return 
	 */
	public Field putAtRandomPosition(int objectType) {
		while (true) {
			int x = (int) (Math.random() * getDimension());
			int y = (int) (Math.random() * getDimension());
			synchronized (getField(x, y)) {
				if (getField(x, y).getCurrentObject() == Field.FREE_FIELD) {
					getField(x, y).putObject(objectType);
					return getField(x, y);
				}
			}
		}
	}

}
