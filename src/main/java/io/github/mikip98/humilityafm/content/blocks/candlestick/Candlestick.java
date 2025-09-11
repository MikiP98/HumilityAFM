package io.github.mikip98.humilityafm.content.blocks.candlestick;

import com.mojang.serialization.MapCodec;
import io.github.mikip98.humilityafm.content.properties.ModProperties;
import io.github.mikip98.humilityafm.content.blocks.candlestick.logic.SimpleCandlestickLogic;
import io.github.mikip98.humilityafm.content.properties.enums.CandleColor;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class Candlestick extends HorizontalFacingBlock implements SimpleCandlestickLogic, Waterloggable {
    public static final Settings defaultSettings = getDefaultSettings();
    public static Settings getDefaultSettings() {
        return Settings.create()
                .strength(0.5f)
                .requiresTool()
                .nonOpaque()
                .sounds(BlockSoundGroup.METAL)
                .luminance(state -> state.get(Properties.LIT) ? 4 : 0);
    }

    // The below commented-out shapes are a simplified variants of the voxel shapes
    // They look more vanilla-like, but are in my opinion more ugly
    // IDK what to use.
    // I'll leave both here for now, while using the more complex ones
//    protected static final VoxelShape voxelShapeEmptyNorth = VoxelShapes.union(
//            Block.createCuboidShape(5.5, 4, 8, 10.5, 7, 16),
//            Block.createCuboidShape(5.5, 7, 8, 10.5, 9, 12)
//    );
//    protected static final VoxelShape voxelShapeEmptySouth = VoxelShapes.union(
//            Block.createCuboidShape(5.5, 4, 0, 10.5, 7, 8),
//            Block.createCuboidShape(5.5, 7, 4, 10.5, 9, 8)
//    );
//    protected static final VoxelShape voxelShapeEmptyEast = VoxelShapes.union(
//            Block.createCuboidShape(0, 4, 5.5, 8, 7, 10.5),
//            Block.createCuboidShape(4, 7, 5.5, 8, 9, 10.5)
//    );
//    protected static final VoxelShape voxelShapeEmptyWest = VoxelShapes.union(
//            Block.createCuboidShape(8, 4, 5.5, 16, 7, 10.5),
//            Block.createCuboidShape(8, 7, 5.5, 12, 9, 10.5)
//    );
//
//    protected static final VoxelShape voxelShapeCandleNorth = VoxelShapes.union(
//            voxelShapeEmptyNorth,
//            Block.createCuboidShape(5, 9, 8, 11, 12, 16)
//    );
//    protected static final VoxelShape voxelShapeCandleSouth = VoxelShapes.union(
//            voxelShapeEmptySouth,
//            Block.createCuboidShape(5, 9, 0, 11, 12, 8)
//    );
//    protected static final VoxelShape voxelShapeCandleEast = VoxelShapes.union(
//            voxelShapeEmptyEast,
//            Block.createCuboidShape(0, 9, 5, 8, 12, 11)
//    );
//    protected static final VoxelShape voxelShapeCandleWest = VoxelShapes.union(
//            voxelShapeEmptyWest,
//            Block.createCuboidShape(8, 9, 5, 16, 12, 11)
//    );

    protected static final VoxelShape voxelShapeEmptyNorth = VoxelShapes.union(
            Block.createCuboidShape(7.5, 4, 10, 8.5, 5, 16),
            Block.createCuboidShape(7.5, 5, 10, 8.5, 8, 11),
            Block.createCuboidShape(6, 7.999, 8.5, 10, 8.001, 12.5),  // Dripper
            Block.createCuboidShape(7, 8, 9.5, 9, 9, 11.5)  // Holder
    );
    protected static final VoxelShape voxelShapeEmptySouth = VoxelShapes.union(
            Block.createCuboidShape(7.5, 4, 0, 8.5, 5, 6),
            Block.createCuboidShape(7.5, 5, 5, 8.5, 8, 6),
            Block.createCuboidShape(6, 7.999, 3.5, 10, 8.001, 7.5),  // Dripper
            Block.createCuboidShape(7, 8, 4.5, 9, 9, 6.5)  // Holder
    );
    protected static final VoxelShape voxelShapeEmptyEast = VoxelShapes.union(
            Block.createCuboidShape(0, 4, 7.5, 6, 5, 8.5),
            Block.createCuboidShape(5, 5, 7.5, 6, 8, 8.5),
            Block.createCuboidShape(3.5, 7.999, 6, 7.5, 8.001, 10),  // Dripper
            Block.createCuboidShape(4.5, 8, 7, 6.5, 9, 9)  // Holder
    );
    protected static final VoxelShape voxelShapeEmptyWest = VoxelShapes.union(
            Block.createCuboidShape(10, 4, 7.5, 16, 5, 8.5),
            Block.createCuboidShape(10, 5, 7.5, 11, 8, 8.5),
            Block.createCuboidShape(8.5, 7.999, 6, 12.5, 8.001, 10),  // Dripper
            Block.createCuboidShape(9.5, 8, 7, 11.5, 9, 9)  // Holder
    );

    protected static final VoxelShape voxelShapeCandleNorth = VoxelShapes.union(
            voxelShapeEmptyNorth,
            Block.createCuboidShape(7, 9.0001, 9.5, 9, 11, 11.5)
    );  // Empty + Candle
    protected static final VoxelShape voxelShapeCandleSouth = VoxelShapes.union(
            voxelShapeEmptySouth,
            Block.createCuboidShape(7, 9.0001, 4.5, 9, 11, 6.5)
    );  // Empty + Candle
    protected static final VoxelShape voxelShapeCandleEast = VoxelShapes.union(
            voxelShapeEmptyEast,
            Block.createCuboidShape(4.5, 9.0001, 7, 6.5, 11, 9)
    );  // Empty + Candle
    protected static final VoxelShape voxelShapeCandleWest = VoxelShapes.union(
            voxelShapeEmptyWest,
            Block.createCuboidShape(9.5, 9.0001, 7, 11.5, 11, 9)
    );  // Empty + Candle

    protected static final EnumProperty<CandleColor> CANDLE_COLOR = ModProperties.CANDLE_COLOR;

    protected static final MapCodec<Candlestick> CODEC = createCodec(Candlestick::new);

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(Properties.WATERLOGGED);
        builder.add(CANDLE_COLOR);
        builder.add(Properties.LIT);
    }

    public Candlestick(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(FACING, Direction.SOUTH)
                .with(Properties.WATERLOGGED, false)
                .with(CANDLE_COLOR, CandleColor.NONE)
                .with(Properties.LIT, false)
        );
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).isIn(FluidTags.WATER));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (onUseLogic(state, world, pos, player)) return ActionResult.SUCCESS;
        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        // If the candle is lit, display flame and smoke particles + play sound
        if (state.get(Properties.LIT)) {
            // Unfortunately, the code below cannot be cached
            // as it would require making a block entity
            // and that would result in overall worse performance
            double candleWickX = pos.getX() + 0.5;
            final double candleWickY = pos.getY() + 0.78;
            double candleWickZ = pos.getZ() + 0.5;
            switch (state.get(FACING)) {
                case NORTH -> candleWickZ += 0.15;
                case SOUTH -> candleWickZ -= 0.15;
                case EAST -> candleWickX -= 0.15;
                case WEST -> candleWickX += 0.15;
            }

            performRandomDisplayTick(world, candleWickX, candleWickY, candleWickZ, random);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(Properties.HORIZONTAL_FACING);
        if (state.get(ModProperties.CANDLE_COLOR) != CandleColor.NONE) {
            switch (dir) {
                case NORTH: { return voxelShapeCandleNorth; }
                case SOUTH: { return voxelShapeCandleSouth; }
                case EAST: { return voxelShapeCandleEast; }
                case WEST: { return voxelShapeCandleWest; }
            }
        } else {
            switch (dir) {
                case NORTH: { return voxelShapeEmptyNorth; }
                case SOUTH: { return voxelShapeEmptySouth; }
                case EAST: { return voxelShapeEmptyEast; }
                case WEST: { return voxelShapeEmptyWest; }
            }
        }
        return null;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        onStateReplacedLogic(state, world, pos, newState);
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : Fluids.EMPTY.getDefaultState();
    }
}
