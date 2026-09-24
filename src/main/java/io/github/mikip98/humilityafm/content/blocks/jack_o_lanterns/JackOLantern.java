package io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns;

#if POLYMER import eu.pb4.polymer.blocks.api.PolymerTexturedBlock; #endif
import io.github.mikip98.humilityafm.content.blocks.templates.PlainHorizontalFacingBlock;
#if POLYMER import io.github.mikip98.humilityafm.mod_support.polymer.PolymerBlockEntities; #endif
#if POLYMER import io.github.mikip98.humilityafm.mod_support.polymer.PolymerModelCache; #endif
#if POLYMER import net.minecraft.core.BlockPos; #endif
import net.minecraft.world.level.block.Blocks;
#if POLYMER import net.minecraft.world.level.block.EntityBlock; #endif
#if POLYMER import net.minecraft.world.level.block.entity.BlockEntity; #endif
#if POLYMER import net.minecraft.world.level.block.state.BlockState; #endif
#if POLYMER import net.minecraft.world.level.block.state.properties.BlockStateProperties; #endif
#if POLYMER import org.jetbrains.annotations.Nullable; #endif

import java.util.function.Supplier;

public abstract class JackOLantern extends PlainHorizontalFacingBlock #if POLYMER implements PolymerTexturedBlock, EntityBlock #endif {
    public static final Supplier<Properties> defaultSettingsSupplier = () -> Properties.ofFullCopy(Blocks.JACK_O_LANTERN);
    public static final Properties defaultSettings = defaultSettingsSupplier.get();
    public JackOLantern(Properties settings) {
        super(settings);
    }

    #if POLYMER
    @Override
    public BlockState getPolymerBlockState(BlockState state) {
        BlockState mapped = PolymerModelCache.POLYMER_BLOCK_CACHE.get(state);
        if (mapped != null) return mapped;
        return Blocks.JACK_O_LANTERN.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(FACING));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (PolymerModelCache.POLYMER_BLOCK_CACHE.get(state) == null) {
            return new PolymerBlockEntities.FallbackBlockEntity(pos, state);
        }
        return null;
    }
    #endif
}
