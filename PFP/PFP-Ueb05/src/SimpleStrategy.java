import java.util.LinkedList;

public class SimpleStrategy implements Strategy {

	
	/**
	 * Pretty stupid strategy that tries all directions in the order left, right, top, bottom
	 */
	@Override
	public Field decideField(LinkedList<Field> possibleFields, Board board) {
		if (possibleFields.size()>0) {
			return possibleFields.getFirst();
		}
		return null;
	}

}
