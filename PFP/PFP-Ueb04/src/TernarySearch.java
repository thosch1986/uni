/**
 * @author Marc Woerlein (Woerlein@informatik.uni-erlangen.de)
 * @author Georg Dotzler<georg.dotzler@informatik.uni-erlangen.de>
 */
public interface TernarySearch {

	/** Two Numbers with a difference less then EPSILON are considered equal */
	static final double EPSILON = 0.0000000001;

	/**
	 * Finds the minimum of a given function in the interval [left,right]
	 * 
	 * @param f
	 *            the function that is analyzed
	 * @param left
	 *            the left interval value
	 * @param right
	 *            the right interval value
	 * @return the minimum or <code>null</code> with illegal interval positions
	 *         
	 */
	public Double findMinimum(Function f, double left, double right);

}
