public class MatrixImpl implements Matrix {
	
	public static final char[] codeChars = {' ', '\uFF66','\uFF67','\uFF68','\uFF69','\uFF6A','\uFF6B','\uFF6C','\uFF6D','4','2'};
	
	/**
	 * Randomly writes matrix code on a screen
	 * @param width
	 * 				width of the screen
	 * @param height
	 * 				height of the screen
	 * @param threadCharacters
	 * 				character array used to fill the screen
	 * 				the size of the array must be equal to the number of created threads
	 * 				each thread writes a different character to the screen
	 */
	public void randomCode(Screen screen, int width, int height, char[] threadCharacters){
		
		MatrixScreen m = new MatrixScreen(width, height);
		// Erzeugen der Threads:
		Thread[] t = new Thread[threadCharacters.length];
		
		for (int i=0; i < threadCharacters.length; i++) {
			t[i] = new Thread(new randomCode2(width,height, i, m));	// Übergabe
			// Starten der Threads:
			t[i].start();
		}
		
		for (int i=0; i < threadCharacters.length; i++) {
			try {
				t[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Randomly writes matrix code on a screen, the critical parts are synchronized
	 * @param width
	 * 				width of the screen
	 * @param height
	 * 				height of the screen
	 * @param threadCharacters
	 * 				character array used to fill the screen
	 * 				the size of the array must be equal to the number of created threads
	 * 				each thread writes a different character to the screen
	 */
	public void synchronizedCode(Screen screen, int width, int height, char[] threadCharacters){
		
		MatrixScreen m = new MatrixScreen(width, height);
		// Erzeugen der Threads:
		Thread[] t = new Thread[threadCharacters.length];
		
		for (int i=0; i < threadCharacters.length; i++) {
			t[i] = new Thread(new synchronizedCode2(width,height, i, m));	// Übergabe
			// Starten der Threads:
			t[i].start();
		}
		
		for (int i=0; i < threadCharacters.length; i++) {
			try {
				t[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Randomly writes matrix code on a screen, avoids collision without synchronization
	 * @param width
	 * 				width of the screen
	 * @param height
	 * 				height of the screen
	 * @param threadCharacters
	 * 				character array used to fill the screen
	 * 				the size of the array must be equal to the number of created threads
	 * 				each thread writes a different character to the screen
	 */
	public void syncFreeCode(Screen screen, int width, int height, char[] threadCharacters){
		
		MatrixScreen m = new MatrixScreen(width, height);
		// Erzeugen der Threads:
		Thread[] t = new Thread[threadCharacters.length];
		
		for (int i=0; i < threadCharacters.length; i++) {
			t[i] = new Thread(new syncFreeCode2(width,height, i, m, threadCharacters[i]));	// Übergabe
			// Starten der Threads:
			t[i].start();
		}
		
		for (int i=0; i < threadCharacters.length; i++) {
			try {
				t[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	// PRIVATE METHODEN: Hier geschieht das "Magische" ----------------------------------------------------
	/**
	 * @author  thorsten.schmidt
	 */
	private class randomCode2 implements Runnable {
		private int width;
		private int height;
		private int threadCharacters;
		/**
		 * @uml.property  name="m"
		 * @uml.associationEnd  
		 */
		private MatrixScreen m;
		private int x;
		private int y;
		
		// Konstruktor:
		public randomCode2(int width, int height, int threadCharacters, MatrixScreen m) {
			this.width = width;
			this.height = height;
			this.threadCharacters = threadCharacters;
			this.m = m;
		}
		
		// Hier macht der Thread "was":
		public void run() {
			while (!(m.finished())) {
				x = (int) (Math.random() * width);	// Casting wichtig!
				y = (int) (Math.random() * height);	// Casting wichtig!
				if(!(m.hasChar(x,y))) {
					m.setChar(x, y, (char) threadCharacters);
				}
			}
		}
	}
	
	/**
	 * @author  thorsten.schmidt
	 */
	private class synchronizedCode2 implements Runnable {
		private int width;
		private int height;
		private int threadCharacters;
		/**
		 * @uml.property  name="m"
		 * @uml.associationEnd  
		 */
		private MatrixScreen m;
		private int x;
		private int y;
		
		// Konstruktor:
		public synchronizedCode2(int width, int height, int threadCharacters, MatrixScreen m) {
			this.width = width;
			this.height = height;
			this.threadCharacters = threadCharacters;
			this.m = m;
		}
		
		// Hier macht der Thread "was":
		public void run() {
			while (!(m.finished())) {
				x = (int) (Math.random() * width);	// Casting wichtig!
				y = (int) (Math.random() * height);	// Casting wichtig!
				// HIER IST SYNCHRONISATIONSPUNKT!!!
				synchronized (m) { 
					if(!(m.hasChar(x,y))) {
						m.setChar(x, y, (char) threadCharacters);
					}
				}
			}
		}
	}
	
	/**
	 * @author  thorsten.schmidt
	 */
	private class syncFreeCode2 implements Runnable {
		private int width;
		private int height;
		private int threadCharacters;
		/**
		 * @uml.property  name="m"
		 * @uml.associationEnd  
		 */
		private MatrixScreen m;
		private char threadChar;
		
		// Konstruktor:
		public syncFreeCode2(int width, int height, int threadCharacters, MatrixScreen m, char threadChar) {
			this.width = width;
			this.height = height;
			this.threadCharacters = threadCharacters;
			this.m = m;
			this.threadChar = threadChar;
		}
		
		// Hier macht der Thread "was":
		public void run() {

			for (int y=0; !(m.finished()) && y < height; y++) {
				
				for (int x=0; !(m.finished()) && x < width; x = threadCharacters + x) {
					
						m.setChar(x, y, threadChar);

				}
			}
		}
	}
	// -------------------------------------------------------------------------------------------------------------------
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Matrix p = new MatrixImpl();
		int width = 50;
		int height = 50;
		MatrixScreen randomC = new MatrixScreen(width, height);
		MatrixScreen syncC = new MatrixScreen(width, height);
		MatrixScreen freeC = new MatrixScreen(width, height);
		p.randomCode(randomC, width, height, codeChars);
		p.synchronizedCode(syncC, width, height, codeChars);
		p.syncFreeCode(freeC, width, height, codeChars);
	}
	
	

}
