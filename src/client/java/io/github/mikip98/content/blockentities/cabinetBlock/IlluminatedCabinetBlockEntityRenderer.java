package io.github.mikip98.content.blockentities.cabinetBlock;

import io.github.mikip98.content.blocks.cabinet.CabinetBlock;
import io.github.mikip98.helpers.BlockEntityRendererHelper;
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
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import org.joml.Quaternionf;

import java.util.Objects;

import static io.github.mikip98.HumilityAFM.LOGGER;

@Environment(EnvType.CLIENT)
public class IlluminatedCabinetBlockEntityRenderer implements BlockEntityRenderer<IlluminatedCabinetBlockEntity> {

    public IlluminatedCabinetBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(IlluminatedCabinetBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        final ItemStack stack = blockEntity.getStack(0);

        float scaleY = 0.375f;
        Quaternionf rotation;

        // Move the item
        // If the item is a block, move it down a bit
//        if (stack.getItem() instanceof BlockItem) {
//            scaleY = 0.3125f;
//        }
        try {
            switch (blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(CabinetBlock.FACING)) {
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
        } catch (Exception e) {
            matrices.translate(0.5, scaleY, 0.1);
            LOGGER.error("Error rendering illuminated cabinet block entity");
        }

        //Actually render the item
        //MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(Items.JUKEBOX, 1), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
        final BakedModel model = MinecraftClient.getInstance().getItemRenderer().getModel(stack, null, null, 0);
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, false, matrices, vertexConsumers, 255, overlay, model);
        //15728640 + 255 = fully lit


        matrices.translate(blockEntity.getPos().getX(), blockEntity.getPos().getY(), blockEntity.getPos().getZ());

        // Get the block state from the world, you might need to replace it based on your block entity logic
        BlockState blockState = Objects.requireNonNull(blockEntity.getWorld()).getBlockState(blockEntity.getPos());

        // Render the LED strip block
        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(
                blockState, matrices, vertexConsumers,
                BlockEntityRendererHelper.calculateCombinedLight(light, 1.1f), overlay);


        // Mandatory call after GL calls
        matrices.pop();
    }
}