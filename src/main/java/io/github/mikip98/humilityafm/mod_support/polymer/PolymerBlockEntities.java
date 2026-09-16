#if POLYMER
package io.github.mikip98.humilityafm.mod_support.polymer;

import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.ChunkAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
import io.mikip98.humilityval.content.block.entity.AVLBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PolymerBlockEntities {
    public static abstract class PolymerBlockEntityBase extends AVLBlockEntity { // implements PolymerItem, PolymerModelData
        protected final ElementHolder holder = new ElementHolder();
        protected final ItemDisplayElement display = new ItemDisplayElement();
        protected HolderAttachment attachment;
        protected final boolean offset;
        protected final boolean ticking;

        public PolymerBlockEntityBase(
                BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, boolean offset, boolean ticking
        ) {
            super(blockEntityType, blockPos, blockState);
            this.offset = offset;
            this.ticking = ticking;

            this.holder.addElement(display);
            display.setModelTransformation(ItemDisplayContext.FIXED);
            display.setItem(blockState.getBlock().asItem().getDefaultInstance());
            if (offset) display.setTranslation(new Vector3f(0f, -0.51f, 0f));
        }

        @Override
        public void clearRemoved() {
            super.clearRemoved();
            if (this.level instanceof ServerLevel serverLevel) {
                Vec3 offsetPos = Vec3.atCenterOf(this.worldPosition);
                if (offset) offsetPos = offsetPos.add(0, 0.51, 0);
                if (ticking) this.attachment = ChunkAttachment.ofTicking(this.holder, serverLevel, offsetPos);
                else this.attachment = ChunkAttachment.of(this.holder, serverLevel, offsetPos);
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

        protected static ItemStack disguiseItem(PolymerModelData customModelData) {
            final Item disguiseItem = PolymerItems.VIRTUAL_ITEM_BASE;
            final ItemStack disguisedStack = new ItemStack(disguiseItem);

            if (customModelData != null) {
                #if MC_VERSION < 12005
                disguisedStack.getOrCreateTag().putInt("CustomModelData", customModelData.value());
                #else
                disguisedStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(customModelData.value()));
                #endif
            }

            return disguisedStack;
        }
    }

    public static class FallbackBlockEntity extends PolymerBlockEntityBase {
        public FallbackBlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityRegistry.FALLBACK_BLOCK_ENTITY, pos, state, true, false);

            display.setScale(new Vector3f(2.0015f));  // TODO: Make the scale into a config

            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                display.setLeftRotation(new Quaternionf().rotationY(getRotationNSSwap(state)));
            }
        }
    }

    public static class ColouredTorchBlockEntity extends PolymerBlockEntityBase {
        public ColouredTorchBlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityRegistry.COLOURED_TORCH_BLOCK_ENTITY, pos, state, false, false);

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
            super(BlockEntityRegistry.CANDLESTICK_BLOCK_ENTITY, pos, state, false, true);
            this.updateVisualState(state);
        }

        public void updateVisualState(BlockState state) {
            PolymerModelData customData = null;
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