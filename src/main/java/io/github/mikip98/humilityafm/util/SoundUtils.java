package io.github.mikip98.humilityafm.util;

import net.minecraft.entity.Entity;
#if MC_VERSION < 12105
import net.minecraft.entity.player.PlayerEntity;
#endif
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SoundUtils {
    public static void playSoundAtBlockCenter(World world, @Nullable Entity source, BlockPos pos, SoundEvent sound) {
        playSoundAtBlockCenter(world, source, pos, sound, 1.0f, 1.0f);
    }
    public static void playSoundAtBlockCenter(World world, @Nullable Entity source, BlockPos pos, SoundEvent sound, float baseVolume, float basePitch) {
        final float volume = calculateVolume(baseVolume, world.random.nextFloat());
        final float pitch = calculatePitch(basePitch, world.random.nextFloat());

        #if MC_VERSION < 12105
        // TODO: Check this
        //  Older versions required a PlayerEntity specifically, not a generic Entity
        PlayerEntity player = source instanceof PlayerEntity ? (PlayerEntity) source : null;
        world.playSound(player, pos, sound, SoundCategory.BLOCKS, volume, pitch);
        #else
        world.playSound(source, pos, sound, SoundCategory.BLOCKS, volume, pitch);
        #endif
    }

    public static void playSound(World world, double x, double y, double z, SoundEvent sound) {
        playSound(world, null, x, y, z, sound, 1.0f, 1.0f);
    }
    public static void playSound(World world, @Nullable Entity source, double x, double y, double z, SoundEvent sound) {
        playSound(world, null, x, y, z, sound, 1.0f, 1.0f);
    }
    public static void playSound(World world, @Nullable Entity source, double x, double y, double z, SoundEvent sound, float baseVolume, float basePitch) {
        final float volume = calculateVolume(baseVolume, world.random.nextFloat());
        final float pitch = calculatePitch(basePitch, world.random.nextFloat());

        #if MC_VERSION < 12105
        // TODO: Check this
        //  Older versions required a PlayerEntity specifically, not a generic Entity
        PlayerEntity player = source instanceof PlayerEntity ? (PlayerEntity) source : null;
        world.playSound(player, x, y, z, sound, SoundCategory.BLOCKS, volume, pitch);
        #else
        world.playSound(source, x, y, z, sound, SoundCategory.BLOCKS, volume, pitch);
        #endif
    }

    private static float calculateVolume(float baseVolume, float randomValue) {
        // Linear scale for volume: [0.9, 1.0] of base volume.
        final float minVolume = 0.9f;
        return baseVolume * (minVolume + (randomValue * (1.0f - minVolume)));
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
