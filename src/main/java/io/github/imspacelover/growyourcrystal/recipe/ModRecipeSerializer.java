package io.github.imspacelover.growyourcrystal.recipe;

import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import net.minecraft.recipe.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public interface ModRecipeSerializer<T extends Recipe<?>> {

	RecipeSerializer<CrystalBlockRecipe> CRYSTAL_BLOCK = register(
		"crafting_special_crystal_block", new SpecialCraftingRecipe.SpecialRecipeSerializer<>(CrystalBlockRecipe::new));

	RecipeSerializer<CrystallineSolutionRecipe> CRYSTALLINE_SOLUTION = register(
		"crafting_special_crystalline_solution", new SpecialCraftingRecipe.SpecialRecipeSerializer<>(CrystallineSolutionRecipe::new)
	);

	RecipeSerializer<CrystalSeedRecipe> CRYSTAL_SEED = register(
		"crafting_special_crystal_seed", new SpecialCraftingRecipe.SpecialRecipeSerializer<>(CrystalSeedRecipe::new));

	static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
		return Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(GrowYourCrystal.ID, id), serializer);
	}

	static void initialize() {
		GrowYourCrystal.LOGGER.info("Registering {} recipies", GrowYourCrystal.ID);
	}
}
