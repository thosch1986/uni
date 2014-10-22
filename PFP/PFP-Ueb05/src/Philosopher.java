/**
 * created 21.04.2008
 */

/**
 * created 21.04.2008
 */

public abstract class Philosopher implements Runnable {	
	/**
	 * @uml.property  name="firstFork"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	public Fork firstFork = null;
	/**
	 * @uml.property  name="secondFork"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	public Fork secondFork = null;
	static int idc = 0;
	
	/**
	 * Names are out, philosphers have numbers
	 * @uml.property  name="myid"
	 */
	final int myid = idc++;
	
	public Philosopher(Fork firstFork, Fork secondFork){
		this.firstFork = firstFork;
		this.secondFork = secondFork;
	}
	
	
	/**
	 * 
	 * A philosopher takes his two forks (<code>lock</code>), eats (text output)
	 * and releases the two forks again
	 * 
	 */
	public abstract void eat();

	/**
	 * The thinking process of a philosopher is easy, a text output is sufficient ;)
	 */
	public void think(){
		System.out.println("I ("+myid+") think: The time has come to think more wisely, hasn't it?");
	}

	/**
	 * @return "Philosopher-<ID>"
	 */
	public String toString(){
		return "Philosopher-"+myid;
	}

	/**
	 * 
	 * A philosopher thinks, eats, thinks, eats, thinks, eats....
	 * 
	 */
	@Override
	public void run() {
		while (true){
			think();
			eat();
		}
		
	}
}