package io.github.mikip98.humilityafm.content.blockentities.cabinetBlock;

#if MC_VERSION < 260000
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
#else
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
#endif

/**
 * A simple {@code Inventory} implementation with only default methods + an item list getter.

 * Originally by Juuz
 */
public interface ImplementedInventory extends #if MC_VERSION < 260000 Inventory #else Container #endif {

    /**
     * Retrieves the item list of this inventory.
     * Must return the same instance every time it's called.
     */
    #if MC_VERSION < 260000
    DefaultedList<ItemStack> getItems();
    #else
    NonNullList<ItemStack> getItems();
    #endif
    
//    /**
//     * Creates an inventory from the item list.
//     */
//    #if MC_VERSION < 260000
//    static ImplementedInventory of(DefaultedList<ItemStack> items) {
//        return () -> items;
//    }
//    #else
//    static ImplementedInventory of(NonNullList<ItemStack> items) {
//        return () -> items;
//    }
//    #endif
    
    /**
     * Creates a new inventory with the specified size.
     */
//    #if MC_VERSION < 260000
//    static ImplementedInventory ofSize(int size) {
//        return of(DefaultedList.ofSize(size, ItemStack.EMPTY));
//    }
//    #else
//    static ImplementedInventory ofSize(int size) {
//        return of(NonNullList.withSize(size, ItemStack.EMPTY));
//    }
//    #endif
    
    /**
     * Returns the inventory size.
     */
    @Override
    #if MC_VERSION >= 260000
    default int getContainerSize() {
        return getItems().size();
    }
    #endif
    default int size() {
        return getItems().size();
    }
    
    /**
     * Checks if the inventory is empty.
     * @return true if this inventory has only empty stacks, false otherwise.
     */
    @Override
    default boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            ItemStack stack = getStack(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Retrieves the item in the slot.
     */
    @Override
    #if MC_VERSION >= 260000
    default @NonNull ItemStack getItem(int slot) {
        return getItems().get(slot);
    }
    #endif
    default ItemStack getStack(int slot) {
        return getItems().get(slot);
    }
    
    /**
     * Removes items from an inventory slot.
     * @param slot  The slot to remove from.
     * @param count How many items to remove. If there are fewer items in the slot than what are requested,
     *              takes all items in that slot.
     */
    @Override
    #if MC_VERSION < 260000
    default ItemStack removeStack(int slot, int count) {
        ItemStack result = Inventories.splitStack(getItems(), slot, count);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }
    #else
    default @NonNull ItemStack removeItem(int slot, int count) {
        // Inventories.splitStack is ContainerHelper.removeItem
        ItemStack result = ContainerHelper.removeItem(getItems(), slot, count);
        if (!result.isEmpty()) {
            setChanged();
        }
        return result;
    }
    #endif
    
    /**
     * Removes all items from an inventory slot.
     * @param slot The slot to remove from.
     */
    #if MC_VERSION < 260000
    @Override
    default ItemStack removeStack(int slot) {
        return Inventories.removeStack(getItems(), slot);
    }
    #else
    default ItemStack removeItemNoUpdate(int slot) {
        // Inventories.removeStack is ContainerHelper.takeItem
        return ContainerHelper.takeItem(getItems(), slot);
    }
    #endif
    
    /**
     * Replaces the current stack in an inventory slot with the provided stack.
     * @param slot  The inventory slot of which to replace the itemstack.
     * @param stack The replacing itemstack. If the stack is too big for
     *              this inventory ({@link Inventory#getMaxCountPerStack()}),
     *              it gets resized to this inventory's maximum amount.
     */
    @Override
    #if MC_VERSION >= 260000
    default void setItem(int slot, @NonNull ItemStack stack) {
        getItems().set(slot, stack);
    }
    #endif
    default void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
    }
    
    /**
     * Clears the inventory.
     */
    @Override
    #if MC_VERSION >= 260000
    default void clearContent() {
        getItems().clear();
    }
    #endif
    default void clear() {
        getItems().clear();
    }
    
//    /**
//     * Marks the state as dirty.
//     * Must be called after changes in the inventory, so that the game can properly save
//     * the inventory contents and notify neighboring blocks of inventory changes.
//     */
//    @Override
//    default void markDirty() {
//        // Override if you want behavior.
//    }
    
    /**
     * @return true if the player can use the inventory, false otherwise.
     */
    @Override
    #if MC_VERSION < 260000
    default boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
    #else
    default boolean stillValid(@NonNull Player player) {
        return true;
    }
    #endif
}