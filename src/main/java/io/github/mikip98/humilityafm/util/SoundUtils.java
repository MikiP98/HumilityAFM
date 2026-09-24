package io.github.mikip98.humilityafm.util;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SoundUtils {
    public static void playSoundAtBlockCenter(Level level, @Nullable Player player, BlockPos pos, SoundEvent sound) {
        playSoundAtBlockCenter(level, player, pos, sound, 1.0f, 1.0f);
    }
    public static void playSoundAtBlockCenter(
            Level level, @Nullable Player player, BlockPos pos, SoundEvent sound, float baseVolume, float basePitch
    ) {
        final float volume = calculateVolume(baseVolume, level.getRandom().nextFloat());
        final float pitch = calculatePitch(basePitch, level.getRandom().nextFloat());

        level.playSound(player, pos, sound, SoundSource.BLOCKS, volume, pitch);
    }

    public static void playSound(Level level, double x, double y, double z, SoundEvent sound) {
        playSound(level, null, x, y, z, sound, 1.0f, 1.0f);
    }
    public static void playSound(Level level, @Nullable Player player, double x, double y, double z, SoundEvent sound) {
        playSound(level, player, x, y, z, sound, 1.0f, 1.0f);
    }
    public static void playSound(
            Level level, @Nullable Player player, double x, double y, double z, SoundEvent sound, float baseVolume, float basePitch
    ) {
        final float volume = calculateVolume(baseVolume, level.getRandom().nextFloat());
        final float pitch = calculatePitch(basePitch, level.getRandom().nextFloat());

        level.playSound(player, x, y, z, sound, SoundSource.BLOCKS, volume, pitch);
    }

    private static float calculateVolume(float baseVolume, float randomValue) {
        // Linear scale for volume: [0.9, 1.05] of base volume.
        final float minVolume = 0.9f;
        return baseVolume * (minVolume + (randomValue * (1.05f - minVolume)));
    }

    private static float calculatePitch(float basePitch, float randomValue) {
        // Logarithmic scale for human pitch perception: [0.833, 1.2] multiplier.
        // Uses base 1.2 with an exponent range of [-1.0, 1.0]
        return basePitch * getLogarithmicMultiplier(randomValue, 1.2f);
    }

    /**
     * Generates a perfectly symmetrical randomized multiplier for human pitch perception.
     * @param randomValue A float between 0.0 and 1.0 (from world.random.nextFloat())
     * @param maxVariance The maximum multiplier (e.g. 1.2f for a 20% variance)
     */
    protected static float getLogarithmicMultiplier(float randomValue, float maxVariance) {
        return (float) Math.pow(maxVariance, (randomValue * 2.0f) - 1.0f);
    }
}
