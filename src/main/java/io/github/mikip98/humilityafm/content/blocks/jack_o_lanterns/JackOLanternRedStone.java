package io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

public class JackOLanternRedStone extends JackOLantern {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(LIT);
    }

    // Luminance of Redstone Torch boosted by 1 as it was too dark
    public static final Properties defaultSettings = defaultSettingsSupplier.get().luminance((state) -> state.get(LIT) ? 7+1 : 0);

    public JackOLanternRedStone(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(LIT, true));
    }


    @Override
    public @NotNull BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(LIT, !ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
    }

    @SuppressWarnings("deprecation")
    @Override
    #if MC_VERSION < 12104 public #else protected #endif void neighborUpdate(
            BlockState state, World world, BlockPos pos,
            Block sourceBlock,
            #if MC_VERSION < 12104 BlockPos sourcePos #else @Nullable WireOrientation wireOrientation #endif,
            boolean notify
    ) {
        if (!world.isClient()) {
            boolean isLit = state.get(LIT);
            if (isLit == world.isReceivingRedstonePower(pos)) {
                if (isLit) {
                    world.setBlockState(pos, state.cycle(LIT), 2);
                } else {
                    world.scheduleBlockTick(pos, this, 4);
                }
            }
        }
        #if MC_VERSION < 12104
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
        #else
        super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
        #endif
    }

    @SuppressWarnings("deprecation")
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.get(LIT) && !world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, state.cycle(LIT), 2);
        }
    }
}
