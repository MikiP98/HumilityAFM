package io.github.mikip98.humilityafm.content.block_entity_renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mikip98.humilityafm.content.blockentities.LightStripBlockEntity;
import io.github.mikip98.humilityafm.content.blocks.LightStripBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
#if MC_VERSION >= 12105
import net.minecraft.client.renderer.block.ModelBlockRenderer;
#endif
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
#if MC_VERSION >= 12105
import net.minecraft.world.phys.Vec3;
#endif

public class LightStripBlockEntityRenderer implements BlockEntityRenderer<LightStripBlockEntity #if MC_VERSION >= 12111, LightStripBlockEntityRenderer.LightStripRenderState #endif> {
    protected static RenderFunction renderFunction = LightStripBlockEntityRenderer::fakeRunnable;
    public static void enableBrightening() { renderFunction = LightStripBlockEntityRenderer::renderBrightening; }
    public static void disableBrightening() { renderFunction = LightStripBlockEntityRenderer::fakeRunnable; }

    @SuppressWarnings("unused")
    public LightStripBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

    #if MC_VERSION < 12111
    @Override
    public void render(
            LightStripBlockEntity blockEntity, float tickDelta,
            PoseStack poseStack, MultiBufferSource bufferSource,
            int light, int overlay #if MC_VERSION >= 12105, Vec3 cameraPos #endif
    ) {
        renderFunction.execute(blockEntity, poseStack, bufferSource, overlay);
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
    protected static void fakeRunnable(LightStripBlockEntity entity, PoseStack poseStack, MultiBufferSource bufferSource, int overlay) {}
    #else
    protected static void fakeRunnable(LightStripRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, int overlay) {}
    #endif

    #if MC_VERSION < 12111
    protected static void renderBrightening(LightStripBlockEntity entity, PoseStack poseStack, MultiBufferSource bufferSource, int packedOverlay) {
        final Level level = entity.getLevel();
        final BlockPos pos = entity.getBlockPos();
        if (level == null) return;

        final BlockState blockState = level.getBlockState(pos);
        renderBrighteningInternal(blockState, poseStack, bufferSource, packedOverlay);
    }
    #else
    protected static void renderBrightening(LightStripRenderState state, PoseStack poseStack, OrderedRenderCommandQueue queue, int packedOverlay) {
        final MultiBufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        final BlockState blockState = state.blockState;
        renderBrighteningInternal(blockState, poseStack, bufferSource, packedOverlay);
    }
    #endif
    protected static void renderBrighteningInternal(BlockState blockState, PoseStack poseStack, MultiBufferSource bufferSource, int packedOverlay) {
        if (blockState == null || !(blockState.getBlock() instanceof LightStripBlock)) return;

        poseStack.pushPose();

        // Scale of the brightened model
        final float scale = 1.01111f;

        // Calculate a pixel shift constant for centering of the brightened model
        final float pixelShift = (1f/32f)*(1-scale);

        // Move the block up or down depending on the block's half, and centre it
        final float deltaY = blockState.getValue(BlockStateProperties.HALF) == Half.TOP ? (1-scale)-pixelShift : pixelShift;

        // Move the rendered block half of the difference between the original and the scaled size
        final float deltaLongAxis = 0.5f*(1-scale);

        // Calculate correct model offsets
        float deltaX = 0f;
        float deltaZ = 0f;
        switch (blockState.getValue(BlockStateProperties.STAIRS_SHAPE)) {
            case STRAIGHT:
                switch (blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
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
                switch (blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
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
                switch (blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
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
                switch (blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
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
                switch (blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
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

        poseStack.translate(deltaX, deltaY, deltaZ);
        poseStack.scale(scale, scale, scale);

        // Render the LED strip block with custom light value
        // Render the LED strip block with custom light value
        #if MC_VERSION < 12105
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
                poseStack.last(),
                bufferSource.getBuffer(RenderType.solid()),
                blockState,
                Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState),
                1.0f, 1.0f, 1.0f,
                0xF000F0,
                packedOverlay
        );
        #else
        ModelBlockRenderer.renderModel(
                poseStack.last(),
                bufferSource.getBuffer(RenderType.solid()), // No inner #if needed here!
                Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState),
                1.0f, 1.0f, 1.0f,
                0xF000F0,
                packedOverlay
        );
        #endif

        poseStack.popPose();
    }

    #if MC_VERSION < 12111
    @FunctionalInterface
    protected interface RenderFunction {
        void execute(LightStripBlockEntity entity, PoseStack poseStack, MultiBufferSource bufferSource, int overlay);
    }
    #else
    @FunctionalInterface
    protected interface RenderFunction {
        void execute(LightStripRenderState state, PoseStack poseStack, OrderedRenderCommandQueue queue, int overlay);
    }

    public static class LightStripRenderState extends BlockEntityRenderState {
        public BlockState blockState;
        public int overlay;
    }
    #endif
}
