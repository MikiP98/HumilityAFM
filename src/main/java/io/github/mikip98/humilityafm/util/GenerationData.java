package io.github.mikip98.humilityafm.util;

import io.github.mikip98.humilityafm.util.data_types.BlockStrength;
import io.github.mikip98.humilityafm.util.data_types.Pair;
import net.minecraft.block.Blocks;

import java.util.List;
import java.util.Map;

public class GenerationData {
    // --------- Vanilla Data ---------

    /***
     * Vanilla color pallet for wool, beds, banners, etc.
     */
    public static final String[] vanillaColorPallet = {"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"};
    public static final float vanillaTerracottaHardness = Blocks.WHITE_TERRACOTTA.getHardness();  // 1.25f
    public static final float vanillaTerracottaResistance = Blocks.WHITE_TERRACOTTA.getBlastResistance();  // 4.2f

    /***
     * All vanilla wood types
     */
    public static final String[] vanillaWoodTypes = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "bamboo", "crimson", "warped"};
    public static final float vanillaWoodHardness = Blocks.OAK_PLANKS.getHardness();  // 2.0f;
    public static final float vanillaWoodResistance = Blocks.OAK_PLANKS.getBlastResistance();  // 3.0f;
    // TODO: Split nether and overworld wood types, as nether wood does not burn

    /***
     * All stony vanilla materials that stairs can be made from
     * Sorted by their strength (hardness and resistance)
     */
    public static final List<Pair<BlockStrength, String[]>> vanillaStonyMaterialsPerStrength = List.of(
            Pair.of(BlockStrength.of(0.8F), new String[]{"quartz", "sandstone", "red_sandstone"}),
            Pair.of(BlockStrength.of(1.5F, 3.0F), new String[]{"mud_bricks"}),
            Pair.of(BlockStrength.of(1.5F, 6.0F), new String[]{
                    "blackstone", "andesite", "polished_andesite", "diorite", "polished_diorite", "granite",
                    "polished_granite", "polished_blackstone_brick", "prismarine", "dark_prismarine",
                    "prismarine_bricks", "purpur", "stone", "stone_brick", "mossy_stone_brick"
            }),
            Pair.of(BlockStrength.of(2.0F, 6.0F), new String[]{
                    "brick", "cobblestone", "mossy_cobblestone", "nether_brick", "red_nether_brick",
                    "polished_blackstone", "smooth_quartz", "smooth_sandstone", "smooth_red_sandstone"
            }),
            Pair.of(BlockStrength.of(3.0F, 6.0F), new String[]{"cut_copper", "exposed_cut_copper", "weathered_cut_copper", "oxidized_cut_copper"}),
            Pair.of(BlockStrength.of(3.0F, 9.0F), new String[]{"end_stone_brick"}),
            Pair.of(BlockStrength.of(3.5F, 6.0F), new String[]{"cobbled_deepslate", "polished_deepslate", "deepslate_brick", "deepslate_tile"})
    );

    /***
     * Vanilla metals used for candlesticks that do not rust
     */
    public static final String[] vanillaCandlestickMetals = {"iron", "gold"};
    /***
     * Vanilla metals used for candlesticks that rust
     */
    public static final List<String[]> vanillaRustableCandlestickMetals = List.of(
            new String[][]{{"copper", "exposed_copper", "weathered_copper", "oxidized_copper"}}
    );


    // --------- Modded Data ---------
    /***
     * All modded wood types, sorted by mod id
     */
    public static final Map<String, String[]> moddedWoodTypes = Map.of(
            "betternether", new String[]{"stalagnate", "willow", "wart", "mushroom_fir", "mushroom", "anchor_tree", "nether_sakura"}
    );
}
