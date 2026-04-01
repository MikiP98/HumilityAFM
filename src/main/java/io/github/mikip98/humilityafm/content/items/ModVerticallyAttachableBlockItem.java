package io.github.mikip98.humilityafm.content.items;

import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ModVerticallyAttachableBlockItem extends BlockItem {
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
}
