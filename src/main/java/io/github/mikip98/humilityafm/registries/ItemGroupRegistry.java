package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import io.github.mikip98.humilityafm.helpers.*;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class ItemGroupRegistry {
    // Cabinet item group
    final static ItemGroup CABINETS_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(BlockRegistry.CABINET_BLOCK))
            .displayName(Text.translatable("itemGroup.cabinets"))
            .entries((displayContext, entries) -> {
                for (int i = 0; i < CabinetBlockGenerator.cabinetBlockVariants.length; i++) {
                    entries.add(new ItemStack(CabinetBlockGenerator.cabinetBlockVariants[i]));
                }
                for (int i = 0; i < CabinetBlockGenerator.illuminatedCabinetBlockVariants.length; i++) {
                    entries.add(new ItemStack(CabinetBlockGenerator.illuminatedCabinetBlockVariants[i]));
                }
            })
            .build();

    // InnerOuterStairs item group
    final static ItemGroup INNER_OUTER_STAIRS_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(BlockRegistry.OUTER_STAIRS))
            .displayName(Text.translatable("itemGroup.innerOuterStairs"))
            .entries((displayContext, entries) -> {
                for (int i = 0; i < InnerOuterStairsHelper.innerStairsBlockVariants.length; i++) {
                    entries.add(new ItemStack(InnerOuterStairsHelper.innerStairsBlockVariants[i]));
                }
                for (int i = 0; i < InnerOuterStairsHelper.outerStairsBlockVariants.length; i++) {
                    entries.add(new ItemStack(InnerOuterStairsHelper.outerStairsBlockVariants[i]));
                }
            })
            .build();

    // WoodenMosaic item group
    final static ItemGroup WOODEN_MOSAIC_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(WoodenMosaicHelper.woodenMosaicVariants[0]))
            .displayName(Text.translatable("itemGroup.woodenMosaics"))
            .entries((displayContext, entries) -> {
                for (int i = 0; i < WoodenMosaicHelper.woodenMosaicVariants.length; i++) {
//						LOGGER.info("Adding wooden mosaic variant to item group: " + WoodenMosaicHelper.woodenMosaicVariants[i]);
                    entries.add(new ItemStack(WoodenMosaicHelper.woodenMosaicVariants[i])); // TODO: Fix this
                }
            })
            .build();

    // TerracottaTiles item group
    final static ItemGroup TERRACOTTA_TILES_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(TerracottaTilesHelper.terracottaTilesVariants[0]))
            .displayName(Text.translatable("itemGroup.terracottaTiles"))
            .entries((displayContext, entries) -> {
                for (int i = 0; i < TerracottaTilesHelper.terracottaTilesVariants.length; i++) {
//						LOGGER.info("Adding terracotta tiles variant to item group: " + TerracottaTilesHelper.terracottaTilesVariants[i]);
                    entries.add(new ItemStack(TerracottaTilesHelper.terracottaTilesVariants[i]));
                }
            })
            .build();

    // Miscellaneous (Humility Misc) item group
    final static ItemGroup HUMILITY_MISCELLANEOUS_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(PumpkinHelper.PumpkinsVariants[0]))
            .displayName(Text.translatable("itemGroup.humilityMisc"))
            .entries((displayContext, entries) -> {
                for (int i = 0; i < PumpkinHelper.PumpkinsVariants.length; i++) {
                    entries.add(new ItemStack(PumpkinHelper.PumpkinsVariants[i]));
                }
            })
            .build();


    final static ItemGroup LED_ITEM_GROUP;
    final static ItemGroup CANDLESTICK_ITEM_GROUP;
    static {
        // LED item group
        if (ModConfig.enableLEDs) {
            LED_ITEM_GROUP = FabricItemGroup.builder()
                    .icon(() -> new ItemStack(LEDHelper.LEDBlockVariants[0]))
                    .displayName(Text.translatable("itemGroup.leds"))
                    .entries((displayContext, entries) -> {
                        for (int i = 0; i < LEDHelper.LEDBlockVariants.length; i++) {
                            entries.add(new ItemStack(LEDHelper.LEDBlockVariants[i]));
                        }
                        for (int i = 0; i < LEDHelper.LEDPowderVariants.length; i++) {
                            entries.add(new ItemStack(LEDHelper.LEDPowderVariants[i]));
                        }
                    })
                    .build();
        } else {
            LED_ITEM_GROUP = null; // Initialize to null if LEDs are disabled
        }

        // Candlestick item group
        if (ModConfig.enableCandlesticks) {
            CANDLESTICK_ITEM_GROUP = FabricItemGroup.builder()
                .icon(() -> new ItemStack(CandlestickHelper.candlestickVariants[0]))
                .displayName(Text.translatable("itemGroup.candlesticks"))
                .entries((displayContext, entries) -> {
                    for (int i = 0; i < CandlestickHelper.candlestickVariants.length; i++) {
                        entries.add(new ItemStack(CandlestickHelper.candlestickVariants[i]));
                    }
                })
                .build();
        } else {
            CANDLESTICK_ITEM_GROUP = null; // Initialize to null if Candlesticks are disabled
        }
    }


    public static void registerItemGroups() {
        Registry.register(Registries.ITEM_GROUP, getId("cabinets_group"), CABINETS_ITEM_GROUP);
        Registry.register(Registries.ITEM_GROUP, getId("inner_outer_stairs_group"), INNER_OUTER_STAIRS_ITEM_GROUP);
        Registry.register(Registries.ITEM_GROUP, getId("wooden_mosaics_group"), WOODEN_MOSAIC_ITEM_GROUP);
        Registry.register(Registries.ITEM_GROUP, getId("terracotta_tiles_group"), TERRACOTTA_TILES_ITEM_GROUP);
        if (ModConfig.enableLEDs) {
            Registry.register(Registries.ITEM_GROUP, getId("leds_group"), LED_ITEM_GROUP);
        }
        if (ModConfig.enableCandlesticks) {
            Registry.register(Registries.ITEM_GROUP, getId("candlesticks_group"), CANDLESTICK_ITEM_GROUP);
        }
        Registry.register(Registries.ITEM_GROUP, getId("humility_misc_group"), HUMILITY_MISCELLANEOUS_GROUP);
    }
}
