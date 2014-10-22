/**
 * Interface with a single method to multiply a matrix with a vector using a given number of threads.
 * 
 * @author Andreas Kumlehn <andreas.kumlehn@cs.fau.de>
 */
public interface ParallelMatrixVectorMultiplier {
	
	
	/**
	 * Method that multiplies a matrix with a vector using the given number of threads for parallel computations.
	 * 
	 * @param A Matrix A
	 * @param x Vector x
	 * @param numberOfThreads Number of threads to use
	 * @return Resulting vector of the multiplication A*x
	 */
	public double[] multiplyMatrixByVector(double[][] A, double[] x, int numberOfThreads);

}
