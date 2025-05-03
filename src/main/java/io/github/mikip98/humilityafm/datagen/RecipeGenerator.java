package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import io.github.mikip98.humilityafm.helpers.MainHelper;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
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
        // Cabinet Blocks

        // Illuminated Cabinet Blocks
        offerIlluminatedCabinetRecipe(
                exporter,
                BlockRegistry.ILLUMINATED_CABINET_BLOCK,
                BlockRegistry.CABINET_BLOCK
        );
        final Map<String, Item> color2DyeMap = new HashMap<>();
        color2DyeMap.put("white", Items.WHITE_DYE);
        color2DyeMap.put("orange", Items.ORANGE_DYE);
        color2DyeMap.put("magenta", Items.MAGENTA_DYE);
        color2DyeMap.put("light_blue", Items.LIGHT_BLUE_DYE);
        color2DyeMap.put("yellow", Items.YELLOW_DYE);
        color2DyeMap.put("lime", Items.LIME_DYE);
        color2DyeMap.put("pink", Items.PINK_DYE);
        color2DyeMap.put("gray", Items.GRAY_DYE);
        color2DyeMap.put("light_gray", Items.LIGHT_GRAY_DYE);
        color2DyeMap.put("cyan", Items.CYAN_DYE);
        color2DyeMap.put("purple", Items.PURPLE_DYE);
        color2DyeMap.put("blue", Items.BLUE_DYE);
        color2DyeMap.put("brown", Items.BROWN_DYE);
        color2DyeMap.put("green", Items.GREEN_DYE);
        color2DyeMap.put("red", Items.RED_DYE);
        color2DyeMap.put("black", Items.BLACK_DYE);
//        for (int i = 0; i < CabinetBlockHelper.cabinetBlockVariantsNames.length; i++) {
//            IlluminatedCabinetBlock illuminatedCabinetBlock = (IlluminatedCabinetBlock) CabinetBlockHelper.illuminatedCabinetBlockVariants[i];
//            CabinetBlock cabinetBlock = (CabinetBlock) CabinetBlockHelper.cabinetBlockVariants[i];
//            String[] parts = Parts.getParts(illuminatedCabinetBlock);
//
//            offerIlluminatedCabinetRecipe(
//                    exporter,
//                    illuminatedCabinetBlock,
//                    cabinetBlock
//            );
//
//            offerColorChangeRecipie(
//                    exporter,
//                    cabinetBlock,
//                    ModItemTags.,
//                    color2DyeMap.get(C),
//                    MOD_ID + "/cabinets"
//            );
//
//            offerColorChangeRecipie(
//                    exporter,
//                    illuminatedCabinetBlock,
//                    ModItemTags.ILLUMINATED_CABINET_BLOCKS,
//                    Items.GLOW_INK_SAC,
//                    MOD_ID + "/illuminated_cabinets"
//            );
//        }

        short startIndex = CabinetBlockGenerator.woodTypeToStartIndex.get("oak");
        short i = 0;
        List<Item> oakCabinets = List.of(Arrays.copyOfRange(CabinetBlockGenerator.cabinetBlockItemVariants, startIndex, startIndex + MainHelper.vanillaWoolTypes.length));
        for (String color : MainHelper.vanillaWoolTypes) {
            Item cabinetBlockItemVariant = CabinetBlockGenerator.cabinetBlockItemVariants[i];
            Item illuminatedCabinetBlockItemVariant = CabinetBlockGenerator.illuminatedCabinetBlockItemVariants[i];


            List<Item> oakCabinets2 = new ArrayList<>(oakCabinets);
            oakCabinets2.remove(i);
            Ingredient otherOakCabinets = Ingredient.ofItems(oakCabinets2.toArray(new Item[0]));
            offerColorChangeRecipie(
                    exporter,
                    cabinetBlockItemVariant,
                    otherOakCabinets,
                    color2DyeMap.get(color),
                    MOD_ID + "/cabinets"
            );

            offerColorChangeRecipie(
                    exporter,
                    illuminatedCabinetBlockItemVariant,
                    otherOakCabinets,
                    color2DyeMap.get(color),
                    MOD_ID + "/illuminated_cabinets"
            );

            ++i;
        }
    }


    protected static void offerColorChangeRecipie(Consumer<RecipeJsonProvider> exporter, ItemConvertible output, Ingredient input, ItemConvertible dye, String group) {
        ShapelessRecipeJsonBuilder
                .create(RecipeCategory.MISC, output, 1)
                .input(input)
                .input(dye)
                .group(group)
                .criterion(hasItem(dye), conditionsFromItem(dye))
                .offerTo(exporter, getRecipeName(output) + "_color_change");
    }

    protected static void offerIlluminatedCabinetRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible illuminated_cabinet, ItemConvertible cabinet) {
        offerDoubleInputShapelessRecipe(
                exporter,
                illuminated_cabinet,
                cabinet,
                Items.GLOW_INK_SAC,
                MOD_ID + "/illuminated_cabinets",
                1
        );
    }

    protected static void offerDoubleInputShapelessRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible output, ItemConvertible input1, ItemConvertible input2, @Nullable String group, int outputCount) {
        LOGGER.info("Generating recipe with id: {}", getRecipeName(output));
        ShapelessRecipeJsonBuilder
                .create(RecipeCategory.MISC, output, outputCount)
                .input(input1)
                .input(input2)
                .group(group)
                .criterion(hasItem(input1), conditionsFromItem(input1))
                .criterion(hasItem(input2), conditionsFromItem(input2))
                .offerTo(exporter, getRecipeName(output));
    }
}
