
public class SequentialGaussSolver implements GaussEliminationSolver {

	@Override
	public double[] solveEquations(double[][] A, double[] b, int numberOfThreads) {
		
		double[] ergebnis;
		
		// Vorwärtselimination:
		vorwaertselimination(A,b,numberOfThreads);
		// Rückwärtselimination:
		ergebnis = rueckwaertselimination(A,b,numberOfThreads);
		
		return ergebnis;
	}

	private void vorwaertselimination(double[][] A, double[] b, int numberOfThreads) {
		
		double pivot;
		
		// Spalten:
		for (int y = 0; y < A.length; y++) {
			
			// Pivotelement:
			pivot = A[y][y];
			
			if(pivot != 1) {
				// Zeile normalisieren
				b[y] /= pivot;
				for (int x = 0; x < A.length; x++) {
					A[y][x] /= pivot;
					// System.out.println("A[y][x]: " + A[y][x] +" y: "+ y + " x: " + x);
				}
				// Pivot dann auf 1 setzen:
				pivot = A[y][y];
			}
				
			// Werte unter Pivotelement > Setzen auf 0, Rest mit elem multiplizieren
			for (int yy = y + 1; yy < A.length; yy++) {
				
				double elem = A[yy][y];
				// Ergebnis mit Faktor multiplizieren
				b[yy] += (-(elem) * b[y]);
				for (int xx = 0; xx < A[yy].length; xx++) {
					A[yy][xx] += -(elem) * (A[y][xx]);	// Zeile zu Produkt aus Pivot und negativem Zeilenwert teilen)

					//System.out.println("Zeile "+ yy + " Spalte " + xx + " Ergebnis " + A[yy][xx]);
				}	
			}
			
			if(y > 0) {
				// Werte, die ueber dem Pivot Element stehen auf 0 bringen
				for(int k = y-1; 0 <= k; k--) {
					
					double elem = -A[k][y];
					b[k] += (-(elem) * b[y]);
					
					for(int kk = 0; kk < A[k].length; kk++) {
						A[k][kk] += (-(elem) * A[y][kk]);
					}
				}
			}
		}
	}
	
	private double[] rueckwaertselimination(double[][] A, double[] b, int numberOfThreads) {
        // Rücksubstitution:
		double[] ergebnis = new double[A.length];
        for (int y = 0; y < A.length; y++) {
                ergebnis[y] = b[y];
            }
        return ergebnis;
	}

	
}
