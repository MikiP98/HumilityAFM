package io.github.mikip98.humilityafm.content.blocks.templates;

#if MC_VERSION >= 12004
import com.mojang.serialization.MapCodec;
#endif
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mikip98.humilityafm.util.wrappers.BlockStateWrapper;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;

public class PlainHorizontalFacingBlock extends HorizontalDirectionalBlock {
    #if MC_VERSION >= 12004
    protected static final MapCodec<PlainHorizontalFacingBlock> CODEC = createCodec(PlainHorizontalFacingBlock::new);

    @Override
    protected @NonNull MapCodec<? extends PlainHorizontalFacingBlock> codec() { return CODEC; }
    #endif

    @Override
    #if MC_VERSION < 260000
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
    #else
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    #endif
        builder.add(FACING);
    }


    public PlainHorizontalFacingBlock(Properties settings) {
        super(settings);
        updateDefaultState(Map.of(FACING, Direction.SOUTH));
    }

    protected <T extends Comparable<T>> void updateDefaultState(Map<Property<T>, T> map) {
        BlockState newDefaultState = defaultBlockState();
        for (Map.Entry<Property<T>, T> entry : map.entrySet()) {
            newDefaultState = BlockStateWrapper.with(newDefaultState, entry.getKey(), entry.getValue());
        }
        #if MC_VERSION < 260000
        setDefaultState(newDefaultState);
        #else
        registerDefaultState(newDefaultState);
        #endif
    }

    @Override
    #if MC_VERSION < 260000
    public @NotNull BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }
    #else
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }
    #endif

    #if MC_VERSION >= 260000
    protected static <B extends Block> MapCodec<B> createCodec(final Function<Properties, B> constructor) {
        return simpleCodec(constructor);
    }
    #endif
}
