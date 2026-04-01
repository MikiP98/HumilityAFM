package io.github.mikip98.humilityafm.content.blocks.candlestick;

#if MC_VERSION >= 12004
import com.mojang.serialization.MapCodec;
#endif
import io.github.mikip98.humilityafm.content.blocks.Waterloggable;
import io.github.mikip98.humilityafm.content.blocks.templates.PlainHorizontalFacingBlock;
import io.github.mikip98.humilityafm.content.blocks.candlestick.logic.SimpleCandlestickLogic;
import io.github.mikip98.humilityafm.content.properties.ModProperties;
import io.github.mikip98.humilityafm.content.properties.enums.CandleColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class Candlestick extends PlainHorizontalFacingBlock implements SimpleCandlestickLogic, Waterloggable {
    public static final Supplier<Properties> defaultSettingsSupplier = () -> Properties.of()
            .strength(0.5f)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .sound(SoundType.METAL)
            .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 4 : 0);

    public static final Properties defaultSettings = defaultSettingsSupplier.get();

    // The below commented-out shapes are a simplified variants of the voxel shapes
    // They look more vanilla-like, but are in my opinion more ugly
    // IDK what to use.
    // I'll leave both here for now, while using the more complex ones
//    protected static final VoxelShape voxelShapeEmptyNorth = Shapes.or(
//            box(5.5, 4, 8, 10.5, 7, 16),
//            box(5.5, 7, 8, 10.5, 9, 12)
//    );
//    protected static final VoxelShape voxelShapeEmptySouth = Shapes.or(
//            box(5.5, 4, 0, 10.5, 7, 8),
//            box(5.5, 7, 4, 10.5, 9, 8)
//    );
//    protected static final VoxelShape voxelShapeEmptyEast = Shapes.or(
//            box(0, 4, 5.5, 8, 7, 10.5),
//            box(4, 7, 5.5, 8, 9, 10.5)
//    );
//    protected static final VoxelShape voxelShapeEmptyWest = Shapes.or(
//            box(8, 4, 5.5, 16, 7, 10.5),
//            box(8, 7, 5.5, 12, 9, 10.5)
//    );
//
//    protected static final VoxelShape voxelShapeCandleNorth = Shapes.or(
//            voxelShapeEmptyNorth,
//            box(5, 9, 8, 11, 12, 16)
//    );
//    protected static final VoxelShape voxelShapeCandleSouth = Shapes.or(
//            voxelShapeEmptySouth,
//            box(5, 9, 0, 11, 12, 8)
//    );
//    protected static final VoxelShape voxelShapeCandleEast = Shapes.or(
//            voxelShapeEmptyEast,
//            box(0, 9, 5, 8, 12, 11)
//    );
//    protected static final VoxelShape voxelShapeCandleWest = Shapes.or(
//            voxelShapeEmptyWest,
//            box(8, 9, 5, 16, 12, 11)
//    );

    protected static final VoxelShape voxelShapeEmptyNorth = Shapes.or(
            box(7.5, 4, 10, 8.5, 5, 16),
            box(7.5, 5, 10, 8.5, 8, 11),
            box(6, 7.999, 8.5, 10, 8.001, 12.5),  // Dripper
            box(7, 8, 9.5, 9, 9, 11.5)  // Holder
    );
    protected static final VoxelShape voxelShapeEmptySouth = Shapes.or(
            box(7.5, 4, 0, 8.5, 5, 6),
            box(7.5, 5, 5, 8.5, 8, 6),
            box(6, 7.999, 3.5, 10, 8.001, 7.5),  // Dripper
            box(7, 8, 4.5, 9, 9, 6.5)  // Holder
    );
    protected static final VoxelShape voxelShapeEmptyEast = Shapes.or(
            box(0, 4, 7.5, 6, 5, 8.5),
            box(5, 5, 7.5, 6, 8, 8.5),
            box(3.5, 7.999, 6, 7.5, 8.001, 10),  // Dripper
            box(4.5, 8, 7, 6.5, 9, 9)  // Holder
    );
    protected static final VoxelShape voxelShapeEmptyWest = Shapes.or(
            box(10, 4, 7.5, 16, 5, 8.5),
            box(10, 5, 7.5, 11, 8, 8.5),
            box(8.5, 7.999, 6, 12.5, 8.001, 10),  // Dripper
            box(9.5, 8, 7, 11.5, 9, 9)  // Holder
    );

    protected static final VoxelShape voxelShapeCandleNorth = Shapes.or(
            voxelShapeEmptyNorth,
            box(7, 9.0001, 9.5, 9, 11, 11.5)
    );  // Empty + Candle
    protected static final VoxelShape voxelShapeCandleSouth = Shapes.or(
            voxelShapeEmptySouth,
            box(7, 9.0001, 4.5, 9, 11, 6.5)
    );  // Empty + Candle
    protected static final VoxelShape voxelShapeCandleEast = Shapes.or(
            voxelShapeEmptyEast,
            box(4.5, 9.0001, 7, 6.5, 11, 9)
    );  // Empty + Candle
    protected static final VoxelShape voxelShapeCandleWest = Shapes.or(
            voxelShapeEmptyWest,
            box(9.5, 9.0001, 7, 11.5, 11, 9)
    );  // Empty + Candle

    protected static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final EnumProperty<CandleColor> CANDLE_COLOR = ModProperties.CANDLE_COLOR;
    protected static final BooleanProperty LIT = BlockStateProperties.LIT;

    #if MC_VERSION >= 12004
    protected static final MapCodec<Candlestick> CODEC = simpleCodec(Candlestick::new);

    @Override
    protected @NotNull MapCodec<? extends Candlestick> codec() { return CODEC; }
    #endif

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
        builder.add(CANDLE_COLOR);
        builder.add(LIT);
    }

    public Candlestick(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState()
                .setValue(WATERLOGGED, false)
                .setValue(CANDLE_COLOR, CandleColor.NONE)
                .setValue(LIT, false));
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx)
                .setValue(BlockStateProperties.WATERLOGGED, isPlacedInWater(ctx));
    }

    @Override
    #if MC_VERSION < 12006
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (onUseLogic(state, world, pos, player, hand)) return InteractionResult.SUCCESS;
        return super.use(state, world, pos, player, hand, hit);
    }
    #else
    // TODO: When I drop 1.20.x support, I should split it into separate `useWithItem` and `useWithoutItem`
    public @NotNull InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (onUseLogic(state, world, pos, player)) return InteractionResult.SUCCESS;
        return super.useWithoutItem(state, world, pos, player, hit);
    }
    #endif

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // If the candle is lit, display flame and smoke particles + play sound
        if (state.getValue(LIT)) {
            // Unfortunately, the code below cannot be cached
            // as it would require making a block entity
            // and that would result in overall worse performance
            double candleWickX = pos.getX() + 0.5;
            final double candleWickY = pos.getY() + 0.78;
            double candleWickZ = pos.getZ() + 0.5;
            switch (state.getValue(FACING)) {
                case NORTH -> candleWickZ += 0.15;
                case SOUTH -> candleWickZ -= 0.15;
                case EAST -> candleWickX -= 0.15;
                case WEST -> candleWickX += 0.15;
            }

            performRandomDisplayTick(level, candleWickX, candleWickY, candleWickZ, random);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        if (state.getValue(ModProperties.CANDLE_COLOR) != CandleColor.NONE) {
            switch (dir) {
                case NORTH: return voxelShapeCandleNorth;
                case SOUTH: return voxelShapeCandleSouth;
                case EAST: return voxelShapeCandleEast;
                case WEST: return voxelShapeCandleWest;
            }
        } else {
            switch (dir) {
                case NORTH: return voxelShapeEmptyNorth;
                case SOUTH: return voxelShapeEmptySouth;
                case EAST: return voxelShapeEmptyEast;
                case WEST: return voxelShapeEmptyWest;
            }
        }
        throw new IllegalStateException("It's not possible to get here...");
    }

    @Override
    #if MC_VERSION < 12105
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        onStateReplacedLogic(state, level, pos, newState);
        super.onRemove(state, level, pos, newState, isMoving);
    }
    #else
    public void onRemove(BlockState state, Level level, BlockPos pos, boolean isMoving) {
        onStateReplacedLogic(state, world, pos);
        super.onStateReplaced(state, world, pos, moved);
    }
    #endif

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }
}
