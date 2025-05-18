package io.github.mikip98.humilityafm.content.blocks.stairs;

import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.Map;

public class InnerStairs extends OuterStairs {
    protected static final Map<Direction, VoxelShape> bottomVoxelShape = getVoxelShapeMapOfY(0.5f);
    protected static final Map<Direction, VoxelShape> topVoxelShape = getVoxelShapeMapOfY(0f);
    protected static Map<Direction, VoxelShape> getVoxelShapeMapOfY(double y) {
        return Map.of(
                Direction.NORTH, VoxelShapes.union(
                        VoxelShapes.cuboid(0.5f, y, 0f, 1f, y+0.5f, 1f),
                        VoxelShapes.cuboid(0f, y, 0.5f, 0.5f, y+0.5f, 1f)
                ),  // original
                Direction.SOUTH, VoxelShapes.union(
                        VoxelShapes.cuboid(0f, y, 0f, 0.5f, y+0.5f, 1f),
                        VoxelShapes.cuboid(0.5f, y, 0f, 1f, y+0.5f, 0.5f)
                ),  // reverse original
                Direction.EAST, VoxelShapes.union(
                        VoxelShapes.cuboid(0f, y, 0.5f, 1f, y+0.5f, 1f),
                        VoxelShapes.cuboid(0f, y, 0f, 0.5f, y+0.5f, 0.5f)
                ),  // swap x <-> z + reverse
                Direction.WEST,  VoxelShapes.union(
                        VoxelShapes.cuboid(0f, y, 0f, 1f, y+0.5f, 0.5f),
                        VoxelShapes.cuboid(0.5f, y, 0.5f, 1f, y+0.5f, 1f)
                )   // swap x <-> z
        );
    }

    public InnerStairs(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        return state.get(Properties.BLOCK_HALF) == BlockHalf.TOP
                ? topVoxelShape.get(dir)
                : bottomVoxelShape.get(dir);
    }
}
