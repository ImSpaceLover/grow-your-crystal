package io.github.imspacelover.growyourcrystal.client;

import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import io.github.imspacelover.growyourcrystal.block.ModBlocks;
import io.github.imspacelover.growyourcrystal.client.tint.CrystalTintSource;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.item.tint.TintSourceTypes;
import net.minecraft.util.Identifier;

public class GrowYourCrystalClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		TintSourceTypes.ID_MAPPER.put(Identifier.of(GrowYourCrystal.ID, "crystal_color"), CrystalTintSource.CODEC);
//		TintSourceTypes.ID_MAPPER.put(Identifier.of(GrowYourCrystal.ID, "crystal_color"), CrystalTintSource.CODEC);
//		TintSourceTypes.ID_MAPPER.put(Identifier.of(GrowYourCrystal.ID, "crystal_color_1"), CrystalTintSource.CODEC);


		ModColorProviders.register();
		BlockRenderLayerMap.putBlock(ModBlocks.CRYSTAL_SEED_BLOCK, BlockRenderLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(ModBlocks.CRYSTAL_BLOCK, BlockRenderLayer.CUTOUT_MIPPED);
		BlockRenderLayerMap.putBlock(ModBlocks.CRYSTAL_CLUSTER_BLOCK, BlockRenderLayer.CUTOUT_MIPPED);
	}
}
