package io.github.mikip98.humilityafm.content.blocks.candlestick;

import io.github.mikip98.humilityafm.content.properties.ModProperties;
import io.github.mikip98.humilityafm.content.blocks.candlestick.logic.RustableCandlestickLogic;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class FloorRustableCandlestick extends FloorCandlestick implements Waterloggable, RustableCandlestickLogic {
    protected @Nullable BlockState rustPreviousLevel;
    protected @Nullable BlockState rustNextLevel;

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ModProperties.WAXED);
    }

    public FloorRustableCandlestick() {
        this(RustableCandlestick.defaultSettings, null, null);
    }
    public FloorRustableCandlestick(Settings settings) {
        this(settings, null, null);
    }
    public FloorRustableCandlestick(Settings settings, @Nullable BlockState rustPreviousLevel, @Nullable BlockState rustNextLevel) {
        super(settings);
        this.rustPreviousLevel = rustPreviousLevel;
        this.rustNextLevel = rustNextLevel;
        setDefaultState(getDefaultState()
                .with(ModProperties.WAXED, false)
        );
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextDouble() >= 0.96) this.rust(state, world, pos);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return !state.get(ModProperties.WAXED) && rustNextLevel != null;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (onUseRustableLogic(state, world, pos, player, hand)) return ActionResult.SUCCESS;
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
