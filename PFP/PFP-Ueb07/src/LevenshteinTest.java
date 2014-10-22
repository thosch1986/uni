import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * Simple test suite for the Levenshtein distance
 * Examples taken from 
 * http://de.wikipedia.org/wiki/Levenshtein-Distanz
 * http://en.wikipedia.org/wiki/Levenshtein_distance
 * 
 * @author Georg Dotzler<georg.dotzler@cs.fau.de>
 *
 */

public class LevenshteinTest {
	
	private static void print(int[][] table) {
		for (int[] line : table) {
			System.out.println(Arrays.toString(line));
		}
	}


	private Levenshtein l;
	public static final int MAX_THREADS = 10;
	public static final int THREADS = 8;

	@Before
	public void before() {
		l = new LevenshteinImpl();
	}


	@Test
	public void testComputeValue() {
		int[][] table = {{0, 1, 2, 3},{1, 0, 1, 2},{2, 1, 1, 2},{3, 2, 2, 2},{4, 3, 3, 2}};
		char[] wordVertical = "Tor".toCharArray();
		char[] wordHorizontal = "Tier".toCharArray();
		for (int i = 1; i < table.length; i++) {
			for (int j = 1; j < table[0].length; j++) {
				assertEquals(table[i][j],l.computeValue(i, j, table, wordHorizontal, wordVertical));
			}
		}
	}
	
	
	@Test
	public void testTorSeq() {
		int[][] solution = l.computeLevenshteinSeq("Tor".toCharArray(),"Tier".toCharArray());
		assertTrue(solution!=null);
		assertTrue(solution[0]!=null);
		assertEquals(solution[solution.length-1][solution[0].length-1],2);
		//print(solution);
	}

	@Test
	public void testKittenSeq() {
		int[][] solution = l.computeLevenshteinSeq("kitten".toCharArray(),"sitting".toCharArray());
		assertTrue(solution!=null);
		assertTrue(solution[0]!=null);
		assertEquals(solution[solution.length-1][solution[0].length-1],3);
		//print(solution);
	}

	@Test
	public void testSaturdaySeq() {
		int[][] solution = l.computeLevenshteinSeq("Saturday".toCharArray(),"Sunday".toCharArray());
		assertTrue(solution!=null);
		assertTrue(solution[0]!=null);
		assertEquals(solution[solution.length-1][solution[0].length-1],3);
		//print(solution);
	}

	@Test
	public void testHabitSeq() {
		int[][] solution = l.computeLevenshteinSeq("HABIT".toCharArray(),"HOBBIT".toCharArray());
		assertTrue(solution!=null);
		assertTrue(solution[0]!=null);
		assertEquals(solution[solution.length-1][solution[0].length-1],2);
		//print(solution);
	}
	
	@Test
	public void testTorQueue() {
		int[][] solution = l.computeLevenshteinQueue("Tor".toCharArray(),"Tier".toCharArray(),4);
		assertTrue(solution!=null);
		assertTrue(solution[0]!=null);
		assertEquals(solution[solution.length-1][solution[0].length-1],2);
		//print(solution);
	}

	@Test
	public void testKittenQueue() {
		int[][] solution = l.computeLevenshteinQueue("kitten".toCharArray(),"sitting".toCharArray(),4);
		assertTrue(solution!=null);
		assertTrue(solution[0]!=null);
		assertEquals(solution[solution.length-1][solution[0].length-1],3);
		//print(solution);
	}

	@Test
	public void testSaturdayQueue() {
		int[][] solution = l.computeLevenshteinQueue("Saturday".toCharArray(),"Sunday".toCharArray(),4);
		assertTrue(solution!=null);
		assertTrue(solution[0]!=null);
		assertEquals(solution[solution.length-1][solution[0].length-1],3);
		//print(solution);
	}

	@Test
	public void testHabitQueue() {
		int[][] solution = l.computeLevenshteinQueue("HABIT".toCharArray(),"HOBBIT".toCharArray(),4);
		assertTrue(solution!=null);
		assertTrue(solution[0]!=null);
		assertEquals(solution[solution.length-1][solution[0].length-1],2);
		//print(solution);
	}
	
	@Test
	public void testTorExchanger() {
		int[][] solution = l.computeLevenshteinExchanger("Tor".toCharArray(),"Tier".toCharArray(),4);
		assertTrue(solution!=null);
		assertTrue(solution[0]!=null);
		assertEquals(solution[solution.length-1][solution[0].length-1],2);
		//print(solution);
	}

	@Test
	public void testKittenExchanger() {
		int[][] solution = l.computeLevenshteinExchanger("kitten".toCharArray(),"sitting".toCharArray(),4);
		assertTrue(solution!=null);
		assertTrue(solution[0]!=null);
		assertEquals(solution[solution.length-1][solution[0].length-1],3);
		//print(solution);
	}

	@Test
	public void testSaturdayExchanger() {
		int[][] solution = l.computeLevenshteinExchanger("Saturday".toCharArray(),"Sunday".toCharArray(),4);
		assertTrue(solution!=null);
		assertTrue(solution[0]!=null);
		assertEquals(solution[solution.length-1][solution[0].length-1],3);
		//print(solution);
	}

	@Test
	public void testHabitExchanger() {
		int[][] solution = l.computeLevenshteinExchanger("HABIT".toCharArray(),"HOBBIT".toCharArray(),4);
		assertTrue(solution!=null);
		assertTrue(solution[0]!=null);
		assertEquals(solution[solution.length-1][solution[0].length-1],2);
		//print(solution);
	}

	@Test
	public void testMultipleThreadsQueue() {
		for (int i = 1; i < 10; i++){
			System.out.println(i+" Threads:");
			int[][] solution = l.computeLevenshteinQueue("HABIT".toCharArray(),"HOBBIT".toCharArray(),i);
			assertTrue(solution!=null);
			assertTrue(solution[0]!=null);
			assertEquals(solution[solution.length-1][solution[0].length-1],2);
			//print(solution);
		}
	}
	
	@Test
	public void testMultipleThreadsExchanger() {
		for (int i = 1; i < 10; i++){
			System.out.println(i+" Threads:");
			int[][] solution = l.computeLevenshteinExchanger("HABIT".toCharArray(),"HOBBIT".toCharArray(),i);
			assertTrue(solution!=null);
			assertTrue(solution[0]!=null);
			assertEquals(solution[solution.length-1][solution[0].length-1],2);
			//print(solution);
		}
	}

} 

