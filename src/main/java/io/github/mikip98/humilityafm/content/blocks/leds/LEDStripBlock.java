package io.github.mikip98.humilityafm.content.blocks.leds;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.StairShape;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class LEDStripBlock extends StairsBlock {
    public LEDStripBlock(BlockState baseBlockState, Settings settings) {
        super(baseBlockState, settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        VoxelShape shape;
        if (state.get(Properties.STAIR_SHAPE) == StairShape.OUTER_LEFT) {
            if (state.get(Properties.BLOCK_HALF) == BlockHalf.TOP) {
                switch (dir) {
                    case NORTH:
                        shape = VoxelShapes.cuboid(0f, 0.9375f, 0f, 0.0625f, 1f, 0.0625f);
                        break;
                    case SOUTH:
                        shape = VoxelShapes.cuboid(0.9375f, 0.9375f, 0.9375f, 1f, 1f, 1f); // Works
                        break;
                    case EAST:
                        shape = VoxelShapes.cuboid(0.9375f, 0.9375f, 0f, 1f, 1f, 0.0625f);
                        break;
                    case WEST:
                        shape = VoxelShapes.cuboid(0f, 0.9375f, 0.9375f, 0.0625f, 1f, 1f);
                        break;
                    default:
                        shape = VoxelShapes.cuboid(0f, 0.5f, 0f, 1f, 1f, 1.0f);
                }
            } else {
                switch (dir) {
                    case NORTH:
                        shape = VoxelShapes.cuboid(0f, 0f, 0f, 0.0625f, 0.0625f, 0.0625f);
                        break;
                    case SOUTH:
                        shape = VoxelShapes.cuboid(0.9375f, 0f, 0.9375f, 1f, 0.0625f, 1f); // Works
                        break;
                    case EAST:
                        shape = VoxelShapes.cuboid(0.9375f, 0f, 0f, 1f, 0.0625f, 0.0625f);
                        break;
                    case WEST:
                        shape = VoxelShapes.cuboid(0f, 0f, 0.9375f, 0.0625f, 0.0625f, 1f);
                        break;
                    default:
                        shape = VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.5f, 1.0f);
                }
            }
        } else if (state.get(Properties.STAIR_SHAPE) == StairShape.OUTER_RIGHT) {
            if (state.get(Properties.BLOCK_HALF) == BlockHalf.TOP) {
                switch (dir) {
                    case NORTH:
                        shape = VoxelShapes.cuboid(0.9375f, 0.9375f, 0f, 1f, 1f, 0.0625f);
                        break;
                    case SOUTH:
                        shape = VoxelShapes.cuboid(0f, 0.9375f, 0.9375f, 0.0625f, 1f, 1f);
                        break;
                    case EAST:
                        shape = VoxelShapes.cuboid(0.9375f, 0.9375f, 0.9375f, 1f, 1f, 1f);
                        break;
                    case WEST:
                        shape = VoxelShapes.cuboid(0f, 0.9375f, 0f, 0.0625f, 1f, 0.0625f);
                        break;
                    default:
                        shape = VoxelShapes.cuboid(0f, 0.5f, 0f, 1f, 1f, 1.0f);
                }
            } else {
                switch (dir) {
                    case NORTH:
                        shape = VoxelShapes.cuboid(0.9375f, 0f, 0f, 1f, 0.0625f, 0.0625f);
                        break;
                    case SOUTH:
                        shape = VoxelShapes.cuboid(0f, 0f, 0.9375f, 0.0625f, 0.0625f, 1f);
                        break;
                    case EAST:
                        shape = VoxelShapes.cuboid(0.9375f, 0f, 0.9375f, 1f, 0.0625f, 1f);
                        break;
                    case WEST:
                        shape = VoxelShapes.cuboid(0f, 0f, 0f, 0.0625f, 0.0625f, 0.0625f);
                        break;
                    default:
                        shape = VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.5f, 1.0f);
                }
            }
        } else {
            if (state.get(Properties.BLOCK_HALF) == BlockHalf.TOP) {
                switch (dir) {
                    case NORTH:
                        shape = VoxelShapes.cuboid(0f, 0.9375f, 0f, 1f, 1f, 0.0625f);
                        break;
                    case SOUTH:
                        shape = VoxelShapes.cuboid(0f, 0.9375f, 0.9375f, 1f, 1f, 1f);
                        break;
                    case EAST:
                        shape = VoxelShapes.cuboid(0.9375f, 0.9375f, 0f, 1f, 1f, 1f);
                        break;
                    case WEST:
                        shape = VoxelShapes.cuboid(0f, 0.9375f, 0f, 0.0625f, 1f, 1f);
                        break;
                    default:
                        shape = VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.5f, 1.0f);
                }
            } else {
                switch (dir) {
                    case NORTH:
                        shape = VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.0625f, 0.0625f);
                        break;
                    case SOUTH:
                        shape = VoxelShapes.cuboid(0f, 0f, 0.9375f, 1f, 0.0625f, 1f);
                        break;
                    case EAST:
                        shape = VoxelShapes.cuboid(0.9375f, 0f, 0f, 1f, 0.0625f, 1f);
                        break;
                    case WEST:
                        shape = VoxelShapes.cuboid(0f, 0f, 0f, 0.0625f, 0.0625f, 1f);
                        break;
                    default:
                        shape = VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.5f, 1.0f);
                }
            }
            if (state.get(Properties.STAIR_SHAPE) == StairShape.INNER_LEFT) {
                if (state.get(Properties.BLOCK_HALF) == BlockHalf.TOP) {
                    switch (dir) {
                        case NORTH:
                            shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0f, 0.9375f, 0.0625f, 0.0625f, 1f, 1f));
                            break;
                        case SOUTH:
                            shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.9375f, 0.9375f, 0f, 1f, 1f, 0.9375f));
                            break;
                        case EAST:
                            shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0f, 0.9375f, 0f, 0.9375f, 1f, 0.0625f));
                            break;
                        case WEST:
                            shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625f, 0.9375f, 0.9375f, 1f, 1f, 1f));
                            break;
                        default:
                            shape = VoxelShapes.cuboid(0f, 0.5f, 0f, 1f, 1f, 1.0f);
                    }
                } else {

                }
            } else if (state.get(Properties.STAIR_SHAPE) == StairShape.INNER_RIGHT) {
                if (state.get(Properties.BLOCK_HALF) == BlockHalf.TOP) {
                    switch (dir) {
                        case NORTH:
                            shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.9375f, 0.9375f, 0.0625f, 1f, 1f, 1f));
                            break;
                        case SOUTH:
                            shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0f, 0.9375f, 0f, 0.0625f, 1f, 0.9375f));
                            break;
                        case EAST:
                            shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0f, 0.9375f, 0.9375f, 0.9375f, 1f, 1f));
                            break;
                        case WEST:
                            shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625f, 0.9375f, 0f, 1f, 1f, 0.0625f));
                            break;
                        default:
                            shape = VoxelShapes.cuboid(0f, 0.5f, 0f, 1f, 1f, 1.0f);
                    }
                } else {
                    switch (dir) {
                        case NORTH:
                            shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.9375f, 0f, 0.0625f, 1f, 0.0625f, 1f));
                            break;
                        case SOUTH:
                            shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0f, 0f, 0f, 0.0625f, 0.0625f, 0.9375f));
                            break;
                        case EAST:
                            shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0f, 0f, 0.9375f, 0.9375f, 0.0625f, 1f));
                            break;
                        case WEST:
                            shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625f, 0f, 0f, 1f, 0.0625f, 0.0625f));
                            break;
                        default:
                            shape = VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.5f, 1.0f);
                    }
                }
            }
        }
        return shape;
    }
}
