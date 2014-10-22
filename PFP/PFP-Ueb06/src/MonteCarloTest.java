
import static org.junit.Assert.*;
import java.awt.Rectangle;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * Simple test suite for the Monte Carlo Intersection
 * @author Georg Dotzler<georg.dotzler@cs.fau.de>
 *
 */
public class MonteCarloTest {

	private MonteCarlo m;
	public static final int MAX_THREADS = 10;
	public static final int THREADS = 8;
	public static final double EPSILON = 0.01;

	@Before
	public void before() {
		m = new MonteCarloImpl();
	}

	@Test
	public void testIntersection1() {
		Rectangle A = new Rectangle(1, 1, 2, 2);
		Rectangle B = new Rectangle(2, 2, 2, 2);
		double r = m.computeIntersection(A, B, THREADS,
				(long)Integer.MAX_VALUE / 200);
		assertEquals(1.0, r, EPSILON);
	}

	@Test
	public void testIntersection2() {
		Rectangle A = new Rectangle(1, 1, 3, 3);
		Rectangle B = new Rectangle(2, 2, 1, 1);
		double r = m.computeIntersection(A, B, THREADS,
				(long)Integer.MAX_VALUE / 200);
		assertEquals(1.0, r, EPSILON);
	}

	@Test
	public void testIntersection3() {
		Rectangle A = new Rectangle(1, 1, 2, 2);
		Rectangle B = new Rectangle(2, 1, 5, 2);
		double r = m.computeIntersection(A, B, THREADS,
				(long)Integer.MAX_VALUE / 200);
		assertEquals(2.0, r, EPSILON);
	}

	@Test
	public void testIntersection4() {
		Rectangle A = new Rectangle(1, 1, 1, 1);
		Rectangle B = new Rectangle(2, 1, 1, 1);
		double r = m.computeIntersection(A, B, THREADS,
				(long)Integer.MAX_VALUE / 200);
		assertEquals(0.0, r, EPSILON);
	}

	@Test
	public void testMultipleThreads() {
		Rectangle A = new Rectangle(1, 1, 2, 2);
		Rectangle B = new Rectangle(2, 2, 2, 2);
		for (int i = 1; i < MAX_THREADS; i++) {
			double r = m.computeIntersection(A, B, i,
					(long)Integer.MAX_VALUE / 200);
			assertEquals(1.0, r, EPSILON);
		}
	}



}

