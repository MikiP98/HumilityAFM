#if POLYMER
package io.github.mikip98.humilityafm.registries;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.ChunkAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.config.enums.PolymerCabinetFallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;

import static io.github.mikip98.humilityafm.HumilityAFM.*;

public class Polymer {
    public static final Map<Item, PolymerModelData> POLYMER_ITEM_MODEL_CACHE = new IdentityHashMap<>();
    public static final Map<BlockState, BlockState> POLYMER_BLOCK_CACHE = new IdentityHashMap<>();
    public static final Map<Block, PolymerModelData> CABINET_OPEN_MODELS = new IdentityHashMap<>();

    public static BlockState CABINET_TOP_DISGUISE = null;
    public static BlockState CABINET_NORTH_DISGUISE = null;
    public static BlockState CABINET_EAST_DISGUISE = null;
    public static BlockState CABINET_SOUTH_DISGUISE = null;
    public static BlockState CABINET_WEST_DISGUISE = null;
    public static BlockState CABINET_BOTTOM_DISGUISE = null;

    static {
        PolymerBlockModel emptyModel = PolymerBlockModel.of(getId("block/empty"));

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
    }


    public static void init() {}


    public static void requestPolymerModel(Item item, String name) {
        if (item instanceof PolymerItem polymerItem) {
            Item disguise = polymerItem.getPolymerItem(item.getDefaultInstance(), null);
            var modelData = PolymerResourcePackUtils.requestModel(
                    disguise, getId("item/" + name)
            );
            POLYMER_ITEM_MODEL_CACHE.put(item, modelData);
        } else throw new IllegalStateException("Item is not a PolymerItem");
    }

    public static class PolymerItemImpl extends SimplePolymerItem {
        public PolymerItemImpl(Properties properties) {
            super(properties, Items.GLOWSTONE_DUST);
        }
        @Override
        public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayer player) {
            return POLYMER_ITEM_MODEL_CACHE.get(this).value();
        }
    }

    public static class PolymerBlockItemImpl extends BlockItem implements PolymerItem {
        public PolymerBlockItemImpl(Block block, Properties properties) {
            super(block, properties);
        }
        @Override
        public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayer player) {
            if (this.getBlock() instanceof PolymerBlock polymerBlock) {
                return polymerBlock.getPolymerBlock(this.getBlock().defaultBlockState()).asItem();
            }
            throw new IllegalStateException("Block is not a PolymerBlock");
        }
        @Override
        public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayer player) {
            return Polymer.POLYMER_ITEM_MODEL_CACHE.get(this).value();
        }
    }


    public static BlockState requestBlockState(String name) {
        return PolymerBlockResourceUtils.requestBlock(
                BlockModelType.FULL_BLOCK,
                getModelFromBlockstate(name)
        );
    }

