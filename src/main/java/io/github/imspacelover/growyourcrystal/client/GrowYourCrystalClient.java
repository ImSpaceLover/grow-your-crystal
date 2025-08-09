package io.github.imspacelover.growyourcrystal.client;

import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import io.github.imspacelover.growyourcrystal.block.ModBlocks;
import io.github.imspacelover.growyourcrystal.blockentity.CrystalSeedBlockEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.ColorHelper;

public class GrowYourCrystalClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ColorProviderRegistry.BLOCK.register(
			(state, view, pos, tintIndex) -> {
			if (view != null && pos != null && view.getBlockEntity(pos) instanceof CrystalSeedBlockEntity blockEntity) {
//				return ColorHelper.withBrightness(blockEntity.layerColors[tintIndex], 0.9f);

				return blockEntity.layerColors[tintIndex];
			}
			else {
				return DyeColor.WHITE.getFireworkColor();
			}
			}, ModBlocks.CRYSTAL_SEED_BLOCK);

		BlockRenderLayerMap.putBlock(ModBlocks.CRYSTAL_SEED_BLOCK, BlockRenderLayer.CUTOUT);

		GrowYourCrystal.LOGGER.info("Block color Provider registered");
	}
}
