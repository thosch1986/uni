/**
 * @author Georg Dotzler<georg.dotzler@informatik.uni-erlangen.de>
 *
 */
public interface Matrix {
	
	/**
	 * Randomly writes matrix code on a canvas
	 * @param width
	 * 				width of the canvas
	 * @param height
	 * 				height of the canvas
	 * @param threadCharacters
	 * 				character array used to fill the canvas
	 * 				the size of the array must be equal to the number of created threads
	 * 				each thread writes a different character to the canvas
	 */
	public void randomCode(Screen canvas, int width, int height, char[] threadCharacters);
	
	/**
	 * Randomly writes matrix code on a canvas, the critical parts are synchronized
	 * @param width
	 * 				width of the canvas
	 * @param height
	 * 				height of the canvas
	 * @param threadCharacters
	 * 				character array used to fill the canvas
	 * 				the size of the array must be equal to the number of created threads
	 * 				each thread writes a different character to the canvas
	 */
	public void synchronizedCode(Screen canvas, int width, int height, char[] threadCharacters);
	
	/**
	 * Randomly writes matrix code on a canvas, avoids collision without synchronization
	 * @param width
	 * 				width of the canvas
	 * @param height
	 * 				height of the canvas
	 * @param threadCharacters
	 * 				character array used to fill the canvas
	 * 				the size of the array must be equal to the number of created threads
	 * 				each thread writes a different character to the canvas
	 */
	public void syncFreeCode(Screen canvas, int width, int height, char[] threadCharacters);
}
