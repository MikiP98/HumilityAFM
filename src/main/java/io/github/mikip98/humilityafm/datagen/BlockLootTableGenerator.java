package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import io.github.mikip98.humilityafm.generators.CandlestickGenerator;
import io.github.mikip98.humilityafm.generators.ColouredLightsGenerator;
import io.github.mikip98.humilityafm.generators.ForcedCornerStairsGenerator;
import io.github.mikip98.humilityafm.generators.TerracottaTilesGenerator;
import io.github.mikip98.humilityafm.generators.WoodenMosaicGenerator;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;

import java.util.Arrays;

public class BlockLootTableGenerator extends FabricBlockLootTableProvider {
    public BlockLootTableGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        // Cabinet blocks
        addDrop(BlockRegistry.CABINET_BLOCK);  // Manual testing block
        addDrop(BlockRegistry.ILLUMINATED_CABINET_BLOCK);  // Manual testing block
        for (Block block : CabinetBlockGenerator.cabinetBlockVariants) {
            addDrop(block);
        }
        for (Block block : CabinetBlockGenerator.illuminatedCabinetBlockVariants) {
            addDrop(block);
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
        Arrays.stream(CandlestickGenerator.candlestickClassicVariants).forEach(this::addDrop);
        CandlestickGenerator.candlestickRustableVariants.forEach(v -> Arrays.stream(v).forEach(this::addDrop));

        // Light Strips
        Arrays.stream(ColouredLightsGenerator.LightStripBlockVariants).forEach(this::addDrop);
    }
}
