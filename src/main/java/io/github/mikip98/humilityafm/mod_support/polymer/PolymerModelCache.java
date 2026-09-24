#if POLYMER
package io.github.mikip98.humilityafm.mod_support.polymer;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
#if MC_VERSION < 12104 import eu.pb4.polymer.core.api.item.PolymerItem; #endif
#if MC_VERSION < 12104 import eu.pb4.polymer.resourcepack.api.PolymerModelData; #endif
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.config.enums.PolymerCabinetFallback;
import io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns.JackOLantern;
import io.github.mikip98.humilityafm.content.properties.ModProperties;
import io.github.mikip98.humilityafm.content.properties.enums.CandleColor;
import io.github.mikip98.humilityafm.mod_support.SupportedMods;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
#if MC_VERSION < 12111 import net.minecraft.resources.ResourceLocation; #endif
#if MC_VERSION >= 12111 import net.minecraft.resources.Identifier; #endif
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;

import java.util.*;

import static io.github.mikip98.humilityafm.HumilityAFM.*;

public class PolymerModelCache {
    #if MC_VERSION >= 12104
    private static final Set<#if MC_VERSION < 12111 ResourceLocation #else Identifier #endif> VIRTUAL_ITEM_MODELS_TO_GENERATE = new HashSet<>();
    #endif

    public static void init() {
        #if MC_VERSION >= 12104
        PolymerResourcePackUtils.RESOURCE_PACK_CREATION_EVENT.register(builder -> {
            for (#if MC_VERSION < 12111 ResourceLocation #else Identifier #endif modelId : VIRTUAL_ITEM_MODELS_TO_GENERATE) {
                String json = """
                        {
                            "model": {
                                "type": "minecraft:model",
                                "model": "%s"
                            }
                        }
                        """.formatted(modelId.toString());

                String path = "assets/" + modelId.getNamespace() + "/items/" + modelId.getPath() + ".json";
                builder.addStringData(path, json);
            }
        });
        #endif
    }

    public static final Map<Item, #if MC_VERSION < 12104 PolymerModelData #elif MC_VERSION < 12111 ResourceLocation #else Identifier #endif> POLYMER_ITEM_MODEL_CACHE = new IdentityHashMap<>();

    public static void requestPolymerModel(Item item, String name) {
        #if MC_VERSION < 12104
        if (item instanceof PolymerItem polymerItem) {
            Item disguise = polymerItem.getPolymerItem(PolymerItems.VIRTUAL_ITEM_BASE.getDefaultInstance(), null);
            // TODO: Consider getting rid of the 'var'
            var modelData = PolymerResourcePackUtils.requestModel(
                    disguise, getId("item/" + name)
            );
            if (modelData == null) throw new RuntimeException("Failed to disguise item " + name);
            POLYMER_ITEM_MODEL_CACHE.put(item, modelData);
        } else throw new IllegalStateException("Item is not a PolymerItem");
        #else
        POLYMER_ITEM_MODEL_CACHE.put(item, getId(name));
        #endif
    }


    public static final Map<BlockState, BlockState> POLYMER_BLOCK_CACHE = new IdentityHashMap<>();
    public static final Map<Block, Block> STAIR_BASE_CACHE = new IdentityHashMap<>();
    public static final Map<Block, Map<BlockState, #if MC_VERSION < 12104 PolymerModelData #elif MC_VERSION < 12111 ResourceLocation #else Identifier #endif>> CANDLESTICK_MODEL_CACHE = new IdentityHashMap<>();

    public static final Map<Block, #if MC_VERSION < 12104 PolymerModelData #elif MC_VERSION < 12111 ResourceLocation #else Identifier #endif> CABINET_OPEN_MODELS = new IdentityHashMap<>();
    public static BlockState CABINET_TOP_DISGUISE;
    public static BlockState CABINET_NORTH_DISGUISE;
    public static BlockState CABINET_EAST_DISGUISE;
    public static BlockState CABINET_SOUTH_DISGUISE;
    public static BlockState CABINET_WEST_DISGUISE;
    public static BlockState CABINET_BOTTOM_DISGUISE;

    public static final Map<Block, #if MC_VERSION < 12104 PolymerModelData #elif MC_VERSION < 12111 ResourceLocation #else Identifier #endif> LIGHT_STRIP_INNER_MODELS = new IdentityHashMap<>();
    public static final Map<Block, #if MC_VERSION < 12104 PolymerModelData #elif MC_VERSION < 12111 ResourceLocation #else Identifier #endif > LIGHT_STRIP_OUTER_MODELS = new IdentityHashMap<>();
    public static BlockState LIGHT_STRIP_TOP_DISGUISE;
    public static BlockState LIGHT_STRIP_BOTTOM_DISGUISE;

