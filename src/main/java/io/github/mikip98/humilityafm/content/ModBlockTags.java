package io.github.mikip98.humilityafm.content;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class ModBlockTags {

    // Vanilla Cabinet Block Tags
    public static final TagKey<Block> CABINET_BLOCKS = TagKey.of(RegistryKeys.BLOCK, getId("cabinet_blocks"));
    public static final TagKey<Block> ILLUMINATED_CABINET_BLOCKS = TagKey.of(RegistryKeys.BLOCK, getId("illuminated_cabinet_blocks"));
    // Modded Cabinet Block Tags
    public static final TagKey<Block> BETTER_NETHER_CABINET_BLOCKS = TagKey.of(RegistryKeys.BLOCK, getId("better_nether_cabinet_blocks"));
    public static final TagKey<Block> BETTER_END_CABINET_BLOCKS = TagKey.of(RegistryKeys.BLOCK, getId("better_end_cabinet_blocks"));

    // Wooden Mosaic Block Tag
    public static final TagKey<Block> WOODEN_MOSAIC_BLOCKS = TagKey.of(RegistryKeys.BLOCK, getId("wooden_mosaic_blocks"));

    // Terracotta Tiles Block Tag
    public static final TagKey<Block> TERRACOTTA_TILES_BLOCKS = TagKey.of(RegistryKeys.BLOCK, getId("terracotta_tiles_blocks"));

    // Inner & Outer Stairs Block Tags
    // Inner Stairs
    public static final TagKey<Block> WOODEN_INNER_STAIRS = TagKey.of(RegistryKeys.BLOCK, getId("wooden_inner_stairs"));
    public static final TagKey<Block> STONE_INNER_STAIRS = TagKey.of(RegistryKeys.BLOCK, getId("stone_inner_stairs"));
    // Outer Stairs
    public static final TagKey<Block> WOODEN_OUTER_STAIRS = TagKey.of(RegistryKeys.BLOCK, getId("wooden_outer_stairs"));
    public static final TagKey<Block> STONE_OUTER_STAIRS = TagKey.of(RegistryKeys.BLOCK, getId("stone_outer_stairs"));

    // Jack o'Lanterns
    public static final TagKey<Block> JACK_O_LANTERNS = TagKey.of(RegistryKeys.BLOCK, getId("jack_o_lanterns"));
    public static final TagKey<Block> COLOURED_JACK_O_LANTERNS = TagKey.of(RegistryKeys.BLOCK, getId("coloured_jack_o_lanterns"));

    // Candlesticks
    public static final TagKey<Block> CANDLESTICKS = TagKey.of(RegistryKeys.BLOCK, getId("candlesticks"));


    // Vanilla Tags
    public static final TagKey<Block> AXE_MINEABLE = TagKey.of(RegistryKeys.BLOCK, Identifier.of("minecraft:mineable/axe"));
    public static final TagKey<Block> PICKAXE_MINEABLE = TagKey.of(RegistryKeys.BLOCK, Identifier.of("minecraft:mineable/pickaxe"));
}
