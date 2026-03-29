package io.github.mikip98.humilityafm.util.wrappers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class WorldWrapper {
    public static void setBlockState(
            #if MC_VERSION < 260000 World #else Level #endif world,
            BlockPos pos, BlockState state
    ) {
        #if MC_VERSION < 260000
        world.setBlockState(pos, state);
        #else
        world.setBlockAndUpdate(pos, state);
        #endif
    }

    @Deprecated
    public static void setBlockState(
            #if MC_VERSION < 260000 World #else Level #endif world,
            BlockPos pos, BlockState state, int notify
    ) {
        #if MC_VERSION < 260000
        world.setBlockState(pos, state);
        #else
        world.setBlock(pos, state, notify);
        #endif
    }
}
