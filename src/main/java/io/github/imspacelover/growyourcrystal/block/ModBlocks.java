package io.github.imspacelover.growyourcrystal.block;

import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import io.github.imspacelover.growyourcrystal.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModBlocks {

	public static final Block CRYSTAL_SEED_BLOCK = register("crystal_seed", CrystalSeedBlock::new, AbstractBlock.Settings.create().nonOpaque().ticksRandomly(), true);

	private static Block register(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings, boolean shouldRegisterItem) {

		RegistryKey<Block> blockKey = keyOfBlock(name);

		Block block = blockFactory.apply(settings.registryKey(blockKey));

		if (shouldRegisterItem) {
			RegistryKey<Item> itemKey = keyOfItem(name);
			BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey));
			Registry.register(Registries.ITEM, itemKey, blockItem);
		}

		Registry.register(Registries.BLOCK, blockKey, block);
		return block;
	}

	private static RegistryKey<Block> keyOfBlock(String name) {
		return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(GrowYourCrystal.ID, name));
	}

	private static RegistryKey<Item> keyOfItem(String name) {
		return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(GrowYourCrystal.ID, name));
	}

	public static void initialize() {
		ItemGroupEvents.modifyEntriesEvent(ModItems.GROW_YOUR_CRYSTAL_GROUP_KEY).register((itemGroup) -> {
			itemGroup.add(ModBlocks.CRYSTAL_SEED_BLOCK.asItem());
		});
	}

}
