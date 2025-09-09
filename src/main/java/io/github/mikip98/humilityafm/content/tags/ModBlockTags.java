package io.github.mikip98.humilityafm.content.tags;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class ModBlockTags {

    // Cabinet Block Tags
    public static final TagKey<Block> CABINET_BLOCKS = TagKey.of(RegistryKeys.BLOCK, getId("cabinets"));
    public static final TagKey<Block> ILLUMINATED_CABINET_BLOCKS = TagKey.of(RegistryKeys.BLOCK, getId("illuminated_cabinets"));

    // Wooden Mosaic Block Tag
    public static final TagKey<Block> WOODEN_MOSAIC_BLOCKS = TagKey.of(RegistryKeys.BLOCK, getId("wooden_mosaics"));

    // Terracotta Tiles Block Tag
    public static final TagKey<Block> TERRACOTTA_TILES_BLOCKS = TagKey.of(RegistryKeys.BLOCK, getId("terracotta_tiles"));

    // Forced Corner Stairs Block Tags
    // Inner Stairs
    public static final TagKey<Block> WOODEN_INNER_STAIRS = TagKey.of(RegistryKeys.BLOCK, getId("wooden_inner_stairs"));
    public static final TagKey<Block> STONY_INNER_STAIRS = TagKey.of(RegistryKeys.BLOCK, getId("stony_inner_stairs"));
    // Outer Stairs
    public static final TagKey<Block> WOODEN_OUTER_STAIRS = TagKey.of(RegistryKeys.BLOCK, getId("wooden_outer_stairs"));
    public static final TagKey<Block> STONY_OUTER_STAIRS = TagKey.of(RegistryKeys.BLOCK, getId("stony_outer_stairs"));

    // Jack o'Lanterns
    public static final TagKey<Block> JACK_O_LANTERNS = TagKey.of(RegistryKeys.BLOCK, getId("jack_o_lanterns"));
    public static final TagKey<Block> SPECIAL_JACK_O_LANTERNS = TagKey.of(RegistryKeys.BLOCK, getId("special_jack_o_lanterns"));
    public static final TagKey<Block> COLOURED_JACK_O_LANTERNS = TagKey.of(RegistryKeys.BLOCK, getId("coloured_jack_o_lanterns"));

    // Candlesticks
    public static final TagKey<Block> CANDLESTICKS = TagKey.of(RegistryKeys.BLOCK, getId("candlesticks"));


    // Vanilla Tags
    public static final TagKey<Block> AXE_MINEABLE = TagKey.of(RegistryKeys.BLOCK, new Identifier("minecraft:mineable/axe"));
    public static final TagKey<Block> PICKAXE_MINEABLE = TagKey.of(RegistryKeys.BLOCK, new Identifier("minecraft:mineable/pickaxe"));
}
