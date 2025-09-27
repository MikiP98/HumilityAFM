package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.content.tags.ModBlockTags;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.util.generation_data.ActiveGenerationData;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.BlockMaterial;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.MaterialType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class BlockTagGenerator extends FabricTagProvider.BlockTagProvider {
    public BlockTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // ------------ Custom Tags ------------
        // Cabinet Block Tags
        generateCabinetTags();

        // Wooden Mosaic Block Tag
        generateWoodenMosaicTags();

        // Terracotta Tiles Block Tag
        getOrCreateTagBuilder(ModBlockTags.TERRACOTTA_TILES_BLOCKS)
                .add(BlockRegistry.TERRACOTTA_TILE_VARIANTS);

        // Inner & Outer Stairs Block Tags
        generateForcedCornerStairsTags();

        // Jack o'Lanterns
        getOrCreateTagBuilder(ModBlockTags.JACK_O_LANTERNS)
                .addTag(ModBlockTags.SPECIAL_JACK_O_LANTERNS)
                .addOptionalTag(ModBlockTags.COLOURED_JACK_O_LANTERNS);
        getOrCreateTagBuilder(ModBlockTags.SPECIAL_JACK_O_LANTERNS)
                .add(BlockRegistry.JACK_O_LANTERN_SOUL)
                .add(BlockRegistry.JACK_O_LANTERN_REDSTONE);
        getOrCreateTagBuilder(ModBlockTags.COLOURED_JACK_O_LANTERNS)
                .add(BlockRegistry.COLOURED_JACK_O_LANTERNS);

        // Candlesticks
        FabricTagProvider<Block>.FabricTagBuilder tag = getOrCreateTagBuilder(ModBlockTags.CANDLESTICKS)
                .add(BlockRegistry.SIMPLE_CANDLESTICK_FLOOR_VARIANTS)
                .add(BlockRegistry.SIMPLE_CANDLESTICK_WALL_VARIANTS);
        Arrays.stream(BlockRegistry.RUSTABLE_CANDLESTICK_FLOOR_VARIANTS).forEach(tag::add);
        Arrays.stream(BlockRegistry.RUSTABLE_CANDLESTICK_WALL_VARIANTS).forEach(tag::add);

        // ------------ Vanilla Tags ------------
        // Axe Mineable
        getOrCreateTagBuilder(ModBlockTags.AXE_MINEABLE)
                // Cabinets
                .addTag(ModBlockTags.CABINET_BLOCKS)
                .addTag(ModBlockTags.ILLUMINATED_CABINET_BLOCKS)
                // Wooden Mosaics
                .addTag(ModBlockTags.WOODEN_MOSAIC_BLOCKS)
                // Inner & Outer Stairs
                .addTag(ModBlockTags.WOODEN_INNER_STAIRS)
                .addTag(ModBlockTags.WOODEN_OUTER_STAIRS)
                // Jack o'Lanterns
                .addTag(ModBlockTags.JACK_O_LANTERNS);
        // Pickaxe Mineable
        getOrCreateTagBuilder(ModBlockTags.PICKAXE_MINEABLE)
                // Terracotta Tiles
                .addTag(ModBlockTags.TERRACOTTA_TILES_BLOCKS)
                // Inner & Outer Stairs
                .addTag(ModBlockTags.STONY_INNER_STAIRS)
                .addTag(ModBlockTags.STONY_OUTER_STAIRS)
                // Candlesticks
                .addOptionalTag(ModBlockTags.CANDLESTICKS);
    }

    private void generateCabinetTags() {
        final FabricTagProvider<Block>.FabricTagBuilder cabinetTag = getOrCreateTagBuilder(ModBlockTags.CABINET_BLOCKS);
        final FabricTagProvider<Block>.FabricTagBuilder illuminatedCabinetTag = getOrCreateTagBuilder(ModBlockTags.ILLUMINATED_CABINET_BLOCKS);

        cabinetTag
                .add(BlockRegistry.CABINET_BLOCK)
                .add(BlockRegistry.FLOOR_CABINET_BLOCK);
        illuminatedCabinetTag
                .add(BlockRegistry.ILLUMINATED_CABINET_BLOCK)
                .add(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK);

        String cabinetTagPrefix = "cabinet_blocks_";
        String illuminatedCabinetTagPrefix = "illuminated_cabinet_blocks_";
        Map<SupportedMods, TagKey<Block>> cabinetTags = new HashMap<>();
        Map<SupportedMods, TagKey<Block>> illuminatedCabinetTags = new HashMap<>();

        int i = 0;
        for (BlockMaterial material : ActiveGenerationData.cabinetVariantMaterials) {
            SupportedMods sourceMod = material.layers()[0].metadata().sourceMod();
            String sourceModId = sourceMod != null ? sourceMod.modId : "vanilla";

            cabinetTags.computeIfAbsent(sourceMod, k -> getTagKey(cabinetTagPrefix + sourceModId));
            getOrCreateTagBuilder(cabinetTags.get(sourceMod))
                    .add(BlockRegistry.WALL_CABINET_BLOCK_VARIANTS[i])
                    .add(BlockRegistry.FLOOR_CABINET_BLOCK_VARIANTS[i]);

            illuminatedCabinetTags.computeIfAbsent(sourceMod, k -> getTagKey(illuminatedCabinetTagPrefix + sourceModId));
            getOrCreateTagBuilder(illuminatedCabinetTags.get(sourceMod))
                    .add(BlockRegistry.WALL_ILLUMINATED_CABINET_BLOCK_VARIANTS[i])
                    .add(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK_VARIANTS[i]);

            ++i;
        }

        cabinetTags.values().forEach(cabinetTag::addOptionalTag);
        illuminatedCabinetTags.values().forEach(illuminatedCabinetTag::addOptionalTag);
    }

    private void generateWoodenMosaicTags() {
        final FabricTagProvider<Block>.FabricTagBuilder woodenMosaicTag = getOrCreateTagBuilder(ModBlockTags.WOODEN_MOSAIC_BLOCKS);

        String woodenMosaicTagPrefix = "wooden_mosaic_blocks_";
        Map<Set<SupportedMods>, TagKey<Block>> woodenMosaicTags = new HashMap<>();

        int i = 0;
        for (BlockMaterial material : ActiveGenerationData.woodenMosaicVariantMaterials) {
            final SupportedMods layer1SourceMod = material.layers()[0].metadata().sourceMod();
            final SupportedMods layer2SourceMod = material.layers()[1].metadata().sourceMod();
            Set<SupportedMods> sourceMods = new HashSet<>(Arrays.asList(layer1SourceMod, layer2SourceMod));

            if (!woodenMosaicTags.containsKey(sourceMods)) {
                final String layer1SourceModId = layer1SourceMod != null ? layer1SourceMod.modId : "vanilla";
                final String layer2SourceModId = layer2SourceMod != null ? layer2SourceMod.modId : "vanilla";

                woodenMosaicTags.put(sourceMods, getTagKey(woodenMosaicTagPrefix + layer1SourceModId + "_" + layer2SourceModId));
            }

            getOrCreateTagBuilder(woodenMosaicTags.get(sourceMods))
                    .add(BlockRegistry.WOODEN_MOSAIC_VARIANTS[i]);

            ++i;
        }

        woodenMosaicTags.values().forEach(woodenMosaicTag::addOptionalTag);
    }

    private void generateForcedCornerStairsTags() {
        final FabricTagProvider<Block>.FabricTagBuilder woodenInnerStairsTag = getOrCreateTagBuilder(ModBlockTags.WOODEN_INNER_STAIRS);
        final FabricTagProvider<Block>.FabricTagBuilder woodenOuterStairsTag = getOrCreateTagBuilder(ModBlockTags.WOODEN_OUTER_STAIRS);

        final FabricTagProvider<Block>.FabricTagBuilder stonyInnerStairsTag = getOrCreateTagBuilder(ModBlockTags.STONY_INNER_STAIRS);
        final FabricTagProvider<Block>.FabricTagBuilder stonyOuterStairsTag = getOrCreateTagBuilder(ModBlockTags.STONY_OUTER_STAIRS);

        Map<SupportedMods, TagKey<Block>> woodenInnerStairsTags = new HashMap<>();
        Map<SupportedMods, TagKey<Block>> woodenOuterStairsTags = new HashMap<>();

        Map<SupportedMods, TagKey<Block>> stonyInnerStairsTags = new HashMap<>();
        Map<SupportedMods, TagKey<Block>> stonyOuterStairsTags = new HashMap<>();

        final String woodenInnerTagPrefix = "wooden_inner_stairs_";
        final String woodenOuterTagPrefix = "wooden_outer_stairs_";

        final String stonyInnerTagPrefix = "stony_inner_stairs_";
        final String stonyOuterTagPrefix = "stony_outer_stairs_";

        for (BlockMaterial material : ActiveGenerationData.forcedCornerStairsVariantMaterials) {
            if (material.layers().length != 1) throw new IllegalStateException("Forced corner stairs material must have exactly one layer, but found: " + material.layers().length);
            BlockMaterial.Layer layer = material.layers()[0];

            SupportedMods sourceMod = layer.metadata().sourceMod();
            String sourceModId = sourceMod != null ? sourceMod.modId : "vanilla";  // Fallback to 'vanilla' if no mod is specified
            MaterialType type = layer.metadata().type();
            if (type == null) throw new IllegalStateException("Material type for forced corner stairs material must not be null");
            String innerStairsName = "inner_stairs_" + material.getSafeName();
            String outerStairsName = "outer_stairs_" + material.getSafeName();

            switch (type) {
                case BURNABLE_WOOD, FIREPROOF_WOOD:
                    String innerTag = woodenInnerTagPrefix + sourceModId;
                    String outerTag = woodenOuterTagPrefix + sourceModId;

                    woodenInnerStairsTags.computeIfAbsent(sourceMod, m -> getTagKey(innerTag));
                    woodenOuterStairsTags.computeIfAbsent(sourceMod, m -> getTagKey(outerTag));

                    getOrCreateTagBuilder(woodenInnerStairsTags.get(sourceMod)).add(getId(innerStairsName));
                    getOrCreateTagBuilder(woodenOuterStairsTags.get(sourceMod)).add(getId(outerStairsName));
                    break;
                case STONY:
                    String stonyInnerTag = stonyInnerTagPrefix + sourceModId;
                    String stonyOuterTag = stonyOuterTagPrefix + sourceModId;

                    stonyInnerStairsTags.computeIfAbsent(sourceMod, m -> getTagKey(stonyInnerTag));
                    stonyOuterStairsTags.computeIfAbsent(sourceMod, m -> getTagKey(stonyOuterTag));

                    getOrCreateTagBuilder(stonyInnerStairsTags.get(sourceMod)).add(getId(innerStairsName));
                    getOrCreateTagBuilder(stonyOuterStairsTags.get(sourceMod)).add(getId(outerStairsName));
                    break;
            }
        }

        woodenInnerStairsTags.values().forEach(woodenInnerStairsTag::addOptionalTag);
        woodenOuterStairsTags.values().forEach(woodenOuterStairsTag::addOptionalTag);
        stonyInnerStairsTags.values().forEach(stonyInnerStairsTag::addOptionalTag);
        stonyOuterStairsTags.values().forEach(stonyOuterStairsTag::addOptionalTag);
    }

    public static TagKey<Block> getTagKey(String name) {
        return TagKey.of(RegistryKeys.BLOCK, getId(name));
    }
}