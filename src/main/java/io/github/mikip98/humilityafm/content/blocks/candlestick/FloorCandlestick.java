package io.github.mikip98.humilityafm.content.blocks.candlestick;

import io.github.mikip98.humilityafm.content.blocks.Waterloggable;
import io.github.mikip98.humilityafm.content.properties.ModProperties;
import io.github.mikip98.humilityafm.content.blocks.candlestick.logic.SimpleCandlestickLogic;
import io.github.mikip98.humilityafm.content.properties.enums.CandleColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class FloorCandlestick extends Block implements SimpleCandlestickLogic, Waterloggable {
    protected static final VoxelShape voxelShape = Block.box(6, 0, 6, 10, 6, 10);
    protected static final VoxelShape voxelShapeCandle = Block.box(6, 0, 6, 10, 10, 10);

    protected static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final EnumProperty<CandleColor> CANDLE_COLOR = ModProperties.CANDLE_COLOR;
    protected static final BooleanProperty LIT = BlockStateProperties.LIT;

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
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
        return super.onUse(state, world, pos, player, hand, hit);
    }
    #else
    public InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (onUseLogic(state, world, pos, player)) return ActionResultWrapper.SUCCESS;
        return super.onUse(state, world, pos, player, hit);
    }
    #endif

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        // If the candle is lit, display flame and smoke particles + play sound
        if (state.get(Properties.LIT)) {
            final double candleWickX = pos.getX() + 0.5;
            final double candleWickY = pos.getY() + 0.667;
            final double candleWickZ = pos.getZ() + 0.5;
            performRandomDisplayTick(world, candleWickX, candleWickY, candleWickZ, random);
        }
        super.randomDisplayTick(state, world, pos, random);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return (state.get(CANDLE_COLOR) != CandleColor.NONE) ? voxelShapeCandle : voxelShape;
    }

    @Override
    #if MC_VERSION < 12105
    @SuppressWarnings("deprecation")
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        onStateReplacedLogic(state, world, pos, newState);
        super.onStateReplaced(state, world, pos, newState, moved);
    }
    #else
    public void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
        onStateReplacedLogic(state, world, pos);
        super.onStateReplaced(state, world, pos, moved);
    }
    #endif

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : Fluids.EMPTY.getDefaultState();
    }
}
