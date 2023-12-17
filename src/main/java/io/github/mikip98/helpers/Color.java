package io.github.mikip98.helpers;

public class Color {
    public String name;
    public short r;
    public short g;
    public short b;

    private short defaultR;
    private short defaultG;
    private short defaultB;

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

    public short getDefaultR() {
        return defaultR;
    }
    public short getDefaultG() {
        return defaultG;
    }
    public short getDefaultB() {
        return defaultB;
    }

    @Override
    public String toString() {
        return "Color{" +
                "name='" + name + '\'' +
                ", r=" + r +
                ", g=" + g +
                ", b=" + b +
                '}';
    }
}
