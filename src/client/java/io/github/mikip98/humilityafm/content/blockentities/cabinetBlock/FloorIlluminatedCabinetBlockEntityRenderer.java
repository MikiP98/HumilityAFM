package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorIlluminatedCabinetBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class FloorIlluminatedCabinetBlockEntityRenderer implements BlockEntityRenderer<FloorIlluminatedCabinetBlockEntity> {
    @SuppressWarnings("unused")
    public FloorIlluminatedCabinetBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(FloorIlluminatedCabinetBlockEntity blockEntity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {
        World world = blockEntity.getWorld();
        BlockPos pos = blockEntity.getPos();
        if (world == null || pos == null) return;

        BlockState blockState = world.getBlockState(pos);
        if (blockState == null || !(blockState.getBlock() instanceof FloorIlluminatedCabinetBlock)) return;


        FloorCabinetBlockEntityRenderer.renderItem(blockEntity, blockState, matrices, vertexConsumers, 255, overlay);


        final float blockSizeYZ = 0.875f;
        final float posisionConstant = 1.15f;

        final float blockSizeX = 0.25f;
        float posisionConstantX = posisionConstant;
        float posisionConstantZ = posisionConstant;

//        switch (blockState.get(Properties.HORIZONTAL_FACING)) {
//            case NORTH -> {
//                posisionConstantX = 4f;
//                posisionConstantZ = 2f;
//            }
//            case SOUTH -> {
//                posisionConstantX = 4f;
//                posisionConstantZ = 0.3f;
//            }
//            case WEST -> posisionConstantX = 7f;
//        }

        // Render the brightened outside shell of the cabinet
        renderSelf(
                1.1f, 1,
                blockSizeX, blockSizeYZ, posisionConstant, posisionConstantX, posisionConstantZ, 1.005f,
                blockState, matrices, vertexConsumers, light, overlay
        );

        // Render the brightened inside walls of the cabinet
        renderSelf(
                1.15f, 3,
                blockSizeX, blockSizeYZ, posisionConstant, posisionConstantX, posisionConstantZ, 0.992f,
                blockState, matrices, vertexConsumers, light, overlay
        );
    }

    protected static void renderSelf(
            float lightMultiplayer, int lightAddition,
            float blockSizeX, float blockSizeYZ, float posisionConstant, float posisionConstantX, float posisionConstantZ, float scale,
            BlockState blockState, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay
    ) {
        IlluminatedCabinetBlockEntityRenderer.renderSelf(lightMultiplayer, lightAddition, blockSizeX, blockSizeYZ, posisionConstant, posisionConstantX, posisionConstantZ, scale, blockState, matrices, vertexConsumers, light, overlay);
    }
}