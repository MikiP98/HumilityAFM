package io.github.mikip98.helpers;

import io.github.mikip98.content.blocks.stairs.InnerStairs;
import io.github.mikip98.content.blocks.stairs.OuterStairs;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import static io.github.mikip98.HumilityAFM.LOGGER;
import static io.github.mikip98.HumilityAFM.MOD_ID;

public class InnerOuterStairsHelper {
    public InnerOuterStairsHelper() {
        throw new IllegalStateException("Utility class, do not instantiate!\nUse \"init()\" and \"registerInnerOuterStairsVariants()\" instead!");
    }

    private static final float WoodStrength = 2.0f;
    private static final float MudBricksStrength = 1.5f;
    private static final String mudBricksVariant = "mud_bricks";
    private static final float SandStoneANDQuartsStrength = 0.8f;
    private static final String[] sandStoneANDQuartsVariants = {"quartz", "sandstone", "red_sandstone"};
    private static final float Stony1Strength = 1.5f;
    private static final String[] stony1Variants = {"blackstone", "andesite", "polished_andesite", "diorite", "polished_diorite", "granite", "polished_granite", "polished_blackstone_brick", "prismarine", "dark_prismarine", "prismarine_bricks", "purpur", "stone", "stone_brick", "mossy_stone_brick"};
    private static final float Stony2Strength = 2f;
    private static final String[] stony2Variants = {"brick", "cobblestone", "mossy_cobblestone", "nether_brick", "red_nether_brick", "polished_blackstone", "smooth_quartz", "smooth_sandstone", "smooth_red_sandstone"};
    private static final float EndStoneStrength = 3f;
    private static final String endStoneVariant = "end_stone_brick";
    private static final float CopperStrength = 3f;
    private static final String[] cutCopperVariants = {"cut_copper", "exposed_cut_copper", "weathered_cut_copper", "oxidized_cut_copper"};
    private static final float DeepSlateStrength = 3.5f;
    private static final String[] deepSlateVariants = {"cobbled_deepslate", "polished_deepslate", "deepslate_brick", "deepslate_tile"};

    public static String[] innerOuterStairsBlockVariantsNames;

    public static Block[] innerStairsBlockVariants;
    public static Item[] innerStairsBlockItemVariants;

    public static Block[] outerStairsBlockVariants;
    public static Item[] outerStairsBlockItemVariants;

    public static void init() {
        //Create stairs variants
        short stairsBlockVariantsCount = (short) (MainHelper.vanillaWoodTypes.length + 1 + sandStoneANDQuartsVariants.length + stony1Variants.length + stony2Variants.length + 1 + cutCopperVariants.length + deepSlateVariants.length);

        innerOuterStairsBlockVariantsNames = new String[stairsBlockVariantsCount];

        innerStairsBlockVariants = new Block[stairsBlockVariantsCount];
        innerStairsBlockItemVariants = new Item[stairsBlockVariantsCount];

        outerStairsBlockVariants = new Block[stairsBlockVariantsCount];
        outerStairsBlockItemVariants = new Item[stairsBlockVariantsCount];

        short id = 0;
        // Generate wood stairs block variants
        // Generate mud bricks stairs block variant
        // And so on...

        // Generate wood stairs block variants
        FabricBlockSettings WoodStairsBlockSettings = FabricBlockSettings.create().strength(WoodStrength).requiresTool().sounds(BlockSoundGroup.WOOD);
        id = createStairsBlockVariants(MainHelper.vanillaWoodTypes, WoodStairsBlockSettings, id);

        // Generate mud bricks stairs block variant
        FabricBlockSettings MudBricksStairsBlockSettings = FabricBlockSettings.create().strength(MudBricksStrength).requiresTool();
        id = createStairsBlockVariant(mudBricksVariant, MudBricksStairsBlockSettings, id);

        // Generate sandstone and quarts stairs block variants
        FabricBlockSettings SandStoneANDQuartsStairsBlockSettings = FabricBlockSettings.create().strength(SandStoneANDQuartsStrength).requiresTool();
        id = createStairsBlockVariants(sandStoneANDQuartsVariants, SandStoneANDQuartsStairsBlockSettings, id);

        // Generate stony1 stairs block variants
        FabricBlockSettings Stony1StairsBlockSettings = FabricBlockSettings.create().strength(Stony1Strength).requiresTool();
        id = createStairsBlockVariants(stony1Variants, Stony1StairsBlockSettings, id);

        // Generate stony2 stairs block variants
        FabricBlockSettings Stony2StairsBlockSettings = FabricBlockSettings.create().strength(Stony2Strength).requiresTool();
        id = createStairsBlockVariants(stony2Variants, Stony2StairsBlockSettings, id);

        // Generate end stone stairs block variant
        FabricBlockSettings EndStoneStairsBlockSettings = FabricBlockSettings.create().strength(EndStoneStrength).requiresTool();
        id = createStairsBlockVariant(endStoneVariant, EndStoneStairsBlockSettings, id);

        // Generate cut copper stairs block variants
        FabricBlockSettings CutCopperStairsBlockSettings = FabricBlockSettings.create().strength(CopperStrength).requiresTool().sounds(BlockSoundGroup.COPPER);
        id = createStairsBlockVariants(cutCopperVariants, CutCopperStairsBlockSettings, id);

        // Generate deep slate stairs block variants
        FabricBlockSettings DeepSlateStairsBlockSettings = FabricBlockSettings.create().strength(DeepSlateStrength).requiresTool().sounds(BlockSoundGroup.DEEPSLATE);
        createStairsBlockVariants(deepSlateVariants, DeepSlateStairsBlockSettings, id);
    }

