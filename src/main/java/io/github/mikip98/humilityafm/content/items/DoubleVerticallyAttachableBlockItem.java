package io.github.mikip98.humilityafm.content.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class DoubleVerticallyAttachableBlockItem extends BlockItem {
    protected final Block standingBlock;

    public DoubleVerticallyAttachableBlockItem(Block standingBlock, Block wallBlock, Settings settings) {
        super(wallBlock, settings);
        this.standingBlock = standingBlock;
    }

    @Nullable
    protected BlockState getPlacementState(ItemPlacementContext context) {
        if (context.getPlayerLookDirection() == Direction.UP || context.getPlayerLookDirection() == Direction.DOWN) {
            return standingBlock.getPlacementState(context);
        } else {
            return getBlock().getPlacementState(context);
        }
    }
}
