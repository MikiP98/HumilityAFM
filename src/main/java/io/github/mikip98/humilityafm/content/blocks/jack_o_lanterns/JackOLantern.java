package io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns;

import io.github.mikip98.humilityafm.content.blocks.templates.PlainHorizontalFacingBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;

public class JackOLantern extends PlainHorizontalFacingBlock {
    public static final Settings defaultSettings = FabricBlockSettings.copy(Blocks.JACK_O_LANTERN);
    public JackOLantern() { super(defaultSettings); }
    public JackOLantern(Settings settings) {
        super(settings);
    }
}
