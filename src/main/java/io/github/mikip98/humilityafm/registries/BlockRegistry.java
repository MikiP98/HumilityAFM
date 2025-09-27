package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorIlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.IlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns.JackOLanternRedStone;
import io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns.JackOLanternSoul;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class BlockRegistry extends BlockGeneration {
    // Cabinet blocks
    // Testing blocks (from petrified slabs)
    public static final Block CABINET_BLOCK = register(new CabinetBlock(), "wall_cabinet_block");
    public static final Block ILLUMINATED_CABINET_BLOCK = register(new IlluminatedCabinetBlock(), "wall_illuminated_cabinet_block");
    public static final Block FLOOR_CABINET_BLOCK = register(new FloorCabinetBlock(), "cabinet_block");
    public static final Block FLOOR_ILLUMINATED_CABINET_BLOCK = register(new FloorIlluminatedCabinetBlock(), "illuminated_cabinet_block");
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
    public static final Block JACK_O_LANTERN_REDSTONE = registerWithItem(new JackOLanternRedStone(), "jack_o_lantern_redstone");
    public static final Block JACK_O_LANTERN_SOUL = registerWithItem(new JackOLanternSoul(), "jack_o_lantern_soul");

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
    public static void init() {}


    protected static Block registerWithItem(Block block, String name) {
        Identifier id = getId(name);
        Registry.register(Registries.BLOCK, id, block);
        Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings()));
        return block;
    }
    protected static Block register(Block block, String name) {
        Registry.register(Registries.BLOCK, getId(name), block);
        return block;
    }

    protected static void registerFlammable(Block block, int burnTime, int spreadSpeed) {
        FlammableBlockRegistry.getDefaultInstance().add(block, burnTime, spreadSpeed);
    }
}
