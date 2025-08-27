package io.github.mikip98.humilityafm.util.generation_data;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.util.Pair;
import io.github.mikip98.humilityafm.util.generation_data.material_management.SizedIterable;
import io.github.mikip98.humilityafm.util.mod_support.ModSupportManager;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import io.github.mikip98.humilityafm.util.generation_data.material_management.LayeredMaterialArrayCombiner;
import io.github.mikip98.humilityafm.util.generation_data.material_management.MaterialArrayCombiner;
import io.github.mikip98.humilityafm.util.generation_data.material_management.MaterialMultiArrayIterable;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.BlockMaterial;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.BlockStrength;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.MaterialType;

import java.util.*;

import static io.github.mikip98.humilityafm.HumilityAFM.LOGGER;

/**
 * This class contains the materials required from block generation and fabric data generation.
 * It is derived from the RawGenerationData based on the active mods detected by ModSupportManager with added metadata.
 */
public class ActiveGenerationData extends RawGenerationData {
    // UTILITY
    protected static Pair<List<String[]>, List<BlockMaterial.Metadata>> activeVanillaWoodTypes = getGetActiveVanillaWood();
    protected static Pair<List<String[]>, List<BlockMaterial.Metadata>> activeModdedWoodTypes = getGetActiveModdedWood();

    // READY TO USE BLOCK SPECIFIC GENERATION DATA
    public static LayeredMaterialArrayCombiner cabinetVariantMaterials = generateCabinetBlockMaterials();
    public static MaterialMultiArrayIterable forcedCornerStairsVariantMaterials = generateForcedCornerStairsMaterials();
    public static MaterialArrayCombiner woodenMosaicVariantMaterials = generateWoodenMosaicMaterials();
    public static LayeredMaterialArrayCombiner terracottaTilesMaterials = new LayeredMaterialArrayCombiner(
            List.of(new String[][]{RawGenerationData.vanillaColorPallet}),
            List.of(new String[][]{RawGenerationData.vanillaColorPallet})
    );
    // CANDLESTICK BETA
    public static MaterialMultiArrayIterable simpleCandlestickMaterials = generateSimpleCandlestickMaterials();
    public static MaterialMultiArrayIterable[] rustingCandlestickMaterials = generateRustingCandlestickMaterials();
    // COLOURED FEATURE SET BETA
    public static MaterialMultiArrayIterable colouredFeatureSetMaterials = new MaterialMultiArrayIterable(RawGenerationData.vanillaColorPallet);


    /**
     * Made to trigger static initialisation of the class.
     * It also logs some info about the created generation data.
     * The logging also performs some checks that ensure the proper workings of the generation.
     */
    public static void init() {
        // Log the number of generated materials
        // Cabinet materials
        int maxAmount = RawGenerationData.allWoodTypes.size() * RawGenerationData.vanillaColorPallet.length;
        logMaterialCount("Cabinet", cabinetVariantMaterials.size(), maxAmount);
        // Forced corner stairs
        maxAmount = RawGenerationData.allWoodTypes.size() + RawGenerationData.vanillaStonyMaterials.size();
        logMaterialCount("Forced corner stairs", forcedCornerStairsVariantMaterials.size(), maxAmount);
        // Wooden mosaic
        maxAmount = RawGenerationData.allWoodTypes.size() * (RawGenerationData.allWoodTypes.size() - 1);
        logMaterialCount("Wooden mosaic", woodenMosaicVariantMaterials.size(), maxAmount);
        // Terracotta tiles
        maxAmount = RawGenerationData.vanillaColorPallet.length * (RawGenerationData.vanillaColorPallet.length - 1);
        logMaterialCount("Terracotta tiles", terracottaTilesMaterials.size(), maxAmount);

        // Simple candlestick
        maxAmount = RawGenerationData.vanillaCandlestickMetals.length;
        logMaterialCount("Simple candlestick", simpleCandlestickMaterials.size(), maxAmount);
        // Rusting candlestick
        maxAmount = RawGenerationData.vanillaRustableCandlestickMetals.stream().mapToInt((set) -> set.length).sum();
        logMaterialCount("Rusting candlestick", Arrays.stream(rustingCandlestickMaterials).mapToInt(SizedIterable::size).sum(), maxAmount);

        // Coloured feature set
        maxAmount = RawGenerationData.vanillaColorPallet.length;
        logMaterialCount("Coloured feature set", colouredFeatureSetMaterials.size(), maxAmount);
    }


