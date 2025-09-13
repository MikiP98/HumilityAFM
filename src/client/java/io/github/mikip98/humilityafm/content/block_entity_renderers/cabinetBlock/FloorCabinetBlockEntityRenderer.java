package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock;

import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering.ItemFloorRendering;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.FloorCabinetBlockEntity;
import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorCabinetBlock;
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
public class FloorCabinetBlockEntityRenderer implements BlockEntityRenderer<FloorCabinetBlockEntity>, ItemFloorRendering {
    @SuppressWarnings("unused")
    public FloorCabinetBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(FloorCabinetBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = blockEntity.getWorld();
        BlockPos pos = blockEntity.getPos();
        if (world == null || pos == null) return;

        BlockState blockState = world.getBlockState(pos);
        if (blockState == null || !(blockState.getBlock() instanceof FloorCabinetBlock)) return;

        renderItem(blockEntity, blockState, matrices, vertexConsumers, light, overlay);
    }
}