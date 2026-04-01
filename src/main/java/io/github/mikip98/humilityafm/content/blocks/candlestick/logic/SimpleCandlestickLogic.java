package io.github.mikip98.humilityafm.content.blocks.candlestick.logic;

import io.github.mikip98.humilityafm.content.properties.ModProperties;
import io.github.mikip98.humilityafm.content.properties.enums.CandleColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public non-sealed interface SimpleCandlestickLogic extends BaseCandlestickLogic {
    #if MC_VERSION < 12006
    default boolean onUseLogic(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand) {
    #else
    default boolean onUseLogic(BlockState state, Level world, BlockPos pos, Player player) {
        Hand hand = player.getActiveHand();
    #endif
        ItemStack heldItemStack = player.getItemInHand(hand);
        Item heldItem = heldItemStack.getItem();
        if (tryToInsertCandle(state, world, pos, player, heldItemStack, heldItem)) return true;
        if (tryToExtinguishOrRemove(state, world, pos, player, heldItemStack)) return true;
        return tryToLightTheCandle(state, world, pos, player, hand, heldItemStack, heldItem);
    }

    #if MC_VERSION < 12105
    default void onStateReplacedLogic(BlockState state, Level world, BlockPos pos, BlockState newState) {
        dropCandle(state, newState, world, pos);
    }
    #else
    default void onStateReplacedLogic(BlockState state, ServerWorld world, BlockPos pos) {
        // In 1.21.5+, the block is already replaced, before that is is yet to be replaced, so this cannot be merged
        BlockState newState = world.getBlockState(pos);
        dropCandle(state, newState, world, pos);
    }
    #endif

    default void dropCandle(BlockState oldState, BlockState newState, Level world, BlockPos pos) {
        // If the block is replaced with a different block, drop the candle if present
        final CandleColor candleColor = oldState.getValue(ModProperties.CANDLE_COLOR);
        if (newState.getBlock() != oldState.getBlock() && candleColor != CandleColor.NONE) {
            Block.popResource(world, pos, new ItemStack(candleColor.asCandle()));
        }
    }
}