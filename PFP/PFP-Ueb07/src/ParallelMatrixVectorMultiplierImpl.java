
public class ParallelMatrixVectorMultiplierImpl implements ParallelMatrixVectorMultiplier {

	@Override
	public double[] multiplyMatrixByVector(double[][] A, double[] x, int numberOfThreads) {
		
		// Ergebnis-Array, das der Rueckgabe dient
		double[] ergebnis = new double[A.length];
		
		// Marke für synchronized-Blöcke
		Object marke = new Object();
		
		for (int i=0; i < numberOfThreads; i++) {
			
		}
		
		// Thread- und Runnable-Array anlegen:
		Thread[] threadArray = new Thread[numberOfThreads]; 	// Thread-Array anlegen
		Worker[] runnableArray = new Worker[numberOfThreads];	// Runnable-Array anlegen

		// Arbeit auf Threads aufteilen:
		int ueberhang = 0;
		int intervall = 0; // Groesse des Intervalls der abzuarbeitenden Threads
		int start = 0;
		intervall = A.length / numberOfThreads;
		int ende = intervall;

		// Threads starten
		for (int i = 0; i < numberOfThreads-1; i++) {
			runnableArray[i] = new Worker(marke, A, x, start, ende, ergebnis);
			threadArray[i] = new Thread(runnableArray[i]);
			threadArray[i].start();
			
			start += intervall;
			ende += intervall;
		}
		
		// Threads joinen
		for (int i = 0; i < numberOfThreads; i++) {
			try {
				threadArray[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return ergebnis;
	}
	
	public class Worker implements Runnable {

		// Inits:
		Object marke;
		double[][] A;
		double[] x;
		int start;
		int ende;
		double[] ergebnis;
		
		// Konstruktor - Durchschleifen der Werte:
		public Worker(Object marke, double[][] A, double[] x, int start, int ende, double[] ergebnis) {
			this.marke = marke;
			this.A = A;
			this.x = x;
			this.start = start;
			this.ende = ende;
			this.ergebnis = ergebnis;
		}
		
		public void run() {
			
			double localSum = 0;
			
			for (int i = start; i < ende; i++) {
				for (int j = 0; j < x.length; j++) {
					localSum += A[i][j] * x[j];
				}
				
				// Synchronisierter Zugriff:
				synchronized(marke) {
					ergebnis[i] = localSum;
				}
				
				// danach LocalSum (pro Zeile) wieder auf Null setzen!
				localSum = 0;
				
			}
		}
	}

	
}
