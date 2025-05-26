package io.github.mikip98.humilityafm.content.blocks;

import io.github.mikip98.humilityafm.content.blockentities.LightStripBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.StairShape;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class LightStripBlock extends StairsBlock implements BlockEntityProvider {
    // Straight
    protected static final VoxelShape voxelShapeBottomStraightNorth;
    protected static final VoxelShape voxelShapeBottomStraightSouth;
    protected static final VoxelShape voxelShapeBottomStraightEast;
    protected static final VoxelShape voxelShapeBottomStraightWest;

    protected static final VoxelShape voxelShapeTopStraightNorth;
    protected static final VoxelShape voxelShapeTopStraightSouth;
    protected static final VoxelShape voxelShapeTopStraightEast;
    protected static final VoxelShape voxelShapeTopStraightWest;

    // Inner Left
    protected static final VoxelShape voxelShapeBottomInnerLeftNorth;
    protected static final VoxelShape voxelShapeBottomInnerLeftSouth;
    protected static final VoxelShape voxelShapeBottomInnerLeftEast;
    protected static final VoxelShape voxelShapeBottomInnerLeftWest;

    protected static final VoxelShape voxelShapeTopInnerLeftNorth;
    protected static final VoxelShape voxelShapeTopInnerLeftSouth;
    protected static final VoxelShape voxelShapeTopInnerLeftEast;
    protected static final VoxelShape voxelShapeTopInnerLeftWest;

    // Inner Right
    protected static final VoxelShape voxelShapeBottomInnerRightNorth;
    protected static final VoxelShape voxelShapeBottomInnerRightSouth;
    protected static final VoxelShape voxelShapeBottomInnerRightEast;
    protected static final VoxelShape voxelShapeBottomInnerRightWest;

    protected static final VoxelShape voxelShapeTopInnerRightNorth;
    protected static final VoxelShape voxelShapeTopInnerRightSouth;
    protected static final VoxelShape voxelShapeTopInnerRightEast;
    protected static final VoxelShape voxelShapeTopInnerRightWest;

    // Outer Left
    protected static final VoxelShape voxelShapeBottomOuterLeftNorth;
    protected static final VoxelShape voxelShapeBottomOuterLeftSouth;
    protected static final VoxelShape voxelShapeBottomOuterLeftEast;
    protected static final VoxelShape voxelShapeBottomOuterLeftWest;

    protected static final VoxelShape voxelShapeTopOuterLeftNorth;
    protected static final VoxelShape voxelShapeTopOuterLeftSouth;
    protected static final VoxelShape voxelShapeTopOuterLeftEast;
    protected static final VoxelShape voxelShapeTopOuterLeftWest;

    // Outer Right
    protected static final VoxelShape voxelShapeBottomOuterRightNorth;
    protected static final VoxelShape voxelShapeBottomOuterRightSouth;
    protected static final VoxelShape voxelShapeBottomOuterRightEast;
    protected static final VoxelShape voxelShapeBottomOuterRightWest;

    protected static final VoxelShape voxelShapeTopOuterRightNorth;
    protected static final VoxelShape voxelShapeTopOuterRightSouth;
    protected static final VoxelShape voxelShapeTopOuterRightEast;
    protected static final VoxelShape voxelShapeTopOuterRightWest;

    static {
        final Map<Direction, VoxelShape> voxelShapeBottomStraight = getStraightVoxelShape(0);
        voxelShapeBottomStraightNorth = voxelShapeBottomStraight.get(Direction.NORTH);
        voxelShapeBottomStraightSouth = voxelShapeBottomStraight.get(Direction.SOUTH);
        voxelShapeBottomStraightEast = voxelShapeBottomStraight.get(Direction.EAST);
        voxelShapeBottomStraightWest = voxelShapeBottomStraight.get(Direction.WEST);
        final Map<Direction, VoxelShape> topStraightVoxelShape = getStraightVoxelShape(0.9375d);
        voxelShapeTopStraightNorth = topStraightVoxelShape.get(Direction.NORTH);
        voxelShapeTopStraightSouth = topStraightVoxelShape.get(Direction.SOUTH);
        voxelShapeTopStraightEast = topStraightVoxelShape.get(Direction.EAST);
        voxelShapeTopStraightWest = topStraightVoxelShape.get(Direction.WEST);

        final Map<Direction, VoxelShape> bottomInnerLeftVoxelShape = getInnerLeftVoxelShape(voxelShapeBottomStraight);
        voxelShapeBottomInnerLeftNorth = bottomInnerLeftVoxelShape.get(Direction.NORTH);
        voxelShapeBottomInnerLeftSouth = bottomInnerLeftVoxelShape.get(Direction.SOUTH);
        voxelShapeBottomInnerLeftEast = bottomInnerLeftVoxelShape.get(Direction.EAST);
        voxelShapeBottomInnerLeftWest = bottomInnerLeftVoxelShape.get(Direction.WEST);
        final Map<Direction, VoxelShape> topInnerLeftVoxelShape = getInnerLeftVoxelShape(topStraightVoxelShape);
        voxelShapeTopInnerLeftNorth = topInnerLeftVoxelShape.get(Direction.NORTH);
        voxelShapeTopInnerLeftSouth = topInnerLeftVoxelShape.get(Direction.SOUTH);
        voxelShapeTopInnerLeftEast = topInnerLeftVoxelShape.get(Direction.EAST);
        voxelShapeTopInnerLeftWest = topInnerLeftVoxelShape.get(Direction.WEST);

        final Map<Direction, VoxelShape> bottomOuterLeftVoxelShape = getOuterLeftVoxelShape(0);
        voxelShapeBottomOuterLeftNorth = bottomOuterLeftVoxelShape.get(Direction.NORTH);
        voxelShapeBottomOuterLeftSouth = bottomOuterLeftVoxelShape.get(Direction.SOUTH);
        voxelShapeBottomOuterLeftEast = bottomOuterLeftVoxelShape.get(Direction.EAST);
        voxelShapeBottomOuterLeftWest = bottomOuterLeftVoxelShape.get(Direction.WEST);
        final Map<Direction, VoxelShape> topOuterLeftVoxelShape = getOuterLeftVoxelShape(0.9375d);
        voxelShapeTopOuterLeftNorth = topOuterLeftVoxelShape.get(Direction.NORTH);
        voxelShapeTopOuterLeftSouth = topOuterLeftVoxelShape.get(Direction.SOUTH);
        voxelShapeTopOuterLeftEast = topOuterLeftVoxelShape.get(Direction.EAST);
        voxelShapeTopOuterLeftWest = topOuterLeftVoxelShape.get(Direction.WEST);

        voxelShapeBottomInnerRightNorth = voxelShapeBottomInnerLeftEast;
        voxelShapeBottomInnerRightSouth = voxelShapeBottomInnerLeftWest;
        voxelShapeBottomInnerRightEast = voxelShapeBottomInnerLeftSouth;
        voxelShapeBottomInnerRightWest = voxelShapeBottomInnerLeftNorth;

        voxelShapeTopInnerRightNorth = voxelShapeTopInnerLeftEast;
        voxelShapeTopInnerRightSouth = voxelShapeTopInnerLeftWest;
        voxelShapeTopInnerRightEast = voxelShapeTopInnerLeftSouth;
        voxelShapeTopInnerRightWest = voxelShapeTopInnerLeftNorth;

        voxelShapeBottomOuterRightNorth = voxelShapeBottomOuterLeftEast;
        voxelShapeBottomOuterRightSouth = voxelShapeBottomOuterLeftWest;
        voxelShapeBottomOuterRightEast = voxelShapeBottomOuterLeftSouth;
        voxelShapeBottomOuterRightWest = voxelShapeBottomOuterLeftNorth;

        voxelShapeTopOuterRightNorth = voxelShapeTopOuterLeftEast;
        voxelShapeTopOuterRightSouth = voxelShapeTopOuterLeftWest;
        voxelShapeTopOuterRightEast = voxelShapeTopOuterLeftSouth;
        voxelShapeTopOuterRightWest = voxelShapeTopOuterLeftNorth;
    }
    protected static Map<Direction, VoxelShape> getStraightVoxelShape(double y) {
        return Map.of(
                Direction.NORTH, VoxelShapes.cuboid(0f, y, 0f, 1f, y + 0.0625d, 0.0625f),
                Direction.SOUTH, VoxelShapes.cuboid(0f, y, 0.9375f, 1f, y + 0.0625d, 1f),
                Direction.EAST,  VoxelShapes.cuboid(0.9375f, y, 0f, 1f, y + 0.0625d, 1f),
                Direction.WEST,  VoxelShapes.cuboid(0f, y, 0f, 0.0625f, y + 0.0625d, 1f)
        );
    }
    protected static Map<Direction, VoxelShape> getInnerLeftVoxelShape(Map<Direction, VoxelShape> base) {
        return Map.of(
                Direction.NORTH, VoxelShapes.union(base.get(Direction.NORTH), base.get(Direction.WEST)),
                Direction.SOUTH, VoxelShapes.union(base.get(Direction.SOUTH), base.get(Direction.EAST)),
                Direction.EAST,  VoxelShapes.union(base.get(Direction.EAST), base.get(Direction.NORTH)),
                Direction.WEST,  VoxelShapes.union(base.get(Direction.WEST), base.get(Direction.SOUTH))
        );
    }
    protected static Map<Direction, VoxelShape> getOuterLeftVoxelShape(double y) {
        return Map.of(
                Direction.NORTH, VoxelShapes.cuboid(0f, y, 0, 0.0625f, y + 0.0625d, 0.0625f),
                Direction.SOUTH, VoxelShapes.cuboid(0.9375f, y, 0.9375f, 1f, y + 0.0625d, 1f),
                Direction.EAST,  VoxelShapes.cuboid(0.9375f, y, 0f, 1f, y + 0.0625d, 0.0625f),
                Direction.WEST,  VoxelShapes.cuboid(0f, y, 0.9375f, 0.0625f, y + 0.0625d, 1f)
        );
    }


    public static final AbstractBlock.Settings defaultSettings = AbstractBlock.Settings.create().strength(0.5f).sounds(BlockSoundGroup.GLASS).luminance((ignored) -> 9);

    public LightStripBlock() {
        super(Blocks.GLASS.getDefaultState(), defaultSettings);
    }
    public LightStripBlock(Settings settings) {
        super(Blocks.GLASS.getDefaultState(), settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        BlockHalf half = state.get(Properties.BLOCK_HALF);
        StairShape shape = state.get(Properties.STAIR_SHAPE);

        if (half == BlockHalf.TOP) {
            return getVoxelShape(
                    dir, shape,
                    voxelShapeTopStraightNorth, voxelShapeTopStraightSouth, voxelShapeTopStraightEast, voxelShapeTopStraightWest,
                    voxelShapeTopInnerLeftNorth, voxelShapeTopInnerLeftSouth, voxelShapeTopInnerLeftEast, voxelShapeTopInnerLeftWest,
                    voxelShapeTopInnerRightNorth, voxelShapeTopInnerRightSouth, voxelShapeTopInnerRightEast, voxelShapeTopInnerRightWest,
                    voxelShapeTopOuterLeftNorth, voxelShapeTopOuterLeftSouth, voxelShapeTopOuterLeftEast, voxelShapeTopOuterLeftWest,
                    voxelShapeTopOuterRightNorth, voxelShapeTopOuterRightSouth, voxelShapeTopOuterRightEast, voxelShapeTopOuterRightWest
            );
        } else {
            return getVoxelShape(
                    dir, shape,
                    voxelShapeBottomStraightNorth, voxelShapeBottomStraightSouth, voxelShapeBottomStraightEast, voxelShapeBottomStraightWest,
                    voxelShapeBottomInnerLeftNorth, voxelShapeBottomInnerLeftSouth, voxelShapeBottomInnerLeftEast, voxelShapeBottomInnerLeftWest,
                    voxelShapeBottomInnerRightNorth, voxelShapeBottomInnerRightSouth, voxelShapeBottomInnerRightEast, voxelShapeBottomInnerRightWest,
                    voxelShapeBottomOuterLeftNorth, voxelShapeBottomOuterLeftSouth, voxelShapeBottomOuterLeftEast, voxelShapeBottomOuterLeftWest,
                    voxelShapeBottomOuterRightNorth, voxelShapeBottomOuterRightSouth, voxelShapeBottomOuterRightEast, voxelShapeBottomOuterRightWest
            );
        }
    }
    protected static VoxelShape getVoxelShape(
            Direction dir, StairShape shape,
            VoxelShape straightVoxelShapeNorth, VoxelShape straightVoxelShapeSouth, VoxelShape straightVoxelShapeEast, VoxelShape straightVoxelShapeWest,
            VoxelShape innerLeftVoxelShapeNorth, VoxelShape innerLeftVoxelShapeSouth, VoxelShape innerLeftVoxelShapeEast, VoxelShape innerLeftVoxelShapeWest,
            VoxelShape innerRightVoxelShapeNorth, VoxelShape innerRightVoxelShapeSouth, VoxelShape innerRightVoxelShapeEast, VoxelShape innerRightVoxelShapeWest,
            VoxelShape outerLeftVoxelShapeNorth, VoxelShape outerLeftVoxelShapeSouth, VoxelShape outerLeftVoxelShapeEast, VoxelShape outerLeftVoxelShapeWest,
            VoxelShape outerRightVoxelShapeNorth, VoxelShape outerRightVoxelShapeSouth, VoxelShape outerRightVoxelShapeEast, VoxelShape outerRightVoxelShapeWest
    ) {
        switch (shape) {
            case STRAIGHT:
                return getVoxelShape(dir, straightVoxelShapeNorth, straightVoxelShapeSouth, straightVoxelShapeEast, straightVoxelShapeWest);
            case INNER_LEFT:
                return getVoxelShape(dir, innerLeftVoxelShapeNorth, innerLeftVoxelShapeSouth, innerLeftVoxelShapeEast, innerLeftVoxelShapeWest);
            case INNER_RIGHT:
                return getVoxelShape(dir, innerRightVoxelShapeNorth, innerRightVoxelShapeSouth, innerRightVoxelShapeEast, innerRightVoxelShapeWest);
            case OUTER_LEFT:
                return getVoxelShape(dir, outerLeftVoxelShapeNorth, outerLeftVoxelShapeSouth, outerLeftVoxelShapeEast, outerLeftVoxelShapeWest);
            case OUTER_RIGHT:
                return getVoxelShape(dir, outerRightVoxelShapeNorth, outerRightVoxelShapeSouth, outerRightVoxelShapeEast, outerRightVoxelShapeWest);
        }
        return null;
    }
    protected static VoxelShape getVoxelShape(Direction dir, VoxelShape north, VoxelShape south, VoxelShape east, VoxelShape west) {
        switch (dir) {
            case NORTH:
                return north;
            case SOUTH:
                return south;
            case EAST:
                return east;
            case WEST:
                return west;
        }
        return null;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LightStripBlockEntity(pos, state);
    }
}
