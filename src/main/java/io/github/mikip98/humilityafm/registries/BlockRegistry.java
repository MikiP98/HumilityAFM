package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorIlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.IlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns.JackOLanternRedStone;
import io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns.JackOLanternSoul;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
#if MC_VERSION < 12104 import net.minecraft.core.Registry; #endif
#if MC_VERSION < 12104 import net.minecraft.core.registries.BuiltInRegistries; #endif
#if MC_VERSION >= 12104
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
#endif
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
#if MC_VERSION >= 12104
import net.minecraft.world.level.block.Blocks;
#endif
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class BlockRegistry extends BlockGeneration {
    // Cabinet blocks
    // Testing blocks (from petrified slabs)
    public static final Block CABINET_BLOCK = register("wall_cabinet_block", CabinetBlock::new, CabinetBlock.defaultSettings);
    public static final Block ILLUMINATED_CABINET_BLOCK = register("wall_illuminated_cabinet_block", IlluminatedCabinetBlock::new, IlluminatedCabinetBlock.defaultSettings);
    public static final Block FLOOR_CABINET_BLOCK = register("cabinet_block", FloorCabinetBlock::new, FloorCabinetBlock.defaultSettings);
    public static final Block FLOOR_ILLUMINATED_CABINET_BLOCK = register("illuminated_cabinet_block", FloorIlluminatedCabinetBlock::new, FloorIlluminatedCabinetBlock.defaultSettings);
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
    public static final Block[] COLOURED_TORCH_VARIANTS;
    public static final Block[] COLOURED_WALL_TORCH_VARIANTS;
    public static final Block[] COLOURED_JACK_O_LANTERNS;
    static {
        ColouredFeatureBlockSet colouredFeatureBlockSet = generateColouredFeatureBlockSet();
        LIGHT_STRIP_VARIANTS = colouredFeatureBlockSet.lightStripVariants();
        COLOURED_TORCH_VARIANTS = colouredFeatureBlockSet.colouredTorchVariants();
        COLOURED_WALL_TORCH_VARIANTS = colouredFeatureBlockSet.colouredWallTorchVariants();
        COLOURED_JACK_O_LANTERNS = colouredFeatureBlockSet.colouredJackOLanternVariants();
    }


    public static void init() {
        #if MC_VERSION >= 12104
        cache = null;
        #endif
    }


    public static Block registerWithItem(String name, BlockBehaviour.Properties settings) {
        return registerWithItem(name, Block::new, settings);
    }

    public static <T extends Block> T registerWithItem(String name, Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties settings) {
        T block = register(name, blockFactory, settings);
        ItemRegistry.register(name, (itemSettings) -> new BlockItem(block, itemSettings));
        return block;
    }

    public static Block register(String name, BlockBehaviour.Properties settings) {
        return register(name, Block::new, settings);
    }

    #if MC_VERSION >= 12104
    protected static Map<BlockBehaviour.Properties, Block> cache = new IdentityHashMap<>();
    #endif

    public static <T extends Block> T register(String name, Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties settings) {
        #if MC_VERSION < 12104
        return Registry.register(BuiltInRegistries.BLOCK, getId(name), blockFactory.apply(settings));
        #else
        assert cache != null;
        if (cache.containsKey(settings)) {
            return registerRaw(name, blockFactory, BlockBehaviour.Properties.ofFullCopy(cache.get(settings)));
        }
        final T block = registerRaw(name, blockFactory, settings);
        cache.put(settings, block);
        return block;
        #endif
    }

    #if MC_VERSION >= 12104
    @SuppressWarnings("unchecked")
    public static <T extends Block> T registerRaw(String name, Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties settings) {
        return (T) Blocks.register(keyOfBlock(name), (Function<BlockBehaviour.Properties, Block>) blockFactory, settings);
    }
    #endif



    #if MC_VERSION >= 12104
    public static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, getId(name));
    }
    #endif

    protected static void registerFlammable(Block block, int burnTime, int spreadSpeed) {
        FlammableBlockRegistry.getDefaultInstance().add(block, burnTime, spreadSpeed);
    }
}
