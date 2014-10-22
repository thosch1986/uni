

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

public class WordIndexTest {
	
	private HashMap<String, ArrayList<Integer>> lines = null;

	
	
	/**
	 * Text loading and index creation with the WordIndex class
	 * Down and Out in the Magic Kingdom is a book by Cory Doctorow (http://craphound.com/down/download.php)
	 * released under CC BY-NC-SA 1.0 ( http://creativecommons.org/licenses/by-nc-sa/1.0/ )
	 */
	@Before
	public void before() {
		WordIndex wic = new WordIndex();
		File f = new File("./lib/Cory_Doctorow_-_Down_and_Out_in_the_Magic_Kingdom.txt");
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			StringBuffer result = new StringBuffer();
			while ((line = br.readLine()) != null) {
				result.append(line);
				result.append("\n");
			}
			br.close();
			String text = result.toString();
			lines = wic.getLines(text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Empty Strings should not appear in the list
	 */
	@Test
	public void emptyString() {
		assertTrue(lines.get("")==null);
		assertTrue(lines.get(null)==null);
		assertTrue(lines.get(" ")==null);
	}
	
	/**
	 * 	Test for a word in the first line
	 */
	@Test
	public void firstLine() {
		ArrayList<Integer> downList = lines.get("Down");
		assertTrue(downList!=null);
		assertTrue(downList.size()==11);
		assertEquals(1, (int)downList.get(0));
	}
	
	/**
	 * 	Test for a word in the last line
	 */
	@Test
	public void lastLine() {
		ArrayList<Integer> downList = lines.get("eof");
		assertTrue(downList!=null);
		assertTrue(downList.size()==1);
		assertEquals(3475, (int)downList.get(0));
	}
	
	/**
	 * 	Simple test for a word appearing once in the text
	 */
	@Test
	public void testPlague() {
		ArrayList<Integer> plaguesList = lines.get("plagues");
		assertTrue(plaguesList!=null);
		assertTrue(plaguesList.size()==1);
		assertEquals(1995, (int)plaguesList.get(0));
	}
	
	
	/**
	 * 	No results for words not found in the text.
	 *	The hashmap key is case sensitive.
	 */
	@Test
	public void testGloom() {
		ArrayList<Integer> plaguesList = lines.get("Gloom");
		assertTrue(plaguesList==null);
	}
	
	
	/**
	 *	Tests the appearances of body
	 *	Multiple lines (1145) are ignored, the list is sorted in ascending order 
	 */
	@Test
	public void testBody() {
		int[] bodyLines = {767, 891, 1013, 1021, 1145, 1169, 1289, 1351, 1467, 1963, 1975, 2151, 2153, 2181, 2255, 2661, 2989};
		ArrayList<Integer> bodyList = lines.get("body");
		assertTrue(bodyList!=null);
		assertEquals(bodyList.size(),bodyLines.length);
		for (int i = 0; i < bodyLines.length; i ++) {
			assertEquals((int)bodyLines[i], (int)bodyList.get(i));
		}
	}
}
