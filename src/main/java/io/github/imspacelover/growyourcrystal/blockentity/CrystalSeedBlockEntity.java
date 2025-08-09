package io.github.imspacelover.growyourcrystal.blockentity;

import io.github.imspacelover.growyourcrystal.component.CrystallineSolutionComponent;
import io.github.imspacelover.growyourcrystal.component.ModComponents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CrystalSeedBlockEntity extends BlockEntity {
	public static final int MAX_LAYERS = 3;


	public int lightLevel = 0;
	public int redstoneStrength = 0;
	public int foodValue = 0;

	public int[] layerColors = {0xFF000000, 0xFF000000, 0xFF000000};

	public CrystalSeedBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.CRYSTAL_SEED, pos, state);
	}

	public void addCrystallineSolution(World world, BlockPos pos, BlockState state, ItemEntity itemEntity, int layer) {
		ItemStack stack = itemEntity.getStack();
		CrystallineSolutionComponent component = stack.get(ModComponents.CRYSTALLINE_SOLUTION_COMPONENT);

		layerColors[layer] = component.color().getEntityColor();
		if (component.glowing()) {
			lightLevel += 5;
		}
		if (component.redstoneSource()) {
			redstoneStrength += 5;
		}
		if (component.edible()) {
			foodValue += 1;
		}
		markDirty(world, pos, state);
	}

	@Override
	protected void writeData(WriteView view) {
		super.writeData(view);

		view.putIntArray("colors", layerColors);
		view.putInt("lightLevel", lightLevel);
		view.putInt("redstoneStrength", lightLevel);
		view.putInt("foodValue", foodValue);
	}

	@Override
	protected void readData(ReadView view) {
		super.readData(view);

		view.getOptionalIntArray("colors").ifPresent(array -> {
			System.arraycopy(array, 0, layerColors, 0, MAX_LAYERS);
		});

		view.getInt("lightLevel", lightLevel);
		view.getInt("redstoneStrength", redstoneStrength);
		view.getInt("foodValue", foodValue);

		if (world != null) {
			world.updateListeners(pos, getCachedState(), getCachedState(), 0);
		}
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
		return createNbt(registryLookup);
	}

	@Override
	public @Nullable Object getRenderData() {
		return layerColors[0];
	}
}
