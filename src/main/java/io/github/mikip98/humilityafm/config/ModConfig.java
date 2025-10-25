package io.github.mikip98.humilityafm.config;

import io.github.mikip98.humilityafm.config.enums.CreativeItemGroupCategorization;
import io.github.mikip98.humilityafm.config.enums.ModSupportState;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import lombok.Setter;

import java.util.Map;

public class ModConfig extends ModDefaultConfig {
    public static boolean transparentCabinetBlocks = defaultTransparentCabinetBlocks;
    public static boolean illuminatedCabinetBlockBrightening = defaultIlluminatedCabinetBlockBrightening;
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

    public static CreativeItemGroupCategorization creativeItemGroupCategorization = dCreativeItemGroupCategorization;
    public static boolean placeHumilityBlocksInVanillaCreativeCategories = dPlaceHumilityBlocksInVanillaCreativeCategories;

    public static float mosaicsAndTilesStrengthMultiplayer = defaultMosaicsAndTilesStrengthMultiplayer;
    public static int cabinetBlockBurnTime = defaultCabinetBlockBurnTime;
    public static int cabinetBlockFireSpread = defaultCabinetBlockFireSpread;

    public static boolean datagenMode = defaultDatagenMode;
    public static boolean printInChatServerClientMissmatch = defaultPrintInChatServerClientMissmatch;

    public static Map<SupportedMods, ModSupportState> modSupport = defaultModSupport;
}

