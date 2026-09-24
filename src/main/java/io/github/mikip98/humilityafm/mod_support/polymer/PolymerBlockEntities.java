#if POLYMER
package io.github.mikip98.humilityafm.mod_support.polymer;

#if MC_VERSION < 12104 import eu.pb4.polymer.resourcepack.api.PolymerModelData; #endif
import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
import io.mikip98.humilityval.content.block.entity.AVLBlockEntity;
import io.mikip98.humilityval.content.block.entity.polymer.PolymerBlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
#if MC_VERSION >= 12005 import net.minecraft.core.component.DataComponents; #endif
#if MC_VERSION >= 12104 && MC_VERSION < 12111 import net.minecraft.resources.ResourceLocation; #endif
#if MC_VERSION >= 12111 import net.minecraft.resources.Identifier; #endif
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PolymerBlockEntities {
    public static abstract class PolymerBlockEntityBase extends AVLBlockEntity {
        public PolymerBlockEntityBase(
                BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, PolymerProperties polymerProperties
        ) {
            super(blockEntityType, blockPos, blockState, polymerProperties);
        }

        protected static float getRotation(BlockState state) {
            final Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            return switch (facing) {
                case NORTH -> 0f;                      // 0 degrees
                case EAST -> (float) (Math.PI * 0.5);  // 90 degrees
                case SOUTH -> (float) Math.PI;         // 180 degrees
                case WEST -> (float) (Math.PI * 1.5);  // 270 degrees
                default -> throw new IllegalStateException();
            };
        }

        protected static float getRotationNSSwap(BlockState state) {
            final Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            return switch (facing) {
                case NORTH -> (float) Math.PI;         // 180 degrees
                case EAST -> (float) (Math.PI * 0.5);  // 90 degrees
                case SOUTH -> 0f;                      // 0 degrees
                case WEST -> (float) (Math.PI * 1.5);  // 270 degrees
                default -> throw new IllegalStateException();
            };
        }

        protected static float getRotationEWSwap(BlockState state) {
            final Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            return switch (facing) {
                case NORTH -> 0f;                      // 0 degrees
                case EAST -> (float) (Math.PI * 1.5);  // 270 degrees
                case SOUTH -> (float) Math.PI;         // 180 degrees
                case WEST -> (float) (Math.PI * 0.5);  // 90 degrees
                default -> throw new IllegalStateException();
            };
        }

        protected static ItemStack disguiseItem(#if MC_VERSION < 12104 PolymerModelData #elif MC_VERSION < 12111 ResourceLocation #else Identifier #endif customModel) {
            final Item baseItem = PolymerItems.VIRTUAL_ITEM_BASE;
            return PolymerBlockEntityUtil.disguiseItem(baseItem, customModel);
        }
    }

    public static class FallbackBlockEntity extends PolymerBlockEntityBase {
        public FallbackBlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityRegistry.FALLBACK_BLOCK_ENTITY, pos, state, PolymerProperties.of().offset());

            display.setScale(new Vector3f(2.0015f));  // TODO: Make the scale into a config

            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                display.setLeftRotation(new Quaternionf().rotationY(getRotationNSSwap(state)));
            }
        }
    }

    public static class ColouredTorchBlockEntity extends PolymerBlockEntityBase {
        public ColouredTorchBlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityRegistry.COLOURED_TORCH_BLOCK_ENTITY, pos, state, PolymerProperties.of());

            display.setScale(new Vector3f(1.005f));  // TODO: Make the scale into a config

            if (state.hasProperty(WallTorchBlock.FACING)) {
                final Direction facing = state.getValue(WallTorchBlock.FACING);

                final float angleY = getRotationNSSwap(state);
                final Quaternionf rotation = new Quaternionf().rotateY(angleY).rotateX((float) (Math.PI / 8));

                display.setLeftRotation(rotation);

                float offsetAmount = 0.30845f;
                float xOffset = facing.getStepX() * -offsetAmount;
                float zOffset = facing.getStepZ() * -offsetAmount;
                float yOffset = 0.18045f;

                display.setTranslation(new Vector3f(xOffset, yOffset, zOffset));
            } else {
                display.setTranslation(new Vector3f(0f, 0f, 0f));
            }
        }
    }

    public static class CandlestickBlockEntity extends PolymerBlockEntityBase {
        public CandlestickBlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityRegistry.CANDLESTICK_BLOCK_ENTITY, pos, state, PolymerProperties.of().ticking());
            this.updateVisualState(state);
        }

        public void updateVisualState(BlockState state) {
            #if MC_VERSION < 12104 PolymerModelData #elif MC_VERSION < 12111 ResourceLocation #else Identifier #endif customData = null;
            if (PolymerModelCache.CANDLESTICK_MODEL_CACHE.containsKey(state.getBlock())) {
                customData = PolymerModelCache.CANDLESTICK_MODEL_CACHE.get(state.getBlock()).get(state);
            }
            final ItemStack disguisedStack = disguiseItem(customData);
            display.setItem(disguisedStack);

            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                final Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                float angle = getRotationEWSwap(state);
                float offsetAmount = 0.046875f;
                float xOffset = facing.getStepX() * -offsetAmount;
                float zOffset = facing.getStepZ() * -offsetAmount;
                display.setLeftRotation(new Quaternionf().rotateY(angle));
                display.setTranslation(new Vector3f(xOffset, 0, zOffset));
            } else {
                display.setLeftRotation(new Quaternionf());
                display.setTranslation(new Vector3f(0, -0.25f, 0.046875f));
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        public void setBlockState(BlockState state) {
            super.setBlockState(state);
            this.updateVisualState(state);
        }
    }
}
#endif