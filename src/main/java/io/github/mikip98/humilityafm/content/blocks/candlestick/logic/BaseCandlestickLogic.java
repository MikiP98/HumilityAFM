package io.github.mikip98.humilityafm.content.blocks.candlestick.logic;

import io.github.mikip98.humilityafm.content.properties.ModProperties;
import io.github.mikip98.humilityafm.content.properties.enums.CandleColor;
import io.github.mikip98.humilityafm.util.SoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public sealed interface BaseCandlestickLogic permits SimpleCandlestickLogic, RustableCandlestickLogic {
    default boolean tryToInsertCandle(BlockState state, Level world, BlockPos pos, Player player, ItemStack heldItemStack, Item heldItem) {
        if (heldItem instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof CandleBlock) {
                // Check if the candlestick is already holding a candle
                final CandleColor candleColor = state.getValue(ModProperties.CANDLE_COLOR);
                if (candleColor != CandleColor.NONE) {
                    final Item candleItem = candleColor.asCandle();
                    if (heldItem == candleItem) return false;
                    player.getInventory().placeItemBackInInventory(new ItemStack(candleItem));
                }
                world.setBlockAndUpdate(pos, state.setValue(ModProperties.CANDLE_COLOR, CandleColor.getColor(heldItem)));
                if (!player.isCreative()) heldItemStack.shrink(1);
                SoundUtils.playSoundAtBlockCenter(world, player, pos, SoundEvents.STONE_BUTTON_CLICK_ON, 0.9f, 1.1f);
                return true;
            }
        }
        return false;
    }
    default boolean tryToExtinguishOrRemove(BlockState state, Level world, BlockPos pos, Player player, ItemStack heldItemStack) {
        if (heldItemStack.isEmpty() && player.isSneaking()) {
            // Extinguish the candle
            if (state.getValue(BlockStateProperties.LIT)) {
                world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LIT, false));
                world.setBlockAndUpdate(pos, state.with());
                SoundUtils.playSoundAtBlockCenter(world, player, pos, SoundEvents.CANDLE_EXTINGUISH);
                return true;
            }
            // Remove the candle
            else if (state.getValue(ModProperties.CANDLE_COLOR) != CandleColor.NONE) {
                player.getInventory().placeItemBackInInventory(new ItemStack(state.get(ModProperties.CANDLE_COLOR).asCandle()));
                world.setBlockAndUpdate(pos, state.with(ModProperties.CANDLE_COLOR, CandleColor.NONE));
                SoundUtils.playSoundAtBlockCenter(world, player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, 0.9f, 0.9f);
                return true;
            }
        }
        return false;
    }
    default boolean tryToLightTheCandle(BlockState state, Level world, BlockPos pos, Player player, Hand hand, ItemStack heldItemStack, Item heldItem) {
        if (
                heldItem instanceof FlintAndSteelItem
                        && state.getValue(ModProperties.CANDLE_COLOR) != CandleColor.NONE
                        && !state.getValue(BlockStateProperties.LIT)
                        && !state.getValue(BlockStateProperties.WATERLOGGED)
        ) {
            world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LIT, true));
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
