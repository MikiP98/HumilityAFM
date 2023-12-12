package io.github.mikip98.helpers;

public class Color {
    public String name;
    public short r;
    public short g;
    public short b;

    public Color(String name, short r, short g, short b) {
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
    }
    public Color(String name, int r, int g, int b) {
        this.name = name;
        this.r = (short) r;
        this.g = (short) g;
        this.b = (short) b;
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
