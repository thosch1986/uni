
/**
 * @author  thorsten.schmidt
 */
public class PhilosopherWaiter extends Philosopher {
	
	/**
	 * @uml.property  name="w"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	public Waiter w;

	public PhilosopherWaiter(Fork firstFork, Fork secondFork, Waiter w) {
		super(firstFork, secondFork);
		this.w = w;
	}

	@Override
	public void eat() {
		
		// nehme mir beide Gabeln, nur diesmal die hšherwertigere zuerst!:
		
		// frage Waiter!
		if(w.askForPermission(firstFork, secondFork) == true) {
		
			firstFork.lock();
			secondFork.lock();
		
			System.out.println("Take ("+ firstFork +") as 1st Fork!");
			System.out.println("Take ("+ secondFork +") as 2nd Fork!");		
			
			// lege die Gabeln wieder zurueck:
			firstFork.unlock();
			secondFork.unlock();
			
			// Waiter mitteilen, dass Gabeln wieder verfuegbar sind!
			w.giveForksBack(firstFork, secondFork);
			
		} else {
			try {
				Thread.sleep(120);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
