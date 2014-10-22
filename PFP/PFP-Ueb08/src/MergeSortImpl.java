import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

import javax.print.attribute.standard.NumberOfDocuments;

/**
 * 
 * Parallel merge sort
 * 
 * @author Georg Dotzler<georg.dotzler@cs.fau.de>
 * 
 */
public class MergeSortImpl implements MergeSort {

	/**
	 * Sorts an array in ascending order
	 * 
	 * @param array
	 *            the array to be sorted
	 * @param numberOfThreads
	 *            maximal number of additional threads to be used
	 */
	public void sort(int array[], int numberOfThreads) {
		
		//Thread t1 = new Worker(array, numberOfThreads);
		
		sortInner(array, numberOfThreads);
	}

	public int[] sortInner(int array[], int numberOfThreads) {
		if (array.length > 1) {

			int elementeArray1 = array.length / 2;
			int elementeArray2 = elementeArray1;

			if ((array.length % 2) == 1) {
				elementeArray2 += 1;
			}
			
			int array1[] = new int[elementeArray1];
			int array2[] = new int[elementeArray2];

			for (int i = 0; i < elementeArray1; i++) {
				array1[i] = array[i];
			}

			for (int i = elementeArray1; i < elementeArray1 + elementeArray2; i++) {
				array2[i - elementeArray1] = array[i];
			}
			
			array1 = sortInner(array1, numberOfThreads);
			array2 = sortInner(array2, numberOfThreads);

			// Ab hier beginnt das Mergen
			int i = 0, j = 0, k = 0;

			// solange, bis ein Array leer wird!
			while (array1.length != j && array2.length != k) {
				if (array1[j] < array2[k]) {
					array[i] = array1[j];
					i++;
					j++;
				} else {
					array[i] = array2[k];
					i++;
					k++;
				}
			}

			while (array1.length != j) {
				array[i] = array1[j];
				i++;
				j++;
			}
			while (array2.length != k) {
				array[i] = array2[k];
				i++;
				k++;
			}
		}
		return array;
	}

	public static void main(String args[]) {
		// Testen Sie mit anderen Seed-Werten
		Random r = new Random(System.currentTimeMillis());

		// Testen Sie mit mehr Zahlen
		int numNumbers = Integer.MAX_VALUE / 100;

		// Testen Sie mit groesseren Zahlen
		int maxNumber = 1024;

		int mergeSorted[] = new int[numNumbers];
		int javaSorted[] = new int[numNumbers];
		for (int i = 0; i < numNumbers; i++)
			mergeSorted[i] = javaSorted[i] = r.nextInt(maxNumber);

		MergeSort sorter = new MergeSortImpl();
		sorter.sort(mergeSorted, 4);
		Arrays.sort(javaSorted);

		String ok = "Yeah!";
		if (!Arrays.equals(mergeSorted, javaSorted)) {
			System.out.println(Arrays.toString(mergeSorted));
			System.out.println(Arrays.toString(javaSorted));
			ok = "Argh!";
		}

		System.out.println(ok);
	}

	 /* public Worker implements Runnable {
	
		 int[] array;
		 
		 
		 Worker(int[] array) {
			 this.array = array;
		 }
		 
		 run() {
			 if (array.length > 1) {
				 int elementeArray1 = array.length / 2;
					int elementeArray2 = elementeArray1;

					if ((array.length % 2) == 1) {
						elementeArray2 += 1;
					}
					
					int array1[] = new int[elementeArray1];
					int array2[] = new int[elementeArray2];

					for (int i = 0; i < elementeArray1; i++) {
						array1[i] = array[i];
					}

					for (int i = elementeArray1; i < elementeArray1 + elementeArray2; i++) {
						array2[i - elementeArray1] = array[i];
					}
					
					array1 = sortInner(array1, numberOfThreads);
					array2 = sortInner(array2, numberOfThreads);
			 }
		 }
	 }
	 */
}
