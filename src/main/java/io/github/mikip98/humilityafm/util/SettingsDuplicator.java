#if MC_VERSION >= 12104
package io.github.mikip98.humilityafm.util;

import com.mojang.serialization.MapCodec;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import static io.github.mikip98.humilityafm.HumilityAFM.LOGGER;

public abstract class SettingsDuplicator {
    public static AtomicLong count = new AtomicLong(0);
    public static Map<AbstractBlock.Settings, Supplier<AbstractBlock.Settings>> cache = new IdentityHashMap<>();

    public static AbstractBlock.Settings copy(AbstractBlock.Settings sharedSettings) {
        // Tricks vanilla into duplicating the raw settings via a dummy block
        if (cache.containsKey(sharedSettings)) {
            return cache.get(sharedSettings).get();
        }
        final String name = "dummy_" + count.getAndIncrement();
        LOGGER.info("New dummy block created: {}", name);
        final Block block = Blocks.register(BlockRegistry.keyOfBlock(name), DummyBlock::new, sharedSettings);
        cache.put(sharedSettings, () -> AbstractBlock.Settings.copy(block));
        return copy(sharedSettings);
    }

    protected static class DummyBlock extends Block {
        public DummyBlock(Settings settings) {
            super(settings);
        }

        @Override
        protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
            builder.add(Properties.LIT);
            builder.add(Properties.POWER);
        }

        @Override
        protected MapCodec<? extends Block> getCodec() { return null; }

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