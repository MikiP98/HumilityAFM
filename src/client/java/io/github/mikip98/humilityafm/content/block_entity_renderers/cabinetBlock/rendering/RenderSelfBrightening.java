package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mikip98.humilityafm.content.block_entity_renderers.rendering_utils.LightManipulation;
import net.minecraft.client.Minecraft;
#if MC_VERSION < 260000
import net.minecraft.client.renderer.MultiBufferSource;
#endif
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.world.level.block.state.BlockState;

public interface RenderSelfBrightening {
    static void renderSelfBrightening(
            BlockState blockState,
            float posisionConstant, float posisionConstantX, float posisionConstantZ,
            PoseStack poseStack,
            #if MC_VERSION < 12111 MultiBufferSource bufferSource, #endif
            int packedLight, int packedOverlay
    ) {
        final float blockSizeYZ = 0.875f;
        final float blockSizeX = 0.25f;

        // Render the brightened outside shell of the cabinet
        renderSelf(
                1.1f, 1,
                blockSizeX, blockSizeYZ, posisionConstant, posisionConstantX, posisionConstantZ, 1.005f,
                blockState, poseStack, #if MC_VERSION < 12111 bufferSource, #endif packedLight, packedOverlay
        );

        // Render the brightened inside walls of the cabinet
        renderSelf(
                1.15f, 3,
                blockSizeX, blockSizeYZ, posisionConstant, posisionConstantX, posisionConstantZ, 0.992f,
                blockState, poseStack, #if MC_VERSION < 12111 bufferSource, #endif packedLight, packedOverlay
        );
    }

    static void renderSelf(
            float lightMultiplayer, int lightAddition,
            float blockSizeX, float blockSizeYZ,
            float posisionConstant, float posisionConstantX, float posisionConstantZ, float scale,
            BlockState blockState, PoseStack poseStack,
            #if MC_VERSION < 12111 MultiBufferSource bufferSource, #endif
            int packedLight, int packedOverlay
    ) {
        poseStack.pushPose();

        poseStack.translate(
                -blockSizeX /2*(scale-1)*posisionConstantX,
                -blockSizeYZ/2*(scale-1)*posisionConstant,
                -blockSizeYZ/2*(scale-1)*posisionConstantZ
        );
        poseStack.scale(scale, scale, scale);

        int outsideLight = LightManipulation.multiplyLight(packedLight, lightMultiplayer);
        outsideLight = LightManipulation.addLight(outsideLight, lightAddition);

        #if MC_VERSION >= 12111 && MC_VERSION < 260000
        MultiBufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        #endif

        #if MC_VERSION < 260000
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                blockState, poseStack,
                bufferSource,
                outsideLight, packedOverlay
        );
        #else
        // TODO !!!
        BlockModel selfModel = Minecraft.getInstance().getModelManager().getBlockModelSet().get(blockState);

        #endif

        poseStack.popPose();
    }
}
