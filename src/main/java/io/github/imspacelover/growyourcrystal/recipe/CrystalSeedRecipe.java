package io.github.imspacelover.growyourcrystal.recipe;

import io.github.imspacelover.growyourcrystal.block.ModBlocks;
import io.github.imspacelover.growyourcrystal.item.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class CrystalSeedRecipe extends SpecialCraftingRecipe {
	public static final Ingredient CRYSTAL_GROWING_TOOLKIT = Ingredient.ofItem(ModItems.CRYSTAL_GROWING_TOOLKIT);
	public static final Ingredient CRYSTAL_BASE_INGREDIENT = Ingredient.ofItems(
		Stream.of(
			Items.QUARTZ,
			Items.AMETHYST_SHARD,
			Items.DIAMOND,
			ModItems.CREATIVE_CRYSTAL
		));


	public CrystalSeedRecipe(CraftingRecipeCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingRecipeInput input, World world) {
		if (input.getStackCount() != 9) {
			return false;
		} else {
			boolean hasToolkit = false;
			for (int i = 0; i < input.size(); i++) {
				ItemStack itemStack = input.getStackInSlot(i);
				if (!itemStack.isEmpty()) {
					if (CRYSTAL_GROWING_TOOLKIT.test(itemStack)) {
						if (hasToolkit) {
							return false;
						}
						hasToolkit = true;
					} else if (!CRYSTAL_BASE_INGREDIENT.test(itemStack)) {
						return false;
					}
				}
			}
			return hasToolkit;
		}
	}

	@Override
	public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
		return new ItemStack(ModBlocks.CRYSTAL_SEED_BLOCK);
	}

	@Override
	public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
		return ModRecipeSerializer.CRYSTAL_SEED;
	}

	@Override
	public DefaultedList<ItemStack> getRecipeRemainders(CraftingRecipeInput inventory) {
		DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);

		for (int i = 0; i < defaultedList.size(); ++i) {
			ItemStack stack = inventory.getStackInSlot(i);
			Item item = stack.getItem();
			if (item == ModItems.CRYSTAL_GROWING_TOOLKIT) {
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
