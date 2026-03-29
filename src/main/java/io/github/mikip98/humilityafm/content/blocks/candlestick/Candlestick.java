package io.github.mikip98.humilityafm.content.blocks.candlestick;

#if MC_VERSION >= 12004
import com.mojang.serialization.MapCodec;
#endif
import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.templates.PlainHorizontalFacingBlock;
import io.github.mikip98.humilityafm.content.properties.ModProperties;
import io.github.mikip98.humilityafm.content.blocks.candlestick.logic.SimpleCandlestickLogic;
import io.github.mikip98.humilityafm.content.properties.enums.CandleColor;
import io.github.mikip98.humilityafm.util.wrappers.ActionResultWrapper;
import io.github.mikip98.humilityafm.util.wrappers.PropertiesWrapper;
import io.github.mikip98.humilityafm.util.wrappers.VoxelUtils;
#if MC_VERSION < 260000
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.registry.tag.FluidTags;
#if MC_VERSION >= 12105
import net.minecraft.server.world.ServerWorld;
#endif
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
#if MC_VERSION < 12006
import net.minecraft.util.Hand;
#endif
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
#else
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
#endif

import java.util.Map;
import java.util.function.Supplier;

public class Candlestick extends PlainHorizontalFacingBlock implements SimpleCandlestickLogic, Waterloggable {
    #if MC_VERSION < 12006
    public static final Supplier<Settings> defaultSettingsSupplier = () -> Settings.create()
    #else
    public static final Supplier<Properties> defaultSettingsSupplier = () -> Properties.of()
    #endif
            .strength(0.5f)
            #if MC_VERSION < 12006 .requiresTool() #else .requiresCorrectToolForDrops() #endif
            #if MC_VERSION < 12006 .nonOpaque() #else .noOcclusion() #endif
            #if MC_VERSION < 12006 .sounds(BlockSoundGroup.METAL) #else .sound(SoundType.METAL)
            #if MC_VERSION < 12006
            .luminance(state -> state.get(Properties.LIT) ? 4 : 0)
            #else
            .lightLevel(state -> state.getValue(PropertiesWrapper.LIT) ? 4 : 0)
            #endif;

    public static final #if MC_VERSION < 12006 Settings #else Properties #endif defaultSettings = defaultSettingsSupplier.get();

    // The below commented-out shapes are a simplified variants of the voxel shapes
    // They look more vanilla-like, but are in my opinion more ugly
    // IDK what to use.
    // I'll leave both here for now, while using the more complex ones
//    protected static final VoxelShape voxelShapeEmptyNorth = VoxelShapes.union(
//            VoxelUtils.cuboid(5.5, 4, 8, 10.5, 7, 16),
//            VoxelUtils.cuboid(5.5, 7, 8, 10.5, 9, 12)
//    );
//    protected static final VoxelShape voxelShapeEmptySouth = VoxelShapes.union(
//            VoxelUtils.cuboid(5.5, 4, 0, 10.5, 7, 8),
//            VoxelUtils.cuboid(5.5, 7, 4, 10.5, 9, 8)
//    );
//    protected static final VoxelShape voxelShapeEmptyEast = VoxelShapes.union(
//            VoxelUtils.cuboid(0, 4, 5.5, 8, 7, 10.5),
//            VoxelUtils.cuboid(4, 7, 5.5, 8, 9, 10.5)
//    );
//    protected static final VoxelShape voxelShapeEmptyWest = VoxelShapes.union(
//            VoxelUtils.cuboid(8, 4, 5.5, 16, 7, 10.5),
//            VoxelUtils.cuboid(8, 7, 5.5, 12, 9, 10.5)
//    );
//
//    protected static final VoxelShape voxelShapeCandleNorth = VoxelShapes.union(
//            voxelShapeEmptyNorth,
//            VoxelUtils.cuboid(5, 9, 8, 11, 12, 16)
//    );
//    protected static final VoxelShape voxelShapeCandleSouth = VoxelShapes.union(
//            voxelShapeEmptySouth,
//            VoxelUtils.cuboid(5, 9, 0, 11, 12, 8)
//    );
//    protected static final VoxelShape voxelShapeCandleEast = VoxelShapes.union(
//            voxelShapeEmptyEast,
//            VoxelUtils.cuboid(0, 9, 5, 8, 12, 11)
//    );
//    protected static final VoxelShape voxelShapeCandleWest = VoxelShapes.union(
//            voxelShapeEmptyWest,
//            VoxelUtils.cuboid(8, 9, 5, 16, 12, 11)
//    );

