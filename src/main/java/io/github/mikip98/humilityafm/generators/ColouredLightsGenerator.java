package io.github.mikip98.humilityafm.generators;

import io.github.mikip98.humilityafm.content.blocks.LightStripBlock;
import io.github.mikip98.humilityafm.util.GenerationData;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.TorchBlock;
import net.minecraft.particle.ParticleTypes;

public class ColouredLightsGenerator {
    public static final short colourCount = (short) GenerationData.vanillaColorPallet.length;
    public static Block[] colouredTorchWeakVariants = new Block[colourCount];
    public static Block[] colouredTorchVariants = new Block[colourCount];
    public static Block[] colouredTorchStrongVariants = new Block[colourCount];
    public static Block[] LightStripBlockVariants = new Block[colourCount];

    public static void init() {
        for (byte i = 0; i < GenerationData.vanillaColorPallet.length; ++i) {
            colouredTorchWeakVariants[i] = new TorchBlock(FabricBlockSettings.copyOf(Blocks.TORCH).luminance(7), ParticleTypes.FLAME);
            colouredTorchVariants[i] = new TorchBlock(FabricBlockSettings.copyOf(Blocks.TORCH).luminance(11), ParticleTypes.FLAME);
            colouredTorchStrongVariants[i] = new TorchBlock(FabricBlockSettings.copyOf(Blocks.TORCH).luminance(15), ParticleTypes.FLAME);
            LightStripBlockVariants[i] = new LightStripBlock();
        }
    }
}
