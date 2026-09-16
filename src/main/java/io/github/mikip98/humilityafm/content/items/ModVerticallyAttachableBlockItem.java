package io.github.mikip98.humilityafm.content.items;

#if POLYMER import eu.pb4.polymer.core.api.item.PolymerItem; #endif
#if POLYMER import io.github.mikip98.humilityafm.mod_support.polymer.PolymerItems; #endif
#if POLYMER import io.github.mikip98.humilityafm.mod_support.polymer.PolymerModelCache; #endif
import net.minecraft.core.Direction;
#if POLYMER import net.minecraft.server.level.ServerPlayer; #endif
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
#if POLYMER import net.minecraft.world.item.ItemStack; #endif
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ModVerticallyAttachableBlockItem extends BlockItem #if POLYMER implements PolymerItem #endif {
    protected final Block wallBlock;
    protected final Direction verticalAttachmentDirection;

    public ModVerticallyAttachableBlockItem(
            @NotNull Block standingBlock, @NotNull Block wallBlock, Properties settings, Direction verticalAttachmentDirection
    ) {
        super(standingBlock, settings);
        this.wallBlock = wallBlock;
        this.verticalAttachmentDirection = verticalAttachmentDirection;
    }

    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext context) {
        if (context.getNearestLookingDirection() == verticalAttachmentDirection) {
            return getBlock().getStateForPlacement(context);
        } else if (context.getNearestLookingDirection() == verticalAttachmentDirection.getOpposite()) {
            return null;
        } else {
            return wallBlock.getStateForPlacement(context);
        }
    }

    @Override
    public void registerBlocks(Map<Block, Item> map, Item item) {
        super.registerBlocks(map, item);
        map.put(this.wallBlock, item);
    }

    #if POLYMER
    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayer player) {
        return PolymerItems.VIRTUAL_ITEM_BASE;
    }

    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayer player) {
        return PolymerModelCache.POLYMER_ITEM_MODEL_CACHE.get(this).value();
    }
    #endif
}
