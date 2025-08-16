package io.github.imspacelover.growyourcrystal.block;

import com.mojang.serialization.MapCodec;
import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import io.github.imspacelover.growyourcrystal.blockentity.CrystalBlockEntity;
import io.github.imspacelover.growyourcrystal.component.CrystalItemComponent;
import io.github.imspacelover.growyourcrystal.component.ModComponents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.OrientationHelper;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

	//TODO: add crafting recipe from Crystal clusters

public class CrystalBlock extends Block implements BlockEntityProvider {

	public static Identifier CRYSTAL_COMPONENT_ID = Identifier.of(GrowYourCrystal.ID, "crystal_component");

	public static final IntProperty LIGHT_LEVEL = IntProperty.of("light", 0, 15);
	public static final BooleanProperty EMITS_REDSTONE = BooleanProperty.of("emits_redstone");

	public CrystalBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(LIGHT_LEVEL, 0).with(EMITS_REDSTONE, false));
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
		builder.add(LIGHT_LEVEL).add(EMITS_REDSTONE);
	}

	@Override
	protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		if (world.getBlockEntity(pos) instanceof CrystalBlockEntity crystalBlockEntity) {
			return crystalBlockEntity.crystalComponent.redstoneStrength();
		}
		return 0;
	}

	@Override
	protected boolean emitsRedstonePower(BlockState state) {
		return state.get(EMITS_REDSTONE);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		world.updateNeighbors(pos, this);
		super.onPlaced(world, pos, state, placer, itemStack);
	}

	@Override
	protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
		if (world.getBlockEntity(pos) instanceof CrystalBlockEntity crystalBlockEntity) {
			ItemStack itemStack = crystalBlockEntity.getStackWith(this);
			itemStack.set(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT
				.with(LIGHT_LEVEL, state.get(LIGHT_LEVEL))
				.with(EMITS_REDSTONE, state.get(EMITS_REDSTONE)));
			return itemStack;
		} else {
			return super.getPickStack(world, pos, state, includeData);
		}
	}



}
