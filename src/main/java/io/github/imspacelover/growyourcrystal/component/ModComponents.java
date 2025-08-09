package io.github.imspacelover.growyourcrystal.component;

import io.github.imspacelover.growyourcrystal.GrowYourCrystal;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModComponents {

	public static final ComponentType<CrystallineSolutionComponent> CRYSTALLINE_SOLUTION_COMPONENT = Registry.register(
		Registries.DATA_COMPONENT_TYPE,
		Identifier.of(GrowYourCrystal.ID, "crystalline_solution_component"),
		ComponentType.<CrystallineSolutionComponent>builder().codec(CrystallineSolutionComponent.CODEC).build()
	);

	public static void initialize() {
		GrowYourCrystal.LOGGER.info("Registering {} components", GrowYourCrystal.ID);
	}
}
