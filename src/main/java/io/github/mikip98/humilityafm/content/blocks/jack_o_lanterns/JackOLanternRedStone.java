package io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
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
import org.jetbrains.annotations.NotNull;

public class JackOLanternRedStone extends JackOLantern {
    public static final BooleanProperty LIT = Properties.LIT;

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(LIT);
    }


    public JackOLanternRedStone() {
        // Luminance of Redstone Torch boosted by 1 as it was too dark
        super(FabricBlockSettings.copyOf(defaultSettings).luminance((state) -> state.get(LIT) ? 7+1 : 0));
        setDefaultState(getDefaultState().with(LIT, true));
    }


    @Override
    public @NotNull BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(LIT, !ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
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
    }

    @SuppressWarnings("deprecation")
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.get(LIT) && !world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, state.cycle(LIT), 2);
        }
    }
}
