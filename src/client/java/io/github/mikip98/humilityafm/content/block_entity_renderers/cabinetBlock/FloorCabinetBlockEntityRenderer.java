package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock;

#if MC_VERSION >= 12111
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.CabinetBlockEntityRenderState;
#endif
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.ItemFloorRendering;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.FloorCabinetBlockEntity;
import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorCabinetBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
#if MC_VERSION >= 12105
import net.minecraft.world.phys.Vec3;
#endif

public class FloorCabinetBlockEntityRenderer implements
        BlockEntityRenderer<FloorCabinetBlockEntity #if MC_VERSION >= 12111, CabinetBlockEntityRenderState #endif>,
        ItemFloorRendering
{
    @SuppressWarnings("unused")
    public FloorCabinetBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

    #if MC_VERSION < 12111
    @Override
    public void render(
            FloorCabinetBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource,
            int packedLight, int packedOverlay #if MC_VERSION >= 12105, Vec3 cameraPos #endif
    ) {
        Level level = blockEntity.getLevel();
        BlockPos pos = blockEntity.getBlockPos();
        if (level == null) return;

        BlockState blockState = level.getBlockState(pos);
        if (!(blockState.getBlock() instanceof FloorCabinetBlock)) return;

        renderItem(blockEntity, blockState, poseStack, bufferSource, packedLight, packedOverlay);
    }
    #else
    @Override
    public CabinetBlockEntityRenderState createRenderState() {
        return new CabinetBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(
            FloorCabinetBlockEntity blockEntity,
            CabinetBlockEntityRenderState renderState,
            float partialTick, Vec3 cameraPos,
            @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlayCommand
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPos, crumblingOverlayCommand);
        renderState.extractSharedState(blockEntity);
    }

    @Override
    public void render(CabinetBlockEntityRenderState renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        // If there's no item, or the extraction failed, skip rendering to save frames
        // TODO: Check if these checks are required, if yes, move to the interface
        if (!renderState.hasItem || renderState.blockState == null) return;
        renderItem(renderState, matrices, queue, renderState.light);
    }
    #endif
}