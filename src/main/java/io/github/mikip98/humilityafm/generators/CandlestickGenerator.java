package io.github.mikip98.humilityafm.generators;

import io.github.mikip98.humilityafm.content.blocks.candlestick.Candlestick;
import io.github.mikip98.humilityafm.content.blocks.candlestick.RustableCandlestick;
import io.github.mikip98.humilityafm.util.GenerationData;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CandlestickGenerator {
    public CandlestickGenerator() {
        throw new IllegalStateException("Utility class, do not instantiate!\nUse \"init()\" and \"registerCandlestickVariants()\" instead!");
    }

    public static Block[] candlestickClassicVariants;
    public static Item[] candlestickClassicItemVariants;

    public static List<RustableCandlestick[]> candlestickRustableVariants;
    public static List<Item[]> candlestickRustableItemVariants;

    public static void init() {
        candlestickClassicVariants = Arrays.stream(GenerationData.vanillaCandlestickMetals).map(s -> new Candlestick()).toArray(Block[]::new);
        candlestickRustableVariants = new ArrayList<>();

        for (String[] rustableMetals : GenerationData.vanillaRustableCandlestickMetals) {
            RustableCandlestick[] candlestickMetalSet = Arrays.stream(rustableMetals).map(s -> new RustableCandlestick()).toArray(RustableCandlestick[]::new);
            if (rustableMetals.length >= 2) {
                candlestickMetalSet[0].rustNextLevel = candlestickMetalSet[1].getDefaultState();
                for (int i = 1; i < rustableMetals.length - 1; i++) {
                    candlestickMetalSet[i].rustPreviousLevel = candlestickMetalSet[i - 1].getDefaultState();
                    candlestickMetalSet[i].rustNextLevel = candlestickMetalSet[i + 1].getDefaultState();
                }
                candlestickMetalSet[candlestickMetalSet.length - 1].rustPreviousLevel = candlestickMetalSet[candlestickMetalSet.length - 2].getDefaultState();
            }
            candlestickRustableVariants.add(candlestickMetalSet);
        }
    }
}
