package io.github.imspacelover.growyourcrystal.item;

import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import io.github.imspacelover.growyourcrystal.block.ModBlocks;
import io.github.imspacelover.growyourcrystal.component.CrystalItemComponent;
import io.github.imspacelover.growyourcrystal.component.ModComponents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class ModItems {

	public static final Item GROW_YOUR_CRYSTAL_TOOLKIT = register("grow_your_crystal_toolkit", GrowYourCrystalToolkitItem::new, new GrowYourCrystalToolkitItem.Settings()
		.maxDamage(64)
		.translationKey("item.growyourcrystal.growYourCrystalToolkit"));

	public static final Item CRYSTALLINE_SOLUTION = register("crystalline_solution", CrystallineSolutionItem::new, new Item.Settings()
		.translationKey("item.growyourcrystal.crystallineSolution")
		.component(ModComponents.CRYSTAL_ITEM_COMPONENT,
			new CrystalItemComponent(
				List.of(CrystalItemComponent.DEFAULT_COLOR), 0, 0, new FoodComponent(0, 0f, false))));

	public static final RegistryKey<ItemGroup> GROW_YOUR_CRYSTAL_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(GrowYourCrystal.ID, "grow_your_crystal"));
	public static final ItemGroup GROW_YOUR_CRYSTAL_GROUP = FabricItemGroup.builder()
		.icon(() -> new ItemStack(GROW_YOUR_CRYSTAL_TOOLKIT))
		.displayName(Text.translatable("itemGroup.growyourcrystal.growYourCrystal"))
		.build();

	private static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {

		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(GrowYourCrystal.ID, name));

		Item item = itemFactory.apply(settings.registryKey(itemKey));

		Registry.register(Registries.ITEM, itemKey, item);

		return item;
	}

	public static void initialize() {
		Registry.register(Registries.ITEM_GROUP, GROW_YOUR_CRYSTAL_GROUP_KEY, GROW_YOUR_CRYSTAL_GROUP);

		ItemGroupEvents.modifyEntriesEvent(GROW_YOUR_CRYSTAL_GROUP_KEY).register((itemGroup) -> {
			itemGroup.add(GROW_YOUR_CRYSTAL_TOOLKIT);
			itemGroup.add(CRYSTALLINE_SOLUTION);
		});
	}
}
