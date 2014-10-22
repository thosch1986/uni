/**
 * @author Marc Woerlein (Woerlein@informatik.uni-erlangen.de)
 * @author Georg Dotzler<georg.dotzler@informatik.uni-erlangen.de>
 */
public interface ParallelTernarySearch {

	/**
	 * Parallel minimum finder for multiple functions
	 * 
	 * @param f
	 *            the function that are analyzed
	 * @param left
	 *            the left interval values
	 * @param right
	 *            the right interval values
	 * @param nThreads
	 *            number of used threads 
	 * @param rootFinder
	 * 			  responsible to finds the minimum in one function
	 * @return the found minima
	 */
	
	public Double[] findMinimum(Function[] f, double[] left,
			double[] right, int nThreads, TernarySearch rootFinder);

}
