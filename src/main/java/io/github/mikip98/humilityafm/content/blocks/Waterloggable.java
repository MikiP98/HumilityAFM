package io.github.mikip98.humilityafm.content.blocks;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;

public interface Waterloggable extends SimpleWaterloggedBlock {
    default boolean isPlacedInWater(BlockPlaceContext ctx) {
        return ctx.getLevel().getFluidState(ctx.getClickedPos()).is(FluidTags.WATER);
    }
}
