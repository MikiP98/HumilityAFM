package io.github.mikip98.humilityafm.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import static io.github.mikip98.humilityafm.HumilityAFM.MOD_ID;

public abstract class AFMRecipieProvider extends FabricRecipeProvider {
    public AFMRecipieProvider(FabricDataOutput output) { super(output); }

    protected static void offerCabinetRecipe(RecipeExporter exporter, ItemConvertible output, ItemConvertible slab, ItemConvertible carpet, String path_prefix) {
        ShapedRecipeJsonBuilder
                .create(RecipeCategory.MISC, output, 1)
                .pattern(" G ")
                .pattern("SCS")
                .pattern(" S ")
                .input('G', Items.GLASS_PANE)
                .input('S', slab)
                .input('C', carpet)
                .group(MOD_ID + "/cabinets")
                .criterion(hasItem(Items.GLASS_PANE), conditionsFromItem(Items.GLASS_PANE))
                .criterion(hasItem(slab), conditionsFromItem(slab))
                .criterion(hasItem(carpet), conditionsFromItem(carpet))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }

    protected static void  offerAlternateWoodenMosaicRecipe(RecipeExporter exporter, ItemConvertible output, ItemConvertible plank1, ItemConvertible plank2, String path_prefix) {
        ShapedRecipeJsonBuilder
                .create(RecipeCategory.MISC, output, 1)
                .pattern("FS")
                .pattern("  ")
                .pattern("SF")
                .input('F', plank1)
                .input('S', plank2)
                .group(MOD_ID + "/wooden_mosaics")
                .criterion(hasItem(plank1), conditionsFromItem(plank1))
                .criterion(hasItem(plank2), conditionsFromItem(plank2))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }
    protected static void offerWoodenMosaicRecipe(RecipeExporter exporter, ItemConvertible output, ItemConvertible plank1, ItemConvertible plank2, String path_prefix) {
        offerCheckerPatternRecipe(exporter, output, plank1, plank2, MOD_ID + "/wooden_mosaics", path_prefix);
    }
    protected static void offerTerracottaTileRecipe(RecipeExporter exporter, ItemConvertible output, ItemConvertible terracotta1, ItemConvertible terracotta2, String path_prefix) {
        offerCheckerPatternRecipe(exporter, output, terracotta1, terracotta2, MOD_ID + "/terracotta_tiles", path_prefix);
    }

    protected static void offerCheckerPatternRecipe(RecipeExporter exporter, ItemConvertible output, ItemConvertible input1, ItemConvertible input2, String group, String path_prefix) {
        ShapedRecipeJsonBuilder
                .create(RecipeCategory.MISC, output, 1)
                .pattern("FS")
                .pattern("SF")
                .input('F', input1)
                .input('S', input2)
                .group(group)
                .criterion(hasItem(input1), conditionsFromItem(input1))
                .criterion(hasItem(input2), conditionsFromItem(input2))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }

    protected static void offerChangeRecipie(RecipeExporter exporter, ItemConvertible output, ItemConvertible input, String group, String path_prefix) {
        ShapelessRecipeJsonBuilder
                .create(RecipeCategory.MISC, output, 1)
                .input(input)
                .group(group)
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }

    protected static void offerColorChangeRecipie(RecipeExporter exporter, ItemConvertible output, Ingredient input, ItemConvertible dye, String group, String path_prefix) {
        ShapelessRecipeJsonBuilder
                .create(RecipeCategory.MISC, output, 1)
                .input(input)
                .input(dye)
                .group(group)
                .criterion(hasItem(dye), conditionsFromItem(dye))
                .offerTo(exporter, path_prefix + getRecipeName(output) + "_color_change");
    }

    protected static void offerIlluminatedCabinetRecipe(RecipeExporter exporter, ItemConvertible illuminated_cabinet, ItemConvertible cabinet) {
        offerDoubleInputShapelessRecipe(
                exporter,
                illuminated_cabinet,
                cabinet,
                Items.GLOW_INK_SAC,
                MOD_ID + "/illuminated_cabinets",
                1,
                "illuminated_cabinets/"
        );
    }

    protected static void offerDoubleInputShapelessRecipe(RecipeExporter exporter, ItemConvertible output, ItemConvertible input1, ItemConvertible input2, @Nullable String group, int outputCount, String path_prefix) {
        ShapelessRecipeJsonBuilder
                .create(RecipeCategory.MISC, output, outputCount)
                .input(input1)
                .input(input2)
                .group(group)
                .criterion(hasItem(input1), conditionsFromItem(input1))
                .criterion(hasItem(input2), conditionsFromItem(input2))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }
    protected static void offerTripleInputShapelessRecipe(RecipeExporter exporter, ItemConvertible output, ItemConvertible input1, ItemConvertible input2, ItemConvertible input3, @Nullable String group, int outputCount, String path_prefix) {
        ShapelessRecipeJsonBuilder
                .create(RecipeCategory.MISC, output, outputCount)
                .input(input1)
                .input(input2)
                .input(input3)
                .group(group)
                .criterion(hasItem(input1), conditionsFromItem(input1))
                .criterion(hasItem(input2), conditionsFromItem(input2))
                .criterion(hasItem(input3), conditionsFromItem(input3))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }

