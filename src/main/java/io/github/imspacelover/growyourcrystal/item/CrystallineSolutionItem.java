package io.github.imspacelover.growyourcrystal.item;

import io.github.imspacelover.growyourcrystal.component.CrystalItemComponent;
import io.github.imspacelover.growyourcrystal.component.ModComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CrystallineSolutionItem extends Item {
	public CrystallineSolutionItem(Settings settings) {
		super(settings);
	}

	@Override
	public ItemStack getDefaultStack() {
		ItemStack stack = super.getDefaultStack();
		stack.set(ModComponents.CRYSTAL_ITEM_COMPONENT, CrystalItemComponent.DEFAULT);
		return stack;
	}


}
