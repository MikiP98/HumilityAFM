package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.content.ModBlockTags;
import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import io.github.mikip98.humilityafm.helpers.*;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.util.GenerationData;
import io.github.mikip98.humilityafm.util.data_types.Pair;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static io.github.mikip98.humilityafm.HumilityAFM.getIds;

public class BlockTagGenerator extends FabricTagProvider.BlockTagProvider {
    public BlockTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // ------------ Custom Tags ------------
        // Cabinet Block Tags
        getOrCreateTagBuilder(ModBlockTags.CABINET_BLOCKS)
                .add(getIds(Arrays.stream(CabinetBlockGenerator.cabinetBlockVariantsNames).map(s -> "cabinet_block_" + s)))
                .add(BlockRegistry.CABINET_BLOCK);  // Manual testing block
        getOrCreateTagBuilder(ModBlockTags.ILLUMINATED_CABINET_BLOCKS)
                .add(getIds(Arrays.stream(CabinetBlockGenerator.cabinetBlockVariantsNames).map(s -> "illuminated_cabinet_block_" + s)))
                .add(BlockRegistry.ILLUMINATED_CABINET_BLOCK);  // Manual testing block

        // Wooden Mosaic Block Tag
        getOrCreateTagBuilder(ModBlockTags.WOODEN_MOSAIC_BLOCKS)
                .add(getIds(Arrays.stream(WoodenMosaicHelper.woodenMosaicVariantsNames).map(s -> "wooden_mosaic_" + s)))
                .add(BlockRegistry.WOODEN_MOSAIC);  // Manual testing block

        // Terracotta Tiles Block Tag
        getOrCreateTagBuilder(ModBlockTags.TERRACOTTA_TILES_BLOCKS)
                .add(getIds(Arrays.stream(TerracottaTilesHelper.terracottaTilesVariantsNames).map(s -> "terracotta_tiles_" + s)));

        // Inner & Outer Stairs Block Tags
        String[] woodenVariants = GenerationData.vanillaWoodTypes;
        String[] stonyVariants = GenerationData.vanillaStonyMaterialsPerStrength.stream().map(Pair::second)
                .flatMap(Arrays::stream).toArray(String[]::new);

        // Inner Stairs
        getOrCreateTagBuilder(ModBlockTags.WOODEN_INNER_STAIRS)
                .add(getIds(Arrays.stream(woodenVariants).map(s -> "inner_stairs_" + s)))
                .add(BlockRegistry.INNER_STAIRS);  // Manual testing block
        getOrCreateTagBuilder(ModBlockTags.STONE_INNER_STAIRS)
                .add(getIds(Arrays.stream(stonyVariants).map(s -> "inner_stairs_" + s)));
        // Outer Stairs
        getOrCreateTagBuilder(ModBlockTags.WOODEN_OUTER_STAIRS)
                .add(getIds(Arrays.stream(woodenVariants).map(s -> "outer_stairs_" + s)))
                .add(BlockRegistry.OUTER_STAIRS);  // Manual testing block
        getOrCreateTagBuilder(ModBlockTags.STONE_OUTER_STAIRS)
                .add(getIds(Arrays.stream(stonyVariants).map(s -> "outer_stairs_" + s)));


        // ------------ Vanilla Tags ------------
        // Axe Mineable
        getOrCreateTagBuilder(ModBlockTags.AXE_MINEABLE)
                // Cabinets
                .addTag(ModBlockTags.CABINET_BLOCKS)
                .addTag(ModBlockTags.ILLUMINATED_CABINET_BLOCKS)
                .addOptionalTag(ModBlockTags.BETTER_NETHER_CABINET_BLOCKS)
                .addOptionalTag(ModBlockTags.BETTER_END_CABINET_BLOCKS)
                // Wooden Mosaics
                .addTag(ModBlockTags.WOODEN_MOSAIC_BLOCKS)
                // Inner & Outer Stairs
                .addTag(ModBlockTags.WOODEN_INNER_STAIRS)
                .addTag(ModBlockTags.WOODEN_OUTER_STAIRS);
        // Pickaxe Mineable
        getOrCreateTagBuilder(ModBlockTags.PICKAXE_MINEABLE)
                // Terracotta Tiles
                .addTag(ModBlockTags.TERRACOTTA_TILES_BLOCKS)
                // Inner & Outer Stairs
                .addTag(ModBlockTags.STONE_INNER_STAIRS)
                .addTag(ModBlockTags.STONE_OUTER_STAIRS);
    }
}