package io.github.mikip98.humilityafm.datagen;

#if MC_VERSION < 12104
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
#endif
import net.minecraft.core.registries.BuiltInRegistries;
#if MC_VERSION < 12004
import net.minecraft.data.recipes.FinishedRecipe;
#endif
import net.minecraft.data.recipes.RecipeCategory;
#if MC_VERSION >= 12004
import net.minecraft.data.recipes.RecipeOutput;
#endif
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

#if MC_VERSION < 12004
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
    public AFMRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }
    #else
    protected AFMRecipeProvider(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        super(registries, exporter);
    }
    #endif

    protected void offerCabinetRecipe(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION < 12004 Consumer<FinishedRecipe> #else RecipeOutput #endif exporter,
            ItemLike output, ItemLike slab, ItemLike carpet, String path_prefix
    ) {
        ShapedRecipeBuilder
                .shaped(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.MISC, output, 1)
                .pattern(" G ")
                .pattern("SCS")
                .pattern(" S ")
                .define('G', Items.GLASS_PANE)
                .define('S', slab)
                .define('C', carpet)
                .group(MOD_ID + "/cabinets")
                .unlockedBy(getHasName(Items.GLASS_PANE), has(Items.GLASS_PANE))
                .unlockedBy(getHasName(slab), has(slab))
                .unlockedBy(getHasName(carpet), has(carpet))
                .save(exporter, path_prefix + getItemName(output));
    }

    protected void offerAlternateWoodenMosaicRecipe(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif exporter,
            ItemLike output, ItemLike plank1, ItemLike plank2, String path_prefix
    ) {
        ShapedRecipeBuilder
                .shaped(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.MISC, output, 1)
                .pattern("FS")
                .pattern("  ")
                .pattern("SF")
                .define('F', plank1)
                .define('S', plank2)
                .group(MOD_ID + "/wooden_mosaics")
                .unlockedBy(getHasName(plank1), has(plank1))
                .unlockedBy(getHasName(plank2), has(plank2))
                .save(exporter, path_prefix + getItemName(output));
    }
    protected void offerWoodenMosaicRecipe(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif exporter,
            ItemLike output, ItemLike plank1, ItemLike plank2, String path_prefix
    ) {
        offerCheckerPatternRecipe(#if MC_VERSION >= 12104 itemLookup, #endif exporter, output, plank1, plank2, MOD_ID + "/wooden_mosaics", path_prefix);
    }
    protected void offerTerracottaTileRecipe(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif exporter,
            ItemLike output, ItemLike terracotta1, ItemLike terracotta2, String path_prefix
    ) {
        offerCheckerPatternRecipe(#if MC_VERSION >= 12104 itemLookup, #endif exporter, output, terracotta1, terracotta2, MOD_ID + "/terracotta_tiles", path_prefix);
    }

    protected void offerCheckerPatternRecipe(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif exporter,
            ItemLike output, ItemLike input1, ItemLike input2, String group, String path_prefix
    ) {
        ShapedRecipeBuilder
                .shaped(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.MISC, output, 1)
                .pattern("FS")
                .pattern("SF")
                .define('F', input1)
                .define('S', input2)
                .group(group)
                .unlockedBy(getHasName(input1), has(input1))
                .unlockedBy(getHasName(input2), has(input2))
                .save(exporter, path_prefix + getItemName(output));
    }

    protected void offerChangeRecipie(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif exporter,
            ItemLike output, ItemLike input, String group, String path_prefix
    ) {
        ShapelessRecipeBuilder
                .shapeless(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.MISC, output, 1)
                .requires(input)
                .group(group)
                .unlockedBy(getHasName(input), has(input))
                .save(exporter, path_prefix + getItemName(output));
    }

    protected void offerColorChangeRecipie(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif exporter,
            ItemLike output, Ingredient input, ItemLike dye, String group, String path_prefix
    ) {
        ShapelessRecipeBuilder
                .shapeless(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.MISC, output, 1)
                .requires(input)
                .requires(dye)
                .group(group)
                .unlockedBy(getHasName(dye), has(dye))
                .save(exporter, path_prefix + getItemName(output) + "_color_change");
    }

    protected void offerIlluminatedCabinetRecipe(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif exporter,
            ItemLike illuminated_cabinet, ItemLike cabinet
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
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif exporter,
            ItemLike output, ItemLike input1, ItemLike input2,
            @Nullable String group, int outputCount, String path_prefix
    ) {
        ShapelessRecipeBuilder
                .shapeless(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.MISC, output, outputCount)
                .requires(input1)
                .requires(input2)
                .group(group)
                .unlockedBy(getHasName(input1), has(input1))
                .unlockedBy(getHasName(input2), has(input2))
                .save(exporter, path_prefix + getItemName(output));
    }
    protected void offerTripleInputShapelessRecipe(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif exporter,
            ItemLike output, ItemLike input1, ItemLike input2, ItemLike input3,
            @Nullable String group, int outputCount, String path_prefix
    ) {
        ShapelessRecipeBuilder
                .shapeless(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.MISC, output, outputCount)
                .requires(input1)
                .requires(input2)
                .requires(input3)
                .group(group)
                .unlockedBy(getHasName(input1), has(input1))
                .unlockedBy(getHasName(input2), has(input2))
                .unlockedBy(getHasName(input3), has(input3))
                .save(exporter, path_prefix + getItemName(output));
    }

    protected void offerColouredTorchRecipe(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif exporter,
            ItemLike output, ItemLike glowingPowder, int glowingPowderAmount,
            @Nullable String group, String path_prefix
    ) {
        ShapelessRecipeBuilder
                .shapeless(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.MISC, output, 2)
                .requires(Items.STICK)
                .requires(Items.GLOW_INK_SAC)
                .requires(glowingPowder, glowingPowderAmount)
                .group(group)
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .unlockedBy(getHasName(Items.QUARTZ), has(Items.QUARTZ))
                .unlockedBy(getHasName(glowingPowder), has(glowingPowder))
                .save(exporter, path_prefix + getItemName(output));
    }

    protected void offerCandlestickRecipie(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif exporter,
            ItemLike output, ItemLike ingot, String path_prefix
    ) {
        ShapedRecipeBuilder
                .shaped(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.DECORATIONS, output)
                .pattern("I ")
                .pattern("II")
                .define('I', ingot)
                .group(MOD_ID + "/candlestick")
                .unlockedBy(getHasName(ingot), has(ingot))
                .save(exporter, path_prefix + "classic/" + getItemName(output));
        ShapedRecipeBuilder
                .shaped(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.DECORATIONS, output)
                .pattern(" I")
                .pattern("II")
                .define('I', ingot)
                .group(MOD_ID + "/candlestick")
                .unlockedBy(getHasName(ingot), has(ingot))
                .save(exporter, path_prefix + "reversed/" + getItemName(output));
    }

    protected void offerLightStripRecipie(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif exporter,
            ItemLike output, ItemLike glowingPowder, String path_prefix
    ) {
        ShapedRecipeBuilder
                .shaped(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.DECORATIONS, output, 2)
                .pattern("QQQ")
                .pattern("PPP")
                .pattern("GGG")
                .define('Q', Items.QUARTZ)
                .define('P', glowingPowder)
                .define('G', Blocks.GLASS_PANE)
                .group(MOD_ID + "/light_strip")
                .unlockedBy(getHasName(Items.QUARTZ), has(Items.QUARTZ))
                .unlockedBy(getHasName(glowingPowder), has(glowingPowder))
                .unlockedBy(getHasName(Blocks.GLASS_PANE), has(Blocks.GLASS_PANE))
                .save(exporter, path_prefix + "classic/" + getItemName(output));
        ShapedRecipeBuilder
                .shaped(#if MC_VERSION >= 12104 itemLookup, #endif RecipeCategory.DECORATIONS, output, 2)
                .pattern("GGG")
                .pattern("PPP")
                .pattern("QQQ")
                .define('Q', Items.QUARTZ)
                .define('P', glowingPowder)
                .define('G', Blocks.GLASS_PANE)
                .group(MOD_ID + "/light_strip")
                .unlockedBy(getHasName(Items.QUARTZ), has(Items.QUARTZ))
                .unlockedBy(getHasName(glowingPowder), has(glowingPowder))
                .unlockedBy(getHasName(Blocks.GLASS_PANE), has(Blocks.GLASS_PANE))
                .save(exporter, path_prefix + "reversed/" + getItemName(output));
    }

    // Override for the vanilla method to be mod safe
    public static String getHasName(ItemLike item) {
        final ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item.asItem());
        return "has_" + itemId.getNamespace() + ":" + itemId.getPath();
    }
}
