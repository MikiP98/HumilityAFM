package io.github.mikip98.humilityafm.generators;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.IlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.helpers.MainHelper;
import io.github.mikip98.humilityafm.util.GenerationData;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
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
            short i,
            FabricBlockSettings CabinetBlockSettings, FabricBlockSettings IlluminatedCabinetBlockSettings
    ) {
        String cabinetBlockVariantName = woodType + "_" + woolType;
        cabinetBlockVariantsNames[i] = cabinetBlockVariantName;
        cabinetBlockVariants[i] = new CabinetBlock(CabinetBlockSettings);
        illuminatedCabinetBlockVariants[i] = new IlluminatedCabinetBlock(IlluminatedCabinetBlockSettings);
    }


    private static void putVariantInModded(short i, short j) {
        moddedCabinetBlockVariants[j] = cabinetBlockVariants[i];
        moddedCabinetBlockItemVariants[j] = cabinetBlockItemVariants[i];
        moddedIlluminatedCabinetBlockVariants[j] = illuminatedCabinetBlockVariants[i];
        moddedIlluminatedCabinetBlockItemVariants[j] = illuminatedCabinetBlockItemVariants[i];
    }


    public static void init() {
        final FabricBlockSettings CabinetBlockSettings = CabinetBlock.defaultSettings;
        final FabricBlockSettings IlluminatedCabinetBlockSettings = FabricBlockSettings.copyOf(CabinetBlock.defaultSettings).luminance(2);
        // TODO: change the map color parameter based on the current wood type

        //Create cabinet variants
        short cabinetBlockVariantsCount = (short) (MainHelper.vanillaWoodTypes.length * MainHelper.vanillaWoolTypes.length);
        if (ModConfig.betterNetherDetected) {
            cabinetBlockVariantsCount += (short) (MainHelper.betterNetherWoodTypes.length * MainHelper.vanillaWoolTypes.length);
        }

        woodTypeToStartIndex = new HashMap<>();
        cabinetBlockVariantsNames = new String[cabinetBlockVariantsCount];
        cabinetBlockVariants = new Block[cabinetBlockVariantsCount];
        illuminatedCabinetBlockVariants = new Block[cabinetBlockVariantsCount];

        short i = 0;
        for (String woodType : GenerationData.vanillaWoodTypes) {
            woodTypeToStartIndex.put(woodType, i);
            for (String woolColor : GenerationData.vanillaColorPallet) {
                createCabinetBlockVariant(woodType, woolColor, i, CabinetBlockSettings, IlluminatedCabinetBlockSettings);
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
