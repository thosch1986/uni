import java.awt.Color;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

public class Fractals {

	/**
	 * Specifies when the computation is aborted
	 */
	final public static double MAXLENGTH = 2.0;

	/**
	 * Computes a the number of iterations for a Mandelbrot/Julia-set
	 * see ( http://plus.maths.org/issue9/features/mandelbrot/ )
	 * 
	 * @param start
	 *            the initial value of the iteration (z_0)
	 * @param step
	 *            the increment in each iteration (c)
	 * @param maxIter
	 * 			  maximal number of iterations that are processed
	 * @return number of iterations between [0..maxIter]
	 */
	public int computeIterations(Complex start, Complex step, int maxIter) {
		
		int counter = 0;
		Complex z_n = start;

		for (counter = 0; (counter < maxIter) & (z_n.abs() < MAXLENGTH); counter++) {
			z_n = z_n.multiply(z_n).add(step);
		}

		return counter;
	}

	/**
	 * Creates a Julia-Picture in parallel with the size [x,y] using the 
	 * area [realBegin + (imBegin)i, realEnd + (imEnd)i)
	 * 
	 * @param x
	 *            Number of pixels in x direction
	 * @param y
	 *            Number of pixels in y direction
	 * @param palette
	 *            Number of used colors (limits the iteration count)
	 * @param realBegin
	 *            Real begin of the area
	 * @param imBegin
	 *            Imaginary begin of the area
	 * @param realEnd
	 *            Real end of the area
	 * @param imEnd
	 *            Imaginary end of the area
	 * @param threads
	 *            Number of used threads
	 * @param step
	 *            The increment of the Julia-Set 
	 * @return Pixel array of the picture
	 */
	public Color[][] juliaPar(int x, int y,  Color[] palette,  double realBeginn, double imBegin, double realEnd, double imEnd, int threads,
			Complex step) {
		
		double stepX = (Math.abs(realBeginn) + Math.abs(realEnd)) / x;
		double stepY = (Math.abs(imBegin) + Math.abs(imEnd)) / y;
		int maxIter = palette.length;
		Color[][] canvas = new Color[x][y];
		LinkedList<Thread> li = new LinkedList<Thread>();

		/**
		 * Berechnet das Bild zeilenweise
		 */
		int start = 0;
		int range = y / threads;

		for (int i = 0; i < threads; i++) {
			if (i == 0) {
				int firstRange = range + y % threads;
				li.add(new JuliaThread(x, y, start, firstRange, maxIter, stepX,
						stepY, realBeginn, imBegin, realEnd, imEnd, palette,
						canvas, step));
				// System.out.println("thread gestartet, arbeiten von "+ start +
				// " bis "+(firstRange+start));
				start = firstRange;
				continue;

			}

			li.add(new JuliaThread(x, y, start, range, maxIter, stepX,
							stepY, realBeginn, imBegin, realEnd, imEnd,
							palette, canvas, step));
			// System.out.println("thread gestartet, arbeiten von "+ start +
			// " bis "+(range+start));
			start += range;
		}

		for (Thread t : li) {
			t.start();
		}

		for (Thread t : li) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return canvas;
	}
	
	private class JuliaThread extends Thread {
		private int x;
		private int y;
		private int start;
		private int range;
		private int maxIter;
		private double stepX;
		private double stepY;
		private double realBegin;
		private double imBegin;
		private double realEnd;
		private double imEnd;
		private Color[] palette;
		private Color[][] canvas;
		private Complex step;

		public JuliaThread(int x, int y, int start, int range, int maxIter,
				double stepX, double stepY, double realBegin, double imBegin,
				double realEnd, double imEnd, Color[] palette,
				Color[][] canvas, Complex step) {
			this.x = x;
			this.y = y;
			this.start = start;
			this.range = range;
			this.maxIter = maxIter;
			this.stepX = stepX;
			this.stepY = stepY;
			this.realBegin = realBegin;
			this.imBegin = imBegin;
			this.realEnd = realEnd;
			this.imEnd = imEnd;
			this.canvas = canvas;
			this.palette = palette;
			this.step = step;
		}

