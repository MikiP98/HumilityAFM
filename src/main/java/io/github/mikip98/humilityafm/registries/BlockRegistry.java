package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorIlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.IlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns.JackOLantern;
import io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns.JackOLanternRedStone;
import io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns.JackOLanternSoul;
import io.github.mikip98.humilityafm.content.blocks.stairs.InnerStairs;
import io.github.mikip98.humilityafm.content.blocks.stairs.OuterStairs;
import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import io.github.mikip98.humilityafm.generators.ColouredFeatureSetGenerator;
import io.github.mikip98.humilityafm.generators.CandlestickGenerator;
import io.github.mikip98.humilityafm.generators.ForcedCornerStairsGenerator;
import io.github.mikip98.humilityafm.generators.TerracottaTilesGenerator;
import io.github.mikip98.humilityafm.generators.WoodenMosaicGenerator;
import io.github.mikip98.humilityafm.util.GenerationData;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.Arrays;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;
import static io.github.mikip98.humilityafm.registries.ItemGroupRegistry.putIntoItemGroup;

public class BlockRegistry {

    // Cabinet blocks
    public static final CabinetBlock CABINET_BLOCK = new CabinetBlock();
    public static final IlluminatedCabinetBlock ILLUMINATED_CABINET_BLOCK = new IlluminatedCabinetBlock();
    public static final FloorCabinetBlock FLOOR_CABINET_BLOCK = new FloorCabinetBlock();
    public static final FloorIlluminatedCabinetBlock FLOOR_ILLUMINATED_CABINET_BLOCK = new FloorIlluminatedCabinetBlock();

    // Stairs
    private static final float WoodenStairsBlockStrength = 2.0f;
    private static final AbstractBlock.Settings StairsBlockSettings = AbstractBlock.Settings.create().strength(WoodenStairsBlockStrength).requiresTool();
    public static final Block OUTER_STAIRS = new OuterStairs(StairsBlockSettings);
    public static final Block INNER_STAIRS = new InnerStairs(StairsBlockSettings);

    // Wooden mosaics
    private static final float WoodenMosaicStrength = 3.0f * 1.5f;
    private static final AbstractBlock.Settings WoodenMosaicSettings = AbstractBlock.Settings.create().strength(WoodenMosaicStrength).requiresTool().sounds(BlockSoundGroup.WOOD);
    public static final Block WOODEN_MOSAIC = new Block(WoodenMosaicSettings);

    // Jack o'Lanterns
    public static final Block JACK_O_LANTERN_REDSTONE = new JackOLanternRedStone();
    public static final Block JACK_O_LANTERN_SOUL = new JackOLanternSoul();
    public static Block[] COLOURED_WEAK_JACK_O_LANTERNS;
    public static Block[] COLOURED_JACK_O_LANTERNS;
    public static Block[] COLOURED_STRONG_JACK_O_LANTERNS;


