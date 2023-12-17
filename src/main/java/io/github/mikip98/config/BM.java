package io.github.mikip98.config;

public @interface BM {
    // Multiply return value by 2
    record Multiplier(int value) {
        public short get() {
            return (short) (value * 2);
        }
    }
}
