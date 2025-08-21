package io.github.imspacelover.growyourcrystal.blockentity;

import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import io.github.imspacelover.growyourcrystal.block.ModBlocks;
import io.github.imspacelover.growyourcrystal.component.CrystalItemComponent;
import io.github.imspacelover.growyourcrystal.component.ModComponents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
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

	public CrystalItemComponent crystalComponent = CrystalItemComponent.DEFAULT_BLOCK;

	public CrystalBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.CRYSTAL_BLOCK, pos, state);
	}

	public boolean getEmitsRedstone() {
		return crystalComponent.redstoneStrength() > 0;
	}

	@Override
	protected void writeData(WriteView view) {
		super.writeData(view);
		if (this.crystalComponent != null && !crystalComponent.equals(CrystalItemComponent.DEFAULT_BLOCK)) {
			view.put("crystal_component", CrystalItemComponent.CODEC, Optional.of(crystalComponent).orElse(CrystalItemComponent.DEFAULT_BLOCK));
		}
	}

	@Override
	protected void readData(ReadView view) {
		super.readData(view);
		this.crystalComponent = view.read("crystal_component", CrystalItemComponent.CODEC).orElse(CrystalItemComponent.DEFAULT_BLOCK);
		if (world != null) {
			world.updateListeners(pos, getCachedState(), getCachedState(), 0);
		}
	}

	public ItemStack getStackWith(Block block) {
		ItemStack stack = block.asItem().getDefaultStack();
		stack.set(ModComponents.CRYSTAL_ITEM_COMPONENT, Optional.of(this.crystalComponent).orElse(CrystalItemComponent.DEFAULT_BLOCK));
		stack.set(DataComponentTypes.FOOD, Optional.of(this.crystalComponent.getFoodComponent()).orElse(CrystalItemComponent.DEFAULT_FOOD));
		return stack;
	}

	@Override
	protected void addComponents(ComponentMap.Builder builder) {
		super.addComponents(builder);
		builder.add(ModComponents.CRYSTAL_ITEM_COMPONENT, Optional.of(this.crystalComponent).orElse(CrystalItemComponent.DEFAULT_BLOCK));
		builder.add(DataComponentTypes.FOOD, this.crystalComponent.getFoodComponent());
	}


	@Override
	protected void readComponents(ComponentsAccess components) {
		super.readComponents(components);
		this.crystalComponent = components.getOrDefault(ModComponents.CRYSTAL_ITEM_COMPONENT, CrystalItemComponent.DEFAULT_BLOCK);
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
