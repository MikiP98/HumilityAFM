package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Quaternionf;

public non-sealed interface ItemWallRendering extends ItemRendering {
    default PoseStack rotateMatrices(PoseStack poseStack, BlockState blockState) {
        switch (blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case SOUTH -> poseStack.rotate(new Quaternionf().rotationY((float) Math.toRadians(180)));
            case EAST -> poseStack.rotate(new Quaternionf().rotationY((float) Math.toRadians(270)));
            case WEST -> poseStack.rotate(new Quaternionf().rotationY((float) Math.toRadians(90)));
        }
        return poseStack;
    }
}
