package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.content.ModBlockTags;
import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import io.github.mikip98.humilityafm.generators.CandlestickGenerator;
import io.github.mikip98.humilityafm.generators.TerracottaTilesGenerator;
import io.github.mikip98.humilityafm.generators.WoodenMosaicGenerator;
import io.github.mikip98.humilityafm.mixin.BlockSettingsAccessor;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.util.GenerationData;
import io.github.mikip98.humilityafm.util.data_types.Pair;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static io.github.mikip98.humilityafm.registries.BlockRegistry.keyOfBlock;

public class BlockTagGenerator extends FabricTagProvider.BlockTagProvider {
    public BlockTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // ------------ Custom Tags ------------
        // Cabinet Block Tags
        // TODO: I see that 'valueLookupBuilder' takes in Blocks and 'builder' takes in RegistryKey<Block>...
        //  Well... Now is a bit to late to learn this fact... I'm keeping this as it is (as long as it runs)
        valueLookupBuilder(ModBlockTags.CABINET_BLOCKS)
                .add(CabinetBlockGenerator.cabinetBlockVariants)
                .add(BlockRegistry.CABINET_BLOCK);  // Manual testing block
        builder(ModBlockTags.ILLUMINATED_CABINET_BLOCKS)
                .add(toRegistryKey(CabinetBlockGenerator.illuminatedCabinetBlockVariants))
                .add(toRegistryKey(BlockRegistry.ILLUMINATED_CABINET_BLOCK));  // Manual testing block
        builder(ModBlockTags.CABINET_BLOCKS)
                .add(toRegistryKey(CabinetBlockGenerator.floorCabinetBlockVariants))
                .add(toRegistryKey(BlockRegistry.FLOOR_CABINET_BLOCK));  // Manual testing block
        builder(ModBlockTags.ILLUMINATED_CABINET_BLOCKS)
                .add(toRegistryKey(CabinetBlockGenerator.floorIlluminatedCabinetBlockVariants))
                .add(toRegistryKey(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK));  // Manual testing block

        // Wooden Mosaic Block Tag
        builder(ModBlockTags.WOODEN_MOSAIC_BLOCKS)
                .add(toRegistryKey(WoodenMosaicGenerator.woodenMosaicVariants))
                .add(toRegistryKey(BlockRegistry.WOODEN_MOSAIC));  // Manual testing block

        // Terracotta Tiles Block Tag
        builder(ModBlockTags.TERRACOTTA_TILES_BLOCKS)
                .add(toRegistryKey(TerracottaTilesGenerator.terracottaTilesVariants));

        // Inner & Outer Stairs Block Tags
        String[] woodenVariants = GenerationData.vanillaWoodTypes;
        String[] stonyVariants = GenerationData.vanillaStonyMaterialsPerStrength.stream().map(Pair::second)
                .flatMap(Arrays::stream).toArray(String[]::new);

        // Inner Stairs
        builder(ModBlockTags.WOODEN_INNER_STAIRS)
                .add(Arrays.stream(woodenVariants).map(s -> keyOfBlock("inner_stairs_" + s)))
                .add(toRegistryKey(BlockRegistry.INNER_STAIRS));  // Manual testing block
        builder(ModBlockTags.STONE_INNER_STAIRS)
                .add(Arrays.stream(stonyVariants).map(s -> keyOfBlock("inner_stairs_" + s)));
        // Outer Stairs
        builder(ModBlockTags.WOODEN_OUTER_STAIRS)
                .add(Arrays.stream(woodenVariants).map(s -> keyOfBlock("outer_stairs_" + s)))
                .add(toRegistryKey(BlockRegistry.OUTER_STAIRS));  // Manual testing block
        builder(ModBlockTags.STONE_OUTER_STAIRS)
                .add(Arrays.stream(stonyVariants).map(s -> keyOfBlock("outer_stairs_" + s)));

        // Jack o'Lanterns
        builder(ModBlockTags.JACK_O_LANTERNS)
                .add(toRegistryKey(BlockRegistry.JACK_O_LANTERN_SOUL))
                .add(toRegistryKey(BlockRegistry.JACK_O_LANTERN_REDSTONE));
        builder(ModBlockTags.COLOURED_JACK_O_LANTERNS)
                .add(toRegistryKey(BlockRegistry.COLOURED_WEAK_JACK_O_LANTERNS))
                .add(toRegistryKey(BlockRegistry.COLOURED_WEAK_JACK_O_LANTERNS))
                .add(toRegistryKey(BlockRegistry.COLOURED_STRONG_JACK_O_LANTERNS));

        // Candlesticks
        ProvidedTagBuilder<RegistryKey<Block>, Block> tag = builder(ModBlockTags.CANDLESTICKS)
                .add(toRegistryKey(CandlestickGenerator.candlestickClassicStandingVariants))
                .add(toRegistryKey(CandlestickGenerator.candlestickClassicWallVariants));
        CandlestickGenerator.candlestickRustableStandingVariants.forEach((block) -> tag.add(toRegistryKey(block)));
        CandlestickGenerator.candlestickRustableWallVariants.forEach((block) -> tag.add(toRegistryKey(block)));


        // ------------ Vanilla Tags ------------
        // Axe Mineable
        builder(ModBlockTags.AXE_MINEABLE)
                // Cabinets
                .addTag(ModBlockTags.CABINET_BLOCKS)
                .addTag(ModBlockTags.ILLUMINATED_CABINET_BLOCKS)
                .addOptionalTag(ModBlockTags.BETTER_NETHER_CABINET_BLOCKS)
                .addOptionalTag(ModBlockTags.BETTER_END_CABINET_BLOCKS)
                // Wooden Mosaics
                .addTag(ModBlockTags.WOODEN_MOSAIC_BLOCKS)
                // Inner & Outer Stairs
                .addTag(ModBlockTags.WOODEN_INNER_STAIRS)
                .addTag(ModBlockTags.WOODEN_OUTER_STAIRS)
                // Jack o'Lanterns
                .addTag(ModBlockTags.JACK_O_LANTERNS)
                .addOptionalTag(ModBlockTags.COLOURED_JACK_O_LANTERNS);
        // Pickaxe Mineable
        builder(ModBlockTags.PICKAXE_MINEABLE)
                // Terracotta Tiles
                .addTag(ModBlockTags.TERRACOTTA_TILES_BLOCKS)
                // Inner & Outer Stairs
                .addTag(ModBlockTags.STONE_INNER_STAIRS)
                .addTag(ModBlockTags.STONE_OUTER_STAIRS)
                // Candlesticks
                .addOptionalTag(ModBlockTags.CANDLESTICKS);
    }

    @SuppressWarnings("unchecked")
    protected static RegistryKey<Block>[] toRegistryKey(Block[] blocks) {
        return (RegistryKey<Block>[]) Arrays.stream(blocks).map(BlockTagGenerator::toRegistryKey).toArray(RegistryKey[]::new);
    }
    protected static RegistryKey<Block> toRegistryKey(Block block) {
        return ((BlockSettingsAccessor) block.getSettings()).getRegistryKey();
    }
}