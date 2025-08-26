package io.github.mikip98.humilityafm.util.data_types;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.StringIdentifiable;

import java.util.Map;
import java.util.NoSuchElementException;

public enum CandleColor implements StringIdentifiable {
    NONE,
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
            case NONE -> throw new IllegalStateException("No candle item for color NONE");
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

    public static CandleColor getColor(Item item) {
        if (item == Items.CANDLE) return PLAIN;
        else if (item == Items.WHITE_CANDLE) return WHITE;
        else if (item == Items.ORANGE_CANDLE) return ORANGE;
        else if (item == Items.MAGENTA_CANDLE) return MAGENTA;
        else if (item == Items.LIGHT_BLUE_CANDLE) return LIGHT_BLUE;
        else if (item == Items.YELLOW_CANDLE) return YELLOW;
        else if (item == Items.LIME_CANDLE) return LIME;
        else if (item == Items.PINK_CANDLE) return PINK;
        else if (item == Items.GRAY_CANDLE) return GRAY;
        else if (item == Items.LIGHT_GRAY_CANDLE) return LIGHT_GRAY;
        else if (item == Items.CYAN_CANDLE) return CYAN;
        else if (item == Items.PURPLE_CANDLE) return PURPLE;
        else if (item == Items.BLUE_CANDLE) return BLUE;
        else if (item == Items.BROWN_CANDLE) return BROWN;
        else if (item == Items.GREEN_CANDLE) return GREEN;
        else if (item == Items.RED_CANDLE) return RED;
        else if (item == Items.BLACK_CANDLE) return BLACK;
        else throw new IllegalArgumentException("No candle colour for item: " + item);
    }
    public static CandleColor getColor(String name) {
        try {
            return CandleColor.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No candle colour for name: " + name);
        }
    }
}
