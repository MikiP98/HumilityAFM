package io.github.mikip98.datagen;

import io.github.mikip98.HumilityAFM;
import io.github.mikip98.helpers.CabinetBlockHelper;
import io.github.mikip98.helpers.InnerOuterStairsHelper;
import io.github.mikip98.helpers.TerracottaTilesHelper;
import io.github.mikip98.helpers.WoodenMosaicHelper;
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
        addDrop(HumilityAFM.CABINET_BLOCK);  // Manual testing block
        addDrop(HumilityAFM.ILLUMINATED_CABINET_BLOCK);  // Manual testing block
        for (Block block : CabinetBlockHelper.cabinetBlockVariants) {
            addDrop(block);
        }
        for (Block block : CabinetBlockHelper.illuminatedCabinetBlockVariants) {
            addDrop(block);
        }

        // Wooden Mosaics
        addDrop(HumilityAFM.WOODEN_MOSAIC);  // Manual testing block
        for (Block block : WoodenMosaicHelper.woodenMosaicVariants) {
            addDrop(block);
        }

        // Terracotta Tiles
        for (Block block : TerracottaTilesHelper.terracottaTilesVariants) {
            addDrop(block);
        }

        // Inner & Outer Stairs
        addDrop(HumilityAFM.INNER_STAIRS);  // Manual testing block
        addDrop(HumilityAFM.OUTER_STAIRS);  // Manual testing block
        for (Block block : InnerOuterStairsHelper.innerStairsBlockVariants) {
            addDrop(block);
        }
        for (Block block : InnerOuterStairsHelper.outerStairsBlockVariants) {
            addDrop(block);
        }
    }
}
