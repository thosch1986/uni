
public class PhilosopherDeadlock extends Philosopher {

	public PhilosopherDeadlock(Fork firstFork, Fork secondFork) {
		super(firstFork, secondFork);
	}

	@Override
	public void eat() {
		
		// nehme mir beide Gabeln
		firstFork.lock();
		secondFork.lock();
		
		System.out.println("Take ("+ firstFork +") as 1st Fork!");
		System.out.println("Take ("+ secondFork +") as 2nd Fork!");
		
		// esse ....
		
		
		// lege die Gabeln wieder zurueck:
		firstFork.unlock();
		secondFork.unlock();
		
	}

}
