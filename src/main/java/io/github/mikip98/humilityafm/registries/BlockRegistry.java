package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorIlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.IlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns.JackOLanternRedStone;
import io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns.JackOLanternSoul;
#if MC_VERSION >= 12104
import io.github.mikip98.humilityafm.util.SettingsDuplicator;
#endif
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
#if MC_VERSION >= 260000
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
#else
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
#endif
#if MC_VERSION < 12104
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
#elif MC_VERSION < 260000
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
#endif
#if MC_VERSION < 12104
import net.minecraft.util.Identifier;
#endif

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


    /**
     * This method is called from the main mod class to ensure the static initialisation of this class.
     */
    public static void init() {
        #if MC_VERSION >= 12104
        SettingsDuplicator.cache.clear();
        SettingsDuplicator.cache = null;
        SettingsDuplicator.count = null;
        #endif
    }


    public static Block registerWithItem(String name, AbstractBlock.Settings settings) {
        return registerWithItem(name, Block::new, settings);
    }
    public static <T extends Block> T registerWithItem(String name, Function<AbstractBlock.Settings, T> blockFactory, AbstractBlock.Settings settings) {
        T block = register(name, blockFactory, settings);
        ItemRegistry.register(name, (itemSettings) -> new BlockItem(block, itemSettings));
        return block;
    }
    public static Block register(String name, AbstractBlock.Settings settings) {
        return register(name, Block::new, settings);
    }
    #if MC_VERSION >= 12104 @SuppressWarnings("unchecked") #endif
    public static <T extends Block> T register(String name, Function<AbstractBlock.Settings, T> blockFactory, AbstractBlock.Settings settings) {
        #if MC_VERSION < 12104
        return Registry.register(Registries.BLOCK, getId(name), blockFactory.apply(settings));
        #else
        final AbstractBlock.Settings settingsCopy = SettingsDuplicator.copy(settings);
        return (T) Blocks.register(keyOfBlock(name), (Function<AbstractBlock.Settings, Block>) blockFactory, settingsCopy);
        #endif
    }

    #if MC_VERSION >= 12104 && MC_VERSION < 260000
    public static RegistryKey<Block> keyOfBlock(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, getId(name));
    }
    #elif MC_VERSION >= 260000
    public static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, getId(name));
    }
    #endif

    protected static void registerFlammable(Block block, int burnTime, int spreadSpeed) {
        FlammableBlockRegistry.getDefaultInstance().add(block, burnTime, spreadSpeed);
    }
}
