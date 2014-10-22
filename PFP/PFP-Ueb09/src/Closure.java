

import java.util.LinkedList;

import mapreduce.master.ExecutorMaster;
import mapreduce.master.Master;
import mapreduce.stream.MRPair;

public class Closure {

	/**
	 * Computes the transitive closure
	 * 
	 * @param adj The adjacency matrix of the graph
	 * @return For each node i a list of all from i reachable nodes 
	 */

	public LinkedList<Integer>[] closure(int[][] adj){
		//initialize the map reduce framework
		Master<Integer, LinkedList<Integer>, Integer, LinkedList<Integer>> master = new ExecutorMaster<Integer, LinkedList<Integer>, Integer, LinkedList<Integer>, Integer, LinkedList<Integer>>
		(new ClosureMap(adj), new ClosureRed(), 8);

		LinkedList<Integer>[] reachableFrom = new LinkedList[adj.length];
		boolean[] changes = new boolean[adj.length];
		boolean[] copy = new boolean[adj.length];
		boolean changed = false;
		for (int i = 0; i < adj.length;i++){
			reachableFrom[i] = null;
			changes[i] = false;
			copy[i] = false;
		}
		reachableFrom[0] = new LinkedList<Integer>();
		changes[0] = true;

		//Create initial iterable, with a array of changes for each node and an array of Integer lists.
		//Each list corresponds all nodes that are 
		ClosureIterable sourceIterable = new ClosureIterable(changes, reachableFrom);

		do {
			changed = false;
			//compute step and iterate over results
			Iterable<MRPair<Integer, LinkedList<Integer>>> resultIterable  = master.mrRun(sourceIterable);
			for (MRPair<Integer, LinkedList<Integer>> elem:resultIterable){
				if (reachableFrom[elem.key]==null||elem.value.size()>reachableFrom[elem.key].size()){
					reachableFrom[elem.key] = elem.value;
					copy[elem.key] = true;
					changed = true;
				}
			}

			//Switch boolean arrays to initialize next round
			for (int i = 0; i < adj.length; i++){
				changes[i] = false;
			}
			final boolean[] tmp = copy;
			copy = changes;
			changes = tmp;
			sourceIterable.changed = changes;
		} while (changed==true);

		//reachableFrom is converted into a list that contains all possible destinations from node[index]
		LinkedList<Integer>[] reachables = new LinkedList[adj.length];
		for (int i = 0; i < adj.length; i++){
			reachables[i] = new LinkedList<Integer>();
			for (int k = 0; k < reachableFrom.length; k++){
				if (reachableFrom[k].contains(i)){
					reachables[i].add(k);
				}
			}
		}

		master.shutdown();
		return reachables;
	}

	public static void main(String[] args) {
		int[][] adj = new int[][] {
				{  -1,  1, -1, -1, -1, -1},
				{  1, -1,  1, -1, -1, -1},
				{  -1, -1, -1,  1, -1, -1},
				{  -1, -1, -1, -1,  1, -1},
				{  -1, -1, -1, -1, -1,  1},
				{  -1, -1, -1, -1, -1, -1}};
		Closure test = new Closure();
		LinkedList<Integer>[] res = test.closure(adj); //compute closure

		//Small test
		int[][] reference = {{0,1,2,3,4,5},{0,1,2,3,4,5},{3,4,5},{4,5},{5},{}};
		for (int i = 0; i < adj.length; i++){
			if (reference[i]==null){
				if (res[i].size()!=0) {
					return;
				}
			} else {
				if (res[i].size()!=reference[i].length){
					return;
				}
				for (int j = 0; j < reference[i].length; j++){
					if (!res[i].contains(reference[i][j])){
						return;
					}
				}
			}
		}
		System.out.println("Success!");
	}

}
