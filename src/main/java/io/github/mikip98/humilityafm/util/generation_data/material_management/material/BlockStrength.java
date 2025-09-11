package io.github.mikip98.humilityafm.util.generation_data.material_management.material;

public record BlockStrength(float hardness, float resistance) {
    public static BlockStrength of(float hardness, float resistance) { return new BlockStrength(hardness, resistance); }
    public static BlockStrength of(float hardness) { return new BlockStrength(hardness, hardness); }
}
