

/**
 * 
 * This class implements four different versions of the dining philosophers problem
 * @author Georg Dotzler<georg.dotzler@informatik.uni-erlangen.de>
 *
 */
public class Dinner {
	

	/**
	 * Naive implementation, leads to a deadlock sooner or later
	 * @param philosophers
	 * 			number of philosophers at the table
	 */
	public static void startDinnerDeadlock(int philosophers){

		// Anzahl Gablen anlegen;
		int gabeln = philosophers;
		Fork[] forks = new Fork[gabeln];
		
		for (int i=0; i < gabeln; i++) {
			forks[i] = new Fork();	// Gabel in i-ten "Gabelkasten"...
			System.out.println(forks[i]);
		}
		
		// Speicher fuer kommende Threads und Runnables anlegen!
		Thread[] threads = new Thread[philosophers];
		
		// Threads fuer Philosophen anlegen
		for(int i=0; i < philosophers -1; i++) {
			threads[i] = new Thread(new PhilosopherDeadlock(forks[i], forks[i+1]));	
		}
			threads[philosophers -1] = new Thread(new PhilosopherDeadlock(forks[gabeln-1], forks[0]));
			
		// Threads starten:
		for(int i=0; i < philosophers; i++) {
			threads[i].start();
		}
		
		// Threads joinen:
		for(int i=0; i < philosophers; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * In this version, a philosopher that starts to eat is quick enough
	 * to pick up two forks before any other philosopher can lift a finger.
	 * This avoids deadlocks.
	 * @param philosophers
	 * 			number of philosophers at the table
	 */
	public static void startDinnerQuickPhilosphers(int philosophers){
		
				// Marke zum synchronisieren:
				Object marke = new Object();
		
				// Anzahl Gablen anlegen;
				int gabeln = philosophers;
				Fork[] forks = new Fork[gabeln];
				
				for (int i=0; i < gabeln; i++) {
					forks[i] = new Fork();	// Gabel in i-ten "Gabelkasten"...
					System.out.println(forks[i]);
				}
				
				// Speicher fuer kommende Threads und Runnables anlegen!
				Thread[] threads = new Thread[philosophers];
				
				// Threads fuer Philosophen anlegen
				for(int i=0; i < philosophers -1; i++) {
					threads[i] = new Thread(new PhilosopherQuick(forks[i], forks[i+1], marke));
				}
				
				// Thread fŸr letzten Philosophen:
				threads[philosophers -1] = new Thread(new PhilosopherQuick(forks[gabeln-1], forks[0], marke));
					
				// Threads starten:
				for(int i=0; i < philosophers; i++) {
					threads[i].start();
				}
				
				// Threads joinen:
				for(int i=0; i < philosophers; i++) {
					try {
						threads[i].join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
		
	} 
	
	/**
	 * In this version the forks are numbered and each philosopher wants to pick up
	 * the fork with the higher number first. This avoids deadlocks.
	 * @param philosophers
	 * 			number of philosophers at the table
	 */
	public static void startDinnerOrderedForks(int philosophers){
		
		// Anzahl Gablen anlegen;
		int gabeln = philosophers;
		Fork[] forks = new Fork[gabeln];
		
		for (int i=0; i < gabeln; i++) {
			forks[i] = new Fork();	// Gabel in i-ten "Gabelkasten"...
			System.out.println(forks[i]);
		}
		
		// Speicher fuer kommende Threads und Runnables anlegen!
		Thread[] threads = new Thread[philosophers];
		
		// Threads fuer Philosophen anlegen
		for(int i=0; i < philosophers -1; i++) {
			threads[i] = new Thread(new PhilosopherDeadlock(forks[i], forks[i+1]));	
		}
			// HIER DER "TRICK": "Rumdrecken beim Aufruf der Gabeln!!!"
			threads[philosophers -1] = new Thread(new PhilosopherDeadlock(forks[0], forks[gabeln-1]));
			
		// Threads starten:
		for(int i=0; i < philosophers; i++) {
			threads[i].start();
		}
		
		// Threads joinen:
		for(int i=0; i < philosophers; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	} 
	
	/**
	 * In this version a philosopher has to ask a waiter for permission before he can eat.
	 * This avoids deadlocks.
	 * @param philosophers
	 * 			number of philosophers at the table
	 */
	public static void startDinnerWaiter(int philosophers){
		
		// Anzahl Gablen anlegen;
		int gabeln = philosophers;
		Fork[] forks = new Fork[gabeln];
		
		for (int i=0; i < gabeln; i++) {
			forks[i] = new Fork();	// Gabel in i-ten "Gabelkasten"...
			//System.out.println(forks[i]);
		}
		
		// Waiter
		Waiter w = new Waiter(philosophers);
		
		// Speicher fuer kommende Threads und Runnables anlegen!
		Thread[] threads = new Thread[philosophers];
		
		// Threads fuer Philosophen anlegen
		for(int i=0; i < philosophers -1; i++) {
			threads[i] = new Thread(new PhilosopherWaiter(forks[i], forks[i+1], w));	
		}
			threads[philosophers -1] = new Thread(new PhilosopherWaiter(forks[gabeln - 1], forks[0], w));
			
		// Threads starten:
		for(int i=0; i < philosophers; i++) {
			threads[i].start();
		}
		
		// Threads joinen:
		for(int i=0; i < philosophers; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public static void main(String[] args){
		//Dinner.startDinnerDeadlock(3);
		// Dinner.startDinnerQuickPhilosphers(7);
		// Dinner.startDinnerOrderedForks(5);
		Dinner.startDinnerWaiter(9);
	}
	
}
