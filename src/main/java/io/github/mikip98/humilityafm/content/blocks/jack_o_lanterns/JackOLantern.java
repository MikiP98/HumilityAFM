package io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns;

import io.github.mikip98.humilityafm.content.blocks.templates.PlainHorizontalFacingBlock;
import net.minecraft.block.Blocks;

import java.util.function.Supplier;

public class JackOLantern extends PlainHorizontalFacingBlock {
    public static final Supplier<Settings> defaultSettingsSupplier = () -> Settings.copy(Blocks.JACK_O_LANTERN);
    public static final Settings defaultSettings = defaultSettingsSupplier.get();
    public JackOLantern() { super(defaultSettings); }
    public JackOLantern(Settings settings) {
        super(settings);
    }
}
