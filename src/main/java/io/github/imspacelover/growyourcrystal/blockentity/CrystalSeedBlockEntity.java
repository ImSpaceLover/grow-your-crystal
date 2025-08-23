package io.github.imspacelover.growyourcrystal.blockentity;

import io.github.imspacelover.growyourcrystal.block.ModBlocks;
import io.github.imspacelover.growyourcrystal.component.CrystalItemComponent;
import io.github.imspacelover.growyourcrystal.component.ModComponents;
import io.github.imspacelover.growyourcrystal.util.ColorUtils;
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

import java.util.List;
import java.util.Optional;

public class CrystalSeedBlockEntity extends BlockEntity {
	public static final int MAX_LAYERS = 3;
	public CrystalItemComponent crystalComponent = CrystalItemComponent.DEFAULT_BLOCK;
	public boolean growing = false;

	public CrystalSeedBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.CRYSTAL_SEED, pos, state);
	}

	public void addCrystallineSolution(World world, BlockPos pos, BlockState state, ItemEntity itemEntity, int layer) {
		ItemStack stack = itemEntity.getStack();
		CrystalItemComponent component = stack.get(ModComponents.CRYSTAL_ITEM_COMPONENT);

		if (component != null) {
			addComponent(component, layer);
		}
		growing = true;

		markDirty(world, pos, state);
	}

	public void addComponent(CrystalItemComponent component, int layer) {
		List<Integer> colors = new java.util.ArrayList<>(List.copyOf(this.crystalComponent.colors()));
		while (colors.size() < MAX_LAYERS) {
			colors.add(ColorUtils.DEFAULT_COLOR);
		}
		colors.set(layer, component.colors().getFirst());
		crystalComponent = new CrystalItemComponent(
			colors,
			Math.min(crystalComponent.lightLevel() + component.lightLevel(), 15),
			Math.min(crystalComponent.redstoneStrength() + component.redstoneStrength(), 15),
			crystalComponent.foodNutrition() + component.foodNutrition());
	}


	@Override
	protected void writeData(WriteView view) {
		super.writeData(view);
		if (!crystalComponent.equals(CrystalItemComponent.DEFAULT)) {
			view.put("crystal_component", CrystalItemComponent.CODEC, Optional.of(crystalComponent).orElse(CrystalItemComponent.DEFAULT_BLOCK));
		}
		view.putBoolean("growing", growing);
	}

	@Override
	protected void readData(ReadView view) {
		super.readData(view);
		this.crystalComponent = view.read("crystal_component", CrystalItemComponent.CODEC).orElse(CrystalItemComponent.DEFAULT_BLOCK);
		this.growing = view.getBoolean("growing", false);
		if (world != null) {
			world.updateListeners(pos, getCachedState(), getCachedState(), 0);
		}
	}

	public boolean getEmitsRedstone() {
		return crystalComponent.redstoneStrength() > 0;
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
		return super.getRenderData();
	}
}
