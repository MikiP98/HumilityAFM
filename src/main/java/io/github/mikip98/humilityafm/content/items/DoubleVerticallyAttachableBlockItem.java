package io.github.mikip98.humilityafm.content.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DoubleVerticallyAttachableBlockItem extends BlockItem {
    protected final Block wallBlock;

    public DoubleVerticallyAttachableBlockItem(@NotNull Block standingBlock, @NotNull Block wallBlock, Settings settings) {
        super(standingBlock, settings);
        this.wallBlock = wallBlock;
    }

    @Nullable
    protected BlockState getPlacementState(ItemPlacementContext context) {
        if (context.getPlayerLookDirection() == Direction.UP || context.getPlayerLookDirection() == Direction.DOWN) {
            return getBlock().getPlacementState(context);
        } else {
            return wallBlock.getPlacementState(context);
        }
    }

    @Override
    public void appendBlocks(Map<Block, Item> map, Item item) {
        super.appendBlocks(map, item);
        map.put(this.wallBlock, item);
    }
}
