#if MC_VERSION >= 12104
package io.github.mikip98.humilityafm.util;

import com.mojang.serialization.MapCodec;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import static io.github.mikip98.humilityafm.HumilityAFM.LOGGER;

public abstract class SettingsDuplicator {
    public static AtomicLong count = new AtomicLong(0);
    public static Map<BlockBehaviour.Properties, Supplier<BlockBehaviour.Properties>> cache = new IdentityHashMap<>();

    public static BlockBehaviour.Properties copy(BlockBehaviour.Properties sharedSettings) {
        // Tricks vanilla into duplicating the raw settings via a dummy block
        if (cache.containsKey(sharedSettings)) {
            return cache.get(sharedSettings).get();
        }
        final String name = "dummy_" + count.getAndIncrement();
        LOGGER.info("New dummy block created: {}", name);
        final Block block = Blocks.register(BlockRegistry.keyOfBlock(name), DummyBlock::new, sharedSettings);
        cache.put(sharedSettings, () -> BlockBehaviour.Properties.ofFullCopy(block));
        return copy(sharedSettings);
    }

    protected static class DummyBlock extends Block {
        public DummyBlock(Properties settings) {
            super(settings);
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(BlockStateProperties.LIT);
            builder.add(BlockStateProperties.POWER);
        }

        @Override
        protected MapCodec<? extends Block> codec() { return null; }

        @Override
        public Item asItem() {
            return null;
        }

        @Override
        protected Block asBlock() {
            return null;
        }
    }
}
#endif