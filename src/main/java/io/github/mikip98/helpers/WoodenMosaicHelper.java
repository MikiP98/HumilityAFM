package io.github.mikip98.helpers;

import io.github.mikip98.HumilityAFM;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class WoodenMosaicHelper {
    public WoodenMosaicHelper() {
        throw new IllegalStateException("Utility class, do not instantiate!\nUse \"init()\" and \"registerWoodenMosaicVariants()\" instead!");
    }

    private static final float WoodenMosaicStrength = 3.0f * 1.5f;
    private static final FabricBlockSettings WoodenMosaicSettings = FabricBlockSettings.create().strength(WoodenMosaicStrength).requiresTool().sounds(BlockSoundGroup.WOOD);

    public static String[] woodenMosaicVariantsNames;

    public static Block[] woodenMosaicVariants;
    public static Item[] woodenMosaicItemVariants;

    public static void init() {
        //Create wooden mosaic variants
        String[] woodTypes = MainHelper.getWoodTypes();
        short woodenMosaicVariantsCount = (short) (woodTypes.length * (woodTypes.length-1));

        woodenMosaicVariantsNames = new String[woodenMosaicVariantsCount];

        woodenMosaicVariants = new Block[woodenMosaicVariantsCount];
        woodenMosaicItemVariants = new Item[woodenMosaicVariantsCount];

        short i = 0;
        for (String woodType1 : woodTypes) {
            for (String woodType2 : woodTypes) {
                if (woodType1.equals(woodType2)) {
                    continue;
                }
                String woodenMosaicVariantName = woodType1 + "_" + woodType2;
                woodenMosaicVariantsNames[i] = woodenMosaicVariantName;
                //LOGGER.info("Creating wooden mosaic variant: " + woodenMosaicVariantsNames[i]);
                // Create wooden mosaic variant
                woodenMosaicVariants[i] = new Block(WoodenMosaicSettings);
                // Create wooden mosaic variant item
                woodenMosaicItemVariants[i] = new BlockItem(woodenMosaicVariants[i], new FabricItemSettings());
                i++;
            }
        }
    }

    public static void registerWoodenMosaicVariants() {
        //Register wooden mosaic variants
        short i = 0;
        for (String woodenMosaicVariantName : woodenMosaicVariantsNames) {
            //LOGGER.info("Registering wooden mosaic variant: " + woodenMosaicVariantName);
            // Register wooden mosaic variant
            Registry.register(Registries.BLOCK, new Identifier(HumilityAFM.MOD_ID, "wooden_mosaic_" + woodenMosaicVariantName), woodenMosaicVariants[i]);
            // Register wooden mosaic variant item
            Registry.register(Registries.ITEM, new Identifier(HumilityAFM.MOD_ID, "wooden_mosaic_" + woodenMosaicVariantName), woodenMosaicItemVariants[i]);
            ++i;
        }

        // Make wooden mosaic variants flammable
        for (Block woodenMosaicVariant : woodenMosaicVariants) {
            byte burn = (byte) Math.round(5/1.5);
            byte spread = (byte) Math.round(20/1.5);
            FlammableBlockRegistry.getDefaultInstance().add(woodenMosaicVariant, burn, spread);
        }
    }
}
