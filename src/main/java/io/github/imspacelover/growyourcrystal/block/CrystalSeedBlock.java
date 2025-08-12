package io.github.imspacelover.growyourcrystal.block;


import com.mojang.serialization.MapCodec;
import io.github.imspacelover.growyourcrystal.blockentity.CrystalBlockEntity;
import io.github.imspacelover.growyourcrystal.blockentity.CrystalSeedBlockEntity;
import io.github.imspacelover.growyourcrystal.component.ModComponents;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CrafterBlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
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
import net.minecraft.util.math.Position;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.AdvancedExplosionBehavior;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class CrystalSeedBlock extends Block implements BlockEntityProvider, Waterloggable {

	public static final IntProperty AGE = Properties.AGE_3;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final int GROWTH_TICKS = 80;

	public final Map<Integer, VoxelShape> SHAPES = Map.of(
		AGE.getValues().get(0), Block.createCubeShape(4),
		AGE.getValues().get(1), Block.createCubeShape(8),
		AGE.getValues().get(2), Block.createCubeShape(12),
		AGE.getValues().get(3), Block.createCubeShape(16)
		);

	public CrystalSeedBlock(Settings settings) {
		super(settings);
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
		builder.add(WATERLOGGED, AGE);
	}

		@Override
	protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof CrystalSeedBlockEntity crystalSeedBlockEntity
			&& entity instanceof ItemEntity itemEntity
			&& !itemEntity.getStack().isEmpty()
			&& itemEntity.getStack().contains(ModComponents.CRYSTAL_ITEM_COMPONENT)
			&& !world.isClient()
			&& state.get(AGE) != 3
			&& state.get(WATERLOGGED)
			&& !crystalSeedBlockEntity.growing) {
				progressGrowth(state, world, pos, crystalSeedBlockEntity, itemEntity, handler);
			}
		super.onEntityCollision(state, world, pos, entity, handler);
	}

	@Override
	protected boolean hasSidedTransparency(BlockState state) {
		return super.hasSidedTransparency(state);
	}

	private void progressGrowth(BlockState state, World world, BlockPos pos, CrystalSeedBlockEntity blockEntity, ItemEntity itemEntity, EntityCollisionHandler handler) {
		blockEntity.addCrystallineSolution(world, pos, state, itemEntity, state.get(AGE));

		world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

		Position itemPos = itemEntity.getPos();

		world.createExplosion(null,
			null,
			new AdvancedExplosionBehavior(false, false, Optional.of(0.0F),
			Registries.BLOCK.getOptional(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())),
			itemPos.getX(),
			itemPos.getY(),
			itemPos.getZ(),
			5,
			false,
			World.ExplosionSourceType.NONE,
			ParticleTypes.HAPPY_VILLAGER,
			ParticleTypes.DRIPPING_WATER,
			Registries.SOUND_EVENT.getEntry(SoundEvents.ITEM_BONE_MEAL_USE));

		itemEntity.remove(Entity.RemovalReason.KILLED);
		world.scheduleBlockTick(pos, this, GROWTH_TICKS);
	}

	@Override
	protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if ((Boolean)state.get(WATERLOGGED)) {
			this.tickGrowth(state, world, pos, random);
		}
	}

	@Override
	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.get(AGE) != 3) {
			return;
		}
		if (random.nextInt(4) == 0) {
			Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
			BlockPos blockPos = pos.offset(direction);
			BlockState blockState = world.getBlockState(blockPos);
			Block block = null;
			if (canGrowIn(blockState)) {
				block = ModBlocks.CRYSTAL_CLUSTER_BLOCK;
			}
			if (block != null) {
				BlockState blockState2 = block.getDefaultState()
					.with(CrystalClusterBlock.FACING, direction)
					.with(CrystalClusterBlock.WATERLOGGED, blockState.getFluidState().getFluid() == Fluids.WATER);
				world.setBlockState(blockPos, blockState2);
				if (world.getBlockEntity(pos) instanceof CrystalSeedBlockEntity crystalSeedBlockEntity) {
					if (world.getBlockEntity(blockPos) instanceof CrystalBlockEntity crystalBlockEntity) {
						crystalBlockEntity.setCrystalComponent(crystalSeedBlockEntity.crystalComponent);
					}
				}
			}
		}
	}

	public static boolean canGrowIn(BlockState state) {
		return state.isAir() || state.isOf(Blocks.WATER) && state.getFluidState().getLevel() == 8;
	}

	private void tickGrowth(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (world.getBlockEntity(pos) instanceof CrystalSeedBlockEntity blockEntity) {
			blockEntity.growing = false;
		}

		world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
		world.setBlockState(pos, state.with(AGE, state.get(AGE) + 1).with(WATERLOGGED, Boolean.FALSE), Block.NOTIFY_ALL_AND_REDRAW);
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
