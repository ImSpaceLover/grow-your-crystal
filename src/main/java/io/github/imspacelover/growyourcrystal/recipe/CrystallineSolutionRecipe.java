package io.github.imspacelover.growyourcrystal.recipe;

import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import io.github.imspacelover.growyourcrystal.component.CrystalItemComponent;
import io.github.imspacelover.growyourcrystal.component.ModComponents;
import io.github.imspacelover.growyourcrystal.item.GrowYourCrystalToolkitItem;
import io.github.imspacelover.growyourcrystal.item.ModItems;
import net.minecraft.component.type.FoodComponent;
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
import net.minecraft.util.Colors;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
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
		if (input.getStackCount() < 2) {
			return false;
		} else {
			int redstoneModifiers = 0;
			int lightModifiers = 0;
			int foodModifiers = 0;
			int colorModifiers = 0;
			boolean hasToolkit = false;

			for (int i = 0; i < input.size(); i++) {
				ItemStack itemStack = input.getStackInSlot(i);
				if (!itemStack.isEmpty()) {
					if (LIGHT_MODIFIER.test(itemStack)) {
						GrowYourCrystal.LOGGER.info(String.format("item in slot %d is glowstone", i));
						if (lightModifiers > 5) {
							return false;
						}
						lightModifiers += 1;
					} else if (REDSTONE_MODIFIER.test(itemStack)) {
						GrowYourCrystal.LOGGER.info(String.format("item in slot %d is redstone", i));
						if (redstoneModifiers > 5) {
							return false;
						}
						redstoneModifiers += 1;
					} else if (FOOD_MODIFIER.test(itemStack)) {
						GrowYourCrystal.LOGGER.info(String.format("item in slot %d is sugar", i));
						if (foodModifiers > 5) {
							return false;
						}
						foodModifiers += 1;
					} else if (itemStack.getItem() instanceof DyeItem) {
						GrowYourCrystal.LOGGER.info(String.format("item in slot %d is a dye", i));
						if (colorModifiers > 2) {
							return false;
						}
						colorModifiers += 1;
					} else {
						if (GROW_YOUR_CRYSTAL_TOOLKIT.test(itemStack)) {
							hasToolkit = true;
						} else {
							return false;
						}
						GrowYourCrystal.LOGGER.info(String.format("item in slot %d is a toolkit", i));
					}
				}
			}
			return hasToolkit && colorModifiers > 0;
		}
	}

	@Override
	public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
		int redstoneModifiers = 0;
		int lightModifiers = 0;
		int foodModifiers = 0;

		List<Integer> colors = new ArrayList<>();

		for (int i = 0; i < input.size(); i++) {
			ItemStack itemStack = input.getStackInSlot(i);
			if (!itemStack.isEmpty()) {
				lightModifiers += LIGHT_MODIFIER.test(itemStack) ? 1 : 0;
				redstoneModifiers += REDSTONE_MODIFIER.test(itemStack) ? 1 : 0;
				foodModifiers += FOOD_MODIFIER.test(itemStack) ? 1 : 0;
				if (itemStack.getItem() instanceof DyeItem dyeItem) {
					colors.add(dyeItem.getColor().getEntityColor());
				}
			}
		}

//		int colorMix = ColorHelper.mix(colors.getFirst(), colors.getLast());

		int colorMix = ColorHelper.average(colors.getFirst(), colors.getLast());
		ItemStack itemStack = new ItemStack(ModItems.CRYSTALLINE_SOLUTION);
		itemStack.set(ModComponents.CRYSTAL_ITEM_COMPONENT, new CrystalItemComponent(
			List.of(colorMix),
			lightModifiers,
			redstoneModifiers,
			new FoodComponent(foodModifiers, 1.2f, false)));
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
