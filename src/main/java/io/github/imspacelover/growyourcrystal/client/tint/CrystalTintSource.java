package io.github.imspacelover.growyourcrystal.client.tint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.imspacelover.growyourcrystal.component.CrystalItemComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record CrystalTintSource(int defaultColor, int layer) implements TintSource {
	public static final MapCodec<CrystalTintSource> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
			Codecs.RGB.fieldOf("default").forGetter(CrystalTintSource::defaultColor),
			Codec.INT.fieldOf("layer").forGetter(CrystalTintSource::defaultColor)
			).apply(instance, CrystalTintSource::new)
	);

	@Override
	public int getTint(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user) {
		return CrystalItemComponent.getColor(stack, defaultColor, layer);
	}

	@Override
	public MapCodec<CrystalTintSource> getCodec() {
		return CODEC;
	}
}
