package io.github.mikip98.helpers;

import io.github.mikip98.content.blocks.cabinet.IlluminatedCabinetBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static io.github.mikip98.HumilityAFM.LOGGER;

public class BlockEntityRendererHelper {

    // Helper method to check if the block entity is valiable for rendering
    public static boolean entityDestroyed(BlockEntity entity) {
        World world = entity.getWorld();
        BlockPos pos = entity.getPos();

        if (world == null || pos == null) {
            // If the world or position is null, return early
            return true;
        }

        BlockState blockState = world.getBlockState(pos);

        if (blockState == null || !(blockState.getBlock() instanceof IlluminatedCabinetBlock)) {
            // If the block state is null or not an instance of LEDStripBlock, return early
            return true;
        }

        return false;
    }

    // Helper method to calculate combined light with brightness adjustment
    public static int calculateCombinedLight(int light, float brightness) {
//        return 0xF000F0;  // Max block light (15) and max skylight (15) - ChatGPT
//        return 0xFF0000;  // Max block light (15) and max skylight (15) - GitHub Copilot
//        LOGGER.info("Light: " + light);

        // Extract 3 bits from the first 'F' position
        int light1 = (light >> 20) & 0b1111;
        // Extract 3 bits from the second 'F' position
        int light2 = (light >> 4) & 0b1111;

//        int blockLight = light & 0xFF;
//        int skyLight = (light >> 16) & 0xFF;

        // Adjust the brightness here, you may need to fine-tune the formula based on your needs
        light1 = (int)(light1 * brightness);
        light2 = (int)(light2 * brightness);

        // Clamp the values to ensure they stay within the valid range
        light1 = Math.min(15, light1);
        light2 = Math.min(15, light2);

        int combinedLight = ((light1 & 0b1111) << 20) | ((light2 & 0b1111) << 4); // (light1 << 16) | (light2 << 4)
//        LOGGER.info("Combined light: " + combinedLight);



        // Original decimal value
        int originalValue = light;

        // Split the original value into two 3-bit numbers
        int firstNumber = (originalValue >> 20) & 0b1111;
        int secondNumber = (originalValue >> 4) & 0b1111;

        // Multiply both numbers by 1.5 and clamp to 15
        int multipliedFirst = Math.min((int) (firstNumber * 1.5), 15);
        int multipliedSecond = Math.min((int) (secondNumber * 1.5), 15);

        // Merge the two numbers back into the original decimal value
        int mergedValue = ((multipliedFirst & 0b1111) << 20) | ((multipliedSecond & 0b1111) << 4);

        // Print the results
//        System.out.println("Original Value: " + originalValue);
//        System.out.println("First Number: " + firstNumber);
//        System.out.println("Second Number: " + secondNumber);
//        System.out.println("Multiplied and Clamped First: " + multipliedFirst);
//        System.out.println("Multiplied and Clamped First: " + Integer.toHexString(multipliedFirst));
//        System.out.println("Multiplied and Clamped Second: " + multipliedSecond);
//        System.out.println("Multiplied and Clamped Second: " + Integer.toHexString(multipliedSecond));
//        System.out.println("Merged Value: " + mergedValue);
//        System.out.println("Merged Value: " + Integer.toHexString(mergedValue));



        return combinedLight;
//        return blockLight | (skyLight << 16);
    }
}
