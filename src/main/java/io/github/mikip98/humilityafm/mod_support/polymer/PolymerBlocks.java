#if POLYMER
package io.github.mikip98.humilityafm.mod_support.polymer;

import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.core.api.block.SimplePolymerBlock;
#if POLYMER && MC_VERSION >= 260000 import net.fabricmc.fabric.api.networking.v1.context.PacketContext; #endif
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
#if POLYMER && MC_VERSION >= 12104 && MC_VERSION < 260000 import xyz.nucleoid.packettweaker.PacketContext; #endif

public class PolymerBlocks {
    // TODO: Consider merging those into one
    public static class PureTexturedBlock extends SimplePolymerBlock implements PolymerTexturedBlock {
        private final BlockState mappedState;

        public PureTexturedBlock(Properties properties, BlockState mappedState) {
            super(properties, mappedState.getBlock());
            this.mappedState = mappedState;
        }

        @Override
        public BlockState getPolymerBlockState(BlockState state #if MC_VERSION >= 12104, PacketContext context #endif) {
            return this.mappedState;
        }
    }

    public static class FallbackTexturedBlock extends Block implements PolymerBlock, EntityBlock {
        private final Block disguise;

        public FallbackTexturedBlock(Properties properties, Block disguise) {
            super(properties);
            this.disguise = disguise;
        }

        #if MC_VERSION < 12006
        @Override
        public Block getPolymerBlock(BlockState state) {
            return this.disguise;
        }
        #endif

        @Override
        public BlockState getPolymerBlockState(BlockState state #if MC_VERSION >= 12104, PacketContext context #endif) {
            return this.disguise.defaultBlockState();
        }

        @Nullable
        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new PolymerBlockEntities.FallbackBlockEntity(pos, state);
        }
    }
}
#endif