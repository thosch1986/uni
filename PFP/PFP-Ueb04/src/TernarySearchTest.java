

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Small ternary search test
 * 
 * @author Georg Dotzler<georg.dotzler@informatik.uni-erlangen.de>
 */
public class TernarySearchTest {
	/**
	 * @uml.property  name="ePSILON"
	 */
	final double EPSILON = 0.000001;

	/**
	 * @uml.property  name="polynom" multiplicity="(0 -1)" dimension="2"
	 */
	double[][] polynom = {{0,1},{0,0,1},{0,-1,0.5},{42,-2,-0.5,1.0/3.0},{128,-30,-0.5,2,0.25},{128,-30,-0.5,2,0.25}};
	/**
	 * @uml.property  name="solution" multiplicity="(0 -1)" dimension="1"
	 */
	double[] solution = {-1,0,1,2,2,-5};
	/**
	 * @uml.property  name="bordersLeft" multiplicity="(0 -1)" dimension="1"
	 */
	double[] bordersLeft = {-1,-1,-5,-1,-1,-10};
	/**
	 * @uml.property  name="bordersRight" multiplicity="(0 -1)" dimension="1"
	 */
	double[] bordersRight = {1, 1, 1, 5, 4, -4};

	Function[] getFunctions(){
		Function[] list = new Function[polynom.length];
		for (int i = 0; i < polynom.length; i++){
			list[i] = new Polynomial(polynom[i]);
		}
		return list;
	}


	@Test
	public void testTernarySearch() {
		TernarySearch b = new TernarySearchImpl();
		Function[] f = getFunctions();
		for (int i = 0; i < polynom.length;i++){
			Double result = b.findMinimum(f[i], bordersLeft[i], bordersRight[i]);
			System.err.println("TernarySearch "+i+": "+i+" solutionSequential: "+solution[i]+" result: "+result);
			assertTrue(result!=null);
			assertEquals(solution[i], result, EPSILON);
		}
	}

	@Test
	public void testThreadedSearch() {
		TernarySearch ts = new TernarySearchImpl();
		ParallelTernarySearch b = new ThreadedTernarySearch();
		Function[] f = getFunctions();
		Double[] result = b.findMinimum(f, bordersLeft, bordersRight,4, ts);
		for (int j = 0; j < f.length; j++) {
			System.err.println("ThreadedTernarySearch "+j+": "+j+" solution: "+solution[j]+" result: "+result[j]);
			assertTrue(result[j]!=null);
			assertEquals(solution[j], result[j], EPSILON);
		}
	}

	@Test
	public void testTaskedSearch() {
		TernarySearch ts = new TernarySearchImpl();
		ParallelTernarySearch b = new TaskedTernarySearch();
		Function[] f = getFunctions();
		Double[] result = b.findMinimum(f, bordersLeft, bordersRight,4, ts);
		for (int j = 0; j < f.length; j++) {
			System.err.println("TaskedTernarySerach "+j+": "+j+" solution: "+solution[j]+" result: "+result[j]);
			assertTrue(result[j]!=null);
			assertEquals(solution[j], result[j], EPSILON);
		}
	}



}