    public static void register() {
        // ............ TEST BLOCKS & BLOCK ITEMS ............
        // Register Cabinets
        register(CABINET_BLOCK, "wall_cabinet_block");
        register(ILLUMINATED_CABINET_BLOCK, "wall_illuminated_cabinet_block");
        register(FLOOR_CABINET_BLOCK, "cabinet_block");
        register(FLOOR_ILLUMINATED_CABINET_BLOCK, "illuminated_cabinet_block");
        // Register Stairs
        registerWithItem(OUTER_STAIRS, "outer_stairs");
        registerWithItem(INNER_STAIRS, "inner_stairs");
        // Register Wooden Mosaic
        registerWithItem(WOODEN_MOSAIC, "wooden_mosaic");


        // ............ FINAL BLOCKS & BLOCK ITEMS ............
        // Special Jack-O-Lanterns
        registerWithItem(JACK_O_LANTERN_REDSTONE, "jack_o_lantern_redstone");
        registerWithItem(JACK_O_LANTERN_SOUL, "jack_o_lantern_soul");
        putIntoItemGroup(JACK_O_LANTERN_REDSTONE, ItemGroups.COLORED_BLOCKS);
        putIntoItemGroup(JACK_O_LANTERN_REDSTONE, ItemGroups.REDSTONE);
        putIntoItemGroup(JACK_O_LANTERN_SOUL, ItemGroups.COLORED_BLOCKS);

        // Register cabinets
        registerArray(
                CabinetBlockGenerator.cabinetBlockVariants,
                CabinetBlockGenerator.cabinetBlockVariantsNames,
                "wall_cabinet_block_"
        );
        registerFlammable(CabinetBlockGenerator.cabinetBlockVariants, ModConfig.cabinetBlockBurnTime, ModConfig.cabinetBlockFireSpread);

        // Register illuminated cabinets
        registerArray(
                CabinetBlockGenerator.illuminatedCabinetBlockVariants,
                CabinetBlockGenerator.cabinetBlockVariantsNames,
                "wall_illuminated_cabinet_block_"
        );
        registerFlammable(CabinetBlockGenerator.illuminatedCabinetBlockVariants, ModConfig.cabinetBlockBurnTime, ModConfig.cabinetBlockFireSpread);

        // Register floor cabinets
        registerArray(
                CabinetBlockGenerator.floorCabinetBlockVariants,
                CabinetBlockGenerator.cabinetBlockVariantsNames,
                "cabinet_block_"
        );
        registerFlammable(CabinetBlockGenerator.floorCabinetBlockVariants, ModConfig.cabinetBlockBurnTime, ModConfig.cabinetBlockFireSpread);

        // Register floor illuminated cabinets
        registerArray(
                CabinetBlockGenerator.floorIlluminatedCabinetBlockVariants,
                CabinetBlockGenerator.cabinetBlockVariantsNames,
                "illuminated_cabinet_block_"
        );
        registerFlammable(CabinetBlockGenerator.floorIlluminatedCabinetBlockVariants, ModConfig.cabinetBlockBurnTime, ModConfig.cabinetBlockFireSpread);

        // Register coloured feature set
        if (ModConfig.enableColouredFeatureSetBeta) {
            // Register Coloured Torches
            registerArrayWithItems(
                    ColouredFeatureSetGenerator.colouredTorchWeakVariants,
                    GenerationData.vanillaColorPallet,
                    "coloured_torch_",
                    "_weak"
            );
            registerArrayWithItems(
                    ColouredFeatureSetGenerator.colouredTorchVariants,
                    GenerationData.vanillaColorPallet,
                    "coloured_torch_"
            );
            registerArrayWithItems(
                    ColouredFeatureSetGenerator.colouredTorchStrongVariants,
                    GenerationData.vanillaColorPallet,
                    "coloured_torch_",
                    "_strong"
            );
            putIntoItemGroup(ColouredFeatureSetGenerator.colouredTorchWeakVariants, ItemGroups.COLORED_BLOCKS);
            putIntoItemGroup(ColouredFeatureSetGenerator.colouredTorchVariants, ItemGroups.COLORED_BLOCKS);
            putIntoItemGroup(ColouredFeatureSetGenerator.colouredTorchStrongVariants, ItemGroups.COLORED_BLOCKS);

            // Register Light Strips
            registerArrayWithItems(
                    ColouredFeatureSetGenerator.LightStripBlockVariants,
                    GenerationData.vanillaColorPallet,
                    "light_strip_"
            );
            putIntoItemGroup(ColouredFeatureSetGenerator.LightStripBlockVariants, ItemGroups.COLORED_BLOCKS);

            // Register coloured Jack o'Lanterns
            COLOURED_WEAK_JACK_O_LANTERNS = Arrays.stream(GenerationData.vanillaColorPallet).map(s -> registerWithItem(new JackOLantern(), "coloured_weak_jack_o_lantern_" + s)).toArray(Block[]::new);
            COLOURED_JACK_O_LANTERNS = Arrays.stream(GenerationData.vanillaColorPallet).map(s -> registerWithItem(new JackOLantern(), "coloured_jack_o_lantern_" + s)).toArray(Block[]::new);
            COLOURED_STRONG_JACK_O_LANTERNS = Arrays.stream(GenerationData.vanillaColorPallet).map(s -> registerWithItem(new JackOLantern(), "coloured_strong_jack_o_lantern_" + s)).toArray(Block[]::new);

        }

        // Register Forced corner stairs
        registerArrayWithItems(
                ForcedCornerStairsGenerator.innerStairsBlockVariants,
                ForcedCornerStairsGenerator.innerOuterStairsBlockVariantsNames,
                "inner_stairs_"
        );
        putIntoItemGroup(ForcedCornerStairsGenerator.innerStairsBlockVariants, ItemGroups.BUILDING_BLOCKS);
        registerArrayWithItems(
                ForcedCornerStairsGenerator.outerStairsBlockVariants,
                ForcedCornerStairsGenerator.innerOuterStairsBlockVariantsNames,
                "outer_stairs_"
        );
        putIntoItemGroup(ForcedCornerStairsGenerator.outerStairsBlockVariants, ItemGroups.BUILDING_BLOCKS);

        // Register Wooden Mosaics
        registerArrayWithItems(
                WoodenMosaicGenerator.woodenMosaicVariants,
                WoodenMosaicGenerator.woodenMosaicVariantsNames,
                "wooden_mosaic_"
        );
        byte burn = (byte) Math.round(5 * ModConfig.mosaicsAndTilesStrengthMultiplayer);  // TODO: Find correct vanilla values
        byte spread = (byte) Math.round(20 / ModConfig.mosaicsAndTilesStrengthMultiplayer);
        registerFlammable(WoodenMosaicGenerator.woodenMosaicVariants, burn, spread);
        putIntoItemGroup(WoodenMosaicGenerator.woodenMosaicVariants, ItemGroups.BUILDING_BLOCKS);

        // Register Terracotta Tiles
        registerArrayWithItems(
                TerracottaTilesGenerator.terracottaTilesVariants,
                TerracottaTilesGenerator.terracottaTilesVariantsNames,
                "terracotta_tiles_"
        );
        putIntoItemGroup(TerracottaTilesGenerator.terracottaTilesVariants, ItemGroups.BUILDING_BLOCKS);

        // Register Candlestick variants
        if (ModConfig.enableCandlestickBeta) {
            registerArray(
                    CandlestickGenerator.candlestickClassicStandingVariants,
                    GenerationData.vanillaCandlestickMetals,
                    "candlestick_"
            );
            registerArray(
                    CandlestickGenerator.candlestickClassicWallVariants,
                    GenerationData.vanillaCandlestickMetals,
                    "candlestick_wall_"
            );
            for (int i = 0; i < CandlestickGenerator.candlestickRustableStandingVariants.size(); i++) {
                registerArray(
                        CandlestickGenerator.candlestickRustableStandingVariants.get(i),
                        GenerationData.vanillaRustableCandlestickMetals.get(i),
                        "candlestick_"
                );
                registerArray(
                        CandlestickGenerator.candlestickRustableWallVariants.get(i),
                        GenerationData.vanillaRustableCandlestickMetals.get(i),
                        "candlestick_wall_"
                );
            }
        }
    }


    protected static void registerArrayWithItems(Block[] blocks, String[] names, String prefix) {
        for (int i = 0; i < blocks.length; i++) {
            registerWithItem(blocks[i], prefix + names[i]);
        }
    }
    protected static void registerArrayWithItems(Block[] blocks, String[] names, String prefix, String suffix) {
        for (int i = 0; i < blocks.length; i++) {
            registerWithItem(blocks[i], prefix + names[i] + suffix);
        }
    }
    protected static Block registerWithItem(Block block, String name) {
        Identifier id = getId(name);
        Registry.register(Registries.BLOCK, id, block);
        Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings()));
        return block;
    }
    protected static void registerArray(Block[] blocks, String[] names, String prefix) {
        for (int i = 0; i < blocks.length; i++) {
            register(blocks[i], prefix + names[i]);
        }
    }
    protected static void register(Block block, String name) {
        Registry.register(Registries.BLOCK, getId(name), block);
    }


    protected static void registerFlammable(Block[] blocks, int burnTime, int spreadSpeed) {
        for (Block block : blocks) {
            FlammableBlockRegistry.getDefaultInstance().add(block, burnTime, spreadSpeed);
        }
    }
}
