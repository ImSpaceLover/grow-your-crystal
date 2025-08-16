package io.github.imspacelover.growyourcrystal.client;

import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import io.github.imspacelover.growyourcrystal.block.ModBlocks;
import io.github.imspacelover.growyourcrystal.blockentity.CrystalBlockEntity;
import io.github.imspacelover.growyourcrystal.blockentity.CrystalSeedBlockEntity;
import io.github.imspacelover.growyourcrystal.component.CrystalItemComponent;
import io.github.imspacelover.growyourcrystal.util.ColorUtils;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

public class ModColorProviders {
	public static void register() {
		ColorProviderRegistry.BLOCK.register(
			(state, view, pos, tintIndex) -> {
				if (view != null && pos != null && view.getBlockEntity(pos) instanceof CrystalSeedBlockEntity blockEntity) {
					if (blockEntity.crystalComponent.colors().isEmpty()) {
						return ColorUtils.DEFAULT_COLOR;
					}
					int color = blockEntity.crystalComponent.colors().get(tintIndex);
					return ColorUtils.getTweakedColor(color);
				}
				else {
					return ColorUtils.DEFAULT_COLOR;
				}
			},
			ModBlocks.CRYSTAL_SEED_BLOCK);

		ColorProviderRegistry.BLOCK.register(
			(state, view, pos, tintIndex) -> {
				if (view != null && pos != null && view.getBlockEntity(pos) instanceof CrystalBlockEntity blockEntity) {
					if (blockEntity.crystalComponent.colors() != null && blockEntity.crystalComponent.colors().size() <= tintIndex) {
						return ColorUtils.DEFAULT_COLOR;
					}	else {
						int color = blockEntity.crystalComponent.colors().get(tintIndex);
						return ColorUtils.getTweakedColor(color);
					}
				}
				else {
					return ColorUtils.DEFAULT_COLOR;
				}
			},
			ModBlocks.CRYSTAL_BLOCK,
			ModBlocks.CRYSTAL_CLUSTER_BLOCK);
	}
}
