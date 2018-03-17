package com.winter.mayawinterfox.util;

import java.awt.*;
import java.util.Random;

public class ColorUtil {

	/**
	 * Get a random color in between the hues
	 *
	 * @param lower between 0 and 1
	 * @param upper between 0 and 1
	 * @return A color
	 */
	public static Color withinTwoHues(float lower, float upper) {
		return Color.getHSBColor((new Random().nextFloat() * (upper - lower)) + lower, 1.0f, 1.0f);
	}

}
