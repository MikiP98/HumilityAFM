package io.github.mikip98.humilityafm.util.mod_support;

import org.jetbrains.annotations.NotNull;

public enum SupportedMods {
    BETTER_NETHER("Better Nether", "betternether"),
    BETTER_END("Better End", "betterend"),
    BIOMES_O_PLENTY("Biomes o' Plenty", "biomesoplenty"),

    SHIMMER("Shimmer", "shimmer");


    public final String modName;
    public final String modId;

    SupportedMods(String modName, String modId) {
        this.modName = modName;
        this.modId = modId;
    }

    public static @NotNull SupportedMods fromModId(String modId) {
        for (SupportedMods mod : values()) {
            if (mod.modId.equals(modId)) {
                return mod;
            }
        }
        throw new IllegalArgumentException("No SupportedMods found with modId: " + modId);
    }
}
