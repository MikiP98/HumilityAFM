package io.github.mikip98.humilityafm.content.blocks.candlestick.logic;

import io.github.mikip98.humilityafm.content.ModProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoneycombItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public non-sealed interface RustableCandlestickLogic extends BaseCandlestickLogic {
    BlockState getRustPreviousLevel();
    BlockState getRustNextLevel();
    void setRustPreviousLevel(BlockState rustPreviousLevel);
    void setRustNextLevel(BlockState rustNextLevel);

    default boolean onUseLogic(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        ItemStack heldItemStack = player.getStackInHand(hand);
        Item heldItem = heldItemStack.getItem();
        if (tryToWax(state, world, pos, player, heldItemStack, heldItem)) return true;
        return tryToDeWaxOrDeRust(state, world, pos, player, hand, heldItemStack, heldItem);
    }

    default boolean tryToWax(BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack heldItemStack, Item heldItem) {
        if (heldItem instanceof HoneycombItem && !state.get(ModProperties.WAXED)) {
            world.setBlockState(pos, state.with(ModProperties.WAXED, true), Block.NOTIFY_ALL);
            if (!player.isCreative()) heldItemStack.decrement(1);
            emmitWaxOnParticles(state, world, pos);
            world.playSoundAtBlockCenter(pos, SoundEvents.ITEM_HONEYCOMB_WAX_ON, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
            return true;
        }
        return false;
    }
    default boolean tryToDeWaxOrDeRust(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack heldItemStack, Item heldItem) {
        if (heldItem instanceof AxeItem) {
            // De-wax
            if (state.get(ModProperties.WAXED)) {
                world.setBlockState(pos, state.with(ModProperties.WAXED, false), Block.NOTIFY_ALL);
                if (!player.isCreative()) heldItemStack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
                emmitWaxOffParticles(state, world, pos);
                world.playSoundAtBlockCenter(pos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
                return true;
            }
            // De-rust
            else if (getRustPreviousLevel() != null) {
                // ServerWorld check is required to be spam proof
                if (!world.isClient) derust(state, world, pos);
                return true;
            }
        }
        return false;
    }

    default BlockState getChanagedBlockState(BlockState newBase, BlockState state) {
        return newBase
                .with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING))
                .with(Properties.WATERLOGGED, state.get(Properties.WATERLOGGED))
                .with(ModProperties.CANDLE_COLOR, state.get(ModProperties.CANDLE_COLOR))
                .with(Properties.LIT, state.get(Properties.LIT))
                .with(ModProperties.WAXED, state.get(ModProperties.WAXED));
    }
    default void rust(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, getChanagedBlockState(getRustNextLevel(), state), Block.NOTIFY_ALL);
    }
    default void derust(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, getChanagedBlockState(getRustPreviousLevel(), state), Block.NOTIFY_ALL);
    }

    default void emmitWaxingParticles(BlockState state, World world, BlockPos pos, DefaultParticleType particleType) {
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

    default void emmitWaxOnParticles(BlockState state, World world, BlockPos pos) {
        emmitWaxingParticles(state, world, pos, ParticleTypes.WAX_ON);
    }
    default void emmitWaxOffParticles(BlockState state, World world, BlockPos pos) {
        emmitWaxingParticles(state, world, pos, ParticleTypes.WAX_OFF);
    }
}
