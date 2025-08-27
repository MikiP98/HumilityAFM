package io.github.mikip98.humilityafm.content.properties;

import io.github.mikip98.humilityafm.content.properties.enums.CandleColor;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;

public class ModProperties {
    public static final BooleanProperty WAXED = BooleanProperty.of("waxed");
    public static final EnumProperty<CandleColor> CANDLE_COLOR = EnumProperty.of("candle_color", CandleColor.class);
}
