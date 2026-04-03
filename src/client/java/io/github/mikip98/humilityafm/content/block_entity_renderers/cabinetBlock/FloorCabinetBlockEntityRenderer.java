package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock;

#if MC_VERSION >= 12111
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.CabinetBlockEntityRenderState;
#endif
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.ItemFloorRendering;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.FloorCabinetBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
#if MC_VERSION >= 12105
import net.minecraft.world.phys.Vec3;
#endif
#if MC_VERSION >= 12111
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
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
            @NonNull FloorCabinetBlockEntity blockEntity,
            @NonNull CabinetBlockEntityRenderState renderState,
            float partialTick, @NonNull Vec3 cameraPos,
            @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPos, crumblingOverlay);
        renderState.extractSharedState(blockEntity);
    }

    @Override
    public void submit(
            CabinetBlockEntityRenderState renderState,
            @NonNull PoseStack poseStack,
            @NonNull SubmitNodeCollector collector,
            @NonNull CameraRenderState cameraState
    ) {
        // If there's no item, or the extraction failed, skip rendering to save frames
        // TODO: Check if these checks are required, if yes, move to the interface
        if (!renderState.hasItem || renderState.blockState == null) return;
        renderItem(renderState, poseStack, collector, renderState.light);
    }
    #endif
}