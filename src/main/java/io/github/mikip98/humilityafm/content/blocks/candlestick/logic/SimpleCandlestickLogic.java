package io.github.mikip98.humilityafm.content.blocks.candlestick.logic;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public non-sealed interface SimpleCandlestickLogic extends BaseCandlestickLogic {
    default boolean onUseLogic(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack heldItemStack = player.getStackInHand(hand);
        Item heldItem = heldItemStack.getItem();
        if (tryToInsertCandle(state, world, pos, player, heldItemStack, heldItem)) return true;
        if (tryToExtinguishOrRemove(state, world, pos, player, heldItemStack)) return true;
        return tryToLightTheCandle(state, world, pos, player, hand, heldItemStack, heldItem);
    }
}