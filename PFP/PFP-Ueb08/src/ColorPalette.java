/**
 * created 09.04.2008
 */
import java.awt.Color;

/**
 * @author Silvia Schreier<sisaschr@stud.informatik.uni-erlangen.de>
 * @author Georg Dotzler<georg.dotzler@informatik.uni-erlangen.de>
 */
public class ColorPalette {

	/**
	 * Creates a color array with a gradient
	 * 
	 * @param n
	 * 			number of generated colors
	 * @param colors
	 *          the colors at the border of the gradient
	 * @return the color array
	 */
	public static Color[] create(int n, Color[] colors) {
		Color[] palette = new Color[n];
		int stepWidth = (n - 1) / (colors.length - 1);
		int start = 0;
		for (int i = 0; i < colors.length - 1; i++) {
			if (i == colors.length - 2) {
				stepWidth = palette.length - start;
			}
			Color startColor = colors[i];
			Color endColor = colors[i + 1];
			palette[start] = startColor;
			double r = startColor.getRed();
			double g = startColor.getGreen();
			double b = startColor.getBlue();
			double rSchritt = ((double) (endColor.getRed() - startColor
					.getRed()))
					/ stepWidth;
			double gSchritt = ((double) (endColor.getGreen() - startColor
					.getGreen()))
					/ stepWidth;
			double bSchritt = ((double) (endColor.getBlue() - startColor
					.getBlue()))
					/ stepWidth;
			for (int j = 1; j < stepWidth; j++) {
				r += rSchritt;
				g += gSchritt;
				b += bSchritt;
				palette[start + j] = new Color((int) r, (int) g, (int) b);
			}
			start += stepWidth;
		}
		palette[palette.length - 1] = colors[colors.length - 1];
		return palette;
	}

	/**
	 * 
	 * Creates a color gradient with 200 colors from white over blue to black
	 * 
	 * @return a standard color gradient 
	 */
	public static Color[] createStandardGradient() {
		return create(200,
				new Color[] { Color.WHITE, Color.BLUE, Color.BLACK });
	}

}
