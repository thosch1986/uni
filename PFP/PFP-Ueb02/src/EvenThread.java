
import java.lang.Thread;

public class EvenThread extends Thread {
	
	/**
	 * @uml.property  name="t"
	 */
	private int t;

	public EvenThread(int t) {	// Konstruktor
		this.t = t;		// "Setter"

	}

	// Was soll der gestartete Thread machen?
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

	public static void main(String[] args) {
		
		// Einlesen des ersten Arguments
		int n = Integer.parseInt(args[0]);
		
		// Was passiert hier? ==> Marian fragen!
		EvenThread[] threads = new EvenThread[n];	// Speicher fŸr kommende Threads anlegen!
		
		// Threads erzeugen
		for(int i=0; i<n; i++) {
			threads[i] = new EvenThread(i);
		}
		
		// Threads starten
		for (int i=0; i<n; i++) {
			threads[i].start();
		}
		
		// Threads joinen
		for (int i=0; i<n; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
