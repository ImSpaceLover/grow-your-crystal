package io.github.imspacelover.growyourcrystal.recipe;

import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import io.github.imspacelover.growyourcrystal.component.CrystalItemComponent;
import io.github.imspacelover.growyourcrystal.component.ModComponents;
import io.github.imspacelover.growyourcrystal.item.ModItems;
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
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CrystallineSolutionRecipe extends SpecialCraftingRecipe {


	// TODO: MAKE INGREDIENTS USE TAGS INSTEAD
	public static final Ingredient LIGHT_MODIFIER = Ingredient.ofItems(
		Stream.of(
			Items.GLOWSTONE_DUST,
			Items.GLOW_INK_SAC
		));

	public static final Ingredient REDSTONE_MODIFIER = Ingredient.ofItem(Items.REDSTONE);
	public static final Ingredient FOOD_MODIFIER = Ingredient.ofItem(Items.SUGAR);
	public static final Ingredient CRYSTAL_GROWING_TOOLKIT = Ingredient.ofItem(ModItems.CRYSTAL_GROWING_TOOLKIT);
	public static final Ingredient CRYSTAL_BASE_INGREDIENT = Ingredient.ofItems(
		Stream.of(
			Items.QUARTZ,
			Items.AMETHYST_SHARD,
			Items.DIAMOND,
			ModItems.CREATIVE_CRYSTAL
		));

	protected enum CRYSTAL_TYPE {
		NORMAL,
		CREATIVE
	}

	public CrystallineSolutionRecipe(CraftingRecipeCategory category) {
		super(category);
	}
	@Override
	public boolean matches(CraftingRecipeInput input, World world) {
		if (input.getStackCount() < 3) {
			return false;
		} else {
			int redstoneModifiers = 0;
			int lightModifiers = 0;
			int foodModifiers = 0;
			int colorModifiers = 0;
			boolean hasCrystalBase = false;
			boolean hasToolkit = false;

			for (int i = 0; i < input.size(); i++) {
				ItemStack itemStack = input.getStackInSlot(i);
				if (!itemStack.isEmpty()) {
					if (LIGHT_MODIFIER.test(itemStack)) {
						GrowYourCrystal.LOGGER.info(String.format("item in slot %d is glowstone", i));
						if (lightModifiers > 4) {
							return false;
						}
						lightModifiers += 1;
					} else if (REDSTONE_MODIFIER.test(itemStack)) {
						GrowYourCrystal.LOGGER.info(String.format("item in slot %d is redstone", i));
						if (redstoneModifiers > 4) {
							return false;
						}
						redstoneModifiers += 1;
					} else if (FOOD_MODIFIER.test(itemStack)) {
						GrowYourCrystal.LOGGER.info(String.format("item in slot %d is sugar", i));
						if (foodModifiers > 4) {
							return false;
						}
						foodModifiers += 1;
					} else if (itemStack.getItem() instanceof DyeItem) {
						GrowYourCrystal.LOGGER.info(String.format("item in slot %d is a dye", i));
						if (colorModifiers > 1) {
							return false;
						}
						colorModifiers += 1;
					} else if (CRYSTAL_BASE_INGREDIENT.test(itemStack)){
						GrowYourCrystal.LOGGER.info(String.format("item in slot %d is a crystal base", i));
						if (hasCrystalBase) {
							return false;
						}
						hasCrystalBase = true;
					} else {
						if (CRYSTAL_GROWING_TOOLKIT.test(itemStack)) {
							GrowYourCrystal.LOGGER.info(String.format("item in slot %d is the toolkit", i));
							if (hasToolkit) {
								return false ;
							}
							else {
								hasToolkit = true;
							}
						}
						else {
							return false;
						}
					}
				}
			}
			return hasToolkit && hasCrystalBase && colorModifiers > 0;
		}
	}

	@Override
	public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
		int redstoneModifiers = 0;
		int lightModifiers = 0;
		int foodModifiers = 0;
		CRYSTAL_TYPE crystalType = CRYSTAL_TYPE.NORMAL;
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
				if (itemStack.getItem().equals(ModItems.CREATIVE_CRYSTAL)) {
					crystalType = CRYSTAL_TYPE.CREATIVE;
				}
			}
		}

		int colorMix = mixColors(colors);

		ItemStack itemStack = crystalType == (CRYSTAL_TYPE.NORMAL) ? new ItemStack(ModItems.CRYSTALLINE_SOLUTION) : new ItemStack(ModItems.CREATIVE_SOLUTION);
		itemStack.set(ModComponents.CRYSTAL_ITEM_COMPONENT, new CrystalItemComponent(
			List.of(colorMix),
			lightModifiers,
			redstoneModifiers,
			foodModifiers));
		return itemStack;
	}

	private static int mixColors(List<Integer> colors) {
		int redSum = 0;
		int greenSum = 0;
		int blueSum = 0;
		int lightness = 0;
		int colorCount = 0;
		for (Integer color : colors) {
			int red = ColorHelper.getRed(color);
			int green = ColorHelper.getGreen(color);
			int blue = ColorHelper.getBlue(color);
			lightness += Math.max(red, Math.max(green, blue));
			redSum += red;
			greenSum += green;
			blueSum += blue;
			colorCount++;
		}

		int redResult = redSum / colorCount;
		int greenResult = greenSum / colorCount;
		int blueResult = blueSum / colorCount;
		float lightnessResult = (float)lightness / colorCount;
		float g = Math.max(redResult, Math.max(greenResult, blueResult));
		redResult = (int)(redResult * lightnessResult / g);
		greenResult = (int)(greenResult * lightnessResult / g);
		blueResult = (int)(blueResult * lightnessResult / g);
		int colorResult = ColorHelper.getArgb(0, redResult, greenResult, blueResult);
		return colorResult;
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
