package io.github.mikip98.humilityafm.util;

import java.util.Map;

public class GenerationData {
    // --------- Vanilla Data ---------

    /***
     * Vanilla color pallet for wool, beds, banners, etc.
     */
    public static final String[] vanillaColorPallet = {"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"};

    /***
     * All vanilla wood types
     */
    public static final String[] vanillaWoodTypes = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "bamboo", "crimson", "warped"};


    // --------- Modded Data ---------
    /***
     * All modded wood types, sorted by mod id
     */
    public static final Map<String, String[]> moddedWoodTypes = Map.of(
            "betternether", new String[]{"stalagnate", "willow", "wart", "mushroom_fir", "mushroom", "anchor_tree", "nether_sakura"}
    );
}
