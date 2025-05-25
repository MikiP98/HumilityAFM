package io.github.mikip98.humilityafm.content.blockentities;

import io.github.mikip98.humilityafm.content.blocks.LightStripBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.minecraft.block.enums.BlockHalf.TOP;

public class LightStripBlockEntityRenderer implements BlockEntityRenderer<LightStripBlockEntity> {
    @SuppressWarnings("unused")
    public LightStripBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(LightStripBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        renderFunction.execute(entity, matrices, vertexConsumers, overlay);
    }


    @FunctionalInterface
    protected interface RenderFunction {
        void execute(LightStripBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int overlay);
    }

    protected static RenderFunction renderFunction = LightStripBlockEntityRenderer::fakeRunnable;
    public static void enableBrightening() {
        renderFunction = LightStripBlockEntityRenderer::renderBrightening;
    }

    protected static void fakeRunnable(LightStripBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int overlay) {}
    protected static void renderBrightening(LightStripBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int overlay) {
        World world = entity.getWorld();
        BlockPos pos = entity.getPos();
        if (world == null || pos == null) {
            return;
        }

        BlockState blockState = world.getBlockState(pos);
        if (blockState == null || !(blockState.getBlock() instanceof LightStripBlock)) {
            return;
        }


        matrices.push();

        // Scale the rendered block by a configurable factor
        final float scale = 1.01111f;

        // Calculate a pixel shift constant for centering the block
        final float pixelShift = (1f/32f)*(1-scale);

        // Move the block up or down depending on the block's half, and center it
        final float deltaY = blockState.get(StairsBlock.HALF) == TOP ? (1-scale)-pixelShift : pixelShift;

        // Move the rendered block half of the difference between the original and the scaled size
        final float deltaLongAxis = 0.5f*(1-scale);

        float deltaX = 0f;
        float deltaZ = 0f;
        switch (blockState.get(Properties.STAIR_SHAPE)) {
            case STRAIGHT:
                switch (blockState.get(Properties.HORIZONTAL_FACING)) {
                    case NORTH:
                        deltaX = deltaLongAxis;
                        // Center the block on the Z axis
                        deltaZ = pixelShift;
                        break;
                    case SOUTH:
                        deltaX = deltaLongAxis;
                        // Center the block on the Z axis
                        deltaZ = (1-scale)-pixelShift;
                        break;
                    case EAST:
                        // Center the block on the X axis
                        deltaX = (1-scale)-pixelShift;
                        deltaZ = deltaLongAxis;
                        break;
                    case WEST:
                        // Center the block on the X axis
                        deltaX = pixelShift;
                        deltaZ = deltaLongAxis;
                        break;
                }
                break;
            case INNER_LEFT:
                switch (blockState.get(Properties.HORIZONTAL_FACING)) {
                    case NORTH:
                        // Center the block on the X and Z axis
                        deltaX = pixelShift;
                        deltaZ = pixelShift;
                        break;
                    case SOUTH:
                        // Center the block on the X and Z axis
                        deltaX = (1-scale)-pixelShift;
                        deltaZ = (1-scale)-pixelShift;
                        break;
                    case EAST:
                        // Center the block on the X and Z axis
                        deltaX = deltaLongAxis*2 - pixelShift;
                        deltaZ = pixelShift;
                        break;
                    case WEST:
                        // Center the block on the X and Z axis
                        deltaX = pixelShift;
                        deltaZ = deltaLongAxis*2 - pixelShift;
                        break;
                }
                break;
            case INNER_RIGHT:
                switch (blockState.get(Properties.HORIZONTAL_FACING)) {
                    case NORTH:
                        // Center the block on the X and Z axis
                        deltaX = deltaLongAxis*2 - pixelShift;
                        deltaZ = pixelShift;
                        break;
                    case SOUTH:
                        // Center the block on the X and Z axis
                        deltaX = pixelShift;
                        deltaZ = deltaLongAxis*2 - pixelShift;
                        break;
                    case EAST:
                        // Center the block on the X and Z axis
                        deltaX = (1-scale)-pixelShift;
                        deltaZ = (1-scale)-pixelShift;
                        break;
                    case WEST:
                        // Center the block on the X and Z axis
                        deltaX = pixelShift;
                        deltaZ = pixelShift;
                        break;
                }
                break;
            case OUTER_LEFT:
                switch (blockState.get(Properties.HORIZONTAL_FACING)) {
                    case NORTH:
                        // Center the block on the X and Z axis
                        deltaX = pixelShift;
                        deltaZ = pixelShift;
                        break;
                    case SOUTH:
                        // Center the block on the X and Z axis
                        deltaX = 2*deltaLongAxis;
                        deltaZ = 2*deltaLongAxis;
                        break;
                    case EAST:
                        // Center the block on the X and Z axis
                        deltaX = 2*deltaLongAxis;
                        deltaZ = pixelShift;
                        break;
                    case WEST:
                        // Center the block on the X and Z axis
                        deltaX = pixelShift;
                        deltaZ = 2*deltaLongAxis;
                        break;
                }
                break;
            case OUTER_RIGHT:
                switch (blockState.get(Properties.HORIZONTAL_FACING)) {
                    case NORTH:
                        // Center the block on the X and Z axis
                        deltaX = 2*deltaLongAxis;
                        deltaZ = pixelShift;
                        break;
                    case SOUTH:
                        // Center the block on the X and Z axis
                        deltaX = pixelShift;
                        deltaZ = 2*deltaLongAxis;
                        break;
                    case EAST:
                        // Center the block on the X and Z axis
                        deltaX = 2*deltaLongAxis;
                        deltaZ = 2*deltaLongAxis;
                        break;
                    case WEST:
                        // Center the block on the X and Z axis
                        deltaX = pixelShift;
                        deltaZ = pixelShift;
                        break;
                }
                break;
        }

        matrices.translate(deltaX, deltaY, deltaZ);
        matrices.scale(scale, scale, scale);

        // Render the LED strip block with custom light value
        MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(
                matrices.peek(),
                vertexConsumers.getBuffer(RenderLayer.getSolid()),
                blockState,
                MinecraftClient.getInstance().getBlockRenderManager().getModel(blockState),
                1.0f, 1.0f, 1.0f,
                0xF000F0,
                overlay
        );

        matrices.pop();
    }
}
