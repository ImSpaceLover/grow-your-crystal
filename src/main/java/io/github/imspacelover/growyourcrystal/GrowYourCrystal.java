package io.github.imspacelover.growyourcrystal;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrowYourCrystal implements ModInitializer {
	public static final String ID = "grow_your_crystal";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@Override
	public void onInitialize() {
		LOGGER.info("[Grow Your Crystal] Hello, Minecraft!");
	}
}
