package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.generators.ColouredFeatureSetGenerator;
import io.github.mikip98.humilityafm.generators.ForcedCornerStairsGenerator;
import io.github.mikip98.humilityafm.generators.TerracottaTilesGenerator;
import io.github.mikip98.humilityafm.generators.WoodenMosaicGenerator;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.registries.ItemRegistry;
import io.github.mikip98.humilityafm.util.GenerationData;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static io.github.mikip98.humilityafm.HumilityAFM.MOD_ID;

public class AFMRecipeGenerator extends FabricRecipeProvider {
    public AFMRecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new AFMRecipieProvider(registryLookup, exporter) {
            @Override
            public void generate() {
                RegistryWrapper.Impl<Item> itemLookup = registries.getOrThrow(RegistryKeys.ITEM);

                // ............ TEST BLOCKS & BLOCK ITEMS ............

                // Cabinet Blocks
                offerCabinetRecipe(
                        itemLookup, exporter,
                        ItemRegistry.CABINET_ITEM,
                        Items.PETRIFIED_OAK_SLAB,
                        Items.WHITE_CARPET,
                        "cabinets/"
                );

                // Illuminated Cabinet Blocks
                offerIlluminatedCabinetRecipe(
                        itemLookup, exporter,
                        ItemRegistry.ILLUMINATED_CABINET_ITEM,
                        ItemRegistry.CABINET_ITEM
                );

                // ............ FINAL BLOCKS & BLOCK ITEMS ............
                generateCabinetRecipies(itemLookup, exporter);
                generateWoodenMosaicRecipies(itemLookup, exporter);
                generateTerracottaTileRecipies(itemLookup, exporter);
                generateForcedCornerStairsRecipies(itemLookup, exporter);
                generateJackOLanternRecipies(itemLookup, exporter);
                generateGlowingPowderRecipies(itemLookup, exporter);
                generateColouredTorchRecipies(itemLookup, exporter);
                // Optional
                generateCandlestickRecipies(itemLookup, exporter);
                generateLightStripRecipies(itemLookup, exporter);
            }

            private void generateCabinetRecipies(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter) {
                int i = 0;
                for (String woodType : GenerationData.vanillaWoodTypes) {
                    ItemConvertible[] currentWoodTypeCabinets = Arrays.copyOfRange(ItemRegistry.CABINET_ITEM_VARIANTS, i, i + GenerationData.vanillaColorPallet.length);
                    ItemConvertible[] currentWoodTypeIlluminatedCabinets = Arrays.copyOfRange(ItemRegistry.ILLUMINATED_CABINET_ITEM_VARIANTS, i, i + GenerationData.vanillaColorPallet.length);

                    for (String color : GenerationData.vanillaColorPallet) {
                        ItemConvertible cabinetBlockItemVariant = ItemRegistry.CABINET_ITEM_VARIANTS[i];
                        ItemConvertible illuminatedCabinetBlockItemVariant = ItemRegistry.ILLUMINATED_CABINET_ITEM_VARIANTS[i];

                        offerCabinetRecipe(
                                itemLookup, exporter,
                                cabinetBlockItemVariant,
                                getItemFromName(woodType + "_slab"),
                                getItemFromName(color + "_carpet"),
                                "cabinets/"
                        );

                        offerIlluminatedCabinetRecipe(
                                itemLookup, exporter,
                                illuminatedCabinetBlockItemVariant,
                                cabinetBlockItemVariant
                        );

                        // Color change recipies
                        Item currentDye = getItemFromName(color + "_dye");

                        List<ItemConvertible> currentWoodTypeCabinetsWithoutCurrentColor = new ArrayList<>(List.of(currentWoodTypeCabinets));
                        currentWoodTypeCabinetsWithoutCurrentColor.remove(i % GenerationData.vanillaColorPallet.length);

                        offerColorChangeRecipie(
                                itemLookup, exporter,
                                cabinetBlockItemVariant,
                                Ingredient.ofItems(currentWoodTypeCabinetsWithoutCurrentColor.toArray(new ItemConvertible[0])),
                                currentDye,
                                MOD_ID + "/cabinets",
                                "cabinets/color_change/"
                        );

                        List<ItemConvertible> currentWoodTypeIlluminatedCabinetsWithoutCurrentColor = new ArrayList<>(List.of(currentWoodTypeIlluminatedCabinets));
                        currentWoodTypeIlluminatedCabinetsWithoutCurrentColor.remove(i % GenerationData.vanillaColorPallet.length);

                        offerColorChangeRecipie(
                                itemLookup, exporter,
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

            private void generateWoodenMosaicRecipies(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter) {
                int i = 0;
                for (String woodType : GenerationData.vanillaWoodTypes) {
                    Item plank = getItemFromName(woodType + "_planks");
                    for (String woodType2 : GenerationData.vanillaWoodTypes) {
                        if (woodType.equals(woodType2)) continue;

                        Item plank2 = getItemFromName(woodType2 + "_planks");

                        offerWoodenMosaicRecipe(
                                itemLookup, exporter,
                                WoodenMosaicGenerator.woodenMosaicVariants[i],
                                plank,
                                plank2,
                                "wooden_mosaics/"
                        );
                        int j = getMirrorIndex(i, GenerationData.vanillaWoodTypes.length);
                        offerChangeRecipie(
                                itemLookup, exporter,
                                WoodenMosaicGenerator.woodenMosaicVariants[i],
                                WoodenMosaicGenerator.woodenMosaicVariants[j],
                                MOD_ID + "/wooden_mosaics",
                                "wooden_mosaics/rotation/"
                        );

                        ++i;
                    }
                }
            }
            private void generateTerracottaTileRecipies(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter) {
                int i = 0;
                for (String color : GenerationData.vanillaColorPallet) {
                    Item terracotta = getItemFromName(color + "_terracotta");
                    for (String color2 : GenerationData.vanillaColorPallet) {
                        if (color.equals(color2)) continue;

                        Item terracotta2 = getItemFromName(color2 + "_terracotta");

                        offerTerracottaTileRecipe(
                                itemLookup, exporter,
                                TerracottaTilesGenerator.terracottaTilesVariants[i],
                                terracotta,
                                terracotta2,
                                "terracotta_tiles/"
                        );
                        int j = getMirrorIndex(i, GenerationData.vanillaColorPallet.length);
                        offerChangeRecipie(
                                itemLookup, exporter,
                                TerracottaTilesGenerator.terracottaTilesVariants[i],
                                TerracottaTilesGenerator.terracottaTilesVariants[j],
                                MOD_ID + "/terracotta_tiles",
                                "terracotta_tiles/rotation/"
                        );

                        ++i;
                    }
                }
            }
            private int getMirrorIndex(int index, int n) {
                int i = index / (n - 1);
                int j_offset = index % (n - 1);
                int j = (j_offset >= i) ? j_offset + 1 : j_offset;

                int mirrorOffset = (i < j) ? i : i - 1;

                // Return the mirrored index
                return j * (n - 1) + mirrorOffset;
            }

            private void generateForcedCornerStairsRecipies(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter) {
                int i = 0;
                for (String woodType : GenerationData.vanillaWoodTypes) {
                    Item stairs = getItemFromName(woodType + "_stairs");
                    ItemConvertible inner_stairs = ForcedCornerStairsGenerator.innerStairsBlockVariants[i];
                    ItemConvertible outer_stairs = ForcedCornerStairsGenerator.outerStairsBlockVariants[i];
                    offerChangeRecipie(itemLookup, exporter, inner_stairs, stairs, MOD_ID + "/stairs", "stairs/inner/");
                    offerChangeRecipie(itemLookup, exporter, outer_stairs, inner_stairs, MOD_ID + "/stairs", "stairs/outer/");
                    offerChangeRecipie(itemLookup, exporter, stairs, outer_stairs, MOD_ID + "/stairs", "stairs/normal/");
                    ++i;
                }
            }

            private void generateJackOLanternRecipies(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter) {
                Item carved_pumpkin = getItemFromName("carved_pumpkin");
                offerDoubleInputShapelessRecipe(
                        itemLookup, exporter,
                        BlockRegistry.JACK_O_LANTERN_REDSTONE,
                        carved_pumpkin,
                        getItemFromName("redstone_torch"),
                        MOD_ID + "/jack_o_lanterns",
                        1,
                        "jack_o_lanterns/"
                );
                offerDoubleInputShapelessRecipe(
                        itemLookup, exporter,
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

                    ItemConvertible weakTorch = ColouredFeatureSetGenerator.colouredTorchWeakVariants[i];
                    ItemConvertible torch = ColouredFeatureSetGenerator.colouredTorchVariants[i];
                    ItemConvertible strongTorch = ColouredFeatureSetGenerator.colouredTorchStrongVariants[i];

                    offerDoubleInputShapelessRecipe(
                            itemLookup, exporter,
                            weakJackOLantern,
                            carved_pumpkin,
                            weakTorch,
                            MOD_ID + "/jack_o_lanterns",
                            1,
                            "jack_o_lanterns/weak/"
                    );
                    offerDoubleInputShapelessRecipe(
                            itemLookup, exporter,
                            jackOLantern,
                            carved_pumpkin,
                            torch,
                            MOD_ID + "/jack_o_lanterns",
                            1,
                            "jack_o_lanterns/normal/"
                    );
                    offerDoubleInputShapelessRecipe(
                            itemLookup, exporter,
                            strongJackOLantern,
                            carved_pumpkin,
                            strongTorch,
                            MOD_ID + "/jack_o_lanterns",
                            1,
                            "jack_o_lanterns/strong/"
                    );
                }
            }

            private void generateGlowingPowderRecipies(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter) {
                Item glowstoneDust = Items.GLOWSTONE_DUST;
                Item redstone = Items.REDSTONE;
                int i = 0;
                for (String color : GenerationData.vanillaColorPallet) {
                    Item glowingPowder = ItemRegistry.GLOWING_POWDER_VARIANTS[i];
                    Item dye = getItemFromName(color + "_dye");
                    offerTripleInputShapelessRecipe(
                            itemLookup, exporter,
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

            private void generateColouredTorchRecipies(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter) {
                for (int i = 0; i < GenerationData.vanillaColorPallet.length; i++) {
                    ItemConvertible weakColouredTorch = ColouredFeatureSetGenerator.colouredTorchWeakVariants[i];
                    ItemConvertible colouredTorch = ColouredFeatureSetGenerator.colouredTorchVariants[i];
                    ItemConvertible strongColouredTorch = ColouredFeatureSetGenerator.colouredTorchStrongVariants[i];

                    Item glowingPowder = ItemRegistry.GLOWING_POWDER_VARIANTS[i];

                    // Weak Coloured Torch
                    offerColouredTorchRecipe(
                            itemLookup, exporter,
                            weakColouredTorch,
                            glowingPowder, 1,
                            MOD_ID + "/coloured_torches",
                            "coloured_torches/weak/"
                    );

                    // Coloured Torch
                    offerColouredTorchRecipe(
                            itemLookup, exporter,
                            colouredTorch,
                            glowingPowder, 2,
                            MOD_ID + "/coloured_torches",
                            "coloured_torches/normal/"
                    );
                    offerColouredTorchUpgradeRecipe(
                            itemLookup, exporter,
                            colouredTorch,
                            weakColouredTorch,
                            glowingPowder, 1,
                            MOD_ID + "/coloured_torches",
                            "coloured_torches/normal/upgrade/"
                    );

                    // Strong Coloured Torch
                    offerColouredTorchRecipe(
                            itemLookup, exporter,
                            strongColouredTorch,
                            glowingPowder, 3,
                            MOD_ID + "/coloured_torches",
                            "coloured_torches/strong/"
                    );
                    offerColouredTorchUpgradeRecipe(
                            itemLookup, exporter,
                            strongColouredTorch,
                            weakColouredTorch,
                            glowingPowder, 2,
                            MOD_ID + "/coloured_torches",
                            "coloured_torches/strong/upgrade_weak/"
                    );
                    offerColouredTorchUpgradeRecipe(
                            itemLookup, exporter,
                            strongColouredTorch,
                            colouredTorch,
                            glowingPowder, 1,
                            MOD_ID + "/coloured_torches",
                            "coloured_torches/strong/upgrade_normal/"
                    );
                }
            }

            private void generateCandlestickRecipies(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter) {
                int i = 0;
                for (String metal : GenerationData.vanillaCandlestickMetals) {
                    Item ingot = getItemFromName(metal + "_ingot");
                    offerCandlestickRecipie(itemLookup, exporter, ItemRegistry.CANDLESTICK_ITEM_VARIANTS[i], ingot, "candlesticks/");
                    ++i;
                }
                i = 0;
                for (String[] metals : GenerationData.vanillaRustableCandlestickMetals) {
                    Item ingot = getItemFromName(metals[0] + "_ingot");
                    offerCandlestickRecipie(itemLookup, exporter, ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS.get(i)[0], ingot, "candlesticks/");
                    ++i;
                }
            }

            private void generateLightStripRecipies(RegistryWrapper.Impl<Item> itemLookup, RecipeExporter exporter) {
                for (int i = 0; i < GenerationData.vanillaColorPallet.length; ++i) {
                    offerLightStripRecipie(
                            itemLookup, exporter,
                            ColouredFeatureSetGenerator.LightStripBlockVariants[i],
                            ItemRegistry.GLOWING_POWDER_VARIANTS[i],
                            "light_strips/"
                    );
                }
            }
        };
    }

    protected static Item getItemFromName(String name) {
        return Registries.ITEM.get(Identifier.of(name));
    }

    @Override
    public String getName() {
        return "AFMRecipeGenerator";
    }
}
