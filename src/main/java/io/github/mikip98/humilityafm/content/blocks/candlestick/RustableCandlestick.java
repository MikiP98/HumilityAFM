package io.github.mikip98.humilityafm.content.blocks.candlestick;

import io.github.mikip98.humilityafm.content.properties.ModProperties;
import io.github.mikip98.humilityafm.content.blocks.candlestick.logic.RustableCandlestickLogic;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class RustableCandlestick extends Candlestick implements RustableCandlestickLogic {
    public static final FabricBlockSettings defaultSettings = FabricBlockSettings.copyOf(Candlestick.defaultSettings)
            .sounds(BlockSoundGroup.COPPER)
            .ticksRandomly();

    protected @Nullable BlockState rustPreviousLevel;
    protected @Nullable BlockState rustNextLevel;

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ModProperties.WAXED);
    }

    public RustableCandlestick() {
        this(defaultSettings);
    }
    public RustableCandlestick(Settings settings) {
        this(settings, null, null);
    }
    public RustableCandlestick(Settings settings, BlockState rustPreviousLevel, BlockState rustNextLevel) {
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
}
