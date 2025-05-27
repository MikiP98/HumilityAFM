package io.github.mikip98.humilityafm.content.blocks.candlestick;

import io.github.mikip98.humilityafm.content.ModProperties;
import io.github.mikip98.humilityafm.util.data_types.CandleColor;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
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
    public static final AbstractBlock.Settings defaultSettings = getDefaultSettings()
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
    public RustableCandlestick(Settings settings, BlockState rustPreviousLevel, BlockState rustNextLevel, boolean ignored) {
        super(settings, false);
        this.rustPreviousLevel = rustPreviousLevel;
        this.rustNextLevel = rustNextLevel;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextDouble() >= 0.96) this.rust(state, world, pos);
    }

    public void rust(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, getChanagedBlockState(rustNextLevel, state), Block.NOTIFY_ALL);
    }
    public void derust(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, getChanagedBlockState(rustPreviousLevel, state), Block.NOTIFY_ALL);
    }
    protected BlockState getChanagedBlockState(BlockState newBase, BlockState state) {
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

    protected void emmitWaxingParticles(BlockState state, World world, BlockPos pos, ParticleEffect particleType) {
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
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
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
                if (!player.isCreative() && !world.isClient) heldItem.damage(
                        1,
                        world.getRandom(),
                        (ServerPlayerEntity) player,
                        () -> player.sendEquipmentBreakStatus(LivingEntity.getSlotForHand(hand))
                );
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

        return super.onUse(state, world, pos, player, hit);
    }
}
