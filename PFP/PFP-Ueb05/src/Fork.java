/**
 * created 21.04.2008
 */
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class represents a fork ;) With the Java lock Mechanism it is possible to take the fork and to give it back
 * @author  Marc Woerlein<woerlein@informatik.uni-erlangen.de>
 * @author  Georg Dotzler<georg.dotzler@informatik.uni-erlangen.de>
 */

public class Fork extends ReentrantLock {

	static int idc = 0;

	/**
	 * @uml.property  name="id"
	 */
	final int id = idc++;

	
	/** Beautiful text output for the fork
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Fork-" + id;
	}

}
