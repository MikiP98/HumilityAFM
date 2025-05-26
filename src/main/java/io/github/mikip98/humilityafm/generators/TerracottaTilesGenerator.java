package io.github.mikip98.humilityafm.generators;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.util.GenerationData;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;

public class TerracottaTilesGenerator {
    public TerracottaTilesGenerator() {
        throw new IllegalStateException("Utility class, do not instantiate!\nUse \"init()\" and \"registerTerracottaTilesVariants()\" instead!");
    }

    public static final String[] terracottaTypes = GenerationData.vanillaColorPallet;

    public static String[] terracottaTilesVariantsNames;
    public static Block[] terracottaTilesVariants;

    public static void init() {
        final AbstractBlock.Settings terracottaTilesSettings = AbstractBlock.Settings.create()
                .strength(
                        GenerationData.vanillaTerracottaHardness * ModConfig.mosaicsAndTilesStrengthMultiplayer,
                        GenerationData.vanillaTerracottaResistance * ModConfig.mosaicsAndTilesStrengthMultiplayer
                )
                .requiresTool();

        //Create terracotta tiles variants
        short terracottaTilesVariantsCount = (short) (terracottaTypes.length * (terracottaTypes.length-1));

        terracottaTilesVariantsNames = new String[terracottaTilesVariantsCount];
        terracottaTilesVariants = new Block[terracottaTilesVariantsCount];

        short i = 0;
        for (String terracottaType1 : terracottaTypes) {
            for (String terracottaType2 : terracottaTypes) {
                if (terracottaType1.equals(terracottaType2)) continue;
                terracottaTilesVariantsNames[i] = terracottaType1 + "_" + terracottaType2;
                terracottaTilesVariants[i] = new Block(terracottaTilesSettings);
                i++;
            }
        }
    }
}
