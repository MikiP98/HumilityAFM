package io.github.mikip98.humilityafm.content.blocks;

import io.github.mikip98.humilityafm.content.blockentities.LightStripBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class LightStripBlock extends StairBlock implements EntityBlock {
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
                Direction.NORTH, box(0, y, 0, 16, y + 1, 1),
                Direction.SOUTH, box(0, y, 15, 16, y + 1, 16),
                Direction.EAST,  box(15, y, 0, 16, y + 1, 16),
                Direction.WEST,  box(0, y, 0, 1, y + 1, 16)
        );
    }
    protected static Map<Direction, VoxelShape> getInnerLeftVoxelShape(Map<Direction, VoxelShape> base) {
        return Map.of(
                Direction.NORTH, Shapes.or(base.get(Direction.NORTH), base.get(Direction.WEST)),
                Direction.SOUTH, Shapes.or(base.get(Direction.SOUTH), base.get(Direction.EAST)),
                Direction.EAST,  Shapes.or(base.get(Direction.EAST), base.get(Direction.NORTH)),
                Direction.WEST,  Shapes.or(base.get(Direction.WEST), base.get(Direction.SOUTH))
        );
    }
    protected static Map<Direction, VoxelShape> getOuterLeftVoxelShape(double y) {
        return Map.of(
                Direction.NORTH, box(0, y, 0, 1, y + 1, 1),
                Direction.SOUTH, box(15, y, 15, 16, y + 1, 16),
                Direction.EAST,  box(15, y, 0, 16, y + 1, 1),
                Direction.WEST,  box(0, y, 15, 1, y + 1, 16)
        );
    }


    public static final Properties defaultSettings = Properties.of().strength(0.5f).sound(SoundType.GLASS).lightLevel((state) -> 9);

    public LightStripBlock(Properties settings) {
        super(Blocks.GLASS.defaultBlockState(), settings);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        Half half = state.getValue(HALF);
        StairsShape shape = state.getValue(SHAPE);

        if (half == Half.TOP) {
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
    protected static @NotNull VoxelShape getVoxelShape(
            Direction dir, StairsShape shape,
            VoxelShape straightVoxelShapeNorth, VoxelShape straightVoxelShapeSouth, VoxelShape straightVoxelShapeEast, VoxelShape straightVoxelShapeWest,
            VoxelShape innerLeftVoxelShapeNorth, VoxelShape innerLeftVoxelShapeSouth, VoxelShape innerLeftVoxelShapeEast, VoxelShape innerLeftVoxelShapeWest,
            VoxelShape innerRightVoxelShapeNorth, VoxelShape innerRightVoxelShapeSouth, VoxelShape innerRightVoxelShapeEast, VoxelShape innerRightVoxelShapeWest,
            VoxelShape outerLeftVoxelShapeNorth, VoxelShape outerLeftVoxelShapeSouth, VoxelShape outerLeftVoxelShapeEast, VoxelShape outerLeftVoxelShapeWest,
            VoxelShape outerRightVoxelShapeNorth, VoxelShape outerRightVoxelShapeSouth, VoxelShape outerRightVoxelShapeEast, VoxelShape outerRightVoxelShapeWest
    ) {
        return switch (shape) {
            case STRAIGHT -> getVoxelShape(dir, straightVoxelShapeNorth, straightVoxelShapeSouth, straightVoxelShapeEast, straightVoxelShapeWest);
            case INNER_LEFT -> getVoxelShape(dir, innerLeftVoxelShapeNorth, innerLeftVoxelShapeSouth, innerLeftVoxelShapeEast, innerLeftVoxelShapeWest);
            case INNER_RIGHT -> getVoxelShape(dir, innerRightVoxelShapeNorth, innerRightVoxelShapeSouth, innerRightVoxelShapeEast, innerRightVoxelShapeWest);
            case OUTER_LEFT -> getVoxelShape(dir, outerLeftVoxelShapeNorth, outerLeftVoxelShapeSouth, outerLeftVoxelShapeEast, outerLeftVoxelShapeWest);
            case OUTER_RIGHT -> getVoxelShape(dir, outerRightVoxelShapeNorth, outerRightVoxelShapeSouth, outerRightVoxelShapeEast, outerRightVoxelShapeWest);
        };
    }
    protected static @NotNull VoxelShape getVoxelShape(Direction dir, VoxelShape north, VoxelShape south, VoxelShape east, VoxelShape west) {
        return switch (dir) {
            case NORTH -> north;
            case SOUTH -> south;
            case EAST -> east;
            case WEST -> west;
            default -> throw new IllegalStateException("It's not possible to get here...");
        };
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LightStripBlockEntity(pos, state);
    }
}
