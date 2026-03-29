#if MC_VERSION >= 12104
package io.github.mikip98.humilityafm.util;

import com.mojang.serialization.MapCodec;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
#if MC_VERSION >= 260000
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
#else
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
#endif

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import static io.github.mikip98.humilityafm.HumilityAFM.LOGGER;

public abstract class SettingsDuplicator {
    public static AtomicLong count = new AtomicLong(0);
    #if MC_VERSION < 260000
    public static Map<AbstractBlock.Settings, Supplier<AbstractBlock.Settings>> cache = new IdentityHashMap<>();
    #else
    public static Map<BlockBehaviour.Properties, Supplier<BlockBehaviour.Properties>> cache = new IdentityHashMap<>();
    #endif

    #if MC_VERSION < 260000
    public static AbstractBlock.Settings copy(AbstractBlock.Settings sharedSettings) {
    #else
    public static BlockBehaviour.Properties copy(BlockBehaviour.Properties sharedSettings) {
    #endif
        // Tricks vanilla into duplicating the raw settings via a dummy block
        if (cache.containsKey(sharedSettings)) {
            return cache.get(sharedSettings).get();
        }
        final String name = "dummy_" + count.getAndIncrement();
        LOGGER.info("New dummy block created: {}", name);
        final Block block = Blocks.register(BlockRegistry.keyOfBlock(name), DummyBlock::new, sharedSettings);
        #if MC_VERSION < 260000
        cache.put(sharedSettings, () -> AbstractBlock.Settings.copy(block));
        #else
        cache.put(sharedSettings, () -> BlockBehaviour.Properties.ofFullCopy(block));
        #endif
        return copy(sharedSettings);
    }

    protected static class DummyBlock extends Block {
        public DummyBlock(#if MC_VERSION < 260000 Settings #else Properties #endif settings) {
            super(settings);
        }

        @Override
        #if MC_VERSION < 260000
        protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
            builder.add(Properties.LIT);
            builder.add(Properties.POWER);
        }
        #else
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(BlockStateProperties.LIT);
            builder.add(BlockStateProperties.POWER);
        }
        #endif

        @Override
        protected MapCodec<? extends Block> #if MC_VERSION < 260000 getCodec() #else codec() #endif { return null; }

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