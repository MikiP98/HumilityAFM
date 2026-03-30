package io.github.mikip98.humilityafm.content.blocks.templates;

#if MC_VERSION >= 12004
import com.mojang.serialization.MapCodec;
#endif
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

public class PlainHorizontalFacingBlock extends HorizontalDirectionalBlock {
    #if MC_VERSION >= 12004
    protected static final MapCodec<PlainHorizontalFacingBlock> CODEC = simpleCodec(PlainHorizontalFacingBlock::new);

    @Override
    protected @NonNull MapCodec<? extends PlainHorizontalFacingBlock> codec() { return CODEC; }
    #endif

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    public PlainHorizontalFacingBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.SOUTH));
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }
}
