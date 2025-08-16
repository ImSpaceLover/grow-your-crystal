package io.github.imspacelover.growyourcrystal.util;

import net.minecraft.util.DyeColor;
import net.minecraft.util.math.ColorHelper;

import java.util.List;

public class ColorUtils {

	public static final int DEFAULT_COLOR = getCrystalColor(DyeColor.WHITE);

	private static List<Integer> DARK_COLORS = List.of(
		DyeColor.BLACK.getEntityColor(),
		DyeColor.GRAY.getEntityColor(),
		DyeColor.LIGHT_GRAY.getEntityColor()
	);

	public static int mixColors(List<Integer> colors) {
		int redSum = 0;
		int greenSum = 0;
		int blueSum = 0;
		int lightness = 0;
		int colorCount = 0;
		for (Integer color : colors) {
			int red = ColorHelper.getRed(color);
			int green = ColorHelper.getGreen(color);
			int blue = ColorHelper.getBlue(color);
			lightness += Math.max(red, Math.max(green, blue));
			redSum += red;
			greenSum += green;
			blueSum += blue;
			colorCount++;
		}

		int redResult = redSum / colorCount;
		int greenResult = greenSum / colorCount;
		int blueResult = blueSum / colorCount;
		float lightnessSum = (float)lightness / colorCount;
		float lightnessResult = Math.max(redResult, Math.max(greenResult, blueResult));
		redResult = (int)(redResult * lightnessSum / lightnessResult);
		greenResult = (int)(greenResult * lightnessSum / lightnessResult);
		blueResult = (int)(blueResult * lightnessSum / lightnessResult);
		int colorResult = ColorHelper.getArgb(255, redResult, greenResult, blueResult);
		return colorResult;
	}

	public static int getCrystalColor(DyeColor color) {
		return mixColors(List.of(color.getEntityColor()));
	}

	public static int getTweakedColor(int color) {
//		return DARK_COLORS.contains(color) ? ColorHelper.withBrightness(color, 0.5f) : ColorHelper.withBrightness(color, 1f);
//		return ColorHelper.withBrightness(color, 0.7f);
		return color;
	}
}
