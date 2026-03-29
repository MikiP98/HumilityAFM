package io.github.mikip98.humilityafm.util.wrappers;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class BlockStateWrapper {
    public static <T extends Comparable<T>> T get(BlockState state, Property<T> property) {
        #if MC_VERSION < 260000
        return state.get(property);
        #else
        return state.getValue(property);
        #endif
    }

    public static <T extends Comparable<T>, V extends T> BlockState with(BlockState state, Property<T> property, V value) {
        #if MC_VERSION < 260000
        return state.with(property, value);
        #else
        return state.setValue(property, value);
        #endif
    }
}
