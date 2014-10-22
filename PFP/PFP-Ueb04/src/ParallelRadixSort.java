public interface ParallelRadixSort {

	/**
	 * Returns the bucket number for the n-th char, where the most significant char has index 0.
	 * Example: <code>getBucket("gfedcba", 2) == 4</code>.
	 * Example: <code>getBucket("bcdefgh", 2) == 3</code>.
	 * Example: <code>getBucket("a", 2) == 0</code>.
	 */
	public int getBucket(String string, int n);

	/**
	 * Sorts an array of strings by looking at the n-th char in every string.
	 * Again, the most significant char has index 0.
	 * 
	 * @param threads Number of threads to be used
	 */
	public void sortByPos(String array[], int n, int threads);

	/**
	 * Sorts an array of strings using radix-sort with multiple threads.
	 * 
	 * @param array to be sorted
	 * @param maxLength Maximal length of the strings in the array
	 * @param threads Number of threads to be used
	 */
	public void radixSort(String array[], int maxLength, int threads);
	
	
}
