package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.config.enums.CreativeItemGroupCategorization;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

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
                List<ItemConvertible[]> items = new ArrayList<>();
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
                        items.toArray(new ItemConvertible[0][])
                );
            }
            case BLOCKS_AND_ITEMS, SINGLE -> {
                List<ItemConvertible[]> blocks = new ArrayList<>();
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
                blocks.add(BlockRegistry.LIGHT_STRIP_VARIANTS);
                blocks.add(ItemRegistry.COLOURED_TORCH_ITEM_VARIANTS);
                blocks.add(BlockRegistry.COLOURED_JACK_O_LANTERNS);
                // Candlestick Beta
                blocks.add(ItemRegistry.CANDLESTICK_ITEM_VARIANTS);
                blocks.addAll(Arrays.asList(ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS));

                if (ModConfig.creativeItemGroupCategorization == CreativeItemGroupCategorization.BLOCKS_AND_ITEMS) {
                    createAndRegisterItemGroup(
                            "humility_block_group", "humilityBlocks",
                            ItemRegistry.CABINET_ITEM,
                            blocks.toArray(new ItemConvertible[0][])
                    );
                    createAndRegisterItemGroup(
                            "humility_item_group", "humilityItems",
                            () -> ItemRegistry.GLOWING_POWDER_VARIANTS[10],
                            ItemRegistry.GLOWING_POWDER_VARIANTS
                    );
                }
                else {
                    blocks.add(ItemRegistry.GLOWING_POWDER_VARIANTS);
                    createAndRegisterItemGroup(
                            "humility_content_group", "humilityContent",
                            ItemRegistry.CABINET_ITEM,
                            blocks.toArray(new ItemConvertible[0][])
                    );
                }
            }
        }


        // -------------------------------------------------------------------------------------------------------------
        // --------- PUT BLOCKS AND ITEMS INTO VANILLA ITEM GROUPS -----------------------------------------------------
        // -------------------------------------------------------------------------------------------------------------
        if (ModConfig.placeHumilityBlocksInVanillaCreativeCategories) {
            // Cabinets
            putIntoItemGroup(ItemRegistry.CABINET_ITEM_VARIANTS, ItemGroups.COLORED_BLOCKS);
            putIntoItemGroup(ItemRegistry.ILLUMINATED_CABINET_ITEM_VARIANTS, ItemGroups.COLORED_BLOCKS);

            // Special Jack-O-Lanterns
            putIntoItemGroup(BlockRegistry.JACK_O_LANTERN_REDSTONE, ItemGroups.COLORED_BLOCKS, ItemGroups.REDSTONE);
            putIntoItemGroup(BlockRegistry.JACK_O_LANTERN_SOUL, ItemGroups.COLORED_BLOCKS);

            // Forced corner stairs
            putIntoItemGroup(BlockRegistry.INNER_STAIRS_BLOCK_VARIANTS, ItemGroups.BUILDING_BLOCKS);
            putIntoItemGroup(BlockRegistry.OUTER_STAIRS_BLOCK_VARIANTS, ItemGroups.BUILDING_BLOCKS);

            // Wooden Mosaics
            putIntoItemGroup(BlockRegistry.WOODEN_MOSAIC_VARIANTS, ItemGroups.BUILDING_BLOCKS);

            // Terracotta Tiles
            putIntoItemGroup(BlockRegistry.TERRACOTTA_TILE_VARIANTS, ItemGroups.BUILDING_BLOCKS);

            // --- Candlestick beta ---
            if (ModConfig.getEnableCandlestickBeta()) {
                // Simple Candlesticks
                putIntoItemGroup(ItemRegistry.CANDLESTICK_ITEM_VARIANTS, ItemGroups.FUNCTIONAL);
                // Rustable Candlesticks
                Arrays.stream(ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS).forEach(s -> putIntoItemGroup(s, ItemGroups.FUNCTIONAL));
            }

            // --- Coloured feature set beta ---
            if (ModConfig.getEnableColouredFeatureSetBeta()) {
                // Glowing powders
                putIntoItemGroup(ItemRegistry.GLOWING_POWDER_VARIANTS, ItemGroups.INGREDIENTS);
                // Light Strips
                putIntoItemGroup(BlockRegistry.LIGHT_STRIP_VARIANTS, ItemGroups.COLORED_BLOCKS);
                // Coloured Torches
                putIntoItemGroup(ItemRegistry.COLOURED_TORCH_ITEM_VARIANTS, ItemGroups.COLORED_BLOCKS);
                // Coloured Jack o'Lanterns
                putIntoItemGroup(BlockRegistry.COLOURED_JACK_O_LANTERNS, ItemGroups.COLORED_BLOCKS);
            }
        }
    }


    protected static boolean itemGroupCreationCheck(ItemConvertible[]... itemSets) {
        return itemSets == null || itemSets.length == 0 || Arrays.stream(itemSets).allMatch((items) -> items == null || items.length == 0);
    }
    protected static void createAndRegisterItemGroup(String name, String translationId, Supplier<ItemConvertible> displayItemSupplier, ItemConvertible[]... itemSets) {
        if (itemGroupCreationCheck(itemSets)) return;
        createAndRegisterItemGroup(name, translationId, displayItemSupplier.get(), itemSets);
    }
    protected static void createAndRegisterItemGroup(String name, String translationId, ItemConvertible displayItem, ItemConvertible[]... itemSets) {
        if (itemGroupCreationCheck(itemSets)) return;
        Registry.register(
                Registries.ITEM_GROUP,
                getId(name),
                FabricItemGroup.builder()
                    .icon(() -> new ItemStack(displayItem))
                    .displayName(Text.translatable("itemGroup." + translationId))
                    .entries((displayContext, entries) -> Arrays.stream(itemSets).forEach(
                            (items) -> { if (items != null) Arrays.stream(items).forEach(entries::add); }
                    ))
                    .build()
        );
    }


    @SafeVarargs
    protected static void putIntoItemGroup(ItemConvertible[] items, RegistryKey<ItemGroup>... itemGroups) {
        for (RegistryKey<ItemGroup> itemGroup : itemGroups)
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> Arrays.stream(items).forEach(content::add));
    }
    @SafeVarargs
    protected static void putIntoItemGroup(ItemConvertible item, RegistryKey<ItemGroup>... itemGroups) {
        for (RegistryKey<ItemGroup> itemGroup : itemGroups)
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> content.add(item));
    }
}
