package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock;

#if MC_VERSION >= 12111
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.CabinetBlockEntityRenderState;
#endif
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.ItemWallRendering;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.CabinetBlockEntity;
import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
#if MC_VERSION >= 12111
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
#endif
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
#if MC_VERSION >= 12105
import net.minecraft.util.math.Vec3d;
#endif
import net.minecraft.world.World;
#if MC_VERSION >= 12111
import org.jetbrains.annotations.Nullable;
#endif

@Environment(EnvType.CLIENT)
public class CabinetBlockEntityRenderer implements
        BlockEntityRenderer<CabinetBlockEntity #if MC_VERSION >= 12111, CabinetBlockEntityRenderState #endif>,
        ItemWallRendering
{
    @SuppressWarnings("unused")
    public CabinetBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    #if MC_VERSION < 12111
    @Override
    public void render(CabinetBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay #if MC_VERSION >= 12105, Vec3d cameraPos #endif) {
        World world = blockEntity.getWorld();
        BlockPos pos = blockEntity.getPos();
        if (world == null || pos == null) return;

        BlockState blockState = world.getBlockState(pos);
        if (blockState == null || !(blockState.getBlock() instanceof CabinetBlock)) return;

        renderItem(blockEntity, blockState, matrices, vertexConsumers, light, overlay);
    }
    #else
    @Override
    public CabinetBlockEntityRenderState createRenderState() {
        return new CabinetBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(
            CabinetBlockEntity blockEntity,
            CabinetBlockEntityRenderState renderState,
            float tickDelta, Vec3d cameraPos,
            @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlayCommand
    ) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, renderState, tickDelta, cameraPos, crumblingOverlayCommand);
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