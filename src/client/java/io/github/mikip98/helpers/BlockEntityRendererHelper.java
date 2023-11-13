package io.github.mikip98.helpers;

public class BlockEntityRendererHelper {


    // Helper method to calculate combined light with brightness adjustment
    public static int calculateCombinedLight(int light, float brightness) {
//        return 0xF000F0;  // Max block light (15) and max skylight (15) - ChatGPT
//        return 0xFF0000;  // Max block light (15) and max skylight (15) - GitHub Copilot

        // Return adjusted brightness values
        int blockLight = light & 0xFF;
        int skyLight = (light >> 16) & 0xFF;

        // Adjust the brightness here, you may need to fine-tune the formula based on your needs
        blockLight = (int)(blockLight * brightness);
        skyLight = (int)(skyLight * brightness);

        // Clamp the values to ensure they stay within the valid range
        blockLight = Math.min(15, blockLight);
        skyLight = Math.min(15, skyLight);

        return blockLight | (skyLight << 16);
    }
}
