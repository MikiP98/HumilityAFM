package io.github.mikip98.humilityafm.util;

import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SoundUtils {
    public static void playSoundAtBlockCenter(@Nullable Entity source, World world, BlockPos pos, SoundEvent sound) {
        playSoundAtBlockCenter(source, world, pos, sound, 0);
    }
    public static void playSoundAtBlockCenter(@Nullable Entity source, World world, BlockPos pos, SoundEvent sound, float volumeOffset) {
        playSoundAtBlockCenter(source, world, pos, sound, volumeOffset, 0);
    }
    public static void playSoundAtBlockCenter(@Nullable Entity source, World world, BlockPos pos, SoundEvent sound, float volumeOffset, float pitchOffset) {
        world.playSound(
                source, pos,
                sound, SoundCategory.BLOCKS,
                getVaryingFloat(volumeOffset), getVaryingFloat(pitchOffset)
        );
    }

    public static float getVaryingFloat(float offset) {
        return (float) (Math.random() * (0.2 + offset) + (0.9 + offset / 2) + offset);
    }
}
