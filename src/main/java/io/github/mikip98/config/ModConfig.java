package io.github.mikip98.config;

import io.github.mikip98.helpers.Color;

import java.util.Hashtable;
import java.util.Map;

public class ModConfig {
    public static boolean TransparentCabinetBlocks = true;
    public static boolean enableLEDs = false;
    public static boolean enableLEDsBrightening = true;
    public static boolean enableLEDRadiusColorCompensation = true;
    public static boolean enableVanillaColoredLights = false;

    public static short LEDColoredLightStrength = 170;
    public static short LEDColoredLightRadius = 9;
    public static short LEDRadiusColorCompensationBias = 0;
    public static int cabinetBlockBurnTime = 24;
    public static int cabinetBlockFireSpread = 9;
    public static float mosaicsAndTilesStrengthMultiplayer = 1.5f;

    public static Map<String, Color> customColorReferences = new Hashtable<>();

    public static boolean shimmerDetected = false;
    public static boolean betterNetherDetected = false;
}

