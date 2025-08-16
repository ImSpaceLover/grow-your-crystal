package io.github.imspacelover.growyourcrystal.client.datagen;

import io.github.imspacelover.growyourcrystal.block.CrystalBlock;
import io.github.imspacelover.growyourcrystal.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.*;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.state.property.Properties;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
	protected ModBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generate() {

		List<Block> blocks = List.of(
			ModBlocks.CRYSTAL_BLOCK,
			ModBlocks.CRYSTAL_CLUSTER_BLOCK
		);

		for (Block block: blocks) {
			addDrop(block, LootTable.builder()
				.pool(LootPool.builder()
					.with(ItemEntry.builder(block)
						.apply(CopyComponentsLootFunction
							.builder(CopyComponentsLootFunction.Source.BLOCK_ENTITY))
						.apply(CopyStateLootFunction
							.builder(block).addProperty(CrystalBlock.LIGHT_LEVEL).addProperty(CrystalBlock.EMITS_REDSTONE))
					)
					.build()));
		}
	}
}
