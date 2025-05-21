package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import io.github.mikip98.humilityafm.generators.CandlestickGenerator;
import io.github.mikip98.humilityafm.generators.ColouredLightsGenerator;
import io.github.mikip98.humilityafm.generators.ForcedCornerStairsGenerator;
import io.github.mikip98.humilityafm.generators.TerracottaTilesGenerator;
import io.github.mikip98.humilityafm.generators.WoodenMosaicGenerator;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.registries.ItemRegistry;
import io.github.mikip98.humilityafm.util.GenerationData;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
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
        // Optional
        generateCandlestickRecipies(exporter);
        generateLightStripRecipies(exporter);
    }

    protected static void generateCabinetRecipies(Consumer<RecipeJsonProvider> exporter) {
        int i = 0;
        for (String woodType : GenerationData.vanillaWoodTypes) {
            ItemConvertible[] currentWoodTypeCabinets = Arrays.copyOfRange(CabinetBlockGenerator.cabinetBlockVariants, i, i + GenerationData.vanillaColorPallet.length);
            ItemConvertible[] currentWoodTypeIlluminatedCabinets = Arrays.copyOfRange(CabinetBlockGenerator.illuminatedCabinetBlockVariants, i, i + GenerationData.vanillaColorPallet.length);

            for (String color : GenerationData.vanillaColorPallet) {
                ItemConvertible cabinetBlockItemVariant = CabinetBlockGenerator.cabinetBlockVariants[i];
                ItemConvertible illuminatedCabinetBlockItemVariant = CabinetBlockGenerator.illuminatedCabinetBlockVariants[i];

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

                List<ItemConvertible> currentWoodTypeCabinetsWithoutCurrentColor = new ArrayList<>(List.of(currentWoodTypeCabinets));
                currentWoodTypeCabinetsWithoutCurrentColor.remove(i % GenerationData.vanillaColorPallet.length);

                offerColorChangeRecipie(
                        exporter,
                        cabinetBlockItemVariant,
                        Ingredient.ofItems(currentWoodTypeCabinetsWithoutCurrentColor.toArray(new ItemConvertible[0])),
                        currentDye,
                        MOD_ID + "/cabinets",
                        "cabinets/color_change/"
                );

                List<ItemConvertible> currentWoodTypeIlluminatedCabinetsWithoutCurrentColor = new ArrayList<>(List.of(currentWoodTypeIlluminatedCabinets));
                currentWoodTypeIlluminatedCabinetsWithoutCurrentColor.remove(i % GenerationData.vanillaColorPallet.length);

                offerColorChangeRecipie(
                        exporter,
                        illuminatedCabinetBlockItemVariant,
                        Ingredient.ofItems(currentWoodTypeIlluminatedCabinetsWithoutCurrentColor.toArray(new ItemConvertible[0])),
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
                        WoodenMosaicGenerator.woodenMosaicVariants[i],
                        plank,
                        plank2,
                        "wooden_mosaics/"
                );
                int j = getMirrorIndex(i, GenerationData.vanillaWoodTypes.length);
                offerChangeRecipie(
                        exporter,
                        WoodenMosaicGenerator.woodenMosaicVariants[i],
                        WoodenMosaicGenerator.woodenMosaicVariants[j],
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
                        TerracottaTilesGenerator.terracottaTilesVariants[i],
                        terracotta,
                        terracotta2,
                        "terracotta_tiles/"
                );
                int j = getMirrorIndex(i, GenerationData.vanillaColorPallet.length);
                offerChangeRecipie(
                        exporter,
                        TerracottaTilesGenerator.terracottaTilesVariants[i],
                        TerracottaTilesGenerator.terracottaTilesVariants[j],
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
            ItemConvertible inner_stairs = ForcedCornerStairsGenerator.innerStairsBlockVariants[i];
            ItemConvertible outer_stairs = ForcedCornerStairsGenerator.outerStairsBlockVariants[i];
            offerChangeRecipie(exporter, inner_stairs, stairs, MOD_ID + "/stairs", "stairs/inner/");
            offerChangeRecipie(exporter, outer_stairs, inner_stairs, MOD_ID + "/stairs", "stairs/outer/");
            offerChangeRecipie(exporter, stairs, outer_stairs, MOD_ID + "/stairs", "stairs/normal/");
            ++i;
        }
    }

    protected static void generateJackOLanternRecipies(Consumer<RecipeJsonProvider> exporter) {
        Item carved_pumpkin = getItemFromName("carved_pumpkin");
        offerDoubleInputShapelessRecipe(
                exporter,
                BlockRegistry.JACK_O_LANTERN_REDSTONE,
                carved_pumpkin,
                getItemFromName("redstone_torch"),
                MOD_ID + "/jack_o_lanterns",
                1,
                "jack_o_lanterns/"
        );
        offerDoubleInputShapelessRecipe(
                exporter,
                BlockRegistry.JACK_O_LANTERN_SOUL,
                carved_pumpkin,
                getItemFromName("soul_torch"),
                MOD_ID + "/jack_o_lanterns",
                1,
                "jack_o_lanterns/"
        );

        for (int i = 0; i < GenerationData.vanillaColorPallet.length; ++i) {
            ItemConvertible weakJackOLantern = BlockRegistry.COLOURED_WEAK_JACK_O_LANTERNS[i];
            ItemConvertible jackOLantern = BlockRegistry.COLOURED_JACK_O_LANTERNS[i];
            ItemConvertible strongJackOLantern = BlockRegistry.COLOURED_STRONG_JACK_O_LANTERNS[i];

            ItemConvertible weakTorch = ColouredLightsGenerator.colouredTorchWeakVariants[i];
            ItemConvertible torch = ColouredLightsGenerator.colouredTorchVariants[i];
            ItemConvertible strongTorch = ColouredLightsGenerator.colouredTorchStrongVariants[i];

            offerDoubleInputShapelessRecipe(
                    exporter,
                    weakJackOLantern,
                    carved_pumpkin,
                    weakTorch,
                    MOD_ID + "/jack_o_lanterns",
                    1,
                    "jack_o_lanterns/weak/"
            );
            offerDoubleInputShapelessRecipe(
                    exporter,
                    jackOLantern,
                    carved_pumpkin,
                    torch,
                    MOD_ID + "/jack_o_lanterns",
                    1,
                    "jack_o_lanterns/normal/"
            );
            offerDoubleInputShapelessRecipe(
                    exporter,
                    strongJackOLantern,
                    carved_pumpkin,
                    strongTorch,
                    MOD_ID + "/jack_o_lanterns",
                    1,
                    "jack_o_lanterns/strong/"
            );
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
            ItemConvertible weakColouredTorch = ColouredLightsGenerator.colouredTorchWeakVariants[i];
            ItemConvertible colouredTorch = ColouredLightsGenerator.colouredTorchVariants[i];
            ItemConvertible strongColouredTorch = ColouredLightsGenerator.colouredTorchStrongVariants[i];

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

    protected static void generateCandlestickRecipies(Consumer<RecipeJsonProvider> exporter) {
        int i = 0;
        for (String metal : GenerationData.vanillaCandlestickMetals) {
            Item ingot = getItemFromName(metal + "_ingot");
            offerCandlestickRecipie(exporter, CandlestickGenerator.candlestickClassicVariants[i], ingot, "candlesticks/");
            ++i;
        }
        i = 0;
        for (String[] metals : GenerationData.vanillaRustableCandlestickMetals) {
            Item ingot = getItemFromName(metals[0] + "_ingot");
            offerCandlestickRecipie(exporter, CandlestickGenerator.candlestickRustableVariants.get(i)[0], ingot, "candlesticks/");
            ++i;
        }
    }

    protected static void generateLightStripRecipies(Consumer<RecipeJsonProvider> exporter) {
        for (int i = 0; i < GenerationData.vanillaColorPallet.length; ++i) {
            offerLightStripRecipie(
                    exporter,
                    ColouredLightsGenerator.LightStripBlockVariants[i],
                    ItemRegistry.glowingPowderVariants[i],
                    "light_strips/"
            );
        }
    }


    protected static Item getItemFromName(String name) {
        return Registries.ITEM.get(new Identifier(name));
    }
}
