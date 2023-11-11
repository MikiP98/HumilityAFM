package io.github.mikip98.content.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class LEDBlockEntityRenderer implements BlockEntityRenderer<LEDBlockEntity> {
    private static final Identifier TERRACOTTA_TEXTURE = new Identifier("minecraft", "block/terracotta");

    public LEDBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(LEDBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        // Get the texture manager
//        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();

        // Bind the terracotta texture
//        textureManager.bindTexture(TERRACOTTA_TEXTURE);

        // Retrieve the VertexConsumer to render the block
//        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getCutout());

        // Apply brightness adjustments
        float brightness = 1.5f;
        int combinedLight = calculateCombinedLight(light, brightness);

        // Use matrices to transform the rendering position if needed
        matrices.push();
        //matrices.translate(0.5, 0.5, 0.5);
        matrices.translate(entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ());

        // Get the block state from the world, you might need to replace it based on your block entity logic
        BlockState blockState = Objects.requireNonNull(entity.getWorld()).getBlockState(entity.getPos());

        // Render the LED strip block
        // You'll need to replace BlockRenderManager.getInstance().renderBlock(...) with your own rendering logic
//        BlockRenderManager.getInstance().renderBlock(
//                Block.getStateId(/* Get the block state here */),
//                matrices,
//                vertexConsumer,
//                combinedLight,
//                overlay
//        );
        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(blockState, matrices, vertexConsumers, combinedLight, overlay);

        // Pop the matrix stack to avoid issues with subsequent rendering
        matrices.pop();
    }

    // Helper method to calculate combined light with brightness adjustment
    private int calculateCombinedLight(int light, float brightness) {
        // Return maximum brightness values
//        return 0xF000F0;  // Max block light (15) and max sky light (15)
//        return 0xFF0000;  // Max block light (15) and max sky light (15)
        return 0xFFFFFF;  // Max block light (15) and max sky light (15)

        // Return adjusted brightness values
//        int blockLight = light & 0xFF;
//        int skyLight = (light >> 16) & 0xFF;
//
//        // Adjust the brightness here, you may need to fine-tune the formula based on your needs
//        blockLight = (int)(blockLight * brightness);
//        skyLight = (int)(skyLight * brightness);
//
//        return blockLight | (skyLight << 16);
    }
}
