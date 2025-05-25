package io.github.mikip98.humilityafm.generators;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.content.blocks.candlestick.Candlestick;
import io.github.mikip98.humilityafm.content.blocks.candlestick.FloorCandlestick;
import io.github.mikip98.humilityafm.content.blocks.candlestick.FloorRustableCandlestick;
import io.github.mikip98.humilityafm.content.blocks.candlestick.RustableCandlestick;
import io.github.mikip98.humilityafm.content.items.ModVerticallyAttachableBlockItem;
import io.github.mikip98.humilityafm.registries.ItemRegistry;
import io.github.mikip98.humilityafm.util.GenerationData;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class CandlestickGenerator {
    public CandlestickGenerator() {
        throw new IllegalStateException("Utility class, do not instantiate!\nUse \"init()\" and \"registerCandlestickVariants()\" instead!");
    }

    public static Block[] candlestickClassicWallVariants;
    public static Block[] candlestickClassicStandingVariants;
    public static List<RustableCandlestick[]> candlestickRustableWallVariants;
    public static List<RustableCandlestick[]> candlestickRustableStandingVariants;

    public static void init() {
        if (!ModConfig.enableCandlestickBeta) return;

        candlestickClassicWallVariants = new Block[GenerationData.vanillaCandlestickMetals.length];
        candlestickClassicStandingVariants = new Block[GenerationData.vanillaCandlestickMetals.length];
        candlestickRustableWallVariants = new ArrayList<>();
        candlestickRustableStandingVariants = new ArrayList<>();

        for (int i = 0; i < GenerationData.vanillaCandlestickMetals.length; ++i) {
            candlestickClassicWallVariants[i] = new Candlestick();
            candlestickClassicStandingVariants[i] = new FloorCandlestick();
            ItemRegistry.CANDLESTICK_ITEM_VARIANTS[i] = ItemRegistry.register(
                    "candlestick_" + GenerationData.vanillaCandlestickMetals[i],
                    new ModVerticallyAttachableBlockItem(
                            candlestickClassicStandingVariants[i],
                            candlestickClassicWallVariants[i],
                            new FabricItemSettings(),
                            Direction.DOWN
                    )
            );
        }

        for (String[] rustableMetals : GenerationData.vanillaRustableCandlestickMetals) {
            Item[] candlestickMetalSetItems = new Item[rustableMetals.length];
            RustableCandlestick[] candlestickWallMetalSet = new RustableCandlestick[rustableMetals.length];
            FloorRustableCandlestick[] candlestickStandingMetalSet = new FloorRustableCandlestick[rustableMetals.length];

            for (int i = 0; i < rustableMetals.length; ++i) {
                candlestickWallMetalSet[i] = new RustableCandlestick();
                candlestickStandingMetalSet[i] = new FloorRustableCandlestick();
                candlestickMetalSetItems[i] = ItemRegistry.register(
                        "candlestick_" + rustableMetals[i],
                        new ModVerticallyAttachableBlockItem(
                                candlestickStandingMetalSet[i],
                                candlestickWallMetalSet[i],
                                new FabricItemSettings(),
                                Direction.DOWN
                        )
                );
            }

            fillRustStages(candlestickWallMetalSet, rustableMetals.length);
            fillRustStages(candlestickStandingMetalSet, rustableMetals.length);

            candlestickRustableWallVariants.add(candlestickWallMetalSet);
            candlestickRustableStandingVariants.add(candlestickStandingMetalSet);
            ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS.add(candlestickMetalSetItems);
        }
    }

    protected static void fillRustStages(RustableCandlestick[] candlestickMetalSet, int length) {
        if (length >= 2) {
            candlestickMetalSet[0].rustNextLevel = candlestickMetalSet[1].getDefaultState();
            for (int i = 1; i < length - 1; i++) {
                candlestickMetalSet[i].rustPreviousLevel = candlestickMetalSet[i - 1].getDefaultState();
                candlestickMetalSet[i].rustNextLevel = candlestickMetalSet[i + 1].getDefaultState();
            }
            candlestickMetalSet[candlestickMetalSet.length - 1].rustPreviousLevel = candlestickMetalSet[candlestickMetalSet.length - 2].getDefaultState();
        }
    }
}
