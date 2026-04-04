package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock;

#if MC_VERSION >= 12111
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.CabinetBlockEntityRenderState;
#endif
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.ItemWallRendering;
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.RenderSelfBrightening;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntity;
#if MC_VERSION >= 12111
import net.minecraft.client.renderer.SubmitNodeCollector;
#else
import io.github.mikip98.humilityafm.content.blocks.cabinet.IlluminatedCabinetBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
#endif
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
#if MC_VERSION >= 12105
import net.minecraft.world.phys.Vec3;
#endif
#if MC_VERSION >= 12111
import io.github.mikip98.humilityafm.content.blocks.cabinet.IlluminatedCabinetBlock;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
#endif

public class IlluminatedCabinetBlockEntityRenderer implements
        BlockEntityRenderer<IlluminatedCabinetBlockEntity #if MC_VERSION >= 12111, CabinetBlockEntityRenderState #endif>,
        ItemWallRendering
{
    protected static RenderFunction renderFunction = IlluminatedCabinetBlockEntityRenderer::fakeRunnable;
    public static void enableBrightening() { renderFunction = IlluminatedCabinetBlockEntityRenderer::selfBrighteningRunner; }
    public static void disableBrightening() { renderFunction = IlluminatedCabinetBlockEntityRenderer::fakeRunnable; }

    @SuppressWarnings("unused")
    public IlluminatedCabinetBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

    #if MC_VERSION < 12111
    @Override
    public void render(
            IlluminatedCabinetBlockEntity blockEntity, float tickDelta,
            PoseStack poseStack, MultiBufferSource bufferSource,
            int light, int overlay #if MC_VERSION >= 12105, Vec3 cameraPos #endif
    ) {
        Level level = blockEntity.getLevel();
        BlockPos pos = blockEntity.getBlockPos();
        if (level == null) return;

        BlockState blockState = level.getBlockState(pos);
        if (!(blockState.getBlock() instanceof IlluminatedCabinetBlock)) return;

        renderItem(blockEntity, blockState, poseStack, bufferSource, 255, overlay);
        renderFunction.execute(blockState, poseStack, bufferSource, light, overlay);
    }
    #else
    @Override
    public @NonNull CabinetBlockEntityRenderState createRenderState() {
        return new CabinetBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(
            @NonNull IlluminatedCabinetBlockEntity blockEntity,
            @NonNull CabinetBlockEntityRenderState renderState,
            float tickDelta, @NonNull Vec3 cameraPos,
            @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, tickDelta, cameraPos, crumblingOverlay);
        renderState.extractSharedState(blockEntity);
    }

    @Override
    public void submit(
            CabinetBlockEntityRenderState renderState,
            @NonNull PoseStack poseStack,
            @NonNull SubmitNodeCollector collector,
            @NonNull CameraRenderState cameraState
    ) {
        if (renderState.blockState == null || !(renderState.blockState.getBlock() instanceof IlluminatedCabinetBlock)) return;
        // TODO: Why the check above?
        if (renderState.hasItem) {
            renderItem(renderState, poseStack, collector, 255);
        }
        renderFunction.execute(renderState.blockState, poseStack, renderState.light, renderState.overlay);
    }
    #endif

    protected static void selfBrighteningRunner(
            BlockState blockState,
            PoseStack poseStack,
            #if MC_VERSION < 12111 MultiBufferSource bufferSource, #endif
            int light, int overlay
    ) {
        final float posisionConstant = 1.15f;
        float posisionConstantX = posisionConstant;
        float posisionConstantZ = posisionConstant;

        switch (blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case NORTH:
                posisionConstantX = 4f;
                posisionConstantZ = 2f;
                break;
            case SOUTH:
                posisionConstantX = 4f;
                posisionConstantZ = 0.3f;
                break;
            case WEST:
                posisionConstantX = 7f;
        }

        RenderSelfBrightening.renderSelfBrightening(
                blockState,
                posisionConstant, posisionConstantX, posisionConstantZ,
                poseStack,
                #if MC_VERSION < 12111 bufferSource, #endif
                light, overlay
        );
    }
    @SuppressWarnings("unused")
    protected static void fakeRunnable(
            BlockState blockState,
            PoseStack poseStack,
            #if MC_VERSION < 12111 MultiBufferSource bufferSource, #endif
            int light, int overlay
    ) {}

    @FunctionalInterface
    protected interface RenderFunction {
        void execute(
                BlockState blockState,
                PoseStack poseStack,
                #if MC_VERSION < 12111 MultiBufferSource bufferSource, #endif
                int light, int overlay
        );
    }
}