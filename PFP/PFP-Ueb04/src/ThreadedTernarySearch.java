
public class ThreadedTernarySearch implements ParallelTernarySearch {
		
	@Override
	public Double[] findMinimum(Function[] f, double[] left, double[] right,
			int nThreads, TernarySearch rootFinder) {
			
			int ueberhang = 0;
			int intervall = 0; // Groesse des Intervalls der abzuarbeitenden Threads
			int intervallAnfangsZaehler = 0;
			int intervallEndeZaehler = 0;
			
			ueberhang = f.length % nThreads;
			intervall = f.length / nThreads + ueberhang / nThreads;
			
			intervallEndeZaehler = intervall;
			
			
			if (f.length != left.length || f.length != right.length) {
				System.err.println("Laenge Function-Array stimmt nicht mit den Werten von 'left' oder 'right' ueberein");
				return null;
			}
		
			// Speicher fuer kommende Threads anlegen!
			TernaryThread[] threads = new TernaryThread[nThreads];
			
			// Speicher fuer Ergebnisse anlegen!
			Double[] ergebnisse = new Double[f.length];
			
			// Threads mit "Arbeitspaketen" befuellen.
			for(int i=0; i < nThreads; i++) {
				threads[i] = new TernaryThread(f, left, right, rootFinder, intervallAnfangsZaehler, intervallEndeZaehler, ergebnisse);
				
				intervallAnfangsZaehler = intervallAnfangsZaehler + intervall;
				intervallEndeZaehler = intervallEndeZaehler + intervall;
			}
			
			// Threads starten
			for (int i=0; i < nThreads; i++) {
				threads[i].start();
			}		
			
			// Threads joinen
			for (int i=0; i < nThreads; i++) {
				
				try {
					threads[i].join();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}	
		
		return ergebnisse;
	}
	
	
	/**
	 * @author  thorsten.schmidt
	 */
	class TernaryThread extends Thread {
		
		/**
		 * @uml.property  name="f"
		 * @uml.associationEnd  multiplicity="(0 -1)"
		 */
		private Function[] f;
		private double[] left;
		private double[] right;
		/**
		 * @uml.property  name="rootFinder"
		 * @uml.associationEnd  
		 */
		private TernarySearch rootFinder;
		private int intervallAnfangsZaehler;
		private int intervallEndeZaehler;
		private Double[] ergebnisse;

		public TernaryThread(Function[] f, double[] left, double[] right, TernarySearch rootFinder, int intervallAnfangsZaehler, int intervallEndeZaehler, Double[] ergebnisse) {	// Konstruktor
			this.f = f;		// "Setter"
			this.left = left;
			this.right = right;
			this.rootFinder = rootFinder;
			this.ergebnisse = ergebnisse;
		}
		
		public void run() {			
			
			for (int i = intervallAnfangsZaehler; i <= intervallEndeZaehler; i++) {
				ergebnisse[i] = rootFinder.findMinimum(f[i], left[i], right[i]);
			}
			
			
		}
		
	}
}



