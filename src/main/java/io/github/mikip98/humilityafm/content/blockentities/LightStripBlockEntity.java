package io.github.mikip98.humilityafm.content.blockentities;

#if POLYMER import eu.pb4.polymer.resourcepack.api.PolymerModelData; #endif
#if POLYMER import io.github.mikip98.humilityafm.mod_support.polymer.PolymerBlockEntities; #endif
#if POLYMER import io.github.mikip98.humilityafm.mod_support.polymer.PolymerModelCache; #endif
import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
#if POLYMER import net.minecraft.core.Direction; #endif
#if POLYMER import net.minecraft.util.Brightness; #endif
#if POLYMER import net.minecraft.world.item.ItemStack; #endif
#if !POLYMER import net.minecraft.world.level.block.entity.BlockEntity; #endif
import net.minecraft.world.level.block.state.BlockState;
#if POLYMER import net.minecraft.world.level.block.state.properties.BlockStateProperties; #endif
#if POLYMER import net.minecraft.world.level.block.state.properties.Half; #endif
#if POLYMER import net.minecraft.world.level.block.state.properties.StairsShape; #endif
#if POLYMER import org.joml.Quaternionf; #endif
#if POLYMER import org.joml.Vector3f; #endif

public class LightStripBlockEntity extends #if POLYMER PolymerBlockEntities.PolymerBlockEntityBase #else BlockEntity #endif {
    public LightStripBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.LIGHT_STRIP_BLOCK_ENTITY, pos, state #if POLYMER, false, true #endif);

        #if POLYMER
        display.setBrightness(new Brightness(15, 15));
        this.updateVisualState(state);
        #endif
    }

    #if POLYMER
    public void updateVisualState(BlockState state) {
        final StairsShape shape = state.getValue(BlockStateProperties.STAIRS_SHAPE);

        PolymerModelData customData = null;
        if (shape == StairsShape.INNER_LEFT || shape == StairsShape.INNER_RIGHT) {
            customData = PolymerModelCache.LIGHT_STRIP_INNER_MODELS.get(state.getBlock());
        } else if (shape == StairsShape.OUTER_LEFT || shape == StairsShape.OUTER_RIGHT) {
            customData = PolymerModelCache.LIGHT_STRIP_OUTER_MODELS.get(state.getBlock());
        }

        if (customData != null) {
            final ItemStack stack = disguiseItem(customData);
            display.setItem(stack);
        } else {
            display.setItem(state.getBlock().asItem().getDefaultInstance());
        }

        final Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

        boolean isLeft = (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT);
        boolean isRight = (shape == StairsShape.INNER_RIGHT || shape == StairsShape.OUTER_RIGHT);

        float xOffset = 0f;
        float zOffset = 0f;
        int angleIndex;

        switch (facing) {
            case SOUTH -> {
                if (isLeft)       { zOffset = 0.4375f;  angleIndex = 2; }
                else if (isRight) { xOffset = -0.4375f; angleIndex = 1; }
                else              { zOffset = 0.5f;     angleIndex = 0; }
            }
            case WEST -> {
                if (isRight)      { zOffset = -0.4375f; angleIndex = 0; }
                else              { xOffset = -0.4375f; angleIndex = 1; }
            }
            case NORTH -> {
                if (isLeft)       { zOffset = -0.4375f; angleIndex = 0; }
                else if (isRight) { xOffset = 0.4375f;  angleIndex = 3; }
                else              { zOffset = -0.5f;    angleIndex = 2; }
            }
            case EAST -> {
                if (isRight)      { zOffset = 0.4375f;  angleIndex = 2; }
                else              { xOffset = 0.4375f;  angleIndex = 3; }
            }
            default -> throw new IllegalStateException("Unexpected direction: " + facing);
        }

        float yRotation = (float) (angleIndex * (Math.PI / 2));
        float yOffset = state.getValue(BlockStateProperties.HALF) == Half.TOP ? 0.203125f : -0.734375f;

        display.setTranslation(new Vector3f(xOffset, yOffset, zOffset));
        display.setLeftRotation(new Quaternionf().rotateY(yRotation));
    }
    #endif
}
