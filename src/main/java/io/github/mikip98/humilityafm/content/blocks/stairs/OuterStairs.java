package io.github.mikip98.humilityafm.content.blocks.stairs;

import io.github.mikip98.humilityafm.content.blocks.Waterloggable;
import io.github.mikip98.humilityafm.content.blocks.templates.PlainHorizontalFacingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

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
        VoxelShape base = box(
                0, 8 - y, 0,
                16, 16 - y, 16
        );
        return Map.of(
                Direction.NORTH, Shapes.or(base, box(8, y, 8, 16, y+8, 16)),  // original
                Direction.SOUTH, Shapes.or(base, box(0, y, 0, 8, y+8, 8)),  // reverse original
                Direction.EAST,  Shapes.or(base, box(0, y, 8, 8, y+8, 16)),  // swap x <-> z + reverse
                Direction.WEST,  Shapes.or(base, box(8, y, 0, 16, y+8, 8))   // swap x <-> z
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

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final EnumProperty<Half> HALF = BlockStateProperties.HALF;

    #if MC_VERSION >= 12004
    protected static final MapCodec<CabinetBlock> CODEC = simpleCodec(CabinetBlock::new);

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }
    #endif

    public OuterStairs(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.SOUTH)
                .setValue(WATERLOGGED, false)
                .setValue(BlockStateProperties.HALF, Half.BOTTOM));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
        builder.add(HALF);
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        final Direction direction = ctx.getNearestLookingDirection();
        return super.getStateForPlacement(ctx)
                .setValue(WATERLOGGED, isPlacedInWater(ctx))
                .setValue(HALF,
                        direction != Direction.DOWN && (
                                direction == Direction.UP || !(ctx.getClickLocation().y - ctx.getClickedPos().getY() > 0.5)
                        ) ? Half.BOTTOM : Half.TOP
                );
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        final Direction dir = state.getValue(FACING);
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
