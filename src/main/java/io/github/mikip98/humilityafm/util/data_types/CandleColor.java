package io.github.mikip98.humilityafm.util.data_types;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.StringIdentifiable;

public enum CandleColor implements StringIdentifiable {
    PLAIN,
    // Vanilla colors
    WHITE,
    ORANGE,
    MAGENTA,
    LIGHT_BLUE,
    YELLOW,
    LIME,
    PINK,
    GRAY,
    LIGHT_GRAY,
    CYAN,
    PURPLE,
    BLUE,
    BROWN,
    GREEN,
    RED,
    BLACK;

    @Override
    public String asString() {
        return name().toLowerCase();
    }

    public Item asCandle() {
        return switch (this) {
            case PLAIN -> Items.CANDLE;
            case WHITE -> Items.WHITE_CANDLE;
            case ORANGE -> Items.ORANGE_CANDLE;
            case MAGENTA -> Items.MAGENTA_CANDLE;
            case LIGHT_BLUE -> Items.LIGHT_BLUE_CANDLE;
            case YELLOW -> Items.YELLOW_CANDLE;
            case LIME -> Items.LIME_CANDLE;
            case PINK -> Items.PINK_CANDLE;
            case GRAY -> Items.GRAY_CANDLE;
            case LIGHT_GRAY -> Items.LIGHT_GRAY_CANDLE;
            case CYAN -> Items.CYAN_CANDLE;
            case PURPLE -> Items.PURPLE_CANDLE;
            case BLUE -> Items.BLUE_CANDLE;
            case BROWN -> Items.BROWN_CANDLE;
            case GREEN -> Items.GREEN_CANDLE;
            case RED -> Items.RED_CANDLE;
            case BLACK -> Items.BLACK_CANDLE;
        };
    }

    public static String getName(CandleColor color) {
        return color.asString();
    }
    public static CandleColor getColor(String name) {
        try {
            return CandleColor.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    public static CandleColor getColor(Item item) {
        for (CandleColor color : CandleColor.values()) {
            if (item == color.asCandle()) {
                return color;
            }
        }
        return null;
    }
}
