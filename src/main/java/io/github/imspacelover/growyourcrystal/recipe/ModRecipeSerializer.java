package io.github.imspacelover.growyourcrystal.recipe;

import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public interface ModRecipeSerializer<T extends Recipe<?>> {

	RecipeSerializer<CrystallineSolutionRecipe> CRYSTALLINE_SOLUTION = register(
		"crafting_special_crystalline_solution", new SpecialCraftingRecipe.SpecialRecipeSerializer<>(CrystallineSolutionRecipe::new)
	);

	static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
		return Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(GrowYourCrystal.ID, id), serializer);
	}

	static void initialize() {
		GrowYourCrystal.LOGGER.info("Registering {} recipies", GrowYourCrystal.ID);
	}
}
