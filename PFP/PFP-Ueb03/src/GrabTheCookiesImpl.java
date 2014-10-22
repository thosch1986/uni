public class GrabTheCookiesImpl implements GrabTheCookies {
	
	

	public static void main(final String[] args) throws InterruptedException {
		final int n = 10;
		final String[] names = {"Cookie Monster", "Count von Count", "Curly Bear", "Ernie", "Miss Piggy"} ;
		GrabTheCookies game = new GrabTheCookiesImpl();
		game.startGame(names, n);
	}
	
	/**
	 * @author  thorsten.schmidt
	 */
	private class PlayerThread extends Thread {
		
		/**
		 * @uml.property  name="p"
		 * @uml.associationEnd  
		 */
		private Player p;
		/**
		 * @uml.property  name="g"
		 * @uml.associationEnd  
		 */
		private Game g;
		
		public PlayerThread(Player p, Game g) {
			this.p = p;
			this.g = g;
		}
		
		public void run() {
			while(!g.isFinished()) {
				p.grab();
			}
		}
	}
	
	 /**
     * Starts a new game with names.length players
     * @param competition
     * @param name 
     *              Name list of the players
     * @param numberOfCookies 
     *              Number of cookies in the game 
     */
	@Override
	public void startGame(String[] names, int numberOfCookies) {
		
		int n = names.length;
		final Game g = new Game(names, numberOfCookies);
		Player[] p = g.getPlayers();
		
		PlayerThread[] threads = new PlayerThread[n];
		
		for (int i=0; i < n; i++) {
			threads[i] = new PlayerThread(p[i], g);
			threads[i].start();
		}
		
		try {
			for (int i=0; i < n; i++) {
				threads[i].join();
			}	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
