package io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns;

import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class JackOLanternSoul extends JackOLantern {
    // Luminance of Soul Torch boosted by 1 as it was too dark
    public static final Settings defaultSettings = defaultSettingsSupplier.get().luminance((ignored) -> 8+1);

    public JackOLanternSoul(Settings settings) {
        super(settings);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(4) == 0) {
            #if MC_VERSION < 12105
            world.addParticle(
            #else
            world.addParticleClient(
            #endif
                    ParticleTypes.SOUL,
                    pos.getX() + 0.5 + (random.nextDouble() - 0.5) * (random.nextInt(4) + 1),
                    pos.getY() + 0.5 + (random.nextDouble() - 0.5) * (random.nextInt(4) + 1),
                    pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * (random.nextInt(4) + 1),
                    0.0, 0.0, 0.0
            );
        }
        if (random.nextInt(12) == 0) {
            #if MC_VERSION < 12105
            world.addParticle(
            #else
            world.addParticleClient(
            #endif
                    ParticleTypes.SOUL_FIRE_FLAME,
                    pos.getX() + random.nextDouble() * random.nextInt(3),
                    pos.getY() + random.nextDouble() * random.nextInt(3),
                    pos.getZ() + random.nextDouble() * random.nextInt(3),
                    0.0, 0.0, 0.0
            );
        }
    }
}
