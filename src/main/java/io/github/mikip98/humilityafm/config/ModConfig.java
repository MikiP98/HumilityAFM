package io.github.mikip98.humilityafm.config;

import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Map;

public class ModConfig extends ModDefaultConfig {
    public static boolean transparentCabinetBlocks = defaultTransparentCabinetBlocks;
    public static boolean enableLightStripBrightening = defaultEnableLightStripBrightening;
    public static boolean enableLightStripRadiusColorCompensation = defaultEnableLightStripRadiusColorCompensation;
    @Setter
    private static boolean enableCandlestickBeta = defaultEnableCandlestickBeta;
    public static boolean getEnableCandlestickBeta() { return enableCandlestickBeta || datagenMode; }
    public static boolean getRawEnableCandlestickBeta() { return enableCandlestickBeta; }
    @Setter
    private static boolean enableColouredFeatureSetBeta = defaultEnableColouredFeatureSetBeta;
    public static boolean getEnableColouredFeatureSetBeta() { return enableColouredFeatureSetBeta || datagenMode; }
    public static boolean getRawEnableColouredFeatureSetBeta() { return enableColouredFeatureSetBeta; }

    public static float mosaicsAndTilesStrengthMultiplayer = defaultMosaicsAndTilesStrengthMultiplayer;
    public static int cabinetBlockBurnTime = defaultCabinetBlockBurnTime;
    public static int cabinetBlockFireSpread = defaultCabinetBlockFireSpread;

    public static boolean datagenMode = defaultDatagenMode;

    public static short lightStripColoredLightStrength = defaultLightStripColoredLightStrength;
    public static short lightStripColoredLightRadius = defaultLightStripColoredLightRadius;
    public static short lightStripRadiusColorCompensationBias = defaultLightStripRadiusColorCompensationBias;

    public static ArrayList<Color> lightStripColors = defaultLightStripColors;
    public static Map<String, Color> pumpkinColors = defaultPumpkinColors;

    public static Map<SupportedMods, ModSupport> modSupport = defaultModSupport;
}

