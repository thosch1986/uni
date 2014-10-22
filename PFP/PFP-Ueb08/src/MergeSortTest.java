import java.util.Arrays;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

public class MergeSortTest {

	@Test
	public void testSortSeq() {
		Random r = new Random(System.currentTimeMillis());

		int len = 100;

		for (int i = 0; i <= len; i++) {
			int mergeSorted[] = new int[i];
			int javaSorted[] = new int[i];

			for (int s = 0; s < i; s++)
				mergeSorted[s] = javaSorted[s] = r.nextInt(10);

			Arrays.sort(javaSorted);

			MergeSort sorter = new MergeSortImpl();
			sorter.sort(mergeSorted,1);

			boolean ok = Arrays.equals(javaSorted, mergeSorted);
			if (!ok) {
				System.out.println("Expected: " + Arrays.toString(javaSorted));
				System.out.println("Actual:   " + Arrays.toString(mergeSorted));
			}
			Assert.assertTrue(ok);
		}
	}

	@Test
	public void testSortPar() {
		Random r = new Random(System.currentTimeMillis());

		int len = 100;

		for (int i = 0; i <= len; i++) {
			int mergeSorted[] = new int[i];
			int javaSorted[] = new int[i];

			for (int s = 0; s < i; s++)
				mergeSorted[s] = javaSorted[s] = r.nextInt(10);

			Arrays.sort(javaSorted);

			MergeSort sorter = new MergeSortImpl();
			sorter.sort(mergeSorted,4);

			boolean ok = Arrays.equals(javaSorted, mergeSorted);
			if (!ok) {
				System.out.println("Expected: " + Arrays.toString(javaSorted));
				System.out.println("Actual:   " + Arrays.toString(mergeSorted));
			}
			Assert.assertTrue(ok);
		}
	}
	
	@Test
	public void testSortLarge() {
		Random r = new Random(System.currentTimeMillis());

		int len = Integer.MAX_VALUE/50;

		for (int i = 1; i <= 8; i++) {
			int mergeSorted[] = new int[len];
			int javaSorted[] = new int[len];

			for (int s = 0; s < len; s++)
				mergeSorted[s] = javaSorted[s] = r.nextInt(len);
			long start = System.currentTimeMillis();
			
			Arrays.sort(javaSorted);
			System.out.println("Java array sort time: "+(System.currentTimeMillis()-start)/1000.0);
			start = System.currentTimeMillis();
			MergeSort sorter = new MergeSortImpl();
			sorter.sort(mergeSorted,i);
			System.out.println("Own time: "+(System.currentTimeMillis()-start)/1000.0);
			boolean ok = Arrays.equals(javaSorted, mergeSorted);
			if (!ok) {
				System.out.println("Expected: " + Arrays.toString(javaSorted));
				System.out.println("Actual:   " + Arrays.toString(mergeSorted));
			}
			Assert.assertTrue(ok);
		}
	}
}
