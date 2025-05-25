package io.github.mikip98.humilityafm.content.blocks.candlestick;

import io.github.mikip98.humilityafm.content.ModProperties;
import io.github.mikip98.humilityafm.util.data_types.CandleColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class FloorRustableCandlestick extends RustableCandlestick {
    protected static final VoxelShape voxelShape = Block.createCuboidShape(6, 0, 6, 10, 6, 10);
    protected static final VoxelShape voxelShapeCandle = Block.createCuboidShape(6, 0, 6, 10, 10, 10);

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.WATERLOGGED);
        builder.add(ModProperties.CANDLE);
        builder.add(CANDLE_COLOR);
        builder.add(Properties.LIT);
        builder.add(ModProperties.WAXED);
    }


    public FloorRustableCandlestick() {
        this(defaultSettings, null, null);
    }

    public FloorRustableCandlestick(Settings settings, BlockState rustPreviousLevel, BlockState rustNextLevel) {
        super(settings, rustPreviousLevel, rustNextLevel, false);
        setDefaultState(getStateManager().getDefaultState()
                .with(Properties.WATERLOGGED, false)
                .with(ModProperties.CANDLE, false)
                .with(CANDLE_COLOR, CandleColor.PLAIN)
                .with(Properties.LIT, false)
                .with(ModProperties.WAXED, false)
        );
    }


    @Override
    protected BlockState getChanagedBlockState(BlockState newBase, BlockState state) {
        return newBase
                .with(Properties.WATERLOGGED, state.get(Properties.WATERLOGGED))
                .with(CANDLE_COLOR, state.get(CANDLE_COLOR))
                .with(Properties.LIT, state.get(Properties.LIT))
                .with(ModProperties.WAXED, state.get(ModProperties.WAXED));
    }


    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).isIn(FluidTags.WATER));
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        // Check if the block is lit (property LIT is true)
        if (state.get(Properties.LIT)) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.7;
            double z = pos.getZ() + 0.5;
            if (random.nextInt(1) == 0) {
                float velocityPlaneMultiplayer = 0.001953125f;
                double velocityY = random.nextDouble() * 0.001953125;
                double velocityX = (random.nextDouble() - 0.5) * velocityPlaneMultiplayer;
                double velocityZ = (random.nextDouble() - 0.5) * velocityPlaneMultiplayer;
                world.addParticle(ParticleTypes.SMALL_FLAME, x, y, z, velocityX, velocityY, velocityZ);

                if (random.nextInt(4) == 0) {
                    velocityPlaneMultiplayer = 0.00390625f;
                    velocityY = random.nextDouble() * 0.00390625;
                    velocityX = (random.nextDouble() - 0.5) * velocityPlaneMultiplayer;
                    velocityZ = (random.nextDouble() - 0.5) * velocityPlaneMultiplayer;
                    world.addParticle(ParticleTypes.SMOKE, x, y, z, velocityX, velocityY, velocityZ);
                }
            }
            if (random.nextInt(4) == 0) {
                world.playSound(x, y, z, SoundEvents.BLOCK_CANDLE_AMBIENT, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
            }
        }
        super.randomDisplayTick(state, world, pos, random, false);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        if (state.get(ModProperties.CANDLE)) {
            return voxelShapeCandle;
        }
        return voxelShape;
    }
}
