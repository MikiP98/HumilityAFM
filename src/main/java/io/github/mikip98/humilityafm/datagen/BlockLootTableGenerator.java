package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.registries.ItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.Arrays;

public class BlockLootTableGenerator extends FabricBlockLootTableProvider {
    public BlockLootTableGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        // Cabinet blocks
        addDrop(BlockRegistry.CABINET_BLOCK, ItemRegistry.CABINET_ITEM);  // Manual testing block
        addDrop(BlockRegistry.ILLUMINATED_CABINET_BLOCK, ItemRegistry.ILLUMINATED_CABINET_ITEM);  // Manual testing block
        addDrop(BlockRegistry.FLOOR_CABINET_BLOCK, ItemRegistry.CABINET_ITEM);  // Manual testing block
        addDrop(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK, ItemRegistry.ILLUMINATED_CABINET_ITEM);  // Manual testing block
        for (int i = 0; i < BlockRegistry.WALL_CABINET_BLOCK_VARIANTS.length; ++i) {
            Item cabinetItem = ItemRegistry.CABINET_ITEM_VARIANTS[i];
            Item illuminatedCabinetItem = ItemRegistry.ILLUMINATED_CABINET_ITEM_VARIANTS[i];

            addDrop(BlockRegistry.WALL_CABINET_BLOCK_VARIANTS[i], cabinetItem);
            addDrop(BlockRegistry.FLOOR_CABINET_BLOCK_VARIANTS[i], cabinetItem);

            addDrop(BlockRegistry.WALL_ILLUMINATED_CABINET_BLOCK_VARIANTS[i], illuminatedCabinetItem);
            addDrop(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK_VARIANTS[i], illuminatedCabinetItem);
        }
        // Wooden Mosaics
        Arrays.stream(BlockRegistry.WOODEN_MOSAIC_VARIANTS).forEach(this::addDrop);
        // Terracotta Tiles
        Arrays.stream(BlockRegistry.TERRACOTTA_TILE_VARIANTS).forEach(this::addDrop);
        // Inner & Outer Stairs
        Arrays.stream(BlockRegistry.INNER_STAIRS_BLOCK_VARIANTS).forEach(this::addDrop);
        Arrays.stream(BlockRegistry.OUTER_STAIRS_BLOCK_VARIANTS).forEach(this::addDrop);
        // Jack o'Lanterns
        addDrop(BlockRegistry.JACK_O_LANTERN_REDSTONE);
        addDrop(BlockRegistry.JACK_O_LANTERN_SOUL);


        // CANDLESTICK BETA
        // Candlesticks
        for (int i = 0; i < ItemRegistry.CANDLESTICK_ITEM_VARIANTS.length; ++i) {
            Item item = ItemRegistry.CANDLESTICK_ITEM_VARIANTS[i];
            addDrop(BlockRegistry.SIMPLE_CANDLESTICK_WALL_VARIANTS[i], item);
            addDrop(BlockRegistry.SIMPLE_CANDLESTICK_FLOOR_VARIANTS[i], item);
        }
        for (int i = 0; i < ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS.length; ++i) {
            Item[] itemSet = ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS[i];
            Block[] wallSet = BlockRegistry.RUSTABLE_CANDLESTICK_WALL_VARIANTS[i];
            Block[] standingSet = BlockRegistry.RUSTABLE_CANDLESTICK_FLOOR_VARIANTS[i];
            for (int j = 0; j < itemSet.length; ++j) {
                addDrop(wallSet[j], itemSet[j]);
                addDrop(standingSet[j], itemSet[j]);
            }
        }


        // COLOURED FEATURE SET BETA
        // Light Strips
        Arrays.stream(BlockRegistry.LIGHT_STRIP_VARIANTS).forEach(this::addDrop);
        // Coloured Torches
        Arrays.stream(BlockRegistry.COLOURED_TORCH_VARIANTS).forEach(this::addDrop);
        // Coloured Jack o'Lanterns
        Arrays.stream(BlockRegistry.COLOURED_JACK_O_LANTERNS).forEach(this::addDrop);
    }
}
