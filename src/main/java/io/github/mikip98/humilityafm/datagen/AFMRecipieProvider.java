package io.github.mikip98.humilityafm.datagen;

import net.minecraft.block.Blocks;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import static io.github.mikip98.humilityafm.HumilityAFM.MOD_ID;

public abstract class AFMRecipieProvider extends RecipeGenerator {
    protected AFMRecipieProvider(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        super(registries, exporter);
    }

    protected void offerCabinetRecipe(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter, ItemConvertible output, ItemConvertible slab, ItemConvertible carpet, String path_prefix) {
        ShapedRecipeJsonBuilder
                .create(itemLookup, RecipeCategory.MISC, output, 1)
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

    protected void offerAlternateWoodenMosaicRecipe(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter, ItemConvertible output, ItemConvertible plank1, ItemConvertible plank2, String path_prefix) {
        ShapedRecipeJsonBuilder
                .create(itemLookup, RecipeCategory.MISC, output, 1)
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
    protected void offerWoodenMosaicRecipe(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter, ItemConvertible output, ItemConvertible plank1, ItemConvertible plank2, String path_prefix) {
        offerCheckerPatternRecipe(itemLookup, exporter, output, plank1, plank2, MOD_ID + "/wooden_mosaics", path_prefix);
    }
    protected void offerTerracottaTileRecipe(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter, ItemConvertible output, ItemConvertible terracotta1, ItemConvertible terracotta2, String path_prefix) {
        offerCheckerPatternRecipe(itemLookup, exporter, output, terracotta1, terracotta2, MOD_ID + "/terracotta_tiles", path_prefix);
    }

    protected void offerCheckerPatternRecipe(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter, ItemConvertible output, ItemConvertible input1, ItemConvertible input2, String group, String path_prefix) {
        ShapedRecipeJsonBuilder
                .create(itemLookup, RecipeCategory.MISC, output, 1)
                .pattern("FS")
                .pattern("SF")
                .input('F', input1)
                .input('S', input2)
                .group(group)
                .criterion(hasItem(input1), conditionsFromItem(input1))
                .criterion(hasItem(input2), conditionsFromItem(input2))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }

    protected void offerChangeRecipie(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter, ItemConvertible output, ItemConvertible input, String group, String path_prefix) {
        ShapelessRecipeJsonBuilder
                .create(itemLookup, RecipeCategory.MISC, output, 1)
                .input(input)
                .group(group)
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }

    protected void offerColorChangeRecipie(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter, ItemConvertible output, Ingredient input, ItemConvertible dye, String group, String path_prefix) {
        ShapelessRecipeJsonBuilder
                .create(itemLookup, RecipeCategory.MISC, output, 1)
                .input(input)
                .input(dye)
                .group(group)
                .criterion(hasItem(dye), conditionsFromItem(dye))
                .offerTo(exporter, path_prefix + getRecipeName(output) + "_color_change");
    }

    protected void offerIlluminatedCabinetRecipe(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter, ItemConvertible illuminated_cabinet, ItemConvertible cabinet) {
        offerDoubleInputShapelessRecipe(
                itemLookup, exporter,
                illuminated_cabinet,
                cabinet,
                Items.GLOW_INK_SAC,
                MOD_ID + "/illuminated_cabinets",
                1,
                "illuminated_cabinets/"
        );
    }

    protected void offerDoubleInputShapelessRecipe(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter, ItemConvertible output, ItemConvertible input1, ItemConvertible input2, @Nullable String group, int outputCount, String path_prefix) {
        ShapelessRecipeJsonBuilder
                .create(itemLookup, RecipeCategory.MISC, output, outputCount)
                .input(input1)
                .input(input2)
                .group(group)
                .criterion(hasItem(input1), conditionsFromItem(input1))
                .criterion(hasItem(input2), conditionsFromItem(input2))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }
    protected void offerTripleInputShapelessRecipe(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter, ItemConvertible output, ItemConvertible input1, ItemConvertible input2, ItemConvertible input3, @Nullable String group, int outputCount, String path_prefix) {
        ShapelessRecipeJsonBuilder
                .create(itemLookup, RecipeCategory.MISC, output, outputCount)
                .input(input1)
                .input(input2)
                .input(input3)
                .group(group)
                .criterion(hasItem(input1), conditionsFromItem(input1))
                .criterion(hasItem(input2), conditionsFromItem(input2))
                .criterion(hasItem(input3), conditionsFromItem(input3))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }

    protected void offerColouredTorchRecipe(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter, ItemConvertible output, ItemConvertible glowingPowder, int glowingPowderAmount, @Nullable String group, String path_prefix) {
        ShapelessRecipeJsonBuilder
                .create(itemLookup, RecipeCategory.MISC, output, 2)
                .input(Items.STICK)
                .input(Items.GLOW_INK_SAC)
                .input(glowingPowder, glowingPowderAmount)
                .group(group)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .criterion(hasItem(Items.QUARTZ), conditionsFromItem(Items.QUARTZ))
                .criterion(hasItem(glowingPowder), conditionsFromItem(glowingPowder))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }
    protected void offerColouredTorchUpgradeRecipe(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter, ItemConvertible output, ItemConvertible weakerColouredTorch, ItemConvertible glowingPowder, int glowingPowderAmount, @Nullable String group, String path_prefix) {
        ShapelessRecipeJsonBuilder
                .create(itemLookup, RecipeCategory.MISC, output)
                .input(weakerColouredTorch)
                .input(glowingPowder, glowingPowderAmount)
                .group(group)
                .criterion(hasItem(weakerColouredTorch), conditionsFromItem(weakerColouredTorch))
                .criterion(hasItem(glowingPowder), conditionsFromItem(glowingPowder))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }

    protected void offerCandlestickRecipie(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter, ItemConvertible output, ItemConvertible ingot, String path_prefix) {
        ShapedRecipeJsonBuilder
                .create(itemLookup, RecipeCategory.DECORATIONS, output)
                .pattern("I ")
                .pattern("II")
                .input('I', ingot)
                .group(MOD_ID + "/candlestick")
                .criterion(hasItem(ingot), conditionsFromItem(ingot))
                .offerTo(exporter, path_prefix + "classic/" + getRecipeName(output));
        ShapedRecipeJsonBuilder
                .create(itemLookup, RecipeCategory.DECORATIONS, output)
                .pattern(" I")
                .pattern("II")
                .input('I', ingot)
                .group(MOD_ID + "/candlestick")
                .criterion(hasItem(ingot), conditionsFromItem(ingot))
                .offerTo(exporter, path_prefix + "reversed/" + getRecipeName(output));
    }

    protected void offerLightStripRecipie(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter, ItemConvertible output, ItemConvertible glowingPowder, String path_prefix) {
        ShapedRecipeJsonBuilder
                .create(itemLookup, RecipeCategory.DECORATIONS, output, 2)
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
                .create(itemLookup, RecipeCategory.DECORATIONS, output, 2)
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
