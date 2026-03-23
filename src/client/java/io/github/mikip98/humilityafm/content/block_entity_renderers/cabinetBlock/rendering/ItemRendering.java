package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering;

import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.ImplementedInventory;
import net.minecraft.block.BlockState;
#if MC_VERSION >= 12104
import net.minecraft.block.entity.BlockEntity;
#endif
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
#if MC_VERSION < 12104
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
#endif
import net.minecraft.client.util.math.MatrixStack;
#if MC_VERSION >= 12108
import net.minecraft.item.ItemDisplayContext;
#endif
import net.minecraft.item.ItemStack;
#if MC_VERSION >= 12104 && MC_VERSION < 12108
import net.minecraft.item.ModelTransformationMode;
#endif

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
        #if MC_VERSION < 12104
        final BakedModel model = MinecraftClient.getInstance().getItemRenderer().getModel(stack, null, null, 0);
        MinecraftClient.getInstance().getItemRenderer().renderItem(
                stack, ModelTransformationMode.FIXED, false, matrices, vertexConsumers, light, overlay, model
        );
        #else
        MinecraftClient.getInstance().getItemRenderer().renderItem(
                stack,
                #if MC_VERSION < 12108 ModelTransformationMode.FIXED #else ItemDisplayContext.FIXED #endif,
                light, overlay,
                matrices, vertexConsumers,
                ((BlockEntity) blockEntity).getWorld(),
                0
        );
        #endif

        // Mandatory call after GL calls
        matrices.pop();
    }
}