    /**
     * Sets all the active generation data to 'null' to free up memory.
     * This should be called after all blocks have been generated and registered.
     * Note that this does not prevent further usage of the data, it will simply cause a NPE if attempted.
     * Does nothing if in datagen mode is enabled, as it somehow ran before static data initialisation.
     */
    public static void clear() {
        // For some reason does not work with datagen as it somehow runs before static initialisation...
        // This isn't much of a problem though as datagen does not need lower memory usage
        if (ModConfig.datagenMode) return;

        // UTILITY
        activeVanillaWoodTypes = null;
        activeModdedWoodTypes = null;
        // READY TO USE BLOCK SPECIFIC GENERATION DATA
        cabinetVariantMaterials = null;
        forcedCornerStairsVariantMaterials = null;
        woodenMosaicVariantMaterials = null;
        terracottaTilesMaterials = null;
        // CANDLESTICK BETA
        simpleCandlestickMaterials = null;
        rustingCandlestickMaterials = null;
        // COLOURED FEATURE SET BETA
        colouredFeatureSetMaterials = null;
    }


    @SuppressWarnings("unchecked")
    protected static LayeredMaterialArrayCombiner generateCabinetBlockMaterials() {
        List<BlockMaterial.Metadata[]> metadataArrays = new ArrayList<>();

        // LAYER 1 - WOOD (first vanilla, then modded)
        List<String[]> woodLayer = new ArrayList<>();
        List<BlockMaterial.Metadata> woodMetadataList = new ArrayList<>();
        // FIRST, vanilla wood (first burnable, then fireproof)
        woodLayer.addAll(activeVanillaWoodTypes.left());
        woodMetadataList.addAll(activeVanillaWoodTypes.right());
        // SECOND, modded wood (first burnable, then fireproof)
        woodLayer.addAll(activeModdedWoodTypes.left());
        woodMetadataList.addAll(activeModdedWoodTypes.right());
        metadataArrays.add(woodMetadataList.toArray(BlockMaterial.Metadata[]::new));

        // LAYER 2 - COLOUR
        List<String[]> colourLayer = new ArrayList<>();
        colourLayer.add(RawGenerationData.vanillaColorPallet);
        metadataArrays.add(new BlockMaterial.Metadata[]{BlockMaterial.Metadata.empty()});

        List<String[]>[] layers = new List[]{woodLayer, colourLayer};

        return new LayeredMaterialArrayCombiner(metadataArrays, layers);
    }

    protected static MaterialMultiArrayIterable generateForcedCornerStairsMaterials() {
        List<String[]> materialSuppliers = new ArrayList<>();
        List<BlockMaterial.Metadata> materialMetadataList = new ArrayList<>();

        // FIRST, vanilla wood types (first burnable, then fireproof)
        materialSuppliers.addAll(activeVanillaWoodTypes.left());
        materialMetadataList.addAll(activeVanillaWoodTypes.right());

        // SECOND, vanilla stony materials
        for (Pair<BlockStrength, String[]> material : RawGenerationData.vanillaStonyMaterialsPerStrength) {
            materialSuppliers.add(material.second());
            materialMetadataList.add(BlockMaterial.Metadata.of(MaterialType.STONY, material.first(), null));
        }

        // FOURTH, modded wood types (first burnable, then fireproof)
        materialSuppliers.addAll(activeModdedWoodTypes.left());
        materialMetadataList.addAll(activeModdedWoodTypes.right());

        // FIFTH, modded stony materials
        // TODO

        return MaterialMultiArrayIterable.of(materialMetadataList.toArray(BlockMaterial.Metadata[]::new), materialSuppliers.toArray(String[][]::new));
    }

    protected static MaterialArrayCombiner generateWoodenMosaicMaterials() {
        List<String[]> woodenMosaicWoodSuppliers = new ArrayList<>();
        List<BlockMaterial.Metadata> materialMetadataList = new ArrayList<>();

        // FIRST, vanilla wood types (first burnable, then fireproof)
        woodenMosaicWoodSuppliers.addAll(activeVanillaWoodTypes.left());
        materialMetadataList.addAll(activeVanillaWoodTypes.right());
        // SECOND, modded wood types (first burnable, then fireproof)
        woodenMosaicWoodSuppliers.addAll(activeModdedWoodTypes.left());
        materialMetadataList.addAll(activeModdedWoodTypes.right());

        return new MaterialArrayCombiner(
                materialMetadataList.toArray(BlockMaterial.Metadata[]::new),
                woodenMosaicWoodSuppliers.toArray(String[][]::new)
        );
    }

    // CANDLESTICK BETA ------------------------------------------------------------------------------------------------
    protected static MaterialMultiArrayIterable generateSimpleCandlestickMaterials() {
        return MaterialMultiArrayIterable.of(
                new BlockMaterial.Metadata[]{BlockMaterial.Metadata.empty()},
                new String[][]{RawGenerationData.vanillaCandlestickMetals}
        );
    }

