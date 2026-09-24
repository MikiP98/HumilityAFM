package io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns;

#if MC_VERSION >= 12003 && MC_VERSION < 260300 import com.mojang.serialization.MapCodec; #endif
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class JackOLanternSoul extends JackOLantern {
    #if MC_VERSION >= 12003 && MC_VERSION < 260300
    protected static final MapCodec<JackOLanternSoul> CODEC = simpleCodec(JackOLanternSoul::new);

    @Override
    protected @NotNull MapCodec<? extends JackOLanternSoul> codec() {
        return CODEC;
    }
    #endif

    // Luminance of Soul Torch boosted by 1 as it was too dark
    public static final Properties defaultSettings = defaultSettingsSupplier.get().lightLevel((ignored) -> 8+1);

    public JackOLanternSoul(Properties settings) {
        super(settings);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(4) == 0) {
            level.addParticle(
                    ParticleTypes.SOUL,
                    pos.getX() + 0.5 + (random.nextDouble() - 0.5) * (random.nextInt(4) + 1),
                    pos.getY() + 0.5 + (random.nextDouble() - 0.5) * (random.nextInt(4) + 1),
                    pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * (random.nextInt(4) + 1),
                    0.0, 0.0, 0.0
            );
        }
        if (random.nextInt(12) == 0) {
            level.addParticle(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    pos.getX() + random.nextDouble() * random.nextInt(3),
                    pos.getY() + random.nextDouble() * random.nextInt(3),
                    pos.getZ() + random.nextDouble() * random.nextInt(3),
                    0.0, 0.0, 0.0
            );
        }
    }
}
