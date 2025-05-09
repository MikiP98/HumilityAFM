package io.github.mikip98.humilityafm.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static io.github.mikip98.humilityafm.HumilityAFM.MOD_ID;

public abstract class AFMRecipieProvider extends FabricRecipeProvider {
    public AFMRecipieProvider(FabricDataOutput output) { super(output); }

    protected static void offerCabinetRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible output, ItemConvertible slab, ItemConvertible carpet, String path_prefix) {
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

    protected static void offerWoodenMosaicRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible output, ItemConvertible plank1, ItemConvertible plank2, String path_prefix) {
        offerCheckerPatternRecipe(exporter, output, plank1, plank2, MOD_ID + "/wooden_mosaics", path_prefix);
    }
    protected static void offerTerracottaTileRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible output, ItemConvertible terracotta1, ItemConvertible terracotta2, String path_prefix) {
        offerCheckerPatternRecipe(exporter, output, terracotta1, terracotta2, MOD_ID + "/terracotta_tiles", path_prefix);
    }

    protected static void offerCheckerPatternRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible output, ItemConvertible input1, ItemConvertible input2, String group, String path_prefix) {
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

    protected static void offerChangeRecipie(Consumer<RecipeJsonProvider> exporter, ItemConvertible output, ItemConvertible input, String group, String path_prefix) {
        ShapelessRecipeJsonBuilder
                .create(RecipeCategory.MISC, output, 1)
                .input(input)
                .group(group)
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }

    protected static void offerColorChangeRecipie(Consumer<RecipeJsonProvider> exporter, ItemConvertible output, Ingredient input, ItemConvertible dye, String group, String path_prefix) {
        ShapelessRecipeJsonBuilder
                .create(RecipeCategory.MISC, output, 1)
                .input(input)
                .input(dye)
                .group(group)
                .criterion(hasItem(dye), conditionsFromItem(dye))
                .offerTo(exporter, path_prefix + getRecipeName(output) + "_color_change");
    }

    protected static void offerIlluminatedCabinetRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible illuminated_cabinet, ItemConvertible cabinet) {
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

    protected static void offerDoubleInputShapelessRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible output, ItemConvertible input1, ItemConvertible input2, @Nullable String group, int outputCount, String path_prefix) {
        ShapelessRecipeJsonBuilder
                .create(RecipeCategory.MISC, output, outputCount)
                .input(input1)
                .input(input2)
                .group(group)
                .criterion(hasItem(input1), conditionsFromItem(input1))
                .criterion(hasItem(input2), conditionsFromItem(input2))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }
}
