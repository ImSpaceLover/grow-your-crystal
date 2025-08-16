package io.github.imspacelover.growyourcrystal.client.datagen;

import io.github.imspacelover.growyourcrystal.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootTableProvider;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricEntityLootTableProvider {
	protected ModLootTableProvider(FabricDataOutput output, @NotNull CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(output, registryLookup);
	}

	private static final Identifier PIGLIN_BARTER_LOOT_TABLE_ID =  Identifier.of("minecraft", "gameplay/piglin_bartering");

	@Override
	public void generate() {
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
				if (PIGLIN_BARTER_LOOT_TABLE_ID.equals(key.getValue()) && source.isBuiltin()) {
					LootPool.Builder pool = LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1))
						.with(ItemEntry.builder(ModItems.CRYSTAL_GROWING_TOOLKIT).weight(10))
						.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
					tableBuilder.pool(pool);
				}
			}
		);
	}
}
