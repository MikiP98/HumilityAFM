package io.github.mikip98.humilityafm.datagen;

#if MC_VERSION < 12104
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
#endif
import net.minecraft.block.Blocks;
#if MC_VERSION < 12004
import net.minecraft.data.server.recipe.RecipeJsonProvider;
#elif MC_VERSION < 12104
import net.minecraft.data.server.recipe.RecipeExporter;
#endif
#if MC_VERSION < 12104
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
#else
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
#endif
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
#if MC_VERSION >= 12006
import net.minecraft.registry.RegistryWrapper;
#endif
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

#if MC_VERSION == 12001
import java.util.function.Consumer;
#endif
#if MC_VERSION >= 12006 && MC_VERSION < 12104
import java.util.concurrent.CompletableFuture;
#endif

import static io.github.mikip98.humilityafm.HumilityAFM.MOD_ID;

public abstract class AFMRecipeProvider extends #if MC_VERSION < 12104 FabricRecipeProvider #else RecipeGenerator #endif {
    #if MC_VERSION < 12006
    public AFMRecipeProvider(FabricDataOutput output) { super(output); }
    #elif MC_VERSION < 12104
    public AFMRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }
    #else
    protected AFMRecipeProvider(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        super(registries, exporter);
    }
    #endif

    protected void offerCabinetRecipe(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter,
            ItemConvertible output, ItemConvertible slab, ItemConvertible carpet, String path_prefix
    ) {
        ShapedRecipeJsonBuilder
                .create(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.MISC, output, 1)
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

    protected void offerAlternateWoodenMosaicRecipe(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter,
            ItemConvertible output, ItemConvertible plank1, ItemConvertible plank2, String path_prefix
    ) {
        ShapedRecipeJsonBuilder
                .create(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.MISC, output, 1)
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
    protected void offerWoodenMosaicRecipe(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter,
            ItemConvertible output, ItemConvertible plank1, ItemConvertible plank2, String path_prefix
    ) {
        offerCheckerPatternRecipe(#if MC_VERSION >= 12104 itemLookup, #endif exporter, output, plank1, plank2, MOD_ID + "/wooden_mosaics", path_prefix);
    }
    protected void offerTerracottaTileRecipe(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter,
            ItemConvertible output, ItemConvertible terracotta1, ItemConvertible terracotta2, String path_prefix
    ) {
        offerCheckerPatternRecipe(#if MC_VERSION >= 12104 itemLookup, #endif exporter, output, terracotta1, terracotta2, MOD_ID + "/terracotta_tiles", path_prefix);
    }

    protected void offerCheckerPatternRecipe(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter,
            ItemConvertible output, ItemConvertible input1, ItemConvertible input2, String group, String path_prefix
    ) {
        ShapedRecipeJsonBuilder
                .create(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.MISC, output, 1)
                .pattern("FS")
                .pattern("SF")
                .input('F', input1)
                .input('S', input2)
                .group(group)
                .criterion(hasItem(input1), conditionsFromItem(input1))
                .criterion(hasItem(input2), conditionsFromItem(input2))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }

    protected void offerChangeRecipie(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter,
            ItemConvertible output, ItemConvertible input, String group, String path_prefix
    ) {
        ShapelessRecipeJsonBuilder
                .create(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.MISC, output, 1)
                .input(input)
                .group(group)
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }

    protected void offerColorChangeRecipie(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter,
            ItemConvertible output, Ingredient input, ItemConvertible dye, String group, String path_prefix
    ) {
        ShapelessRecipeJsonBuilder
                .create(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.MISC, output, 1)
                .input(input)
                .input(dye)
                .group(group)
                .criterion(hasItem(dye), conditionsFromItem(dye))
                .offerTo(exporter, path_prefix + getRecipeName(output) + "_color_change");
    }

    protected void offerIlluminatedCabinetRecipe(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter,
            ItemConvertible illuminated_cabinet, ItemConvertible cabinet
    ) {
        offerDoubleInputShapelessRecipe(
                #if MC_VERSION >= 12104 itemLookup, #endif
                exporter,
                illuminated_cabinet,
                cabinet,
                Items.GLOW_INK_SAC,
                MOD_ID + "/illuminated_cabinets",
                1,
                "illuminated_cabinets/"
        );
    }

    protected void offerDoubleInputShapelessRecipe(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter,
            ItemConvertible output, ItemConvertible input1, ItemConvertible input2,
            @Nullable String group, int outputCount, String path_prefix
    ) {
        ShapelessRecipeJsonBuilder
                .create(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.MISC, output, outputCount)
                .input(input1)
                .input(input2)
                .group(group)
                .criterion(hasItem(input1), conditionsFromItem(input1))
                .criterion(hasItem(input2), conditionsFromItem(input2))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }
    protected void offerTripleInputShapelessRecipe(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter,
            ItemConvertible output, ItemConvertible input1, ItemConvertible input2, ItemConvertible input3,
            @Nullable String group, int outputCount, String path_prefix
    ) {
        ShapelessRecipeJsonBuilder
                .create(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.MISC, output, outputCount)
                .input(input1)
                .input(input2)
                .input(input3)
                .group(group)
                .criterion(hasItem(input1), conditionsFromItem(input1))
                .criterion(hasItem(input2), conditionsFromItem(input2))
                .criterion(hasItem(input3), conditionsFromItem(input3))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }

    protected void offerColouredTorchRecipe(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter,
            ItemConvertible output, ItemConvertible glowingPowder, int glowingPowderAmount,
            @Nullable String group, String path_prefix
    ) {
        ShapelessRecipeJsonBuilder
                .create(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.MISC, output, 2)
                .input(Items.STICK)
                .input(Items.GLOW_INK_SAC)
                .input(glowingPowder, glowingPowderAmount)
                .group(group)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .criterion(hasItem(Items.QUARTZ), conditionsFromItem(Items.QUARTZ))
                .criterion(hasItem(glowingPowder), conditionsFromItem(glowingPowder))
                .offerTo(exporter, path_prefix + getRecipeName(output));
    }

    protected void offerCandlestickRecipie(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter,
            ItemConvertible output, ItemConvertible ingot, String path_prefix
    ) {
        ShapedRecipeJsonBuilder
                .create(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.DECORATIONS, output)
                .pattern("I ")
                .pattern("II")
                .input('I', ingot)
                .group(MOD_ID + "/candlestick")
                .criterion(hasItem(ingot), conditionsFromItem(ingot))
                .offerTo(exporter, path_prefix + "classic/" + getRecipeName(output));
        ShapedRecipeJsonBuilder
                .create(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.DECORATIONS, output)
                .pattern(" I")
                .pattern("II")
                .input('I', ingot)
                .group(MOD_ID + "/candlestick")
                .criterion(hasItem(ingot), conditionsFromItem(ingot))
                .offerTo(exporter, path_prefix + "reversed/" + getRecipeName(output));
    }

    protected void offerLightStripRecipie(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter,
            ItemConvertible output, ItemConvertible glowingPowder, String path_prefix
    ) {
        ShapedRecipeJsonBuilder
                .create(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.DECORATIONS, output, 2)
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
                .create(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.DECORATIONS, output, 2)
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
