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

public class DoubleVerticallyAttachableBlockItem extends BlockItem {
    protected final Block wallBlock;

    public DoubleVerticallyAttachableBlockItem(@NotNull Block standingBlock, @NotNull Block wallBlock, Properties settings) {
        super(standingBlock, settings);
        this.wallBlock = wallBlock;
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        if (context.getNearestLookingDirection() == Direction.UP || context.getNearestLookingDirection() == Direction.DOWN) {
            return this.getBlock().getStateForPlacement(context);
        } else {
            return this.wallBlock.getStateForPlacement(context);
        }
    }

    @Override
    public void registerBlocks(Map<Block, Item> map, Item item) {
        super.registerBlocks(map, item);
        map.put(this.wallBlock, item);
    }
}
