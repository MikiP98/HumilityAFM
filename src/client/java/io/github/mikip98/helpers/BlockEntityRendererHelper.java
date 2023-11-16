package io.github.mikip98.helpers;

import io.github.mikip98.content.blocks.cabinet.IlluminatedCabinetBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static io.github.mikip98.HumilityAFM.LOGGER;

public class BlockEntityRendererHelper {
    int maxLight = 0xF000F0;  // Max block light (15) and max skylight (15)

    // Helper method to calculate combined light with brightness adjustment
    public static int multiplyLight(int light, float brightness) {
//        LOGGER.info("Light: " + light);

        // Extract 4 bits from the first position
        int light1 = (light >> 20) & 0b1111;
        // Extract 4 bits from the second position
        int light2 = (light >> 4) & 0b1111;

        // Adjust the brightness here, you may need to fine-tune the formula based on your needs
        light1 = (int)(light1 * brightness);
        light2 = (int)(light2 * brightness);

        // Clamp the values to ensure they stay within the valid range
        light1 = Math.min(15, light1);
        light2 = Math.min(15, light2);

        int combinedLight = ((light1 & 0b1111) << 20) | ((light2 & 0b1111) << 4);
//        LOGGER.info("Combined light: " + combinedLight);

        return combinedLight;
    }

    // method to add fixed light amount to the block light
    public static int addLight(int light, int lightToAdd) {
        // Extract 4 bits from the first position
        int light1 = (light >> 20) & 0b1111;
        // Extract 4 bits from the second position
        int light2 = (light >> 4) & 0b1111;

        // Add the light amount here, you may need to fine-tune the formula based on your needs
        light1 += lightToAdd;
        light2 += lightToAdd;

        // Clamp the values to ensure they stay within the valid range
        light1 = Math.min(15, light1);
        light2 = Math.min(15, light2);

        return ((light1 & 0b1111) << 20) | ((light2 & 0b1111) << 4);
    }


    // Helper method to check if the block entity is valiable for rendering
    public static boolean entityDestroyed(BlockEntity entity) {
        World world = entity.getWorld();
        BlockPos pos = entity.getPos();

        if (world == null || pos == null) {
            // If the world or position is null, return early
            return true;
        }

        BlockState blockState = world.getBlockState(pos);

        // If the block state is null or not an instance of LEDStripBlock, return early
        return blockState == null || !(blockState.getBlock() instanceof IlluminatedCabinetBlock);
    }

}
