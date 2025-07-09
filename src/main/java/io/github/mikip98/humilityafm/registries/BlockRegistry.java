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
import io.github.mikip98.humilityafm.generators.ForcedCornerStairsGenerator;
import io.github.mikip98.humilityafm.generators.TerracottaTilesGenerator;
import io.github.mikip98.humilityafm.generators.WoodenMosaicGenerator;
import io.github.mikip98.humilityafm.util.GenerationData;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;

import java.util.Arrays;
import java.util.function.Function;

import static io.github.mikip98.humilityafm.HumilityAFM.LOGGER;
import static io.github.mikip98.humilityafm.HumilityAFM.getId;
import static io.github.mikip98.humilityafm.registries.ItemGroupRegistry.putIntoItemGroup;

public class BlockRegistry {

    // Cabinet blocks
    public static final Block
            CABINET_BLOCK = register("wall_cabinet_block", CabinetBlock::new, CabinetBlock.defaultSettings),
            ILLUMINATED_CABINET_BLOCK = register("wall_illuminated_cabinet_block", IlluminatedCabinetBlock::new, IlluminatedCabinetBlock.defaultSettings),
            FLOOR_CABINET_BLOCK = register("cabinet_block", FloorCabinetBlock::new, FloorCabinetBlock.defaultSettings),
            FLOOR_ILLUMINATED_CABINET_BLOCK = register("illuminated_cabinet_block", FloorIlluminatedCabinetBlock::new, FloorIlluminatedCabinetBlock.defaultSettings);

    // Stairs
    private static final float WoodenStairsBlockStrength = 2.0f;
    private static final Block.Settings StairsBlockSettings = Block.Settings.create().strength(WoodenStairsBlockStrength).requiresTool();
    public static final Block OUTER_STAIRS = registerWithItem("outer_stairs", OuterStairs::new, StairsBlockSettings);
    public static final Block INNER_STAIRS = registerWithItem("inner_stairs", InnerStairs::new, StairsBlockSettings);

    // Wooden mosaics
    private static final float WoodenMosaicStrength = 3.0f * 1.5f;
    private static final Block.Settings WoodenMosaicSettings = Block.Settings.create().strength(WoodenMosaicStrength).requiresTool().sounds(BlockSoundGroup.WOOD);
    public static final Block WOODEN_MOSAIC = registerWithItem("wooden_mosaic", Block::new, WoodenMosaicSettings);

    // Jack o'Lanterns
    public static final Block JACK_O_LANTERN_REDSTONE = registerWithItem("jack_o_lantern_redstone", JackOLanternRedStone::new, JackOLanternRedStone.defaultSettings);
    public static final Block JACK_O_LANTERN_SOUL = registerWithItem("jack_o_lantern_soul", JackOLanternSoul::new, JackOLanternSoul.defaultSettings);
    public static Block[] COLOURED_WEAK_JACK_O_LANTERNS;
    public static Block[] COLOURED_JACK_O_LANTERNS;
    public static Block[] COLOURED_STRONG_JACK_O_LANTERNS;


