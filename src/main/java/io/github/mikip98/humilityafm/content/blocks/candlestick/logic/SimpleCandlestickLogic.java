package io.github.mikip98.humilityafm.content.blocks.candlestick.logic;

import io.github.mikip98.humilityafm.content.properties.ModProperties;
import io.github.mikip98.humilityafm.content.properties.enums.CandleColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public non-sealed interface SimpleCandlestickLogic extends BaseCandlestickLogic {
    default boolean onUseLogic(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        Hand hand = player.getActiveHand();
        ItemStack heldItemStack = player.getStackInHand(hand);
        Item heldItem = heldItemStack.getItem();
        if (tryToInsertCandle(state, world, pos, player, heldItemStack, heldItem)) return true;
        if (tryToExtinguishOrRemove(state, world, pos, player, heldItemStack)) return true;
        return tryToLightTheCandle(state, world, pos, player, hand, heldItemStack, heldItem);
    }
    default void onStateReplacedLogic(BlockState state, World world, BlockPos pos, BlockState newState) {
        // If the block is replaced with a different block, drop the candle if present
        if (newState.getBlock() != state.getBlock() && state.get(ModProperties.CANDLE_COLOR) != CandleColor.NONE)
            Block.dropStack(world, pos, new ItemStack(state.get(ModProperties.CANDLE_COLOR).asCandle()));
    }
}