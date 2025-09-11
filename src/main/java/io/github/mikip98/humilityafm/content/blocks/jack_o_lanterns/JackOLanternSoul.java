package io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns;

import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class JackOLanternSoul extends JackOLantern {
    public static Settings defaultSettings = getDefaultSettings().luminance((ignored) -> 8);
    public JackOLanternSoul(Settings settings) {
        super(settings);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(4) == 0) {
            world.addParticle(
                    ParticleTypes.SOUL,
                    pos.getX() + 0.5 + (random.nextDouble() - 0.5) * (random.nextInt(4) + 1),
                    pos.getY() + 0.5 + (random.nextDouble() - 0.5) * (random.nextInt(4) + 1),
                    pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * (random.nextInt(4) + 1),
                    0.0, 0.0, 0.0
            );
        }
        if (random.nextInt(12) == 0) {
            world.addParticle(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    pos.getX() + random.nextDouble() * random.nextInt(3),
                    pos.getY() + random.nextDouble() * random.nextInt(3),
                    pos.getZ() + random.nextDouble() * random.nextInt(3),
                    0.0, 0.0, 0.0
            );
        }
    }
}
