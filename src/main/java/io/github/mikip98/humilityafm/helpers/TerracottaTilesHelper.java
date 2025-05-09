package io.github.mikip98.humilityafm.helpers;

import io.github.mikip98.humilityafm.HumilityAFM;
import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.util.GenerationData;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TerracottaTilesHelper {
    public TerracottaTilesHelper() {
        throw new IllegalStateException("Utility class, do not instantiate!\nUse \"init()\" and \"registerTerracottaTilesVariants()\" instead!");
    }

    public static final String[] terracottaTypes = GenerationData.vanillaColorPallet;

    public static String[] terracottaTilesVariantsNames;

    public static Block[] terracottaTilesVariants;
    public static Item[] terracottaTilesItemVariants;

    public static void init() {
        final FabricBlockSettings terracottaTilesSettings = FabricBlockSettings.create()
                .strength(
                        GenerationData.vanillaTerracottaHardness * ModConfig.mosaicsAndTilesStrengthMultiplayer,
                        GenerationData.vanillaTerracottaResistance * ModConfig.mosaicsAndTilesStrengthMultiplayer
                )
                .requiresTool();

        //Create terracotta tiles variants
        short terracottaTilesVariantsCount = (short) (terracottaTypes.length * (terracottaTypes.length-1));

        terracottaTilesVariantsNames = new String[terracottaTilesVariantsCount];

        terracottaTilesVariants = new Block[terracottaTilesVariantsCount];
        terracottaTilesItemVariants = new Item[terracottaTilesVariantsCount];

        short i = 0;
        for (String terracottaType1 : terracottaTypes) {
            for (String terracottaType2 : terracottaTypes) {
                if (terracottaType1.equals(terracottaType2)) {
                    continue;
                }
                String terracottaTilesVariantName = terracottaType1 + "_" + terracottaType2;
                terracottaTilesVariantsNames[i] = terracottaTilesVariantName;
                //LOGGER.info("Creating terracotta tiles variant: " + terracottaTilesVariantsNames[i]);
                // Create terracotta tiles variant
                terracottaTilesVariants[i] = new Block(terracottaTilesSettings);
                // Create terracotta tiles variant item
                terracottaTilesItemVariants[i] = new BlockItem(terracottaTilesVariants[i], new FabricItemSettings());
                i++;
            }
        }
    }

    public static void registerTerracottaTilesVariants() {
        //Register terracotta tiles variants
        short i = 0;
        for (String terracottaTilesVariantName : terracottaTilesVariantsNames) {
            //LOGGER.info("Registering terracotta tiles variant: " + terracottaTilesVariantName);
            // Register terracotta tiles variant
            Registry.register(Registries.BLOCK, new Identifier(HumilityAFM.MOD_ID, "terracotta_tiles_" + terracottaTilesVariantName), terracottaTilesVariants[i]);
            // Register terracotta tiles variant item
            Registry.register(Registries.ITEM, new Identifier(HumilityAFM.MOD_ID, "terracotta_tiles_" + terracottaTilesVariantName), terracottaTilesItemVariants[i]);
            i++;
        }
    }
}
