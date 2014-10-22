public class Monitor {

	// no public fields

	private final Object myLock = new Object();

	public void main(String[] args) {
	
	public void someMethod() {
		synchronized(myLock) {
			System.out.println("Hallo!");
		}
		}
	}
}
