package io.github.mikip98.humilityafm.content.blocks.stairs;

import com.mojang.serialization.MapCodec;
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
    protected static final VoxelShape voxelShapeBottomNorth;
    protected static final VoxelShape voxelShapeBottomSouth;
    protected static final VoxelShape voxelShapeBottomEast;
    protected static final VoxelShape voxelShapeBottomWest;

    protected static final VoxelShape voxelShapeTopNorth;
    protected static final VoxelShape voxelShapeTopSouth;
    protected static final VoxelShape voxelShapeTopEast;
    protected static final VoxelShape voxelShapeTopWest;

    protected static Map<Direction, VoxelShape> getVoxelShapeMapOfY(double y) {
        VoxelShape base = VoxelShapes.cuboid(
                0f, 0.5f - y, 0f,
                1f, 1f - y, 1f
        );
        return Map.of(
                Direction.NORTH, VoxelShapes.union(base, VoxelShapes.cuboid(0.5f, y, 0.5f, 1.0f, y+0.5f, 1.0f)),  // original
                Direction.SOUTH, VoxelShapes.union(base, VoxelShapes.cuboid(0.0f, y, 0.0f, 0.5f, y+0.5f, 0.5f)),  // reverse original
                Direction.EAST,  VoxelShapes.union(base, VoxelShapes.cuboid(0.0f, y, 0.5f, 0.5f, y+0.5f, 1.0f)),  // swap x <-> z + reverse
                Direction.WEST,  VoxelShapes.union(base, VoxelShapes.cuboid(0.5f, y, 0.0f, 1.0f, y+0.5f, 0.5f))   // swap x <-> z
        );
    }
    static {
        final Map<Direction, VoxelShape> bottomVoxelShape = getVoxelShapeMapOfY(0.5f);
        final Map<Direction, VoxelShape> topVoxelShape = getVoxelShapeMapOfY(0f);

        voxelShapeBottomNorth = bottomVoxelShape.get(Direction.NORTH);
        voxelShapeBottomSouth = bottomVoxelShape.get(Direction.SOUTH);
        voxelShapeBottomEast = bottomVoxelShape.get(Direction.EAST);
        voxelShapeBottomWest = bottomVoxelShape.get(Direction.WEST);

        voxelShapeTopNorth = topVoxelShape.get(Direction.NORTH);
        voxelShapeTopSouth = topVoxelShape.get(Direction.SOUTH);
        voxelShapeTopEast = topVoxelShape.get(Direction.EAST);
        voxelShapeTopWest = topVoxelShape.get(Direction.WEST);
    }

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    protected static final MapCodec<OuterStairs> CODEC = createCodec(OuterStairs::new);

    public OuterStairs(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(FACING, Direction.SOUTH)
                .with(Properties.WATERLOGGED, false)
                .with(Properties.BLOCK_HALF, BlockHalf.BOTTOM));
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(WATERLOGGED);
        builder.add(Properties.BLOCK_HALF);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER)
                .with(Properties.BLOCK_HALF, ctx.getHitPos().y - ctx.getBlockPos().getY() > 0.5 ? BlockHalf.TOP : BlockHalf.BOTTOM);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : Fluids.EMPTY.getDefaultState();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        if (state.get(Properties.BLOCK_HALF) == BlockHalf.BOTTOM) {
            switch (dir) {
                case NORTH:
                    return voxelShapeBottomNorth;
                case SOUTH:
                    return voxelShapeBottomSouth;
                case EAST:
                    return voxelShapeBottomEast;
                case WEST:
                    return voxelShapeBottomWest;
            }
        } else {
            switch (dir) {
                case NORTH:
                    return voxelShapeTopNorth;
                case SOUTH:
                    return voxelShapeTopSouth;
                case EAST:
                    return voxelShapeTopEast;
                case WEST:
                    return voxelShapeTopWest;
            }
        }
        return VoxelShapes.fullCube();  // Fallback, should not happen
    }
}
