package io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns;

import io.github.mikip98.humilityafm.content.blocks.templates.PlainHorizontalFacingBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;

public class JackOLantern extends PlainHorizontalFacingBlock {
    public static final AbstractBlock.Settings defaultSettings = getDefaultSettings();
    public static AbstractBlock.Settings getDefaultSettings() {
        return AbstractBlock.Settings.copy(Blocks.JACK_O_LANTERN);
    }
    public JackOLantern() { super(defaultSettings); }
    public JackOLantern(Settings settings) {
        super(settings);
    }
}