//    public static void setCachedBlockState(Block block, String name) {
//        if (ModConfig.polymerAllowOptimisedJackOLanterns && block instanceof PolymerTexturedBlock && !(block instanceof PureTexturedBlock)) {
//            final BlockState mappedState = requestBlockState(name);
//            if (mappedState != null) POLYMER_BLOCK_CACHE.put(block, mappedState);
//        }
//    }
//    public static void setCachedBlockState(Block block, String name) {
//        if (ModConfig.polymerAllowOptimisedJackOLanterns && block instanceof PolymerTexturedBlock && !(block instanceof PureTexturedBlock)) {
//            for (BlockState state : block.getStateDefinition().getPossibleStates()) {
//                String variantString = getVariantStringFromState(state);
//
//                BlockState mappedState = PolymerBlockResourceUtils.requestBlock(
//                        BlockModelType.FULL_BLOCK,
//                        PolymerBlockModel.of(getIdRaw(getModelFromBlockstateVariant(name, variantString)))
//                );
//
//                if (mappedState != null) {
//                    POLYMER_BLOCK_CACHE.put(state, mappedState); // Cache state-to-state!
//                }
//            }
//        }
//    }
    public static void setCachedBlockState(Block block, String name) {
        if (ModConfig.polymerAllowOptimisedJackOLanterns && block instanceof PolymerTexturedBlock && !(block instanceof PureTexturedBlock)) {
            for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                BlockState mappedState = PolymerBlockResourceUtils.requestBlock(
                        BlockModelType.FULL_BLOCK,
                        getModelFromBlockstateVariant(name, state)
                );
                if (mappedState != null) {
                    POLYMER_BLOCK_CACHE.put(state, mappedState); // Cache state-to-state!
                }
            }
        }
    }

    public static Block createTexturedBlock(BlockBehaviour.Properties properties, Block disguise, String name) {
        final BlockState mappedState = ModConfig.polymerAllowOptimisedMosaicsAndTiles ? requestBlockState(name) : null;

        if (mappedState != null) {
            return new PureTexturedBlock(properties, mappedState);
        } else {
            return new FallbackTexturedBlock(properties, disguise);
        }
    }

    protected static PolymerBlockModel getModelFromBlockstate(String name) {
        return getModelFromBlockstateVariant(name, (BlockState) null);
    }

    protected static String getModelFromBlockstateVariant(String name, String variantString) {
        final InputStream stream = Polymer.class.getResourceAsStream("/assets/humility-afm/blockstates/" + name + ".json");
        if (stream != null) {
            final JsonObject json = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();
            final JsonObject variants = json.getAsJsonObject("variants");

            if (variants.has(variantString)) {
                return variants.getAsJsonObject(variantString).get("model").getAsString();
            }
            throw new JsonSyntaxException("Variant '" + variantString + "' not found in '" + name + "'");
        }
        throw new IllegalStateException("Could not find blockstate for block -> " + name);
    }
    protected static PolymerBlockModel getModelFromBlockstateVariant(String name, @Nullable BlockState state) {
        final InputStream stream = Polymer.class.getResourceAsStream("/assets/humility-afm/blockstates/" + name + ".json");
        if (stream != null) {
            final JsonObject json = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();
            final JsonObject variants = json.getAsJsonObject("variants");

            for (Map.Entry<String, JsonElement> entry : variants.entrySet()) {
                String variantKey = entry.getKey();

                if (variantKey.isEmpty() || (state != null && matchesState(state, variantKey))) {

                    // Vanilla sometimes puts variants in an array for weighted randoms. We just take the first one.
                    JsonObject variantObj;
                    if (entry.getValue().isJsonArray()) {
                        variantObj = entry.getValue().getAsJsonArray().get(0).getAsJsonObject();
                    } else {
                        variantObj = entry.getValue().getAsJsonObject();
                    }

                    // Extract the model path
                    #if MC_VERSION < 12111 ResourceLocation #else Identifier #endif modelId = getIdRaw(variantObj.get("model").getAsString());

                    // Extract rotations and uvlock (defaulting to 0 and false if they don't exist)
                    int x = variantObj.has("x") ? variantObj.get("x").getAsInt() : 0;
                    int y = variantObj.has("y") ? variantObj.get("y").getAsInt() : 0;
                    boolean uvlock = variantObj.has("uvlock") && variantObj.get("uvlock").getAsBoolean();

                    // Return the fully assembled Polymer model!
                    return PolymerBlockModel.of(modelId, x, y, uvlock, 1);
                }
            }
            throw new JsonSyntaxException("No matching variant found for state in " + name);
        }
        throw new IllegalStateException("Could not find blockstate for block -> " + name);
    }

    protected static boolean matchesState(BlockState state, String variantKey) {
        String[] conditions = variantKey.split(",");
        for (String condition : conditions) {
            String[] parts = condition.split("=");
            if (parts.length != 2) return false;

            String propName = parts[0];
            String propValue = parts[1];

            boolean matched = false;
            for (net.minecraft.world.level.block.state.properties.Property<?> prop : state.getProperties()) {
                if (prop.getName().equals(propName)) {
                    if (state.getValue(prop).toString().toLowerCase().equals(propValue)) {
                        matched = true;
                    }
                    break;
                }
            }
            if (!matched) return false;
        }
        return true;
    }

    protected static String getVariantStringFromState(BlockState state) {
        final ImmutableMap<Property<?>, Comparable<?>> combinations = state.getValues();
        if (state.getValues().isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        combinations.forEach((property, value) -> {
            if (!sb.isEmpty()) sb.append(",");
            sb.append(property.getName()).append("=").append(value.toString().toLowerCase());
        });
        return sb.toString();
    }

    // TODO: Consider changing to PolymerSimpleBlock
    public static class PureTexturedBlock extends Block implements PolymerTexturedBlock {
        private final BlockState mappedState;

        public PureTexturedBlock(Properties properties, BlockState mappedState) {
            super(properties);
            this.mappedState = mappedState;
        }

        @Override
        public Block getPolymerBlock(BlockState state) {
            return this.mappedState.getBlock();
        }

        @Override
        public BlockState getPolymerBlockState(BlockState state) {
            return this.mappedState;
        }
    }

    public static class FallbackTexturedBlock extends Block implements PolymerBlock, EntityBlock {
        private final Block disguise;

        public FallbackTexturedBlock(Properties properties, Block disguise) {
            super(properties);
            this.disguise = disguise;
        }

        @Override
        public Block getPolymerBlock(BlockState state) {
            return this.disguise;
        }

        @Override
        public BlockState getPolymerBlockState(BlockState state) {
            return this.disguise.defaultBlockState();
        }

        @Nullable
        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new FallbackBlockEntity(pos, state);
        }
    }

    protected static #if MC_VERSION < 12111 ResourceLocation #else Identifier #endif getRawModelIdFromBlockstateVariant(String name, String variantString) {
        final InputStream stream = Polymer.class.getResourceAsStream("/assets/humility-afm/blockstates/" + name + ".json");
        if (stream != null) {
            final JsonObject json = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();
            final JsonObject variants = json.getAsJsonObject("variants");
            if (variants.has(variantString)) {
                return getIdRaw(variants.getAsJsonObject(variantString).get("model").getAsString());
            }
        }
        throw new IllegalStateException("Could not find variant " + variantString + " in " + name);
    }

    public static void cacheOpenCabinetModel(Block block, String name) {
        #if MC_VERSION < 12111 ResourceLocation #else Identifier #endif openModelId;
        try {
            openModelId = getRawModelIdFromBlockstateVariant(name, "facing=north,open=true");
        } catch (IllegalStateException e) {
            openModelId = getRawModelIdFromBlockstateVariant(name, "facing=north,half=bottom,open=true");
        }
        PolymerModelData data = PolymerResourcePackUtils.requestModel(Items.GLOWSTONE_DUST, openModelId);
        CABINET_OPEN_MODELS.put(block, data);
    }


    public static class FallbackBlockEntity extends BlockEntity {
        private final ElementHolder holder;
        private HolderAttachment attachment;

        public FallbackBlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityRegistry.FALLBACK_BLOCK_ENTITY, pos, state);

            this.holder = new ElementHolder();
            ItemDisplayElement display = new ItemDisplayElement();

            display.setItem(state.getBlock().asItem().getDefaultInstance());
            display.setModelTransformation(ItemDisplayContext.FIXED);
            display.setScale(new Vector3f(2.001f));
            display.setTranslation(new Vector3f(0f, -0.51f, 0f));

            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                float angle = 0f;
                switch (facing) {
                    case NORTH -> angle = (float) Math.PI;         // 180 degrees
                    case EAST -> angle = (float) (Math.PI * 0.5);  // 90 degrees
                    case SOUTH -> angle = 0f;                      // 0 degrees
                    case WEST -> angle = (float) (Math.PI * 1.5);  // 270 degrees
                }
                display.setLeftRotation(new org.joml.Quaternionf().rotationY(angle));
            }

            this.holder.addElement(display);
        }

        @Override
        public void clearRemoved() {
            super.clearRemoved();
            if (this.level instanceof ServerLevel serverLevel) {
                final Vec3 offsetPos = Vec3.atCenterOf(this.worldPosition).add(0, 0.51, 0);
                this.attachment = ChunkAttachment.of(this.holder, serverLevel, offsetPos);
            }
        }

        @Override
        public void setRemoved() {
            super.setRemoved();
            if (this.attachment != null) {
                this.attachment.destroy();
                this.attachment = null;
            }
        }
    }
}
#endif