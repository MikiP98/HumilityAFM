package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class CabinetBlockEntityRenderer implements BlockEntityRenderer<CabinetBlockEntity> {
    @SuppressWarnings("unused")
    public CabinetBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(CabinetBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = blockEntity.getWorld();
        BlockPos pos = blockEntity.getPos();
        if (world == null || pos == null) return;

        BlockState blockState = world.getBlockState(pos);
        if (blockState == null || !(blockState.getBlock() instanceof CabinetBlock)) return;

        renderItem(blockEntity, blockState, matrices, vertexConsumers, light, overlay);
    }

    public static void renderItem(CabinetBlockEntity blockEntity, BlockState blockState, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        final ItemStack stack = blockEntity.getStack(0);
        if (stack.isEmpty()) return;

        matrices.push();

        // Center in a block; Required for correct rotation
        matrices.translate(0.5, 0.5, 0.5);

        switch (blockState.get(CabinetBlock.FACING)) {
            case SOUTH -> matrices.multiply(new Quaternionf().rotationYXZ((float) Math.toRadians(180), 0, 0));
            case EAST -> matrices.multiply(new Quaternionf().rotationYXZ((float) Math.toRadians(270), 0, 0));
            case WEST -> matrices.multiply(new Quaternionf().rotationYXZ((float) Math.toRadians(90), 0, 0));
        }

        // X -> Left/Right; positive is left; negative is right
        // Y -> Height
        // Z -> Depth; positive is deeper; negative is closer
        float scale = 0.59375f;  // 19/32
        matrices.translate(0, 0, 0.4375 - 3f/64*(1-scale));  // Z is 7/16 - 3f/64*(1-scale)
        matrices.scale(scale, scale, scale);

        // Render the item inside the cabinet
        final BakedModel model = MinecraftClient.getInstance().getItemRenderer().getModel(stack, null, null, 0);
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.FIXED, false, matrices, vertexConsumers, light, overlay, model);

        // Mandatory call after GL calls
        matrices.pop();
    }
}