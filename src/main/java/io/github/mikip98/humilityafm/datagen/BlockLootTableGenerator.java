package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import io.github.mikip98.humilityafm.generators.ForcedCornerStairsGenerator;
import io.github.mikip98.humilityafm.helpers.TerracottaTilesHelper;
import io.github.mikip98.humilityafm.helpers.WoodenMosaicHelper;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;

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
        for (Block block : WoodenMosaicHelper.woodenMosaicVariants) {
            addDrop(block);
        }

        // Terracotta Tiles
        for (Block block : TerracottaTilesHelper.terracottaTilesVariants) {
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
    }
}
