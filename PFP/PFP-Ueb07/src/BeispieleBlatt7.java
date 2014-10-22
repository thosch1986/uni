import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.concurrent.LinkedBlockingQueue;

public class BeispieleBlatt7 {

	public static void main(String[] args) {
		final Exchanger<Integer> ex = new Exchanger<Integer>();

		Thread a = new Thread() {
			public void run() {
				int x = -1;
				try {
					x = ex.exchange(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("a: " + x);
			}
		};
		Thread b = new Thread() {
			public void run() {
				int x = -1;
				try {
					Thread.sleep(1000);
					x = ex.exchange(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("b: " + x);
			}
		};
		a.start();
		b.start();

		final BlockingQueue<Integer> bq = new LinkedBlockingQueue<Integer>();
		Thread c = new Thread() {
			public void run() {
				int x = -1;
				for (int i = 0; i < 20; i++) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					bq.add(i);
				}
				System.out.println("c fertig");
			}
		};
		Thread d = new Thread() {
			public void run() {
				int x = -1;
				try {
					for (int i = 0; i < 20; i++) {
						x = bq.take();
						System.out.println("d: " + x);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		c.start();
		d.start();

		final CyclicBarrier cb = new CyclicBarrier(2);
		Thread e = new Thread() {
			public void run() {
				try {
					Thread.sleep(5000);
					cb.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
				System.out.println("e fertig");
			}
		};
		Thread f = new Thread() {
			public void run() {
				try {
					cb.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
				System.out.println("f fertig");
			}
		};
		e.start();
		f.start();

		final CountDownLatch cdl = new CountDownLatch(2);
		Thread g = new Thread() {
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				cdl.countDown();
				System.out.println("g fertig");
			}
		};
		Thread h = new Thread() {
			public void run() {
				cdl.countDown();
				System.out.println("h fertig");
			}
		};
		Thread i = new Thread() {
			public void run() {
				try {
					cdl.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("i fertig");
			}
		};
		g.start();
		h.start();
		i.start();

	}

}
