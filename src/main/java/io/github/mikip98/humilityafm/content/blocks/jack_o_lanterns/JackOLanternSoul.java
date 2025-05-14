package io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class JackOLanternSoul extends JackOLantern {
    public JackOLanternSoul() {
        super(FabricBlockSettings.copyOf(defaultSettings).luminance(8));
    }
}
