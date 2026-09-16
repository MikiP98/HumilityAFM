#if POLYMER
package io.github.mikip98.humilityafm.mod_support.polymer;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerModelData;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;

import static io.github.mikip98.humilityafm.HumilityAFM.*;

public class PolymerModelCache {
    public static void init() {}

    public static final Map<Item, PolymerModelData> POLYMER_ITEM_MODEL_CACHE = new IdentityHashMap<>();

    public static void requestPolymerModel(Item item, String name) {
        if (item instanceof PolymerItem polymerItem) {
            Item disguise = polymerItem.getPolymerItem(PolymerItems.VIRTUAL_ITEM_BASE.getDefaultInstance(), null);
            var modelData = PolymerResourcePackUtils.requestModel(
                    disguise, getId("item/" + name)
            );
            if (modelData == null) throw new RuntimeException("Failed to disguise item " + name);
            POLYMER_ITEM_MODEL_CACHE.put(item, modelData);
        } else throw new IllegalStateException("Item is not a PolymerItem");
    }


    public static final Map<BlockState, BlockState> POLYMER_BLOCK_CACHE = new IdentityHashMap<>();
    public static final Map<Block, Block> STAIR_BASE_CACHE = new IdentityHashMap<>();
    public static final Map<Block, Map<BlockState, PolymerModelData>> CANDLESTICK_MODEL_CACHE = new IdentityHashMap<>();

    public static final Map<Block, PolymerModelData> CABINET_OPEN_MODELS = new IdentityHashMap<>();
    public static BlockState CABINET_TOP_DISGUISE = null;
    public static BlockState CABINET_NORTH_DISGUISE = null;
    public static BlockState CABINET_EAST_DISGUISE = null;
    public static BlockState CABINET_SOUTH_DISGUISE = null;
    public static BlockState CABINET_WEST_DISGUISE = null;
    public static BlockState CABINET_BOTTOM_DISGUISE = null;

    public static final Map<Block, PolymerModelData> LIGHT_STRIP_INNER_MODELS = new IdentityHashMap<>();
    public static final Map<Block, PolymerModelData> LIGHT_STRIP_OUTER_MODELS = new IdentityHashMap<>();
    public static BlockState LIGHT_STRIP_TOP_DISGUISE = null;
    public static BlockState LIGHT_STRIP_BOTTOM_DISGUISE = null;

    static {
        final PolymerBlockModel emptyModel = PolymerBlockModel.of(getId("block/empty"));

        // TODO: For all the below use .requestEmpty() instead of .requestBlock() when available
        final Function<BlockModelType, BlockState> remapper =
                (type) -> PolymerBlockResourceUtils.requestBlock(type, emptyModel);

        // TODO: Try BlockModelType.{dir}_TRAPDOOR first when available
        // TODO: Try BlockModelType.{dir}_DOOR first when available
        if (ModConfig.polymerAllowSemiFunctionalCabinetStates) {
            if (
                    CABINET_TOP_DISGUISE == null ||
                            CABINET_NORTH_DISGUISE == null ||
                            CABINET_EAST_DISGUISE == null ||
                            CABINET_SOUTH_DISGUISE == null ||
                            CABINET_WEST_DISGUISE == null ||
                            CABINET_BOTTOM_DISGUISE == null
            ) {
                final BlockState mappedState = remapper.apply(BlockModelType.TRANSPARENT_BLOCK);
                if (CABINET_TOP_DISGUISE == null) CABINET_TOP_DISGUISE = mappedState;
                if (CABINET_NORTH_DISGUISE == null) CABINET_NORTH_DISGUISE = mappedState;
                if (CABINET_EAST_DISGUISE == null) CABINET_EAST_DISGUISE = mappedState;
                if (CABINET_SOUTH_DISGUISE == null) CABINET_SOUTH_DISGUISE = mappedState;
                if (CABINET_WEST_DISGUISE == null) CABINET_WEST_DISGUISE = mappedState;
                if (CABINET_BOTTOM_DISGUISE == null) CABINET_BOTTOM_DISGUISE = mappedState;
            }
        }
        if (
                ModConfig.polymerAllowSemiFunctionalCabinetStates &&
                        ModConfig.polymerCabinetFinalFallback == PolymerCabinetFallback.GLASS
        ) {
            final BlockState lastFallback = Blocks.GLASS.defaultBlockState();
            if (CABINET_TOP_DISGUISE == null) CABINET_TOP_DISGUISE = lastFallback;
            if (CABINET_NORTH_DISGUISE == null) CABINET_NORTH_DISGUISE = lastFallback;
            if (CABINET_EAST_DISGUISE == null) CABINET_EAST_DISGUISE = lastFallback;
            if (CABINET_SOUTH_DISGUISE == null) CABINET_SOUTH_DISGUISE = lastFallback;
            if (CABINET_WEST_DISGUISE == null) CABINET_WEST_DISGUISE = lastFallback;
            if (CABINET_BOTTOM_DISGUISE == null) CABINET_BOTTOM_DISGUISE = lastFallback;
        } else {
            final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
            final EnumProperty<Half> HALF = BlockStateProperties.HALF;
            final BooleanProperty OPEN = BlockStateProperties.OPEN;
            final BlockState lastFallbackBase = Blocks.OAK_TRAPDOOR.defaultBlockState()
                    .setValue(HALF, Half.BOTTOM)
                    .setValue(OPEN, false);
            if (CABINET_TOP_DISGUISE == null) CABINET_TOP_DISGUISE = lastFallbackBase.setValue(HALF, Half.TOP).setValue(OPEN, true);
            if (CABINET_NORTH_DISGUISE == null) CABINET_NORTH_DISGUISE = lastFallbackBase.setValue(FACING, Direction.NORTH);
            if (CABINET_EAST_DISGUISE == null) CABINET_EAST_DISGUISE = lastFallbackBase.setValue(FACING, Direction.EAST);
            if (CABINET_SOUTH_DISGUISE == null) CABINET_SOUTH_DISGUISE = lastFallbackBase.setValue(FACING, Direction.SOUTH);
            if (CABINET_WEST_DISGUISE == null) CABINET_WEST_DISGUISE = lastFallbackBase.setValue(FACING, Direction.WEST);
            if (CABINET_BOTTOM_DISGUISE == null) CABINET_BOTTOM_DISGUISE = lastFallbackBase.setValue(OPEN, true);
        }

        final boolean trapdoor = false; // TODO: #if the correct MC version
        if (ModConfig.polymerPreferNonCollidingLightstrip || !trapdoor) {
            final BlockState mappedState = remapper.apply(BlockModelType.VINES_BLOCK);
            LIGHT_STRIP_TOP_DISGUISE = mappedState;
            LIGHT_STRIP_BOTTOM_DISGUISE = mappedState;
        }
        // TODO: Trapdoor when available
//        if (LIGHT_STRIP_TOP_DISGUISE == null) LIGHT_STRIP_TOP_DISGUISE = remapper.apply(BlockModelType.TOP_TRAPDOOR);
//        if (LIGHT_STRIP_BOTTOM_DISGUISE == null) LIGHT_STRIP_BOTTOM_DISGUISE = remapper.apply(BlockModelType.BOTTOM_TRAPDOOR);
        // TODO: Slab when available
//        if (LIGHT_STRIP_TOP_DISGUISE == null) LIGHT_STRIP_TOP_DISGUISE = remapper.apply(BlockModelType.TOP_SLAB);
//        if (LIGHT_STRIP_BOTTOM_DISGUISE == null) LIGHT_STRIP_BOTTOM_DISGUISE = remapper.apply(BlockModelType.BOTTOM_SLAB);
        if (LIGHT_STRIP_TOP_DISGUISE == null) LIGHT_STRIP_TOP_DISGUISE = Blocks.GLASS.defaultBlockState();
        if (LIGHT_STRIP_BOTTOM_DISGUISE == null) LIGHT_STRIP_BOTTOM_DISGUISE = Blocks.GLASS.defaultBlockState();
    }

