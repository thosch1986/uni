import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import mapreduce.master.ExecutorMaster;
import mapreduce.master.Master;
import mapreduce.stream.MRPair;

/**
 * 
 * Analyzes a text to get the line numbers for each word
 * 
 * @author Georg Dotzler<georg.dotzler@cs.fau.de>
 *
 */
public class WordIndex {
	
		/**
		 * 
		 * Computes a HashMap that returns a list of line numbers for each word that appears in the text
		 *  
		 * @param text the text to be analyzed
		 * @return the keys of the hashmap are the words of the text, the values are the ordered lists of line numbers
		 */
		public HashMap<String, ArrayList<Integer>> getLines(String text){
			//TODO
			return null;
		}
		
		public static void main(String[] args) {
			String text = "Sed ut perspiciatis unde omnis iste natus \n"+
						  "error sit voluptatem accusantium doloremque laudantium,\n" +
						  "totam rem aperiam, eaque ipsa quae ab illo inventore veritatis\n" +
						  "et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam\n" +
						  "voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia\n" +
						  "consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.\n" +
						  "Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur,\n" +
						  "adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et\n" +
						  "dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis\n" +
						  "nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid\n" +
						  "ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in\n" +
						  "ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui\n" +
						  "dolorem eum fugiat quo voluptas nulla pariatur?\n";
			
			WordIndexCleanroom wic = new WordIndexCleanroom();
			HashMap<String, ArrayList<Integer>> hashMap = wic.getLines(text);
			
			ArrayList<Integer> ipsum = hashMap.get("ipsum");
			if (ipsum!=null&&ipsum.size()==1&&ipsum.get(0)==7) {
				System.out.println("Veni, vidi, vici!");
			}
		}

}
