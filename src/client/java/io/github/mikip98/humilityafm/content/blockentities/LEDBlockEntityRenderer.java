package io.github.mikip98.humilityafm.content.blockentities;

import io.github.mikip98.humilityafm.content.blocks.light_strips.LightStripBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.minecraft.block.enums.BlockHalf.TOP;

public class LEDBlockEntityRenderer implements BlockEntityRenderer<LEDBlockEntity> {
    @SuppressWarnings("unused")
    public LEDBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(LEDBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        World world = entity.getWorld();
        BlockPos pos = entity.getPos();

        if (world == null || pos == null) {
            // If the world or position is null, return early
            matrices.pop();
            return;
        }

        BlockState blockState = world.getBlockState(pos);

        if (blockState == null || !(blockState.getBlock() instanceof LightStripBlock)) {
            // If the block state is null or not an instance of LEDStripBlock, return early
            matrices.pop();
            return;
        }

        final float blockSize = 0.0625f;
        final byte posisionConstant = 31;

        float blockSizeX = blockSize;
        @SuppressWarnings("unused")
        float blockSizeY = blockSize;
        float blockSizeZ = blockSize;
        byte posisionConstantX = 1;
        byte posisionConstantY = 1;
        byte posisionConstantZ = 1;

        float scale = 1.005f;

        switch (blockState.get(HorizontalFacingBlock.FACING)) {
            case NORTH -> blockSizeX = 1f;
            case SOUTH -> {
                blockSizeX = 1f;
                posisionConstantZ = posisionConstant;
            }
            case EAST -> {
                blockSizeZ = 1f;
                posisionConstantX = posisionConstant;
            }
            case WEST -> blockSizeZ = 1f;
        }
        if (blockState.get(StairsBlock.HALF) == TOP) {
            posisionConstantY = posisionConstant;
        }

        // Move the matrix to the center of the object
        matrices.translate(-blockSizeX/2*(scale-1)*posisionConstantX, -blockSizeY/2*(scale-1)*posisionConstantY, -blockSizeZ/2*(scale-1)*posisionConstantZ);

        matrices.scale(scale, scale, scale);

        // Render the LED strip block with custom light value
        MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(
                matrices.peek(), vertexConsumers.getBuffer(RenderLayer.getSolid()), blockState,
                MinecraftClient.getInstance().getBlockRenderManager().getModel(blockState),
                1.0f, 1.0f, 1.0f, 0xF000F0, overlay);


//        // Use RenderLayer.getSolid() to disable shading and AO
//        RenderLayer renderLayer = RenderLayer.getSolid();
//        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
//
//        // Render the LED strip block
//        MinecraftClient.getInstance().getBlockRenderManager().renderBlock(
//                blockState, pos, world, matrices, vertexConsumer, true, world.getRandom());


//        // Render the LED strip block
//        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(
//                blockState, matrices, vertexConsumers, 0xF000F0, overlay);

        // Pop the matrix stack to avoid issues with subsequent rendering
        matrices.pop();
    }
}
