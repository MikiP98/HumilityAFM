package io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class JackOLanternRedStone extends JackOLantern {
    public JackOLanternRedStone() {
        super(FabricBlockSettings.copyOf(defaultSettings).luminance(7));
    }
}
