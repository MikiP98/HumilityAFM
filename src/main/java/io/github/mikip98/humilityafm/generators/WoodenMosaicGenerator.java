package io.github.mikip98.humilityafm.generators;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.util.GenerationData;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;

public class WoodenMosaicGenerator {
    public WoodenMosaicGenerator() {
        throw new IllegalStateException("Utility class, do not instantiate!\nUse \"init()\" and \"registerWoodenMosaicVariants()\" instead!");
    }

    public static String[] woodenMosaicVariantsNames;
    public static Block[] woodenMosaicVariants;

    public static void init() {
        final AbstractBlock.Settings woodenMosaicSettings = AbstractBlock.Settings.create()
                .strength(
                        GenerationData.vanillaWoodHardness * ModConfig.mosaicsAndTilesStrengthMultiplayer,
                        GenerationData.vanillaWoodResistance * ModConfig.mosaicsAndTilesStrengthMultiplayer
                )
                .sounds(BlockSoundGroup.WOOD)
                .requiresTool();

        //Create wooden mosaic variants
        String[] woodTypes = GenerationData.vanillaWoodTypes;
        short woodenMosaicVariantsCount = (short) (woodTypes.length * (woodTypes.length-1));

        woodenMosaicVariantsNames = new String[woodenMosaicVariantsCount];
        woodenMosaicVariants = new Block[woodenMosaicVariantsCount];

        short i = 0;
        for (String woodType1 : woodTypes) {
            for (String woodType2 : woodTypes) {
                if (woodType1.equals(woodType2)) continue;
                woodenMosaicVariantsNames[i] = woodType1 + "_" + woodType2;
                woodenMosaicVariants[i] = new Block(woodenMosaicSettings);
                i++;
            }
        }
    }
}
