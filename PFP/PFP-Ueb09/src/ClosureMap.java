

import java.io.File;
import java.util.LinkedList;

import mapreduce.factory.MapFactory;
import mapreduce.stream.KeyValueStream;

public class ClosureMap implements MapFactory<Integer, LinkedList<Integer>, Integer, LinkedList<Integer>>{

	int[][] adj;
	public ClosureMap(int[][] adj){
		this.adj = adj;
	}
	
	@Override
	public mapreduce.factory.MapFactory.Mapper<Integer, LinkedList<Integer>, Integer, LinkedList<Integer>> create() {
		return new MapFactory.Mapper<Integer, LinkedList<Integer>, Integer, LinkedList<Integer>>() {

			public LinkedList<Integer> copy(LinkedList<Integer> orig){
				LinkedList<Integer> newList = new LinkedList<Integer>();
				for (Integer i : orig){
					newList.add(i);
				}
				return newList;
			}
			

			public void map(final Integer key, final LinkedList<Integer> input,
					final KeyValueStream<Integer, LinkedList<Integer>> tmpStream) {
				for (int i = 0; i < adj.length; i++){
					if (adj[key][i]!=-1){
						LinkedList<Integer> copy = copy(input);
						if (!copy.contains(key)){
							copy.add(key);
						}
						tmpStream.put(i, copy);
					}
				}
			}
		};
	}

}
