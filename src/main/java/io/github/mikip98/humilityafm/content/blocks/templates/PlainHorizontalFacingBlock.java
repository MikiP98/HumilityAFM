package io.github.mikip98.humilityafm.content.blocks.templates;

#if POLYMER import eu.pb4.polymer.core.api.block.PolymerBlock; #endif
#if POLYMER && MC_VERSION >= 260000 import net.fabricmc.fabric.api.networking.v1.context.PacketContext; #endif
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
#if POLYMER && MC_VERSION >= 12104 && MC_VERSION < 260000 import xyz.nucleoid.packettweaker.PacketContext; #endif

public abstract class PlainHorizontalFacingBlock extends HorizontalDirectionalBlock #if POLYMER implements PolymerBlock #endif {
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    public PlainHorizontalFacingBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.SOUTH));
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    #if POLYMER
    #if MC_VERSION < 12006
    @Override
    public Block getPolymerBlock(BlockState state) {
        return getPolymerBlockState(state).getBlock();
    }
    #endif
    #if MC_VERSION >= 12104
    public abstract BlockState getPolymerBlockState(BlockState state);
    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return getPolymerBlockState(state);
    }
    #endif
    #endif
}
