/**
 * created 21.12.2009
 */

/**
 * Interface to the N-Queens Problem
 * 
 * @author Marc Woerlein<woerlein@informatik.uni-erlangen.de>
 * 
 */
public interface NQueensInterface {

	/**
	 * 
	 * Computes the number of different possibilities to arrange
	 * n-Queens on a n*n board with a parallel algorithm
	 * 
	 * The type of parallelization is open to the implementation, only the number of
	 * threads is fixed
	 * 
	 * @param n
	 *          The size of the board (n*n) and the number of queens
	 * @param threads
	 *          Number of threads to be used
	 * @return 
	 * 			Number of distinct or unique solutions of the problem
	 */
	public long solutions(final int n, int threads);

}