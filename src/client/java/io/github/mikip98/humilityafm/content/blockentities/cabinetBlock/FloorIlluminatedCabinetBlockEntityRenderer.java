package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.rendering.ItemFloorRendering;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.rendering.RenderSelfBrightening;
import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorIlluminatedCabinetBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class FloorIlluminatedCabinetBlockEntityRenderer implements BlockEntityRenderer<FloorIlluminatedCabinetBlockEntity>, ItemFloorRendering, RenderSelfBrightening {
    @SuppressWarnings("unused")
    public FloorIlluminatedCabinetBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(FloorIlluminatedCabinetBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = blockEntity.getWorld();
        BlockPos pos = blockEntity.getPos();
        if (world == null || pos == null) return;

        BlockState blockState = world.getBlockState(pos);
        if (blockState == null || !(blockState.getBlock() instanceof FloorIlluminatedCabinetBlock)) return;

        renderItem(blockEntity, blockState, matrices, vertexConsumers, 255, overlay);
        final float posisionConstant = 1.15f;
        renderSelfBrightening(blockState, posisionConstant, posisionConstant, posisionConstant, matrices, vertexConsumers, light, overlay);
    }
}