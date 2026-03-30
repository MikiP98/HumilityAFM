package io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns;

import io.github.mikip98.humilityafm.content.blocks.templates.PlainHorizontalFacingBlock;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

public class JackOLantern extends PlainHorizontalFacingBlock {
    public static final Supplier<Properties> defaultSettingsSupplier = () -> Properties.copy(Blocks.JACK_O_LANTERN);
    public static final Properties defaultSettings = defaultSettingsSupplier.get();
    public JackOLantern(Properties settings) {
        super(settings);
    }
}
