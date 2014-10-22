
public class PhilosopherOrdered extends Philosopher {

	public PhilosopherOrdered(Fork firstFork, Fork secondFork) {
		super(firstFork, secondFork);
	}

	@Override
	public void eat() {
		
		// nehme mir beide Gabeln, nur diesmal die hšherwertigere zuerst!:
		secondFork.lock();
		firstFork.lock();
		System.out.println("Take ("+ firstFork +") as 1st Fork!");
		System.out.println("Take ("+ secondFork +") as 2nd Fork!");		
		
		// lege die Gabeln wieder zurueck:
		secondFork.unlock();
		firstFork.unlock();
	}

}
