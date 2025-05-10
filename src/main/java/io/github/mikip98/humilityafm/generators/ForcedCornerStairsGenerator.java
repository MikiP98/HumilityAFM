package io.github.mikip98.humilityafm.generators;

import io.github.mikip98.humilityafm.content.blocks.stairs.InnerStairs;
import io.github.mikip98.humilityafm.content.blocks.stairs.OuterStairs;
import io.github.mikip98.humilityafm.util.GenerationData;
import io.github.mikip98.humilityafm.util.data_types.BlockStrength;
import io.github.mikip98.humilityafm.util.data_types.Pair;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import static io.github.mikip98.humilityafm.HumilityAFM.MOD_ID;

public class ForcedCornerStairsGenerator {
    public ForcedCornerStairsGenerator() {
        throw new IllegalStateException("Utility class, do not instantiate!\nUse \"init()\" and \"registerInnerOuterStairsVariants()\" instead!");
    }

    public static String[] innerOuterStairsBlockVariantsNames;

    public static Block[] innerStairsBlockVariants;
    public static Item[] innerStairsBlockItemVariants;

    public static Block[] outerStairsBlockVariants;
    public static Item[] outerStairsBlockItemVariants;

    public static void init() {
        //Create stairs variants
        int stairsBlockVariantsCount = GenerationData.vanillaWoodTypes.length;
        for (Pair<BlockStrength, String[]> entry : GenerationData.vanillaStonyMaterialsPerStrength) {
            stairsBlockVariantsCount += entry.second().length;
        }
//        if (ModConfig.betterNetherDetected) {
//            stairsBlockVariantsCount += (short) betterNetherWoodTypes.length;
//        }

        innerOuterStairsBlockVariantsNames = new String[stairsBlockVariantsCount];

        innerStairsBlockVariants = new Block[stairsBlockVariantsCount];
        innerStairsBlockItemVariants = new Item[stairsBlockVariantsCount];

        outerStairsBlockVariants = new Block[stairsBlockVariantsCount];
        outerStairsBlockItemVariants = new Item[stairsBlockVariantsCount];

        int i = 0;
        // Generate wood stairs block variants
        FabricBlockSettings WoodStairsBlockSettings = FabricBlockSettings.create()
                .strength(GenerationData.vanillaWoodHardness, GenerationData.vanillaWoodResistance)
                .requiresTool()
                .sounds(BlockSoundGroup.WOOD);
        i = createStairsBlockVariants(GenerationData.vanillaWoodTypes, WoodStairsBlockSettings, i);

        // Generate stony stairs block variants
        for (Pair<BlockStrength, String[]> entry : GenerationData.vanillaStonyMaterialsPerStrength) {
            BlockStrength blockStrength = entry.first();
            FabricBlockSettings StonyStairsBlockSettings = FabricBlockSettings.create()
                    .strength(blockStrength.hardness(), blockStrength.resistance())
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE);  // TODO: Consider adding a custom sound group for stony materials
            i = createStairsBlockVariants(entry.second(), StonyStairsBlockSettings, i);
        }
    }


    protected static int createStairsBlockVariants(String[] stairsVariants, FabricBlockSettings variantSettings, int id) {
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
