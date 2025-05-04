package io.github.mikip98.humilityafm.util.data_types;

public record Torch(String type, byte lightLevel) {
    public static Torch of(String type, byte lightLevel) {
        return new Torch(type, lightLevel);
    }
}