    public static void initLateCache() {
        // --- CANDLESTICKS ---
        cacheCandlestickBlockItemModels(BlockRegistry.SIMPLE_CANDLESTICK_FLOOR_VARIANTS);
        cacheCandlestickBlockItemModels(BlockRegistry.SIMPLE_CANDLESTICK_WALL_VARIANTS);
        for (Block[] blocks : BlockRegistry.RUSTABLE_CANDLESTICK_FLOOR_VARIANTS) cacheCandlestickBlockItemModels(blocks);
        for (Block[] blocks : BlockRegistry.RUSTABLE_CANDLESTICK_WALL_VARIANTS) cacheCandlestickBlockItemModels(blocks);

        // --- CABINETS ---
        cacheCabinetOpenModels(BlockRegistry.WALL_CABINET_BLOCK_VARIANTS);
        cacheCabinetOpenModels(BlockRegistry.FLOOR_CABINET_BLOCK_VARIANTS);
        cacheCabinetOpenModels(BlockRegistry.WALL_ILLUMINATED_CABINET_BLOCK_VARIANTS);
        cacheCabinetOpenModels(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK_VARIANTS);

        // --- LIGHT STRIPS ---
        if (BlockRegistry.LIGHT_STRIP_VARIANTS != null) {
            for (Block block : BlockRegistry.LIGHT_STRIP_VARIANTS) {
                cacheLightStripModels(block, BuiltInRegistries.BLOCK.getKey(block).getPath());
            }
        }

        // ---- JACK O'LANTERNS ---
        cachedJackOLanternBlockStates(BlockRegistry.JACK_O_LANTERN_REDSTONE);
        cachedJackOLanternBlockStates(BlockRegistry.JACK_O_LANTERN_SOUL);
        for (Block block : BlockRegistry.COLOURED_JACK_O_LANTERNS) cachedJackOLanternBlockStates(block);

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
        final PolymerModelData data = PolymerResourcePackUtils.requestModel(PolymerItems.VIRTUAL_ITEM_BASE, openModelId);
        CABINET_OPEN_MODELS.put(block, data);
    }

    public static void cacheStairBase(Block customStair, SupportedMods mod, String material) {
        if (material.endsWith("bricks")) material = material.substring(0, material.length() - 1);
        final String stairPath = material + "_stairs";
        final Block base = BuiltInRegistries.BLOCK.get(getVMId(mod, stairPath));
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

        final PolymerModelData innerData = PolymerResourcePackUtils.requestModel(PolymerItems.VIRTUAL_ITEM_BASE, innerId);
        final PolymerModelData outerData = PolymerResourcePackUtils.requestModel(PolymerItems.VIRTUAL_ITEM_BASE, outerId);

        LIGHT_STRIP_INNER_MODELS.put(customStrip, innerData);
        LIGHT_STRIP_OUTER_MODELS.put(customStrip, outerData);
    }

    public static void cacheCandlestickModels(Block customCandlestick, String blockstateName) {
        final Map<BlockState, PolymerModelData> stateMap = new IdentityHashMap<>();

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
            PolymerModelData modelData = PolymerResourcePackUtils.requestModel(disguiseItem, modelId);
            stateMap.put(state, modelData);
        }

        CANDLESTICK_MODEL_CACHE.put(customCandlestick, stateMap);
    }
}
#endif