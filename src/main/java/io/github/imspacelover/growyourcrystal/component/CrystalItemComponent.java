package io.github.imspacelover.growyourcrystal.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ColorHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public record CrystalItemComponent(List<Integer> colors, int lightLevel, int redstoneStrength, int foodNutrition) implements TooltipAppender {

	public static final int DEFAULT_COLOR = DyeColor.WHITE.getEntityColor();
	public static final FoodComponent DEFAULT_FOOD = new FoodComponent(0, 0, false);

	public static final CrystalItemComponent DEFAULT = new CrystalItemComponent(new ArrayList<>(), 0, 0,  0);

	public static final Codec<CrystalItemComponent> CODEC = RecordCodecBuilder.create(builder -> {
		return builder.group(
			Codec.INT.listOf().optionalFieldOf("colors", List.of(DEFAULT_COLOR)).forGetter(CrystalItemComponent::colors),
			Codec.INT.optionalFieldOf("lightLevel", 0).forGetter(CrystalItemComponent::lightLevel),
			Codec.INT.optionalFieldOf("redstoneStrength", 0).forGetter(CrystalItemComponent::redstoneStrength),
			Codec.INT.optionalFieldOf("foodNutrition", 0).forGetter(CrystalItemComponent::foodNutrition)
		).apply(builder, CrystalItemComponent::new);
	});

	@Override
	public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
		textConsumer.accept(Text.translatable("item.tooltip.grow_your_crystal.crystal_component.colors", toHexColorList(colors)).formatted(Formatting.GRAY));
		if (lightLevel > 0) {
			textConsumer.accept(Text.translatable("item.tooltip.grow_your_crystal.crystal_component.light_level", this.lightLevel).formatted(Formatting.GRAY));
		}
		if (redstoneStrength > 0) {
			textConsumer.accept(Text.translatable("item.tooltip.grow_your_crystal.crystal_component.redstone_strength", this.redstoneStrength).formatted(Formatting.GRAY));
		}
		if (foodNutrition() > 0) {
			textConsumer.accept(Text.translatable("item.tooltip.grow_your_crystal.crystal_component.food_nutrition", this.foodNutrition()).formatted(Formatting.GRAY));
		}
	}

	private String toHexColorList(List<Integer> colors) {
		return colors.stream()
			.map(i -> String.format(Locale.ROOT, "#%06X", i))
			.toList().toString().replace("[", "").replace("]", "");
	}

	public FoodComponent getFoodComponent() {
		return new FoodComponent(this.foodNutrition, 1.2f, false);
	}

	public static int getColor(ItemStack stack, int defaultColor, int layer) {
		if (stack.contains(ModComponents.CRYSTAL_ITEM_COMPONENT)) {
			List<Integer> colors = stack.get(ModComponents.CRYSTAL_ITEM_COMPONENT).colors;
			int color = colors.size() > layer ? colors.get(layer) : defaultColor;
			return getTweakedColor(color);
		}
		return defaultColor;
	}

	private static List<Integer> DARK_COLORS = List.of(
		DyeColor.BLACK.getEntityColor(),
		DyeColor.GRAY.getEntityColor(),
		DyeColor.LIGHT_GRAY.getEntityColor()
	);

	public static int getTweakedColor(int color) {
		return DARK_COLORS.contains(color) ? ColorHelper.withBrightness(color, 0.5f) : ColorHelper.withBrightness(color, 1f);
//		return color;
	}
}
