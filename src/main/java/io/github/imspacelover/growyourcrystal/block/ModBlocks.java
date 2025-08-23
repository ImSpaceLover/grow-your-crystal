package io.github.imspacelover.growyourcrystal.block;

import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import io.github.imspacelover.growyourcrystal.component.CrystalItemComponent;
import io.github.imspacelover.growyourcrystal.component.ModComponents;
import io.github.imspacelover.growyourcrystal.item.ModItems;
import io.github.imspacelover.growyourcrystal.util.ColorUtils;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Function;

public class ModBlocks {

	public static final Block CRYSTAL_BLOCK = register("crystal_block", CrystalBlock::new,
		AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK)
			.nonOpaque()
			.luminance(CrystalBlock::getLuminance)
			.requiresTool()
			.hardness(Blocks.AMETHYST_BLOCK.getHardness()),
		true);

	public static final Block CRYSTAL_SEED_BLOCK = register("crystal_seed", CrystalSeedBlock::new,
		AbstractBlock.Settings.create().sounds(BlockSoundGroup.AMETHYST_BLOCK)
			.nonOpaque()
			.ticksRandomly()
			.requiresTool()
			.luminance(CrystalSeedBlock::getLuminance)
			.pistonBehavior(PistonBehavior.BLOCK)
			.hardness(Blocks.AMETHYST_BLOCK.getHardness()),
		true);

	public static final Block CREATIVE_SEED_BLOCK = register("creative_seed", CreativeSeedBlock::new,
		AbstractBlock.Settings.create().sounds(BlockSoundGroup.AMETHYST_BLOCK)
			.nonOpaque()
			.ticksRandomly()
			.requiresTool()
			.luminance(CrystalSeedBlock::getLuminance)
			.pistonBehavior(PistonBehavior.BLOCK)
			.hardness(Blocks.AMETHYST_BLOCK.getHardness()),
		true);

	public static final Block CRYSTAL_CLUSTER_BLOCK = register("crystal_cluster", CrystalClusterBlock::new,
		AbstractBlock.Settings.create().sounds(BlockSoundGroup.LARGE_AMETHYST_BUD)
			.requiresTool()
			.nonOpaque().ticksRandomly()
			.luminance(CrystalClusterBlock::getLuminance)
			.requiresTool()
			.pistonBehavior(PistonBehavior.DESTROY)
			.hardness(Blocks.AMETHYST_CLUSTER.getHardness()),
		new Item.Settings().food(new FoodComponent.Builder().build()));

	private static Block register(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings, boolean shouldRegisterItem) {
		RegistryKey<Block> blockKey = keyOfBlock(name);

		Block block = blockFactory.apply(settings.registryKey(blockKey));

		if (shouldRegisterItem) {
			RegistryKey<Item> itemKey = keyOfItem(name);
			BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey).useBlockPrefixedTranslationKey());
			Registry.register(Registries.ITEM, itemKey, blockItem);
		}

		Registry.register(Registries.BLOCK, blockKey, block);
		return block;
	}

	private static Block register(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings, Item.Settings itemSettings) {
		RegistryKey<Block> blockKey = keyOfBlock(name);

		Block block = blockFactory.apply(settings.registryKey(blockKey));

		RegistryKey<Item> itemKey = keyOfItem(name);
		BlockItem blockItem = new BlockItem(block, itemSettings.registryKey(itemKey).useBlockPrefixedTranslationKey());
		Registry.register(Registries.ITEM, itemKey, blockItem);


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
			itemGroup.add(ModBlocks.CREATIVE_SEED_BLOCK.asItem());
			for (DyeColor dyeColor: DyeColor.values()) {
				int color = ColorUtils.getCrystalColor(dyeColor);
				ItemStack crystalBlockStack = ModBlocks.CRYSTAL_BLOCK.asItem().getDefaultStack();
				crystalBlockStack.set(ModComponents.CRYSTAL_ITEM_COMPONENT,
					new CrystalItemComponent(List.of(color, color, color), 0, 0, 0));
				itemGroup.add(crystalBlockStack);
			}
			for (DyeColor dyeColor : DyeColor.values()) {
				int color = ColorUtils.getCrystalColor(dyeColor);
				ItemStack crystalClusterStack = ModBlocks.CRYSTAL_CLUSTER_BLOCK.asItem().getDefaultStack();
				crystalClusterStack.set(ModComponents.CRYSTAL_ITEM_COMPONENT,
					new CrystalItemComponent(List.of(color, color, color), 0, 0, 0));
				itemGroup.add(crystalClusterStack);
			}
		});
	}
}
