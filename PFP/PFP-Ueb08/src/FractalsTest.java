import java.awt.Color;

import org.junit.Test;


public class FractalsTest {

	@Test
	public void TestMandelbrotSeq1(){
		int x = 500;
		int y = 500;
		long start = System.currentTimeMillis();
		Color[] palette = ColorPalette.createStandardGradient();
		Color[][] array = new Fractals().mandelbrotSeq(x, y,-1.5,-1,0.5,1, palette);
		System.out.println("Mandel 1 seq: "+(System.currentTimeMillis() - start));
		Canvas.show(array, "mandelSeq 1");

	}

	@Test
	public void TestMandelbrotSeq2(){
		int x = 500;
		int y = 500;
		long start = System.currentTimeMillis();
		Color[] palette = ColorPalette.createStandardGradient();
		Color[][] array = new Fractals().mandelbrotSeq(x, y,-0.87484,0.22884,-0.85084,0.25284,  palette);
		System.out.println("Mandel 2 seq: "+(System.currentTimeMillis() - start));
		Canvas.show(array, "mandelSeq 2");

	}

	@Test
	public void TestMandelbrotPar1(){
		int x = 500;
		int y = 500;
		long start = System.currentTimeMillis();
		Color[] palette = ColorPalette.createStandardGradient();
		Color[][] array = new Fractals().mandelbrotPar(x, y,-1.5,-1,0.5,1, palette,4);
		System.out.println("Mandel 1 par: "+(System.currentTimeMillis() - start));
		Canvas.show(array, "mandelPar 1");

	}

	@Test
	public void TestMandelbrotPar2(){
		int x = 500;
		int y = 500;
		long start = System.currentTimeMillis();
		Color[] palette = ColorPalette.createStandardGradient();
		Color[][] array = new Fractals().mandelbrotPar(x, y,-0.87484,0.22884,-0.85084,0.25284,  palette,4);
		System.out.println("Mandel 2 par: "+(System.currentTimeMillis() - start));
		Canvas.show(array, "mandelPar 2");

	}



	@Test
	public void TestJuliaSeqLarge1(){
		int x = 1500;
		int y = 1500;
		long start = System.currentTimeMillis();
		Color[] palette = ColorPalette.createStandardGradient();
		Color[][] array = new Fractals().juliaPar(x, y, palette,-1,-1,1,1,1,new Complex(-0.81,-0.177));        
		System.out.println("Julia 1 Seq: "+(System.currentTimeMillis() - start));
		Canvas.show(array, "Julia 1 Seq");

	}

	@Test
	public void TestJuliaParLarge1(){
		int x = 1500;
		int y = 1500;
		long start = System.currentTimeMillis();
		Color[] palette = ColorPalette.createStandardGradient();
		Color[][] array = new Fractals().juliaPar(x, y, palette,-1,-1,1,1,4,new Complex(-0.81,-0.177));        
		System.out.println("Julia 1: "+(System.currentTimeMillis() - start));
		Canvas.show(array, "Julia 1");

	}
	
	@Test
	public void TestJuliaParSmall1(){
		int x = 500;
		int y = 500;
		long start = System.currentTimeMillis();
		Color[] palette = ColorPalette.createStandardGradient();
		Color[][] array = new Fractals().juliaPar(x, y, palette,-1,-1,1,1,4,new Complex(-0.81,-0.177));        
		System.out.println("Julia 1: "+(System.currentTimeMillis() - start));
		Canvas.show(array, "Julia 1");

	}
	
	@Test
	public void TestJuliaPar2(){
		int x = 500;
		int y = 500;
		long start = System.currentTimeMillis();
		Color[] palette = ColorPalette.createStandardGradient();
		Color[][] array = new Fractals().juliaPar(x, y, palette,-1,-1,1,1,4,new Complex(-0.8,0.156));
		System.out.println("Julia 2: "+(System.currentTimeMillis() - start));
		Canvas.show(array, "Julia 2");
		try {
			Thread.sleep(10000);

		} catch (Exception e){

		}
	}
}
