/**
 * created 10.04.2008
 */

/**
 * @param <Type>
 * 
 * @author Marc Woerlein<woerlein@informatik.uni-erlangen.de>
 * @author Georg Dotzler<georg.dotzler@informatik.uni-erlangen.de>
 */
public class LinkedList<Type> {

	/**
	 * @author  thorsten.schmidt
	 */
	private static class Item<Type> {
		/**
		 * @uml.property  name="next"
		 * @uml.associationEnd  
		 */
		Item<Type> next;

		/**
		 * @uml.property  name="prev"
		 * @uml.associationEnd  
		 */
		Item<Type> prev;

		final Type value;

		Item(Type value) {
			this.value = value;
		}
	}

	/**
	 * Monitor object to synchronize the list
	 * @uml.property  name="monitor"
	 */
	private final Object monitor = new Object();

	/**
	 * Head pointer of the list <code>next</code> points to the first and <code>prev</code> to the last element of the list
	 * @uml.property  name="head"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private final Item<Type> head;

	/**
	 * Number of inserted elements
	 * @uml.property  name="size"
	 */
	private int size;

	/**
	 * creates an empty list
	 */
	public LinkedList() {
		synchronized (monitor) {
			head = new Item<Type>(null);
			head.next = head.prev = head;
			size = 0;
		}
	}

	/**
	 * Appends the given value at the end of the list
	 * 
	 * @param value
	 */
	public void add(Type value) {
		synchronized (monitor) {
			final Item<Type> n = new Item<Type>(value);
			n.next = head;
			n.prev = head.prev;
			n.next.prev = n.prev.next = n;
			size++;
		}
	}

	/**
	 * @return Number of inserted elements
	 */
	public int size() {
		synchronized (monitor) {
			return size;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		synchronized (monitor) {
			if (head.next == head)
				return "[]";
			StringBuilder b = new StringBuilder();
			b.append('[');
			b.append(head.next.value);
			int c = 1;
			for (Item<Type> ack = head.next.next; ack != head; ack = ack.next) {
				if ((c++) % 10 == 0)
					b.append('\n');
				b.append(", " + ack.value);
			}
			b.append(']');
			return b.toString();
		}
	}
}