    protected static MaterialMultiArrayIterable[] generateRustingCandlestickMaterials() {
        List<MaterialMultiArrayIterable> rustingCandlestickMaterialList = new ArrayList<>();
        for (String[] metalSet : RawGenerationData.vanillaRustableCandlestickMetals) {
            rustingCandlestickMaterialList.add(MaterialMultiArrayIterable.of(
                    new BlockMaterial.Metadata[]{BlockMaterial.Metadata.empty()},
                    new String[][]{metalSet}
            ));
        }
        return rustingCandlestickMaterialList.toArray(MaterialMultiArrayIterable[]::new);
    }


    // -----------------------------------------------------------------------------------------------------------------
    // ------ UTILITY --------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Metadata containing collection of vanilla wood types
     * @return A pair of lists of vanilla wood types and their metadata
     */
    protected static Pair<List<String[]>, List<BlockMaterial.Metadata>> getGetActiveVanillaWood() {
        List<String[]> vanillaWood = new ArrayList<>();
        List<BlockMaterial.Metadata> vanillaWoodMetadataList = new ArrayList<>();

        // FIRST, vanilla burnable wood types
        vanillaWood.add(RawGenerationData.vanillaOverworldWoodTypes);
        vanillaWoodMetadataList.add(BlockMaterial.Metadata.of(MaterialType.BURNABLE_WOOD));
        // SECOND, vanilla fireproof wood types
        vanillaWood.add(RawGenerationData.vanillaNetherWoodTypes);
        vanillaWoodMetadataList.add(BlockMaterial.Metadata.of(MaterialType.FIREPROOF_WOOD));

        return Pair.of(vanillaWood, vanillaWoodMetadataList);
    }
    /**
     * Retrieves and returns modded wood types from currently active supported mods.
     * @return A pair of lists of modded wood types and their metadata
     */
    protected static Pair<List<String[]>, List<BlockMaterial.Metadata>> getGetActiveModdedWood() {
        List<String[]> activeWoodTypes = new ArrayList<>();
        List<BlockMaterial.Metadata> activeWoodMetadataList = new ArrayList<>();

        // FIRST, modded burnable wood types (and fireproof if added by the same mod)
        Set<SupportedMods> alreadyAddedMods = EnumSet.noneOf(SupportedMods.class);
        for (Map.Entry<SupportedMods, String[]> modEntry : RawGenerationData.moddedBurnableWoodTypes.entrySet()) {
            SupportedMods mod = modEntry.getKey();
            if (ModSupportManager.isModLoaded(mod)) {
                alreadyAddedMods.add(mod);

                String[] moddedWoodTypes = modEntry.getValue();
                activeWoodTypes.add(moddedWoodTypes);
                activeWoodMetadataList.add(BlockMaterial.Metadata.of(MaterialType.BURNABLE_WOOD, null, mod));

                if (RawGenerationData.moddedFireProofWoodTypes.containsKey(mod)) {
                    moddedWoodTypes = RawGenerationData.moddedFireProofWoodTypes.get(mod);
                    activeWoodTypes.add(moddedWoodTypes);
                    activeWoodMetadataList.add(BlockMaterial.Metadata.of(MaterialType.FIREPROOF_WOOD, null, mod));
                }
            }
        }
        // SECOND, modded fireproof wood types (if not already added above)
        for (Map.Entry<SupportedMods, String[]> modEntry : RawGenerationData.moddedFireProofWoodTypes.entrySet()) {
            SupportedMods mod = modEntry.getKey();
            if (ModSupportManager.isModLoaded(mod) && !alreadyAddedMods.contains(mod)) {
                String[] moddedWoodTypes = modEntry.getValue();
                activeWoodTypes.add(moddedWoodTypes);
                activeWoodMetadataList.add(BlockMaterial.Metadata.of(MaterialType.FIREPROOF_WOOD, null, mod));
            }
        }
        return Pair.of(activeWoodTypes, activeWoodMetadataList);
    }

    /**
     * Logs the number of active variant materials.
     * Additionally, throws if the active number of materials is greater than the maximal allowed,
     * Or if the number of active variant materials does not match the expected amount (datagen mode).
     * @param title The name of the material group
     * @param activeAmount The number of active materials
     * @param maxAmount The maximum amount of materials possible if all supported mods are present
     */
    protected static void logMaterialCount(String title, int activeAmount, int maxAmount) {
        LOGGER.info("{} variant materials initialised with {} out of {} materials.", title, activeAmount, maxAmount);

        if (activeAmount > maxAmount)
            throw new IllegalStateException(title + " variant materials size of '" + activeAmount + "' exceeds the max expected size of '" + maxAmount + "'!");

        if (ModConfig.datagenMode && activeAmount != maxAmount)
            throw new IllegalStateException(title + " variant materials size does not match the expected size! " +
                    "Expected: " + maxAmount + ", Actual: " + activeAmount +
                    ". Make sure all supported mods are present during datagen."
            );
    }
}
