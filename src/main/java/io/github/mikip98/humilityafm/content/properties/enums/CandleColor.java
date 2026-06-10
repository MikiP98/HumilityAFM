package io.github.mikip98.humilityafm.content.properties.enums;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public enum CandleColor implements StringRepresentable {
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
    public @NotNull String getSerializedName() {
        return name().toLowerCase();
    }

    public Item asCandle() {
        return switch (this) {
            case NONE -> throw new IllegalStateException("No candle item for color NONE");
            case PLAIN -> Items.CANDLE;
            #if MC_VERSION < 260200
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
            #else
            case WHITE -> Items.DYED_CANDLE.white();
            case ORANGE -> Items.DYED_CANDLE.orange();
            case MAGENTA -> Items.DYED_CANDLE.magenta();
            case LIGHT_BLUE -> Items.DYED_CANDLE.lightBlue();
            case YELLOW -> Items.DYED_CANDLE.yellow();
            case LIME -> Items.DYED_CANDLE.lime();
            case PINK -> Items.DYED_CANDLE.pink();
            case GRAY -> Items.DYED_CANDLE.gray();
            case LIGHT_GRAY -> Items.DYED_CANDLE.lightGray();
            case CYAN -> Items.DYED_CANDLE.cyan();
            case PURPLE -> Items.DYED_CANDLE.purple();
            case BLUE -> Items.DYED_CANDLE.blue();
            case BROWN -> Items.DYED_CANDLE.brown();
            case GREEN -> Items.DYED_CANDLE.green();
            case RED -> Items.DYED_CANDLE.red();
            case BLACK -> Items.DYED_CANDLE.black();
            #endif
        };
    }

    public static CandleColor getColor(Item item) {
        if (item == Items.CANDLE) return PLAIN;
        #if MC_VERSION < 260200
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
        #else
        else if (item == Items.DYED_CANDLE.white()) return WHITE;
        else if (item == Items.DYED_CANDLE.orange()) return ORANGE;
        else if (item == Items.DYED_CANDLE.magenta()) return MAGENTA;
        else if (item == Items.DYED_CANDLE.lightBlue()) return LIGHT_BLUE;
        else if (item == Items.DYED_CANDLE.yellow()) return YELLOW;
        else if (item == Items.DYED_CANDLE.lime()) return LIME;
        else if (item == Items.DYED_CANDLE.pink()) return PINK;
        else if (item == Items.DYED_CANDLE.gray()) return GRAY;
        else if (item == Items.DYED_CANDLE.lightGray()) return LIGHT_GRAY;
        else if (item == Items.DYED_CANDLE.cyan()) return CYAN;
        else if (item == Items.DYED_CANDLE.purple()) return PURPLE;
        else if (item == Items.DYED_CANDLE.blue()) return BLUE;
        else if (item == Items.DYED_CANDLE.brown()) return BROWN;
        else if (item == Items.DYED_CANDLE.green()) return GREEN;
        else if (item == Items.DYED_CANDLE.red()) return RED;
        else if (item == Items.DYED_CANDLE.black()) return BLACK;
        #endif
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
