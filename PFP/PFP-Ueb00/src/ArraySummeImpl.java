import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class ArraySummeImpl implements ArraySumme {

	public long summe(long[] array, int nThreads) {
		// falsche Thread-Anzahl:
		if (nThreads < 1) {
			nThreads = 1;
		}
		if (array == null) {
			return 0;
		}
		if (array.length < nThreads) {
			nThreads = array.length;
		}
		
		// ExecutorService anlegen
		ExecutorService e = Executors.newFixedThreadPool(nThreads);
		
		int steps, rest;
		// Schrittweite fŸr alle Threads bis auf den letzten:
		steps = array.length / nThreads;
		// Schrittweite fŸr letzten Thread:
		rest = steps + array.length % nThreads;
		
		// Callables
		Callable<Long>[] worker = new ArraySummeImplWorker[nThreads];
		// Futures
		Future<Long>[] futures = new Future[nThreads];
		
		for (int i=0; i < nThreads; i++) {
			worker[i] = new ArraySummeImplWorker(Arrays.copyOfRange(array, i*steps, (i+1)*steps));
			futures[i] = e.submit(worker[i]);
		}
		
		// fuer letzten Thread:
		worker[nThreads - 1] = new ArraySummeImplWorker(Arrays.copyOfRange(array, array.length - rest, array.length));
		futures[nThreads - 1] = e.submit(worker[nThreads - 1]);
		
		e.shutdown();
		// Warte fuer maximal 60 Sekunden
		try {
			e.awaitTermination(60, TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// .......
		
		long ret = 0;
		for (int i=0; i < nThreads; i++) {
			try {
				ret = ret + futures[i].get();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				e1.printStackTrace();
			}
		}
		return ret;
	}
	
	public static void main(String[] args) {
		
		ArraySummeImpl test = new ArraySummeImpl();
		long[] array = {1,2,3,4,5,6};
		int nThreads = 5;
		System.out.println(test.summe(array, nThreads));
	}
}

class ArraySummeImplWorker implements Callable<Long> {
	
	long[] array;
	
	ArraySummeImplWorker(long[] a) {
		array = a;
	}
	
	
	// Callable benoetigt call-Methode
	public Long call() throws Exception {
		long ret = 0;
		
		for(int i=0; i < array.length; i++) {
			ret += array[i];
		}
		return null;
	}
}
