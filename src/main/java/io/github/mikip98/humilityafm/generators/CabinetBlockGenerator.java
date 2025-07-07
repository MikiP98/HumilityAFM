package io.github.mikip98.humilityafm.generators;

import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorIlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.IlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.content.items.DoubleVerticallyAttachableBlockItem;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.registries.ItemRegistry;
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
    public static Block[] illuminatedCabinetBlockVariants;
    public static Block[] floorCabinetBlockVariants;
    public static Block[] floorIlluminatedCabinetBlockVariants;

//    // Modded
//    public static Block[] moddedCabinetBlockVariants;
//    public static Block[] moddedIlluminatedCabinetBlockVariants;


    private static void createCabinetBlockVariant(
            String woodType, String woolType,
            short i
    ) {
        String cabinetBlockVariantName = woodType + "_" + woolType;

        cabinetBlockVariantsNames[i] = cabinetBlockVariantName;

        cabinetBlockVariants[i] = BlockRegistry.register("wall_cabinet_" + cabinetBlockVariantName, CabinetBlock::new, CabinetBlock.defaultSettings);
        illuminatedCabinetBlockVariants[i] = BlockRegistry.register("wall_illuminated_cabinet_block_" + cabinetBlockVariantName, IlluminatedCabinetBlock::new, IlluminatedCabinetBlock.defaultSettings);

        floorCabinetBlockVariants[i] = BlockRegistry.register("cabinet_" + cabinetBlockVariantName, FloorCabinetBlock::new, FloorCabinetBlock.defaultSettings);
        floorIlluminatedCabinetBlockVariants[i] = BlockRegistry.register("illuminated_cabinet_block_" + cabinetBlockVariantName, FloorIlluminatedCabinetBlock::new, FloorIlluminatedCabinetBlock.defaultSettings);

        ItemRegistry.CABINET_ITEM_VARIANTS[i] = ItemRegistry.register(
                "cabinet_" + cabinetBlockVariantName,
                (settings) -> new DoubleVerticallyAttachableBlockItem(floorCabinetBlockVariants[i], cabinetBlockVariants[i], settings),
                new Item.Settings()
        );
        ItemRegistry.ILLUMINATED_CABINET_ITEM_VARIANTS[i] = ItemRegistry.register(
                "illuminated_cabinet_" + cabinetBlockVariantName,
                (settings) -> new DoubleVerticallyAttachableBlockItem(floorIlluminatedCabinetBlockVariants[i], illuminatedCabinetBlockVariants[i], settings),
                new Item.Settings()
        );
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
        floorCabinetBlockVariants = new FloorCabinetBlock[cabinetBlockVariantsCount];
        floorIlluminatedCabinetBlockVariants = new FloorCabinetBlock[cabinetBlockVariantsCount];
        ItemRegistry.CABINET_ITEM_VARIANTS = new Item[cabinetBlockVariantsCount];
        ItemRegistry.ILLUMINATED_CABINET_ITEM_VARIANTS = new Item[cabinetBlockVariantsCount];

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
