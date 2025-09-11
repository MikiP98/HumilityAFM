package io.github.mikip98.humilityafm.util.generation_data;

import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import io.github.mikip98.humilityafm.util.simple_iterables.MultiArrayIterable;
import io.github.mikip98.humilityafm.util.simple_iterables.MultiIterableIterable;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.BlockStrength;
import io.github.mikip98.humilityafm.util.Pair;
import net.minecraft.block.Blocks;

import java.util.*;

import static io.github.mikip98.humilityafm.util.mod_support.SupportedMods.*;

// TODO: Make this stuff protected, so it can only be used in ActiveGenerationData
public abstract class RawGenerationData {
    // --------- Vanilla Data ---------

    /***
     * Vanilla color pallet for wool, beds, banners, etc.
     */
    public static final String[] vanillaColorPallet = {
            "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
            "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"
    };
    public static final float vanillaTerracottaHardness = Blocks.WHITE_TERRACOTTA.getHardness();  // 1.25f
    public static final float vanillaTerracottaResistance = Blocks.WHITE_TERRACOTTA.getBlastResistance();  // 4.2f

    /***
     * All vanilla wood types
     */
    public static final String[] vanillaOverworldWoodTypes = {
            "oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "bamboo"
    };
    public static final String[] vanillaNetherWoodTypes = {"crimson", "warped"};
    public static final MultiArrayIterable<String> vanillaWoodTypes = MultiArrayIterable.of(vanillaOverworldWoodTypes, vanillaNetherWoodTypes);
    public static final float vanillaWoodHardness = Blocks.OAK_PLANKS.getHardness();  // 2.0f;
    public static final float vanillaWoodResistance = Blocks.OAK_PLANKS.getBlastResistance();  // 3.0f;
    public static final int vanillaWoodBurnTime = 5;  // Values taken from FireBlock::registerDefaultFlammables
    public static final int vanillaWoodSpreadSpeed = 20;
    // TODO: Split nether and overworld wood types, as nether wood does not burn

    /***
     * All stony vanilla materials that stairs can be made from
     * Sorted by their strength (hardness and resistance)
     */
    // TODO: Why is this an array of pairs? Shouldn't it be a map?
    //  Array or pairs is faster, though it is a bit ugly.
    @SuppressWarnings("unchecked")
    protected static final Pair<BlockStrength, String[]>[] vanillaStonyMaterialsPerStrength = new Pair[]{
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
            Pair.of(BlockStrength.of(3.0F, 6.0F), new String[]{
                    "cut_copper", "exposed_cut_copper", "weathered_cut_copper", "oxidized_cut_copper"
            }),
            Pair.of(BlockStrength.of(3.0F, 9.0F), new String[]{"end_stone_brick"}),
            Pair.of(BlockStrength.of(3.5F, 6.0F), new String[]{
                    "cobbled_deepslate", "polished_deepslate", "deepslate_brick", "deepslate_tile"
            })
    };
    /***
     * All vanilla stony materials that stairs can be made from in a single iterable.
     */
    protected static final MultiArrayIterable<String> vanillaStonyMaterials = MultiArrayIterable.of(
            Arrays.stream(vanillaStonyMaterialsPerStrength).map(Pair::second).toArray(String[][]::new)
    );

    // TODO: Move to ActiveGenerationData?
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
     * All burnable modded wood types, sorted by the mod they come from.
     */
    public static final Map<SupportedMods, String[]> moddedBurnableWoodTypes = Map.of(
//            BETTER_END, new String[]{
//                    "mossy_glowshroom", "pythadendron"/*, "endlotus"*/, "lacugrove", "dragon_tree", // TODO: 'endlotus' misses its texture
//                    "tenanea", "helix_tree", "umbrella_tree", "jellyshroom", "lucernia"
//            },  // There is no 1.21.4+ version available
            BIOMES_O_PLENTY, new String[]{
                    "fir", "pine", "maple", "redwood", "mahogany", "jacaranda", "palm",
                    "willow", "dead", "magic", "umbran", "hellbark", "empyreal"
            }
    );
    /***
     * All fireproof modded wood types, sorted by the mod they come from.
     */
    public static final Map<SupportedMods, String[]> moddedFireProofWoodTypes = Map.of(
//            BETTER_NETHER, new String[]{  // TODO: 'mushroom' and 'reeds' miss their textures
//                    "anchor_tree"/*, "mushroom"*/, "mushroom_fir", "nether_sakura"/*, "reeds"*/, "rubeus", "stalagnate", "wart", "willow"
//            }  // There is no 1.21.4+ version available
    );
    /***
     * A single iterable containing all modded wood types, both burnable and fireproof.
     */
    protected static final MultiArrayIterable<String> moddedWoodTypes;
    static {
        List<String[]> moddedWoodTypesList = new ArrayList<>();

        moddedWoodTypesList.addAll(moddedBurnableWoodTypes.values());
        moddedWoodTypesList.addAll(moddedFireProofWoodTypes.values());

        moddedWoodTypes = MultiArrayIterable.of(
                moddedWoodTypesList.toArray(String[][]::new)
        );
    }

    // TODO: Modded Stony Materials

    // TODO: Modded Candlestick Metals

    // TODO: Modded Rustable Candlestick Metals


    // --------- Combined Data ---------
    /***
     * A single iterable containing all wood types, both vanilla and modded.
     */
    public static final MultiIterableIterable<String> allWoodTypes = MultiIterableIterable.of(vanillaWoodTypes, moddedWoodTypes);
}
