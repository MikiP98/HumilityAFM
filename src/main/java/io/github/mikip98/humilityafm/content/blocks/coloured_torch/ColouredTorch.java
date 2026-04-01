package io.github.mikip98.humilityafm.content.blocks.coloured_torch;

import io.github.mikip98.humilityafm.util.SoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class ColouredTorch extends TorchBlock {
    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public static final Properties defaultSettings =
            Properties.#if MC_VERSION < 12004 copy #else ofFullCopy #endif(Blocks.TORCH).lightLevel((state) -> state.getValue(POWER));


    public ColouredTorch(SimpleParticleType particleType, Properties properties) {
        #if MC_VERSION < 12004
        super(properties, particleType);
        #else
        super(particleType, properties);
        #endif
        registerDefaultState(defaultBlockState().setValue(POWER, 15));
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWER);
    }

    @Override
    #if MC_VERSION < 12006
    @SuppressWarnings("deprecation")
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
        final int currentPower = state.getValue(POWER);
        if (player.isShiftKeyDown() && player.getItemInHand(hand).isEmpty() && currentPower > 3) {
            SoundUtils.playSoundAtBlockCenter(level, player, pos, SoundEvents.CANDLE_EXTINGUISH, 1.2f, 0.75f);
            level.setBlockAndUpdate(pos, state.setValue(POWER, currentPower - 3));
            return true;
        }
        return false;
    }
}
