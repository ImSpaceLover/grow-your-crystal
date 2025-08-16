package io.github.imspacelover.growyourcrystal.client.datagen;

import io.github.imspacelover.growyourcrystal.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider<Block> {
	public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, RegistryKeys.BLOCK, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		getTagBuilder(BlockTags.NEEDS_IRON_TOOL)
			.add(getID(ModBlocks.CRYSTAL_CLUSTER_BLOCK))
			.add(getID(ModBlocks.CRYSTAL_BLOCK));

		getTagBuilder(BlockTags.PICKAXE_MINEABLE)
			.add(getID(ModBlocks.CRYSTAL_CLUSTER_BLOCK))
			.add(getID(ModBlocks.CRYSTAL_BLOCK));
	}

	public static Identifier getID(Block block) {
		return Registries.BLOCK.getId(block);
	}
}
