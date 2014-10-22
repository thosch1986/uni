public interface GrabTheCookies {

	 /**
     * Starts a new game with names.length players
     * @param competition
     * @param name 
     *              Name list of the players
     * @param numberOfCookies 
     *              Number of cookies in the game 
     */

	public abstract void startGame(String[] names, int numberOfCookies);

}