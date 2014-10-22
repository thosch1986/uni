

public class NoVisiblity {

	private static boolean ready;
	private static int nr;
	
	private static class Reader extends Thread {
		
		public void run() {
			while (!ready) {
				Thread.yield();
			}
			System.out.println(nr);
		}
	}
	
	public static void main(String[] args) {
		new Reader().start();
		nr = 42;
		ready = true;
	}
}
