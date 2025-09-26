package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.registries.ItemRegistry;
import io.github.mikip98.humilityafm.util.generation_data.ActiveGenerationData;
import io.github.mikip98.humilityafm.util.generation_data.RawGenerationData;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import io.github.mikip98.humilityafm.util.Pair;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.BlockMaterial;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.MaterialType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.*;

import static io.github.mikip98.humilityafm.HumilityAFM.MOD_ID;

public class RecipeGenerator extends AFMRecipieProvider {
    public RecipeGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        // ............ TEST BLOCKS & BLOCK ITEMS ............

        // Cabinet Blocks
        offerCabinetRecipe(
                exporter,
                ItemRegistry.CABINET_ITEM,
                Items.PETRIFIED_OAK_SLAB,
                Items.WHITE_CARPET,
                "cabinets/"
        );

        // Illuminated Cabinet Blocks
        offerIlluminatedCabinetRecipe(
                exporter,
                ItemRegistry.ILLUMINATED_CABINET_ITEM,
                ItemRegistry.CABINET_ITEM
        );

        // ............ FINAL BLOCKS & BLOCK ITEMS ............
        generateCabinetRecipies(exporter);
        generateWoodenMosaicRecipies(exporter);
        generateTerracottaTileRecipies(exporter);
        generateForcedCornerStairsRecipies(exporter);
        generateSpecialJackOLanternRecipies(exporter);
        // Optional
        generateCandlestickRecipies(exporter);
        generateGlowingPowderRecipies(exporter);
        generateLightStripRecipies(exporter);
        generateColouredTorchRecipies(exporter);
        generateColouredJackOLanternRecipies(exporter);
    }

    protected static void generateCabinetRecipies(RecipeExporter exporter) {
        Map<Pair<SupportedMods, String>, List<Pair<ItemConvertible, String>>> perWoodCabinets = new HashMap<>();
        Map<Pair<SupportedMods, String>, List<Pair<ItemConvertible, String>>> perWoodIlluminatedCabinets = new HashMap<>();
        int i = 0;
        for (BlockMaterial material : ActiveGenerationData.cabinetVariantMaterials) {
            Pair<SupportedMods, String> key = new Pair<>(material.layers()[0].metadata().sourceMod(), material.layers()[0].name());
            String colourName = material.layers()[1].name();
            perWoodCabinets
                    .computeIfAbsent(key, k -> new ArrayList<>())
                    .add(Pair.of(ItemRegistry.CABINET_ITEM_VARIANTS[i], colourName));
            perWoodIlluminatedCabinets
                    .computeIfAbsent(key, k -> new ArrayList<>())
                    .add(Pair.of(ItemRegistry.ILLUMINATED_CABINET_ITEM_VARIANTS[i], colourName));
            ++i;
        }

        i = 0;
        for (BlockMaterial material : ActiveGenerationData.cabinetVariantMaterials) {
            if (material.layers().length != 2) throw new IllegalStateException("Cabinet material must have exactly two layers, but found: " + Arrays.toString(material.layers()));

            final BlockMaterial.Layer woodLayer = material.layers()[0];
            final String colourName = material.layers()[1].name();
            final SupportedMods woodSourceMod = woodLayer.metadata().sourceMod();

            final Pair<SupportedMods, String> key = new Pair<>(woodSourceMod, woodLayer.name());
            final ItemConvertible[] sameWoodOtherColourCabinets = perWoodCabinets
                    .get(key)
                    .stream()
                    .filter(entry -> !entry.second().equals(colourName))
                    .map(Pair::first).toArray(ItemConvertible[]::new);
            final ItemConvertible[] sameWoodOtherColourIlluminatedCabinets = perWoodIlluminatedCabinets
                    .get(key)
                    .stream()
                    .filter(entry -> !entry.second().equals(colourName))
                    .map(Pair::first).toArray(ItemConvertible[]::new);

            final ItemConvertible cabinetBlockItemVariant = ItemRegistry.CABINET_ITEM_VARIANTS[i];
            final ItemConvertible illuminatedCabinetBlockItemVariant = ItemRegistry.ILLUMINATED_CABINET_ITEM_VARIANTS[i];

            // Normal recipies
            offerCabinetRecipe(
                    exporter,
                    cabinetBlockItemVariant,
                    getItemFromName(woodLayer.name() + "_slab", woodSourceMod),
                    getItemFromName(colourName + "_carpet"),
                    "cabinets/"
            );
            offerIlluminatedCabinetRecipe(
                    exporter,
                    illuminatedCabinetBlockItemVariant,
                    cabinetBlockItemVariant
            );

            // Colour change recipies
            final Item currentDye = getItemFromName(colourName + "_dye");

            offerColorChangeRecipie(
                    exporter,
                    cabinetBlockItemVariant,
                    Ingredient.ofItems(sameWoodOtherColourCabinets),
                    currentDye,
                    MOD_ID + "/cabinets",
                    "cabinets/color_change/"
            );
            offerColorChangeRecipie(
                    exporter,
                    illuminatedCabinetBlockItemVariant,
                    Ingredient.ofItems(sameWoodOtherColourIlluminatedCabinets),
                    currentDye,
                    MOD_ID + "/illuminated_cabinets",
                    "illuminated_cabinets/color_change/"
            );
            ++i;
        }
    }

    protected static void generateWoodenMosaicRecipies(RecipeExporter exporter) {
        Iterable<BlockMaterial> woodenMosaicMaterials = ActiveGenerationData.woodenMosaicVariantMaterials;
        int i = 0;
        for (BlockMaterial material : woodenMosaicMaterials) {
            BlockMaterial.Layer woodMaterial1 = material.layers()[0];
            BlockMaterial.Layer woodMaterial2 = material.layers()[1];

            SupportedMods sourceMod1 = woodMaterial1.metadata().sourceMod();
            SupportedMods sourceMod2 = woodMaterial2.metadata().sourceMod();

            Item plank1 = getItemFromName(woodMaterial1.name() + "_planks", sourceMod1);
            Item plank2 = getItemFromName(woodMaterial2.name() + "_planks", sourceMod2);

            offerWoodenMosaicRecipe(
                    exporter,
                    BlockRegistry.WOODEN_MOSAIC_VARIANTS[i],
                    plank1,
                    plank2,
                    "wooden_mosaics/"
            );
            // TODO: Double check this still works
            final int j = getMirrorIndex(i, RawGenerationData.allWoodTypes.size());
            offerChangeRecipie(
                    exporter,
                    BlockRegistry.WOODEN_MOSAIC_VARIANTS[i],
                    BlockRegistry.WOODEN_MOSAIC_VARIANTS[j],
                    MOD_ID + "/wooden_mosaics",
                    "wooden_mosaics/rotation/"
            );

            // Datapack alternative recipies
            // The output of this should be moved from 'generated/data/humility-afm/recipies'
            // to its own dedicated datapack folder
            offerAlternateWoodenMosaicRecipe(
                    exporter,
                    BlockRegistry.WOODEN_MOSAIC_VARIANTS[i],
                    plank1,
                    plank2,
                    "datapack/wooden_mosaics/alternative/"
            );

            ++i;
        }
    }
    protected static void generateTerracottaTileRecipies(RecipeExporter exporter) {
        int i = 0;
        for (BlockMaterial material : ActiveGenerationData.terracottaTilesMaterials) {
            Item terracotta1 = getItemFromName(material.layers()[0].name() + "_terracotta");
            Item terracotta2 = getItemFromName(material.layers()[1].name() + "_terracotta");

            offerTerracottaTileRecipe(
                    exporter,
                    BlockRegistry.TERRACOTTA_TILE_VARIANTS[i],
                    terracotta1,
                    terracotta2,
                    "terracotta_tiles/"
            );
            final int j = getMirrorIndex(i, RawGenerationData.vanillaColorPallet.length);
            offerChangeRecipie(
                    exporter,
                    BlockRegistry.TERRACOTTA_TILE_VARIANTS[i],
                    BlockRegistry.TERRACOTTA_TILE_VARIANTS[j],
                    MOD_ID + "/terracotta_tiles",
                    "terracotta_tiles/rotation/"
            );
            ++i;
        }
    }
    protected static int getMirrorIndex(int index, int n) {
        int i = index / (n - 1);
        int j_offset = index % (n - 1);
        int j = (j_offset >= i) ? j_offset + 1 : j_offset;

        int mirrorOffset = (i < j) ? i : i - 1;

        // Return the mirrored index
        int result = j * (n - 1) + mirrorOffset;
        if (result >= n * (n - 1)) {
            throw new IllegalStateException("Calculated mirror index out of bounds: " + result + " for index: " + index + " and n: " + n);
        }
        return result;
    }

    protected static void generateForcedCornerStairsRecipies(RecipeExporter exporter) {
        int i = 0;
        for (BlockMaterial stairMaterial : ActiveGenerationData.forcedCornerStairsVariantMaterials) {
            if (stairMaterial.layers().length != 1) throw new IllegalArgumentException("Forced corner stairs material must have exactly one layer, but found: " + Arrays.toString(stairMaterial.layers()));
            final BlockMaterial.Layer materialLayer = stairMaterial.layers()[0];
            final MaterialType materialType = materialLayer.metadata().type();

            String materialName = materialLayer.name();
            if (materialType == MaterialType.STONY) {
                // If the material ends in 'bricks', cut the 's'
                if (materialName.endsWith("bricks")) {
                    materialName = materialName.substring(0, materialName.length() - 1);
                }
            }

            final SupportedMods sourceMod = materialLayer.metadata().sourceMod();
            final Item stairs = getItemFromName(materialName + "_stairs", sourceMod);
            final String modId = sourceMod != null ? sourceMod.modId : "vanilla";

            final ItemConvertible inner_stairs = BlockRegistry.INNER_STAIRS_BLOCK_VARIANTS[i];
            final ItemConvertible outer_stairs = BlockRegistry.OUTER_STAIRS_BLOCK_VARIANTS[i];

            offerChangeRecipie(exporter, inner_stairs, stairs, MOD_ID + "/stairs", "stairs/" + modId + "/inner/");
            offerChangeRecipie(exporter, outer_stairs, inner_stairs, MOD_ID + "/stairs", "stairs/" + modId + "/outer/");
            offerChangeRecipie(exporter, stairs, outer_stairs, MOD_ID + "/stairs", "stairs/" + modId + "/normal/");
            ++i;
        }
    }

    protected static void generateSpecialJackOLanternRecipies(RecipeExporter exporter) {
        Item carved_pumpkin = Items.CARVED_PUMPKIN;
        offerDoubleInputShapelessRecipe(
                exporter,
                BlockRegistry.JACK_O_LANTERN_REDSTONE,
                carved_pumpkin,
                Items.REDSTONE_TORCH,
                MOD_ID + "/jack_o_lanterns",
                1,
                "jack_o_lanterns/"
        );
        offerDoubleInputShapelessRecipe(
                exporter,
                BlockRegistry.JACK_O_LANTERN_SOUL,
                carved_pumpkin,
                Items.SOUL_TORCH,
                MOD_ID + "/jack_o_lanterns",
                1,
                "jack_o_lanterns/"
        );
    }
    protected static void generateColouredJackOLanternRecipies(RecipeExporter exporter) {
        Item carved_pumpkin = Items.CARVED_PUMPKIN;

        for (int i = 0; i < RawGenerationData.vanillaColorPallet.length; ++i) {
            ItemConvertible jackOLantern = BlockRegistry.COLOURED_JACK_O_LANTERNS[i];
            ItemConvertible torch = BlockRegistry.COLOURED_TORCH_VARIANTS[i];

            offerDoubleInputShapelessRecipe(
                    exporter,
                    jackOLantern,
                    carved_pumpkin,
                    torch,
                    MOD_ID + "/jack_o_lanterns",
                    1,
                    "jack_o_lanterns/normal/"
            );
        }
    }

    protected static void generateGlowingPowderRecipies(RecipeExporter exporter) {
        Item glowstoneDust = Items.GLOWSTONE_DUST;
        Item redstone = Items.REDSTONE;
        int i = 0;
        for (String color : RawGenerationData.vanillaColorPallet) {
            Item glowingPowder = ItemRegistry.GLOWING_POWDER_VARIANTS[i];
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

    protected static void generateColouredTorchRecipies(RecipeExporter exporter) {
        final int length = ActiveGenerationData.colouredFeatureSetMaterials.size();
        for (int i = 0; i < length; i++) {
            ItemConvertible colouredTorch = BlockRegistry.COLOURED_TORCH_VARIANTS[i];
            Item glowingPowder = ItemRegistry.GLOWING_POWDER_VARIANTS[i];
            offerColouredTorchRecipe(
                    exporter,
                    colouredTorch,
                    glowingPowder, 3,
                    MOD_ID + "/coloured_torches",
                    "coloured_torches/strong/"
            );
        }
    }

    protected static void generateCandlestickRecipies(RecipeExporter exporter) {
        int i = 0;
        for (BlockMaterial material : ActiveGenerationData.simpleCandlestickMaterials) {
            final String metal = material.layers()[0].name();
            final Item ingot = getItemFromName(metal + "_ingot");
            offerCandlestickRecipie(exporter, ItemRegistry.CANDLESTICK_ITEM_VARIANTS[i], ingot, "candlesticks/");
            ++i;
        }
        i = 0;
        for (Iterable<BlockMaterial> meterials : ActiveGenerationData.rustingCandlestickMaterials) {
            final String metal = meterials.iterator().next().layers()[0].name();
            final Item ingot = getItemFromName(metal + "_ingot");
            offerCandlestickRecipie(exporter, ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS[i][0], ingot, "candlesticks/");
            ++i;
        }
    }

    protected static void generateLightStripRecipies(RecipeExporter exporter) {
        for (int i = 0; i < RawGenerationData.vanillaColorPallet.length; ++i) {
            offerLightStripRecipie(
                    exporter,
                    BlockRegistry.LIGHT_STRIP_VARIANTS[i],
                    ItemRegistry.GLOWING_POWDER_VARIANTS[i],
                    "light_strips/"
            );
        }
    }


    protected static Item getItemFromName(String name) {
        return Registries.ITEM.get(new Identifier(name));
    }
    protected static Item getItemFromName(String name, SupportedMods mod) {
        Item item;
        if (mod == null) {
            item = Registries.ITEM.get(new Identifier(name));
        } else {
            item = Registries.ITEM.get(new Identifier(mod.modId, name));
        }
        if (item == Items.AIR) {
            throw new IllegalStateException("Item not found: " + name + " in mod: " + (mod != null ? mod.modId : "vanilla"));
        }
        return item;
    }
}
