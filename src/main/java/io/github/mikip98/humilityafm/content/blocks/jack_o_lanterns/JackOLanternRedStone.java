package io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
#if MC_VERSION >= 12104
import org.jetbrains.annotations.Nullable;
#endif

public class JackOLanternRedStone extends JackOLantern {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
    }

    // Luminance of Redstone Torch boosted by 1 as it was too dark
    public static final Properties defaultSettings = defaultSettingsSupplier.get().lightLevel((state) -> state.getValue(LIT) ? 7+1 : 0);

    public JackOLanternRedStone(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(LIT, true));
    }


    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx).setValue(LIT, !ctx.getLevel().hasNeighborSignal(ctx.getClickedPos()));
    }

    @SuppressWarnings("deprecation")
    @Override
    #if MC_VERSION < 12104 public #else protected #endif void neighborChanged(
            BlockState state, Level level, BlockPos pos,
            Block sourceBlock,
            #if MC_VERSION < 12104 BlockPos sourcePos #else @Nullable net.minecraft.world.level.redstone.Orientation orientation #endif,
            boolean isMoving
    ) {
        if (!level.isClientSide()) {
            boolean isLit = state.getValue(LIT);
            if (isLit == level.hasNeighborSignal(pos)) {
                if (isLit) {
                    level.setBlock(pos, state.cycle(LIT), 2);
                } else {
                    level.scheduleTick(pos, this, 4);
                }
            }
        }
        #if MC_VERSION < 12104
        super.neighborChanged(state, level, pos, sourceBlock, sourcePos, isMoving);
        #else
        super.neighborChanged(state, level, pos, sourceBlock, orientation, isMoving);
        #endif
    }

    @SuppressWarnings("deprecation")
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT) && !level.hasNeighborSignal(pos)) {
            level.setBlock(pos, state.cycle(LIT), 2);
        }
    }
}
