package io.github.mikip98.humilityafm.util.wrappers;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelUtils {
    public static VoxelShape cuboid(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ) {
        #if MC_VERSION < 260000
        return Block.createCuboidShape(minX, minY, minZ, maxX, maxY, maxZ);
        #else
        return Block.box(minX, minY, minZ, maxX, maxY, maxZ);
        #endif
    }
    public static VoxelShape union(final VoxelShape first, final VoxelShape... others) {
        #if MC_VERSION < 260000
        return VoxelShapes.union(first, others);
        #else
        return Shapes.or(first, others);
        #endif
    }
}
