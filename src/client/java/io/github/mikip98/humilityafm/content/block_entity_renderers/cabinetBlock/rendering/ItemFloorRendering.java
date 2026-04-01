package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import org.joml.Quaternionf;

public non-sealed interface ItemFloorRendering extends ItemRendering {
    default PoseStack rotateMatrices(PoseStack poseStack, BlockState blockState) {
        float rotationX = (float) (blockState.getValue(BlockStateProperties.HALF) == Half.BOTTOM ? Math.toRadians(90) : Math.toRadians(270));
        switch (blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case NORTH -> poseStack.mulPose(new Quaternionf().rotationYXZ(0, rotationX, 0));
            case SOUTH -> poseStack.mulPose(new Quaternionf().rotationYXZ((float) Math.toRadians(180), rotationX, 0));
            case EAST -> poseStack.mulPose(new Quaternionf().rotationYXZ((float) Math.toRadians(270), rotationX, 0));
            case WEST -> poseStack.mulPose(new Quaternionf().rotationYXZ((float) Math.toRadians(90), rotationX, 0));
        }
        return poseStack;
    }
}
