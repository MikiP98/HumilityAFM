package io.github.mikip98.humilityafm.content.blocks.light_strips;

import io.github.mikip98.humilityafm.content.blockentities.LightStripBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
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
    protected static final Map<Direction, VoxelShape> bottomStraightVoxelShape = getStraightVoxelShape(0);
    protected static final Map<Direction, VoxelShape> topStraightVoxelShape = getStraightVoxelShape(0.9375d);
    protected static Map<Direction, VoxelShape> getStraightVoxelShape(double y) {
        return Map.of(
                Direction.NORTH, VoxelShapes.cuboid(0f, y, 0f, 1f, y + 0.0625d, 0.0625f),
                Direction.SOUTH, VoxelShapes.cuboid(0f, y, 0.9375f, 1f, y + 0.0625d, 1f),
                Direction.EAST,  VoxelShapes.cuboid(0.9375f, y, 0f, 1f, y + 0.0625d, 1f),
                Direction.WEST,  VoxelShapes.cuboid(0f, y, 0f, 0.0625f, y + 0.0625d, 1f)
        );
    }

    protected static final Map<Direction, VoxelShape> bottomInnerLeftVoxelShape = getInnerLeftVoxelShape(bottomStraightVoxelShape);
    protected static final Map<Direction, VoxelShape> topInnerLeftVoxelShape = getInnerLeftVoxelShape(topStraightVoxelShape);
    protected static Map<Direction, VoxelShape> getInnerLeftVoxelShape(Map<Direction, VoxelShape> base) {
        return Map.of(
                Direction.NORTH, VoxelShapes.union(base.get(Direction.NORTH), base.get(Direction.WEST)),
                Direction.SOUTH, VoxelShapes.union(base.get(Direction.SOUTH), base.get(Direction.EAST)),
                Direction.EAST,  VoxelShapes.union(base.get(Direction.EAST), base.get(Direction.NORTH)),
                Direction.WEST,  VoxelShapes.union(base.get(Direction.WEST), base.get(Direction.SOUTH))
        );
    }

    protected static final Map<Direction, VoxelShape> bottomOuterLeftVoxelShape = getOuterLeftVoxelShape(0);
    protected static final Map<Direction, VoxelShape> topOuterLeftVoxelShape = getOuterLeftVoxelShape(0.9375d);
    protected static Map<Direction, VoxelShape> getOuterLeftVoxelShape(double y) {
        return Map.of(
                Direction.NORTH, VoxelShapes.cuboid(0f, y, 0, 0.0625f, y + 0.0625d, 0.0625f),
                Direction.SOUTH, VoxelShapes.cuboid(0.9375f, y, 0.9375f, 1f, y + 0.0625d, 1f),
                Direction.EAST,  VoxelShapes.cuboid(0.9375f, y, 0f, 1f, y + 0.0625d, 0.0625f),
                Direction.WEST,  VoxelShapes.cuboid(0f, y, 0.9375f, 0.0625f, y + 0.0625d, 1f)
        );
    }

    protected static final Map<Direction, VoxelShape> bottomInnerRightVoxelShape = Map.of(
            Direction.NORTH, bottomInnerLeftVoxelShape.get(Direction.EAST),
            Direction.SOUTH, bottomInnerLeftVoxelShape.get(Direction.WEST),
            Direction.EAST,  bottomInnerLeftVoxelShape.get(Direction.SOUTH),
            Direction.WEST,  bottomInnerLeftVoxelShape.get(Direction.NORTH)
    );
    protected static final Map<Direction, VoxelShape> topInnerRightVoxelShape = Map.of(
            Direction.NORTH, topInnerLeftVoxelShape.get(Direction.EAST),
            Direction.SOUTH, topInnerLeftVoxelShape.get(Direction.WEST),
            Direction.EAST,  topInnerLeftVoxelShape.get(Direction.SOUTH),
            Direction.WEST,  topInnerLeftVoxelShape.get(Direction.NORTH)
    );
    protected static final Map<Direction, VoxelShape> bottomOuterRightVoxelShape = Map.of(
            Direction.NORTH, bottomOuterLeftVoxelShape.get(Direction.EAST),
            Direction.SOUTH, bottomOuterLeftVoxelShape.get(Direction.WEST),
            Direction.EAST,  bottomOuterLeftVoxelShape.get(Direction.SOUTH),
            Direction.WEST,  bottomOuterLeftVoxelShape.get(Direction.NORTH)
    );
    protected static final Map<Direction, VoxelShape> topOuterRightVoxelShape = Map.of(
            Direction.NORTH, topOuterLeftVoxelShape.get(Direction.EAST),
            Direction.SOUTH, topOuterLeftVoxelShape.get(Direction.WEST),
            Direction.EAST,  topOuterLeftVoxelShape.get(Direction.SOUTH),
            Direction.WEST,  topOuterLeftVoxelShape.get(Direction.NORTH)
    );


    public static final FabricBlockSettings defaultSettings = FabricBlockSettings.create().strength(0.5f).sounds(BlockSoundGroup.GLASS).luminance(9);

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
            return getVoxelShape(dir, shape, topStraightVoxelShape, topInnerLeftVoxelShape, topInnerRightVoxelShape, topOuterLeftVoxelShape, topOuterRightVoxelShape);
        } else {
            return getVoxelShape(dir, shape, bottomStraightVoxelShape, bottomInnerLeftVoxelShape, bottomInnerRightVoxelShape, bottomOuterLeftVoxelShape, bottomOuterRightVoxelShape);
        }
    }
    protected VoxelShape getVoxelShape(Direction dir, StairShape shape, Map<Direction, VoxelShape> straightVoxelShape, Map<Direction, VoxelShape> innerLeftVoxelShape, Map<Direction, VoxelShape> innerRightVoxelShape, Map<Direction, VoxelShape> outerLeftVoxelShape, Map<Direction, VoxelShape> outerRightVoxelShape) {
        return switch (shape) {
            case STRAIGHT -> straightVoxelShape.get(dir);
            case INNER_LEFT -> innerLeftVoxelShape.get(dir);
            case INNER_RIGHT -> innerRightVoxelShape.get(dir);
            case OUTER_LEFT -> outerLeftVoxelShape.get(dir);
            case OUTER_RIGHT -> outerRightVoxelShape.get(dir);
        };
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LightStripBlockEntity(pos, state);
    }
}
