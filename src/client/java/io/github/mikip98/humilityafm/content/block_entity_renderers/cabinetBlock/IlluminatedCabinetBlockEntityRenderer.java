package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock;

import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.ItemWallRendering;
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.RenderSelfBrightening;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntity;
import io.github.mikip98.humilityafm.content.blocks.cabinet.IlluminatedCabinetBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class IlluminatedCabinetBlockEntityRenderer implements BlockEntityRenderer<IlluminatedCabinetBlockEntity>, ItemWallRendering {
    protected static RenderFunction renderFunction = IlluminatedCabinetBlockEntityRenderer::fakeRunnable;
    public static void enableBrightening() { renderFunction = IlluminatedCabinetBlockEntityRenderer::selfBrighteningRunner; }
    public static void disableBrightening() { renderFunction = IlluminatedCabinetBlockEntityRenderer::fakeRunnable; }

    @SuppressWarnings("unused")
    public IlluminatedCabinetBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(
            IlluminatedCabinetBlockEntity blockEntity, float tickProgress,
            MatrixStack matrices, VertexConsumerProvider vertexConsumers,
            int light, int overlay, Vec3d cameraPos
    ) {
        World world = blockEntity.getWorld();
        BlockPos pos = blockEntity.getPos();
        if (world == null || pos == null) return;

        BlockState blockState = world.getBlockState(pos);
        if (blockState == null || !(blockState.getBlock() instanceof IlluminatedCabinetBlock)) return;

        renderItem(blockEntity, blockState, matrices, vertexConsumers, 255, overlay);
        renderFunction.execute(blockState, matrices, vertexConsumers, light, overlay);
    }

    protected static void selfBrighteningRunner(
            BlockState blockState,
            MatrixStack matrices, VertexConsumerProvider vertexConsumers,
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
                matrices, vertexConsumers, light, overlay
        );
    }
    @SuppressWarnings("unused")
    protected static void fakeRunnable(
            BlockState blockState,
            MatrixStack matrices, VertexConsumerProvider vertexConsumers,
            int light, int overlay
    ) {}

    @FunctionalInterface
    protected interface RenderFunction {
        void execute(
                BlockState blockState,
                MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                int light, int overlay
        );
    }
}