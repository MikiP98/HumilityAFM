package io.github.mikip98.helpers;

import io.github.mikip98.content.blocks.CandleStick;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import static io.github.mikip98.HumilityAFM.MOD_ID;

public class CandlestickHelper {
    public CandlestickHelper() {
        throw new IllegalStateException("Utility class, do not instantiate!\nUse \"init()\" and \"registerCandlestickVariants()\" instead!");
    }

    private static final float CandlestickStrength = 0.5f;

    public static String[] candlestickVariantsNames;

    public static Block[] candlestickVariants;
    public static Item[] candlestickItemVariants;

    public static void init() {
        final String[] metals = {"gold", "copper", "weathered_copper", "exposed_copper", "oxidized_copper", "waxed_copper", "waxed_weathered_copper", "waxed_exposed_copper", "waxed_oxidized_copper"};

        final FabricBlockSettings CandlestickSettings = FabricBlockSettings.create().strength(CandlestickStrength).requiresTool().nonOpaque().sounds(BlockSoundGroup.COPPER);

        //Create candlestick variants
        short candlestickVariantsCount = (short) (metals.length * (MainHelper.vanillaWoolTypes.length + 2));

        candlestickVariantsNames = new String[candlestickVariantsCount];

        candlestickVariants = new Block[candlestickVariantsCount];
        candlestickItemVariants = new Item[candlestickVariantsCount];

        int i = 0;
        for (String metal : metals) {
            String candlestickVariantName = "candlestick_" + metal;
            candlestickVariantsNames[i] = candlestickVariantName;
            //LOGGER.info("Creating candlestick variant: " + candlestickVariantsNames[i]);

            candlestickVariants[i] = new CandleStick(CandlestickSettings);
            candlestickItemVariants[i] = new BlockItem(candlestickVariants[i], new Item.Settings());

            i++;

            candlestickVariantName = "candlestick_" + metal + "_candle";
            candlestickVariantsNames[i] = candlestickVariantName;
            //LOGGER.info("Creating candlestick variant: " + candlestickVariantsNames[i]);

            candlestickVariants[i] = new CandleStick(CandlestickSettings);
            candlestickItemVariants[i] = new BlockItem(candlestickVariants[i], new Item.Settings());

            i++;
            for (String color : MainHelper.vanillaWoolTypes) {
                candlestickVariantName = "candlestick_" + metal + "_candle_" + color;
                candlestickVariantsNames[i] = candlestickVariantName;
                //LOGGER.info("Creating candlestick variant: " + candlestickVariantsNames[i]);

                candlestickVariants[i] = new CandleStick(CandlestickSettings);
                candlestickItemVariants[i] = new BlockItem(candlestickVariants[i], new Item.Settings());

                i++;
            }
        }
    }

    public static void registerCandlestickVariants() {
        for (int i = 0; i < candlestickVariants.length; i++) {
            Registry.register(Registries.BLOCK, new Identifier(MOD_ID, candlestickVariantsNames[i]), candlestickVariants[i]);
            Registry.register(Registries.ITEM, new Identifier(MOD_ID, candlestickVariantsNames[i]), candlestickItemVariants[i]);
        }
    }
}
