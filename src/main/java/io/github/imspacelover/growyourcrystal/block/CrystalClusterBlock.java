package io.github.imspacelover.growyourcrystal.block;

import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import io.github.imspacelover.growyourcrystal.blockentity.CrystalBlockEntity;
import io.github.imspacelover.growyourcrystal.component.CrystalItemComponent;
import io.github.imspacelover.growyourcrystal.component.ModComponents;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

	//TODO: add loot table

public class CrystalClusterBlock extends CrystalBlock implements Waterloggable, BlockEntityProvider {

	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final EnumProperty<Direction> FACING = Properties.FACING;
	private final Map<Direction, VoxelShape> shapesByDirection;

	public CrystalClusterBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(WATERLOGGED, false).with(FACING, Direction.UP).with(LIGHT_LEVEL, 0));
		this.shapesByDirection = VoxelShapes.createFacingShapeMap(Block.createCuboidZShape(12, 16.0F - 8.0F, 16.0));
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return super.createBlockEntity(pos, state);
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return (VoxelShape)this.shapesByDirection.get(state.get(FACING));
	}

	@Override
	protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = state.get(FACING);
		BlockPos blockPos = pos.offset(direction.getOpposite());
		return world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, direction);
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

		return direction == ((Direction)state.get(FACING)).getOpposite() && !state.canPlaceAt(world, pos)
			? Blocks.AIR.getDefaultState()
			: super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		WorldAccess worldAccess = ctx.getWorld();
		BlockPos blockPos = ctx.getBlockPos();
		return this.getDefaultState().with(WATERLOGGED, worldAccess.getFluidState(blockPos).getFluid() == Fluids.WATER).with(FACING, ctx.getSide());
	}

	@Override
	protected BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, FACING, LIGHT_LEVEL, EMITS_REDSTONE);
	}

}
