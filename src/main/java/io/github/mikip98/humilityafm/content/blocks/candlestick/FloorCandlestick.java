package io.github.mikip98.humilityafm.content.blocks.candlestick;

import io.github.mikip98.humilityafm.content.blocks.Waterloggable;
import io.github.mikip98.humilityafm.content.properties.ModProperties;
import io.github.mikip98.humilityafm.content.blocks.candlestick.logic.SimpleCandlestickLogic;
import io.github.mikip98.humilityafm.content.properties.enums.CandleColor;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class FloorCandlestick extends Block implements SimpleCandlestickLogic, Waterloggable {
    protected static final VoxelShape voxelShape = Block.box(6, 0, 6, 10, 6, 10);
    protected static final VoxelShape voxelShapeCandle = Block.box(6, 0, 6, 10, 10, 10);

    protected static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final EnumProperty<CandleColor> CANDLE_COLOR = ModProperties.CANDLE_COLOR;
    protected static final BooleanProperty LIT = BlockStateProperties.LIT;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
        builder.add(CANDLE_COLOR);
        builder.add(LIT);
    }

    public FloorCandlestick(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState()
                .setValue(WATERLOGGED, false)
                .setValue(CANDLE_COLOR, CandleColor.NONE)
                .setValue(LIT, false));
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(WATERLOGGED, isPlacedInWater(ctx));
    }

    @Override
    #if MC_VERSION < 12006
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (onUseLogic(state, world, pos, player, hand)) return InteractionResult.SUCCESS;
        return super.use(state, world, pos, player, hand, hit);
    }
    #else
    public @NotNull InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (onUseLogic(state, world, pos, player)) return InteractionResult.SUCCESS;
        return super.useWithoutItem(state, world, pos, player, hit);
    }
    #endif

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // If the candle is lit, display flame and smoke particles + play sound
        if (state.getValue(LIT)) {
            final double candleWickX = pos.getX() + 0.5;
            final double candleWickY = pos.getY() + 0.667;
            final double candleWickZ = pos.getZ() + 0.5;
            performRandomDisplayTick(level, candleWickX, candleWickY, candleWickZ, random);
        }
        super.animateTick(state, level, pos, random);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (state.getValue(CANDLE_COLOR) != CandleColor.NONE) ? voxelShapeCandle : voxelShape;
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
        onStateReplacedLogic(state, level, pos);
        super.onRemove(state, level, pos, moved);
    }
    #endif

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }
}
