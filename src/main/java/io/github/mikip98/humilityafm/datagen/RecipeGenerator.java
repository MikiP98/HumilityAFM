package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.util.GenerationData;
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
    }

    protected static void generateCabinetRecipies(Consumer<RecipeJsonProvider> exporter) {
        short i = 0;
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
        LOGGER.info("Generating recipe with id: {}", getRecipeName(output));
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
