/**
 * created 06.05.2008
 */

// import java.util.concurrent.atomic.AtomicBoolean; Volatile besser?!
// import java.util.concurrent.atomic.AtomicInteger; Volatile besser?!
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Marc Woerlein<woerlein@informatik.uni-erlangen.de>
 * 
 */
public class FixedThreadExecutor implements SimpleExecutorService {

	// Atomar:
	// private AtomicBoolean isShutdown = new AtomicBoolean(false);
	private volatile boolean isShutdown = false;
	private volatile int laufendeThreads = 0;

	private int anzahlThreads = 0;
	private ReentrantLock lock;

	private final int MAX_QUEUE = 50000;

	LinkedBlockingQueue<Thread> threads = null;
	LinkedBlockingQueue<FutureTask> warteschlange = null;

	// Konstruktor erhält die Thread-Anzahl:
	public FixedThreadExecutor(int anzahlThreads) {

		this.anzahlThreads = anzahlThreads;
		isShutdown = false;

		threads = new LinkedBlockingQueue<Thread>();
		warteschlange = new LinkedBlockingQueue<FutureTask>();

		lock = new ReentrantLock();

		for (Thread t : threads) {
			threads.add(new Worker()); // fueger neuen Worker-Thread in die
										// Liste ein!
		}

		for (Thread t : threads) { // t => Laufvariable!
			t.start(); // fueger neuen Worker-Thread in die Liste ein!
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#isShutdown()
	 */
	public boolean isShutdown() {

		return isShutdown;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#shutdown()
	 */
	public void shutdown() {
		isShutdown = true;

		if (laufendeThreads == 0 & warteschlange.size() == 0) {
			for (Thread t : threads) {
				// "sanftes herunterfahren der Threads"
				t.interrupt();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#shutdownNow()
	 */
	public List<Runnable> shutdownNow() {
		isShutdown = true;

		List<Runnable> list = new LinkedList<Runnable>();

		// "alle Threads radikal herunterfahren"
		for (Thread t : threads) {
			t.interrupt();
		}

		// was ist noch in der Warteschlange?
		list.addAll(warteschlange);
		// Inhalt der Warteschlange eliminieren:
		warteschlange.clear();

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.util.concurrent.ExecutorService#submit(java.util.concurrent.Callable
	 * )
	 */
	public <T> Future<T> submit(Callable<T> task) {

		// Falls keine Threads mehr existieren:
		if (this.isShutdown() == true) {
			return null;
		}

		Future<T> futureResult = new FutureTask(task);
		try {
			warteschlange.put((FutureTask) futureResult); // ans Ende hängen,
															// morphe in einen
															// FT Container
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return futureResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#submit(java.lang.Runnable,
	 * java.lang.Object)
	 */
	public <T> Future<T> submit(Runnable task, T result) {

		// Falls keine Threads mehr existieren:
		if (this.isShutdown() == true) {
			return null;
		}

		Future<T> futureResult = new FutureTask(task, result);
		try {
			warteschlange.put((FutureTask) futureResult); // ans Ende hängen,
															// morphe in einen
															// FT Container
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return futureResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.Executor#execute(java.lang.Runnable)
	 */
	public void execute(Runnable command) {

		// Falls keine Threads mehr existieren:
		if (this.isShutdown() == true) {
			return;
		}
		// "Haue das Element an's Ende der Warteschlange
		warteschlange.add((FutureTask) command);
	}

	// Was soll der Thread machen? Siehe hier:
	class Worker extends Thread {

		private Runnable execute;

		public void run() {
			while (true) {
				// Marke eliminiert Wettlauf:
				try {
					lock.lockInterruptibly();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} // bis auf nicht beendete Threads;
				if ((isShutdown = true) && (warteschlange.size() == 0)) {
					// hat dieser Thread die Marke?
					if (lock.isHeldByCurrentThread()) {
						// Marke abgeben:
						lock.unlock();
					}
					return;
				}

				try {
					execute = warteschlange.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				laufendeThreads++;

				// hat dieser Thread noch die Marke?
				if (lock.isHeldByCurrentThread()) {
					// Marke abgeben:
					lock.unlock();
				}

				execute.run();
				laufendeThreads--;
			}
		}
	}
}
