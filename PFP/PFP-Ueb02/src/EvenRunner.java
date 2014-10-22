
import java.lang.Runnable;

public class EvenRunner implements Runnable {
	
	/**
	 * @uml.property  name="t"
	 */
	private int t;

	public EvenRunner(int t) {	// Konstruktor
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
		
		// Was passiert hier? ==> Marian fragen!
		EvenRunner[] run = new EvenRunner[n];	// Speicher fŸr Runnables anlegen!
		Thread[] t = new Thread[n];	// Threadspeicher anlegen!
		
		// Threads erzeugen
		for (int i=0; i<n; i++) {
			run[i] = new EvenRunner(i);
			t[i] = new Thread(run[i]);
		}
		
		// Threads starten
		for (int i=0; i<n; i++) {
			t[i].start();
		}
		
		// Threads joinen
		for (int i=0; i<n; i++) {
			try {
				t[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
}
