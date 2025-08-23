package io.github.imspacelover.growyourcrystal.block;

import io.github.imspacelover.growyourcrystal.blockentity.CrystalBlockEntity;
import io.github.imspacelover.growyourcrystal.blockentity.CrystalSeedBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

public class CreativeSeedBlock extends CrystalSeedBlock {
	public CreativeSeedBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.get(AGE) != 3) {
			return;
		}
		Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
		BlockPos blockPos = pos.offset(direction);
		BlockState blockState = world.getBlockState(blockPos);
		Block block = null;
		if (canGrowIn(blockState)) {
			block = ModBlocks.CRYSTAL_CLUSTER_BLOCK;
		}
		if (block != null) {
			if (world.getBlockEntity(pos) instanceof CrystalSeedBlockEntity crystalSeedBlockEntity) {
				BlockState blockStateCluster = block.getDefaultState()
					.with(CrystalClusterBlock.FACING, direction)
					.with(CrystalClusterBlock.WATERLOGGED, blockState.getFluidState().getFluid() == Fluids.WATER)
					.with(CrystalClusterBlock.LIGHT_LEVEL, crystalSeedBlockEntity.crystalComponent.lightLevel())
					.with(CrystalClusterBlock.EMITS_REDSTONE, crystalSeedBlockEntity.getEmitsRedstone());
				world.setBlockState(blockPos, blockStateCluster);
				if (world.getBlockEntity(blockPos) instanceof CrystalBlockEntity crystalBlockEntity) {
					crystalBlockEntity.setCrystalComponent(crystalSeedBlockEntity.crystalComponent);
				}
			}
		}
	}
}
