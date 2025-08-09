package io.github.mikip98.humilityafm.util.data_types;

import java.text.MessageFormat;

public class Color {
    public String name;
    public short r;
    public short g;
    public short b;

    public final short defaultR;
    public final short defaultG;
    public final short defaultB;

    public Color(String name, short r, short g, short b) {
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
        this.defaultR = r;
        this.defaultG = g;
        this.defaultB = b;
    }
    public Color(String name, int r, int g, int b) {
        this(name, (short) r, (short) g, (short) b);
    }

    @Override
    public String toString() {
        return String.format("Color{name='%s', r=%s, g=%s, b=%s}", name, r, g, b);
    }
}
