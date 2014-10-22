import java.util.concurrent.*;
import java.util.*;

public class TaskedTernarySearch implements ParallelTernarySearch {
		
	/**
	 * @author  thorsten.schmidt
	 */
	private class TernaryThread implements Callable<Double> {
		/**
		 * @uml.property  name="f"
		 * @uml.associationEnd  
		 */
		private Function f;
		private double left;
		private double right;
		/**
		 * @uml.property  name="rootFinder"
		 * @uml.associationEnd  
		 */
		private TernarySearch rootFinder;
		
		public TernaryThread(Function f, double left, double right, TernarySearch rootFinder) {
			this.f = f;
			this.left = left;
			this.right = right;
			this.rootFinder = rootFinder;
		}
		
		public Double call() throws Exception {
			return rootFinder.findMinimum(f, left, right);
		}
	}
	
	public Double[] findMinimum(Function[] f, double[] left, double[] right, int nThreads, TernarySearch rootFinder) {
		
		if (f.length != left.length || f.length != right.length) {
			System.err.println("Laenge Function-Array stimmt nicht mit den Werten von 'left' oder 'right' ueberein");
			return null;
		}
		
		// Callable, Future, ExecutorService
		Callable<Double>[] callableArray = new Callable[f.length];
		Future<Double>[] futureArray = new Future[f.length];	
		ExecutorService executor = Executors.newFixedThreadPool(nThreads);
		// Array fuer Ergebnisse:
		Double[] ergebnis = new Double[f.length];
		
		for (int i=0; i < f.length; i++) {
			callableArray[i] = new TernaryThread(f[i], left[i], right[i], rootFinder);
			futureArray[i] = executor.submit(callableArray[i]);	// Uebergabe Callables in Future-Array
		}
		
		for (int i=0; i < f.length; i++) {
			try {
				executor.shutdown();
				ergebnis[i] = futureArray[i].get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return ergebnis;
	}

}