    // Generate stairs block variants function
    private static short createStairsBlockVariants(String[] stairsVariants, FabricBlockSettings variantSettings, short id) {
        // Generate stairs block variants
        for (String stairsVariant : stairsVariants) {
            innerOuterStairsBlockVariantsNames[id] = stairsVariant;
            //LOGGER.info("Creating inner stairs block variant: " + innerOuterStairsBlockVariantsNames[i]);

            // Create inner stairs block variant
            innerStairsBlockVariants[id] = new InnerStairs(variantSettings);
            // Create inner stairs block variant item
            innerStairsBlockItemVariants[id] = new BlockItem(innerStairsBlockVariants[id], new FabricItemSettings());

            // Create outer stairs block variant
            outerStairsBlockVariants[id] = new OuterStairs(variantSettings);
            // Create outer stairs block variant item
            outerStairsBlockItemVariants[id] = new BlockItem(outerStairsBlockVariants[id], new FabricItemSettings());
            ++id;
        }
        return id;
    }
    private static short createStairsBlockVariant(String stairsVariant, FabricBlockSettings variantSettings, short id) {
        // Generate stairs block variant
        innerOuterStairsBlockVariantsNames[id] = stairsVariant;
        //LOGGER.info("Creating inner stairs block variant: " + innerOuterStairsBlockVariantsNames[i]);

        // Create inner stairs block variant
        innerStairsBlockVariants[id] = new InnerStairs(variantSettings);
        // Create inner stairs block variant item
        innerStairsBlockItemVariants[id] = new BlockItem(innerStairsBlockVariants[id], new FabricItemSettings());

        // Create outer stairs block variant
        outerStairsBlockVariants[id] = new OuterStairs(variantSettings);
        // Create outer stairs block variant item
        outerStairsBlockItemVariants[id] = new BlockItem(outerStairsBlockVariants[id], new FabricItemSettings());
        return ++id;
    }


    public static void registerInnerOuterStairsVariants() {
        //Register stairs variants
        short i = 0;
        for (Block innerStairsBlockVariant : innerStairsBlockVariants) {
//            LOGGER.info("Registering inner stairs block variant: " + innerOuterStairsBlockVariantsNames[i]);
            Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "inner_stairs_" + innerOuterStairsBlockVariantsNames[i]), innerStairsBlockVariant);
            ++i;
        }
        i = 0;
        for (Block outerStairsBlockVariant : outerStairsBlockVariants) {
            //LOGGER.info("Registering outer stairs block variant: " + innerOuterStairsBlockVariantsNames[i + 1]);
            Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "outer_stairs_" + innerOuterStairsBlockVariantsNames[i]), outerStairsBlockVariant);
            ++i;
        }

        i = 0;
        for (Item innerStairsItemVariant : innerStairsBlockItemVariants) {
            //LOGGER.info("Registering inner stairs block variant: " + innerOuterStairsBlockVariantsNames[i]);
            Registry.register(Registries.ITEM, new Identifier(MOD_ID, "inner_stairs_" + innerOuterStairsBlockVariantsNames[i]), innerStairsItemVariant);
            ++i;
        }
        i = 0;
        for (Item outerStairsItemVariant : outerStairsBlockItemVariants) {
            //LOGGER.info("Registering outer stairs block variant: " + innerOuterStairsBlockVariantsNames[i + 1]);
            Registry.register(Registries.ITEM, new Identifier(MOD_ID, "outer_stairs_" + innerOuterStairsBlockVariantsNames[i]), outerStairsItemVariant);
            ++i;
        }
    }
}
