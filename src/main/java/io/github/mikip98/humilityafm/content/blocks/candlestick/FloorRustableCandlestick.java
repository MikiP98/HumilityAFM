package io.github.mikip98.humilityafm.content.blocks.candlestick;

import io.github.mikip98.humilityafm.content.blocks.Waterloggable;
import io.github.mikip98.humilityafm.content.properties.ModProperties;
import io.github.mikip98.humilityafm.content.blocks.candlestick.logic.RustableCandlestickLogic;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class FloorRustableCandlestick extends FloorCandlestick implements Waterloggable, RustableCandlestickLogic {
    protected @Nullable BlockState rustPreviousLevel;
    protected @Nullable BlockState rustNextLevel;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ModProperties.WAXED);
    }

    public FloorRustableCandlestick(Properties settings) {
        this(settings, null, null);
    }
    public FloorRustableCandlestick(Properties settings, @Nullable BlockState rustPreviousLevel, @Nullable BlockState rustNextLevel) {
        super(settings);
        this.rustPreviousLevel = rustPreviousLevel;
        this.rustNextLevel = rustNextLevel;
        registerDefaultState(defaultBlockState()
                .setValue(ModProperties.WAXED, false));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextDouble() >= 0.96) this.rust(state, level, pos);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return !state.getValue(ModProperties.WAXED) && rustNextLevel != null;
    }

    @Override
    #if MC_VERSION < 12006
    public @NotNull InteractionResult use(
            BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit
    ) {
        if (onUseLogicInternal(state, level, pos, player, hand)) return InteractionResult.SUCCESS;
        return super.use(state, level, pos, player, hand, hit);
    }
    #else
    public @NonNull InteractionResult useWithoutItem(
            @NonNull BlockState state, @NonNull Level world, @NonNull BlockPos pos, Player player, @NonNull BlockHitResult hit
    ) {
        final InteractionHand hand = player.getUsedItemHand();
        if (onUseLogicInternal(state, level, pos, player, hand)) return InteractionResult.SUCCESS;
        return super.use(state, level, pos, player, hit);
    }
    #endif

    protected boolean onUseLogicInternal(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
        final double x = pos.getX() + 0.5;
        final double y = pos.getY() + 0.35;
        final double z = pos.getZ() + 0.5;
        final double randomSpread = 0.625;
        return onUseRustableLogic(state, level, pos, player, hand, x, y, z, randomSpread);
    }

    @Override
    public BlockState getChangedBlockState(BlockState newBase, BlockState state) {
        return getChangedBlockStateUniversal(newBase, state);
    }
}
