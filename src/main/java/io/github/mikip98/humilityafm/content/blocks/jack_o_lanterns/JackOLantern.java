package io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns;

import io.github.mikip98.humilityafm.content.blocks.templates.PlainHorizontalFacingBlock;
import net.minecraft.block.Blocks;

public class JackOLantern extends PlainHorizontalFacingBlock {
    public static final Settings defaultSettings = getDefaultSettings();
    public static Settings getDefaultSettings() {
        return Settings.copy(Blocks.JACK_O_LANTERN);
    }
    public JackOLantern(Settings settings) {
        super(settings);
    }
}
