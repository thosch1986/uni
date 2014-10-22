import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.*;

public class ParallelGaussSolver implements GaussEliminationSolver {

	@Override
	public double[] solveEquations(double[][] A, double[] b, int numberOfThreads) {

		double[] ergebnis;

		// Vorwärtselimination:
		vorwaertselimination(A, b, numberOfThreads);
		// Rückwärtselimination:
		ergebnis = rueckwaertselimination(A, b, numberOfThreads);

		return ergebnis;
	}

	private void vorwaertselimination(double[][] A, double[] b,
			int numberOfThreads) {

		// Cyclic Barrier
		CyclicBarrier barrier = new CyclicBarrier(numberOfThreads);

		// Thread
		Thread[] threadArray = new Thread[numberOfThreads];
		Worker[] runnableArray = new Worker[numberOfThreads];

		// Zaehler:
		AtomicInteger zaehler = new AtomicInteger(0);

		// Arbeit auf Threads aufteilen:
		// int ueberhang = 0;
		int intervall = 0; // Groesse des Intervalls der abzuarbeitenden Threads
		int start = 0;
		intervall = A.length / numberOfThreads;
		int ende = intervall;

		// Threads starten:
		for (int i = 0; i < numberOfThreads - 1; i++) {
			runnableArray[i] = new Worker(A, b, barrier, i, start, ende,
					zaehler);
			threadArray[i] = new Thread(runnableArray[i]);
			threadArray[i].start();

			start += intervall;
			ende += intervall;
		}

		// Threads joinen:
		for (int i = 0; i < numberOfThreads; i++) {
			try {
				threadArray[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	
	private double[] rueckwaertselimination(double[][] A, double[] b, int numberOfThreads) {
        // Rücksubstitution:
		double[] ergebnis = new double[A.length];
        for (int y = 0; y < A.length; y++) {
                ergebnis[y] = b[y];
            }
        return ergebnis;
        
	}

	class Worker implements Runnable {

		AtomicInteger zaehler;

		double[][] A;
		double[] b;
		CyclicBarrier barrier;
		int ii;
		int start;
		int ende;

		public Worker(double[][] A, double[] b, CyclicBarrier barrier, int i,
				int start, int ende, AtomicInteger zaehler) {
			this.A = A;
			this.b = b;
			this.barrier = barrier;
			this.ii = i;
			this.start = start;
			this.ende = ende;
			this.zaehler = zaehler;
		}

		public void run() {

			for (int i = 0; i < b.length; i++) {
				if (i == 0) {
					int counter = zaehler.intValue();
					double pivot = A[i][i];

					if (pivot != 1) {
						b[counter] /= pivot;
						for (int j = 0; j < A.length; j++) {
							A[counter][j] /= pivot;
						}
						pivot = A[counter][counter];
					}
					zaehler.incrementAndGet();
				}

				try {
					barrier.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}

				// hier kommt der Thread ins Spiel...
				for (int k = start; k < ende; k++) {
					if (i != k) {
						double elem = A[k][i];

						b[k] += -(elem) * b[i];

						for (int l = 0; l < A[ii].length; l++) {
							A[k][l] += -(elem) * A[i][l];
						}
					}
				}

				try {
					barrier.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
			}

			try {
				barrier.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}

		}

	}
}
