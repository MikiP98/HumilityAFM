package io.github.mikip98.humilityafm.content.blocks.candlestick.logic;

import io.github.mikip98.humilityafm.content.properties.ModProperties;
import io.github.mikip98.humilityafm.content.properties.enums.CandleColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public sealed interface BaseCandlestickLogic permits SimpleCandlestickLogic, RustableCandlestickLogic {
    default boolean tryToInsertCandle(BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack heldItemStack, Item heldItem) {
        if (heldItem instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof CandleBlock) {
                // Check if the candlestick is already holding a candle
                if (state.get(ModProperties.CANDLE_COLOR) != CandleColor.NONE) {
                    // If the already inserted candle is the same as the one being inserted, do nothing
                    if (heldItem == state.get(ModProperties.CANDLE_COLOR).asCandle()) return false;
                    // if it's a different candle, drop the already held candle
                    player.getInventory().offerOrDrop(new ItemStack(state.get(ModProperties.CANDLE_COLOR).asCandle()));
                }
                world.setBlockState(pos, state.with(ModProperties.CANDLE_COLOR, CandleColor.getColor(heldItem)), Block.NOTIFY_ALL);
                if (!player.isCreative()) heldItemStack.decrement(1);
                world.playSoundAtBlockCenter(pos, SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 0.9f, 1.1f, true);
                return true;
            }
        }
        return false;
    }
    default boolean tryToExtinguishOrRemove(BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack heldItemStack) {
        if (heldItemStack.isEmpty() && player.isSneaking()) {
            // Extinguish the candle
            if (state.get(Properties.LIT)) {
                world.setBlockState(pos, state.with(Properties.LIT, false), Block.NOTIFY_ALL);
                world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_CANDLE_EXTINGUISH, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
                return true;
            }
            // Remove the candle
            else if (state.get(ModProperties.CANDLE_COLOR) != CandleColor.NONE) {
                player.getInventory().offerOrDrop(new ItemStack(state.get(ModProperties.CANDLE_COLOR).asCandle()));
                world.setBlockState(pos, state.with(ModProperties.CANDLE_COLOR, CandleColor.NONE), Block.NOTIFY_ALL);
                world.playSoundAtBlockCenter(pos, SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 0.9f, 0.9f, true);
                return true;
            }
        }
        return false;
    }
    default boolean tryToLightTheCandle(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack heldItemStack, Item heldItem) {
        if (
                heldItem instanceof FlintAndSteelItem
                        && state.get(ModProperties.CANDLE_COLOR) != CandleColor.NONE
                        && !state.get(Properties.LIT)
                        && !state.get(Properties.WATERLOGGED)
        ) {
            world.setBlockState(pos, state.with(Properties.LIT, true), Block.NOTIFY_ALL);
            damageItem(heldItemStack, player, world, hand);
            world.playSoundAtBlockCenter(pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
            return true;
        }
        return false;
    }

    default void performRandomDisplayTick(World world, double candleWickX, double candleWickY, double candleWickZ, Random random) {
        if (random.nextInt(1) == 0) {
            Velocity velocity = getRandomVelocity(random, 0.001953125f);
            world.addParticle(
                    ParticleTypes.SMALL_FLAME,
                    candleWickX, candleWickY, candleWickZ,
                    velocity.x(), velocity.y(), velocity.z()
            );

            if (random.nextInt(3) == 0) {
                velocity = getRandomVelocity(random, 0.00390625f);
                world.addParticle(
                        ParticleTypes.SMOKE,
                        candleWickX, candleWickY, candleWickZ,
                        velocity.x(), velocity.y(), velocity.z()
                );
            }

            if (random.nextInt(2) == 0) {
                world.playSound(
                        candleWickX, candleWickY, candleWickZ,
                        SoundEvents.BLOCK_CANDLE_AMBIENT, SoundCategory.BLOCKS,
                        1.0f, 1.0f, true
                );
            }
        }
    }

    default Velocity getRandomVelocity(Random random, float velocityMultiplayer) {
        double velocityY = random.nextDouble() * velocityMultiplayer;
        double velocityX = (random.nextDouble() - 0.5) * velocityMultiplayer;
        double velocityZ = (random.nextDouble() - 0.5) * velocityMultiplayer;
        return new Velocity(velocityX, velocityY, velocityZ);
    }
    record Velocity(double x, double y, double z) {}

    default void damageItem(ItemStack heldItemStack, PlayerEntity player, World world, Hand hand) {
        if (!player.isCreative() && !world.isClient)
            heldItemStack.damage(
                    1,
                    (ServerWorld) world,
                    (ServerPlayerEntity) player,
                    (item) -> player.sendEquipmentBreakStatus(item, LivingEntity.getSlotForHand(hand))
            );
    }
}