		public void run() {
			try {
				this.runEx();
			} catch (Exception e) {

			}
		}

		public void runEx() throws Exception {
			int i;
			int j;
			double valueX = realBegin;
			double valueY = imEnd - (stepY * start);
			for (i = 0; i < x; i++) {
				for (j = start; j < start + range; j++) {
					canvas[i][j] = palette[this.computeIterations(new Complex(
							valueX, valueY), step, maxIter) - 1];
					valueY -= stepY;
				}
				valueX += stepX;
				valueY = imEnd - (stepY * start);
			}
		}

		private int computeIterations(Complex start, Complex step, int maxIter) {

			int count = 0;
			Complex z_n = start;

			for (count = 0; (count < maxIter) & (z_n.abs() < MAXLENGTH); count++)
				z_n = z_n.multiply(z_n).add(step);

			return count;
		}

	}
	
	public Color[][] juliaSeq(int x, int y, Color[] palette, double realBeginn,
			double imBegin, double realEnd, double imEnd, int threads,
			Complex step) { // computeIterations(new Complex(valueX,valueY),
		// step, maxiter);
		double stepX = (Math.abs(realBeginn) + Math.abs(realEnd)) / x;
		double stepY = (Math.abs(imBegin) + Math.abs(imEnd)) / y;
		// System.out.println("stepX :"+stepX + " stepY :"+stepY); //RICHTIG
		int maxiter = palette.length;/* 200 */// System.out.println("laenge platte: "
		// +palette.length);
		double valueX = realBeginn;// realBegin;
		double valueY = imEnd;// imBegin;
		int itValue = 0;
		Color[][] canvas = new Color[x][y];
		int i;
		int j;

		for (i = 0; i < x; i++) {
			for (j = 0; j < y; j++) {
				itValue = computeIterations(new Complex(valueX, valueY), step,
						maxiter);
				canvas[i][j] = palette[itValue - 1];
				valueY -= stepY;
			}
			valueX += stepX;
			valueY = imEnd;
		}

		return canvas;

	}

