
/**
 * @author  thorsten.schmidt
 */
public class Waiter {

	/**
	 * @uml.property  name="waiter"
	 */
	private boolean[] waiter = null;
	/**
	 * @uml.property  name="philosophers"
	 */
	private int philosophers;
	
	// Konstruktor:
	public Waiter(int philosophers) {
		
		this.philosophers = philosophers;
		waiter = new boolean[philosophers];
		
		// Gabeln sind "verfuegbar"
		for(int i=0; i < philosophers; i++) {
			waiter[i] = true;
		}
	}
	
	public boolean askForPermission(Fork firstFork, Fork secondFork) {
		
		synchronized(this) { // Synchronisierungsobjekt "an mir selbst"
			
		if (waiter[firstFork.id] && waiter[secondFork.id]) {	// wenn gabel1 & gabel2 verfuegbar
			
			waiter[firstFork.id] = false;
			waiter[secondFork.id] = false;
			return true;
		} else {
		// Gabel 1 & Gabel 2 nicht verfuegbar
		return false;
		}
	}
  }
	
	public void giveForksBack(Fork firstFork, Fork secondFork) {
		
		synchronized(this) {
			// setze die Gabeln wieder auf "verfuegbar"
			waiter[firstFork.id] = true;
			waiter[secondFork.id] = true;
		}
	}
}