    protected static final PolymerBlockModel EMPTY_MODEL = PolymerBlockModel.of(getId("block/empty"));
    protected static final Map<BlockModelType, BlockState> EMPTY_STATE_CACHE = new EnumMap<>(BlockModelType.class);

    protected static BlockState getEmptyState(BlockModelType type) {
        if (type == null) return null;
        if (EMPTY_STATE_CACHE.containsKey(type)) return EMPTY_STATE_CACHE.get(type);
        final BlockState state =
                #if MC_VERSION < 12100
                PolymerBlockResourceUtils.requestBlock(type, EMPTY_MODEL);
                #else
                PolymerBlockResourceUtils.requestEmpty(type);
                #endif
        EMPTY_STATE_CACHE.put(type, state);
        return state;
    }

    protected static BlockState resolveFallback(BlockState finalFallback, BlockModelType... fallbackTree) {
        for (BlockModelType type : fallbackTree) {
            final BlockState state = getEmptyState(type);
            if (state != null) return state;
        }
        return finalFallback;
    }

    static {
        BlockState cabinetFinalFallbackTop;
        BlockState cabinetFinalFallbackNorth;
        BlockState cabinetFinalFallbackEast;
        BlockState cabinetFinalFallbackSouth;
        BlockState cabinetFinalFallbackWest;
        BlockState cabinetFinalFallbackBottom;

        if (
                ModConfig.polymerAllowSemiFunctionalCabinetStates &&
                ModConfig.polymerCabinetFinalFallback == PolymerCabinetFallback.GLASS
        ) {
            final BlockState lastFallback = Blocks.GLASS.defaultBlockState();
            cabinetFinalFallbackTop = lastFallback;
            cabinetFinalFallbackNorth = lastFallback;
            cabinetFinalFallbackEast = lastFallback;
            cabinetFinalFallbackSouth = lastFallback;
            cabinetFinalFallbackWest = lastFallback;
            cabinetFinalFallbackBottom = lastFallback;
        } else {
            final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
            final EnumProperty<Half> HALF = BlockStateProperties.HALF;
            final BooleanProperty OPEN = BlockStateProperties.OPEN;
            final BlockState lastFallbackBase = Blocks.OAK_TRAPDOOR.defaultBlockState()
                    .setValue(HALF, Half.BOTTOM)
                    .setValue(OPEN, false);

            cabinetFinalFallbackTop = lastFallbackBase.setValue(HALF, Half.TOP).setValue(OPEN, true);
            cabinetFinalFallbackNorth = lastFallbackBase.setValue(FACING, Direction.NORTH);
            cabinetFinalFallbackEast = lastFallbackBase.setValue(FACING, Direction.EAST);
            cabinetFinalFallbackSouth = lastFallbackBase.setValue(FACING, Direction.SOUTH);
            cabinetFinalFallbackWest = lastFallbackBase.setValue(FACING, Direction.WEST);
            cabinetFinalFallbackBottom = lastFallbackBase.setValue(OPEN, true);
        }

        BlockModelType transparentCabinetFallback =
                ModConfig.polymerAllowSemiFunctionalCabinetStates ? PolymerNullableBlockModelTypes.LEAVES : null;

        CABINET_TOP_DISGUISE = resolveFallback(cabinetFinalFallbackTop,
                PolymerNullableBlockModelTypes.TRAPDOOR_TOP,
                transparentCabinetFallback
        );
        CABINET_NORTH_DISGUISE = resolveFallback(cabinetFinalFallbackNorth,
                PolymerNullableBlockModelTypes.TRAPDOOR_NORTH,
                PolymerNullableBlockModelTypes.DOOR_NORTH,
                transparentCabinetFallback
        );
        CABINET_EAST_DISGUISE = resolveFallback(cabinetFinalFallbackEast,
                PolymerNullableBlockModelTypes.TRAPDOOR_EAST,
                PolymerNullableBlockModelTypes.DOOR_EAST,
                transparentCabinetFallback
        );
        CABINET_SOUTH_DISGUISE = resolveFallback(cabinetFinalFallbackSouth,
                PolymerNullableBlockModelTypes.TRAPDOOR_SOUTH,
                PolymerNullableBlockModelTypes.DOOR_SOUTH,
                transparentCabinetFallback
        );
        CABINET_WEST_DISGUISE = resolveFallback(cabinetFinalFallbackWest,
                PolymerNullableBlockModelTypes.TRAPDOOR_WEST,
                PolymerNullableBlockModelTypes.DOOR_WEST,
                transparentCabinetFallback
        );
        CABINET_BOTTOM_DISGUISE = resolveFallback(cabinetFinalFallbackBottom,
                PolymerNullableBlockModelTypes.TRAPDOOR_BOTTOM,
                transparentCabinetFallback
        );


        BlockModelType vinesLightStripFallback =
                ModConfig.polymerPreferNonCollidingLightstrip ? PolymerNullableBlockModelTypes.VINES : null;

        LIGHT_STRIP_TOP_DISGUISE = resolveFallback(Blocks.GLASS.defaultBlockState(),
                vinesLightStripFallback,
                PolymerNullableBlockModelTypes.TRAPDOOR_TOP,
                PolymerNullableBlockModelTypes.SLAB_TOP

        );
        LIGHT_STRIP_BOTTOM_DISGUISE = resolveFallback(Blocks.GLASS.defaultBlockState(),
                vinesLightStripFallback,
                PolymerNullableBlockModelTypes.TRAPDOOR_BOTTOM,
                PolymerNullableBlockModelTypes.SLAB_BOTTOM
        );
    }

