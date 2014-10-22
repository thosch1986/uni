
import java.util.concurrent.*;

public class ArrayDotProductImpl implements ArrayDotProduct {
//	private int n;
	
//	public ArrayDotProductImpl(int n) {
//		this.n = n;
//	}
//	public static void main(String[] args) {
//		//
//	}
	
	@SuppressWarnings("unchecked")
	public long dotProduct(long[] array1, long[] array2, int threads) {
		
		long summe = 0;
		int laenge1 = array1.length;	// Laenge Array 1
		int laenge2 = array2.length;	// Laenge Array 2
		int laenge;
		if (laenge1 < laenge2) {
			laenge = laenge2;
		} else {
			laenge = laenge1;
		}
		int start;
		int ende;
		int breite;
		
		
		// Arrays:
		Callable<Long>[] callableArray = new Callable[threads]; // Callable-Array
		ExecutorService e = Executors.newFixedThreadPool(threads);	// Executor-Pool mit Threads
		Future<Long>[] futureArray = new Future[threads];	// Future-Array (Future, weil Ergebnisse noch nicht bekannt)
		
		// Ergebnisse der Threads werden in einem Future-Array gespeichert!
		if (laenge < threads) {
			start = 0;
			breite = laenge/threads;
			ende = breite -1;
			
			for (int i=0; i<laenge; i++) {
				callableArray[i] = new Multiplizierer(array1, array2, i, i); // Werte aus Multiplizierer auslesen
				futureArray[i] = e.submit(callableArray[i]);	// an Futureobjekt Ÿbergeben.
			}
			for (int i=0; i<laenge; i++) {
				try {
					summe += futureArray[i].get();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (ExecutionException e1) {
					e1.printStackTrace();
				}
			}
		
		} else if (threads >= laenge) {
			for (int i = 0; i <= laenge; i++) {
				callableArray[i] = new Multiplizierer(array1, array2, i, i);
				futureArray[i] = e.submit(callableArray[i]);
			}
			for (int j = 0; j < laenge; j++) {
				try {
				summe = summe + futureArray[j].get();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (ExecutionException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			breite = laenge/threads; 	// LŠnge des Arrays durch die Thread-Anzahl
			start = 0; ende = breite -1;
			for (int i = 0; i < threads-1; i++) {
				callableArray[i] = new Multiplizierer(array1, array2, start, ende);
				futureArray[i] = e.submit(callableArray[i]);
				start = start + breite;
				ende = ende + breite;
			}
			
			for (int i=0; i < threads; i++) {
				try {
					summe = summe + futureArray[i].get();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (ExecutionException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		
		return 0;
	}
	
	// Vektormultiplikation
	class Multiplizierer implements Callable<Long> {
		private long[] array1;
		private long[] array2;
		private int start;
		private int ende;
		private long summe;
		
		public Multiplizierer(long[] array1, long[] array2, int start, int ende ) {
			this.array1 = array1;
			this.array2 = array2;
			this.start = start;
			this.ende = ende;
		}
		
		public Long call() throws Exception {
			summe = 0;
			for (int i = start; i<= ende; i++) {
				summe = summe + array1[i] * array2[i];
			}
			return summe;
		}
	}

}
