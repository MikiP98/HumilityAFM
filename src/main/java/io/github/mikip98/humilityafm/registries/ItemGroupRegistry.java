package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.config.enums.CreativeItemGroupCategorization;
#if MC_VERSION < 260000
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
#else
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
#endif
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class ItemGroupRegistry {
    public static void registerItemGroups() {
        switch (ModConfig.creativeItemGroupCategorization) {
            case SEPARATE -> {
                createAndRegisterItemGroup(
                        "cabinets_group", "cabinets",
                        ItemRegistry.CABINET_ITEM,
                        ItemRegistry.CABINET_ITEM_VARIANTS,
                        ItemRegistry.ILLUMINATED_CABINET_ITEM_VARIANTS
                );
                createAndRegisterItemGroup(
                        "inner_outer_stairs_group", "innerOuterStairs",
                        BlockRegistry.OUTER_STAIRS_BLOCK_VARIANTS[0],
                        BlockRegistry.INNER_STAIRS_BLOCK_VARIANTS,
                        BlockRegistry.OUTER_STAIRS_BLOCK_VARIANTS
                );
                createAndRegisterItemGroup(
                        "wooden_mosaics_group", "woodenMosaics",
                        BlockRegistry.WOODEN_MOSAIC_VARIANTS[0],
                        BlockRegistry.WOODEN_MOSAIC_VARIANTS
                );
                createAndRegisterItemGroup(
                        "terracotta_tiles_group", "terracottaTiles",
                        BlockRegistry.TERRACOTTA_TILE_VARIANTS[0],
                        BlockRegistry.TERRACOTTA_TILE_VARIANTS
                );
                List<ItemLike[]> items = new ArrayList<>();
                items.add(new Block[]{BlockRegistry.JACK_O_LANTERN_REDSTONE});
                items.add(new Block[]{BlockRegistry.JACK_O_LANTERN_SOUL});
                if (ModConfig.getEnableColouredFeatureSetBeta()) {
                    items.add(ItemRegistry.GLOWING_POWDER_VARIANTS);
                    items.add(BlockRegistry.LIGHT_STRIP_VARIANTS);
                    items.add(ItemRegistry.COLOURED_TORCH_ITEM_VARIANTS);
                    items.add(BlockRegistry.COLOURED_JACK_O_LANTERNS);
                }
                if (ModConfig.getEnableCandlestickBeta()) {
                    items.add(ItemRegistry.CANDLESTICK_ITEM_VARIANTS);
                    items.addAll(Arrays.asList(ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS));
                }
                createAndRegisterItemGroup(
                        "humility_misc_group", "humilityMisc",
                        BlockRegistry.JACK_O_LANTERN_SOUL,
                        items.toArray(new ItemLike[0][])
                );
            }
            case BLOCKS_AND_ITEMS, SINGLE -> {
                List<ItemLike[]> blocks = new ArrayList<>();
                // --- Cabinets ---
                blocks.add(ItemRegistry.CABINET_ITEM_VARIANTS);
                blocks.add(ItemRegistry.ILLUMINATED_CABINET_ITEM_VARIANTS);
                // --- Forced Corner Stairs ---
                blocks.add(BlockRegistry.OUTER_STAIRS_BLOCK_VARIANTS);
                blocks.add(BlockRegistry.INNER_STAIRS_BLOCK_VARIANTS);
                // --- Wooden Mosaics ---
                blocks.add(BlockRegistry.WOODEN_MOSAIC_VARIANTS);
                // --- Terracotta Tiles ---
                blocks.add(BlockRegistry.TERRACOTTA_TILE_VARIANTS);
                // --- Misc ---
                blocks.add(new Block[]{BlockRegistry.JACK_O_LANTERN_REDSTONE});
                blocks.add(new Block[]{BlockRegistry.JACK_O_LANTERN_SOUL});
                // Coloured Feature Set Beta
                if (ModConfig.getEnableColouredFeatureSetBeta()) {
                    blocks.add(BlockRegistry.LIGHT_STRIP_VARIANTS);
                    blocks.add(ItemRegistry.COLOURED_TORCH_ITEM_VARIANTS);
                    blocks.add(BlockRegistry.COLOURED_JACK_O_LANTERNS);
                }
                // Candlestick Beta
                if (ModConfig.getEnableCandlestickBeta()) {
                    blocks.add(ItemRegistry.CANDLESTICK_ITEM_VARIANTS);
                    blocks.addAll(Arrays.asList(ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS));
                }

                if (ModConfig.creativeItemGroupCategorization == CreativeItemGroupCategorization.BLOCKS_AND_ITEMS) {
                    createAndRegisterItemGroup(
                            "humility_block_group", "humilityBlocks",
                            ItemRegistry.CABINET_ITEM,
                            blocks.toArray(new ItemLike[0][])
                    );
                    if (ModConfig.getEnableColouredFeatureSetBeta()) {
                        createAndRegisterItemGroup(
                                "humility_item_group", "humilityItems",
                                () -> ItemRegistry.GLOWING_POWDER_VARIANTS[10],
                                ItemRegistry.GLOWING_POWDER_VARIANTS
                        );
                    }
                }
                else {
                    if (ModConfig.getEnableColouredFeatureSetBeta()) {
                        blocks.add(ItemRegistry.GLOWING_POWDER_VARIANTS);
                    }
                    createAndRegisterItemGroup(
                            "humility_content_group", "humilityContent",
                            ItemRegistry.CABINET_ITEM,
                            blocks.toArray(new ItemLike[0][])
                    );
                }
            }
        }


        // -------------------------------------------------------------------------------------------------------------
        // --------- PUT BLOCKS AND ITEMS INTO VANILLA ITEM GROUPS -----------------------------------------------------
        // -------------------------------------------------------------------------------------------------------------
        if (ModConfig.placeHumilityBlocksInVanillaCreativeCategories) {
            // Cabinets
            putIntoItemGroup(ItemRegistry.CABINET_ITEM_VARIANTS, CreativeModeTabs.COLORED_BLOCKS);
            putIntoItemGroup(ItemRegistry.ILLUMINATED_CABINET_ITEM_VARIANTS, CreativeModeTabs.COLORED_BLOCKS);

            // Special Jack-O-Lanterns
            putIntoItemGroup(BlockRegistry.JACK_O_LANTERN_REDSTONE, CreativeModeTabs.COLORED_BLOCKS, CreativeModeTabs.REDSTONE_BLOCKS);
            putIntoItemGroup(BlockRegistry.JACK_O_LANTERN_SOUL, CreativeModeTabs.COLORED_BLOCKS);

            // Forced corner stairs
            putIntoItemGroup(BlockRegistry.INNER_STAIRS_BLOCK_VARIANTS, CreativeModeTabs.BUILDING_BLOCKS);
            putIntoItemGroup(BlockRegistry.OUTER_STAIRS_BLOCK_VARIANTS, CreativeModeTabs.BUILDING_BLOCKS);

            // Wooden Mosaics
            putIntoItemGroup(BlockRegistry.WOODEN_MOSAIC_VARIANTS, CreativeModeTabs.BUILDING_BLOCKS);

            // Terracotta Tiles
            putIntoItemGroup(BlockRegistry.TERRACOTTA_TILE_VARIANTS, CreativeModeTabs.BUILDING_BLOCKS);

            // --- Candlestick beta ---
            if (ModConfig.getEnableCandlestickBeta()) {
                // Simple Candlesticks
                putIntoItemGroup(ItemRegistry.CANDLESTICK_ITEM_VARIANTS, CreativeModeTabs.FUNCTIONAL_BLOCKS);
                // Rustable Candlesticks
                Arrays.stream(ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS).forEach(s -> putIntoItemGroup(s, CreativeModeTabs.FUNCTIONAL_BLOCKS));
            }

            // --- Coloured feature set beta ---
            if (ModConfig.getEnableColouredFeatureSetBeta()) {
                // Glowing powders
                putIntoItemGroup(ItemRegistry.GLOWING_POWDER_VARIANTS, CreativeModeTabs.INGREDIENTS);
                // Light Strips
                putIntoItemGroup(BlockRegistry.LIGHT_STRIP_VARIANTS, CreativeModeTabs.COLORED_BLOCKS);
                // Coloured Torches
                putIntoItemGroup(ItemRegistry.COLOURED_TORCH_ITEM_VARIANTS, CreativeModeTabs.COLORED_BLOCKS);
                // Coloured Jack o'Lanterns
                putIntoItemGroup(BlockRegistry.COLOURED_JACK_O_LANTERNS, CreativeModeTabs.COLORED_BLOCKS);
            }
        }
    }


    protected static boolean itemGroupCreationCheck(ItemLike[]... itemSets) {
        return itemSets == null || itemSets.length == 0 || Arrays.stream(itemSets).allMatch((items) -> items == null || items.length == 0);
    }
    protected static void createAndRegisterItemGroup(String name, String translationId, ItemLike displayItem, ItemLike[]... itemSets) {
        if (itemGroupCreationCheck(itemSets)) return;
        Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                getId(name),
                #if MC_VERSION < 260000 FabricItemGroup #else FabricCreativeModeTab #endif.builder()
                        .icon(() -> new ItemStack(displayItem))
                        .title(Component.translatable("itemGroup." + translationId))
                        .displayItems((ignored, output) -> Arrays.stream(itemSets).forEach(
                                (items) -> { if (items != null) Arrays.stream(items).forEach(output::accept); }
                        ))
                        .build()
        );
    }


    @SafeVarargs
    protected static void putIntoItemGroup(ItemLike[] items, ResourceKey<CreativeModeTab>... itemGroups) {
        for (ResourceKey<CreativeModeTab> itemGroup : itemGroups) {
            #if MC_VERSION < 260000
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register(
                content -> Arrays.stream(items).forEach(content::accept)
            );
            #else
            CreativeModeTabEvents.modifyOutputEvent(itemGroup).register(
                    (creativeTab) ->  Arrays.stream(items).forEach(creativeTab::accept)
            );
            #endif
        }
    }
    @SafeVarargs
    protected static void putIntoItemGroup(ItemLike item, ResourceKey<CreativeModeTab>... itemGroups) {
        putIntoItemGroup(new ItemLike[]{item}, itemGroups);
    }
}
