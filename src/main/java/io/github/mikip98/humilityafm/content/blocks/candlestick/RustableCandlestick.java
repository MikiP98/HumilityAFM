package io.github.mikip98.humilityafm.content.blocks.candlestick;

import io.github.mikip98.humilityafm.content.ModProperties;
import io.github.mikip98.humilityafm.util.data_types.CandleColor;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class RustableCandlestick extends Candlestick {
    public static final FabricBlockSettings defaultSettings = FabricBlockSettings.copyOf(Candlestick.defaultSettings)
            .sounds(BlockSoundGroup.COPPER);

    protected static final EnumProperty<CandleColor> CANDLE_COLOR = ModProperties.CANDLE_COLOR;

    public BlockState rustPreviousLevel;
    public BlockState rustNextLevel;

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ModProperties.WAXED);
    }

    public RustableCandlestick() {
        this(defaultSettings, null, null);
    }
    public RustableCandlestick(Settings settings, BlockState rustPreviousLevel, BlockState rustNextLevel) {
        super(settings);
        this.rustPreviousLevel = rustPreviousLevel;
        this.rustNextLevel = rustNextLevel;
        setDefaultState(getDefaultState()
                .with(ModProperties.WAXED, false)
        );
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextDouble() >= 0.96) this.rust(state, world, pos);
    }

    public void rust(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, getChanageBlockState(rustNextLevel, state), Block.NOTIFY_ALL);
    }
    public void derust(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, getChanageBlockState(rustPreviousLevel, state), Block.NOTIFY_ALL);
    }
    protected BlockState getChanageBlockState(BlockState newBase, BlockState state) {
        return newBase
                .with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING))
                .with(Properties.WATERLOGGED, state.get(Properties.WATERLOGGED))
                .with(CANDLE_COLOR, state.get(CANDLE_COLOR))
                .with(Properties.LIT, state.get(Properties.LIT))
                .with(ModProperties.WAXED, state.get(ModProperties.WAXED));
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return !state.get(ModProperties.WAXED) && rustNextLevel != null;
    }

    protected void emmitWaxingParticles(BlockState state, World world, BlockPos pos, DefaultParticleType particleType) {
        Random random = world.random;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;

        // Center the particles on the candlestick
        switch (state.get(Properties.HORIZONTAL_FACING)) {
            case NORTH:
                z += 0.15;
                break;
            case SOUTH:
                z -= 0.15;
                break;
            case EAST:
                x -= 0.15;
                break;
            case WEST:
                x += 0.15;
                break;
        }

        for (int i = 0; i < 5; i++) {
            double randomX = x + ((random.nextDouble() - 0.5) * 2 / 3);
            double randomY = y + ((random.nextDouble() - 0.5) * 2 / 3);
            double randomZ = z + ((random.nextDouble() - 0.5) * 2 / 3);
            world.addParticle(particleType, randomX, randomY, randomZ, 0.0, 0.0, 0.0);
        }
    }

    protected void emmitWaxOnParticles(BlockState state, World world, BlockPos pos) {
        emmitWaxingParticles(state, world, pos, ParticleTypes.WAX_ON);
    }
    protected void emmitWaxOffParticles(BlockState state, World world, BlockPos pos) {
        emmitWaxingParticles(state, world, pos, ParticleTypes.WAX_OFF);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getStackInHand(hand);
        // Wax
        if (heldItem.getItem() instanceof HoneycombItem && !state.get(ModProperties.WAXED)) {
            world.setBlockState(pos, state.with(ModProperties.WAXED, true), Block.NOTIFY_ALL);
            if (!player.isCreative()) heldItem.decrement(1);
            emmitWaxOnParticles(state, world, pos);
            world.playSoundAtBlockCenter(pos, SoundEvents.ITEM_HONEYCOMB_WAX_ON, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
            return ActionResult.SUCCESS;
        }
        // De-wax & De-rust
        if (heldItem.getItem() instanceof AxeItem) {
            // De-wax
            if (state.get(ModProperties.WAXED)) {
                world.setBlockState(pos, state.with(ModProperties.WAXED, false), Block.NOTIFY_ALL);
                if (!player.isCreative()) heldItem.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
                emmitWaxOffParticles(state, world, pos);
                world.playSoundAtBlockCenter(pos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
                return ActionResult.SUCCESS;
            }
            // De-rust
            else if (rustPreviousLevel != null) {
                // ServerWorld check is required to be spam proof
                if (!world.isClient) derust(state, world, pos);
                return ActionResult.SUCCESS;
            }
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }
}
