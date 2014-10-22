

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

/**
 * 
 * @author Andreas Kumlehn <andreas.kumlehn@cs.fau.de>
 * 
 * Several tests for the implementation of the dot product of two arrays.
 *
 */
public class ArrayDotProductTest {
	
	private static long[] generateRandomArray(final int size) {
		Random rand = new Random(System.currentTimeMillis());
		final long[] array = new long[size];
		for (int i = 0; i < size; i++) {
			array[i] = (long) (rand.nextLong() * 1000);
		}
		return array;
	}

	@Test
	public final void testDotProductShort() {
		int threads = 5;
		ArrayDotProductImpl dot = new ArrayDotProductImpl();
		long[] array1 = new long[] {3, 5, 7};
		long[] array2 = new long[] {2, 4, 6};
		assertEquals(dotProductLinear(array1, array2), dot.dotProduct(array1, array2, threads));
	}
	
	@Test
	public final void testDotProductMedium() {
		int threads = 5;
		ArrayDotProductImpl dot = new ArrayDotProductImpl();
		long[] array1 = new long[] { 11, 22, 33, 44, 55};
		long[] array2 = new long[] { 66, 77, 88, 99, 110}; 
		assertEquals(dotProductLinear(array1, array2), dot.dotProduct(array1, array2, threads));
	}
	
	@Test
	public final void testDotProductSingleThread() {
		int threads = 1;
		ArrayDotProductImpl dot = new ArrayDotProductImpl();
		long[] array1 = new long[] { 11, 22, 33, 44, 55};
		long[] array2 = new long[] { 66, 77, 88, 99, 110}; 
		assertEquals(dotProductLinear(array1, array2), dot.dotProduct(array1, array2, threads));
	}
	
	@Test
	public final void testDotProductSeveralThreads() {
		int threads = 10;
		ArrayDotProductImpl dot = new ArrayDotProductImpl();
		long[] array1 = new long[] { 11, 22, 33, 44, 55};
		long[] array2 = new long[] { 66, 77, 88, 99, 110}; 
		assertEquals(dotProductLinear(array1, array2), dot.dotProduct(array1, array2, threads));
	}
	
	@Test
	public final void testDotProduct() {
		int maxThreads = 20;
		int maxSize = 30;
		
		ArrayDotProductImpl dot = new ArrayDotProductImpl();
		
		for (int threads = 1; threads < maxThreads; threads++) {
			for (int j = 1; j < maxSize; j++) {
				long[] array1 = generateRandomArray(j);
				long[] array2 = generateRandomArray(j);
				assertEquals(dotProductLinear(array1, array2), dot.dotProduct(array1, array2, threads));
			}
		}
	}
	
	public static long dotProductLinear(long[] array1, long[] array2) {
		long result = 0;
		for (int i = 0; i < array1.length; i++) {
			result += array1[i] * array2[i];
		}
		return result;
	}

}
