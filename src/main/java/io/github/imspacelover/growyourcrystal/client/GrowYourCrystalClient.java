package io.github.imspacelover.growyourcrystal.client;

import io.github.imspacelover.growyourcrystal.block.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.render.BlockRenderLayer;

public class GrowYourCrystalClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModColorProviders.register();
		BlockRenderLayerMap.putBlock(ModBlocks.CRYSTAL_SEED_BLOCK, BlockRenderLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(ModBlocks.CRYSTAL_BLOCK, BlockRenderLayer.CUTOUT_MIPPED);
		BlockRenderLayerMap.putBlock(ModBlocks.CRYSTAL_CLUSTER_BLOCK, BlockRenderLayer.CUTOUT_MIPPED);
	}
}
