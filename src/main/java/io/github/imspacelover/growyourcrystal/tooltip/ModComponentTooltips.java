package io.github.imspacelover.growyourcrystal.tooltip;

import io.github.imspacelover.growyourcrystal.component.ModComponents;
import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;

public class ModComponentTooltips {

	public static void initialize() {
		ComponentTooltipAppenderRegistry.addLast(ModComponents.CRYSTAL_ITEM_COMPONENT);
	}
}
