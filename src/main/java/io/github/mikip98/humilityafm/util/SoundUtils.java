package io.github.mikip98.humilityafm.util;

import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoundUtils {
    public static void playSoundAtBlockCenter(World world, BlockPos pos, SoundEvent sound, float volumeOffset) {
        world.playSoundAtBlockCenter(
                pos, sound, SoundCategory.BLOCKS,
                getVaryingFloat(volumeOffset), getVaryingFloat(0),
                true
        );
    }

    public static float getVaryingFloat(float offset) {
        return (float) (Math.random() * 0.2 + 0.9 + offset);
    }
}
