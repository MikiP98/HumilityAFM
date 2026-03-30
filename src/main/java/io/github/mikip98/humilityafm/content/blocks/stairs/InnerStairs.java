package io.github.mikip98.humilityafm.content.blocks.stairs;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class InnerStairs extends OuterStairs {
    protected static final VoxelShape voxelShapeBottomNorth;
    protected static final VoxelShape voxelShapeBottomSouth;
    protected static final VoxelShape voxelShapeBottomEast;
    protected static final VoxelShape voxelShapeBottomWest;

    protected static final VoxelShape voxelShapeTopNorth;
    protected static final VoxelShape voxelShapeTopSouth;
    protected static final VoxelShape voxelShapeTopEast;
    protected static final VoxelShape voxelShapeTopWest;

    protected static Map<Direction, VoxelShape> getVoxelShapeMapOfY (double y) {
        VoxelShape base = box(
                0, 8 - y, 0,
                16, 16 - y, 16
        );
        return Map.of(
                Direction.NORTH, Shapes.or(
                        base,
                        box(8, y, 0, 16, y + 8, 16),
                        box(0, y, 8, 8, y + 8, 16)
                ),  // original
                Direction.SOUTH, Shapes.or(
                        base,
                        box(0, y, 0, 8, y + 8, 16),
                        box(8, y, 0, 16, y + 8, 8)
                ),  // reverse original
                Direction.EAST, Shapes.or(
                        base,
                        box(0, y, 8, 16, y + 8, 16),
                        box(0, y, 0, 8, y + 8, 8)
                ),  // swap x <-> z + reverse
                Direction.WEST, Shapes.or(
                        base,
                        box(0, y, 0, 16, y + 8, 8),
                        box(8, y, 8, 16, y + 8, 16)
                )   // swap x <-> z
        );
    }
    static {
        final Map<Direction, VoxelShape> bottomVoxelShape = getVoxelShapeMapOfY(8);
        final Map<Direction, VoxelShape> topVoxelShape = getVoxelShapeMapOfY(0);

        voxelShapeBottomNorth = bottomVoxelShape.get(Direction.NORTH);
        voxelShapeBottomSouth = bottomVoxelShape.get(Direction.SOUTH);
        voxelShapeBottomEast = bottomVoxelShape.get(Direction.EAST);
        voxelShapeBottomWest = bottomVoxelShape.get(Direction.WEST);

        voxelShapeTopNorth = topVoxelShape.get(Direction.NORTH);
        voxelShapeTopSouth = topVoxelShape.get(Direction.SOUTH);
        voxelShapeTopEast = topVoxelShape.get(Direction.EAST);
        voxelShapeTopWest = topVoxelShape.get(Direction.WEST);
    }

    public InnerStairs(Properties settings) {
        super(settings);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        if (state.getValue(HALF) == Half.BOTTOM) {
            switch (dir) {
                case NORTH: return voxelShapeBottomNorth;
                case SOUTH: return voxelShapeBottomSouth;
                case EAST: return voxelShapeBottomEast;
                case WEST: return voxelShapeBottomWest;
            }
        } else {
            switch (dir) {
                case NORTH: return voxelShapeTopNorth;
                case SOUTH: return voxelShapeTopSouth;
                case EAST: return voxelShapeTopEast;
                case WEST: return voxelShapeTopWest;
            }
        }
        throw new IllegalStateException("It's not possible to get here...");
    }
}
