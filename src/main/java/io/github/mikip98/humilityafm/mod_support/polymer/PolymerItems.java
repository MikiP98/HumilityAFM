#if POLYMER
package io.github.mikip98.humilityafm.mod_support.polymer;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
#if POLYMER && MC_VERSION >= 12104 && MC_VERSION < 12111 import net.minecraft.resources.ResourceLocation; #endif
#if POLYMER && MC_VERSION >= 260000 import net.fabricmc.fabric.api.networking.v1.context.PacketContext; #endif
#if POLYMER && MC_VERSION >= 260000 import net.minecraft.core.HolderLookup; #endif
#if POLYMER && MC_VERSION >= 12111 import net.minecraft.resources.Identifier; #endif
#if POLYMER && MC_VERSION < 12104 import net.minecraft.server.level.ServerPlayer; #endif
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
#if POLYMER && MC_VERSION < 12104 import org.jetbrains.annotations.Nullable; #endif
#if POLYMER && MC_VERSION >= 12104 && MC_VERSION < 260000 import xyz.nucleoid.packettweaker.PacketContext; #endif

public class PolymerItems {
    public static final Item VIRTUAL_ITEM_BASE = Items.GLOWSTONE_DUST;
    // TODO: Add a config for item like items or block like items or dynamic
    //  *Normal items have buggy block placement*

    public static class PolymerItemImpl extends SimplePolymerItem {
        public PolymerItemImpl(Properties properties) {
            super(properties, VIRTUAL_ITEM_BASE);
        }
        #if MC_VERSION < 12104
        @Override
        public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayer player) {
            return PolymerModelCache.POLYMER_ITEM_MODEL_CACHE.get(this).value();
        }
        #else
        @Override
        public #if MC_VERSION < 12111 ResourceLocation #else Identifier #endif getPolymerItemModel(ItemStack itemStack, PacketContext context #if MC_VERSION >= 260000, HolderLookup.Provider lookup #endif) {
            return PolymerModelCache.POLYMER_ITEM_MODEL_CACHE.get(this);
        }
        #endif
    }

    public static class PolymerBlockItemImpl extends BlockItem implements PolymerItem {
        public PolymerBlockItemImpl(Block block, Properties properties) {
            super(block, properties);
        }
        @Override
        public Item getPolymerItem(ItemStack itemStack, #if MC_VERSION < 12104 @Nullable ServerPlayer player #else PacketContext context #endif) {
            return PolymerItems.VIRTUAL_ITEM_BASE;
        }
        #if MC_VERSION < 12104
        @Override
        public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayer player) {
            return PolymerModelCache.POLYMER_ITEM_MODEL_CACHE.get(this).value();
        }
        #else
        @Override
        public #if MC_VERSION < 12111 ResourceLocation #else Identifier #endif getPolymerItemModel(ItemStack itemStack, PacketContext context #if MC_VERSION >= 260000, HolderLookup.Provider lookup #endif) {
            return PolymerModelCache.POLYMER_ITEM_MODEL_CACHE.get(this);
        }
        #endif
    }
}
#endif