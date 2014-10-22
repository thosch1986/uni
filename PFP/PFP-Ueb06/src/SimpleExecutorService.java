/**
 * created 06.05.2008
 */

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author Marc Woerlein<woerlein@informatik.uni-erlangen.de>
 * 
 */
public interface SimpleExecutorService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#isShutdown()
	 */
	public boolean isShutdown();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#shutdown()
	 */
	public void shutdown();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#shutdownNow()
	 */
	public List<Runnable> shutdownNow();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#submit(java.util.concurrent.Callable)
	 */
	public <T> Future<T> submit(Callable<T> task);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#submit(java.lang.Runnable,
	 *      java.lang.Object)
	 */
	public <T> Future<T> submit(Runnable task, T result);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.Executor#execute(java.lang.Runnable)
	 */
	public void execute(Runnable command);
}
