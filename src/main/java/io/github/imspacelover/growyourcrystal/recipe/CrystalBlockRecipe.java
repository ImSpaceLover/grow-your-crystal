package io.github.imspacelover.growyourcrystal.recipe;

import io.github.imspacelover.growyourcrystal.block.ModBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import java.util.Map;

public class CrystalBlockRecipe extends SpecialCraftingRecipe {
	public CrystalBlockRecipe(CraftingRecipeCategory category) {
		super(category);
	}


	// TODO: Make data driven recipes for more block crafts
	@Override
	public boolean matches(CraftingRecipeInput input, World world) {
		RawShapedRecipe recipe = RawShapedRecipe.create(
			Map.of('#', Ingredient.ofItem(ModBlocks.CRYSTAL_CLUSTER_BLOCK)),
			"##",
					"##");
		return recipe.matches(input);
	}

	@Override
	public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
		return input.getStackInSlot(0).copyComponentsToNewStack(ModBlocks.CRYSTAL_BLOCK.asItem(), 4);
	}

	@Override
	public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
		return ModRecipeSerializer.CRYSTAL_BLOCK;
	}

//	public ItemStack result;
//
//	public CrystalBlockRecipe(String group, CraftingRecipeCategory category, RawShapedRecipe raw, ItemStack result, boolean showNotification) {
//		super(group, category, raw, result, showNotification);
//		this.result = result;
//	}
//
//	@Override
//	public ItemStack craft(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
//		return craftingRecipeInput.getStackInSlot(1).copyComponentsToNewStack(this.result.getItem(), 1);
//	}


}
