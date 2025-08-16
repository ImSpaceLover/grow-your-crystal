package io.github.imspacelover.growyourcrystal.client.datagen;

import io.github.imspacelover.growyourcrystal.block.ModBlocks;
import io.github.imspacelover.growyourcrystal.client.tint.CrystalTintSource;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;

public class ModModelProvider extends FabricModelProvider {
	public ModModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
//		itemModelGenerator.register(ModBlocks.CRYSTAL_CLUSTER_BLOCK, );
//		itemModelGenerator.registerSubModel()
		itemModelGenerator.registerWithTintedLayer(ModBlocks.CRYSTAL_CLUSTER_BLOCK.asItem(), "0", new CrystalTintSource(0, 0));
//		itemModelGenerator.registerSubModel(ModBlocks.CRYSTAL_CLUSTER_BLOCK.asItem(), "layer",
//			Models.GENERATED_THREE_LAYERS);
//		ItemModels itemModels = new ItemModels();
//		ItemModels.
//		itemModelGenerator.registerSubModel()
	}
}
