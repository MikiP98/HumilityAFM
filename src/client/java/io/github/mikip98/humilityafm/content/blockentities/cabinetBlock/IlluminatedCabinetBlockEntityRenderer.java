package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.IlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.helpers.BlockEntityRendererHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class IlluminatedCabinetBlockEntityRenderer implements BlockEntityRenderer<IlluminatedCabinetBlockEntity> {
    @SuppressWarnings("unused")
    public IlluminatedCabinetBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(IlluminatedCabinetBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = blockEntity.getWorld();
        BlockPos pos = blockEntity.getPos();
        if (world == null || pos == null) return;

        BlockState blockState = world.getBlockState(pos);
        if (blockState == null || !(blockState.getBlock() instanceof IlluminatedCabinetBlock)) return;


        CabinetBlockEntityRenderer.renderItem(blockEntity, blockState, matrices, vertexConsumers, 255, overlay);


        final float blockSizeYZ = 0.875f;
        final float posisionConstant = 1.15f;

        final float blockSizeX = 0.25f;
        float posisionConstantX = posisionConstant;
        float posisionConstantZ = posisionConstant;

        switch (blockState.get(CabinetBlock.FACING)) {
            case NORTH -> {
                posisionConstantX = 4f;
                posisionConstantZ = 2f;
            }
            case SOUTH -> {
                posisionConstantX = 4f;
                posisionConstantZ = 0.3f;
            }
            case WEST -> posisionConstantX = 7f;
        }

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
        matrices.push();

        matrices.translate(
                -blockSizeX /2*(scale-1)*posisionConstantX,
                -blockSizeYZ/2*(scale-1)*posisionConstant,
                -blockSizeYZ/2*(scale-1)*posisionConstantZ
        );
        matrices.scale(scale, scale, scale);

        int outsideLight = BlockEntityRendererHelper.multiplyLight(light, lightMultiplayer);
        outsideLight = BlockEntityRendererHelper.addLight(outsideLight, lightAddition);

        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(
                blockState, matrices, vertexConsumers, outsideLight, overlay); // 0xF000F0

        matrices.pop();
    }
}