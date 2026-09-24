package io.github.mikip98.humilityafm.content.blocks;

#if MC_VERSION >= 12003 && MC_VERSION < 260300 import com.mojang.serialization.MapCodec; #endif
#if POLYMER import eu.pb4.polymer.blocks.api.PolymerTexturedBlock; #endif
import io.github.mikip98.humilityafm.content.blockentities.LightStripBlockEntity;
#if POLYMER import io.github.mikip98.humilityafm.mod_support.polymer.PolymerModelCache; #endif
#if MC_VERSION >= 260000 import net.fabricmc.fabric.api.networking.v1.context.PacketContext; #endif
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
#if POLYMER import net.minecraft.server.level.ServerLevel; #endif
#if POLYMER && MC_VERSION >= 12104 import net.minecraft.util.RandomSource; #endif
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
#if POLYMER && MC_VERSION < 260000 import xyz.nucleoid.packettweaker.PacketContext; #endif

import java.util.Map;

public class LightStripBlock extends StairBlock implements EntityBlock #if POLYMER, PolymerTexturedBlock #endif {
    protected static final VoxelShape[] SHAPE_LOOKUP = new VoxelShape[40];

    protected static int getIndex(Half half, StairsShape shape, Direction dir) {
        return (half.ordinal() * 20) + (shape.ordinal() * 4) + (dir.ordinal() - 2);
    }

    protected static void inputShape(Half half, StairsShape shape, Direction dir, VoxelShape voxelShape) {
        SHAPE_LOOKUP[getIndex(half, shape, dir)] = voxelShape;
    }

    static {
        final Map<Direction, VoxelShape> bottomStraight = getStraightVoxelShape(0);
        final Map<Direction, VoxelShape> topStraight = getStraightVoxelShape(15);

        final Map<Direction, VoxelShape> bottomInnerLeft = getInnerLeftVoxelShape(bottomStraight);
        final Map<Direction, VoxelShape> topInnerLeft = getInnerLeftVoxelShape(topStraight);

        final Map<Direction, VoxelShape> bottomOuterLeft = getOuterLeftVoxelShape(0);
        final Map<Direction, VoxelShape> topOuterLeft = getOuterLeftVoxelShape(15);

        final Direction[] horizontalDirections = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

        for (Direction direction : horizontalDirections) {
            // STRAIGHT
            inputShape(Half.BOTTOM, StairsShape.STRAIGHT, direction, bottomStraight.get(direction));
            inputShape(Half.TOP,    StairsShape.STRAIGHT, direction, topStraight.get(direction));

            // INNER_LEFT
            inputShape(Half.BOTTOM, StairsShape.INNER_LEFT, direction, bottomInnerLeft.get(direction));
            inputShape(Half.TOP,    StairsShape.INNER_LEFT, direction, topInnerLeft.get(direction));

            // OUTER_LEFT
            inputShape(Half.BOTTOM, StairsShape.OUTER_LEFT, direction, bottomOuterLeft.get(direction));
            inputShape(Half.TOP,    StairsShape.OUTER_LEFT, direction, topOuterLeft.get(direction));

            final Direction rotatedDirection = switch (direction) {
                case NORTH -> Direction.EAST;
                case SOUTH -> Direction.WEST;
                case EAST  -> Direction.SOUTH;
                case WEST  -> Direction.NORTH;
                default -> throw new IllegalStateException();
            };

            // INNER_RIGHT
            inputShape(Half.BOTTOM, StairsShape.INNER_RIGHT, direction, bottomInnerLeft.get(rotatedDirection));
            inputShape(Half.TOP,    StairsShape.INNER_RIGHT, direction, topInnerLeft.get(rotatedDirection));

            // OUTER_RIGHT
            inputShape(Half.BOTTOM, StairsShape.OUTER_RIGHT, direction, bottomOuterLeft.get(rotatedDirection));
            inputShape(Half.TOP,    StairsShape.OUTER_RIGHT, direction, topOuterLeft.get(rotatedDirection));
        }
    }
    protected static Map<Direction, VoxelShape> getStraightVoxelShape(int y) {
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
    protected static Map<Direction, VoxelShape> getOuterLeftVoxelShape(int y) {
        return Map.of(
                Direction.NORTH, box(0, y, 0, 1, y + 1, 1),
                Direction.SOUTH, box(15, y, 15, 16, y + 1, 16),
                Direction.EAST,  box(15, y, 0, 16, y + 1, 1),
                Direction.WEST,  box(0, y, 15, 1, y + 1, 16)
        );
    }

    #if MC_VERSION >= 12003 && MC_VERSION < 260300
    protected static final MapCodec<LightStripBlock> CODEC = simpleCodec(LightStripBlock::new);

    @Override
    public @NotNull MapCodec<? extends LightStripBlock> codec() { return CODEC; }
    #endif


    public static final Properties defaultSettings = Properties.of().strength(0.5f).sound(SoundType.GLASS).lightLevel((state) -> 9);

    public LightStripBlock(Properties settings) {
        super(Blocks.GLASS.defaultBlockState(), settings);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_LOOKUP[getIndex(state.getValue(HALF), state.getValue(SHAPE), state.getValue(FACING))];
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LightStripBlockEntity(pos, state);
    }

    #if POLYMER
    #if MC_VERSION < 12006
    @Override
    public Block getPolymerBlock(BlockState state) {
        return getPolymerBlockState(state).getBlock();
    }
    #endif

    @Override
    public BlockState getPolymerBlockState(BlockState state #if MC_VERSION >= 12104, PacketContext context #endif) {
        return state.getValue(HALF) == Half.TOP ? PolymerModelCache.LIGHT_STRIP_TOP_DISGUISE : PolymerModelCache.LIGHT_STRIP_BOTTOM_DISGUISE;
    }

    @Override
    #if MC_VERSION < 12104
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        final BlockState newState = super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    #else
    protected @NotNull BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        final BlockState newState = super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
        #endif
        if (state != newState && level instanceof ServerLevel serverLevel) {
            final BlockEntity blockEntity = serverLevel.getBlockEntity(pos);
            if (blockEntity instanceof LightStripBlockEntity lightStripEntity) {
                lightStripEntity.updateVisualState(newState);
            }
        }
        return newState;
    }

    #if MC_VERSION < 12105
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            level.removeBlockEntity(pos);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
    #else
    // TODO: Make sure this is necessary
    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean isMoving) {
        level.removeBlockEntity(pos);
        super.affectNeighborsAfterRemoval(state, level, pos, isMoving);
    }
    #endif
    #endif
}
