package io.github.mikip98.content.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class LEDBlockEntityRenderer implements BlockEntityRenderer<LEDBlockEntity> {
    public LEDBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(LEDBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        int renderLight = 0xFFFFFF;

        // Use matrices ensure correct rendering (eliminate Z-fighting)
        matrices.push();
        matrices.translate(entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ());

        // Get the block state from the world, you might need to replace it based on your block entity logic
        BlockState blockState = Objects.requireNonNull(entity.getWorld()).getBlockState(entity.getPos());

        // Render the LED strip block
        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(
                blockState, matrices, vertexConsumers, renderLight, overlay);

        // Pop the matrix stack to avoid issues with subsequent rendering
        matrices.pop();
    }

    // Helper method to calculate combined light with brightness adjustment
    private int calculateCombinedLight(int light, float brightness) {
//        return 0xF000F0;  // Max block light (15) and max skylight (15) - ChatGPT
//        return 0xFF0000;  // Max block light (15) and max skylight (15) - GitHub Copilot

        // Return adjusted brightness values
        int blockLight = light & 0xFF;
        int skyLight = (light >> 16) & 0xFF;

        // Adjust the brightness here, you may need to fine-tune the formula based on your needs
        blockLight = (int)(blockLight * brightness);
        skyLight = (int)(skyLight * brightness);

        return blockLight | (skyLight << 16);
    }
}
