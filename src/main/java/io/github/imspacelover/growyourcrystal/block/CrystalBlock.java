package io.github.imspacelover.growyourcrystal.block;

import com.mojang.serialization.MapCodec;
import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import io.github.imspacelover.growyourcrystal.blockentity.CrystalBlockEntity;
import io.github.imspacelover.growyourcrystal.blockentity.ModBlockEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrystalBlock extends Block implements BlockEntityProvider {
	public static final IntProperty LIGHT_LEVEL = Properties.LEVEL_15;

	public CrystalBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected MapCodec<? extends Block> getCodec() {
		return super.getCodec();
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CrystalBlockEntity(pos, state);
	}

	public static int getLuminance(BlockState state) {
		return state.get(LIGHT_LEVEL);
	}

	@Override
	protected int getOpacity(BlockState state) {
		return 1;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(LIGHT_LEVEL);
	}

	@Override
	protected List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
		BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
		if (blockEntity instanceof CrystalBlockEntity crystalBlockEntity) {
			builder = builder.addDynamicDrop(Identifier.of(GrowYourCrystal.ID, "crystal_component"), lootConsumer -> {
				lootConsumer.accept(crystalBlockEntity.getStackWith());
			});
		}

		return super.getDroppedStacks(state, builder);
	}

//	@Override
//	protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
//		if (world.getBlockEntity(pos) instanceof CrystalBlockEntity crystalBlockEntity) {
//			return crystalBlockEntity.getStackWith();
//		} else {
//			return super.getPickStack(world, pos, state, true);
//		}
//	}
}
