
public interface Screen {

	
	/**
	 * Sets the point(x,y) to the specified character
	 * 
	 * @param x
	 * @param y
	 * @param character
	 */
	 public void setChar(final int x, final int y, final char character);
	 
	/**
	 * @return <code>true</code>, if the number of colorize calls is equal or greater as the number of points
	 */
	public boolean finished();
	
	/**
	 * checks, whether a point has an assigned character or not
	 * 
	 * @param x
	 * @param y
	 * @return <code>true</code>, if the the point (x,y) already has a color
	 */
	public boolean hasChar(final int x, final int y);
	
	/**
	 * @return Number of points in x-direction
	 */
	public int getX();
	
	/**
	 * @return Number of points in y-direction
	 */
	public int getY();
}
