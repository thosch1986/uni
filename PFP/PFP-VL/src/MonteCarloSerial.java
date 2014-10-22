import java.util.Random;


public class MonteCarloSerial {

	public static void main(String[] args) {
		
		int pts = Integer.MAX_VALUE / 32;
		int cnt = 0;
		long start = System.currentTimeMillis();	// Zeitmessung Start
		
		Random rnd = new Random(System.currentTimeMillis());
		for(int i = 0; i < pts; i++) {
			// Platziere zufaellig Punkte im Quadrat (0,0) bis (1,1) und zaehle.
			double x = rnd.nextDouble();
			double y = rnd.nextDouble();
			double d = x*x + y*y;
			if (d<= 1.0) cnt++;
		}
		
		long end = System.currentTimeMillis();		// Zeitmessung Ende
		
		// Berechne Verhaeltnis von Punktanzahl im Viertelkreis und ausserhalb
		System.out.println("pi: " + (((double) cnt)/pts) * 4);
		System.out.println("took " + (end - start) + " ms");
	}
}
