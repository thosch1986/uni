
/**
 * @author  thorsten.schmidt
 */
public class PhilosopherQuick extends Philosopher {

	/**
	 * @uml.property  name="marke"
	 */
	private Object marke;

	public PhilosopherQuick(Fork firstFork, Fork secondFork, Object marke) {
		super(firstFork, secondFork);
		this.marke = marke;
	}

	@Override
	public void eat() {
		
		synchronized(marke) {
			// nehme mir beide Gabeln:
			firstFork.lock();
			System.out.println("I'm Philosopher Nr. " + myid + " and I took ("+ firstFork +") as 1st Fork!");
			secondFork.lock();
			System.out.println("I'm Philosopher Nr. " + myid + " and I took ("+ secondFork +") as 2nd Fork!");
		
		// Unlock muss aus synchronized raus!
		}
		// lege die Gabeln wieder zurueck:
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		firstFork.unlock();
		secondFork.unlock();
			
		
	}

}
