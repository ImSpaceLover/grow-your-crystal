package io.github.imspacelover.growyourcrystal.client;

import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import io.github.imspacelover.growyourcrystal.block.ModBlocks;
import io.github.imspacelover.growyourcrystal.blockentity.CrystalBlockEntity;
import io.github.imspacelover.growyourcrystal.blockentity.CrystalSeedBlockEntity;
import io.github.imspacelover.growyourcrystal.component.CrystalItemComponent;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.ColorHelper;

public class ModColorProviders {
	public static void register() {
		ColorProviderRegistry.BLOCK.register(
			(state, view, pos, tintIndex) -> {
				if (view != null && pos != null && view.getBlockEntity(pos) instanceof CrystalSeedBlockEntity blockEntity) {
					if (blockEntity.crystalComponent.colors().isEmpty()) {
						return CrystalItemComponent.DEFAULT_COLOR;
					}
					int color = blockEntity.crystalComponent.colors().get(tintIndex);
					return color == DyeColor.BLACK.getEntityColor() ? ColorHelper.withBrightness(color, 0.4f) : ColorHelper.withBrightness(color, 1f);
				}
				else {
					return CrystalItemComponent.DEFAULT_COLOR;
				}
			},
			ModBlocks.CRYSTAL_SEED_BLOCK);

		ColorProviderRegistry.BLOCK.register(
			(state, view, pos, tintIndex) -> {
				if (view != null && pos != null && view.getBlockEntity(pos) instanceof CrystalBlockEntity blockEntity) {
					if (blockEntity.crystalComponent.colors().size() < 3) {
						return CrystalItemComponent.DEFAULT_COLOR;
					}	else {
						int color = blockEntity.crystalComponent.colors().get(tintIndex);
						return color == DyeColor.BLACK.getEntityColor() ? ColorHelper.withBrightness(color, 0.4f) : ColorHelper.withBrightness(color, 1f);
					}
				}
				else {
					return CrystalItemComponent.DEFAULT_COLOR;
				}
			},
			ModBlocks.CRYSTAL_BLOCK,
			ModBlocks.CRYSTAL_CLUSTER_BLOCK);
	}
}