    protected static final VoxelShape voxelShapeEmptyNorth = VoxelUtils.union(
            VoxelUtils.cuboid(7.5, 4, 10, 8.5, 5, 16),
            VoxelUtils.cuboid(7.5, 5, 10, 8.5, 8, 11),
            VoxelUtils.cuboid(6, 7.999, 8.5, 10, 8.001, 12.5),  // Dripper
            VoxelUtils.cuboid(7, 8, 9.5, 9, 9, 11.5)  // Holder
    );
    protected static final VoxelShape voxelShapeEmptySouth = VoxelUtils.union(
            VoxelUtils.cuboid(7.5, 4, 0, 8.5, 5, 6),
            VoxelUtils.cuboid(7.5, 5, 5, 8.5, 8, 6),
            VoxelUtils.cuboid(6, 7.999, 3.5, 10, 8.001, 7.5),  // Dripper
            VoxelUtils.cuboid(7, 8, 4.5, 9, 9, 6.5)  // Holder
    );
    protected static final VoxelShape voxelShapeEmptyEast = VoxelUtils.union(
            VoxelUtils.cuboid(0, 4, 7.5, 6, 5, 8.5),
            VoxelUtils.cuboid(5, 5, 7.5, 6, 8, 8.5),
            VoxelUtils.cuboid(3.5, 7.999, 6, 7.5, 8.001, 10),  // Dripper
            VoxelUtils.cuboid(4.5, 8, 7, 6.5, 9, 9)  // Holder
    );
    protected static final VoxelShape voxelShapeEmptyWest = VoxelUtils.union(
            VoxelUtils.cuboid(10, 4, 7.5, 16, 5, 8.5),
            VoxelUtils.cuboid(10, 5, 7.5, 11, 8, 8.5),
            VoxelUtils.cuboid(8.5, 7.999, 6, 12.5, 8.001, 10),  // Dripper
            VoxelUtils.cuboid(9.5, 8, 7, 11.5, 9, 9)  // Holder
    );

    protected static final VoxelShape voxelShapeCandleNorth = VoxelUtils.union(
            voxelShapeEmptyNorth,
            VoxelUtils.cuboid(7, 9.0001, 9.5, 9, 11, 11.5)
    );  // Empty + Candle
    protected static final VoxelShape voxelShapeCandleSouth = VoxelUtils.union(
            voxelShapeEmptySouth,
            VoxelUtils.cuboid(7, 9.0001, 4.5, 9, 11, 6.5)
    );  // Empty + Candle
    protected static final VoxelShape voxelShapeCandleEast = VoxelUtils.union(
            voxelShapeEmptyEast,
            VoxelUtils.cuboid(4.5, 9.0001, 7, 6.5, 11, 9)
    );  // Empty + Candle
    protected static final VoxelShape voxelShapeCandleWest = VoxelUtils.union(
            voxelShapeEmptyWest,
            VoxelUtils.cuboid(9.5, 9.0001, 7, 11.5, 11, 9)
    );  // Empty + Candle

    protected static final BooleanProperty WATERLOGGED = PropertiesWrapper.WATERLOGGED;
    protected static final EnumProperty<CandleColor> CANDLE_COLOR = ModProperties.CANDLE_COLOR;
    protected static final BooleanProperty LIT = PropertiesWrapper.LIT;

    #if MC_VERSION >= 12004
    protected static final MapCodec<Candlestick> CODEC = createCodec(Candlestick::new);

    @Override
    protected @NonNull MapCodec<? extends Candlestick> #if MC_VERSION < 260000 getCodec() #else codec() #endif { return CODEC; }
    #endif

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(WATERLOGGED);
        builder.add(CANDLE_COLOR);
        builder.add(LIT);
    }

    public Candlestick(#if MC_VERSION < 260000 Settings #else Properties #endif settings) {
        super(settings);
        updateDefaultState(Map.of(WATERLOGGED, false, CANDLE_COLOR, CandleColor.NONE, LIT, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).isIn(FluidTags.WATER));
    }

    #if MC_VERSION < 12006
    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (onUseLogic(state, world, pos, player, hand)) return ActionResult.SUCCESS;
        return super.onUse(state, world, pos, player, hand, hit);
    }
    #else
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (onUseLogic(state, world, pos, player)) return ActionResultWrapper.SUCCESS;
        return super.onUse(state, world, pos, player, hit);
    }
    #endif

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

    @SuppressWarnings("deprecation")
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
