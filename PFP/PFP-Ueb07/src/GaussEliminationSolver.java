/**
 * Interface with a single method that solves a given system of linear equations using the given number of threads.
 * 
 * @author Andreas Kumlehn <andreas.kumlehn@cs.fau.de>
 *
 */
public interface GaussEliminationSolver {
	
	
	/**
	 * Method that solves a given system of linear equations and returns the unique solution vector using parallel computations.
	 * 
	 * @param A Matrix A of the equation system A*x=b
	 * @param b Right hand vector of the equation system A*x=b
	 * @param numberOfThreads Number of threads to use
	 * @return Solution vector x that solves the equation system A*x=b
	 */
	double[] solveEquations(double[][] A, double[] b, int numberOfThreads);

}
