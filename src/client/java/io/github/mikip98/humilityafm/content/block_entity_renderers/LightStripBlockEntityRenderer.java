package io.github.mikip98.humilityafm.content.block_entity_renderers;

import io.github.mikip98.humilityafm.content.blockentities.LightStripBlockEntity;
import io.github.mikip98.humilityafm.content.blocks.LightStripBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
#if MC_VERSION >= 12105
import net.minecraft.client.render.block.BlockModelRenderer;
#endif
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
#if MC_VERSION >= 12111
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
#endif
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
#if MC_VERSION >= 12105
import net.minecraft.util.math.Vec3d;
#endif
import net.minecraft.world.World;
#if MC_VERSION >= 12111
import org.jetbrains.annotations.Nullable;
#endif

import static net.minecraft.block.enums.BlockHalf.TOP;

public class LightStripBlockEntityRenderer implements BlockEntityRenderer<LightStripBlockEntity #if MC_VERSION >= 12111, LightStripBlockEntityRenderer.LightStripRenderState #endif> {
    protected static RenderFunction renderFunction = LightStripBlockEntityRenderer::fakeRunnable;
    public static void enableBrightening() { renderFunction = LightStripBlockEntityRenderer::renderBrightening; }
    public static void disableBrightening() { renderFunction = LightStripBlockEntityRenderer::fakeRunnable; }

    @SuppressWarnings("unused")
    public LightStripBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    #if MC_VERSION < 12111
    @Override
    public void render(LightStripBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay #if MC_VERSION >= 12105, Vec3d cameraPos #endif) {
        renderFunction.execute(entity, matrices, vertexConsumers, overlay);
    }
    #else
    @Override
    public LightStripRenderState createRenderState() {
        return new LightStripRenderState();
    }

    @Override
    public void updateRenderState(
            LightStripBlockEntity blockEntity, LightStripRenderState renderState,
            float tickDelta, Vec3d cameraPos,
            @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlayCommand
    ) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, renderState, tickDelta, cameraPos, crumblingOverlayCommand);
        renderState.blockState = blockEntity.getCachedState();
        renderState.overlay = net.minecraft.client.render.OverlayTexture.DEFAULT_UV;
    }

    @Override
    public void render(LightStripRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        VertexConsumerProvider vertexConsumers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        renderFunction.execute(state, matrices, queue, state.overlay);
    }
    #endif

    #if MC_VERSION < 12111
    protected static void fakeRunnable(LightStripBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int overlay) {}
    protected static void renderBrightening(LightStripBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int overlay) {
        World world = entity.getWorld();
        BlockPos pos = entity.getPos();
        if (world == null || pos == null) return;

        BlockState blockState = world.getBlockState(pos);
        #else
    protected static void fakeRunnable(LightStripRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, int overlay) {}
    protected static void renderBrightening(LightStripRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, int overlay) {
        VertexConsumerProvider vertexConsumers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        BlockState blockState = state.blockState;
    #endif

        if (blockState == null || !(blockState.getBlock() instanceof LightStripBlock)) return;

        matrices.push();

        // Scale of the brightened model
        final float scale = 1.01111f;

        // Calculate a pixel shift constant for centering of the brightened model
        final float pixelShift = (1f/32f)*(1-scale);

        // Move the block up or down depending on the block's half, and centre it
        final float deltaY = blockState.get(StairsBlock.HALF) == TOP ? (1-scale)-pixelShift : pixelShift;

        // Move the rendered block half of the difference between the original and the scaled size
        final float deltaLongAxis = 0.5f*(1-scale);

        // Calculate correct model offsets
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
                        // 1.9375 -> 1.875 z-fight, 2.0 z-fight on the other side
                        deltaX = 1.9375f * deltaLongAxis;
                        deltaZ = 1.9375f * deltaLongAxis;
                        break;
                    case EAST:
                        // Center the block on the X and Z axis
                        deltaX = 1.9375f * deltaLongAxis;
                        deltaZ = pixelShift;
                        break;
                    case WEST:
                        // Center the block on the X and Z axis
                        // 1.9375 -> 1.875 z-fight, 2.0 z-fight on the other side
                        deltaX = pixelShift;
                        deltaZ = 1.9375f * deltaLongAxis;
                        break;
                }
                break;
            case OUTER_RIGHT:
                switch (blockState.get(Properties.HORIZONTAL_FACING)) {
                    case NORTH:
                        // Center the block on the X and Z axis
                        deltaX = 1.9375f * deltaLongAxis;
                        deltaZ = pixelShift;
                        break;
                    case SOUTH:
                        // Center the block on the X and Z axis
                        deltaX = pixelShift;
                        deltaZ = 1.9375f * deltaLongAxis;
                        break;
                    case EAST:
                        // Center the block on the X and Z axis
                        deltaX = 1.9375f * deltaLongAxis;
                        deltaZ = 1.9375f * deltaLongAxis;
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
        #if MC_VERSION < 12105
        MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(
                matrices.peek(),
                vertexConsumers.getBuffer(RenderLayer.getSolid()),
                blockState,
                MinecraftClient.getInstance().getBlockRenderManager().getModel(blockState),
                1.0f, 1.0f, 1.0f,
                0xF000F0,
                overlay
        );
        #else
        BlockModelRenderer.render(
                matrices.peek(),
                vertexConsumers.getBuffer(#if MC_VERSION < 12111 RenderLayer.getSolid() #else RenderLayers.solid() #endif),
                MinecraftClient.getInstance().getBlockRenderManager().getModel(blockState),
                1.0f, 1.0f, 1.0f,
                0xF000F0,
                overlay
        );
        #endif

        matrices.pop();
    }

    #if MC_VERSION < 12111
    @FunctionalInterface
    protected interface RenderFunction {
        void execute(LightStripBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int overlay);
    }
    #else
    @FunctionalInterface
    protected interface RenderFunction {
        void execute(LightStripRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, int overlay);
    }

    public static class LightStripRenderState extends BlockEntityRenderState {
        public BlockState blockState;
        public int overlay;
    }
    #endif
}
