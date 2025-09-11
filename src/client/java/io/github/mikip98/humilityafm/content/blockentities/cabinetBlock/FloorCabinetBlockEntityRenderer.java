package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class FloorCabinetBlockEntityRenderer implements BlockEntityRenderer<FloorCabinetBlockEntity> {
    @SuppressWarnings("unused")
    public FloorCabinetBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(FloorCabinetBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = entity.getWorld();
        BlockPos pos = entity.getPos();
        if (world == null || pos == null) return;

        BlockState blockState = world.getBlockState(pos);
        if (blockState == null || !(blockState.getBlock() instanceof CabinetBlock)) return;

        renderItem(entity, blockState, matrices, vertexConsumers, light, overlay);
    }

    public static void renderItem(CabinetBlockEntity blockEntity, BlockState blockState, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        final ItemStack stack = blockEntity.getStack(0);
        if (stack.isEmpty()) return;

        float scale = 0.59375f;  // 19/32

        matrices.push();

        // Center in a block; Required for correct rotation
        matrices.translate(0.5, 0.5, 0.5);

        float rotationX = (float) (blockState.get(Properties.BLOCK_HALF) == BlockHalf.BOTTOM ? Math.toRadians(90) : Math.toRadians(270));
        switch (blockState.get(Properties.HORIZONTAL_FACING)) {
            case NORTH -> matrices.multiply(new Quaternionf().rotationYXZ(0, rotationX, 0));
            case SOUTH -> matrices.multiply(new Quaternionf().rotationYXZ((float) Math.toRadians(180), rotationX, 0));
            case EAST -> matrices.multiply(new Quaternionf().rotationYXZ((float) Math.toRadians(270), rotationX, 0));
            case WEST -> matrices.multiply(new Quaternionf().rotationYXZ((float) Math.toRadians(90), rotationX, 0));
        }

        // X -> Left/Right; positive is left; negative is right
        // Y -> Height
        // Z -> Depth; positive is deeper; negative is closer
        matrices.translate(0, 0, 0.4375 - 3f/64*(1-scale));
        matrices.scale(scale, scale, scale);

        // Render the item inside the cabinet
        MinecraftClient.getInstance().getItemRenderer().renderItem(
                stack,
                ModelTransformationMode.FIXED,
                light,
                overlay,
                matrices,
                vertexConsumers,
                blockEntity.getWorld(),
                0
        );

        // Mandatory call after GL calls
        matrices.pop();
    }
}