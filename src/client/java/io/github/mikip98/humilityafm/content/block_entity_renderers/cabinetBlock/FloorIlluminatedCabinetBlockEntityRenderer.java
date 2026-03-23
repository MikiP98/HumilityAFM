package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock;

#if MC_VERSION >= 12111
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.CabinetBlockEntityRenderState;
#endif
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.ItemFloorRendering;
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.RenderSelfBrightening;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.FloorIlluminatedCabinetBlockEntity;
#if MC_VERSION >= 12111
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntity;
#endif
import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorIlluminatedCabinetBlock;
#if MC_VERSION >= 12111
import io.github.mikip98.humilityafm.content.blocks.cabinet.IlluminatedCabinetBlock;
#endif
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
#if MC_VERSION >= 12111
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
public class FloorIlluminatedCabinetBlockEntityRenderer implements
        BlockEntityRenderer<FloorIlluminatedCabinetBlockEntity #if MC_VERSION >= 12111, CabinetBlockEntityRenderState #endif>,
        ItemFloorRendering
{
    protected static RenderFunction renderFunction = FloorIlluminatedCabinetBlockEntityRenderer::fakeRunnable;
    public static void enableBrightening() { renderFunction = RenderSelfBrightening::renderSelfBrightening; }
    public static void disableBrightening() { renderFunction = FloorIlluminatedCabinetBlockEntityRenderer::fakeRunnable; }

    @SuppressWarnings("unused")
    public FloorIlluminatedCabinetBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    #if MC_VERSION < 12111
    @Override
    public void render(FloorIlluminatedCabinetBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay #if MC_VERSION >= 12105, Vec3d cameraPos #endif) {
        World world = blockEntity.getWorld();
        BlockPos pos = blockEntity.getPos();
        if (world == null || pos == null) return;

        BlockState blockState = world.getBlockState(pos);
        if (blockState == null || !(blockState.getBlock() instanceof FloorIlluminatedCabinetBlock)) return;

        renderItem(blockEntity, blockState, matrices, vertexConsumers, 255, overlay);
        final float positionConstant = 1.15f;
        renderFunction.execute(blockState, positionConstant, positionConstant, positionConstant, matrices, vertexConsumers, light, overlay);
    }
    #else@Override
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
            MatrixStack matrices,
            #if MC_VERSION < 12111 VertexConsumerProvider vertexConsumers, #endif
            int light, int overlay
    ) {}

    @FunctionalInterface
    protected interface RenderFunction {
        void execute(
                BlockState blockState,
                float positionConstant, float positionConstantX, float positionConstantZ,
                MatrixStack matrices,
                #if MC_VERSION < 12111 VertexConsumerProvider vertexConsumers, #endif
                int light, int overlay
        );
    }
}