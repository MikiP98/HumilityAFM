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
#if MC_VERSION >= 12104
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
#endif
import net.minecraft.core.registries.BuiltInRegistries;
#if MC_VERSION >= 12104
import net.minecraft.core.registries.Registries;
#endif
#if MC_VERSION < 12004
import net.minecraft.data.recipes.FinishedRecipe;
#else
import net.minecraft.data.recipes.RecipeOutput;
#endif
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
#if MC_VERSION >= 12006
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.NotNull;
#endif

import java.util.*;
#if MC_VERSION < 12004
import java.util.function.Consumer;
#elif MC_VERSION >= 12006
import java.util.concurrent.CompletableFuture;
#endif

import static io.github.mikip98.humilityafm.HumilityAFM.*;

public class AMFRecipeGenerator extends #if MC_VERSION < 12104 AFMRecipeProvider #else FabricRecipeProvider #endif {
    #if MC_VERSION < 12006
    public AMFRecipeGenerator(FabricDataOutput output) {
        super(output);
    }
    
    #else
    public AMFRecipeGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }
    #endif

    #if MC_VERSION < 12104
    @Override
    public void buildRecipes(
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif output
    ) {
    #else
    @Override
    protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput output) {
        return new AFMRecipeProvider(registryLookup, output) {
            @Override
            public void buildRecipes() {
                HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);
    #endif

        // ............ TEST BLOCKS & BLOCK ITEMS ............

        // Cabinet Blocks
        offerCabinetRecipe(
                #if MC_VERSION >= 12104 itemLookup, #endif output,
                ItemRegistry.CABINET_ITEM,
                Items.PETRIFIED_OAK_SLAB,
                Items.WHITE_CARPET,
                "cabinets/"
        );

        // Illuminated Cabinet Blocks
        offerIlluminatedCabinetRecipe(
                #if MC_VERSION >= 12104 itemLookup, #endif output,
                ItemRegistry.ILLUMINATED_CABINET_ITEM,
                ItemRegistry.CABINET_ITEM
        );

        // ............ FINAL BLOCKS & BLOCK ITEMS ............
        generateCabinetRecipes(#if MC_VERSION >= 12104 itemLookup, #endif output);
        generateWoodenMosaicRecipes(#if MC_VERSION >= 12104 itemLookup, #endif output);
        generateTerracottaTileRecipes(#if MC_VERSION >= 12104 itemLookup, #endif output);
        generateForcedCornerStairsRecipes(#if MC_VERSION >= 12104 itemLookup, #endif output);
        generateSpecialJackOLanternRecipes(#if MC_VERSION >= 12104 itemLookup, #endif output);
        // Optional
        generateCandlestickRecipes(#if MC_VERSION >= 12104 itemLookup, #endif output);
        generateGlowingPowderRecipes(#if MC_VERSION >= 12104 itemLookup, #endif output);
        generateLightStripRecipes(#if MC_VERSION >= 12104 itemLookup, #endif output);
        generateColouredTorchRecipes(#if MC_VERSION >= 12104 itemLookup, #endif output);
        generateColouredJackOLanternRecipes(#if MC_VERSION >= 12104 itemLookup, #endif output);
    }

    protected void generateCabinetRecipes(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif output
    ) {
        Map<Pair<SupportedMods, String>, List<Pair<ItemLike, String>>> perWoodCabinets = new HashMap<>();
        Map<Pair<SupportedMods, String>, List<Pair<ItemLike, String>>> perWoodIlluminatedCabinets = new HashMap<>();
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

            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif currentExporter = output;

            // Apply the condition if the source mod isn't vanilla Minecraft
            if (woodSourceMod != null) {
                #if MC_VERSION < 12006
                currentExporter = withConditions(output, DefaultResourceConditions.allModsLoaded(woodSourceMod.modId));
                #else
                currentExporter = withConditions(output, ResourceConditions.allModsLoaded(woodSourceMod.modId));
                #endif
            }

            final Pair<SupportedMods, String> key = new Pair<>(woodSourceMod, woodLayer.name());
            final ItemLike[] sameWoodOtherColourCabinets = perWoodCabinets
                    .get(key)
                    .stream()
                    .filter(entry -> !entry.second().equals(colourName))
                    .map(Pair::first).toArray(ItemLike[]::new);
            final ItemLike[] sameWoodOtherColourIlluminatedCabinets = perWoodIlluminatedCabinets
                    .get(key)
                    .stream()
                    .filter(entry -> !entry.second().equals(colourName))
                    .map(Pair::first).toArray(ItemLike[]::new);

            final ItemLike cabinetBlockItemVariant = ItemRegistry.CABINET_ITEM_VARIANTS[i];
            final ItemLike illuminatedCabinetBlockItemVariant = ItemRegistry.ILLUMINATED_CABINET_ITEM_VARIANTS[i];

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
                    Ingredient.of(sameWoodOtherColourCabinets),
                    currentDye,
                    MOD_ID + "/cabinets",
                    "cabinets/color_change/"
            );
            offerColorChangeRecipie(
                    #if MC_VERSION >= 12104 itemLookup, #endif currentExporter,
                    illuminatedCabinetBlockItemVariant,
                    Ingredient.of(sameWoodOtherColourIlluminatedCabinets),
                    currentDye,
                    MOD_ID + "/illuminated_cabinets",
                    "illuminated_cabinets/color_change/"
            );
            ++i;
        }
    }

    protected void generateWoodenMosaicRecipes(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif output
    ) {
        Iterable<BlockMaterial> woodenMosaicMaterials = ActiveGenerationData.woodenMosaicVariantMaterials;
        int i = 0;
        for (BlockMaterial material : woodenMosaicMaterials) {
            BlockMaterial.Layer woodMaterial1 = material.layers()[0];
            BlockMaterial.Layer woodMaterial2 = material.layers()[1];

            SupportedMods sourceMod1 = woodMaterial1.metadata().sourceMod();
            SupportedMods sourceMod2 = woodMaterial2.metadata().sourceMod();

            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif currentExporter = output;

            // Apply the condition if the source mod isn't vanilla Minecraft
            if (sourceMod1 != null && sourceMod2 != null)
                #if MC_VERSION < 12006
                currentExporter = withConditions(output, DefaultResourceConditions.allModsLoaded(sourceMod1.modId, sourceMod2.modId));
                #else
                currentExporter = withConditions(output, ResourceConditions.allModsLoaded(sourceMod1.modId, sourceMod2.modId));
                #endif
            else if (sourceMod1 != null || sourceMod2 != null) {
                #if MC_VERSION < 12006
                currentExporter = withConditions(output, DefaultResourceConditions.allModsLoaded(Objects.requireNonNullElse(sourceMod1, sourceMod2).modId));
                #else
                currentExporter = withConditions(output, ResourceConditions.allModsLoaded(Objects.requireNonNullElse(sourceMod1, sourceMod2).modId));
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

    protected void generateTerracottaTileRecipes(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif output
    ) {
        int i = 0;
        for (BlockMaterial material : ActiveGenerationData.terracottaTilesMaterials) {
            Item terracotta1 = getItemFromName(material.layers()[0].name() + "_terracotta");
            Item terracotta2 = getItemFromName(material.layers()[1].name() + "_terracotta");

            offerTerracottaTileRecipe(
                    #if MC_VERSION >= 12104 itemLookup, #endif output,
                    BlockRegistry.TERRACOTTA_TILE_VARIANTS[i],
                    terracotta1,
                    terracotta2,
                    "terracotta_tiles/"
            );
            final int j = getMirrorIndex(i, RawGenerationData.vanillaColorPallet.length);
            offerChangeRecipie(
                    #if MC_VERSION >= 12104 itemLookup, #endif output,
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

    protected void generateForcedCornerStairsRecipes(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif output
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

            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif currentExporter = output;

            // Apply the condition if the source mod isn't vanilla Minecraft
            if (sourceMod != null) {
                #if MC_VERSION < 12006
                currentExporter = withConditions(output, DefaultResourceConditions.allModsLoaded(sourceMod.modId));
                #else
                currentExporter = withConditions(output, ResourceConditions.allModsLoaded(sourceMod.modId));
                #endif
            }

            final ItemLike inner_stairs = BlockRegistry.INNER_STAIRS_BLOCK_VARIANTS[i];
            final ItemLike outer_stairs = BlockRegistry.OUTER_STAIRS_BLOCK_VARIANTS[i];

            offerChangeRecipie(#if MC_VERSION >= 12104 itemLookup, #endif currentExporter, inner_stairs, stairs, MOD_ID + "/stairs", "stairs/" + modId + "/inner/");
            offerChangeRecipie(#if MC_VERSION >= 12104 itemLookup, #endif currentExporter, outer_stairs, inner_stairs, MOD_ID + "/stairs", "stairs/" + modId + "/outer/");
            offerChangeRecipie(#if MC_VERSION >= 12104 itemLookup, #endif currentExporter, stairs, outer_stairs, MOD_ID + "/stairs", "stairs/" + modId + "/normal/");
            ++i;
        }
    }

    protected void generateSpecialJackOLanternRecipes(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif output
    ) {
        Item carved_pumpkin = Items.CARVED_PUMPKIN;
        offerDoubleInputShapelessRecipe(
                #if MC_VERSION >= 12104 itemLookup, #endif output,
                BlockRegistry.JACK_O_LANTERN_REDSTONE,
                carved_pumpkin,
                Items.REDSTONE_TORCH,
                MOD_ID + "/jack_o_lanterns",
                1,
                "jack_o_lanterns/"
        );
        offerDoubleInputShapelessRecipe(
                #if MC_VERSION >= 12104 itemLookup, #endif output,
                BlockRegistry.JACK_O_LANTERN_SOUL,
                carved_pumpkin,
                Items.SOUL_TORCH,
                MOD_ID + "/jack_o_lanterns",
                1,
                "jack_o_lanterns/"
        );
    }

    protected void generateColouredJackOLanternRecipes(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif output
    ) {
        Item carved_pumpkin = Items.CARVED_PUMPKIN;

        for (int i = 0; i < RawGenerationData.vanillaColorPallet.length; ++i) {
            ItemLike jackOLantern = BlockRegistry.COLOURED_JACK_O_LANTERNS[i];
            ItemLike torch = BlockRegistry.COLOURED_TORCH_VARIANTS[i];

            offerDoubleInputShapelessRecipe(
                    #if MC_VERSION >= 12104 itemLookup, #endif output,
                    jackOLantern,
                    carved_pumpkin,
                    torch,
                    MOD_ID + "/jack_o_lanterns",
                    1,
                    "jack_o_lanterns/normal/"
            );
        }
    }

    protected void generateGlowingPowderRecipes(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif output
    ) {
        Item glowstoneDust = Items.GLOWSTONE_DUST;
        Item redstone = Items.REDSTONE;
        int i = 0;
        for (String color : RawGenerationData.vanillaColorPallet) {
            Item glowingPowder = ItemRegistry.GLOWING_POWDER_VARIANTS[i];
            Item dye = getItemFromName(color + "_dye");
            offerTripleInputShapelessRecipe(
                    #if MC_VERSION >= 12104 itemLookup, #endif output,
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

    protected void generateColouredTorchRecipes(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif output
    ) {
        final int length = ActiveGenerationData.colouredFeatureSetMaterials.size();
        for (int i = 0; i < length; i++) {
            ItemLike colouredTorch = BlockRegistry.COLOURED_TORCH_VARIANTS[i];
            Item glowingPowder = ItemRegistry.GLOWING_POWDER_VARIANTS[i];
            offerColouredTorchRecipe(
                    #if MC_VERSION >= 12104 itemLookup, #endif output,
                    colouredTorch,
                    glowingPowder, 3,
                    MOD_ID + "/coloured_torches",
                    "coloured_torches/"
            );
        }
    }

    protected void generateCandlestickRecipes(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif output
    ) {
        int i = 0;
        for (BlockMaterial material : ActiveGenerationData.simpleCandlestickMaterials) {
            final String metal = material.layers()[0].name();
            final Item ingot = getItemFromName(metal + "_ingot");
            offerCandlestickRecipie(#if MC_VERSION >= 12104 itemLookup, #endif output, ItemRegistry.CANDLESTICK_ITEM_VARIANTS[i], ingot, "candlesticks/");
            ++i;
        }
        i = 0;
        for (Iterable<BlockMaterial> meterials : ActiveGenerationData.rustingCandlestickMaterials) {
            final String metal = meterials.iterator().next().layers()[0].name();
            final Item ingot = getItemFromName(metal + "_ingot");
            offerCandlestickRecipie(#if MC_VERSION >= 12104 itemLookup, #endif output, ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS[i][0], ingot, "candlesticks/");
            ++i;
        }
    }

    protected void generateLightStripRecipes(
            #if MC_VERSION >= 12104 HolderLookup.RegistryLookup<Item> itemLookup, #endif
            #if MC_VERSION == 12001 Consumer<FinishedRecipe> #else RecipeOutput #endif output
    ) {
        for (int i = 0; i < RawGenerationData.vanillaColorPallet.length; ++i) {
            offerLightStripRecipie(
            #if MC_VERSION >= 12104 itemLookup, #endif output,
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
        return getItemFromName(name, null);
    }
    protected static Item getItemFromName(String name, SupportedMods mod) {
        return BuiltInRegistries.ITEM.getOptional(getVMId(mod, name))
                .orElseThrow(() -> new IllegalStateException(
                        "Item not found: '" + name + "' in mod: '" + (mod != null ? mod.modId : "vanilla") + "'"
                ));
    }

    #if MC_VERSION >= 12104
    @Override
    public @NotNull String getName() {
        return "AFMRecipeGenerator";
    }
    #endif
}
