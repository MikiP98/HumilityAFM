package io.github.mikip98.humilityafm.util.wrappers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PlayerWrapper {
    public static boolean isSneaking(Player player) {
        #if MC_VERSION < 260000
        return player.isSneaking();
        #else
        return player.isShiftKeyDown();
    }

    public static void offerOrDrop(Player player, ItemStack item) {
        offerOrDrop(player.getInventory(), item);
    }
    protected static void offerOrDrop(Inventory inventory, ItemStack item) {
        #if MC_VERSION < 260000
        inventory.offerOrDrop(item);
        #else
        inventory.placeItemBackInInventory(item);
        #endif
    }
}
