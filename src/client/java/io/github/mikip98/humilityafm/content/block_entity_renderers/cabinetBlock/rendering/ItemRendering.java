package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering;

import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public sealed interface ItemRendering permits ItemFloorRendering, ItemWallRendering {
    MatrixStack rotateMatrices(MatrixStack matrices, BlockState blockState);

    default void renderItem(
            ImplementedInventory blockEntity, BlockState blockState,
            MatrixStack matrices, VertexConsumerProvider vertexConsumers,
            int light, int overlay
    ) {
        final ItemStack stack = blockEntity.getStack(0);
        if (stack.isEmpty()) return;

        float scale = 0.59375f;  // 19/32

        // Mandatory call before GL calls
        matrices.push();
        // Center in a block; Required for correct rotation
        matrices.translate(0.5, 0.5, 0.5);
        // Rotate the item
        matrices = rotateMatrices(matrices, blockState);
        // X -> Left/Right; positive is left; negative is right
        // Y -> Height
        // Z -> Depth; positive is deeper; negative is closer
        matrices.translate(0, 0, 0.4375 - 3f/64*(1-scale));
        matrices.scale(scale, scale, scale);

        // Render the item inside the cabinet
        final BakedModel model = MinecraftClient.getInstance().getItemRenderer().getModel(stack, null, null, 0);
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.FIXED, false, matrices, vertexConsumers, light, overlay, model);

        // Mandatory call after GL calls
        matrices.pop();
    }
}
