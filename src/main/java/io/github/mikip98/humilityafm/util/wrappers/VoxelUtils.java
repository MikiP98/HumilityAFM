package io.github.mikip98.humilityafm.util.wrappers;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelUtils {
    @Deprecated
    public static VoxelShape cuboid(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ) {
        return Block.box(minX, minY, minZ, maxX, maxY, maxZ);
    }
    @Deprecated
    public static VoxelShape union(final VoxelShape first, final VoxelShape... others) {
        return Shapes.or(first, others);
    }
}
