package io.github.imspacelover.growyourcrystal.item;

import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.logging.Logger;

public class GrowYourCrystalToolkitItem extends Item {
	public GrowYourCrystalToolkitItem(Settings settings) {
		super(settings);
	}

//	@Override
//	public ItemStack getRecipeRemainder(ItemStack stack) {
//		GrowYourCrystal.LOGGER.info(Integer.toString(stack.getDamage()));
//		stack.setDamage(stack.getDamage() + 1);
//		GrowYourCrystal.LOGGER.info(Integer.toString(stack.getDamage()));
//		return stack;
//	}
}
