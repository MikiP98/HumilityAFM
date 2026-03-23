package io.github.mikip98.humilityafm.content.blocks.candlestick.logic;

import io.github.mikip98.humilityafm.content.properties.ModProperties;
import io.github.mikip98.humilityafm.content.properties.enums.CandleColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
#if MC_VERSION >= 12105
import net.minecraft.server.world.ServerWorld;
#endif
import net.minecraft.util.Hand;
#if MC_VERSION < 12006
import net.minecraft.util.hit.BlockHitResult;
#endif
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public non-sealed interface SimpleCandlestickLogic extends BaseCandlestickLogic {
    #if MC_VERSION < 12006
    default boolean onUseLogic(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
    #else
    default boolean onUseLogic(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        Hand hand = player.getActiveHand();
    #endif
        ItemStack heldItemStack = player.getStackInHand(hand);
        Item heldItem = heldItemStack.getItem();
        if (tryToInsertCandle(state, world, pos, player, heldItemStack, heldItem)) return true;
        if (tryToExtinguishOrRemove(state, world, pos, player, heldItemStack)) return true;
        return tryToLightTheCandle(state, world, pos, player, hand, heldItemStack, heldItem);
    }

    #if MC_VERSION < 12105
    default void onStateReplacedLogic(BlockState state, World world, BlockPos pos, BlockState newState) {
        dropCandle(state, newState, world, pos);
    }
    #else
    default void onStateReplacedLogic(BlockState state, ServerWorld world, BlockPos pos) {
        // In 1.21.5+, the block is already replaced, before that is is yet to be replaced, so this cannot be merged
        BlockState newState = world.getBlockState(pos);
        dropCandle(state, newState, world, pos);
    }
    #endif

    default void dropCandle(BlockState oldState, BlockState newState, World world, BlockPos pos) {
        // If the block is replaced with a different block, drop the candle if present
        if (newState.getBlock() != oldState.getBlock() && oldState.get(ModProperties.CANDLE_COLOR) != CandleColor.NONE) {
            Block.dropStack(world, pos, new ItemStack(oldState.get(ModProperties.CANDLE_COLOR).asCandle()));
        }
    }
}