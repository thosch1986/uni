

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import mapreduce.stream.MRPair;
/**
 * 
 * Allows to iterate over the input in the map 
 * 
 * @author Georg Dotzler<georg.dotzler@cs.fau.de>
 *
 */
public class ClosureIterable implements Iterable<MRPair<Integer, LinkedList<Integer>>>{
	public boolean[] changed;
	public LinkedList<Integer>[] values;

	/**
	 * 
	 * @param changed true if the corresponding list (linked by the array index) was modified in this round
	 * @param values values[0] lists all parent nodes to node 0 
	 */
	public ClosureIterable(boolean[] changed, LinkedList<Integer>[] values){
		this.changed = changed;
		this.values = values;
	}
	@Override
	public Iterator<MRPair<Integer, LinkedList<Integer>>> iterator() {
		return new Iterator<MRPair<Integer,LinkedList<Integer>>>() {
			int pos = 0;
			@Override
			public boolean hasNext() {
				while ( pos < values.length &&changed[pos]!=true){
					pos++;
				}
				return pos < values.length;
			}

			@Override
			public MRPair<Integer, LinkedList<Integer>> next() {
				if (hasNext()){
					return new MRPair<Integer, LinkedList<Integer>>(pos, values[pos++]);
				}
				throw new NoSuchElementException();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();

			}

		};
	}

}
