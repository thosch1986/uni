import java.awt.Point;
import java.awt.Rectangle;
import java.util.concurrent.atomic.AtomicInteger;

public class MonteCarloImpl implements MonteCarlo {

	private AtomicInteger globalSum = new AtomicInteger(0);

	/**
	 * 
	 * Computes intersection of two rectangles
	 * 
	 * @param rectangleA
	 *            the first rectangle
	 * @param rectangleB
	 *            the second rectangle
	 * @param threads
	 *            the number of threads to be used
	 * @param iterations
	 *            the total number of random points used for the calculation
	 * @return the intersection size
	 */
	@Override
	public double computeIntersection(Rectangle A, Rectangle B, int threads,
			long iterations) {

		// Threads erstellen
		Thread[] threadArray = new Thread[threads];
		Worker[] runnableArray = new Worker[threads];
		
		Rectangle C = new Rectangle(A);
        
		/*
		Rectangle C = new Rectangle (0,0,0,0);
		C = A; //das hier waere falsch, aber warum??????
		*/
		
		// Bilde grosses Rechteck aus A und B:
		C.add(B);
		
		int ueberhang = 0;
		int intervall = 0; // Groesse des Intervalls der abzuarbeitenden Threads
		int start = 0;
		
		intervall = (int) (iterations / threads);
		ueberhang = (int) (iterations % threads);
		System.out.println("Ueberhang: " + ueberhang);
		
		int ende = intervall;
		
		// Threads starten
		for (int i = 0; i < threads; i++) {
			
			if (ueberhang > 0) {
				ueberhang--;
				ende += 1;
			}
			
			System.out.println("1) Start: " + start + " Ende: " + ende + " Iterations: " + iterations);
			runnableArray[i] = new Worker(A,B,C, start, ende, globalSum);
			threadArray[i] = new Thread(runnableArray[i]);
			threadArray[i].start();
			
			if (ueberhang >= 0) {
			 	start++;
			 }
			
			start += intervall;
			ende += intervall;
			System.out.println("2) Start: " + start + " Ende: " + ende + " Iterations: " + iterations);
		}

		// Threads joinen
		for (int i = 0; i < threads; i++) {
			try {
				threadArray[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		//return (double) ((globalSum.get()/iterations)*(C.width * C.height));
		return ((double) (globalSum.get() * (double) C.width * (double) C.height) / iterations);
	}

	public class Worker extends Thread {

		Rectangle A, B, C;
		long anzahlPunkte;
		int start;
		int ende;
		AtomicInteger globalSum;

		Worker(Rectangle A, Rectangle B, Rectangle C, int start, int ende, AtomicInteger globalSum) {
			this.A = A;
			this.B = B;
			this.C = C;
			this.start = start;
			this.ende = ende;
			this.globalSum = globalSum;
		}

		public void run() {
			// haue mir einen X- und Y-Wert im Viereck heraus!
			int mySum = 0;

			for (int i = start; i <= ende; i++) {
				double x = (C.x + (Math.random() * (C.width - C.x )));
				double y = (C.y + (Math.random() * (C.height - C.y )));

				if (A.contains(x, y) && B.contains(x, y)) {
					mySum++;
					//System.out.println("Hallo");
				}
			}
			globalSum.addAndGet(mySum);
		}
	}

	public static void main(String[] args) {
		Rectangle A = new Rectangle(0, 0, 2, 2);
		Rectangle B = new Rectangle(1, 1, 2, 2);
		MonteCarlo m = new MonteCarloImpl();
		long iterations = (long) Integer.MAX_VALUE / 20;		// 2
 		double r = m.computeIntersection(A, B, 8, iterations);
		System.err.println("Result: " + r);
	}

}
