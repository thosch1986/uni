/**
 * 
 * Interface for the Levenshtein distance
 * 
 * @author Georg Dotzler<georg.dotzler@cs.fau.de>
 *
 */

public interface Levenshtein {


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
	public int computeValue(int row, int column, int[][] table, char[] wordHorizontal, char[] wordVertical);
	
	
	/**
	 * 
	 * Returns the matrix that represents the Levenshtein distances between
	 * all prefixes of the two words. The Levenshtein distance between both words is found
	 * at matrix[wordVertical.length()-1][wordHorizontal.length()-1].
	 * @param wordHorizontal second word, used horizontal in the matrix
	 * @param wordVertical first word, used vertical in the matrix
	 * @return
	 */
	public int[][] computeLevenshteinSeq(char[] wordHorizontal, char[] wordVertical);
	
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
	public int[][] computeLevenshteinQueue(char[] wordHorizontal, char[] wordVertical,  int numberOfThreads);
	
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
	public int[][] computeLevenshteinExchanger(char[] wordHorizontal, char[] wordVertical,  int numberOfThreads);

}
