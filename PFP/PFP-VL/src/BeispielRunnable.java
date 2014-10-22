
public class BeispielRunnable implements Runnable {
	
	public void run() {
		System.out.println("Hello from a thread!");
	}
	
	public static void main(String[] args) {
		(new Thread(new BeispielRunnable(), "myName")).start(); // Warum hier einklammern?
	}
}
