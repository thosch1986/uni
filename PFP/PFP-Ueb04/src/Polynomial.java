


/**
 * @author Marc Woerlein<woerlein@informatik.uni-erlangen.de>
 * @author Georg Dotzler<georg.dotzler@informatik.uni-erlangen.de>
 * 
 */
public class Polynomial implements Function {

	/**
	 * @uml.property  name="coefficients" multiplicity="(0 -1)" dimension="1"
	 */
	final private double[] coefficients;

	/**
	 * Creates a new Polynomial with the specified coefficients
	 * 
	 * @param coefficients
	 */
	public Polynomial(double[] coefficients) {
		this.coefficients = coefficients;
	}

	@Override
	public double compute(double x) {
		double sum = 0;
		for (int i = 0; i < coefficients.length; i++) {
			double prod = coefficients[i];
			for (int j = 0; j < i; j++) {
				prod *= x;
			}
			sum += prod;
		}
		return sum;
	}

	/**
	 * Creates a random polynomial in the given degree
	 * 
	 * @param degree
	 * @return a random polynomial
	 */
	public static Polynomial randomPolynomial(int degree) {
		final double[] coefficients = new double[degree + 1];
		for (int i = 0; i <= degree; i++) {
			coefficients[i] = Math.random() * 100;
			if (Math.random() > 0.5) {
				coefficients[i] *= -1;
			}
		}
		return new Polynomial(coefficients);
	}

}
