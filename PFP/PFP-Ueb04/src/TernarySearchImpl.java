

public class TernarySearchImpl implements TernarySearch {

	
	/**
	 * Finds the minimum of a given function in the interval [left,right]
	 * 
	 * @param f
	 *            the function that is analyzed
	 * @param left
	 *            the left interval value
	 * @param right
	 *            the right interval value
	 * @return the minimum or <code>null</code> with illegal interval positions
	 *         
	 */
	@Override
	public Double findMinimum(Function f, double left, double right) {
		
		if((right - left) < EPSILON) {
			return (left + right)/2;
		}
		
		double leftRest = (2*left + right)/3;
		double rightRest = (left + 2*right)/3;
		
		if (leftRest > rightRest) {
			return findMinimum(f, leftRest, right);
		} else {
			return findMinimum(f, left, rightRest);
		}
	}

	public static void main(String[] args) {
		int count = 1000;
		Function[] f = new Function[count];
		double[] l = new double[count];
		double[] r = new double[count];
		for (int i = 0; i < count; i++) {
			f[i] = Polynomial.randomPolynomial((int) (Math.random() * 3));
			l[i] = -Math.random()*1000;
			r[i] = Math.random()*1000;
		}
		try {
			TernarySearch search = new TernarySearchImpl();
			ParallelTernarySearch tabm = new TaskedTernarySearch();
			// ParallelTernarySearch tabm = new ThreadedTernarySearch();
			double start = System.currentTimeMillis();
			@SuppressWarnings("unused")
			Double[] taskedRoots = tabm.findMinimum(f, l, r, 4, search);
	        double end = System.currentTimeMillis();
	        System.err.println("Time: "+(end-start));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
