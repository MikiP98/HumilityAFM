package io.github.mikip98.humilityafm.content.blocks.candlestick.logic;

import io.github.mikip98.humilityafm.content.properties.ModProperties;
import io.github.mikip98.humilityafm.content.properties.enums.CandleColor;
import io.github.mikip98.humilityafm.util.wrappers.BlockStateWrapper;
import io.github.mikip98.humilityafm.util.wrappers.PropertiesWrapper;
import io.github.mikip98.humilityafm.util.wrappers.SoundUtils;
#if MC_VERSION >= 260000
import io.github.mikip98.humilityafm.util.wrappers.WorldWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
#else
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleBlock;
#if MC_VERSION >= 12006 && MC_VERSION < 12111
import net.minecraft.entity.LivingEntity;
#endif
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
#endif

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
                SoundUtils.playSoundAtBlockCenter(world, player, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, 0.9f, 1.1f);
                return true;
            }
        }
        return false;
    }
    default boolean tryToExtinguishOrRemove(BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack heldItemStack) {
        if (heldItemStack.isEmpty() && player.isSneaking()) {
            // Extinguish the candle
            if (BlockStateWrapper.get(state, PropertiesWrapper.LIT)) {
                WorldWrapper.setBlockState(world, pos, BlockStateWrapper.with(state, PropertiesWrapper.LIT, false), Block.NOTIFY_ALL);
                world.setBlockState(pos, state.with(), Block.NOTIFY_ALL);
                SoundUtils.playSoundAtBlockCenter(world, player, pos, SoundEvents.BLOCK_CANDLE_EXTINGUISH);
                return true;
            }
            // Remove the candle
            else if (BlockStateWrapper.get(state, ModProperties.CANDLE_COLOR) != CandleColor.NONE) {
                player.getInventory().offerOrDrop(new ItemStack(state.get(ModProperties.CANDLE_COLOR).asCandle()));
                WorldWrapper.setBlockState(world, pos, state.with(ModProperties.CANDLE_COLOR, CandleColor.NONE), Block.NOTIFY_ALL);
                SoundUtils.playSoundAtBlockCenter(world, player, pos, SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, 0.9f, 0.9f);
                return true;
            }
        }
        return false;
    }
    default boolean tryToLightTheCandle(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack heldItemStack, Item heldItem) {
        if (
                heldItem instanceof FlintAndSteelItem
                        && BlockStateWrapper.get(state, ModProperties.CANDLE_COLOR) != CandleColor.NONE
                        && !BlockStateWrapper.get(state, PropertiesWrapper.LIT)
                        && !BlockStateWrapper.get(state, PropertiesWrapper.WATERLOGGED)
        ) {
            world.setBlockState(pos, BlockStateWrapper.with(state, PropertiesWrapper.LIT, true), Block.NOTIFY_ALL);
            damageItem(heldItemStack, player, hand);
            SoundUtils.playSoundAtBlockCenter(world, player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE);
            return true;
        }
        return false;
    }

    default void performRandomDisplayTick(World world, double candleWickX, double candleWickY, double candleWickZ, Random random) {
        if (random.nextInt(1) == 0) {
            Velocity velocity = getRandomVelocity(random, 0.001953125f);
            #if MC_VERSION < 12105
            world.addParticle(
            #else
            world.addParticleClient(
            #endif
                    ParticleTypes.SMALL_FLAME,
                    candleWickX, candleWickY, candleWickZ,
                    velocity.x(), velocity.y(), velocity.z()
            );

            if (random.nextInt(3) == 0) {
                velocity = getRandomVelocity(random, 0.00390625f);
                #if MC_VERSION < 12105
                world.addParticle(
                #else
                world.addParticleClient(
                #endif
                        ParticleTypes.SMOKE,
                        candleWickX, candleWickY, candleWickZ,
                        velocity.x(), velocity.y(), velocity.z()
                );
            }

            if (random.nextInt(2) == 0) {
                SoundUtils.playSound(world, candleWickX, candleWickY, candleWickZ, SoundEvents.BLOCK_CANDLE_AMBIENT);
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

    default void damageItem(ItemStack heldItemStack, PlayerEntity player, Hand hand) {
        if (!player.isCreative()) {
            #if MC_VERSION < 12006
            heldItemStack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
            #elif MC_VERSION < 12111
            heldItemStack.damage(1, player, LivingEntity.getSlotForHand(hand));
            #else
            heldItemStack.damage(1, player, hand);
            #endif
        }
    }
}
