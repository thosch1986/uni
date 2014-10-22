

import java.util.LinkedList;

import mapreduce.factory.ReduceFactory;
import mapreduce.stream.KeyValueStream;

public class ClosureRed implements ReduceFactory<Integer, LinkedList<Integer>, Integer, LinkedList<Integer>> {

	@Override
	public mapreduce.factory.ReduceFactory.Reducer<Integer, LinkedList<Integer>, Integer, LinkedList<Integer>> create() {
		return new ReduceFactory.Reducer<Integer, LinkedList<Integer>, Integer, LinkedList<Integer>>() {

			@Override
			public void reduce(Integer key, Iterable<LinkedList<Integer>> values,
					KeyValueStream<Integer, LinkedList<Integer>> resultStream) {
				int min = -1;
				LinkedList<Integer> newList = new LinkedList<Integer>();
				for (LinkedList<Integer> v : values) {
					for (Integer i : v){
						newList.add(i);
					}
				}
				resultStream.put(key, newList);
			}

		};
	}

}
