package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock;

#if MC_VERSION >= 12111
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.CabinetBlockEntityRenderState;
#endif
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.ItemWallRendering;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.CabinetBlockEntity;
#if MC_VERSION < 12111
import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
#endif
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
    public @NonNull CabinetBlockEntityRenderState createRenderState() {
        return new CabinetBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(
            @NonNull CabinetBlockEntity blockEntity,
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