package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import io.github.mikip98.humilityafm.generators.CandlestickGenerator;
import io.github.mikip98.humilityafm.generators.ColouredLightsGenerator;
import io.github.mikip98.humilityafm.generators.ForcedCornerStairsGenerator;
import io.github.mikip98.humilityafm.generators.TerracottaTilesGenerator;
import io.github.mikip98.humilityafm.generators.WoodenMosaicGenerator;
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
        for (int i = 0; i < CabinetBlockGenerator.cabinetBlockVariants.length; ++i) {
            Item cabinetItem = ItemRegistry.CABINET_ITEM_VARIANTS[i];
            Item illuminatedCabinetItem = ItemRegistry.ILLUMINATED_CABINET_ITEM_VARIANTS[i];

            addDrop(CabinetBlockGenerator.cabinetBlockVariants[i], cabinetItem);
            addDrop(CabinetBlockGenerator.floorCabinetBlockVariants[i], cabinetItem);

            addDrop(CabinetBlockGenerator.illuminatedCabinetBlockVariants[i], illuminatedCabinetItem);
            addDrop(CabinetBlockGenerator.floorIlluminatedCabinetBlockVariants[i], illuminatedCabinetItem);
        }

        // Wooden Mosaics
        addDrop(BlockRegistry.WOODEN_MOSAIC);  // Manual testing block
        for (Block block : WoodenMosaicGenerator.woodenMosaicVariants) {
            addDrop(block);
        }

        // Terracotta Tiles
        for (Block block : TerracottaTilesGenerator.terracottaTilesVariants) {
            addDrop(block);
        }

        // Inner & Outer Stairs
        addDrop(BlockRegistry.INNER_STAIRS);  // Manual testing block
        addDrop(BlockRegistry.OUTER_STAIRS);  // Manual testing block
        for (Block block : ForcedCornerStairsGenerator.innerStairsBlockVariants) {
            addDrop(block);
        }
        for (Block block : ForcedCornerStairsGenerator.outerStairsBlockVariants) {
            addDrop(block);
        }

        // Jack o'Lanterns
        addDrop(BlockRegistry.JACK_O_LANTERN_REDSTONE);
        addDrop(BlockRegistry.JACK_O_LANTERN_SOUL);
        Arrays.stream(BlockRegistry.COLOURED_JACK_O_LANTERNS).forEach(this::addDrop);

        // Candlesticks
        for (int i = 0; i < ItemRegistry.CANDLESTICK_ITEM_VARIANTS.length; ++i) {
            Item item = ItemRegistry.CANDLESTICK_ITEM_VARIANTS[i];
            addDrop(CandlestickGenerator.candlestickClassicWallVariants[i], item);
            addDrop(CandlestickGenerator.candlestickClassicStandingVariants[i], item);
        }
        for (int i = 0; i < ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS.size(); ++i) {
            Item[] itemSet = ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS.get(i);
            Block[] wallSet = CandlestickGenerator.candlestickRustableWallVariants.get(i);
            Block[] standingSet = CandlestickGenerator.candlestickRustableStandingVariants.get(i);
            for (int j = 0; j < ItemRegistry.CANDLESTICK_ITEM_VARIANTS.length; ++j) {
                addDrop(wallSet[i], itemSet[i]);
                addDrop(standingSet[i], itemSet[i]);
            }
        }

        // Light Strips
        Arrays.stream(ColouredLightsGenerator.LightStripBlockVariants).forEach(this::addDrop);
    }
}
