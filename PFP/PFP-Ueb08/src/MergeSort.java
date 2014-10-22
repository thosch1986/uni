/**
 * 
 * Interface for the parallel merge sort implementation
 * 
 * @author Georg Dotzler<georg.dotzler@cs.fau.de>
 *
 */


public interface MergeSort {
	
	/**
	 * Sorts an array in ascending order
	 * @param array the array to be sorted
	 * @param numberOfThreads maximal number of additional threads to be used
	 */
	public void sort(int array[], int numberOfThreads);
}
