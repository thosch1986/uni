import static org.junit.Assert.*;

import org.junit.Test;

public class GaussEliminationSolverTest {

	@Test
	public final void testSequentialSolveEquations() {
		// example taken from http://en.wikipedia.org/wiki/Gaussian_elimination
		double[][] A = { { 2, 1, -1 }, { -3, -1, 2 }, { -2, 1, 2 } };
		double[] b = { 8, -11, -3 };

		double[] x = { 2, 3, -1 };

		int numberOfThreads = 2;

		GaussEliminationSolver solver = new SequentialGaussSolver();
		double[] sequentialResult = solver
				.solveEquations(A, b, numberOfThreads);
		assertArrayEquals(x, sequentialResult, 0.001);
	}

	@Test
	public final void testParallelSolveEquationsSmall() {
		// example taken from http://en.wikipedia.org/wiki/Gaussian_elimination
		double[][] A = { { 2, 1, -1 }, { -3, -1, 2 }, { -2, 1, 2 } };
		double[] b = { 8, -11, -3 };

		double[] x = { 2, 3, -1 };

		int numberOfThreads = 2;

		GaussEliminationSolver solver = new ParallelGaussSolver();
		double[] sequentialResult = solver
				.solveEquations(A, b, numberOfThreads);
		assertArrayEquals(x, sequentialResult, 0.001);
	}

	@Test
	public final void testParallelSolveEquationsLarge() {
		// example taken from http://en.wikipedia.org/wiki/Gaussian_elimination
		double[][] A = { { 0.29, 0.98, 1.86, 2.84, 3.63, 5.59 },
				{ 2.94, 4.12, 5.88, 8.43, 10.39, 12.35 },
				{ 8.14, 9.71, 12.06, 15.10, 17.84, 20.59 },
				{ 14.90, 18.24, 21.47, 24.71, 27.55, 31.18 },
				{ 24.90, 27.94, 32.45, 36.08, 39.71, 43.92 },
				{ 36.37, 39.61, 43.82, 47.25, 51.37, 55.29 } };

		double[] b = { 9.41, 15.78, 22.75, 29.90, 37.16, 44.61 };

		double[] x = { 0.9194, 3.3751, -9.4757, 7.9921, -5.9785, 4.0188 };

		int numberOfThreads = 2;

		GaussEliminationSolver solver = new ParallelGaussSolver();
		double[] sequentialResult = solver
				.solveEquations(A, b, numberOfThreads); 
		assertArrayEquals(x, sequentialResult, 0.001);
	}
	
	@Test
	public final void testParallelSolveEquationsLargeStress() {
		// example taken from http://en.wikipedia.org/wiki/Gaussian_elimination
		double[][] A = { { 0.29, 0.98, 1.86, 2.84, 3.63, 5.59 },
				{ 2.94, 4.12, 5.88, 8.43, 10.39, 12.35 },
				{ 8.14, 9.71, 12.06, 15.10, 17.84, 20.59 },
				{ 14.90, 18.24, 21.47, 24.71, 27.55, 31.18 },
				{ 24.90, 27.94, 32.45, 36.08, 39.71, 43.92 },
				{ 36.37, 39.61, 43.82, 47.25, 51.37, 55.29 } };		
		
		double[] b = { 9.41, 15.78, 22.75, 29.90, 37.16, 44.61 };

		double[] x = { 0.9194, 3.3751, -9.4757, 7.9921, -5.9785, 4.0188 };

		for (int i = 1; i < 12; i ++) {
			GaussEliminationSolver solver = new ParallelGaussSolver();
			double[] sequentialResult = solver
					.solveEquations(A, b, i); 
			assertArrayEquals(x, sequentialResult, 0.001);
		}
	}

}
