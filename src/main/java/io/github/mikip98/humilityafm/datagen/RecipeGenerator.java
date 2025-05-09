package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import io.github.mikip98.humilityafm.generators.ForcedCornerStairsGenerator;
import io.github.mikip98.humilityafm.helpers.PumpkinHelper;
import io.github.mikip98.humilityafm.helpers.TerracottaTilesHelper;
import io.github.mikip98.humilityafm.helpers.WoodenMosaicHelper;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.util.GenerationData;
import io.github.mikip98.humilityafm.util.data_types.Torch;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

import static io.github.mikip98.humilityafm.HumilityAFM.MOD_ID;

public class RecipeGenerator extends FabricRecipeProvider {
    public RecipeGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        // ............ TEST BLOCKS & BLOCK ITEMS ............

        // Cabinet Blocks
        offerCabinetRecipe(
                exporter,
                BlockRegistry.CABINET_BLOCK,
                Items.PETRIFIED_OAK_SLAB,
                Items.WHITE_CARPET,
                "cabinets/"
        );

        // Illuminated Cabinet Blocks
        offerIlluminatedCabinetRecipe(
                exporter,
                BlockRegistry.ILLUMINATED_CABINET_BLOCK,
                BlockRegistry.CABINET_BLOCK
        );

        // ............ FINAL BLOCKS & BLOCK ITEMS ............
        generateCabinetRecipies(exporter);
        generateWoodenMosaicRecipies(exporter);
        generateTerracottaTileRecipies(exporter);
        generateForcedCornerStairsRecipies(exporter);
        generateJackOLanternRecipies(exporter);
    }

    protected static void generateCabinetRecipies(Consumer<RecipeJsonProvider> exporter) {
        int i = 0;
        for (String woodType : GenerationData.vanillaWoodTypes) {
            Item[] currentWoodTypeCabinets = Arrays.copyOfRange(CabinetBlockGenerator.cabinetBlockItemVariants, i, i + GenerationData.vanillaColorPallet.length);
            Item[] currentWoodTypeIlluminatedCabinets = Arrays.copyOfRange(CabinetBlockGenerator.illuminatedCabinetBlockItemVariants, i, i + GenerationData.vanillaColorPallet.length);

            for (String color : GenerationData.vanillaColorPallet) {
                Item cabinetBlockItemVariant = CabinetBlockGenerator.cabinetBlockItemVariants[i];
                Item illuminatedCabinetBlockItemVariant = CabinetBlockGenerator.illuminatedCabinetBlockItemVariants[i];

                offerCabinetRecipe(
                        exporter,
                        cabinetBlockItemVariant,
                        getItemFromName(woodType + "_slab"),
                        getItemFromName(color + "_carpet"),
                        "cabinets/"
                );

                offerIlluminatedCabinetRecipe(
                        exporter,
                        illuminatedCabinetBlockItemVariant,
                        cabinetBlockItemVariant
                );

                // Color change recipies
                Item currentDye = getItemFromName(color + "_dye");

                List<Item> currentWoodTypeCabinetsWithoutCurrentColor = new ArrayList<>(List.of(currentWoodTypeCabinets));
                currentWoodTypeCabinetsWithoutCurrentColor.remove(i % GenerationData.vanillaColorPallet.length);

                offerColorChangeRecipie(
                        exporter,
                        cabinetBlockItemVariant,
                        Ingredient.ofItems(currentWoodTypeCabinetsWithoutCurrentColor.toArray(new Item[0])),
                        currentDye,
                        MOD_ID + "/cabinets",
                        "cabinets/color_change/"
                );

                List<Item> currentWoodTypeIlluminatedCabinetsWithoutCurrentColor = new ArrayList<>(List.of(currentWoodTypeIlluminatedCabinets));
                currentWoodTypeIlluminatedCabinetsWithoutCurrentColor.remove(i % GenerationData.vanillaColorPallet.length);

                offerColorChangeRecipie(
                        exporter,
                        illuminatedCabinetBlockItemVariant,
                        Ingredient.ofItems(currentWoodTypeIlluminatedCabinetsWithoutCurrentColor.toArray(new Item[0])),
                        currentDye,
                        MOD_ID + "/illuminated_cabinets",
                        "illuminated_cabinets/color_change/"
                );

                ++i;
            }
        }
    }

    protected static void generateWoodenMosaicRecipies(Consumer<RecipeJsonProvider> exporter) {
        int i = 0;
        for (String woodType : GenerationData.vanillaWoodTypes) {
            Item plank = getItemFromName(woodType + "_planks");
            for (String woodType2 : GenerationData.vanillaWoodTypes) {
                if (woodType.equals(woodType2)) continue;

                Item plank2 = getItemFromName(woodType2 + "_planks");

                offerWoodenMosaicRecipe(
                        exporter,
                        WoodenMosaicHelper.woodenMosaicItemVariants[i],
                        plank,
                        plank2,
                        "wooden_mosaics/"
                );
                int j = getMirrorIndex(i, GenerationData.vanillaWoodTypes.length);
                offerChangeRecipie(
                        exporter,
                        WoodenMosaicHelper.woodenMosaicItemVariants[i],
                        WoodenMosaicHelper.woodenMosaicItemVariants[j],
                        MOD_ID + "/wooden_mosaics",
                        "wooden_mosaics/rotation/"
                );

                ++i;
            }
        }
    }
    protected static void generateTerracottaTileRecipies(Consumer<RecipeJsonProvider> exporter) {
        int i = 0;
        for (String color : GenerationData.vanillaColorPallet) {
            Item terracotta = getItemFromName(color + "_terracotta");
            for (String color2 : GenerationData.vanillaColorPallet) {
                if (color.equals(color2)) continue;

                Item terracotta2 = getItemFromName(color2 + "_terracotta");

                offerTerracottaTileRecipe(
                        exporter,
                        TerracottaTilesHelper.terracottaTilesItemVariants[i],
                        terracotta,
                        terracotta2,
                        "terracotta_tiles/"
                );
                int j = getMirrorIndex(i, GenerationData.vanillaColorPallet.length);
                offerChangeRecipie(
                        exporter,
                        TerracottaTilesHelper.terracottaTilesItemVariants[i],
                        TerracottaTilesHelper.terracottaTilesItemVariants[j],
                        MOD_ID + "/terracotta_tiles",
                        "terracotta_tiles/rotation/"
                );

                ++i;
            }
        }
    }
    protected static int getMirrorIndex(int index, int n) {
        int i = index / (n - 1);
        int j_offset = index % (n - 1);
        int j = (j_offset >= i) ? j_offset + 1 : j_offset;

        int mirrorOffset = (i < j) ? i : i - 1;
        int mirrorIndex = j * (n - 1) + mirrorOffset;

        return mirrorIndex;
    }

    protected static void generateForcedCornerStairsRecipies(Consumer<RecipeJsonProvider> exporter) {
        int i = 0;
        for (String woodType : GenerationData.vanillaWoodTypes) {
            Item stairs = getItemFromName(woodType + "_stairs");
            Item inner_stairs = ForcedCornerStairsGenerator.innerStairsBlockItemVariants[i];
            Item outer_stairs = ForcedCornerStairsGenerator.outerStairsBlockItemVariants[i];
            offerChangeRecipie(exporter, inner_stairs, stairs, MOD_ID + "/stairs", "stairs/inner/");
            offerChangeRecipie(exporter, outer_stairs, inner_stairs, MOD_ID + "/stairs", "stairs/outer/");
            ++i;
        }
    }

    protected static void generateJackOLanternRecipies(Consumer<RecipeJsonProvider> exporter) {
        int i = 0;
        Item carved_pumpkin = getItemFromName("carved_pumpkin");
        for (Torch torch : PumpkinHelper.torches) {
            Item jack_o_lantern = PumpkinHelper.PumpkinsItemVariants[i];
            offerDoubleInputShapelessRecipe(
                    exporter,
                    jack_o_lantern,
                    carved_pumpkin,
                    getItemFromName(torch.type() + "_torch"),
                    MOD_ID + "/jack_o_lanterns",
                    1,
                    "jack_o_lanterns/"
            );
            ++i;
        }
    }


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


    protected static Item getItemFromName(String name) {
        return Registries.ITEM.get(new Identifier(name));
    }
}
