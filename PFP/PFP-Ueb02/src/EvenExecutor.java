
import java.lang.Runnable;
import java.util.concurrent.*;

public class EvenExecutor implements Runnable {
	
	/**
	 * @uml.property  name="t"
	 */
	private int t;

	public EvenExecutor(int t) {	// Konstruktor
		this.t = t;		// "Setter"

	}
	
	public void run() {
		try {
			// Ueberpruefung, ob Threadnummer gerade/ungerade.
			if (t%2 == 0) {
				System.out.println(t + " is even.");
			} else if (t%2 == 1) {
				System.out.println(t + " is odd.");
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		// Einlesen des ersten Arguments
		int n = Integer.parseInt(args[0]);
		
		// Executorpool anlegen!
		ExecutorService e = Executors.newFixedThreadPool(n);
		
		// Threads erzeugen & starten
		for (int i=0; i<n; i++) {
//			run[i] = new EvenExecutor(i);
//			t[i] = new Thread(run[i]); // Runnables in Thread "propfen"
			e.execute(new EvenExecutor(i));	// ???
		}
		
		e.shutdown();	// ExecutorService wird geschlossen!
		try {
			e.awaitTermination(60, TimeUnit.SECONDS);
		} catch (InterruptedException ee) {
			
		}
	}
}
