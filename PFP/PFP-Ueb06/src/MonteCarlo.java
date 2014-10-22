import java.awt.Rectangle;

/**
 * 
 * Interface for the Monte Carlo intersection implementation
 * 
 * @author Georg Dotzler<georg.dotzler@cs.fau.de>
 *
 */

public interface MonteCarlo {

	/**
	 * 
	 * Computes intersection of two rectangles
	 * 
	 * @param rectangleA the first rectangle
	 * @param rectangleB the second rectangle
	 * @param threads the number of threads to be used
	 * @param iterations the total number of random points used for the calculation
	 * @return the intersection size
	 */
	public double computeIntersection(Rectangle rectangleA, Rectangle rectangleB, int threads, long iterations);
	
	
}