	/**
	 * Creates a Mandelbrot-Picture in parallel with the size [x,y] using the 
	 * area [realBegin + (imBegin)i, realEnd + (imEnd)i)
	 * 
	 * @param x
	 *            Number of pixels in x direction
	 * @param y
	 *            Number of pixels in y direction
	 * @param realBegin
	 *            Real begin of the area
	 * @param imBegin
	 *            Imaginary begin of the area
	 * @param realEnd
	 *            Real end of the area
	 * @param imEnd
	 *            Imaginary end of the area
	 * @param palette
	 *            Number of used colors (limits the iteration count)
	 * @param threads
	 *            Number of used threads
	 * @return Pixel array of the picture
	 */
	public Color[][] mandelbrotPar(int x, int y, double realBegin, double imBegin, double realEnd, double imEnd, Color[] palette, int threads) {
		
		double stepX = (Math.abs(realBegin) + Math.abs(realEnd)) / x;
		double stepY = (Math.abs(imBegin) + Math.abs(imEnd)) / y;
		int maxIter = palette.length;
		Color[][] canvas = new Color[x][y];
		LinkedList<Thread> li = new LinkedList<Thread>();
		CountDownLatch cdl = new CountDownLatch(threads);
		/**
		 * Berechnet das Bild spaltenweise
		 */
		int start = 0;
		int range = x / threads;

		for (int i = 0; i < threads; i++) {
			if (i == 0) {
				int firstRange = range + x % threads;
				li.add(new MandelThread(x, y, start, firstRange, maxIter,
						stepX, stepY, realBegin, imBegin, realEnd, imEnd,
						palette, canvas, cdl));
				start = firstRange;
				continue;
			}

			li.add(new MandelThread(x, y, start, range, maxIter, stepX, stepY,
					realBegin, imBegin, realEnd, imEnd, palette, canvas, cdl));
			start += range;
		}

		for (Thread t : li) {
			t.start();
		}

		try {
			cdl.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return canvas;
	}
	
	private class MandelThread extends Thread {
		private int x;
		private int y;
		private int start;
		private int range;
		private int maxIter;
		private double stepX;
		private double stepY;
		private double realBegin;
		private double imBegin;
		private double realEnd;
		private double imEnd;
		private Color[] palette;
		private Color[][] canvas;
		private CountDownLatch cdl;

		public MandelThread(int x, int y, int start, int range, int maxIter,
				double stepX, double stepY, double realBegin, double imBegin,
				double realEnd, double imEnd, Color[] palette,
				Color[][] canvas, CountDownLatch cdl) {
			this.x = x;
			this.y = y;
			this.start = start;
			this.range = range;
			this.maxIter = maxIter;
			this.stepX = stepX;
			this.stepY = stepY;
			this.realBegin = realBegin;
			this.imBegin = imBegin;
			this.realEnd = realEnd;
			this.imEnd = imEnd;
			this.canvas = canvas;
			this.palette = palette;
			this.cdl = cdl;
		}

		public void run() {
			try {
				this.runEx();
			} catch (Exception e) {

			}
		}

		public void runEx() throws Exception {
			int i;
			int j;
			double valueX = realBegin + (stepX * start);
			double valueY = imEnd;
			for (i = start; i < start + range; i++) {
				for (j = 0; j < y; j++) {
					canvas[i][j] = palette[this.computeIterations(
							new Complex(0), new Complex(valueX, valueY),
							maxIter) - 1];
					valueY -= stepY;
				}
				valueX += stepX;
				valueY = imEnd;
			}
			cdl.countDown();
		}

		private int computeIterations(Complex start, Complex step, int maxIter) {

			int count = 0;
			Complex z_n = start;

			for (count = 0; (count < maxIter) & (z_n.abs() < MAXLENGTH); count++)
				z_n = z_n.multiply(z_n).add(step);

			return count;
		}

	}

	/**
	 * Creates a Mandelbrot-Picture with the size [x,y] using the 
	 * area [realBegin + (imBegin)i, realEnd + (imEnd)i)
	 * 
	 * @param x
	 *            Number of pixels in x direction
	 * @param y
	 *            Number of pixels in y direction
	 * @param realBegin
	 *            Real begin of the area
	 * @param imBegin
	 *            Imaginary begin of the area
	 * @param realEnd
	 *            Real end of the area
	 * @param imEnd
	 *            Imaginary end of the area
	 * @param palette
	 *            Number of used colors (limits the iteration count)
	 * @param threads
	 *            Number of used threads
	 * @return Pixel array of the picture
	 */
	public Color[][] mandelbrotSeq(int x, int y, double realBegin, double imBegin, double realEnd, double imEnd, Color[] palette) {
		
		double stepX = (Math.abs(realBegin) + Math.abs(realEnd)) / x;
		double stepY = (Math.abs(imBegin) + Math.abs(imEnd)) / y;
		// System.out.println("stepX :"+stepX + " stepY :"+stepY); //RICHTIG
		int maxiter = palette.length;/* 200 */// System.out.println("laenge platte: "
		// +palette.length);
		double valueX = realBegin;// realBegin;
		double valueY = imEnd;// imBegin;
		int itValue = 0;
		Color[][] canvas = new Color[x][y];
		int i;
		int j;
		// System.out.println("Start valueX :"+valueX + " valueY :"+valueY);
		for (i = 0; i < x; i++) {
			for (j = 0; j < y; j++) {
				itValue = computeIterations(new Complex(0), new Complex(valueX,
						valueY), maxiter);
				// System.out.println("compute: "+itValue);
				canvas[i][j] = palette[itValue - 1];

				valueY -= stepY;

			}

			/*
			 * if(i == x-2) System.out.println("Ende valueX :"+valueX +
			 * " valueY :"+valueY);
			 */
			valueX += stepX;
			valueY = imEnd;
		}

		return canvas;
	}
	
	public static void main(String[] args) {
		int x = 500;
		int y = 500;
		Color[] palette = ColorPalette.createStandardGradient();
		Color[][] array = new Fractals().mandelbrotSeq(x, y,-1.5,-1,0.5,1, palette);
		Canvas.show(array, "mandelSeq");
	}
}
