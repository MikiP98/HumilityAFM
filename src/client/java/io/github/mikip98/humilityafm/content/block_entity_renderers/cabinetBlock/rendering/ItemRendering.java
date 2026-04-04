package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.ImplementedInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
#if MC_VERSION >= 12111
import net.minecraft.client.renderer.SubmitNodeCollector;
#endif
import net.minecraft.client.renderer.entity.ItemRenderer;
#if MC_VERSION < 12104
import net.minecraft.client.resources.model.BakedModel;
#endif
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
#if MC_VERSION >= 12104
import net.minecraft.world.level.block.entity.BlockEntity;
#endif
import net.minecraft.world.level.block.state.BlockState;

public sealed interface ItemRendering permits ItemFloorRendering, ItemWallRendering {
    PoseStack rotateMatrices(PoseStack poseStack, BlockState blockState);

    #if MC_VERSION < 12111
    default void renderItem(
            ImplementedInventory blockEntity, BlockState blockState,
            PoseStack poseStack, MultiBufferSource bufferSource,
            int packedLight, int packedOverlay
    ) {
        final ItemStack stack = blockEntity.getItem(0);
        if (stack.isEmpty()) return;

        float scale = 0.59375f;  // 19/32

        // Mandatory call before GL calls
        poseStack.pushPose();
        // Center in a block; Required for correct rotation
        poseStack.translate(0.5, 0.5, 0.5);
        // Rotate the item
        poseStack = rotateMatrices(poseStack, blockState);
        // X -> Left/Right; positive is left; negative is right
        // Y -> Height
        // Z -> Depth; positive is deeper; negative is closer
        poseStack.translate(0, 0, 0.4375 - 3f/64*(1-scale));
        poseStack.scale(scale, scale, scale);

        // Render the item inside the cabinet
        final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        #if MC_VERSION < 12104
        final BakedModel model = itemRenderer.getModel(stack, null, null, 0);
        itemRenderer.render(
                stack, ItemDisplayContext.FIXED, false, poseStack, bufferSource, packedLight, packedOverlay, model
        );
        #else
        itemRenderer.renderStatic(
                stack,
                ItemDisplayContext.FIXED,
                packedLight, packedOverlay,
                poseStack, bufferSource,
                ((BlockEntity) blockEntity).getLevel(),
                0
        );
        #endif
        // TODO: Why on 1.21.4+ I pass the world, but not before?

        // Mandatory call after GL calls
        poseStack.popPose();
    }
    #else
    default void renderItem(
            CabinetBlockEntityRenderState state,
            PoseStack poseStack, SubmitNodeCollector collector,
            int packedLight
    ) {
        float scale = 0.59375f;

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack = rotateMatrices(poseStack, state.blockState);
        poseStack.translate(0, 0, 0.4375 - 3f/64*(1-scale));
        poseStack.scale(scale, scale, scale);

        state.itemRenderState.submit(poseStack, collector, packedLight, state.overlay, 0);

        poseStack.popPose();
    }
    #endif
}
