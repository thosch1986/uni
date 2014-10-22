import java.util.concurrent.atomic.AtomicInteger;



public class AtomarerZaehler {

	private AtomicInteger zaehler = new AtomicInteger(0);
	public int getNext() {
		return zaehler.incrementAndGet();
	}
	
	int getValue() {
		return zaehler.get();
	}
}
