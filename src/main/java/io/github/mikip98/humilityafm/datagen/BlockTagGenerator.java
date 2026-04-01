package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.content.tags.ModBlockTags;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.util.generation_data.ActiveGenerationData;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.BlockMaterial;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.MaterialType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class BlockTagGenerator extends FabricTagProvider.BlockTagProvider {
    public BlockTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    protected void addTags(HolderLookup.Provider arg) {
        // ------------ Custom Tags ------------
        // Cabinet Block Tags
        generateCabinetTags();

        // Wooden Mosaic Block Tag
        generateWoodenMosaicTags();

        // Terracotta Tiles Block Tag
        getCommonTagBuilder(ModBlockTags.TERRACOTTA_TILES_BLOCKS)
                .add(BlockRegistry.TERRACOTTA_TILE_VARIANTS);

        // Inner & Outer Stairs Block Tags
        generateForcedCornerStairsTags();

        // Jack o'Lanterns
        getCommonTagBuilder(ModBlockTags.JACK_O_LANTERNS)
                .addTag(ModBlockTags.SPECIAL_JACK_O_LANTERNS)
                .addOptionalTag(ModBlockTags.COLOURED_JACK_O_LANTERNS);
        getCommonTagBuilder(ModBlockTags.SPECIAL_JACK_O_LANTERNS)
                .add(BlockRegistry.JACK_O_LANTERN_SOUL)
                .add(BlockRegistry.JACK_O_LANTERN_REDSTONE);
        getCommonTagBuilder(ModBlockTags.COLOURED_JACK_O_LANTERNS)
                .add(BlockRegistry.COLOURED_JACK_O_LANTERNS);

        // Candlesticks
        TagBuilderWrapper tag = getCommonTagBuilder(ModBlockTags.CANDLESTICKS)
                .add(BlockRegistry.SIMPLE_CANDLESTICK_FLOOR_VARIANTS)
                .add(BlockRegistry.SIMPLE_CANDLESTICK_WALL_VARIANTS);
        Arrays.stream(BlockRegistry.RUSTABLE_CANDLESTICK_FLOOR_VARIANTS).forEach(tag::add);
        Arrays.stream(BlockRegistry.RUSTABLE_CANDLESTICK_WALL_VARIANTS).forEach(tag::add);

        // ------------ Vanilla Tags ------------
        // Axe Mineable
        getCommonTagBuilder(ModBlockTags.AXE_MINEABLE)
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
        getCommonTagBuilder(ModBlockTags.PICKAXE_MINEABLE)
                // Terracotta Tiles
                .addTag(ModBlockTags.TERRACOTTA_TILES_BLOCKS)
                // Inner & Outer Stairs
                .addTag(ModBlockTags.STONY_INNER_STAIRS)
                .addTag(ModBlockTags.STONY_OUTER_STAIRS)
                // Candlesticks
                .addOptionalTag(ModBlockTags.CANDLESTICKS);
    }
    // TODO: Consider changing '.add()' to '.addOptional()' for modded block in modded tags
    //  This would get rid of the 21 tag loading errors, but will increase the JAR size

    private void generateCabinetTags() {
        final TagBuilderWrapper cabinetTag = getCommonTagBuilder(ModBlockTags.CABINET_BLOCKS);
        final TagBuilderWrapper illuminatedCabinetTag = getCommonTagBuilder(ModBlockTags.ILLUMINATED_CABINET_BLOCKS);

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
            getCommonTagBuilder(cabinetTags.get(sourceMod))
                    .add(BlockRegistry.WALL_CABINET_BLOCK_VARIANTS[i])
                    .add(BlockRegistry.FLOOR_CABINET_BLOCK_VARIANTS[i]);

            illuminatedCabinetTags.computeIfAbsent(sourceMod, k -> getTagKey(illuminatedCabinetTagPrefix + sourceModId));
            getCommonTagBuilder(illuminatedCabinetTags.get(sourceMod))
                    .add(BlockRegistry.WALL_ILLUMINATED_CABINET_BLOCK_VARIANTS[i])
                    .add(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK_VARIANTS[i]);

            ++i;
        }

        cabinetTags.values().forEach(cabinetTag::addOptionalTag);
        illuminatedCabinetTags.values().forEach(illuminatedCabinetTag::addOptionalTag);
    }

    private void generateWoodenMosaicTags() {
        final TagBuilderWrapper woodenMosaicTag = getCommonTagBuilder(ModBlockTags.WOODEN_MOSAIC_BLOCKS);

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

            getCommonTagBuilder(woodenMosaicTags.get(sourceMods))
                    .add(BlockRegistry.WOODEN_MOSAIC_VARIANTS[i]);

            ++i;
        }

        woodenMosaicTags.values().forEach(woodenMosaicTag::addOptionalTag);
    }

    private void generateForcedCornerStairsTags() {
        final TagBuilderWrapper woodenInnerStairsTag = getCommonTagBuilder(ModBlockTags.WOODEN_INNER_STAIRS);
        final TagBuilderWrapper woodenOuterStairsTag = getCommonTagBuilder(ModBlockTags.WOODEN_OUTER_STAIRS);

        final TagBuilderWrapper stonyInnerStairsTag = getCommonTagBuilder(ModBlockTags.STONY_INNER_STAIRS);
        final TagBuilderWrapper stonyOuterStairsTag = getCommonTagBuilder(ModBlockTags.STONY_OUTER_STAIRS);

        Map<SupportedMods, TagKey<Block>> woodenInnerStairsTags = new HashMap<>();
        Map<SupportedMods, TagKey<Block>> woodenOuterStairsTags = new HashMap<>();

        Map<SupportedMods, TagKey<Block>> stonyInnerStairsTags = new HashMap<>();
        Map<SupportedMods, TagKey<Block>> stonyOuterStairsTags = new HashMap<>();

        final String woodenInnerTagPrefix = "wooden_inner_stairs_";
        final String woodenOuterTagPrefix = "wooden_outer_stairs_";

        final String stonyInnerTagPrefix = "stony_inner_stairs_";
        final String stonyOuterTagPrefix = "stony_outer_stairs_";

        int i = 0;
        for (BlockMaterial material : ActiveGenerationData.forcedCornerStairsVariantMaterials) {
            if (material.layers().length != 1) throw new IllegalStateException("Forced corner stairs material must have exactly one layer, but found: " + material.layers().length);
            BlockMaterial.Layer layer = material.layers()[0];

            SupportedMods sourceMod = layer.metadata().sourceMod();
            String sourceModId = sourceMod != null ? sourceMod.modId : "vanilla";  // Fallback to 'vanilla' if no mod is specified
            MaterialType type = layer.metadata().type();
            if (type == null) throw new IllegalStateException("Material type for forced corner stairs material must not be null");

            switch (type) {
                case BURNABLE_WOOD, FIREPROOF_WOOD:
                    String innerTag = woodenInnerTagPrefix + sourceModId;
                    String outerTag = woodenOuterTagPrefix + sourceModId;

                    woodenInnerStairsTags.computeIfAbsent(sourceMod, m -> getTagKey(innerTag));
                    woodenOuterStairsTags.computeIfAbsent(sourceMod, m -> getTagKey(outerTag));

                    getCommonTagBuilder(woodenInnerStairsTags.get(sourceMod)).add(BlockRegistry.INNER_STAIRS_BLOCK_VARIANTS[i]);
                    getCommonTagBuilder(woodenOuterStairsTags.get(sourceMod)).add(BlockRegistry.OUTER_STAIRS_BLOCK_VARIANTS[i]);
                    break;
                case STONY:
                    String stonyInnerTag = stonyInnerTagPrefix + sourceModId;
                    String stonyOuterTag = stonyOuterTagPrefix + sourceModId;

                    stonyInnerStairsTags.computeIfAbsent(sourceMod, m -> getTagKey(stonyInnerTag));
                    stonyOuterStairsTags.computeIfAbsent(sourceMod, m -> getTagKey(stonyOuterTag));

                    getCommonTagBuilder(stonyInnerStairsTags.get(sourceMod)).add(BlockRegistry.INNER_STAIRS_BLOCK_VARIANTS[i]);
                    getCommonTagBuilder(stonyOuterStairsTags.get(sourceMod)).add(BlockRegistry.OUTER_STAIRS_BLOCK_VARIANTS[i]);
                    break;
            }
            ++i;
        }

        woodenInnerStairsTags.values().forEach(woodenInnerStairsTag::addOptionalTag);
        woodenOuterStairsTags.values().forEach(woodenOuterStairsTag::addOptionalTag);
        stonyInnerStairsTags.values().forEach(stonyInnerStairsTag::addOptionalTag);
        stonyOuterStairsTags.values().forEach(stonyOuterStairsTag::addOptionalTag);
    }

    public static TagKey<Block> getTagKey(String name) {
        return TagKey.create(Registries.BLOCK, getId(name));
    }

    protected TagBuilderWrapper getCommonTagBuilder(TagKey<Block> tag) {
        return new TagBuilderWrapper(tag);
    }

    protected class TagBuilderWrapper {
        #if MC_VERSION < 12105
        final FabricTagProvider<Block>.FabricTagBuilder builder;
        public TagBuilderWrapper(TagKey<Block> tag) {
            this.builder = getOrCreateTagBuilder(tag);
        }
        public TagBuilderWrapper add(Block... blocks) { builder.add(blocks); return this; }
        public TagBuilderWrapper addTag(TagKey<Block> tag) { builder.addTag(tag); return this; }
        public TagBuilderWrapper addOptionalTag(TagKey<Block> tag) { builder.addOptionalTag(tag); return this; }
        #else
        final protected TagBuilder builder;
        public TagBuilderWrapper(TagKey<Block> tag) {
            this.builder = getTagBuilder(tag);
        }
        public TagBuilderWrapper add(Block... blocks) {
            Arrays.stream(blocks).forEach((block) -> builder.add(Registries.BLOCK.getId(block)));
            return this;
        }
        public TagBuilderWrapper addTag(TagKey<Block> tag) {
            builder.addTag(tag.id());
            return this;
        }
        public TagBuilderWrapper addOptionalTag(TagKey<Block> tag) {
            builder.addOptionalTag(tag.id());
            return this;
        }
        #endif
    }
}