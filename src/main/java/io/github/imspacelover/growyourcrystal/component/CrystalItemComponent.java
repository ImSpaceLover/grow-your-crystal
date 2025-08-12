package io.github.imspacelover.growyourcrystal.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ColorHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public record CrystalItemComponent(List<Integer> colors, int lightLevel, int redstoneStrength, FoodComponent foodComponent) implements TooltipAppender {

	public static final int DEFAULT_COLOR = DyeColor.WHITE.getEntityColor();
	public static final FoodComponent DEFAULT_FOOD = new FoodComponent(0, 0, false);

	public static final CrystalItemComponent DEFAULT = new CrystalItemComponent(new ArrayList<>(), 0, 0,  DEFAULT_FOOD);

	public static final Codec<CrystalItemComponent> CODEC = RecordCodecBuilder.create(builder -> {
		return builder.group(
			Codec.INT.listOf().optionalFieldOf("colors", List.of(DEFAULT_COLOR)).forGetter(CrystalItemComponent::colors),
			Codec.INT.optionalFieldOf("lightLevel", 0).forGetter(CrystalItemComponent::lightLevel),
			Codec.INT.optionalFieldOf("redstoneStrength", 0).forGetter(CrystalItemComponent::redstoneStrength),
			FoodComponent.CODEC.optionalFieldOf("foodComponent", DEFAULT_FOOD).forGetter(CrystalItemComponent::foodComponent)
		).apply(builder, CrystalItemComponent::new);
	});


	@Override
	public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
		textConsumer.accept(Text.translatable("item.tooltip.growyourcrystal.crystalItem.colors", toHexColorList(colors)).formatted(Formatting.GRAY));
		if (lightLevel > 0) {
			textConsumer.accept(Text.translatable("item.tooltip.growyourcrystal.crystalItem.lightLevel", this.lightLevel).formatted(Formatting.GRAY));
		}
		if (redstoneStrength > 0) {
			textConsumer.accept(Text.translatable("item.tooltip.growyourcrystal.crystalItem.redstoneStrength", this.redstoneStrength).formatted(Formatting.GRAY));
		}
		if (foodComponent.nutrition() > 0) {
			textConsumer.accept(Text.translatable("item.tooltip.growyourcrystal.crystalItem.foodNutrition", this.foodComponent.nutrition()).formatted(Formatting.GRAY));
		}
	}

	private String toHexColorList(List<Integer> colors) {
		return colors.stream()
			.map(i -> String.format(Locale.ROOT, "#%06X", i))
			.toList().toString().replace("[", "").replace("]", "");
	}
}
