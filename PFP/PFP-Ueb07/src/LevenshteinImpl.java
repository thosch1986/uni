import java.util.Arrays;


public class LevenshteinImpl implements Levenshtein{

	/**
	 * Computes the value of the table at position [row][column], with 1 <= row < table.length and
	 * 1 <= column < table[0].length
	 * 
	 * 
	 * @param row
	 * @param column
	 * @param table the half filled table with the Levenshtein distances of the prefixes.
	 * @param wordHorizontal the word used horizontal in the comparison
	 * @param wordVertical the word used vertically in the comparison
	 * @return
	 */
	public int computeValue(int row, int column, int[][] table, char[] wordVertical, char[] wordHorizontal) {
		
		int whl = wordVertical.length;
		int wvl = wordHorizontal.length;
		
		char vergleichsCharH;
		char vergleichsCharV;
		
		int cost = 0;	// Kosten
		
		if(whl == 0) {
			return wvl;
		}
		if(wvl == 0) {
			return whl;
		}
		
		// Initialisieren der ersten Zeile und Spalte:
		for(int i=0; i <= whl; i++) {
			table[i][0] = i;
		}
		for(int j=0; j <= wvl; j++) {
			table[0][j] = j;
		}
		
		//
		for (int i = 0; i < whl; i++) {
			vergleichsCharH = wordHorizontal[i];
			for (int j=0; j < wvl; j++) {
				vergleichsCharV = wordVertical[j];
				// Vergleich:
				if (vergleichsCharH == vergleichsCharV) {
					cost = 0;
				} else {
					cost = 1;
				}
				
				table[i][j] = Math.min(table[i-1][j-1] + cost, Math.min(table[i][j-1] + 1, table[i-1][j] + 1));
			}
		}
		
		return table[row][column];
	}

	/**
	 * 
	 * Returns the matrix that represents the Levenshtein distances between
	 * all prefixes of the two words. The Levenshtein distance between both words is found
	 * at matrix[wordVertical.length()-1][wordHorizontal.length()-1].
	 * @param wordHorizontal second word, used horizontal in the matrix
	 * @param wordVertical first word, used vertical in the matrix
	 * @return
	 */
	public int[][] computeLevenshteinSeq(char[] wordVertical, char[] wordHorizontal) {
		
		int whl = wordVertical.length;
		int wvl = wordHorizontal.length;
		
		char vergleichsCharH;
		char vergleichsCharV;
		
		int table[][] = new int[whl][wvl]; //Hilfsmatrix
		
		int cost = 0;	// Kosten
		
		// Initialisieren der ersten Zeile und Spalte:
		for(int i=0; i <= whl; i++) {
			table[i][0] = i;
		}
		for(int j=0; j <= wvl; j++) {
			table[0][j] = j;
		}
		
		//
		for (int i = 0; i < whl; i++) {
			vergleichsCharH = wordHorizontal[i];
			for (int j=0; j < wvl; j++) {
				vergleichsCharV = wordVertical[j];
				// Vergleich:
				if (vergleichsCharH == vergleichsCharV) {
					cost = 0;
				} else {
					cost = 1;
				}
				
				table[i][j] = Math.min(table[i-1][j-1] + cost, Math.min(table[i][j-1] + 1, table[i-1][j] + 1));
			}
		}
		
		return table;
	}

	/**
	 * 
	 * Returns the matrix that represents the Levenshtein distances between
	 * all prefixes of the two words. The Levenshtein distance between both words is found
	 * at matrix[wordVertical.length()-1][wordHorizontal.length()-1].
	 * 
	 * @param wordHorizontal second word, used horizontal in the matrix
	 * @param wordVertical first word, used vertical in the matrix
	 * @param numberOfThreads Number of threads to be used in the pipeline
	 * @return
	 */
	public int[][] computeLevenshteinQueue(char[] wordVertical, char[] wordHorizontal, int numberOfThreads) {
		
		int whl = wordVertical.length;
		int wvl = wordHorizontal.length;
		
		char vergleichsCharH;
		char vergleichsCharV;
		
		int table[][] = new int[whl][wvl]; //Hilfsmatrix
		
		int cost = 0;	// Kosten
		
		// Initialisieren der ersten Zeile und Spalte:
		for(int i=0; i <= whl; i++) {
			table[i][0] = i;
		}
		for(int j=0; j <= wvl; j++) {
			table[0][j] = j;
		}
		
		//
		for (int i = 0; i < whl; i++) {
			vergleichsCharH = wordHorizontal[i];
			for (int j=0; j < wvl; j++) {
				vergleichsCharV = wordVertical[j];
				// Vergleich:
				if (vergleichsCharH == vergleichsCharV) {
					cost = 0;
				} else {
					cost = 1;
				}
				
				table[i][j] = Math.min(table[i-1][j-1] + cost, Math.min(table[i][j-1] + 1, table[i-1][j] + 1));
			}
		}
		
		return table;
	}

	/**
	 * 
	 * Returns the matrix that represents the Levenshtein distances between
	 * all prefixes of the two words. The Levenshtein distance between both words is found
	 * at matrix[wordVertical.length()-1][wordHorizontal.length()-1].
	 * @param wordHorizontal second word, used horizontal in the matrix
	 * @param wordVertical first word, used vertical in the matrix
	 * @param numberOfThreads Number of threads to be used in the pipeline
	 * @return
	 */
	public int[][] computeLevenshteinExchanger(char[] wordVertical, char[] wordHorizontal, int numberOfThreads) {
		
		int whl = wordVertical.length;
		int wvl = wordHorizontal.length;
		
		char vergleichsCharH;
		char vergleichsCharV;
		
		int table[][] = new int[whl][wvl]; //Hilfsmatrix
		
		int cost = 0;	// Kosten
		
		// Initialisieren der ersten Zeile und Spalte:
		for(int i=0; i <= whl; i++) {
			table[i][0] = i;
		}
		for(int j=0; j <= wvl; j++) {
			table[0][j] = j;
		}
		
		//
		for (int i = 0; i < whl; i++) {
			vergleichsCharH = wordHorizontal[i];
			for (int j=0; j < wvl; j++) {
				vergleichsCharV = wordVertical[j];
				// Vergleich:
				if (vergleichsCharH == vergleichsCharV) {
					cost = 0;
				} else {
					cost = 1;
				}
				
				table[i][j] = Math.min(table[i-1][j-1] + cost, Math.min(table[i][j-1] + 1, table[i-1][j] + 1));
			}
		}
		
		return table;
		
	}

	private static void print(int[][] table) {
		for (int[] line : table) {
			System.out.println(Arrays.toString(line));
		}
	}
	
	public static void main(String[] args) throws Exception {
		Levenshtein k = new LevenshteinImpl();
		int[][] tableSeq = (k.computeLevenshteinSeq("Tor".toCharArray(),"Tier".toCharArray()));
		int[][] tableQueue = (k.computeLevenshteinQueue("Tor".toCharArray(),"Tier".toCharArray(),4));
		int[][] tableExchanger = (k.computeLevenshteinQueue("Tor".toCharArray(),"Tier".toCharArray(),4));
		print(tableSeq);
		print(tableQueue);
		print(tableExchanger);
	}

}

