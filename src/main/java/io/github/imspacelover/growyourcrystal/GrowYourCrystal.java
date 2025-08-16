package io.github.imspacelover.growyourcrystal;

import io.github.imspacelover.growyourcrystal.block.ModBlocks;
import io.github.imspacelover.growyourcrystal.blockentity.ModBlockEntities;
import io.github.imspacelover.growyourcrystal.component.ModComponents;
import io.github.imspacelover.growyourcrystal.item.ModItems;
import io.github.imspacelover.growyourcrystal.recipe.ModRecipeSerializer;
import io.github.imspacelover.growyourcrystal.tooltip.ModComponentTooltips;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrowYourCrystal implements ModInitializer {
	public static final String ID = "grow_your_crystal";
	public static final Logger LOGGER = LoggerFactory.getLogger("GrowYourCrystal");

	@Override
	public void onInitialize() {
		ModComponents.initialize();
		ModComponentTooltips.initialize();


		ModItems.initialize();

		ModBlockEntities.initialize();
		ModBlocks.initialize();

		ModRecipeSerializer.initialize();
	}
}
