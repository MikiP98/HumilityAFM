package io.github.mikip98.humilityafm.generators;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.content.blocks.light_strips.LightStripBlock;
import io.github.mikip98.humilityafm.util.GenerationData;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.TorchBlock;
import net.minecraft.particle.ParticleTypes;

public class ColouredLightsGenerator {
    public static final short colourCount = (short) GenerationData.vanillaColorPallet.length;

    public static String[] colouredTorchesNames = new String[colourCount*3];
    public static Block[] colouredTorchesVariants = new Block[colourCount*3];

    public static Block[] LightStripBlockVariants;

    public static void init() {
        if (ModConfig.enableLightStrips) {
            LightStripBlockVariants = new Block[colourCount];
        }

        short i = 0;
        for (String colour : GenerationData.vanillaColorPallet) {
            colouredTorchesNames[i*3] = colour + "_weak";
            colouredTorchesNames[i*3+1] = colour;
            colouredTorchesNames[i*3+2] = colour + "_strong";
            colouredTorchesVariants[i*3] = new TorchBlock(FabricBlockSettings.create().luminance(5), ParticleTypes.FLAME);
            colouredTorchesVariants[i*3+1] = new TorchBlock(FabricBlockSettings.create().luminance(10), ParticleTypes.FLAME);
            colouredTorchesVariants[i*3+2] = new TorchBlock(FabricBlockSettings.create().luminance(15), ParticleTypes.FLAME);

            if (ModConfig.enableLightStrips) LightStripBlockVariants[i] = new LightStripBlock();
            ++i;
        }
    }
}
