package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock;

#if MC_VERSION >= 12111
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.CabinetBlockEntityRenderState;
#endif
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.ItemWallRendering;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.CabinetBlockEntity;
import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
#if MC_VERSION >= 12105
import net.minecraft.world.phys.Vec3;
#endif

public class CabinetBlockEntityRenderer implements
        BlockEntityRenderer<CabinetBlockEntity #if MC_VERSION >= 12111, CabinetBlockEntityRenderState #endif>,
        ItemWallRendering
{
    @SuppressWarnings("unused")
    public CabinetBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

    #if MC_VERSION < 12111
    @Override
    public void render(
            CabinetBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource,
            int packedLight, int packedOverlay #if MC_VERSION >= 12105, Vec3 cameraPos #endif
    ) {
        final Level level = blockEntity.getLevel();
        final BlockPos pos = blockEntity.getBlockPos();
        if (level == null) return;

        final BlockState blockState = level.getBlockState(pos);
        if (!(blockState.getBlock() instanceof CabinetBlock)) return;

        renderItem(blockEntity, blockState, poseStack, bufferSource, packedLight, packedOverlay);
    }
    #else
    @Override
    public CabinetBlockEntityRenderState createRenderState() {
        return new CabinetBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(
            CabinetBlockEntity blockEntity,
            CabinetBlockEntityRenderState renderState,
            float partialTick, Vec3 cameraPos,
            @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlayCommand
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPos, crumblingOverlayCommand);
        renderState.extractSharedState(blockEntity);
    }

    @Override
    public void render(CabinetBlockEntityRenderState renderState, PoseStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        // If there's no item, or the extraction failed, skip rendering to save frames
        // TODO: Check if these checks are required, if yes, move to the interface
        if (!renderState.hasItem || renderState.blockState == null) return;
        renderItem(renderState, matrices, queue, renderState.light);
    }
    #endif
}