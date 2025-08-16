//package io.github.imspacelover.growyourcrystal.client.datagen;
//
//import io.github.imspacelover.growyourcrystal.block.ModBlocks;
//import io.github.imspacelover.growyourcrystal.item.ModItems;
//import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
//import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
//import net.minecraft.data.recipe.RecipeExporter;
//import net.minecraft.data.recipe.RecipeGenerator;
//import net.minecraft.data.recipe.StonecuttingRecipeJsonBuilder;
//import net.minecraft.item.ItemConvertible;
//import net.minecraft.item.ItemStack;
//import net.minecraft.recipe.Ingredient;
//import net.minecraft.recipe.book.RecipeCategory;
//import net.minecraft.registry.RegistryWrapper;
//
//import java.util.concurrent.CompletableFuture;
//
//public class ModRecipeProvider extends FabricRecipeProvider {
//	public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
//		super(output, registriesFuture);
//	}
//
//	@Override
//	protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
//		return new RecipeGenerator(registryLookup, exporter) {
//			@Override
//			public void generate() {
//				StonecuttingRecipeJsonBuilder.createStonecutting(
//					Ingredient.ofItem(ModBlocks.CRYSTAL_CLUSTER_BLOCK.asItem()),
//					RecipeCategory.BUILDING_BLOCKS,
//					new ItemStack(ModBlocks.CRYSTAL_BLOCK).;
//			}
//		};
//	}
//
//	@Override
//	public String getName() {
//		return "";
//	}
//}
