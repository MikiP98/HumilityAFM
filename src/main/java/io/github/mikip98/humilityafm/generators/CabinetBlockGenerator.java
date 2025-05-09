package io.github.mikip98.humilityafm.generators;

import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.IlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.util.GenerationData;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public class CabinetBlockGenerator {
    public CabinetBlockGenerator() {
        throw new IllegalStateException("Utility class, do not instantiate!\nUse \"init()\" and \"registerCabinetBlockVariants()\" instead!");
    }

    public static Map<String, Short> woodTypeToStartIndex;
    public static String[] cabinetBlockVariantsNames;

    // Vanilla
    public static Block[] cabinetBlockVariants;
    public static Item[] cabinetBlockItemVariants;

    public static Block[] illuminatedCabinetBlockVariants;
    public static Item[] illuminatedCabinetBlockItemVariants;

    // Modded
    public static Block[] moddedCabinetBlockVariants;
    public static Item[] moddedCabinetBlockItemVariants;

    public static Block[] moddedIlluminatedCabinetBlockVariants;
    public static Item[] moddedIlluminatedCabinetBlockItemVariants;


    private static void createCabinetBlockVariant(
            String woodType, String woolType,
            short i
    ) {
        String cabinetBlockVariantName = woodType + "_" + woolType;
        cabinetBlockVariantsNames[i] = cabinetBlockVariantName;
        cabinetBlockVariants[i] = new CabinetBlock();
        illuminatedCabinetBlockVariants[i] = new IlluminatedCabinetBlock();
    }


    private static void putVariantInModded(short i, short j) {
        moddedCabinetBlockVariants[j] = cabinetBlockVariants[i];
        moddedCabinetBlockItemVariants[j] = cabinetBlockItemVariants[i];
        moddedIlluminatedCabinetBlockVariants[j] = illuminatedCabinetBlockVariants[i];
        moddedIlluminatedCabinetBlockItemVariants[j] = illuminatedCabinetBlockItemVariants[i];
    }


    public static void init() {
        // TODO: change the map color parameter based on the current wood type

        //Create cabinet variants
        short cabinetBlockVariantsCount = (short) (GenerationData.vanillaWoodTypes.length * GenerationData.vanillaColorPallet.length);
//        if (ModConfig.betterNetherDetected) {
//            cabinetBlockVariantsCount += (short) (GenerationData.betterNetherWoodTypes.length * GenerationData.vanillaWoolTypes.length);
//        }

        woodTypeToStartIndex = new HashMap<>();
        cabinetBlockVariantsNames = new String[cabinetBlockVariantsCount];
        cabinetBlockVariants = new Block[cabinetBlockVariantsCount];
        illuminatedCabinetBlockVariants = new Block[cabinetBlockVariantsCount];

        short i = 0;
        for (String woodType : GenerationData.vanillaWoodTypes) {
            woodTypeToStartIndex.put(woodType, i);
            for (String woolColor : GenerationData.vanillaColorPallet) {
                createCabinetBlockVariant(woodType, woolColor, i);
                ++i;
            }
        }
//        if (ModConfig.betterNetherDetected) {
//            short moddedCabinetCount = (short) (MainHelper.betterNetherWoodTypes.length * MainHelper.vanillaWoolTypes.length);
//
//            moddedCabinetBlockVariants = new Block[moddedCabinetCount];
//            moddedCabinetBlockItemVariants = new Item[moddedCabinetCount];
//            moddedIlluminatedCabinetBlockVariants = new Block[moddedCabinetCount];
//            moddedIlluminatedCabinetBlockItemVariants = new Item[moddedCabinetCount];
//
//            short j = 0;
//            for (String woodType : MainHelper.betterNetherWoodTypes) {
//                for (String woolType : MainHelper.vanillaWoolTypes) {
//                    createCabinetBlockVariant(woodType, woolType, i, CabinetBlockSettings, IlluminatedCabinetBlockSettings);
//                    putVariantInModded(i, j);
//
//                    ++i;
//                    ++j;
//                }
//            }
//        }
    }
}
