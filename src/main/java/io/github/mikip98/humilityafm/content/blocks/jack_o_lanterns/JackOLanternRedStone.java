package io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns;

#if MC_VERSION >= 12003 && MC_VERSION < 260300 import com.mojang.serialization.MapCodec; #endif
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
#if POLYMER import net.minecraft.world.level.block.Blocks; #endif
#if POLYMER import net.minecraft.world.level.block.entity.BlockEntity; #endif
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
#if MC_VERSION >= 12104 import net.minecraft.world.level.redstone.Orientation; #endif
import org.jetbrains.annotations.NotNull;
#if MC_VERSION >= 12104 || POLYMER import org.jetbrains.annotations.Nullable; #endif

public class JackOLanternRedStone extends JackOLantern {
    #if MC_VERSION >= 12003 && MC_VERSION < 260300
    protected static final MapCodec<JackOLanternRedStone> CODEC = simpleCodec(JackOLanternRedStone::new);

    @Override
    protected @NotNull MapCodec<? extends JackOLanternRedStone> codec() {
        return CODEC;
    }
    #endif

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
    }

    // Luminance of Redstone Torch boosted by 1 as it was too dark
    public static final Properties defaultSettings = defaultSettingsSupplier.get()
            .lightLevel((state) -> state.getValue(LIT) ? 7+1 : 0);

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
            #if MC_VERSION < 12104 BlockPos sourcePos #else @Nullable Orientation orientation #endif,
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

    #if POLYMER
    #if MC_VERSION < 12108  // Why is this not needed on 1.21.8+ ?
    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.is(newState.getBlock()) && state.getValue(LIT) && !newState.getValue(LIT)) {
            level.removeBlockEntity(pos);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
    #endif
    @Override
    public BlockState getPolymerBlockState(BlockState state) {
        return state.getValue(LIT) ? super.getPolymerBlockState(state) : Blocks.CARVED_PUMPKIN.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(FACING));
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(LIT) ? super.newBlockEntity(pos, state) : null;
    }
    #endif
}
