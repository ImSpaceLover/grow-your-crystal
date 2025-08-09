package io.github.imspacelover.growyourcrystal.item;

import io.github.imspacelover.growyourcrystal.component.CrystallineSolutionComponent;
import io.github.imspacelover.growyourcrystal.component.ModComponents;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.function.Consumer;

public class CrystallineSolutionItem extends Item {
	public CrystallineSolutionItem(Settings settings) {
		super(settings);
	}
}
