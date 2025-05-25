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

public class ModVerticallyAttachableBlockItem extends BlockItem {
    protected final Block wallBlock;
    protected final Direction verticalAttachmentDirection;

    public ModVerticallyAttachableBlockItem(@NotNull Block standingBlock, @NotNull Block wallBlock, Item.Settings settings, Direction verticalAttachmentDirection) {
        super(standingBlock, settings);
        this.wallBlock = wallBlock;
        this.verticalAttachmentDirection = verticalAttachmentDirection;
    }

    @Nullable
    protected BlockState getPlacementState(ItemPlacementContext context) {
        if (context.getPlayerLookDirection() == verticalAttachmentDirection) {
            return getBlock().getPlacementState(context);
        } else if (context.getPlayerLookDirection() == verticalAttachmentDirection.getOpposite()) {
            return null;
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
