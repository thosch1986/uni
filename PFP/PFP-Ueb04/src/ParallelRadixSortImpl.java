import java.util.ArrayList;
import java.util.Arrays;

public class ParallelRadixSortImpl implements ParallelRadixSort {

	/**
	 * Returns the bucket number for the n-th char, where the most significant char has index 0.
	 * Example: <code>getBucket("gfedcba", 2) == 4</code>.
	 * Example: <code>getBucket("bcdefgh", 2) == 3</code>.
	 * Example: <code>getBucket("a", 2) == 0</code>.
	 */
	public int getBucket(String string, int n) {
		
		int ergebnis = 0;
		// hier
		if (string.length() < n) {
			return ergebnis;
		} else {
			// gehe ueber den String bis zur angegebenen Stelle n
			// lese das Zeichen an Stelle n aus und speichere es als Char.
			char exzerpt = string.charAt(n); 
			ergebnis = (int) exzerpt;
			
			ergebnis = ergebnis - 97;
			
			return ergebnis;
		}
	}

	/**
	 * Sorts an array of strings by looking at the n-th char in every string.
	 * Again, the most significant char has index 0.
	 * 
	 * @param threads Number of threads to be used
	 */
	public void sortByPos(String array[], int n, int threads) {
		@SuppressWarnings("unchecked")
		ArrayList<String> buckets[] = (ArrayList<String>[]) new ArrayList[26];
		
		// Hier die Threads aufrufen.
		int ueberhang = 0;
		int intervall = 0; // Groesse des Intervalls der abzuarbeitenden Threads
		int intervallAnfangsZaehler = 0;
		int intervallEndeZaehler = 0;
		
		ueberhang = array.length % threads;
		intervall = array.length / threads + ueberhang / threads;
		
		intervallEndeZaehler = intervall;
		
		intervallAnfangsZaehler = intervallAnfangsZaehler + intervall;
		intervallEndeZaehler = intervallEndeZaehler + intervall;
		

			
		
		// Speicher fuer kommende Threads anlegen!
		Thread[] t = new Thread[threads];
		
		// Threads mit "Arbeitspaketen" befuellen.
		for(int i=0; i < threads; i++) {
			for (int j=0; j < array.length; j++) {
				
				// bekomme Bucket zu String Wert:
				int bucketNr = getBucket(array[j], n);
				
				// Speichere Stringinhalt des Arrays an Stelle i in die ArrayList im Bucket "Bucket Nr"
				// keine Ahnung wie das geht.
				// buckets[bucketNr] = new ArrayList();
				buckets[bucketNr].add(array[j]);
		}
		}
		
		// Threads starten
		for (int i=0; i < threads; i++) {
			t[i].start();
		}		
		
		// Threads joinen
		for (int i=0; i < threads; i++) {
			
			try {
				t[i].join();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}	
			
	}

	/**
	 * Sorts an array of strings using radix-sort with multiple threads.
	 * 
	 * @param array to be sorted
	 * @param maxLength Maximal length of the strings in the array
	 * @param threads Number of threads to be used
	 */
	public void radixSort(String array[], int maxLength, int threads) {

			sortByPos(array, maxLength, threads);
		
	}

	/**
	 * Free for editing
	 */
	public static void main(String[] args) {
		final String[] example = new String[] {
				"zzzzz", "a", "jjkpr", "byddi", "gsbyk", "ricsn", "cjtmt", "tkclc", "xzdaz", "qtzjn", "zcltf", 
				"ixawn", "efdcx", "bkwpr", "tdovt", "ental", "tmyfg", "pfqjt", "oeclt", "rywkl", "wjtfq", 
				"fraru", "cqtwd", "jyzhe", "bxfeu", "zojdr", "qyriu", "imcfh", "gwkpw", "tgrlt", "oxpgv", 
				"pfvpe", "kselt", "tbylv", "ayogf", "sogji", "kkdco", "khfon", "jahiu", "hvuzj", "fcdty", 
				"qjiuu", "cxmxq", "okrwg", "aoiti", "wemdq", "muiqt", "uzrsj", "yjtrr", "wuelv", "wbgpr", 
				"ddrmk", "zcrse", "dbduh", "iaxxa", "ksypm", "pxnqy", "jzaqm", "xzyle", "zovqe", "mogfc", 
				"pxscw", "bfcfe", "ab", "znlmw", "xhlen", "fujlr", "eapam", "ljkgk", "mmbpx", "jtndr", 
				"bgqja", "foyso", "brepp", "vebxa", "fasay", "ainyh", "xkkkr", "fcrkg", "yfzos", "qvsxb", 
				"xphpf", "zfqgl", "rmpwg", "xkcd", "pxwbz", "nhyft", "nhexl", "wwdsn", "gskrr", "fctcn", 
				"bjizn", "csibp", "erdym", "hqgit", "hxpzs", "aujmc", "yekep", "awutf", "adwwu", "roduy", 
				"iyzav", "vypdb", "uqyfe", "mixty", "fbhkf", "zzghm", "kjhod", "nhffy", "snprt", "unnyh", 
				"rsrfh", "ufjhw", "wvjve", "fmewp", "uvfzz", "xlwqu", "qhwih", "jjbdc", "ezioz", "xrmdh", 
				"xbdah", "occjm", "dhnuz", "cqvina", "hpvuh", "fpdje", "wurul"};

		final String[] radixSorted = example.clone();
		final int threads = 4;
		ParallelRadixSortImpl radixSort = new ParallelRadixSortImpl();
		int length = 0;
		for (String s : example) {
			length = Math.max(length, s.length());
		}
		
		radixSort.radixSort(radixSorted, length, threads);

		final String[] javaSorted = example.clone();
		Arrays.sort(javaSorted);

		String ok = "Yeah!";
		if (!Arrays.equals(radixSorted, javaSorted))
			ok = "Argh!";

		System.out.println(ok);
	}
}
