package io.github.mikip98.humilityafm.datagen;

#if MC_VERSION < 12104
import io.github.mikip98.humilityafm.datagen.AFMRecipeProvider;
#endif
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.registries.ItemRegistry;
import io.github.mikip98.humilityafm.util.generation_data.ActiveGenerationData;
import io.github.mikip98.humilityafm.util.generation_data.RawGenerationData;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import io.github.mikip98.humilityafm.util.Pair;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.BlockMaterial;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.MaterialType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
#if MC_VERSION < 12006
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
#endif
#if MC_VERSION < 12004
import net.minecraft.data.server.recipe.RecipeJsonProvider;
#endif
#if MC_VERSION >= 12006
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
#endif
#if MC_VERSION > 12001 && MC_VERSION < 12104
import net.minecraft.data.server.recipe.RecipeExporter;
#elif MC_VERSION > 12001
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
#endif
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
#if MC_VERSION >= 12006
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
#endif

import java.util.*;
#if MC_VERSION < 12004
import java.util.function.Consumer;
#elif MC_VERSION >= 12006
import java.util.concurrent.CompletableFuture;
#endif import static io.github.mikip98.humilityafm.HumilityAFM.*;

public class AMFRecipeGenerator extends #if MC_VERSION < 12104 AFMRecipeProvider #else FabricRecipeProvider #endif {
    #if MC_VERSION < 12006
    public AMFRecipeGenerator(FabricDataOutput output) {
        super(output);
    }
    #else
    public AMFRecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }
    #endif

    #if MC_VERSION < 12104
    @Override
    public void generate(
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter
    ) {
    #else
    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new AFMRecipeProvider(registryLookup, exporter) {
            @Override
            public void generate() {
                RegistryWrapper.Impl<Item> itemLookup = registries.getOrThrow(RegistryKeys.ITEM);
    #endif

        // ............ TEST BLOCKS & BLOCK ITEMS ............

        // Cabinet Blocks
        offerCabinetRecipe(
                #if MC_VERSION >= 12104 itemLookup, #endif exporter,
                ItemRegistry.CABINET_ITEM,
                Items.PETRIFIED_OAK_SLAB,
                Items.WHITE_CARPET,
                "cabinets/"
        );

        // Illuminated Cabinet Blocks
        offerIlluminatedCabinetRecipe(
                #if MC_VERSION >= 12104 itemLookup, #endif exporter,
                ItemRegistry.ILLUMINATED_CABINET_ITEM,
                ItemRegistry.CABINET_ITEM
        );

        // ............ FINAL BLOCKS & BLOCK ITEMS ............
        generateCabinetRecipies(#if MC_VERSION >= 12104 itemLookup, #endif exporter);
        generateWoodenMosaicRecipies(#if MC_VERSION >= 12104 itemLookup, #endif exporter);
        generateTerracottaTileRecipies(#if MC_VERSION >= 12104 itemLookup, #endif exporter);
        generateForcedCornerStairsRecipies(#if MC_VERSION >= 12104 itemLookup, #endif exporter);
        generateSpecialJackOLanternRecipies(#if MC_VERSION >= 12104 itemLookup, #endif exporter);
        // Optional
        generateCandlestickRecipies(#if MC_VERSION >= 12104 itemLookup, #endif exporter);
        generateGlowingPowderRecipies(#if MC_VERSION >= 12104 itemLookup, #endif exporter);
        generateLightStripRecipes(#if MC_VERSION >= 12104 itemLookup, #endif exporter);
        generateColouredTorchRecipies(#if MC_VERSION >= 12104 itemLookup, #endif exporter);
        generateColouredJackOLanternRecipies(#if MC_VERSION >= 12104 itemLookup, #endif exporter);
    }

    protected void generateCabinetRecipies(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter
    ) {
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
            if (material.layers().length != 2)
                throw new IllegalStateException("Cabinet material must have exactly two layers, but found: " + Arrays.toString(material.layers()));

            final BlockMaterial.Layer woodLayer = material.layers()[0];
            final String colourName = material.layers()[1].name();
            final SupportedMods woodSourceMod = woodLayer.metadata().sourceMod();

            #if MC_VERSION == 12001
            Consumer<RecipeJsonProvider> currentExporter = exporter;
            #else
            RecipeExporter currentExporter = exporter;
            #endif

            // Apply the condition if the source mod isn't vanilla Minecraft
            if (woodSourceMod != null) {
                #if MC_VERSION < 12006
                currentExporter = withConditions(exporter, DefaultResourceConditions.allModsLoaded(woodSourceMod.modId));
                #else
                currentExporter = withConditions(exporter, ResourceConditions.allModsLoaded(woodSourceMod.modId));
                #endif
            }

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

            // Normal recipes
            offerCabinetRecipe(
                    #if MC_VERSION >= 12104 itemLookup, #endif currentExporter,
                    cabinetBlockItemVariant,
                    getItemFromName(woodLayer.name() + "_slab", woodSourceMod),
                    getItemFromName(colourName + "_carpet"),
                    "cabinets/"
            );
            offerIlluminatedCabinetRecipe(
                    #if MC_VERSION >= 12104 itemLookup, #endif currentExporter,
                    illuminatedCabinetBlockItemVariant,
                    cabinetBlockItemVariant
            );

            // Colour change recipies
            final Item currentDye = getItemFromName(colourName + "_dye");

            offerColorChangeRecipie(
                    #if MC_VERSION >= 12104 itemLookup, #endif currentExporter,
                    cabinetBlockItemVariant,
                    Ingredient.ofItems(sameWoodOtherColourCabinets),
                    currentDye,
                    MOD_ID + "/cabinets",
                    "cabinets/color_change/"
            );
            offerColorChangeRecipie(
                    #if MC_VERSION >= 12104 itemLookup, #endif currentExporter,
                    illuminatedCabinetBlockItemVariant,
                    Ingredient.ofItems(sameWoodOtherColourIlluminatedCabinets),
                    currentDye,
                    MOD_ID + "/illuminated_cabinets",
                    "illuminated_cabinets/color_change/"
            );
            ++i;
        }
    }

    protected void generateWoodenMosaicRecipies(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter
    ) {
        Iterable<BlockMaterial> woodenMosaicMaterials = ActiveGenerationData.woodenMosaicVariantMaterials;
        int i = 0;
        for (BlockMaterial material : woodenMosaicMaterials) {
            BlockMaterial.Layer woodMaterial1 = material.layers()[0];
            BlockMaterial.Layer woodMaterial2 = material.layers()[1];

            SupportedMods sourceMod1 = woodMaterial1.metadata().sourceMod();
            SupportedMods sourceMod2 = woodMaterial2.metadata().sourceMod();

            #if MC_VERSION == 12001
            Consumer<RecipeJsonProvider> currentExporter = exporter;
            #else
            RecipeExporter currentExporter = exporter;
            #endif

            // Apply the condition if the source mod isn't vanilla Minecraft
            if (sourceMod1 != null && sourceMod2 != null)
                #if MC_VERSION < 12006
                currentExporter = withConditions(exporter, DefaultResourceConditions.allModsLoaded(sourceMod1.modId, sourceMod2.modId));
                #else
                currentExporter = withConditions(exporter, ResourceConditions.allModsLoaded(sourceMod1.modId, sourceMod2.modId));
                #endif
            else if (sourceMod1 != null || sourceMod2 != null) {
                #if MC_VERSION < 12006
                currentExporter = withConditions(exporter, DefaultResourceConditions.allModsLoaded(Objects.requireNonNullElse(sourceMod1, sourceMod2).modId));
                #else
                currentExporter = withConditions(exporter, ResourceConditions.allModsLoaded(Objects.requireNonNullElse(sourceMod1, sourceMod2).modId));
                #endif
            }

            Item plank1 = getItemFromName(woodMaterial1.name() + "_planks", sourceMod1);
            Item plank2 = getItemFromName(woodMaterial2.name() + "_planks", sourceMod2);

            offerWoodenMosaicRecipe(
                    #if MC_VERSION >= 12104 itemLookup, #endif currentExporter,
                    BlockRegistry.WOODEN_MOSAIC_VARIANTS[i],
                    plank1,
                    plank2,
                    "wooden_mosaics/"
            );
            // TODO: Double check this still works
            final int j = getMirrorIndex(i, RawGenerationData.allWoodTypes.size());
            offerChangeRecipie(
                    #if MC_VERSION >= 12104 itemLookup, #endif currentExporter,
                    BlockRegistry.WOODEN_MOSAIC_VARIANTS[i],
                    BlockRegistry.WOODEN_MOSAIC_VARIANTS[j],
                    MOD_ID + "/wooden_mosaics",
                    "wooden_mosaics/rotation/"
            );

            // Datapack alternative recipies
            // The output of this should be moved from 'generated/data/humility-afm/recipies'
            // to its own dedicated datapack folder
            offerAlternateWoodenMosaicRecipe(
                    #if MC_VERSION >= 12104 itemLookup, #endif currentExporter,
                    BlockRegistry.WOODEN_MOSAIC_VARIANTS[i],
                    plank1,
                    plank2,
                    "datapack/wooden_mosaics/alternative/"
            );

            ++i;
        }
    }

    protected void generateTerracottaTileRecipies(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter
    ) {
        int i = 0;
        for (BlockMaterial material : ActiveGenerationData.terracottaTilesMaterials) {
            Item terracotta1 = getItemFromName(material.layers()[0].name() + "_terracotta");
            Item terracotta2 = getItemFromName(material.layers()[1].name() + "_terracotta");

            offerTerracottaTileRecipe(
                    #if MC_VERSION >= 12104 itemLookup, #endif exporter,
                    BlockRegistry.TERRACOTTA_TILE_VARIANTS[i],
                    terracotta1,
                    terracotta2,
                    "terracotta_tiles/"
            );
            final int j = getMirrorIndex(i, RawGenerationData.vanillaColorPallet.length);
            offerChangeRecipie(
                    #if MC_VERSION >= 12104 itemLookup, #endif exporter,
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

    protected void generateForcedCornerStairsRecipies(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter
    ) {
        int i = 0;
        for (BlockMaterial stairMaterial : ActiveGenerationData.forcedCornerStairsVariantMaterials) {
            if (stairMaterial.layers().length != 1)
                throw new IllegalArgumentException("Forced corner stairs material must have exactly one layer, but found: " + Arrays.toString(stairMaterial.layers()));
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

            #if MC_VERSION == 12001
            Consumer<RecipeJsonProvider> currentExporter = exporter;
            #else
            RecipeExporter currentExporter = exporter;
            #endif

            // Apply the condition if the source mod isn't vanilla Minecraft
            if (sourceMod != null) {
                #if MC_VERSION < 12006
                currentExporter = withConditions(exporter, DefaultResourceConditions.allModsLoaded(sourceMod.modId));
                #else
                currentExporter = withConditions(exporter, ResourceConditions.allModsLoaded(sourceMod.modId));
                #endif
            }

            final ItemConvertible inner_stairs = BlockRegistry.INNER_STAIRS_BLOCK_VARIANTS[i];
            final ItemConvertible outer_stairs = BlockRegistry.OUTER_STAIRS_BLOCK_VARIANTS[i];

            offerChangeRecipie(#if MC_VERSION >= 12104 itemLookup, #endif currentExporter, inner_stairs, stairs, MOD_ID + "/stairs", "stairs/" + modId + "/inner/");
            offerChangeRecipie(#if MC_VERSION >= 12104 itemLookup, #endif currentExporter, outer_stairs, inner_stairs, MOD_ID + "/stairs", "stairs/" + modId + "/outer/");
            offerChangeRecipie(#if MC_VERSION >= 12104 itemLookup, #endif currentExporter, stairs, outer_stairs, MOD_ID + "/stairs", "stairs/" + modId + "/normal/");
            ++i;
        }
    }

    protected void generateSpecialJackOLanternRecipies(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter
    ) {
        Item carved_pumpkin = Items.CARVED_PUMPKIN;
        offerDoubleInputShapelessRecipe(
                #if MC_VERSION >= 12104 itemLookup, #endif exporter,
                BlockRegistry.JACK_O_LANTERN_REDSTONE,
                carved_pumpkin,
                Items.REDSTONE_TORCH,
                MOD_ID + "/jack_o_lanterns",
                1,
                "jack_o_lanterns/"
        );
        offerDoubleInputShapelessRecipe(
                #if MC_VERSION >= 12104 itemLookup, #endif exporter,
                BlockRegistry.JACK_O_LANTERN_SOUL,
                carved_pumpkin,
                Items.SOUL_TORCH,
                MOD_ID + "/jack_o_lanterns",
                1,
                "jack_o_lanterns/"
        );
    }

    protected void generateColouredJackOLanternRecipies(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter
    ) {
        Item carved_pumpkin = Items.CARVED_PUMPKIN;

        for (int i = 0; i < RawGenerationData.vanillaColorPallet.length; ++i) {
            ItemConvertible jackOLantern = BlockRegistry.COLOURED_JACK_O_LANTERNS[i];
            ItemConvertible torch = BlockRegistry.COLOURED_TORCH_VARIANTS[i];

            offerDoubleInputShapelessRecipe(
                    #if MC_VERSION >= 12104 itemLookup, #endif exporter,
                    jackOLantern,
                    carved_pumpkin,
                    torch,
                    MOD_ID + "/jack_o_lanterns",
                    1,
                    "jack_o_lanterns/normal/"
            );
        }
    }

    protected void generateGlowingPowderRecipies(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter
    ) {
        Item glowstoneDust = Items.GLOWSTONE_DUST;
        Item redstone = Items.REDSTONE;
        int i = 0;
        for (String color : RawGenerationData.vanillaColorPallet) {
            Item glowingPowder = ItemRegistry.GLOWING_POWDER_VARIANTS[i];
            Item dye = getItemFromName(color + "_dye");
            offerTripleInputShapelessRecipe(
                    #if MC_VERSION >= 12104 itemLookup, #endif exporter,
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

    protected void generateColouredTorchRecipies(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter
    ) {
        final int length = ActiveGenerationData.colouredFeatureSetMaterials.size();
        for (int i = 0; i < length; i++) {
            ItemConvertible colouredTorch = BlockRegistry.COLOURED_TORCH_VARIANTS[i];
            Item glowingPowder = ItemRegistry.GLOWING_POWDER_VARIANTS[i];
            offerColouredTorchRecipe(
                    #if MC_VERSION >= 12104 itemLookup, #endif exporter,
                    colouredTorch,
                    glowingPowder, 3,
                    MOD_ID + "/coloured_torches",
                    "coloured_torches/"
            );
        }
    }

    protected void generateCandlestickRecipies(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter
    ) {
        int i = 0;
        for (BlockMaterial material : ActiveGenerationData.simpleCandlestickMaterials) {
            final String metal = material.layers()[0].name();
            final Item ingot = getItemFromName(metal + "_ingot");
            offerCandlestickRecipie(#if MC_VERSION >= 12104 itemLookup, #endif exporter, ItemRegistry.CANDLESTICK_ITEM_VARIANTS[i], ingot, "candlesticks/");
            ++i;
        }
        i = 0;
        for (Iterable<BlockMaterial> meterials : ActiveGenerationData.rustingCandlestickMaterials) {
            final String metal = meterials.iterator().next().layers()[0].name();
            final Item ingot = getItemFromName(metal + "_ingot");
            offerCandlestickRecipie(#if MC_VERSION >= 12104 itemLookup, #endif exporter, ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS[i][0], ingot, "candlesticks/");
            ++i;
        }
    }

    protected void generateLightStripRecipes(
            #if MC_VERSION >= 12104 RegistryWrapper.Impl<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<RecipeJsonProvider> #else RecipeExporter #endif exporter
    ) {
        for (int i = 0; i < RawGenerationData.vanillaColorPallet.length; ++i) {
            offerLightStripRecipie(
            #if MC_VERSION >= 12104 itemLookup, #endif exporter,
                    BlockRegistry.LIGHT_STRIP_VARIANTS[i],
                    ItemRegistry.GLOWING_POWDER_VARIANTS[i],
                    "light_strips/"
            );
        }
    }
        #if MC_VERSION >= 12104
        };
    }
        #endif


    protected static Item getItemFromName(String name) {
        return Registries.ITEM.get(getVanillaId(name));
    }
    protected static Item getItemFromName(String name, SupportedMods mod) {
        Item item = Registries.ITEM.get(getVMId(mod, name));
        if (item == Items.AIR)
            throw new IllegalStateException("Item not found: '" + name + "' in mod: '" + (mod != null ? mod.modId : "vanilla") + "'");
        return item;
    }

    #if MC_VERSION >= 12104
    @Override
    public String getName() {
        return "AFMRecipeGenerator";
    }
    #endif
}
