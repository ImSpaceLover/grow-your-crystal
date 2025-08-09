package io.github.imspacelover.growyourcrystal.client;

import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import io.github.imspacelover.growyourcrystal.block.ModBlocks;
import io.github.imspacelover.growyourcrystal.blockentity.ModBlockEntities;
import io.github.imspacelover.growyourcrystal.client.ModBlockEntityRenderers.CrystalSeedBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class GrowYourCrystalClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> view != null && view.getBlockEntityRenderData(pos) instanceof Integer integer ? integer : 0xFF00FF00, ModBlocks.CRYSTAL_SEED_BLOCK);
//		ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
//			if (view != null && pos != null) {
//				return (view.getBlockEntityRenderData(pos) instanceof Integer integer ? integer : 0x006400);
//			}
//			}, ModBlocks.CRYSTAL_SEED_BLOCK);

		GrowYourCrystal.LOGGER.info("Block color Provider registered");
	}
}