    public static void initLateCache() {
        // --- CANDLESTICKS ---
        if (ModConfig.getEnableCandlestickBeta()) {
            cacheCandlestickBlockItemModels(BlockRegistry.SIMPLE_CANDLESTICK_FLOOR_VARIANTS);
            cacheCandlestickBlockItemModels(BlockRegistry.SIMPLE_CANDLESTICK_WALL_VARIANTS);
            for (Block[] blocks : BlockRegistry.RUSTABLE_CANDLESTICK_FLOOR_VARIANTS) cacheCandlestickBlockItemModels(blocks);
            for (Block[] blocks : BlockRegistry.RUSTABLE_CANDLESTICK_WALL_VARIANTS) cacheCandlestickBlockItemModels(blocks);
        }

        // --- CABINETS ---
        cacheCabinetOpenModels(BlockRegistry.WALL_CABINET_BLOCK_VARIANTS);
        cacheCabinetOpenModels(BlockRegistry.FLOOR_CABINET_BLOCK_VARIANTS);
        cacheCabinetOpenModels(BlockRegistry.WALL_ILLUMINATED_CABINET_BLOCK_VARIANTS);
        cacheCabinetOpenModels(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK_VARIANTS);

        // --- LIGHT STRIPS ---
        if (ModConfig.getEnableColouredFeatureSetBeta()) {
            if (BlockRegistry.LIGHT_STRIP_VARIANTS != null) {
                for (Block block : BlockRegistry.LIGHT_STRIP_VARIANTS) {
                    cacheLightStripModels(block, BuiltInRegistries.BLOCK.getKey(block).getPath());
                }
            }
        }

        // ---- JACK O'LANTERNS ---
        cachedJackOLanternBlockStates(BlockRegistry.JACK_O_LANTERN_REDSTONE);
        cachedJackOLanternBlockStates(BlockRegistry.JACK_O_LANTERN_SOUL);
        if (ModConfig.getEnableColouredFeatureSetBeta()) {
            for (Block block : BlockRegistry.COLOURED_JACK_O_LANTERNS) cachedJackOLanternBlockStates(block);
        }

        // Note: Forced Corner Stairs are done in 'BlockGeneration' as they require material data
    }

    protected static void cacheCandlestickBlockItemModels(Block[] blocks) {
        for (Block block : blocks) cacheCandlestickModels(block, BuiltInRegistries.BLOCK.getKey(block).getPath());
    }

    protected static void cacheCabinetOpenModels(Block[] blocks) {
        for (Block block : blocks) cacheOpenCabinetModel(block, BuiltInRegistries.BLOCK.getKey(block).getPath());
    }

