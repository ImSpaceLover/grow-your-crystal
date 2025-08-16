package io.github.imspacelover.growyourcrystal.util;

import io.github.imspacelover.growyourcrystal.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

public class ModLootTableModifier {


	public static void modifyLootTables() {
		LootTableEvents.MODIFY.register(((key, tableBuilder, source, lookup) -> {
			if (LootTables.PIGLIN_BARTERING_GAMEPLAY.equals(key)) {
				LootPool.Builder poolBuilder = LootPool.builder()
					.rolls(ConstantLootNumberProvider.create(1.0F))
					.conditionally(RandomChanceLootCondition.builder(0.05f))
					.with(ItemEntry.builder(ModItems.CRYSTAL_GROWING_TOOLKIT).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))));
				tableBuilder.pool(poolBuilder);
			}

			if (LootTables.BASTION_OTHER_CHEST.equals(key)) {
				LootPool.Builder poolBuilder = LootPool.builder()
					.rolls(ConstantLootNumberProvider.create(1.0f))
					.conditionally(RandomChanceLootCondition.builder(0.5f))
					.with(ItemEntry.builder(ModItems.CRYSTAL_GROWING_TOOLKIT).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))));
				tableBuilder.pool(poolBuilder);
			}
		}));
	}
}
