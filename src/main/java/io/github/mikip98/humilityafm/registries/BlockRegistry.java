package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorIlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.IlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns.JackOLanternRedStone;
import io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns.JackOLanternSoul;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Function;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class BlockRegistry extends BlockGeneration {
    // Cabinet blocks
    // Testing blocks (from petrified slabs)
    public static final Block CABINET_BLOCK = register("wall_cabinet_block", CabinetBlock::new, CabinetBlock.defaultSettings);
    public static final Block ILLUMINATED_CABINET_BLOCK = register("wall_illuminated_cabinet_block", IlluminatedCabinetBlock::new, CabinetBlock.defaultSettings);
    public static final Block FLOOR_CABINET_BLOCK = register("cabinet_block", FloorCabinetBlock::new, IlluminatedCabinetBlock.defaultSettings);
    public static final Block FLOOR_ILLUMINATED_CABINET_BLOCK = register("illuminated_cabinet_block", FloorIlluminatedCabinetBlock::new, IlluminatedCabinetBlock.defaultSettings);
    // Final variants
    public static final Block[] WALL_CABINET_BLOCK_VARIANTS;
    public static final Block[] WALL_ILLUMINATED_CABINET_BLOCK_VARIANTS;
    public static final Block[] FLOOR_CABINET_BLOCK_VARIANTS;
    public static final Block[] FLOOR_ILLUMINATED_CABINET_BLOCK_VARIANTS;
    static {
        CabinetBlockSet cabinetBlockSet = generateCabinetBlockSet();
        WALL_CABINET_BLOCK_VARIANTS = cabinetBlockSet.wallCabinets();
        WALL_ILLUMINATED_CABINET_BLOCK_VARIANTS = cabinetBlockSet.wallIlluminatedCabinets();
        FLOOR_CABINET_BLOCK_VARIANTS = cabinetBlockSet.floorCabinets();
        FLOOR_ILLUMINATED_CABINET_BLOCK_VARIANTS = cabinetBlockSet.floorIlluminatedCabinets();
    }

    // Stairs
    public static final Block[] INNER_STAIRS_BLOCK_VARIANTS;
    public static final Block[] OUTER_STAIRS_BLOCK_VARIANTS;
    static {
        ForcedCornerStairsBlockSet forcedCornerStairsBlockSet = generateForcedCornerStairsBlockSet();
        INNER_STAIRS_BLOCK_VARIANTS = forcedCornerStairsBlockSet.innerStairs();
        OUTER_STAIRS_BLOCK_VARIANTS = forcedCornerStairsBlockSet.outerStairs();
    }

    // Wooden mosaics
    public static final Block[] WOODEN_MOSAIC_VARIANTS = generateWoodenMosaicVariants();
    // Terracotta tiles
    public static final Block[] TERRACOTTA_TILE_VARIANTS = generateTerracottaTilesVariants();
    // Jack o'Lanterns
    public static final Block JACK_O_LANTERN_REDSTONE = registerWithItem("jack_o_lantern_redstone", JackOLanternRedStone::new, JackOLanternRedStone.defaultSettings);
    public static final Block JACK_O_LANTERN_SOUL = registerWithItem("jack_o_lantern_soul", JackOLanternSoul::new, JackOLanternSoul.defaultSettings);

    // CANDLESTICK BETA
    public static final Block[] SIMPLE_CANDLESTICK_WALL_VARIANTS;
    public static final Block[] SIMPLE_CANDLESTICK_FLOOR_VARIANTS;
    public static final Block[][] RUSTABLE_CANDLESTICK_WALL_VARIANTS;
    public static final Block[][] RUSTABLE_CANDLESTICK_FLOOR_VARIANTS;
    static {
        CandlestickBlockSet candlestickBlockSet = generateCandlestickBlockSet();
        SIMPLE_CANDLESTICK_WALL_VARIANTS = candlestickBlockSet.simpleCandlestickWallVariants();
        SIMPLE_CANDLESTICK_FLOOR_VARIANTS = candlestickBlockSet.simpleCandlestickFloorVariants();
        RUSTABLE_CANDLESTICK_WALL_VARIANTS = candlestickBlockSet.rustableCandlestickWallVariants();
        RUSTABLE_CANDLESTICK_FLOOR_VARIANTS = candlestickBlockSet.rustableCandlestickFloorVariants();
    }

    // COLOURED FEATURES BETA
    public static final Block[] LIGHT_STRIP_VARIANTS;
    public static final Block[] COLOURED_TORCH_WEAK_VARIANTS;
    public static final Block[] COLOURED_TORCH_VARIANTS;
    public static final Block[] COLOURED_TORCH_STRONG_VARIANTS;
    public static final Block[] COLOURED_JACK_O_LANTERNS_WEAK;
    public static final Block[] COLOURED_JACK_O_LANTERNS;
    public static final Block[] COLOURED_JACK_O_LANTERNS_STRONG;
    static {
        ColouredFeatureBlockSet colouredFeatureBlockSet = generateColouredFeatureBlockSet();
        LIGHT_STRIP_VARIANTS = colouredFeatureBlockSet.lightStripVariants();
        COLOURED_TORCH_WEAK_VARIANTS = colouredFeatureBlockSet.colouredTorchWeakVariants();
        COLOURED_TORCH_VARIANTS = colouredFeatureBlockSet.colouredTorchVariants();
        COLOURED_TORCH_STRONG_VARIANTS = colouredFeatureBlockSet.colouredTorchStrongVariants();
        COLOURED_JACK_O_LANTERNS_WEAK = colouredFeatureBlockSet.colouredJackOLanternWeakVariants();
        COLOURED_JACK_O_LANTERNS = colouredFeatureBlockSet.colouredJackOLanternVariants();
        COLOURED_JACK_O_LANTERNS_STRONG = colouredFeatureBlockSet.colouredJackOLanternStrongVariants();
    }


    /**
     * This method is called from the main mod class to ensure the static initialisation of this class.
     */
    public static void init() {}


    public static RegistryKey<Block> keyOfBlock(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, getId(name));
    }

    public static Block registerWithItem(String name, Function<AbstractBlock.Settings, Block> blockFactory, Block.Settings settings) {
        Block block = Blocks.register(keyOfBlock(name), blockFactory, settings);
        ItemRegistry.register(name, (itemSettings) -> new BlockItem(block, itemSettings));
        return block;
    }
    public static Block register(String name, Function<Block.Settings, Block> blockFactory, Block.Settings settings) {
        return Blocks.register(keyOfBlock(name), blockFactory, settings);
    }

    protected static void registerFlammable(Block block, int burnTime, int spreadSpeed) {
        FlammableBlockRegistry.getDefaultInstance().add(block, burnTime, spreadSpeed);
    }
}
