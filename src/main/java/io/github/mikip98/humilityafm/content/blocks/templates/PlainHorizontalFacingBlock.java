package io.github.mikip98.humilityafm.content.blocks.templates;

#if MC_VERSION >= 12004
import com.mojang.serialization.MapCodec;
#endif
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

public class PlainHorizontalFacingBlock extends HorizontalFacingBlock {
    #if MC_VERSION >= 12004
    protected static final MapCodec<PlainHorizontalFacingBlock> CODEC = createCodec(PlainHorizontalFacingBlock::new);

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }
    #endif

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public PlainHorizontalFacingBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(FACING, Direction.SOUTH));
    }

    @Override
    public @NotNull BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }
}
