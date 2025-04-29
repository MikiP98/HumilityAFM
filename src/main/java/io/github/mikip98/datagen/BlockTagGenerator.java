package io.github.mikip98.datagen;

import io.github.mikip98.HumilityAFM;
import io.github.mikip98.content.BlockTags;
import io.github.mikip98.helpers.*;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static io.github.mikip98.HumilityAFM.getIds;

public class BlockTagGenerator extends FabricTagProvider.BlockTagProvider {
    public BlockTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // Cabinet Block Tags
        getOrCreateTagBuilder(BlockTags.CABINET_BLOCKS)
                .add(getIds(Arrays.stream(CabinetBlockHelper.cabinetBlockVariantsNames).map(s -> "cabinet_block_" + s)))
                .add(HumilityAFM.CABINET_BLOCK);  // Manual testing block
        getOrCreateTagBuilder(BlockTags.ILLUMINATED_CABINET_BLOCKS)
                .add(getIds(Arrays.stream(CabinetBlockHelper.cabinetBlockVariantsNames).map(s -> "illuminated_cabinet_block_" + s)))
                .add(HumilityAFM.ILLUMINATED_CABINET_BLOCK);  // Manual testing block

        // Wooden Mosaic Block Tag
        getOrCreateTagBuilder(BlockTags.WOODEN_MOSAIC_BLOCKS)
                .add(getIds(Arrays.stream(WoodenMosaicHelper.woodenMosaicVariantsNames).map(s -> "wooden_mosaic_" + s)))
                .add(HumilityAFM.WOODEN_MOSAIC);  // Manual testing block

        // Terracotta Tiles Block Tag
        getOrCreateTagBuilder(BlockTags.TERRACOTTA_TILES_BLOCKS)
                .add(getIds(Arrays.stream(TerracottaTilesHelper.terracottaTilesVariantsNames).map(s -> "terracotta_tiles_" + s)));

        // Inner & Outer Stairs Block Tags
        String[] woodenVariants = MainHelper.vanillaWoodTypes;
        String[] singleStonyStairVariants = new String[]{
                InnerOuterStairsHelper.mudBricksVariant,
                InnerOuterStairsHelper.endStoneVariant
        };
        String[] stonyVariants = Stream.of(
                singleStonyStairVariants,
                InnerOuterStairsHelper.sandStoneANDQuartsVariants,
                InnerOuterStairsHelper.stony1Variants,
                InnerOuterStairsHelper.stony2Variants,
                InnerOuterStairsHelper.cutCopperVariants,
                InnerOuterStairsHelper.deepSlateVariants
        ).flatMap(Stream::of).toArray(String[]::new);;
        // Inner Stairs
        getOrCreateTagBuilder(BlockTags.WOODEN_INNER_STAIRS)
                .add(getIds(Arrays.stream(woodenVariants).map(s -> "inner_stairs_" + s)))
                .add(HumilityAFM.INNER_STAIRS);  // Manual testing block
        getOrCreateTagBuilder(BlockTags.STONE_INNER_STAIRS)
                .add(getIds(Arrays.stream(stonyVariants).map(s -> "inner_stairs_" + s)));
        // Outer Stairs
        getOrCreateTagBuilder(BlockTags.WOODEN_OUTER_STAIRS)
                .add(getIds(Arrays.stream(woodenVariants).map(s -> "outer_stairs_" + s)))
                .add(HumilityAFM.OUTER_STAIRS);  // Manual testing block
        getOrCreateTagBuilder(BlockTags.STONE_OUTER_STAIRS)
                .add(getIds(Arrays.stream(stonyVariants).map(s -> "outer_stairs_" + s)));

        // Vanilla Tags
        // Axe Mineable
        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                // Cabinets
                .addTag(BlockTags.CABINET_BLOCKS)
                .addTag(BlockTags.ILLUMINATED_CABINET_BLOCKS)
                .addOptionalTag(BlockTags.BETTER_NETHER_CABINET_BLOCKS)
                .addOptionalTag(BlockTags.BETTER_END_CABINET_BLOCKS)
                // Wooden Mosaics
                .addTag(BlockTags.WOODEN_MOSAIC_BLOCKS)
                // Inner & Outer Stairs
                .addTag(BlockTags.WOODEN_INNER_STAIRS)
                .addTag(BlockTags.WOODEN_OUTER_STAIRS);
        // Pickaxe Mineable
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                // Terracotta Tiles
                .addTag(BlockTags.TERRACOTTA_TILES_BLOCKS)
                // Inner & Outer Stairs
                .addTag(BlockTags.STONE_INNER_STAIRS)
                .addTag(BlockTags.STONE_OUTER_STAIRS);
    }
}