    protected static void offerColouredTorchRecipe(RecipeExporter exporter, ItemConvertible output, ItemConvertible glowingPowder, int glowingPowderAmount, @Nullable String group, String path_prefix) {
        ShapelessRecipeJsonBuilder
                .create(RecipeCategory.MISC, output, 2)
                .input(Items.STICK)
                .input(Items.GLOW_INK_SAC)
                .input(glowingPowder, glowingPowderAmount)
                .group(group)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .criterion(hasItem(Items.QUARTZ), conditionsFromItem(Items.QUARTZ))
                .criterion(hasItem(glowingPowder), conditionsFromItem(glowingPowder))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }
    protected static void offerColouredTorchUpgradeRecipe(RecipeExporter exporter, ItemConvertible output, ItemConvertible weakerColouredTorch, ItemConvertible glowingPowder, int glowingPowderAmount, @Nullable String group, String path_prefix) {
        ShapelessRecipeJsonBuilder
                .create(RecipeCategory.MISC, output)
                .input(weakerColouredTorch)
                .input(glowingPowder, glowingPowderAmount)
                .group(group)
                .criterion(hasItem(weakerColouredTorch), conditionsFromItem(weakerColouredTorch))
                .criterion(hasItem(glowingPowder), conditionsFromItem(glowingPowder))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }

    protected static void offerCandlestickRecipie(RecipeExporter exporter, ItemConvertible output, ItemConvertible ingot, String path_prefix) {
        ShapedRecipeJsonBuilder
                .create(RecipeCategory.DECORATIONS, output)
                .pattern("I ")
                .pattern("II")
                .input('I', ingot)
                .group(MOD_ID + "/candlestick")
                .criterion(hasItem(ingot), conditionsFromItem(ingot))
                .offerTo(exporter, path_prefix + "classic/" + getRecipeName(output));
        ShapedRecipeJsonBuilder
                .create(RecipeCategory.DECORATIONS, output)
                .pattern(" I")
                .pattern("II")
                .input('I', ingot)
                .group(MOD_ID + "/candlestick")
                .criterion(hasItem(ingot), conditionsFromItem(ingot))
                .offerTo(exporter, path_prefix + "reversed/" + getRecipeName(output));
    }

    protected static void offerLightStripRecipie(RecipeExporter exporter, ItemConvertible output, ItemConvertible glowingPowder, String path_prefix) {
        ShapedRecipeJsonBuilder
                .create(RecipeCategory.DECORATIONS, output, 2)
                .pattern("QQQ")
                .pattern("PPP")
                .pattern("GGG")
                .input('Q', Items.QUARTZ)
                .input('P', glowingPowder)
                .input('G', Blocks.GLASS_PANE)
                .group(MOD_ID + "/light_strip")
                .criterion(hasItem(Items.QUARTZ), conditionsFromItem(Items.QUARTZ))
                .criterion(hasItem(glowingPowder), conditionsFromItem(glowingPowder))
                .criterion(hasItem(Blocks.GLASS_PANE), conditionsFromItem(Blocks.GLASS_PANE))
                .offerTo(exporter, path_prefix + "classic/" + getRecipeName(output));
        ShapedRecipeJsonBuilder
                .create(RecipeCategory.DECORATIONS, output, 2)
                .pattern("GGG")
                .pattern("PPP")
                .pattern("QQQ")
                .input('Q', Items.QUARTZ)
                .input('P', glowingPowder)
                .input('G', Blocks.GLASS_PANE)
                .group(MOD_ID + "/light_strip")
                .criterion(hasItem(Items.QUARTZ), conditionsFromItem(Items.QUARTZ))
                .criterion(hasItem(glowingPowder), conditionsFromItem(glowingPowder))
                .criterion(hasItem(Blocks.GLASS_PANE), conditionsFromItem(Blocks.GLASS_PANE))
                .offerTo(exporter, path_prefix + "reversed/" + getRecipeName(output));
    }

    // Override for the vanilla method to be mod safe
    public static String hasItem(ItemConvertible item) {
        final Identifier itemId = Registries.ITEM.getId(item.asItem());
        return "has_" + itemId.getNamespace() + ":" + itemId.getPath();
    }
}
