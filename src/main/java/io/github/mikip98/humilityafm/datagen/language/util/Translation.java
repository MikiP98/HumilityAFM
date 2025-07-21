package io.github.mikip98.humilityafm.datagen.language.util;

public class Translation {
    private String string;

    public Translation(String string) {
        this.string = string;
    }

    public Translation translate(String input, String output) {
        this.string = this.string.replace(input, output).replace(input.toLowerCase(), output.toLowerCase());
        return this;
    }
    public String getString() {
        return string;
    }
}
