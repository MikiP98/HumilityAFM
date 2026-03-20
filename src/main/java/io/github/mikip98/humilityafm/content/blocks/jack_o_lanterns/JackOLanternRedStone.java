package io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
#if MC_VERSION >= 12104
import net.minecraft.world.block.WireOrientation;
#endif
import org.jetbrains.annotations.NotNull;
#if MC_VERSION >= 12104
import org.jetbrains.annotations.Nullable;
#endif

public class JackOLanternRedStone extends JackOLantern {
    public static final BooleanProperty LIT = Properties.LIT;

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(LIT);
    }

    // Luminance of Redstone Torch boosted by 1 as it was too dark
    public static final Settings defaultSettings = defaultSettingsSupplier.get().luminance((state) -> state.get(LIT) ? 7+1 : 0);

    public JackOLanternRedStone(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(LIT, true));
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
        if (!world.isClient) {
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
