package io.github.mikip98.content.blocks;

import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class LEDBlock extends HorizontalFacingBlock implements Waterloggable {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public LEDBlock(AbstractBlock.Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(Properties.HORIZONTAL_FACING, Direction.SOUTH)
                .with(Properties.WATERLOGGED, false)
                .with(Properties.BLOCK_HALF, BlockHalf.BOTTOM));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
        builder.add(WATERLOGGED);
        builder.add(Properties.BLOCK_HALF);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER)
                .with(Properties.BLOCK_HALF, ctx.getHitPos().y - ctx.getBlockPos().getY() > 0.5 ? BlockHalf.TOP : BlockHalf.BOTTOM);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : Fluids.EMPTY.getDefaultState();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        if (state.get(Properties.BLOCK_HALF) == BlockHalf.TOP) {
            switch(dir) {
                case NORTH:
                    return VoxelShapes.cuboid(0f, 0.9375f, 0.9375f, 1f, 1f, 1f);
                case SOUTH:
                    return VoxelShapes.cuboid(0f, 0.9375f, 0f, 1f, 1f, 0.0625f);
                case EAST:
                    return VoxelShapes.cuboid(0f, 0.9375f, 0f, 0.0625f, 1f, 1f);
                case WEST:
                    return VoxelShapes.cuboid(0.9375f, 0.9375f, 0f, 1f, 1f, 1f);
                default:
                    return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.5f, 1.0f);
            }
        }
        switch(dir) {
            case NORTH:
                return VoxelShapes.cuboid(0f, 0f, 0.9375f, 1f, 0.0625f, 1f);
            case SOUTH:
                return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.0625f, 0.0625f);
            case EAST:
                return VoxelShapes.cuboid(0f, 0f, 0f, 0.0625f, 0.0625f, 1f);
            case WEST:
                return VoxelShapes.cuboid(0.9375f, 0f, 0f, 1f, 0.0625f, 1f);
            default:
                return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.5f, 1.0f);
        }
    }
}