    public static void register() {

        // ............ FINAL BLOCKS & BLOCK ITEMS ............
        // Special Jack-O-Lanterns
        putIntoItemGroup(JACK_O_LANTERN_REDSTONE, ItemGroups.COLORED_BLOCKS);
        putIntoItemGroup(JACK_O_LANTERN_REDSTONE, ItemGroups.REDSTONE);
        putIntoItemGroup(JACK_O_LANTERN_SOUL, ItemGroups.COLORED_BLOCKS);

        // Register cabinets
        registerFlammable(CabinetBlockGenerator.cabinetBlockVariants, ModConfig.cabinetBlockBurnTime, ModConfig.cabinetBlockFireSpread);

        // Register illuminated cabinets
        registerFlammable(CabinetBlockGenerator.illuminatedCabinetBlockVariants, ModConfig.cabinetBlockBurnTime, ModConfig.cabinetBlockFireSpread);

        // Register floor cabinets
        registerFlammable(CabinetBlockGenerator.floorCabinetBlockVariants, ModConfig.cabinetBlockBurnTime, ModConfig.cabinetBlockFireSpread);

        // Register floor illuminated cabinets
        registerFlammable(CabinetBlockGenerator.floorIlluminatedCabinetBlockVariants, ModConfig.cabinetBlockBurnTime, ModConfig.cabinetBlockFireSpread);

        // Register coloured feature set
        LOGGER.info("ModConfig.enableColouredFeatureSetBeta -> {}", ModConfig.enableColouredFeatureSetBeta);
        if (ModConfig.enableColouredFeatureSetBeta) {
            // Register Coloured Torches
            putIntoItemGroup(ColouredFeatureSetGenerator.colouredTorchWeakVariants, ItemGroups.COLORED_BLOCKS);
            putIntoItemGroup(ColouredFeatureSetGenerator.colouredTorchVariants, ItemGroups.COLORED_BLOCKS);
            putIntoItemGroup(ColouredFeatureSetGenerator.colouredTorchStrongVariants, ItemGroups.COLORED_BLOCKS);

            // Register Light Strips
            putIntoItemGroup(ColouredFeatureSetGenerator.LightStripBlockVariants, ItemGroups.COLORED_BLOCKS);

            // Register coloured Jack o'Lanterns
            COLOURED_WEAK_JACK_O_LANTERNS = Arrays.stream(GenerationData.vanillaColorPallet).map(s -> registerWithItem("coloured_weak_jack_o_lantern_" + s, JackOLantern::new, JackOLantern.defaultSettings)).toArray(Block[]::new);
            COLOURED_JACK_O_LANTERNS = Arrays.stream(GenerationData.vanillaColorPallet).map(s -> registerWithItem("coloured_jack_o_lantern_" + s, JackOLantern::new, JackOLantern.defaultSettings)).toArray(Block[]::new);
            COLOURED_STRONG_JACK_O_LANTERNS = Arrays.stream(GenerationData.vanillaColorPallet).map(s -> registerWithItem("coloured_strong_jack_o_lantern_" + s, JackOLantern::new, JackOLantern.defaultSettings)).toArray(Block[]::new);
        }

        // Register Forced corner stairs
        putIntoItemGroup(ForcedCornerStairsGenerator.innerStairsBlockVariants, ItemGroups.BUILDING_BLOCKS);
        putIntoItemGroup(ForcedCornerStairsGenerator.outerStairsBlockVariants, ItemGroups.BUILDING_BLOCKS);

        // Register Wooden Mosaics
        byte burn = (byte) Math.round(5 * ModConfig.mosaicsAndTilesStrengthMultiplayer);  // TODO: Find correct vanilla values
        byte spread = (byte) Math.round(20 / ModConfig.mosaicsAndTilesStrengthMultiplayer);
        registerFlammable(WoodenMosaicGenerator.woodenMosaicVariants, burn, spread);
        putIntoItemGroup(WoodenMosaicGenerator.woodenMosaicVariants, ItemGroups.BUILDING_BLOCKS);

        // Register Terracotta Tiles
        putIntoItemGroup(TerracottaTilesGenerator.terracottaTilesVariants, ItemGroups.BUILDING_BLOCKS);
    }

    public static Block registerWithItem(String name, Function<AbstractBlock.Settings, Block> blockFactory, Block.Settings settings) {
        Block block = Blocks.register(keyOfBlock(name), blockFactory, settings);
        ItemRegistry.register(name, (itemSettings) -> new BlockItem(block, itemSettings));
        return block;
    }

    public static Block register(String name, Function<Block.Settings, Block> blockFactory, Block.Settings settings) {
        return Blocks.register(keyOfBlock(name), blockFactory, settings);
    }

    public static RegistryKey<Block> keyOfBlock(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, getId(name));
    }

    protected static void registerFlammable(Block[] blocks, int burnTime, int spreadSpeed) {
        for (Block block : blocks) {
            FlammableBlockRegistry.getDefaultInstance().add(block, burnTime, spreadSpeed);
        }
    }
}
