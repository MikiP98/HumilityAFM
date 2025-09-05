package io.github.mikip98.humilityafm.content.blocks.candlestick.logic;

import io.github.mikip98.humilityafm.content.properties.ModProperties;
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

    default boolean onUseRustableLogic(
            BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            double offsetX, double offsetY, double offsetZ
    ) {
        ItemStack heldItemStack = player.getStackInHand(hand);
        Item heldItem = heldItemStack.getItem();
        if (tryToWax(state, world, pos, player, heldItemStack, heldItem, offsetX, offsetY, offsetZ)) return true;
        return tryToDeWaxOrDeRust(state, world, pos, player, hand, heldItemStack, heldItem, offsetX, offsetY, offsetZ);
    }

    default boolean tryToWax(
            BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack heldItemStack, Item heldItem, double offsetX, double offsetY, double offsetZ
    ) {
        if (heldItem instanceof HoneycombItem && !state.get(ModProperties.WAXED)) {
            world.setBlockState(pos, state.with(ModProperties.WAXED, true), Block.NOTIFY_ALL);
            if (!player.isCreative()) heldItemStack.decrement(1);
            emmitWaxOnParticles(world, offsetX, offsetY, offsetZ);
            world.playSound(offsetX, offsetY, offsetZ, SoundEvents.ITEM_HONEYCOMB_WAX_ON, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
            return true;
        }
        return false;
    }
    default boolean tryToDeWaxOrDeRust(
            BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack heldItemStack, Item heldItem,
            double offsetX, double offsetY, double offsetZ
    ) {
        if (heldItem instanceof AxeItem) {
            // De-wax
            if (state.get(ModProperties.WAXED)) {
                world.setBlockState(pos, state.with(ModProperties.WAXED, false), Block.NOTIFY_ALL);
                if (!player.isCreative()) heldItemStack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
                emmitWaxOffParticles(world, offsetX, offsetY, offsetZ);
                world.playSound(offsetX, offsetY, offsetZ, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
                return true;
            }
            // De-rust
            else if (getRustPreviousLevel() != null) {
                if (!player.isCreative()) heldItemStack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
                // ServerWorld check is required to be spam proof
                if (!world.isClient) derust(state, world, pos);
                return true;
            }
        }
        return false;
    }

    BlockState getChangedBlockState(BlockState newBase, BlockState state);
    default BlockState getChangedBlockStateUniversal(BlockState newBase, BlockState state) {
        return newBase
                .with(Properties.WATERLOGGED, state.get(Properties.WATERLOGGED))
                .with(ModProperties.CANDLE_COLOR, state.get(ModProperties.CANDLE_COLOR))
                .with(Properties.LIT, state.get(Properties.LIT))
                .with(ModProperties.WAXED, state.get(ModProperties.WAXED));
    }
    default void rust(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, getChangedBlockState(getRustNextLevel(), state), Block.NOTIFY_ALL);
    }
    default void derust(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, getChangedBlockState(getRustPreviousLevel(), state), Block.NOTIFY_ALL);
    }

    default void emmitWaxingParticles(
            World world, DefaultParticleType particleType,
            double offsetX, double offsetY, double offsetZ
    ) {
        Random random = world.random;

        for (int i = 0; i < 5; i++) {
            double randomX = offsetX + ((random.nextDouble() - 0.5) * 2 / 3);
            double randomY = offsetY + ((random.nextDouble() - 0.5) * 2 / 3);
            double randomZ = offsetZ + ((random.nextDouble() - 0.5) * 2 / 3);
            world.addParticle(particleType, randomX, randomY, randomZ, 0.0, 0.0, 0.0);
        }
    }

    default void emmitWaxOnParticles(World world, double offsetX, double offsetY, double offsetZ) {
        emmitWaxingParticles(world, ParticleTypes.WAX_ON, offsetX, offsetY, offsetZ);
    }
    default void emmitWaxOffParticles(World world, double offsetX, double offsetY, double offsetZ) {
        emmitWaxingParticles(world, ParticleTypes.WAX_OFF, offsetX, offsetY, offsetZ);
    }
}
