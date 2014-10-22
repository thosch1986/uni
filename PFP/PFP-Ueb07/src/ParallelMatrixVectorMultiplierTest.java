import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test cases for implementations of the interface ParallelVectorMatrixMultiplier.
 * 
 * @author Andreas Kumlehn <andreas.kumlehn@cs.fau.de>
 *
 */
public class ParallelMatrixVectorMultiplierTest {

	/**
	 * Test method for {@link ParallelMatrixVectorMultiplier#multiplyMatrixByVector(double[][], double[], int)}.
	 */
	@Test
	public final void testMultiplyMatrixByVector3x3() {
		double[][] A = { 
				{2, 1, -1},
				{-3, -1, 2},
				{-2, 1, 2}
			};
		double[] b = { 8, -11, -3};
		
		double[] x = {2, 3, -1};
		
		int numberOfThreads = 2;
		ParallelMatrixVectorMultiplier multiplier = new ParallelMatrixVectorMultiplierImpl();
		assertArrayEquals(b, multiplier.multiplyMatrixByVector(A, x, numberOfThreads), 0.001);
	}
	
	@Test
	public final void testMultiplyMatrixByVector5x3() {
		double[][] A = { 
				{2, 1, -1},
				{-3, -1, 2},
				{-2, 1, 2},
				{2, 1, -1},
				{-3, -1, 2}
			};
		double[] b = { 8, -11, -3, 8, -11};
		
		double[] x = {2, 3, -1};
		
		int numberOfThreads = 2;
		ParallelMatrixVectorMultiplier multiplier = new ParallelMatrixVectorMultiplierImpl();
		assertArrayEquals(b, multiplier.multiplyMatrixByVector(A, x, numberOfThreads), 0.001);
	}

}
