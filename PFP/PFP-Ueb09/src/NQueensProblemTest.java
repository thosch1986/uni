import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 * @author Georg Dotzler<georg.dotzler@informatik.uni-erlangen.de>
 *
 */
public class NQueensProblemTest {
	
	/**
	 * 1 Queen, 1x1 board, 4 Threads
	 */
	@Test
	public void test1(){
		int N = 1;
		long ref_u = 1;
		long ref_a = 1;
		long start = System.currentTimeMillis();
		NQueensInterface n = new NQueens();
		long res = n.solutions(N, 4);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size"+N+" in "+(stop-start));
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 2 Queens, 2x2 board, 4 Threads
	 */
	@Test
	public void test2(){
		int N = 2;
		long ref_u = 0;
		long ref_a = 0;
		long start = System.currentTimeMillis();
		NQueensInterface n = new NQueens();
		long res = n.solutions(N, 4);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size"+N+" in "+(stop-start));
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 3 Queens, 3x3 board, 4 Threads
	 */
	@Test
	public void test3(){
		int N = 3;
		long ref_u = 0;
		long ref_a = 0;
		long start = System.currentTimeMillis();
		NQueensInterface n = new NQueens();
		long res = n.solutions(N, 4);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size"+N+" in "+(stop-start));
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 4 Queens, 4x4 board, 4 Threads
	 */
	@Test
	public void test4(){
		int N = 4;
		long ref_u = 1;
		long ref_a = 2;
		long start = System.currentTimeMillis();
		NQueensInterface n = new NQueens();
		long res = n.solutions(N, 4);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size"+N+" in "+(stop-start));
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 5 Queens, 5x5 board, 4 Threads
	 */
	@Test
	public void test5(){
		int N = 5;
		long ref_u = 2;
		long ref_a = 10;
		long start = System.currentTimeMillis();
		NQueensInterface n = new NQueens();
		long res = n.solutions(N, 4);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size"+N+" in "+(stop-start));
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 6 Queens, 6x6 board, 4 Threads
	 */
	@Test
	public void test6(){
		int N = 6;
		long ref_u = 1;
		long ref_a = 4;
		long start = System.currentTimeMillis();
		NQueensInterface n = new NQueens();
		long res = n.solutions(N, 4);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size"+N+" in "+(stop-start));
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 7 Queens, 7x7 board, 4 Threads
	 */
	@Test
	public void test7(){
		int N = 7;
		long ref_u = 6;
		long ref_a = 40;
		long start = System.currentTimeMillis();
		NQueensInterface n = new NQueens();
		long res = n.solutions(N, 4);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size"+N+" in "+(stop-start));
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 8 Queens, 8x8 board, 4 Threads
	 */
	@Test
	public void test8(){
		int N = 8;
		long ref_u = 12;
		long ref_a = 92;
		long start = System.currentTimeMillis();
		NQueensInterface n = new NQueens();
		long res = n.solutions(N, 4);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size"+N+" in "+(stop-start));
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 9 Queens, 9x9 board, 4 Threads
	 */
	@Test
	public void test9(){
		int N = 9;
		long ref_u = 46;
		long ref_a = 352;
		long start = System.currentTimeMillis();
		NQueensInterface n = new NQueens();
		long res = n.solutions(N, 4);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size"+N+" in "+(stop-start));
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 10 Queens, 10x10 board, 4 Threads
	 */
	@Test
	public void test10(){
		int N = 10;
		long ref_u = 92;
		long ref_a = 724;
		long start = System.currentTimeMillis();
		NQueensInterface n = new NQueens();
		long res = n.solutions(N, 4);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size"+N+" in "+(stop-start));
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 13 Queens, 13x13 board, 1 Thread
	 */
	@Test
	public void test13(){
		int N = 13;
		long ref_u = 9233;
		long ref_a = 73712;
		long start = System.currentTimeMillis();
		NQueensInterface n = new NQueens();
		long res = n.solutions(N, 1);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size"+N+" in "+(stop-start));
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 13 Queens, 13x13 board, 4 Threads
	 */
	@Test
	public void test13Par(){
		int N = 13;
		long ref_u = 9233;
		long ref_a = 73712;
		long start = System.currentTimeMillis();
		NQueens n = new NQueens();
		long res = n.solutions(N, 4);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size"+N+" in "+(stop-start));
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 14 Queens, 14x14 board, 1 Thread
	 */
	@Test
	public void test14Seq(){
		int N = 14;
		long ref_u = 45752;
		long ref_a = 365596;
		long start = System.currentTimeMillis();
		NQueens n = new NQueens();
		long res = n.solutions(N, 1);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size "+N+" in "+(stop-start)+" with "+1+" Thread.");
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 14 Queens, 14x14 board, 2 Threads
	 */
	@Test
	public void test14Par2(){
		int N = 14;
		long ref_u = 45752;
		long ref_a = 365596;
		long start = System.currentTimeMillis();
		NQueens n = new NQueens();
		long res = n.solutions(N, 2);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size "+N+" in "+(stop-start)+" with "+2+" Threads.");
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 14 Queens, 14x14 board, 3 Threads
	 */
	@Test
	public void test14Par3(){
		int N = 14;
		long ref_u = 45752;
		long ref_a = 365596;
		long start = System.currentTimeMillis();
		NQueens n = new NQueens();
		long res = n.solutions(N, 3);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size "+N+" in "+(stop-start)+" with "+3+" Threads.");
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 14 Queens, 14x14 board, 4 Threads
	 */
	@Test
	public void test14Par4(){
		int N = 14;
		long ref_u = 45752;
		long ref_a = 365596;
		long start = System.currentTimeMillis();
		NQueens n = new NQueens();
		long res = n.solutions(N, 4);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size "+N+" in "+(stop-start)+" with "+4+" Threads.");
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 14 Queens, 14x14 board, 5 Threads
	 */
	@Test
	public void test14Par5(){
		int N = 14;
		long ref_u = 45752;
		long ref_a = 365596;
		long start = System.currentTimeMillis();
		NQueens n = new NQueens();
		long res = n.solutions(N, 5);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size "+N+" in "+(stop-start)+" with "+5+" Threads.");
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 14 Queens, 14x14 board, 6 Threads
	 */
	@Test
	public void test14Par6(){
		int N = 14;
		long ref_u = 45752;
		long ref_a = 365596;
		long start = System.currentTimeMillis();
		NQueens n = new NQueens();
		long res = n.solutions(N, 6);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size "+N+" in "+(stop-start)+" with "+6+" Threads.");
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 14 Queens, 14x14 board, 7 Threads
	 */
	@Test
	public void test14Par7(){
		int N = 14;
		long ref_u = 45752;
		long ref_a = 365596;
		long start = System.currentTimeMillis();
		NQueens n = new NQueens();
		long res = n.solutions(N, 7);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size "+N+" in "+(stop-start)+" with "+7+" Threads.");
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 14 Queens, 14x14 board, 8 Threads
	 */
	@Test
	public void test14Par8(){
		int N = 14;
		long ref_u = 45752;
		long ref_a = 365596;
		long start = System.currentTimeMillis();
		NQueens n = new NQueens();
		long res = n.solutions(N, 8);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size "+N+" in "+(stop-start)+" with "+8+" Threads.");
		assertTrue(res==ref_u||res==ref_a);
	}
	
	/**
	 * 14 Queens, 14x14 board, 9 Threads
	 */
	@Test
	public void test14Par9(){
		int N = 14;
		long ref_u = 45752;
		long ref_a = 365596;
		long start = System.currentTimeMillis();
		NQueens n = new NQueens();
		long res = n.solutions(N, 9);
		long stop = System.currentTimeMillis();
		System.err.println("Found "+res+" solutions for size "+N+" in "+(stop-start)+" with "+9+" Threads.");
		assertTrue(res==ref_u||res==ref_a);
	}

	
}
