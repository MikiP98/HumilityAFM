package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock;

#if MC_VERSION >= 12111
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.CabinetBlockEntityRenderState;
#endif
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.ItemFloorRendering;
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.RenderSelfBrightening;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.FloorIlluminatedCabinetBlockEntity;
import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorIlluminatedCabinetBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
#if MC_VERSION >= 12105
import net.minecraft.world.phys.Vec3;
#endif

public class FloorIlluminatedCabinetBlockEntityRenderer implements
        BlockEntityRenderer<FloorIlluminatedCabinetBlockEntity #if MC_VERSION >= 12111, CabinetBlockEntityRenderState #endif>,
        ItemFloorRendering
{
    protected static RenderFunction renderFunction = FloorIlluminatedCabinetBlockEntityRenderer::fakeRunnable;
    public static void enableBrightening() { renderFunction = RenderSelfBrightening::renderSelfBrightening; }
    public static void disableBrightening() { renderFunction = FloorIlluminatedCabinetBlockEntityRenderer::fakeRunnable; }

    @SuppressWarnings("unused")
    public FloorIlluminatedCabinetBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

    #if MC_VERSION < 12111
    @Override
    public void render(
            FloorIlluminatedCabinetBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource,
            int packedLight, int packedOverlay #if MC_VERSION >= 12105, Vec3 cameraPos #endif
    ) {
        Level level = blockEntity.getLevel();
        BlockPos pos = blockEntity.getBlockPos();
        if (level == null) return;

        BlockState blockState = level.getBlockState(pos);
        if (!(blockState.getBlock() instanceof FloorIlluminatedCabinetBlock)) return;

        renderItem(blockEntity, blockState, poseStack, bufferSource, 255, packedOverlay);
        final float positionConstant = 1.15f;
        renderFunction.execute(
                blockState, positionConstant, positionConstant, positionConstant, poseStack, bufferSource, packedLight, packedOverlay
        );
    }
    #else
    @Override
    public CabinetBlockEntityRenderState createRenderState() {
        return new CabinetBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(
            FloorIlluminatedCabinetBlockEntity blockEntity,
            CabinetBlockEntityRenderState renderState,
            float tickDelta, Vec3d cameraPos,
            @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlayCommand
    ) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, renderState, tickDelta, cameraPos, crumblingOverlayCommand);
        renderState.extractSharedState(blockEntity);
    }

    @Override
    public void render(CabinetBlockEntityRenderState renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        if (renderState.blockState == null || !(renderState.blockState.getBlock() instanceof IlluminatedCabinetBlock)) return;
        // TODO: Why the check above?
        if (renderState.hasItem) {
            renderItem(renderState, matrices, queue, 255);
        }
        final float positionConstant = 1.15f;
        renderFunction.execute(
                renderState.blockState,
                positionConstant, positionConstant, positionConstant,
                matrices, renderState.light, renderState.overlay
        );
    }
    #endif

    @SuppressWarnings("unused")
    static void fakeRunnable(
            BlockState blockState,
            float positionConstant, float positionConstantX, float positionConstantZ,
            PoseStack poseStack,
            #if MC_VERSION < 12111 MultiBufferSource bufferSource, #endif
            int light, int overlay
    ) {}

    @FunctionalInterface
    protected interface RenderFunction {
        void execute(
                BlockState blockState,
                float positionConstant, float positionConstantX, float positionConstantZ,
                PoseStack poseStack,
                #if MC_VERSION < 12111 MultiBufferSource bufferSource, #endif
                int light, int overlay
        );
    }
}