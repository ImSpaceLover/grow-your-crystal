package io.github.imspacelover.growyourcrystal.block;


import com.mojang.serialization.MapCodec;
import io.github.imspacelover.growyourcrystal.blockentity.CrystalSeedBlockEntity;
import io.github.imspacelover.growyourcrystal.component.ModComponents;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class CrystalSeedBlock extends Block implements BlockEntityProvider, Waterloggable {

	public static final IntProperty AGE = Properties.AGE_3;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

	public static final int GROWTH_TICKS = 8000;

	public final Map<Integer, VoxelShape> SHAPES = Map.of(
		AGE.getValues().get(0), Block.createCubeShape(4),
		AGE.getValues().get(1), Block.createCubeShape(8),
		AGE.getValues().get(2), Block.createCubeShape(12),
		AGE.getValues().get(3), Block.createCubeShape(16)
		);

	public CrystalSeedBlock(Settings settings) {
		super(settings.nonOpaque());
		setDefaultState(getDefaultState().with(AGE, 0));
	}

	@Override
	protected MapCodec<? extends Block> getCodec() {
		return super.getCodec();
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CrystalSeedBlockEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return BlockEntityProvider.super.getTicker(world, state, type);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.WATERLOGGED);
		builder.add(AGE);
	}

	@Override
	protected boolean isTransparent(BlockState state) {
		return true;
	}

	@Override
	protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof CrystalSeedBlockEntity crystalSeedBlockEntity
			&& entity instanceof ItemEntity itemEntity
			&& !itemEntity.getStack().isEmpty()
			&& itemEntity.getStack().contains(ModComponents.CRYSTALLINE_SOLUTION_COMPONENT)
			&& !world.isClient()
			&& state.get(AGE) != 3
			&& (Boolean) state.get(WATERLOGGED)) {
				progressGrowth(state, world, pos, crystalSeedBlockEntity, itemEntity, handler);
			}
		super.onEntityCollision(state, world, pos, entity, handler);
	}

	private void progressGrowth(BlockState state, World world, BlockPos pos, CrystalSeedBlockEntity blockEntity, ItemEntity itemEntity, EntityCollisionHandler handler) {
		blockEntity.addCrystallineSolution(world, pos, state, itemEntity, state.get(AGE));
		itemEntity.remove(Entity.RemovalReason.KILLED);
		world.scheduleBlockTick(pos, this, GROWTH_TICKS);
	}

	@Override
	protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if ((Boolean)state.get(WATERLOGGED)) {
			this.tickGrowth(state, world, pos, random);
		}
	}

	private void tickGrowth(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 1.0F, 1.0F);
		world.setBlockState(pos, state.with(AGE, state.get(AGE) + 1), Block.NOTIFY_LISTENERS);
		world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(state));
	}

	@Override
	public boolean canFillWithFluid(@Nullable LivingEntity filler, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		if (state.getProperties().contains(Properties.AGE_3) && state.get(AGE) == 3) {
			return false;
		}
		return Waterloggable.super.canFillWithFluid(filler, world, pos, state, fluid);
	}

	@Override
	public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		return Waterloggable.super.tryFillWithFluid(world, pos, state, fluidState);
	}

	@Override
	public ItemStack tryDrainFluid(@Nullable LivingEntity drainer, WorldAccess world, BlockPos pos, BlockState state) {
		return Waterloggable.super.tryDrainFluid(drainer, world, pos, state);
	}

	@Override
	public Optional<SoundEvent> getBucketFillSound() {
		return Waterloggable.super.getBucketFillSound();
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) && state.get(AGE) != 3 ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	protected BlockState getStateForNeighborUpdate(
		BlockState state,
		WorldView world,
		ScheduledTickView tickView,
		BlockPos pos,
		Direction direction,
		BlockPos neighborPos,
		BlockState neighborState,
		Random random
	) {
		if ((Boolean)state.get(WATERLOGGED)) {
			tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		return this.getDefaultState().with(WATERLOGGED, fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8);
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPES.get(state.get(AGE));
	}
}
