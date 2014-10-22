

/**
 * @author Andreas Kumlehn <andreas.kumlehn@cs.fau.de>
 *
 * Interface for the calculation of the dot product of two arrays.
 */
public interface ArrayDotProduct {
	
	/**
	 * Method to calculate the dot product of two arrays with long values.
	 * 
	 * @param array1 First array
	 * @param array2 Second array
	 * @param threads Number of threads
	 * @return The value of the dot product of the two given arrays
	 */
	public long dotProduct(long[] array1, long[] array2, int threads);

}
