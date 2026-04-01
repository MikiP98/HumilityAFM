package io.github.mikip98.humilityafm.content.blocks.candlestick.logic;

import io.github.mikip98.humilityafm.content.properties.ModProperties;
import io.github.mikip98.humilityafm.content.properties.enums.CandleColor;
import io.github.mikip98.humilityafm.util.SoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
        if (heldItemStack.isEmpty() && player.isShiftKeyDown()) {
            // Extinguish the candle
            if (state.getValue(BlockStateProperties.LIT)) {
                world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LIT, false));
                SoundUtils.playSoundAtBlockCenter(world, player, pos, SoundEvents.CANDLE_EXTINGUISH);
                return true;
            }
            // Remove the candle
            else {
                final CandleColor candleColour = state.getValue(ModProperties.CANDLE_COLOR);
                if (candleColour != CandleColor.NONE) {
                    player.getInventory().placeItemBackInInventory(new ItemStack(candleColour.asCandle()));
                    world.setBlockAndUpdate(pos, state.setValue(ModProperties.CANDLE_COLOR, CandleColor.NONE));
                    SoundUtils.playSoundAtBlockCenter(world, player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, 0.9f, 0.9f);
                    return true;
                }
            }
        }
        return false;
    }
    default boolean tryToLightTheCandle(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, ItemStack heldItemStack, Item heldItem) {
        if (
                heldItem instanceof FlintAndSteelItem
                        && state.getValue(ModProperties.CANDLE_COLOR) != CandleColor.NONE
                        && !state.getValue(BlockStateProperties.LIT)
                        && !state.getValue(BlockStateProperties.WATERLOGGED)
        ) {
            world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LIT, true));
            damageItem(heldItemStack, player, hand);
            SoundUtils.playSoundAtBlockCenter(world, player, pos, SoundEvents.FLINTANDSTEEL_USE);
            return true;
        }
        return false;
    }

    default void performRandomDisplayTick(Level world, double candleWickX, double candleWickY, double candleWickZ, RandomSource random) {
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
                SoundUtils.playSound(world, candleWickX, candleWickY, candleWickZ, SoundEvents.CANDLE_AMBIENT);
            }
        }
    }

    default Velocity getRandomVelocity(RandomSource random, float velocityMultiplayer) {
        double velocityY = random.nextDouble() * velocityMultiplayer;
        double velocityX = (random.nextDouble() - 0.5) * velocityMultiplayer;
        double velocityZ = (random.nextDouble() - 0.5) * velocityMultiplayer;
        return new Velocity(velocityX, velocityY, velocityZ);
    }
    record Velocity(double x, double y, double z) {}

    default void damageItem(ItemStack heldItemStack, Player player, InteractionHand hand) {
        if (!player.isCreative()) {
            #if MC_VERSION < 12006
            heldItemStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
            #elif MC_VERSION < 12111
            EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            heldItemStack.hurtAndBreak(1, player, slot);
            #else
            heldItemStack.hurtAndBreak(1, player, hand);
            #endif
        }
    }
}
