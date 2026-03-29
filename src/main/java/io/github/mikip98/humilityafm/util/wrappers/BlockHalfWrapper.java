package io.github.mikip98.humilityafm.util.wrappers;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;

public class BlockHalfWrapper {
    public static final #if MC_VERSION < 260000 BlockHalf #else Half #endif BOTTOM =
        #if MC_VERSION < 260000 BlockHalf.BOTTOM #else Half.BOTTOM #endif;

    public static final #if MC_VERSION < 260000 BlockHalf #else Half #endif TOP =
        #if MC_VERSION < 260000 BlockHalf.TOP #else Half.TOP #endif;
}
