import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class MonteCarloParallel {

	class MonteCarloWorker implements Runnable {
		private int id;
		private int numThreads;
		private int pts;
		private AtomicInteger globalSum;
		private Random rnd;
		
		// Konstruktor
		public MonteCarloWorker(int d, int numThreads, int pts, AtomicInteger globalSum) {
			this.id = id;
			this.numThreads = numThreads;
			this.pts = pts;
			this.globalSum = globalSum;
			this.rnd = new Random(System.currentTimeMillis());	// nicht ganz korrekt. Threads koennten gleiche Uhrzeit sehen
		}
		
		public void run() {
			int mySum = 0;
			for (int i = id; i < pts; i += numThreads) {
				double x = rnd.nextDouble();
				double y = rnd.nextDouble();
				double d = x*x + y*y;
				if (d <= 1.0) {
					mySum++;	// Zaehle zunaechst nur selbst erzeugte Punkte
				}
			}
			globalSum.addAndGet(mySum);
		}
	}
	
	public static void main(String[] args) {
		int numThr = Integer.parseInt(args[0]);
		int pts = Integer.MAX_VALUE / 32;
		
		Thread[] threads = new Thread[numThr];
		AtomicInteger sum = new AtomicInteger(0);
		long start = System.currentTimeMillis();
		
		for(int i=0; i < numThr; i++) {
			MonteCarloWorker worker = new MonteCarloWorker(i, numThr, pts, sum);
			threads[i] = new Thread(worker);	// Erzeugen eines Threads
			threads[i].start();
		}
		
		for (int i=0; i < numThr; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println("pi: " + (((double) sum.get())/pts) * 4);
		System.out.println("computation took " + (end - start) + " ms");
	}
}
