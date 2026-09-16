#if POLYMER
package io.github.mikip98.humilityafm.mod_support.polymer;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class PolymerItems {
    public static final Item VIRTUAL_ITEM_BASE = Items.GLOWSTONE_DUST;
    // TODO: Add a config for item like items or block like items or dynamic
    //  *Normal items have buggy block placement*

    public static class PolymerItemImpl extends SimplePolymerItem {
        public PolymerItemImpl(Properties properties) {
            super(properties, VIRTUAL_ITEM_BASE);
        }
        @Override
        public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayer player) {
            return PolymerModelCache.POLYMER_ITEM_MODEL_CACHE.get(this).value();
        }
    }

    public static class PolymerBlockItemImpl extends BlockItem implements PolymerItem {
        public PolymerBlockItemImpl(Block block, Properties properties) {
            super(block, properties);
        }
        @Override
        public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayer player) {
            return VIRTUAL_ITEM_BASE;
        }
        @Override
        public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayer player) {
            return PolymerModelCache.POLYMER_ITEM_MODEL_CACHE.get(this).value();
        }
    }
}
#endif