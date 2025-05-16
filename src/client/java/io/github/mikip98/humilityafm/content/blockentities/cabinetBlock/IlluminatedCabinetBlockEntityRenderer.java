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
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class IlluminatedCabinetBlockEntityRenderer implements BlockEntityRenderer<IlluminatedCabinetBlockEntity> {
    @SuppressWarnings("unused")
    public IlluminatedCabinetBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(IlluminatedCabinetBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        World world = blockEntity.getWorld();
        BlockPos pos = blockEntity.getPos();

        if (world == null || pos == null) {
            // If the world or position is null, return early
            matrices.pop();
            return;
        }

        BlockState blockState = world.getBlockState(pos);

        if (blockState == null || !(blockState.getBlock() instanceof IlluminatedCabinetBlock)) {
            matrices.pop();
            return;
        }

        final ItemStack stack = blockEntity.getStack(0);

        float scaleY = 0.375f;
        Quaternionf rotation;

        // Move the item
        // If the item is a block, move it down a bit
//        if (stack.getItem() instanceof BlockItem) {
//            scaleY = 0.3125f;
//        }
        switch (blockState.get(CabinetBlock.FACING)) {
            case NORTH -> {
                matrices.translate(0.5, scaleY, 0.9);

                rotation = new Quaternionf().rotationYXZ((float) Math.toRadians(180), 0.0f, 0.0f); // Create a rotation quaternion for a 180-degree rotation around the Y-axis
                matrices.multiply(rotation);
            }
            case SOUTH -> matrices.translate(0.5, scaleY, 0.1);
            case EAST -> {
                matrices.translate(0.1, scaleY, 0.5);
                rotation = new Quaternionf().rotationYXZ((float) Math.toRadians(90), 0.0f, 0.0f);
                matrices.multiply(rotation);
            }
            case WEST -> {
                matrices.translate(0.9, scaleY, 0.5);
                rotation = new Quaternionf().rotationYXZ((float) Math.toRadians(270), 0.0f, 0.0f);
                matrices.multiply(rotation);
            }
        }

        //Actually render the item
        //MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(Items.JUKEBOX, 1), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
        final BakedModel model = MinecraftClient.getInstance().getItemRenderer().getModel(stack, null, null, 0);
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, false, matrices, vertexConsumers, 255, overlay, model);
        //15728640 + 255 = fully lit

        matrices.pop(); // Pop before rendering the block
        matrices.push(); // Push again to render the block

        float blockSizeYZ = 0.875f;
//        byte posisionConstant = 8;
        float posisionConstant = 1.15f;

        float blockSizeX = 0.25f;
        float posisionConstantX = posisionConstant;
        float posisionConstantZ = posisionConstant;

        float scale = 1.005f;

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

        matrices.translate(-blockSizeX/2*(scale-1)*posisionConstantX, -blockSizeYZ/2*(scale-1)*posisionConstant, -blockSizeYZ/2*(scale-1)*posisionConstantZ);
        matrices.scale(scale, scale, scale);

        // Render the LED strip block
        int outsideLight = BlockEntityRendererHelper.multiplyLight(light, 1.1f);
        outsideLight = BlockEntityRendererHelper.addLight(outsideLight, 1);

        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(
                blockState, matrices, vertexConsumers, outsideLight, overlay); // 0xF000F0

        matrices.pop();
        matrices.push();

        scale = 0.992f;

        matrices.translate(-blockSizeX/2*(scale-1)*posisionConstantX, -blockSizeYZ/2*(scale-1)*posisionConstant, -blockSizeYZ/2*(scale-1)*posisionConstantZ);
        matrices.scale(scale, scale, scale);

        // Brighten the inside of the illuminated cabinet
        int insideLight = BlockEntityRendererHelper.multiplyLight(light, 1.15f);
        insideLight = BlockEntityRendererHelper.addLight(insideLight, 3);

        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(
                blockState, matrices, vertexConsumers, insideLight, overlay); // 0xF000F0

        matrices.pop();
    }
}