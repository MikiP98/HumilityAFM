package io.github.mikip98.config;

import io.github.mikip98.helpers.Color;

import java.util.Dictionary;

public class ModConfig {
    public static boolean enableLEDs = false;
    public static boolean enableLEDsBrightening = true;
    public static boolean enableIlluminatedCabinetBlockBrightening = true;
    public static boolean forceCabinetBlockResourcePackCompatibility = false;
    public static short LEDColoredLightStrength = 170;
    public static short LEDColoredLightRadius = 8;

    public static int cabinetBlockBurnTime = 24;
    public static int cabinetBlockFireSpread = 9;
    public static float mosaicsAndTilesStrengthMultiplayer = 1.5f;

    public static Dictionary<String, Color> customColorReferences = null;
}
