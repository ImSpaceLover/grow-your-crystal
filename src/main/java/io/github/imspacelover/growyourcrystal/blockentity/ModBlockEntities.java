package io.github.imspacelover.growyourcrystal.blockentity;

import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import io.github.imspacelover.growyourcrystal.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

	public static final BlockEntityType<CrystalBlockEntity> CRYSTAL_BLOCK = register("crystal_blockentity", CrystalBlockEntity::new, ModBlocks.CRYSTAL_BLOCK, ModBlocks.CRYSTAL_CLUSTER_BLOCK);

	public static final BlockEntityType<CrystalSeedBlockEntity> CRYSTAL_SEED = register("crystal_seed", CrystalSeedBlockEntity::new, ModBlocks.CRYSTAL_SEED_BLOCK);

		public static <T extends BlockEntity>BlockEntityType<T> register(
		String name,
		FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
		Block... blocks) {
		Identifier id = Identifier.of(GrowYourCrystal.ID, name);
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
	}

	public static void initialize() {

	}
}
