package io.github.mikip98.humilityafm.content.properties;

import io.github.mikip98.humilityafm.content.properties.enums.CandleColor;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ModProperties {
    public static final BooleanProperty WAXED = BooleanProperty.create("waxed");
    public static final EnumProperty<CandleColor> CANDLE_COLOR = EnumProperty.create("candle_color", CandleColor.class);
}
