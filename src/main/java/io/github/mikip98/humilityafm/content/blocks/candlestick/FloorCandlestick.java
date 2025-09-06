package io.github.mikip98.humilityafm.content.blocks.candlestick;

import io.github.mikip98.humilityafm.content.properties.ModProperties;
import io.github.mikip98.humilityafm.content.blocks.candlestick.logic.SimpleCandlestickLogic;
import io.github.mikip98.humilityafm.content.properties.enums.CandleColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class FloorCandlestick extends Block implements SimpleCandlestickLogic, Waterloggable {
    protected static final VoxelShape voxelShape = Block.createCuboidShape(6, 0, 6, 10, 6, 10);
    protected static final VoxelShape voxelShapeCandle = Block.createCuboidShape(6, 0, 6, 10, 10, 10);

    protected static final EnumProperty<CandleColor> CANDLE_COLOR = ModProperties.CANDLE_COLOR;

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.WATERLOGGED);
        builder.add(CANDLE_COLOR);
        builder.add(Properties.LIT);
    }

    public FloorCandlestick() {
        this(Candlestick.defaultSettings);
    }
    public FloorCandlestick(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(Properties.WATERLOGGED, false)
                .with(CANDLE_COLOR, CandleColor.NONE)
                .with(Properties.LIT, false)
        );
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState()
                .with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).isIn(FluidTags.WATER));
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (onUseLogic(state, world, pos, player, hand, hit)) return ActionResult.SUCCESS;
        return super.onUse(state, world, pos, player, hand, hit);
    }

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

    @SuppressWarnings("deprecation")
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        onStateReplacedLogic(state, world, pos, newState);
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : Fluids.EMPTY.getDefaultState();
    }
}
