package io.github.mikip98.humilityafm.generators;

import io.github.mikip98.humilityafm.content.blocks.stairs.InnerStairs;
import io.github.mikip98.humilityafm.content.blocks.stairs.OuterStairs;
import io.github.mikip98.humilityafm.util.GenerationData;
import io.github.mikip98.humilityafm.util.data_types.BlockStrength;
import io.github.mikip98.humilityafm.util.data_types.Pair;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;

public class ForcedCornerStairsGenerator {
    public ForcedCornerStairsGenerator() {
        throw new IllegalStateException("Utility class, do not instantiate!\nUse \"init()\" and \"registerInnerOuterStairsVariants()\" instead!");
    }

    public static String[] innerOuterStairsBlockVariantsNames;
    public static Block[] innerStairsBlockVariants;
    public static Block[] outerStairsBlockVariants;

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
        outerStairsBlockVariants = new Block[stairsBlockVariantsCount];

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
            innerStairsBlockVariants[id] = new InnerStairs(variantSettings);
            outerStairsBlockVariants[id] = new OuterStairs(variantSettings);
            ++id;
        }
        return id;
    }
}
