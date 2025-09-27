package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import org.joml.Quaternionf;

public non-sealed interface ItemWallRendering extends ItemRendering {
    default MatrixStack rotateMatrices(MatrixStack matrices, BlockState blockState) {
        switch (blockState.get(Properties.HORIZONTAL_FACING)) {
            case SOUTH -> matrices.multiply(new Quaternionf().rotationYXZ((float) Math.toRadians(180), 0, 0));
            case EAST -> matrices.multiply(new Quaternionf().rotationYXZ((float) Math.toRadians(270), 0, 0));
            case WEST -> matrices.multiply(new Quaternionf().rotationYXZ((float) Math.toRadians(90), 0, 0));
        }
        return matrices;
    }
}
