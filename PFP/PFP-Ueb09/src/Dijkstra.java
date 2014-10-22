
import mapreduce.master.ExecutorMaster;
import mapreduce.master.Master;
import mapreduce.stream.MRPair;

public class Dijkstra {

	/**
	 * 
	 * Computes the average of the minimal distance from one node to all other nodes in the graph
	 *  
	 * @param adj The adjacency matrix of the graph
	 * @param start The index of the examined node
	 * @return The average value
	 */
	public double average(int[][] adj, int start) {
			//TODO
			return -1;
	}
	
	/**
	 * 
	 * Computes the shortest paths in a graph
	 *  
	 * @param adj The adjacency matrix of the graph
	 * @param start The index of the start node of the algorithm
	 * @return Array with the shortest paths from the start node to the other nodes
	 */
	public int[] dijkstra(int[][] adj, int start){
		// Ergebnisarray in der Länge der Adj-Matrix:
		int[] result = new int[adj.length];
		
		
		// hier Iterator aufrufen!
			// solange bis sich nichts mehr ändert!
		
		//hier map aufrufen!
		
		
		// hier reduce aufrufen!
		
		// Ergebnis hier zurückgeben!
		return null;
	}
	
	public static void main(String[] args) {
		final int[][] adj = new int[][] {
				{  0,  7,  1,  8, -1, -1, -1, -1, -1 },
				{  7,  0, -1, -1, -1, -1, 13, -1, -1 },
				{  1, -1,  0,  4, -1,  6, 18, 16, -1 },
				{  8, -1,  4,  0,  9, -1, -1, -1, -1 },
				{ -1, -1, -1,  9,  0, -1, -1,  2, -1 },
				{ -1, -1,  6, -1, -1,  0,  5, -1,  3 },
				{ -1, 13, 18, -1, -1,  5,  0, -1, 11 },
				{ -1, -1, 16, -1,  2, -1, -1,  0, 10 },
				{ -1, -1, -1, -1, -1,  3, 11, 10, 0 } };
		DijkstraCleanroom d = new DijkstraCleanroom();
		int[][] res = new int[adj.length][];
		double[] averages = new double[adj.length];
		for ( int i = 0; i < adj.length; i++){
			res[i] = d.dijkstra(adj, i);
		}
		for ( int i = 0; i < adj.length; i++){
			averages[i] = d.average(adj, i);
		}
		for (int i = 0; i <res.length;i++){
			double sum = 0;
			System.out.println((String.valueOf((char)(65+i)))+": ");
			for (int j = 0; j < res[i].length;j++){
				sum += res[i][j];
				System.out.print("("+(String.valueOf((char)(65+j)))+", "+res[i][j]+")");
			}
			sum = sum /res[i].length;
			System.out.println();
			System.out.println("Summe: "+sum);
			System.out.println();
		}
	}
	
	
}
