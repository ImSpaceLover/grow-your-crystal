package io.github.imspacelover.growyourcrystal.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import java.util.function.Consumer;

public record CrystallineSolutionComponent(DyeColor color, boolean glowing, boolean redstoneSource, boolean edible) implements TooltipAppender {

	public static final Codec<CrystallineSolutionComponent> CODEC = RecordCodecBuilder.create(builder -> {
		return builder.group(
			DyeColor.CODEC.fieldOf("color").forGetter(CrystallineSolutionComponent::color),
			Codec.BOOL.optionalFieldOf("glowing", false).forGetter(CrystallineSolutionComponent::glowing),
			Codec.BOOL.optionalFieldOf("redstoneSource", false).forGetter(CrystallineSolutionComponent::redstoneSource),
			Codec.BOOL.optionalFieldOf("edible", false).forGetter(CrystallineSolutionComponent::edible)
		).apply(builder, CrystallineSolutionComponent::new);
	});


	@Override
	public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
		textConsumer.accept(Text.translatable("item.growyourcrystal.crystallineSolution.color", this.color()).formatted(Formatting.GRAY));
		if (glowing) {
			textConsumer.accept(Text.translatable("item.tooltip.growyourcrystal.crystallineSolution.glowing", this.glowing).formatted(Formatting.GRAY));
		}
		if (glowing) {
			textConsumer.accept(Text.translatable("item.tooltip.growyourcrystal.crystallineSolution.redstoneEmitter", this.redstoneSource).formatted(Formatting.GRAY));
		}
		if (glowing) {
			textConsumer.accept(Text.translatable("item.tooltip.growyourcrystal.crystallineSolution.edible", this.edible).formatted(Formatting.GRAY));
		}
	}
}
