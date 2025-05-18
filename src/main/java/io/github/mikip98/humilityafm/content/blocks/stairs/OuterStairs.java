package io.github.mikip98.humilityafm.content.blocks.stairs;

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

import java.util.Map;

public class OuterStairs extends HorizontalFacingBlock implements Waterloggable {
    protected static final Map<Direction, VoxelShape> bottomVoxelShape = getVoxelShapeMapOfY(
            VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.5f, 1.0f),
            0.5f
    );
    protected static final Map<Direction, VoxelShape> topVoxelShape = getVoxelShapeMapOfY(
            VoxelShapes.cuboid(0f, 0.5f, 0f, 1f, 1f, 1.0f),
            0f
    );
    protected static Map<Direction, VoxelShape> getVoxelShapeMapOfY(VoxelShape base, double y) {
        return Map.of(
                Direction.NORTH, VoxelShapes.union(base, VoxelShapes.cuboid(0.5f, y, 0.5f, 1.0f, y+0.5f, 1.0f)),  // original
                Direction.SOUTH, VoxelShapes.union(base, VoxelShapes.cuboid(0.0f, y, 0.0f, 0.5f, y+0.5f, 0.5f)),  // reverse original
                Direction.EAST,  VoxelShapes.union(base, VoxelShapes.cuboid(0.0f, y, 0.5f, 0.5f, y+0.5f, 1.0f)),  // swap x <-> z + reverse
                Direction.WEST,  VoxelShapes.union(base, VoxelShapes.cuboid(0.5f, y, 0.0f, 1.0f, y+0.5f, 0.5f))   // swap x <-> z
        );
    }

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public OuterStairs(Settings settings) {
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

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : Fluids.EMPTY.getDefaultState();
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        return state.get(Properties.BLOCK_HALF) == BlockHalf.TOP
                ? topVoxelShape.get(dir)
                : bottomVoxelShape.get(dir);
    }
}