    public static void cachedJackOLanternBlockStates(Block block) {
        final String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        if (ModConfig.polymerAllowOptimisedJackOLanterns && block instanceof JackOLantern) {
            for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                final BlockState mappedState = PolymerBlockResourceUtils.requestBlock(
                        BlockModelType.FULL_BLOCK,
                        PolymerUtil.getModelFromBlockstateVariant(name, state)
                );
                if (mappedState != null) POLYMER_BLOCK_CACHE.put(state, mappedState);
            }
        }
    }

    public static void cacheOpenCabinetModel(Block block, String name) {
        #if MC_VERSION < 12111 ResourceLocation #else Identifier #endif openModelId;
        try {
            openModelId = PolymerUtil.getRawModelIdFromBlockstateVariant(name, "facing=north,open=true");
        } catch (IllegalStateException e) {
            openModelId = PolymerUtil.getRawModelIdFromBlockstateVariant(name, "facing=north,half=bottom,open=true");
        }
        #if MC_VERSION < 12104
        final PolymerModelData data = PolymerResourcePackUtils.requestModel(PolymerItems.VIRTUAL_ITEM_BASE, openModelId);
        CABINET_OPEN_MODELS.put(block, data);
        #else
        VIRTUAL_ITEM_MODELS_TO_GENERATE.add(openModelId);
        CABINET_OPEN_MODELS.put(block, openModelId);
        #endif
    }

    public static void cacheStairBase(Block customStair, SupportedMods mod, String material) {
        if (material.endsWith("bricks")) material = material.substring(0, material.length() - 1);
        final String stairPath = material + "_stairs";
        final Block base = BuiltInRegistries.BLOCK.get(getVMId(mod, stairPath)) #if MC_VERSION >= 12104 .get().value() #endif;  // TODO: Consider adding an util method in HumilityVAL
        if (base == Blocks.AIR) throw new IllegalArgumentException(
                "Custom stair block is not found -> " + (mod == null ? "minecraft" : mod.modId) + ":" + stairPath
        );
        STAIR_BASE_CACHE.put(customStair, base);
    }

    public static void cacheLightStripModels(Block customStrip, String blockstateName) {
        final #if MC_VERSION < 12111 ResourceLocation #else Identifier #endif innerId =
                PolymerUtil.getRawModelIdFromBlockstateVariant(blockstateName, "facing=south,half=bottom,shape=inner_left");
        final #if MC_VERSION < 12111 ResourceLocation #else Identifier #endif outerId =
                PolymerUtil.getRawModelIdFromBlockstateVariant(blockstateName, "facing=south,half=bottom,shape=outer_left");

        #if MC_VERSION < 12104
        final PolymerModelData innerData = PolymerResourcePackUtils.requestModel(PolymerItems.VIRTUAL_ITEM_BASE, innerId);
        final PolymerModelData outerData = PolymerResourcePackUtils.requestModel(PolymerItems.VIRTUAL_ITEM_BASE, outerId);

        LIGHT_STRIP_INNER_MODELS.put(customStrip, innerData);
        LIGHT_STRIP_OUTER_MODELS.put(customStrip, outerData);
        #else
        VIRTUAL_ITEM_MODELS_TO_GENERATE.add(innerId);
        VIRTUAL_ITEM_MODELS_TO_GENERATE.add(outerId);

        LIGHT_STRIP_INNER_MODELS.put(customStrip, innerId);
        LIGHT_STRIP_OUTER_MODELS.put(customStrip, outerId);
        #endif
    }

    public static void cacheCandlestickModels(Block customCandlestick, String blockstateName) {
        final Map<BlockState, #if MC_VERSION < 12104 PolymerModelData #elif MC_VERSION < 12111 ResourceLocation #else Identifier #endif> stateMap = new IdentityHashMap<>();

        final Item disguiseItem = PolymerItems.VIRTUAL_ITEM_BASE;

        boolean isWall = blockstateName.startsWith("candlestick_wall_");
        String material = isWall ? blockstateName.substring(17) : blockstateName.substring(12);

        for (BlockState state : customCandlestick.getStateDefinition().getPossibleStates()) {
            // Build the base path: block/candlestick/standing/iron/candlestick_iron
            String path = "block/candlestick/" + (isWall ? "wall/" : "standing/") + material + "/" + blockstateName;

            // Append the colour and lit states
            final CandleColor color = state.getValue(ModProperties.CANDLE_COLOR);
            if (color != CandleColor.NONE) {
                final String colorName = color.getSerializedName();
                if (colorName.equals("plain")) {
                    path += "_candle";
                } else {
                    path += "_" + colorName;
                }

                if (state.getValue(BlockStateProperties.LIT)) {
                    path += "_lit";
                }
            }

            final #if MC_VERSION < 12111 ResourceLocation #else Identifier #endif modelId = getId(path);
            #if MC_VERSION < 12104
            PolymerModelData modelData = PolymerResourcePackUtils.requestModel(disguiseItem, modelId);
            stateMap.put(state, modelData);
            #else
            VIRTUAL_ITEM_MODELS_TO_GENERATE.add(modelId);
            stateMap.put(state, modelId);
            #endif
        }

        CANDLESTICK_MODEL_CACHE.put(customCandlestick, stateMap);
    }
}
#endif