package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock;

#if MC_VERSION >= 12111
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.CabinetBlockEntityRenderState;
#endif
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.ItemWallRendering;
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.RenderSelfBrightening;
#if MC_VERSION >= 12111
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.CabinetBlockEntity;
#endif
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntity;
import io.github.mikip98.humilityafm.content.blocks.cabinet.IlluminatedCabinetBlock;
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
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
#if MC_VERSION >= 12105
import net.minecraft.util.math.Vec3d;
#endif
import net.minecraft.world.World;
#if MC_VERSION >= 12111
import org.jetbrains.annotations.Nullable;
#endif

@Environment(EnvType.CLIENT)
public class IlluminatedCabinetBlockEntityRenderer implements
        BlockEntityRenderer<IlluminatedCabinetBlockEntity #if MC_VERSION >= 12111, CabinetBlockEntityRenderState #endif>,
        ItemWallRendering
{
    protected static RenderFunction renderFunction = IlluminatedCabinetBlockEntityRenderer::fakeRunnable;
    public static void enableBrightening() { renderFunction = IlluminatedCabinetBlockEntityRenderer::selfBrighteningRunner; }
    public static void disableBrightening() { renderFunction = IlluminatedCabinetBlockEntityRenderer::fakeRunnable; }

    @SuppressWarnings("unused")
    public IlluminatedCabinetBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    #if MC_VERSION < 12111
    @Override
    public void render(
            IlluminatedCabinetBlockEntity blockEntity, float tickDelta,
            MatrixStack matrices, VertexConsumerProvider vertexConsumers,
            int light, int overlay #if MC_VERSION >= 12105, Vec3d cameraPos #endif
    ) {
        World world = blockEntity.getWorld();
        BlockPos pos = blockEntity.getPos();
        if (world == null || pos == null) return;

        BlockState blockState = world.getBlockState(pos);
        if (blockState == null || !(blockState.getBlock() instanceof IlluminatedCabinetBlock)) return;

        renderItem(blockEntity, blockState, matrices, vertexConsumers, 255, overlay);
        renderFunction.execute(blockState, matrices, vertexConsumers, light, overlay);
    }
    #else
    @Override
    public CabinetBlockEntityRenderState createRenderState() {
        return new CabinetBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(
            IlluminatedCabinetBlockEntity blockEntity,
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
        renderFunction.execute(renderState.blockState, matrices, renderState.light, renderState.overlay);
    }
    #endif

    protected static void selfBrighteningRunner(
            BlockState blockState,
            MatrixStack matrices,
            #if MC_VERSION < 12111 VertexConsumerProvider vertexConsumers, #endif
            int light, int overlay
    ) {
        final float posisionConstant = 1.15f;
        float posisionConstantX = posisionConstant;
        float posisionConstantZ = posisionConstant;

        switch (blockState.get(Properties.HORIZONTAL_FACING)) {
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
                matrices,
                #if MC_VERSION < 12111 vertexConsumers, #endif
                light, overlay
        );
    }
    @SuppressWarnings("unused")
    protected static void fakeRunnable(
            BlockState blockState,
            MatrixStack matrices,
            #if MC_VERSION < 12111 VertexConsumerProvider vertexConsumers, #endif
            int light, int overlay
    ) {}

    @FunctionalInterface
    protected interface RenderFunction {
        void execute(
                BlockState blockState,
                MatrixStack matrices,
                #if MC_VERSION < 12111 VertexConsumerProvider vertexConsumers, #endif
                int light, int overlay
        );
    }
}