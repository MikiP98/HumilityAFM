package io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns;

import io.github.mikip98.humilityafm.util.SoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class ColouredJackOLantern extends JackOLantern {
    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public ColouredJackOLantern(Properties settings) {
        super(settings.lightLevel((state) -> state.getValue(POWER)));
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
        return onUseLogic(state, level, pos, player, hand) ? InteractionResult.SUCCESS : super.use(state, level, pos, player, hand, hit);
    }
    #else
    public @NonNull InteractionResult useWithoutItem(
            @NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, Player player, @NonNull BlockHitResult hit
    ) {
        final InteractionHand hand = player.getUsedItemHand();
        return onUseLogic(state, level, pos, player, hand) ? InteractionResult.SUCCESS : super.use(state, level, pos, player, hit);
    }
    #endif

    protected static boolean onUseLogic(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand) {
        final int currentPower = state.getValue(POWER);
        if (player.isShiftKeyDown() && player.getItemInHand(hand).isEmpty() && currentPower > 3) {
            SoundUtils.playSoundAtBlockCenter(world, player, pos, SoundEvents.PUMPKIN_CARVE);
            world.setBlockAndUpdate(pos, state.setValue(POWER, currentPower - 3));
            return true;
        }
        return false;
    }
}
