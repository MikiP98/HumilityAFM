package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering;

import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import org.joml.Quaternionf;

public non-sealed interface ItemFloorRendering extends ItemRendering {
    default MatrixStack rotateMatrices(MatrixStack matrices, BlockState blockState) {
        float rotationX = (float) (blockState.get(Properties.BLOCK_HALF) == BlockHalf.BOTTOM ? Math.toRadians(90) : Math.toRadians(270));
        switch (blockState.get(Properties.HORIZONTAL_FACING)) {
            case NORTH -> matrices.multiply(new Quaternionf().rotationYXZ(0, rotationX, 0));
            case SOUTH -> matrices.multiply(new Quaternionf().rotationYXZ((float) Math.toRadians(180), rotationX, 0));
            case EAST -> matrices.multiply(new Quaternionf().rotationYXZ((float) Math.toRadians(270), rotationX, 0));
            case WEST -> matrices.multiply(new Quaternionf().rotationYXZ((float) Math.toRadians(90), rotationX, 0));
        }
        return matrices;
    }
}
