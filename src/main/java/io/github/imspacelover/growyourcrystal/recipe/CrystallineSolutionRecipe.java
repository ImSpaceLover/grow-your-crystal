package io.github.imspacelover.growyourcrystal.recipe;

import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import io.github.imspacelover.growyourcrystal.component.CrystallineSolutionComponent;
import io.github.imspacelover.growyourcrystal.component.ModComponents;
import io.github.imspacelover.growyourcrystal.item.GrowYourCrystalToolkitItem;
import io.github.imspacelover.growyourcrystal.item.ModItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;

public class CrystallineSolutionRecipe extends SpecialCraftingRecipe {

	public static final Ingredient LIGHT_MODIFIER = Ingredient.ofItem(Items.GLOWSTONE_DUST);
	public static final Ingredient REDSTONE_MODIFIER = Ingredient.ofItem(Items.REDSTONE);
	public static final Ingredient FOOD_MODIFIER = Ingredient.ofItem(Items.SUGAR);
	public static final Ingredient GROW_YOUR_CRYSTAL_TOOLKIT = Ingredient.ofItem(ModItems.GROW_YOUR_CRYSTAL_TOOLKIT);


	public CrystallineSolutionRecipe(CraftingRecipeCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingRecipeInput input, World world) {
		if (input.getStackCount() < 2 || input.getStackCount() > 3) {
			return false;
		} else {
			boolean hasRedstone = false;
			boolean hasGlowstone = false;
			boolean hasSugar = false;
			boolean hasDye = false;
			boolean hasToolkit = false;

			for (int i = 0; i < input.size(); i++) {
				ItemStack itemStack = input.getStackInSlot(i);
				if (!itemStack.isEmpty()) {
					if (LIGHT_MODIFIER.test(itemStack)) {
						if (hasGlowstone) {
							return false;
						}

						hasGlowstone = true;
					} else if (REDSTONE_MODIFIER.test(itemStack)) {
						if (hasRedstone) {
							return false;
						}


						hasRedstone = true;
					} else if (GROW_YOUR_CRYSTAL_TOOLKIT.test(itemStack)) {
						if (hasToolkit) {
							return false;
						}


						hasToolkit = true;
					} else if (FOOD_MODIFIER.test(itemStack)) {
						if (hasSugar) {
							return false;
						}


						hasSugar = true;
					} else {
						if (!(itemStack.getItem() instanceof DyeItem)) {
							return false;
						}


						hasDye = true;
					}
				}
			}

			return hasToolkit && hasDye;
		}
	}

	@Override
	public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
		boolean hasRedstone = false;
		boolean hasGlowstone = false;
		boolean hasSugar = false;

		DyeColor color = DyeColor.WHITE;

		for (int i = 0; i < input.size(); i++) {
			ItemStack itemStack = input.getStackInSlot(i);
			if (!itemStack.isEmpty()) {
				if (LIGHT_MODIFIER.test(itemStack)) {
					hasGlowstone = true;
				} else if (REDSTONE_MODIFIER.test(itemStack)) {
					hasRedstone = true;
				} else if (FOOD_MODIFIER.test(itemStack)) {
					hasSugar = true;
				} else if (itemStack.getItem() instanceof DyeItem dyeItem) {
					color = dyeItem.getColor();
				}
			}
		}

		ItemStack itemStack = new ItemStack(ModItems.CRYSTALLINE_SOLUTION);
		itemStack.set(ModComponents.CRYSTALLINE_SOLUTION_COMPONENT, new CrystallineSolutionComponent(color, hasGlowstone, hasRedstone, hasSugar));
		return itemStack;
	}

	@Override
	public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
		return ModRecipeSerializer.CRYSTALLINE_SOLUTION;
	}

	@Override
	public DefaultedList<ItemStack> getRecipeRemainders(CraftingRecipeInput inventory) {
		DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);

		for (int i = 0; i < defaultedList.size(); ++i) {
			ItemStack stack = inventory.getStackInSlot(i);
			Item item = stack.getItem();
			if (item instanceof GrowYourCrystalToolkitItem) {
				int newDamage = stack.getDamage() + 1;
				if (newDamage < stack.getMaxDamage()) {
					stack = stack.copy();
					stack.setDamage(newDamage);
					defaultedList.set(i, stack);
				}
			} else {
				defaultedList.set(i, stack.getRecipeRemainder());
			}
		}

		return defaultedList;
	}
}
