package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import io.github.mikip98.humilityafm.generators.ColouredLightsGenerator;
import io.github.mikip98.humilityafm.generators.ForcedCornerStairsGenerator;
import io.github.mikip98.humilityafm.helpers.PumpkinHelper;
import io.github.mikip98.humilityafm.helpers.TerracottaTilesHelper;
import io.github.mikip98.humilityafm.helpers.WoodenMosaicHelper;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.registries.ItemRegistry;
import io.github.mikip98.humilityafm.util.GenerationData;
import io.github.mikip98.humilityafm.util.data_types.Torch;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.function.Consumer;

import static io.github.mikip98.humilityafm.HumilityAFM.MOD_ID;

public class RecipeGenerator extends AFMRecipieProvider {
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
        generateGlowingPowderRecipies(exporter);
        generateColouredTorchRecipies(exporter);
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

        // Return the mirrored index
        return j * (n - 1) + mirrorOffset;
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

    protected static void generateGlowingPowderRecipies(Consumer<RecipeJsonProvider> exporter) {
        Item glowstoneDust = Items.GLOWSTONE_DUST;
        Item redstone = Items.REDSTONE;
        int i = 0;
        for (String color : GenerationData.vanillaColorPallet) {
            Item glowingPowder = ItemRegistry.glowingPowderVariants[i];
            Item dye = getItemFromName(color + "_dye");
            offerTripleInputShapelessRecipe(
                    exporter,
                    glowingPowder,
                    glowstoneDust,
                    redstone,
                    dye,
                    MOD_ID + "/glowing_powder",
                    1,
                    "glowing_powder/"
            );
            ++i;
        }
    }

    protected static void generateColouredTorchRecipies(Consumer<RecipeJsonProvider> exporter) {
        for (int i = 0; i < GenerationData.vanillaColorPallet.length; i++) {
            Item weakColouredTorch = ColouredLightsGenerator.colouredTorchesItemVariants[i * 3];
            Item colouredTorch = ColouredLightsGenerator.colouredTorchesItemVariants[i * 3 + 1];
            Item strongColouredTorch = ColouredLightsGenerator.colouredTorchesItemVariants[i * 3 + 2];

            Item glowingPowder = ItemRegistry.glowingPowderVariants[i];

            // Weak Coloured Torch
            offerColouredTorchRecipe(
                    exporter,
                    weakColouredTorch,
                    glowingPowder, 1,
                    MOD_ID + "/coloured_torches",
                    "coloured_torches/weak/"
            );

            // Coloured Torch
            offerColouredTorchRecipe(
                    exporter,
                    colouredTorch,
                    glowingPowder, 2,
                    MOD_ID + "/coloured_torches",
                    "coloured_torches/normal/"
            );
            offerColouredTorchUpgradeRecipe(
                    exporter,
                    colouredTorch,
                    weakColouredTorch,
                    glowingPowder, 1,
                    MOD_ID + "/coloured_torches",
                    "coloured_torches/normal/upgrade/"
            );

            // Strong Coloured Torch
            offerColouredTorchRecipe(
                    exporter,
                    strongColouredTorch,
                    glowingPowder, 3,
                    MOD_ID + "/coloured_torches",
                    "coloured_torches/strong/"
            );
            offerColouredTorchUpgradeRecipe(
                    exporter,
                    strongColouredTorch,
                    weakColouredTorch,
                    glowingPowder, 2,
                    MOD_ID + "/coloured_torches",
                    "coloured_torches/strong/upgrade_weak/"
            );
            offerColouredTorchUpgradeRecipe(
                    exporter,
                    strongColouredTorch,
                    colouredTorch,
                    glowingPowder, 1,
                    MOD_ID + "/coloured_torches",
                    "coloured_torches/strong/upgrade_normal/"
            );
        }
    }


    protected static Item getItemFromName(String name) {
        return Registries.ITEM.get(new Identifier(name));
    }
}
