package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.rendering;

import io.github.mikip98.humilityafm.helpers.BlockEntityRendererHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public interface RenderSelfBrightening {
    default void renderSelfBrightening(
            BlockState blockState,
            float posisionConstant, float posisionConstantX, float posisionConstantZ,
            MatrixStack matrices, VertexConsumerProvider vertexConsumers,
            int light, int overlay
    ) {
        final float blockSizeYZ = 0.875f;
        final float blockSizeX = 0.25f;

        // Render the brightened outside shell of the cabinet
        renderSelf(
                1.1f, 1,
                blockSizeX, blockSizeYZ, posisionConstant, posisionConstantX, posisionConstantZ, 1.005f,
                blockState, matrices, vertexConsumers, light, overlay
        );

        // Render the brightened inside walls of the cabinet
        renderSelf(
                1.15f, 3,
                blockSizeX, blockSizeYZ, posisionConstant, posisionConstantX, posisionConstantZ, 0.992f,
                blockState, matrices, vertexConsumers, light, overlay
        );
    }

    default void renderSelf(
            float lightMultiplayer, int lightAddition,
            float blockSizeX, float blockSizeYZ,
            float posisionConstant, float posisionConstantX, float posisionConstantZ, float scale,
            BlockState blockState, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
            int light, int overlay
    ) {
        matrices.push();

        matrices.translate(
                -blockSizeX /2*(scale-1)*posisionConstantX,
                -blockSizeYZ/2*(scale-1)*posisionConstant,
                -blockSizeYZ/2*(scale-1)*posisionConstantZ
        );
        matrices.scale(scale, scale, scale);

        int outsideLight = BlockEntityRendererHelper.multiplyLight(light, lightMultiplayer);
        outsideLight = BlockEntityRendererHelper.addLight(outsideLight, lightAddition);

        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(
                blockState, matrices, vertexConsumers, outsideLight, overlay
        );

        matrices.pop();
    }
}
