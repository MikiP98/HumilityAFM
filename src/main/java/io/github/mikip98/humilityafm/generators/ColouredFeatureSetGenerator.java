package io.github.mikip98.humilityafm.generators;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.content.blocks.LightStripBlock;
import io.github.mikip98.humilityafm.util.GenerationData;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.TorchBlock;
import net.minecraft.particle.ParticleTypes;

public class ColouredFeatureSetGenerator {
    public static final short colourCount = (short) GenerationData.vanillaColorPallet.length;
    public static Block[] colouredTorchWeakVariants = new Block[colourCount];
    public static Block[] colouredTorchVariants = new Block[colourCount];
    public static Block[] colouredTorchStrongVariants = new Block[colourCount];
    public static Block[] LightStripBlockVariants = new Block[colourCount];

    public static void init() {
        if (!ModConfig.enableColouredFeatureSetBeta) return;

        for (byte i = 0; i < GenerationData.vanillaColorPallet.length; ++i) {
            colouredTorchWeakVariants[i] = new TorchBlock(ParticleTypes.FLAME, AbstractBlock.Settings.copy(Blocks.TORCH).luminance((ignored) -> 7));
            colouredTorchVariants[i] = new TorchBlock(ParticleTypes.FLAME, AbstractBlock.Settings.copy(Blocks.TORCH).luminance((ignored) -> 11));
            colouredTorchStrongVariants[i] = new TorchBlock(ParticleTypes.FLAME, AbstractBlock.Settings.copy(Blocks.TORCH).luminance((ignored) -> 15));
            LightStripBlockVariants[i] = new LightStripBlock();
        }
    }
}
