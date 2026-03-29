package io.github.mikip98.humilityafm.util.wrappers;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PlayerEntityWrapper {
    public static ItemStack getStackInHand(
            #if MC_VERSION < 260000 PlayerEntity #else Player player #endif,
            #if MC_VERSION < 260000 Hand #else InteractionHand hand
    ) {
        #if MC_VERSION < 260000
        return player.getStackInHand(hand);
        #else
        return player.getItemInHand(hand);
        #endif
    }
}
