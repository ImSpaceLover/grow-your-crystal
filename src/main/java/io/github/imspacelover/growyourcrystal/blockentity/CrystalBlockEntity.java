package io.github.imspacelover.growyourcrystal.blockentity;

import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import io.github.imspacelover.growyourcrystal.block.ModBlocks;
import io.github.imspacelover.growyourcrystal.component.CrystalItemComponent;
import io.github.imspacelover.growyourcrystal.component.ModComponents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CrystalBlockEntity extends BlockEntity {
	public static final int MAX_LAYERS = 3;

	public CrystalItemComponent crystalComponent = CrystalItemComponent.DEFAULT;

	public CrystalBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.CRYSTAL_BLOCK, pos, state);
	}

	@Override
	protected void writeData(WriteView view) {
		super.writeData(view);
		if (!crystalComponent.equals(CrystalItemComponent.DEFAULT)) {
			view.put("crystal_component", CrystalItemComponent.CODEC, Optional.of(crystalComponent).orElse(CrystalItemComponent.DEFAULT));
		}
	}

	@Override
	protected void readData(ReadView view) {
		super.readData(view);
		this.crystalComponent = view.read("crystal_component", CrystalItemComponent.CODEC).orElse(CrystalItemComponent.DEFAULT);
		if (world != null) {
			world.updateListeners(pos, getCachedState(), getCachedState(), 0);
		}
	}

	public ItemStack getStackWith() {
		ItemStack stack = ModBlocks.CRYSTAL_BLOCK.asItem().getDefaultStack();
		stack.set(ModComponents.CRYSTAL_ITEM_COMPONENT, crystalComponent);
		return stack;
	}

	public void setCrystalComponent(CrystalItemComponent component) {
		crystalComponent = component;
		markDirty();
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
}
