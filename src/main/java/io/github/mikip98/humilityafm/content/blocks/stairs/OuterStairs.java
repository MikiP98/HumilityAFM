package io.github.mikip98.humilityafm.content.blocks.stairs;

#if MC_VERSION >= 12004
import com.mojang.serialization.MapCodec;
#endif
import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.templates.PlainHorizontalFacingBlock;
import io.github.mikip98.humilityafm.util.wrappers.BlockHalfWrapper;
import io.github.mikip98.humilityafm.util.wrappers.VoxelUtils;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;

import java.util.Map;

public class OuterStairs extends PlainHorizontalFacingBlock implements Waterloggable {
    protected static final VoxelShape voxelShapeBottomNorth;
    protected static final VoxelShape voxelShapeBottomSouth;
    protected static final VoxelShape voxelShapeBottomEast;
    protected static final VoxelShape voxelShapeBottomWest;

    protected static final VoxelShape voxelShapeTopNorth;
    protected static final VoxelShape voxelShapeTopSouth;
    protected static final VoxelShape voxelShapeTopEast;
    protected static final VoxelShape voxelShapeTopWest;

    protected static Map<Direction, VoxelShape> getVoxelShapeMapOfY(double y) {
        VoxelShape base = VoxelUtils.cuboid(
                0, 8 - y, 0,
                16, 16 - y, 16
        );
        return Map.of(
                Direction.NORTH, VoxelUtils.union(base, VoxelUtils.cuboid(8, y, 8, 16, y+8, 16)),  // original
                Direction.SOUTH, VoxelUtils.union(base, VoxelUtils.cuboid(0, y, 0, 8, y+8, 8)),  // reverse original
                Direction.EAST,  VoxelUtils.union(base, VoxelUtils.cuboid(0, y, 8, 8, y+8, 16)),  // swap x <-> z + reverse
                Direction.WEST,  VoxelUtils.union(base, VoxelUtils.cuboid(8, y, 0, 16, y+8, 8))   // swap x <-> z
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

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    #if MC_VERSION < 260000
    protected static final EnumProperty<BlockHalf> HALF = Properties.BLOCK_HALF;
    #else
    protected static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    #endif

    #if MC_VERSION >= 12004
    protected static final MapCodec<CabinetBlock> CODEC = createCodec(CabinetBlock::new);

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }
    #endif

    public OuterStairs(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(FACING, Direction.SOUTH)
                .with(WATERLOGGED, false)
                .with(Properties.BLOCK_HALF, BlockHalfWrapper.BOTTOM));
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

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : Fluids.EMPTY.getDefaultState();
    }

    @SuppressWarnings("deprecation")
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
